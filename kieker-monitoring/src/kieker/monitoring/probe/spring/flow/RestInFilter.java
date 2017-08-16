/***************************************************************************
 * Copyright 2017 Kieker Project (http://kieker-monitoring.net)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ***************************************************************************/

package kieker.monitoring.probe.spring.flow;

import kieker.common.logging.Log;
import kieker.common.logging.LogFactory;
import kieker.common.record.controlflow.OperationExecutionRecord;
import kieker.monitoring.core.controller.IMonitoringController;
import kieker.monitoring.core.controller.MonitoringController;
import kieker.monitoring.core.registry.ControlFlowRegistry;
import kieker.monitoring.core.registry.SessionRegistry;
import kieker.monitoring.probe.IMonitoringProbe;
import kieker.monitoring.probe.servlet.SessionAndTraceRegistrationFilter;
import kieker.monitoring.timer.ITimeSource;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Allows to monitor incoming REST requests.
 *
 * @author Teerat Pitakrat, Thomas Duellmann
 *
 * @since 1.13
 */
public class RestInFilter extends OncePerRequestFilter implements Filter, IMonitoringProbe  {

    private static final String SESSION_ID_ASYNC_TRACE = "NOSESSION-ASYNCIN";

    private static final IMonitoringController MONITORING_CTRL = MonitoringController.getInstance();
    private static final SessionRegistry SESSION_REGISTRY = SessionRegistry.INSTANCE;
    private static final ControlFlowRegistry CF_REGISTRY = ControlFlowRegistry.INSTANCE;

    private static final ITimeSource TIMESOURCE = MONITORING_CTRL.getTimeSource();
    private static final String VM_NAME = MONITORING_CTRL.getHostname();

    private static final Log LOG = LogFactory.getLog(SessionAndTraceRegistrationFilter.class);

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {
        final String signature;
        final AtomicReference<String> sessionId = new AtomicReference<>(SESSION_REGISTRY.recallThreadLocalSessionId());
        final AtomicLong traceId = new AtomicLong(-1);
        long tin;
        int eoi;
        int ess;

        if (!MONITORING_CTRL.isMonitoringEnabled()) {
            filterChain.doFilter(httpServletRequest, httpServletResponse);
            return;
        }

        signature = "public void com.example.intercept.in.RestInInterceptor.interceptIncoming" + httpServletRequest.getMethod() + "Request()";

        final List<String> requestRestHeader = Collections.list(httpServletRequest.getHeaders(RestConstants.HEADER_FIELD));

        if (requestRestHeader.isEmpty()) {
            LOG.debug("No monitoring data found in the incoming request header");
            // LOG.info("Will continue without sending back reponse header");
            traceId.set(CF_REGISTRY.getAndStoreUniqueThreadLocalTraceId());
            CF_REGISTRY.storeThreadLocalEOI(0);
            CF_REGISTRY.storeThreadLocalESS(1); // next operation is ess + 1
            eoi = 0;
            ess = 0;
        } else {
            final String operationExecutionHeader = requestRestHeader.get(0);
            if (LOG.isDebugEnabled()) {
                LOG.info("Received request: " + httpServletRequest.getRequestURI() + "with header = " + requestRestHeader.toString());
            }
            final String[] headerArray = operationExecutionHeader.split(",");

            // Extract session id
            sessionId.set(headerArray[1]);
            if ("null".equals(sessionId.get())) {
                sessionId.set(OperationExecutionRecord.NO_SESSION_ID);
            }

            // Extract EOI
            final String eoiStr = headerArray[2];
            eoi = -1;
            try {
                eoi = 1 + Integer.parseInt(eoiStr);
            } catch (final NumberFormatException exc) {
                LOG.warn("Invalid eoi", exc);
            }

            // Extract ESS
            final String essStr = headerArray[3];
            ess = -1;
            try {
                ess = Integer.parseInt(essStr);
            } catch (final NumberFormatException exc) {
                LOG.warn("Invalid ess", exc);
            }

            // Extract trace id
            final String traceIdStr = headerArray[0];
            if (traceIdStr != null) {
                try {
                    traceId.set(Long.parseLong(traceIdStr));
                } catch (final NumberFormatException exc) {
                    LOG.warn("Invalid trace id", exc);
                }
            } else {
                traceId.set(CF_REGISTRY.getUniqueTraceId());
                sessionId.set(SESSION_ID_ASYNC_TRACE);
                eoi = 0; // EOI of this execution
                ess = 0; // ESS of this execution
            }

            // Store thread-local values
            CF_REGISTRY.storeThreadLocalTraceId(traceId.get());
            CF_REGISTRY.storeThreadLocalEOI(eoi); // this execution has EOI=eoi; next execution will get eoi with incrementAndRecall
            CF_REGISTRY.storeThreadLocalESS(ess + 1); // this execution has ESS=ess
            SESSION_REGISTRY.storeThreadLocalSessionId(sessionId.get());
        }

        tin = TIMESOURCE.getTime(); // the entry timestamp


        HttpServletResponseWrapper wrapper = new HttpServletResponseWrapper(httpServletResponse) {

            @Override
            public void setStatus(int sc) {
                super.setStatus(sc);
                handleStatus(sc);
            }

            @Override
            @SuppressWarnings("deprecation")
            public void setStatus(int sc, String sm) {
                super.setStatus(sc, sm);
                handleStatus(sc);
            }
            @Override
            public void sendError(int sc, String msg) throws IOException {
                super.sendError(sc, msg);
                handleStatus(sc);
            }
            @Override
            public void sendError(int sc) throws IOException {
                super.sendError(sc);
                handleStatus(sc);
            }
            private void handleStatus(int code) {
                addHeader(RestConstants.HEADER_FIELD, traceId + "," + sessionId + "," + CF_REGISTRY.recallThreadLocalEOI() + "," + Integer.toString(CF_REGISTRY.recallThreadLocalESS()));
            }
        };


        try {
            filterChain.doFilter(httpServletRequest, wrapper);
        } finally {
            SESSION_REGISTRY.unsetThreadLocalSessionId();

            final long tout = TIMESOURCE.getTime();

            // Log this execution
            MONITORING_CTRL.newMonitoringRecord(
                    new OperationExecutionRecord(signature, sessionId.get(), traceId.get(), tin, tout, VM_NAME, eoi, ess));

            // Reset the thread-local trace information
            CF_REGISTRY.unsetThreadLocalTraceId();
            CF_REGISTRY.unsetThreadLocalEOI();
            CF_REGISTRY.unsetThreadLocalESS();
        }
    }
}