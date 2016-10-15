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
 * Interceptor for outgoing HTTP requests in spring based on the Jersey interceptor.
 *
 * @author Teerat Pitakrat, Thomas F. Duellmann
 *
 * @since 1.13
 */
public class RestOutInterceptor implements ClientHttpRequestInterceptor {

	public static final String SIGNATURE = "public void " + RestOutInterceptor.class.getName()
			+ ".intercept(org.springframework.http.HttpRequest, byte[], org.springframework.http.client.ClientHttpRequestExecution)";

	private static final Log LOG = LogFactory.getLog(RestOutInterceptor.class);
	private static final IMonitoringController CTRLINST = MonitoringController.getInstance();
	private static final ITimeSource TIME = CTRLINST.getTimeSource();
	private static final String VMNAME = CTRLINST.getHostname();
	private static final ControlFlowRegistry CF_REGISTRY = ControlFlowRegistry.INSTANCE;
	private static final SessionRegistry SESSION_REGISTRY = SessionRegistry.INSTANCE;

	public RestOutInterceptor() {
		// empty constructor
	}

	@Override
	public ClientHttpResponse intercept(final HttpRequest request, final byte[] body, final ClientHttpRequestExecution execution) throws IOException {

		if (!CTRLINST.isMonitoringEnabled()) {
			return execution.execute(request, body);
		}
		boolean entrypoint = true;
		final String hostname = VMNAME;
		final String sessionId = SESSION_REGISTRY.recallThreadLocalSessionId();
		final int eoi; // this is executionOrderIndex-th execution in this trace
		final int ess; // this is the height in the dynamic call tree of this execution
		final int nextESS;
		long traceId = CF_REGISTRY.recallThreadLocalTraceId(); // traceId, -1 if entry point
		if (traceId == -1) {
			entrypoint = true;
			traceId = CF_REGISTRY.getAndStoreUniqueThreadLocalTraceId();
			CF_REGISTRY.storeThreadLocalEOI(0);
			CF_REGISTRY.storeThreadLocalESS(1); // next operation is ess + 1
			eoi = 0;
			ess = 0;
			nextESS = 1;
		} else {
			entrypoint = false;
			eoi = CF_REGISTRY.incrementAndRecallThreadLocalEOI();
			ess = CF_REGISTRY.recallAndIncrementThreadLocalESS();
			nextESS = ess + 1;
			if ((eoi == -1) || (ess == -1)) {
				LOG.error("eoi and/or ess have invalid values:" + " eoi == " + eoi + " ess == " + ess);
				CTRLINST.terminateMonitoring();
			}
		}

		// Get request header
		final HttpHeaders headers = request.getHeaders();

		headers.add("KiekerTracingInfo", Long.toString(traceId) + "," + sessionId + "," + Integer.toString(eoi) + "," + Integer.toString(nextESS));

		if (LOG.isDebugEnabled()) {
			LOG.debug("Sending request to " + request.getURI().toString() + " with header = " + headers.toString());
		}

		// measure before
		final long tin = TIME.getTime();
		// execution of the called method
		Object retval = null;
		try {
			retval = execution.execute(request, body);
		} finally {
			// measure after
			final long tout = TIME.getTime();

			// Process response
			if (retval instanceof ClientHttpResponse) {
				final ClientHttpResponse response = (ClientHttpResponse) retval;
				final HttpHeaders responseHeaders = response.getHeaders();
				if (responseHeaders != null) {
					final List<String> responseHeaderList = responseHeaders.get("KiekerTracingInfo");

					if (responseHeaderList != null) {
						if (LOG.isDebugEnabled()) {
							LOG.debug("Received response from " + responseHeaders.getLocation().toString() + " with header = " + responseHeaders.toString());
						}
						final String[] responseHeaderArray = responseHeaderList.get(0).split(",");

						// Extract trace id
						final String retTraceIdStr = responseHeaderArray[0];
						Long retTraceId = -1L;
						if (!"null".equals(retTraceIdStr)) {
							try {
								retTraceId = Long.parseLong(retTraceIdStr);
							} catch (final NumberFormatException exc) {
								LOG.warn("Invalid tradeId");
							}
						}
						if (traceId != retTraceId) {
							LOG.error("TraceId in response header (" + retTraceId + ") is different from that in request header (" + traceId + ")");
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
								CF_REGISTRY.storeThreadLocalEOI(retEOI);
							} catch (final NumberFormatException exc) {
								LOG.warn("Invalid eoi", exc);
							}
						}

					} else {
						if (LOG.isDebugEnabled()) {
							LOG.debug("No monitoring data found in the response header from " + responseHeaders.getLocation().toString() + ". Is it instrumented?");
						}
					}
				} else {
					if (LOG.isDebugEnabled()) {
						LOG.debug("Response header from " + response.getHeaders().getLocation().toString() + " is null. Is it instrumented?");
					}
				}
				response.close();
			}

			CTRLINST.newMonitoringRecord(new OperationExecutionRecord(RestOutInterceptor.SIGNATURE, sessionId, traceId, tin, tout, hostname, eoi, ess));
			// cleanup
			if (entrypoint) {
				CF_REGISTRY.unsetThreadLocalTraceId();
				CF_REGISTRY.unsetThreadLocalEOI();
				CF_REGISTRY.unsetThreadLocalESS();
				SESSION_REGISTRY.unsetThreadLocalSessionId();
			} else {
				CF_REGISTRY.storeThreadLocalESS(ess); // next operation is ess
			}
		}
		return (ClientHttpResponse) retval;
	}
}
