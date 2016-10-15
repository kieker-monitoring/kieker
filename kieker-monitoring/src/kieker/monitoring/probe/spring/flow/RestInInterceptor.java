/***************************************************************************
 * Copyright 2015 Kieker Project (http://kieker-monitoring.net)
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

import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.mvc.WebContentInterceptor;

import kieker.common.logging.Log;
import kieker.common.logging.LogFactory;
import kieker.common.record.controlflow.OperationExecutionRecord;
import kieker.monitoring.core.controller.IMonitoringController;
import kieker.monitoring.core.controller.MonitoringController;
import kieker.monitoring.core.registry.ControlFlowRegistry;
import kieker.monitoring.core.registry.SessionRegistry;
import kieker.monitoring.timer.ITimeSource;

/**
 * Interceptor for incoming HTTP requests in spring based on the Jersey interceptor.
 *
 * @author Teerat Pitakrat, Thomas F. Duellmann
 *
 * @since 1.13
 */
public class RestInInterceptor extends WebContentInterceptor {

	public static final String SESSION_ID_ASYNC_TRACE = "NOSESSION-ASYNCIN";

	private static final Log LOG = LogFactory.getLog(RestInInterceptor.class);

	private static final IMonitoringController CTRLINST = MonitoringController.getInstance();
	private static final ITimeSource TIME = CTRLINST.getTimeSource();
	private static final String VMNAME = CTRLINST.getHostname();
	private static final ControlFlowRegistry CF_REGISTRY = ControlFlowRegistry.INSTANCE;
	private static final SessionRegistry SESSION_REGISTRY = SessionRegistry.INSTANCE;

	private final ThreadLocal<ThreadSpecificInterceptedData> threadSpecificInterceptedData = new ThreadLocal<ThreadSpecificInterceptedData>();

	public RestInInterceptor() {
		// empty constructor
	}

	@Override
	public boolean preHandle(final HttpServletRequest request, final HttpServletResponse response, final Object handler) {

		if (!CTRLINST.isMonitoringEnabled()) {
			return true;
		}

		final String signature = request.getMethod();
		String sessionId = SESSION_REGISTRY.recallThreadLocalSessionId();
		long traceId = -1L;
		long tin;
		final String hostname = VMNAME;
		int eoi;
		int ess;

		final List<String> requestRestHeader = Collections.list(request.getHeaders(RestInterceptorConstants.HEADER_FIELD));

		if ((requestRestHeader == null) || (requestRestHeader.isEmpty())) {
			LOG.debug("No monitoring data found in the incoming request header");
			// LOG.info("Will continue without sending back reponse header");
			traceId = CF_REGISTRY.getAndStoreUniqueThreadLocalTraceId();
			CF_REGISTRY.storeThreadLocalEOI(0);
			CF_REGISTRY.storeThreadLocalESS(1); // next operation is ess + 1
			eoi = 0;
			ess = 0;
		} else {
			final String operationExecutionHeader = requestRestHeader.get(0);
			if (LOG.isDebugEnabled()) {
				LOG.debug("Received request: " + request.getRequestURI() + "with header = " + requestRestHeader.toString());
			}
			final String[] headerArray = operationExecutionHeader.split(",");

			// Extract session id
			sessionId = headerArray[1];
			if ("null".equals(sessionId)) {
				sessionId = OperationExecutionRecord.NO_SESSION_ID;
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
					traceId = Long.parseLong(traceIdStr);
				} catch (final NumberFormatException exc) {
					LOG.warn("Invalid trace id", exc);
				}
			} else {
				traceId = CF_REGISTRY.getUniqueTraceId();
				sessionId = SESSION_ID_ASYNC_TRACE;
				eoi = 0; // EOI of this execution
				ess = 0; // ESS of this execution
			}

			// Store thread-local values
			CF_REGISTRY.storeThreadLocalTraceId(traceId);
			CF_REGISTRY.storeThreadLocalEOI(eoi); // this execution has EOI=eoi; next execution will get eoi with incrementAndRecall
			CF_REGISTRY.storeThreadLocalESS(ess + 1); // this execution has ESS=ess
			SESSION_REGISTRY.storeThreadLocalSessionId(sessionId);
		}

		// measure before
		tin = TIME.getTime();

		this.threadSpecificInterceptedData.set(new ThreadSpecificInterceptedData(signature, sessionId, traceId, tin, hostname, eoi, ess));

		return true;
	}

	@Override
	public void afterCompletion(final HttpServletRequest request, final HttpServletResponse response, final Object handler, final Exception exception) {
		// measure after
		final long tout = TIME.getTime();

		final ThreadSpecificInterceptedData tsid = this.threadSpecificInterceptedData.get();
		CTRLINST.newMonitoringRecord(new OperationExecutionRecord(tsid.getSignature(), tsid.getSessionId(), tsid.getTraceId(), tsid.getTin(), tout,
				tsid.getHostname(), tsid.getEoi(), tsid.getEss()));
		// cleanup
		CF_REGISTRY.unsetThreadLocalTraceId();
		CF_REGISTRY.unsetThreadLocalEOI();
		CF_REGISTRY.unsetThreadLocalESS();
	}
}
