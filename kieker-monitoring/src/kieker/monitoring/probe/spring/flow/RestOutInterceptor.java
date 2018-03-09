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
import java.util.List;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;

import kieker.common.logging.Log;
import kieker.common.logging.LogFactory;
import kieker.common.record.controlflow.OperationExecutionRecord;
import kieker.monitoring.core.controller.IMonitoringController;
import kieker.monitoring.core.controller.MonitoringController;
import kieker.monitoring.core.registry.ControlFlowRegistry;
import kieker.monitoring.core.registry.SessionRegistry;
import kieker.monitoring.timer.ITimeSource;

/**
 * Allows to monitor outgoing REST requests.
 *
 * @author Teerat Pitakrat, Thomas Duellmann
 *
 * @since 1.13
 */
public class RestOutInterceptor implements ClientHttpRequestInterceptor {

	private static final String SIGNATURE = "kieker.monitoring.probe.spring.flow.RestOutInterceptor.interceptOutgoingRequest()";

	private static final Log LOG = LogFactory.getLog(RestOutInterceptor.class);
	private static final IMonitoringController CTRLINST = MonitoringController.getInstance();
	private static final ITimeSource TIME = RestOutInterceptor.CTRLINST.getTimeSource();
	private static final String VMNAME = RestOutInterceptor.CTRLINST.getHostname();
	private static final ControlFlowRegistry CF_REGISTRY = ControlFlowRegistry.INSTANCE;
	private static final SessionRegistry SESSION_REGISTRY = SessionRegistry.INSTANCE;

	/**
	 * Create a REST output interceptor.
	 */
	public RestOutInterceptor() {
		// empty constructor
	}

	@Override
	public ClientHttpResponse intercept(final HttpRequest request, final byte[] body,
			final ClientHttpRequestExecution execution) throws IOException {

		if (!RestOutInterceptor.CTRLINST.isMonitoringEnabled()) {
			return execution.execute(request, body);
		}
		final boolean entrypoint;
		final String sessionId = RestOutInterceptor.SESSION_REGISTRY.recallThreadLocalSessionId();
		final int eoi; // this is executionOrderIndex-th execution in this trace
		final int ess; // this is the height in the dynamic call tree of this execution
		final int nextESS;
		long traceId = RestOutInterceptor.CF_REGISTRY.recallThreadLocalTraceId(); // traceId, -1 if entry point
		if (traceId == -1) {
			entrypoint = true;
			traceId = RestOutInterceptor.CF_REGISTRY.getAndStoreUniqueThreadLocalTraceId();
			RestOutInterceptor.CF_REGISTRY.storeThreadLocalEOI(0);
			RestOutInterceptor.CF_REGISTRY.storeThreadLocalESS(1); // next operation is ess + 1
			eoi = 0;
			ess = 0;
			nextESS = 1;
		} else {
			entrypoint = false;
			eoi = RestOutInterceptor.CF_REGISTRY.incrementAndRecallThreadLocalEOI();
			ess = RestOutInterceptor.CF_REGISTRY.recallAndIncrementThreadLocalESS();
			nextESS = ess + 1;
			if ((eoi == -1) || (ess == -1)) {
				RestOutInterceptor.LOG
						.error("eoi and/or ess have invalid values:" + " eoi == " + eoi + " ess == " + ess);
				RestOutInterceptor.CTRLINST.terminateMonitoring();
			}
		}

		// Get request header
		final HttpHeaders headers = request.getHeaders();

		headers.add(RestConstants.HEADER_FIELD, Long.toString(traceId) + "," + sessionId + "," + Integer.toString(eoi)
				+ "," + Integer.toString(nextESS));

		if (RestOutInterceptor.LOG.isDebugEnabled()) {
			RestOutInterceptor.LOG.debug(
					"Sending request to " + request.getURI().toString() + " with header = " + headers.toString());
		}

		// measure before
		final long tin = RestOutInterceptor.TIME.getTime();
		// execution of the called method
		Object retval = null;
		try {
			retval = execution.execute(request, body);
		} finally {
			// measure after
			final long tout = RestOutInterceptor.TIME.getTime();

			// Process response
			if (retval instanceof ClientHttpResponse) {
				final ClientHttpResponse response = (ClientHttpResponse) retval;
				final HttpHeaders responseHeaders = response.getHeaders();
				if (responseHeaders != null) {
					final List<String> responseHeaderList = responseHeaders.get(RestConstants.HEADER_FIELD);

					if (responseHeaderList != null) {
						if (RestOutInterceptor.LOG.isDebugEnabled()) {
							RestOutInterceptor.LOG
									.debug("Received response from " + responseHeaders.getLocation().toString()
											+ " with header = " + responseHeaders.toString());
						}
						final String[] responseHeaderArray = responseHeaderList.get(0).split(",");

						// Extract trace id
						final String retTraceIdStr = responseHeaderArray[0];
						Long retTraceId = -1L;
						if (!"null".equals(retTraceIdStr)) {
							try {
								retTraceId = Long.parseLong(retTraceIdStr);
							} catch (final NumberFormatException exc) {
								RestOutInterceptor.LOG.warn("Invalid tradeId");
							}
						}
						if (traceId != retTraceId) {
							RestOutInterceptor.LOG.error("TraceId in response header (" + retTraceId
									+ ") is different from that in request header (" + traceId + ")");
						}

						// Extract session id
						String retSessionId = responseHeaderArray[1];
						if ("null".equals(retSessionId)) {
							retSessionId = OperationExecutionRecord.NO_SESSION_ID;
						}

						// Extract eoi
						int retEOI = -1;
						final String retEOIStr = responseHeaderArray[2];
						if (!"null".equals(retEOIStr)) {
							try {
								retEOI = Integer.parseInt(retEOIStr);
								RestOutInterceptor.CF_REGISTRY.storeThreadLocalEOI(retEOI);
							} catch (final NumberFormatException exc) {
								RestOutInterceptor.LOG.warn("Invalid eoi", exc);
							}
						}

					} else {
						if (RestOutInterceptor.LOG.isDebugEnabled()) {
							RestOutInterceptor.LOG.debug("No monitoring data found in the response header from "
									+ responseHeaders.getLocation().toString() + ". Is it instrumented?");
						}
					}
				} else {
					if (RestOutInterceptor.LOG.isDebugEnabled()) {
						RestOutInterceptor.LOG.debug("Response header from "
								+ response.getHeaders().getLocation().toString() + " is null. Is it instrumented?");
					}
				}
				response.close();
			}

			RestOutInterceptor.CTRLINST.newMonitoringRecord(new OperationExecutionRecord(RestOutInterceptor.SIGNATURE,
					sessionId, traceId, tin, tout, RestOutInterceptor.VMNAME, eoi, ess));
			// cleanup
			if (entrypoint) {
				RestOutInterceptor.CF_REGISTRY.unsetThreadLocalTraceId();
				RestOutInterceptor.CF_REGISTRY.unsetThreadLocalEOI();
				RestOutInterceptor.CF_REGISTRY.unsetThreadLocalESS();
				RestOutInterceptor.SESSION_REGISTRY.unsetThreadLocalSessionId();
			} else {
				RestOutInterceptor.CF_REGISTRY.storeThreadLocalESS(ess); // next operation is ess
			}
		}
		return (ClientHttpResponse) retval;
	}
}