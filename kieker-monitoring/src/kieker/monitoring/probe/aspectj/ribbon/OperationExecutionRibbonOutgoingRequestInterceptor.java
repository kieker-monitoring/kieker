/***************************************************************************
 * Copyright 2014 Kieker Project (http://kieker-monitoring.net)
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

package kieker.monitoring.probe.aspectj.ribbon;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.core.MultivaluedMap;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.DeclarePrecedence;

import com.netflix.niws.client.http.HttpClientResponse;

import kieker.common.logging.Log;
import kieker.common.logging.LogFactory;
import kieker.common.record.controlflow.OperationExecutionRecord;
import kieker.monitoring.core.controller.IMonitoringController;
import kieker.monitoring.core.controller.MonitoringController;
import kieker.monitoring.core.registry.ControlFlowRegistry;
import kieker.monitoring.core.registry.SessionRegistry;
import kieker.monitoring.probe.aspectj.AbstractAspectJProbe;

/**
 * @author Teerat Pitakrat
 *
 * @since 1.12
 */
@Aspect
@DeclarePrecedence("kieker.monitoring.probe.aspectj.operationExecution.*,kieker.monitoring.probe.aspectj.ribbon.*")
public class OperationExecutionRibbonOutgoingRequestInterceptor extends AbstractAspectJProbe {
	private static final Log LOG = LogFactory.getLog(OperationExecutionRibbonOutgoingRequestInterceptor.class);

	private static final IMonitoringController CTRLINST = MonitoringController.getInstance();
	// private static final ITimeSource TIME = CTRLINST.getTimeSource();
	// private static final String VMNAME = CTRLINST.getHostname();
	private static final ControlFlowRegistry CF_REGISTRY = ControlFlowRegistry.INSTANCE;
	private static final SessionRegistry SESSIONREGISTRY = SessionRegistry.INSTANCE;

	@Around("execution(private * com.netflix.niws.client.http.RestClient.execute(com.netflix.niws.client.http.HttpClientRequest$Verb, java.net.URI, javax.ws.rs.core.MultivaluedMap, javax.ws.rs.core.MultivaluedMap, com.netflix.client.config.IClientConfig, java.lang.Object))")
	public Object operation(final ProceedingJoinPoint thisJoinPoint) throws Throwable { // NOCS (Throwable)
		if (!CTRLINST.isMonitoringEnabled()) {
			return thisJoinPoint.proceed();
		}
		final String signature = this.signatureToLongString(thisJoinPoint.getSignature());
		if (!CTRLINST.isProbeActivated(signature)) {
			return thisJoinPoint.proceed();
		}
		// collect data
		final boolean entrypoint;
		// final String hostname = VMNAME;
		final String sessionId = SESSIONREGISTRY.recallThreadLocalSessionId();
		final int eoi; // this is executionOrderIndex-th execution in this trace
		final int ess; // this is the height in the dynamic call tree of this execution
		long traceId = CF_REGISTRY.recallThreadLocalTraceId(); // traceId, -1 if entry point
		if (traceId == -1) {
			entrypoint = true;
			traceId = CF_REGISTRY.getAndStoreUniqueThreadLocalTraceId();
			CF_REGISTRY.storeThreadLocalEOI(0);
			CF_REGISTRY.storeThreadLocalESS(1); // next operation is ess + 1
			eoi = 0;
			ess = 0;
		} else {
			entrypoint = false;
			// eoi = CF_REGISTRY.incrementAndRecallThreadLocalEOI(); // ess > 1
			// ess = CF_REGISTRY.recallAndIncrementThreadLocalESS(); // ess >= 0
			eoi = CF_REGISTRY.recallThreadLocalEOI();
			ess = CF_REGISTRY.recallThreadLocalESS();
			if ((eoi == -1) || (ess == -1)) {
				LOG.error("eoi and/or ess have invalid values:" + " eoi == " + eoi + " ess == " + ess);
				CTRLINST.terminateMonitoring();
			}
		}

		// Embed id, ess, eoi into outgoing request's header
		final Object[] args = thisJoinPoint.getArgs();

		if (args[2] == null) {
			// Put all values into one string with comma seperator.
			// This is a hack as only one value in one list can be passed to the server.
			final MultivaluedMap<String, String> requestHeader = new MultivaluedHashMap<String, String>();
			final List<String> requestHeaderList = new ArrayList<String>(4);
			requestHeaderList.add(Long.toString(traceId) + "," + sessionId + "," + Integer.toString(eoi) + "," + Integer.toString(ess));
			// LOG.info("");
			LOG.info("requestHeader = " + requestHeaderList);
			// LOG.info("");
			requestHeader.put(RibbonHeaderConstants.OPERATION_EXECUTION_HEADER, requestHeaderList);

			args[2] = requestHeader;
		}

		// measure before
		// final long tin = TIME.getTime();
		// execution of the called method
		final Object retval;
		try {
			retval = thisJoinPoint.proceed(args);

			// LOG.error("retval = " + retval.getClass().getName());
			if (retval instanceof HttpClientResponse) {
				final HttpClientResponse response = (HttpClientResponse) retval;
				final MultivaluedMap<String, String> responseHeader = response.getHeaders();
				if (responseHeader != null) {
					final List<String> responseHeaderList = responseHeader.get(RibbonHeaderConstants.OPERATION_EXECUTION_HEADER);
					if (responseHeaderList != null) {
						// LOG.info("");
						LOG.info("responseHeader = " + responseHeaderList.toString());
						// LOG.info("");
						final String[] responseArray = responseHeaderList.get(0).split(",");

						// Extract trace id
						final String retTraceIdStr = responseArray[0];
						if (retTraceIdStr != "null") {
							final Long retTraceId = Long.parseLong(retTraceIdStr);
						}
						// check trace id should be the same

						// Extract session id
						String retSessionId = responseArray[1];
						if (retSessionId == "null") {
							retSessionId = OperationExecutionRecord.NO_SESSION_ID;
						}

						// Extract eoi
						int retEOI = -1;
						final String retEOIStr = responseArray[2];
						if (!retEOIStr.equals("null")) {
							try {
								retEOI = Integer.parseInt(retEOIStr);
								CF_REGISTRY.storeThreadLocalEOI(retEOI);
							} catch (final NumberFormatException exc) {
								LOG.warn("Invalid eoi", exc);
							}
						}

					} else {
						LOG.warn("No monitoring data found in the response header");
						LOG.warn("Is middletier instrumented?");
						// CTRLINST.terminateMonitoring();
					}
				} else {
					LOG.warn("Response header is null");
					LOG.warn("Is middletier instrumented?");
					// CTRLINST.terminateMonitoring();
				}
			}
		} finally {
			// measure after
			// final long tout = TIME.getTime();
			// CTRLINST.newMonitoringRecord(new OperationExecutionRecord(signature, sessionId, traceId, tin, tout, hostname, eoi, ess));
			// cleanup
			if (entrypoint) {
				CF_REGISTRY.unsetThreadLocalTraceId();
				CF_REGISTRY.unsetThreadLocalEOI();
				CF_REGISTRY.unsetThreadLocalESS();
			} else {
				CF_REGISTRY.storeThreadLocalESS(ess); // next operation is ess
			}
		}
		return retval;
	}
}
