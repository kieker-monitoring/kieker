/***************************************************************************
 * Copyright 2022 Kieker Project (http://kieker-monitoring.net)
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

package kieker.monitoring.probe.aspectj.jersey;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.DeclarePrecedence;
import org.glassfish.jersey.client.ClientRequest;
import org.glassfish.jersey.client.ClientResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import kieker.common.record.controlflow.OperationExecutionRecord;
import kieker.monitoring.core.controller.IMonitoringController;
import kieker.monitoring.core.controller.MonitoringController;
import kieker.monitoring.core.registry.ControlFlowRegistry;
import kieker.monitoring.core.registry.SessionRegistry;
import kieker.monitoring.probe.aspectj.AbstractAspectJProbe;
import kieker.monitoring.timer.ITimeSource;

import jakarta.ws.rs.core.MultivaluedHashMap;
import jakarta.ws.rs.core.MultivaluedMap;

/**
 * @author Teerat Pitakrat
 *
 * @since 1.12
 */
@Aspect
@DeclarePrecedence("kieker.monitoring.probe.aspectj.operationExecution.*,kieker.monitoring.probe.aspectj.jersey.*")
public class OperationExecutionJerseyClientInterceptor extends AbstractAspectJProbe {
	public static final String SESSION_ID_ASYNC_TRACE = "NOSESSION-ASYNCIN";

	private static final Logger LOGGER = LoggerFactory.getLogger(OperationExecutionJerseyClientInterceptor.class);

	private static final IMonitoringController CTRLINST = MonitoringController.getInstance();
	private static final ITimeSource TIME = OperationExecutionJerseyClientInterceptor.CTRLINST.getTimeSource();
	private static final String VMNAME = OperationExecutionJerseyClientInterceptor.CTRLINST.getHostname();
	private static final ControlFlowRegistry CF_REGISTRY = ControlFlowRegistry.INSTANCE;
	private static final SessionRegistry SESSION_REGISTRY = SessionRegistry.INSTANCE;

	/**
	 * Default constructor.
	 */
	public OperationExecutionJerseyClientInterceptor() {
		// empty default constructor
	}

	/**
	 * Method to intercept outgoing request and incoming response.
	 *
	 * @return value of the intercepted method
	 */
	@Around("execution(public com.sun.jersey.api.client.ClientResponse com.sun.jersey.client.apache4.ApacheHttpClient4Handler.handle(com.sun.jersey.api.client.ClientRequest))")
	public Object operation(final ProceedingJoinPoint thisJoinPoint) throws Throwable { // NOCS (Throwable)
		if (!OperationExecutionJerseyClientInterceptor.CTRLINST.isMonitoringEnabled()) {
			return thisJoinPoint.proceed();
		}
		final String signature = this.signatureToLongString(thisJoinPoint.getSignature());
		if (!OperationExecutionJerseyClientInterceptor.CTRLINST.isProbeActivated(signature)) {
			return thisJoinPoint.proceed();
		}

		boolean entrypoint = true;
		final String hostname = OperationExecutionJerseyClientInterceptor.VMNAME;
		final String sessionId = OperationExecutionJerseyClientInterceptor.SESSION_REGISTRY
				.recallThreadLocalSessionId();
		final int eoi; // this is executionOrderIndex-th execution in this trace
		final int ess; // this is the height in the dynamic call tree of this execution
		final int nextESS;
		// traceId, -1 if entry point
		long traceId = OperationExecutionJerseyClientInterceptor.CF_REGISTRY.recallThreadLocalTraceId();
		if (traceId == -1) {
			entrypoint = true;
			traceId = OperationExecutionJerseyClientInterceptor.CF_REGISTRY.getAndStoreUniqueThreadLocalTraceId();
			OperationExecutionJerseyClientInterceptor.CF_REGISTRY.storeThreadLocalEOI(0);
			OperationExecutionJerseyClientInterceptor.CF_REGISTRY.storeThreadLocalESS(1); // next operation is ess + 1
			eoi = 0;
			ess = 0;
			nextESS = 1;
		} else {
			entrypoint = false;
			eoi = OperationExecutionJerseyClientInterceptor.CF_REGISTRY.incrementAndRecallThreadLocalEOI();
			ess = OperationExecutionJerseyClientInterceptor.CF_REGISTRY.recallAndIncrementThreadLocalESS();
			nextESS = ess + 1;
			if ((eoi == -1) || (ess == -1)) {
				OperationExecutionJerseyClientInterceptor.LOGGER
						.error("eoi and/or ess have invalid values: eoi == {} ess == {}", eoi, ess);
				OperationExecutionJerseyClientInterceptor.CTRLINST.terminateMonitoring();
			}
		}

		// Get request header
		final Object[] args = thisJoinPoint.getArgs();
		final ClientRequest request = (ClientRequest) args[0];
		final URI uri = request.getUri();
		// LOG.info("URI = " + uri.toString());

		// This is a hack to put all values in the header
		MultivaluedMap<String, Object> requestHeader = request.getHeaders();
		if (requestHeader == null) {
			requestHeader = new MultivaluedHashMap<>();
		}

		final List<Object> requestHeaderList = new ArrayList<>(4);
		requestHeaderList.add(Long.toString(traceId) + "," + sessionId + "," + Integer.toString(eoi) + ","
				+ Integer.toString(nextESS));
		requestHeader.put(JerseyHeaderConstants.OPERATION_EXECUTION_JERSEY_HEADER, requestHeaderList);
		OperationExecutionJerseyClientInterceptor.LOGGER.debug("Sending request to {} with header = {}", uri.toString(),
				requestHeader.toString());

		// measure before
		final long tin = OperationExecutionJerseyClientInterceptor.TIME.getTime();
		// execution of the called method
		Object retval = null;
		try {
			retval = thisJoinPoint.proceed(args);
		} finally {
			// measure after
			final long tout = OperationExecutionJerseyClientInterceptor.TIME.getTime();

			// Process response
			if (retval instanceof ClientResponse) {
				final ClientResponse response = (ClientResponse) retval;
				final MultivaluedMap<String, String> responseHeader = response.getHeaders();
				if (responseHeader != null) {
					final List<String> responseHeaderList = responseHeader
							.get(JerseyHeaderConstants.OPERATION_EXECUTION_JERSEY_HEADER);
					if (responseHeaderList != null) {
						OperationExecutionJerseyClientInterceptor.LOGGER.debug(
								"Received response from {} with header = {}", uri.toString(),
								responseHeader.toString());
						final String[] responseHeaderArray = responseHeaderList.get(0).split(",");

						// Extract trace id
						final String retTraceIdStr = responseHeaderArray[0];
						Long retTraceId = -1L;
						if (!"null".equals(retTraceIdStr)) {
							try {
								retTraceId = Long.parseLong(retTraceIdStr);
							} catch (final NumberFormatException exc) {
								OperationExecutionJerseyClientInterceptor.LOGGER.warn("Invalid tradeId");
							}
						}
						if (traceId != retTraceId) {
							OperationExecutionJerseyClientInterceptor.LOGGER.error(
									"TraceId in response header ({}) is different from that in request header ({})",
									retTraceId, traceId);
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
								OperationExecutionJerseyClientInterceptor.CF_REGISTRY.storeThreadLocalEOI(retEOI);
							} catch (final NumberFormatException exc) {
								OperationExecutionJerseyClientInterceptor.LOGGER.warn("Invalid eoi", exc);
							}
						}

					} else {
						OperationExecutionJerseyClientInterceptor.LOGGER.debug(
								"No monitoring data found in the response header from {}. Is it instrumented?",
								uri.toString());
					}
				} else {
					OperationExecutionJerseyClientInterceptor.LOGGER
							.debug("Response header from {} is null. Is it instrumented?", uri.toString());
				}
			}

			OperationExecutionJerseyClientInterceptor.CTRLINST.newMonitoringRecord(
					new OperationExecutionRecord(signature, sessionId, traceId, tin, tout, hostname, eoi, ess));
			// cleanup
			if (entrypoint) {
				OperationExecutionJerseyClientInterceptor.CF_REGISTRY.unsetThreadLocalTraceId();
				OperationExecutionJerseyClientInterceptor.CF_REGISTRY.unsetThreadLocalEOI();
				OperationExecutionJerseyClientInterceptor.CF_REGISTRY.unsetThreadLocalESS();
				OperationExecutionJerseyClientInterceptor.SESSION_REGISTRY.unsetThreadLocalSessionId();
			} else {
				OperationExecutionJerseyClientInterceptor.CF_REGISTRY.storeThreadLocalESS(ess); // next operation is ess
			}
		}
		return retval;
	}
}
