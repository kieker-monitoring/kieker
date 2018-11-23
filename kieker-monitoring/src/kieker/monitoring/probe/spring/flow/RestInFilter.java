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

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.filter.OncePerRequestFilter;

import kieker.common.record.controlflow.OperationExecutionRecord;
import kieker.monitoring.core.controller.IMonitoringController;
import kieker.monitoring.core.controller.MonitoringController;
import kieker.monitoring.core.registry.ControlFlowRegistry;
import kieker.monitoring.core.registry.SessionRegistry;
import kieker.monitoring.probe.IMonitoringProbe;
import kieker.monitoring.timer.ITimeSource;

/**
 * Allows to monitor incoming REST requests.
 *
 * @author Teerat Pitakrat, Thomas Duellmann
 *
 * @since 1.13
 */
public class RestInFilter extends OncePerRequestFilter implements IMonitoringProbe {

	private static final String SESSION_ID_ASYNC_TRACE = "NOSESSION-ASYNCIN";

	private static final IMonitoringController MONITORING_CTRL = MonitoringController.getInstance();
	private static final SessionRegistry SESSION_REGISTRY = SessionRegistry.INSTANCE;
	private static final ControlFlowRegistry CF_REGISTRY = ControlFlowRegistry.INSTANCE;

	private static final ITimeSource TIMESOURCE = MONITORING_CTRL.getTimeSource();
	private static final String VM_NAME = MONITORING_CTRL.getHostname();

	private static final Logger LOGGER = LoggerFactory.getLogger(RestInFilter.class);

	/** default constructor. */
	public RestInFilter() {
		// nothing to be done here
	}

	@Override
	protected final void doFilterInternal(final HttpServletRequest httpServletRequest,
			final HttpServletResponse httpServletResponse, final FilterChain filterChain)
			throws ServletException, IOException {
		if (!MONITORING_CTRL.isMonitoringEnabled()) {
			filterChain.doFilter(httpServletRequest, httpServletResponse);
			return;
		}

		final String signature = "public void com.example.intercept.in.RestInInterceptor.interceptIncoming"
				+ httpServletRequest.getMethod() + "Request()";

		@SuppressWarnings("unchecked")
		final List<String> requestRestHeader = Collections.list(httpServletRequest.getHeaders(RestConstants.HEADER_FIELD));
		final AtomicLong traceId = new AtomicLong(-1);
		final AtomicReference<String> sessionId = new AtomicReference<>(SESSION_REGISTRY.recallThreadLocalSessionId());
		int eoi;
		int ess;

		if (requestRestHeader.isEmpty()) {
			LOGGER.debug("No monitoring data found in the incoming request header");
			// LOG.info("Will continue without sending back reponse header");
			traceId.set(CF_REGISTRY.getAndStoreUniqueThreadLocalTraceId());
			CF_REGISTRY.storeThreadLocalEOI(0);
			CF_REGISTRY.storeThreadLocalESS(1); // next operation is ess + 1
			eoi = 0;
			ess = 0;
		} else {
			final String operationExecutionHeader = requestRestHeader.get(0);
			LOGGER.info("Received request: {} with header = {}", httpServletRequest.getRequestURI(), requestRestHeader.toString());
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
				LOGGER.warn("Invalid eoi", exc);
			}

			// Extract ESS
			final String essStr = headerArray[3];
			ess = -1;
			try {
				ess = Integer.parseInt(essStr);
			} catch (final NumberFormatException exc) {
				LOGGER.warn("Invalid ess", exc);
			}

			// Extract trace id
			final String traceIdStr = headerArray[0];
			if (traceIdStr != null) {
				try {
					traceId.set(Long.parseLong(traceIdStr));
				} catch (final NumberFormatException exc) {
					LOGGER.warn("Invalid trace id", exc);
				}
			} else {
				traceId.set(CF_REGISTRY.getUniqueTraceId());
				sessionId.set(SESSION_ID_ASYNC_TRACE);
				eoi = 0; // EOI of this execution
				ess = 0; // ESS of this execution
			}

			this.storeThreadLocalValues(traceId, eoi, ess, sessionId);
		}

		final long tin = TIMESOURCE.getTime(); // the entry timestamp

		final HttpServletResponseWrapper wrapper = new RestServletWrapper(httpServletResponse, traceId, sessionId, CF_REGISTRY);

		try {
			filterChain.doFilter(httpServletRequest, wrapper);
		} finally {
			SESSION_REGISTRY.unsetThreadLocalSessionId();

			final long tout = TIMESOURCE.getTime();

			// Log this execution
			MONITORING_CTRL.newMonitoringRecord(new OperationExecutionRecord(signature, sessionId.get(), traceId.get(),
					tin, tout, VM_NAME, eoi, ess));

			// Reset the thread-local trace information
			CF_REGISTRY.unsetThreadLocalTraceId();
			CF_REGISTRY.unsetThreadLocalEOI();
			CF_REGISTRY.unsetThreadLocalESS();
		}
	}

	private void storeThreadLocalValues(final AtomicLong traceId, final int eoi, final int ess, final AtomicReference<String> sessionId) {
		// Store thread-local values
		CF_REGISTRY.storeThreadLocalTraceId(traceId.get());
		CF_REGISTRY.storeThreadLocalEOI(eoi); // this execution has EOI=eoi; next execution will get eoi with
												// incrementAndRecall
		CF_REGISTRY.storeThreadLocalESS(ess + 1); // this execution has ESS=ess
		SESSION_REGISTRY.storeThreadLocalSessionId(sessionId.get());
	}

	/**
	 * Wrapper class.
	 *
	 * @author Reiner Jung
	 *
	 */
	private static class RestServletWrapper extends HttpServletResponseWrapper {

		private final AtomicLong traceId;
		private final AtomicReference<String> sessionId;
		private final ControlFlowRegistry cfRegistry;

		public RestServletWrapper(final HttpServletResponse response, final AtomicLong traceId, final AtomicReference<String> sessionId,
				final ControlFlowRegistry cfRegistry) {
			super(response);
			this.traceId = traceId;
			this.sessionId = sessionId;
			this.cfRegistry = cfRegistry;
		}

		@Override
		public void setStatus(final int sc) {
			super.setStatus(sc);
			this.handleStatus();
		}

		@Override
		public void setStatus(final int sc, final String sm) {
			super.setStatus(sc, sm);
			this.handleStatus();
		}

		@Override
		public void sendError(final int sc, final String msg) throws IOException {
			super.sendError(sc, msg);
			this.handleStatus();
		}

		@Override
		public void sendError(final int sc) throws IOException {
			super.sendError(sc);
			this.handleStatus();
		}

		private void handleStatus() {
			this.addHeader(RestConstants.HEADER_FIELD,
					this.traceId + "," + this.sessionId + "," + this.cfRegistry.recallThreadLocalEOI() + ","
							+ Integer.toString(this.cfRegistry.recallThreadLocalESS()));
		}
	};

}
