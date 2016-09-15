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

//import org.springframework.web.servlet.mvc.WebContentInterceptor;

public class RestInInterceptor {// extends WebContentInterceptor {
	//
	// public static final String SESSION_ID_ASYNC_TRACE = "NOSESSION-ASYNCIN";
	//
	// private static Log LOG = LogFactory.getLog(RestInInterceptor.class);
	//
	// private static final IMonitoringController CTRLINST = MonitoringController.getInstance();
	// private static final ITimeSource TIME = CTRLINST.getTimeSource();
	// private static final String VMNAME = CTRLINST.getHostname();
	// private static final ControlFlowRegistry CF_REGISTRY = ControlFlowRegistry.INSTANCE;
	// private static final SessionRegistry SESSION_REGISTRY = SessionRegistry.INSTANCE;
	//
	// @Override
	// public boolean preHandle(final HttpServletRequest request, final HttpServletResponse response, final Object handler) {
	// if (!CTRLINST.isMonitoringEnabled()) {
	// return thisJoinPoint.proceed();
	// }
	// final String signature = this.signatureToLongString(thisJoinPoint.getSignature());
	// if (!CTRLINST.isProbeActivated(signature)) {
	// return thisJoinPoint.proceed();
	// }
	//
	// boolean entrypoint = true;
	// final String hostname = VMNAME;
	// String sessionId = SESSION_REGISTRY.recallThreadLocalSessionId();
	// Long traceId = -1L;
	// int eoi; // this is executionOrderIndex-th execution in this trace
	// int ess; // this is the height in the dynamic call tree of this execution
	//
	// final Object[] args = thisJoinPoint.getArgs();
	// final ContainerRequest request = (ContainerRequest) args[1];
	//
	// final MultivaluedMap<String, String> requestHeader = request.getRequestHeaders();
	// final List<String> requestJerseyHeader = requestHeader.get(JerseyHeaderConstants.OPERATION_EXECUTION_JERSEY_HEADER);
	// if ((requestJerseyHeader == null) || (requestJerseyHeader.isEmpty())) {
	// LOG.debug("No monitoring data found in the incoming request header");
	// // LOG.info("Will continue without sending back reponse header");
	// traceId = CF_REGISTRY.getAndStoreUniqueThreadLocalTraceId();
	// CF_REGISTRY.storeThreadLocalEOI(0);
	// CF_REGISTRY.storeThreadLocalESS(1); // next operation is ess + 1
	// eoi = 0;
	// ess = 0;
	// } else {
	// final String operationExecutionHeader = requestJerseyHeader.get(0);
	// if (LOG.isDebugEnabled()) {
	// LOG.debug("Received request: " + request.getRequestUri() + "with header = " + requestHeader.toString());
	// }
	// final String[] headerArray = operationExecutionHeader.split(",");
	//
	// // Extract session id
	// sessionId = headerArray[1];
	// if ("null".equals(sessionId)) {
	// sessionId = OperationExecutionRecord.NO_SESSION_ID;
	// }
	//
	// // Extract EOI
	// final String eoiStr = headerArray[2];
	// eoi = -1;
	// try {
	// eoi = 1 + Integer.parseInt(eoiStr);
	// } catch (final NumberFormatException exc) {
	// LOG.warn("Invalid eoi", exc);
	// }
	//
	// // Extract ESS
	// final String essStr = headerArray[3];
	// ess = -1;
	// try {
	// ess = Integer.parseInt(essStr);
	// } catch (final NumberFormatException exc) {
	// LOG.warn("Invalid ess", exc);
	// }
	//
	// // Extract trace id
	// final String traceIdStr = headerArray[0];
	// if (traceIdStr != null) {
	// try {
	// traceId = Long.parseLong(traceIdStr);
	// } catch (final NumberFormatException exc) {
	// LOG.warn("Invalid trace id", exc);
	// }
	// } else {
	// traceId = CF_REGISTRY.getUniqueTraceId();
	// sessionId = SESSION_ID_ASYNC_TRACE;
	// entrypoint = true;
	// eoi = 0; // EOI of this execution
	// ess = 0; // ESS of this execution
	// }
	//
	// // Store thread-local values
	// CF_REGISTRY.storeThreadLocalTraceId(traceId);
	// CF_REGISTRY.storeThreadLocalEOI(eoi); // this execution has EOI=eoi; next execution will get eoi with incrementAndRecall
	// CF_REGISTRY.storeThreadLocalESS(ess + 1); // this execution has ESS=ess
	// SESSION_REGISTRY.storeThreadLocalSessionId(sessionId);
	// }
	//
	// // measure before
	// final long tin = TIME.getTime();
	// // execution of the called method
	// final Object retval;
	// try {
	// retval = thisJoinPoint.proceed();
	// } finally {
	// // measure after
	// final long tout = TIME.getTime();
	// CTRLINST.newMonitoringRecord(new OperationExecutionRecord(signature, sessionId, traceId, tin, tout, hostname, eoi, ess));
	// // cleanup
	// if (entrypoint) {
	// this.unsetKiekerThreadLocalData();
	// } else {
	// CF_REGISTRY.storeThreadLocalESS(ess); // next operation is ess
	// }
	// }
	// return retval;
	//
	// }
	//
	// @Override
	// public void afterCompletion(final HttpServletRequest request, final HttpServletResponse response, final Object handler, final Exception exception) {
	//
	// }
}
