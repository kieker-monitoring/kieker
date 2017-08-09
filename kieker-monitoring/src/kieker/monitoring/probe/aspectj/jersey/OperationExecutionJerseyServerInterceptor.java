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

package kieker.monitoring.probe.aspectj.jersey;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.core.MultivaluedMap;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.DeclarePrecedence;

import com.sun.jersey.spi.container.ContainerRequest;
import com.sun.jersey.spi.container.ContainerResponse;

import kieker.common.logging.Log;
import kieker.common.logging.LogFactory;
import kieker.common.record.controlflow.OperationExecutionRecord;
import kieker.monitoring.core.controller.IMonitoringController;
import kieker.monitoring.core.controller.MonitoringController;
import kieker.monitoring.core.registry.ControlFlowRegistry;
import kieker.monitoring.core.registry.SessionRegistry;
import kieker.monitoring.probe.aspectj.AbstractAspectJProbe;
import kieker.monitoring.timer.ITimeSource;

/**
 * @author Teerat Pitakrat
 *
 * @since 1.12
 */
@Aspect
@DeclarePrecedence("kieker.monitoring.probe.aspectj.jersey.*,kieker.monitoring.probe.aspectj.operationExecution.*")
public class OperationExecutionJerseyServerInterceptor extends AbstractAspectJProbe {
	public static final String SESSION_ID_ASYNC_TRACE = "NOSESSION-ASYNCIN";

	private static final Log LOG = LogFactory.getLog(OperationExecutionJerseyServerInterceptor.class);

	private static final IMonitoringController CTRLINST = MonitoringController.getInstance();
	private static final ITimeSource TIME = CTRLINST.getTimeSource();
	private static final String VMNAME = CTRLINST.getHostname();
	private static final ControlFlowRegistry CF_REGISTRY = ControlFlowRegistry.INSTANCE;
	private static final SessionRegistry SESSION_REGISTRY = SessionRegistry.INSTANCE;

	/**
	 * Default constructor.
	 */
	public OperationExecutionJerseyServerInterceptor() {
		// empty default constructor
	}

	/**
	 * Method to intercept incoming request.
	 *
	 * @return value of the intercepted method
	 */
	@Around("execution(private void com.sun.jersey.server.impl.application.WebApplicationImpl._handleRequest(com.sun.jersey.server.impl.application.WebApplicationContext, com.sun.jersey.spi.container.ContainerRequest, com.sun.jersey.spi.container.ContainerResponse))")
	public Object operationHandleRequest(final ProceedingJoinPoint thisJoinPoint) throws Throwable { // NOCS (Throwable)
		if (!CTRLINST.isMonitoringEnabled()) {
			return thisJoinPoint.proceed();
		}
		final String signature = this.signatureToLongString(thisJoinPoint.getSignature());
		if (!CTRLINST.isProbeActivated(signature)) {
			return thisJoinPoint.proceed();
		}

		boolean entrypoint = true;
		final String hostname = VMNAME;
		String sessionId = SESSION_REGISTRY.recallThreadLocalSessionId();
		Long traceId = -1L;
		int eoi; // this is executionOrderIndex-th execution in this trace
		int ess; // this is the height in the dynamic call tree of this execution

		final Object[] args = thisJoinPoint.getArgs();
		final ContainerRequest request = (ContainerRequest) args[1];

		final MultivaluedMap<String, String> requestHeader = request.getRequestHeaders();
		final List<String> requestJerseyHeader = requestHeader.get(JerseyHeaderConstants.OPERATION_EXECUTION_JERSEY_HEADER);
		if ((requestJerseyHeader == null) || (requestJerseyHeader.isEmpty())) {
			LOG.debug("No monitoring data found in the incoming request header");
			// LOG.info("Will continue without sending back reponse header");
			traceId = CF_REGISTRY.getAndStoreUniqueThreadLocalTraceId();
			CF_REGISTRY.storeThreadLocalEOI(0);
			CF_REGISTRY.storeThreadLocalESS(1); // next operation is ess + 1
			eoi = 0;
			ess = 0;
		} else {
			final String operationExecutionHeader = requestJerseyHeader.get(0);
			if (LOG.isDebugEnabled()) {
				LOG.debug("Received request: " + request.getRequestUri() + "with header = " + requestHeader.toString());
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
				entrypoint = true;
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
		final long tin = TIME.getTime();
		// execution of the called method
		final Object retval;
		try {
			retval = thisJoinPoint.proceed();
		} finally {
			// measure after
			final long tout = TIME.getTime();
			CTRLINST.newMonitoringRecord(new OperationExecutionRecord(signature, sessionId, traceId, tin, tout, hostname, eoi, ess));
			// cleanup
			if (entrypoint) {
				this.unsetKiekerThreadLocalData();
			} else {
				CF_REGISTRY.storeThreadLocalESS(ess); // next operation is ess
			}
		}
		return retval;
	}

	/**
	 * Method to intercept outgoing response.
	 *
	 * @return value of the intercepted method
	 */
	@Around("execution(public void com.sun.jersey.spi.container.ContainerResponse.write())")
	public Object operationWriteResponse(final ProceedingJoinPoint thisJoinPoint) throws Throwable { // NOCS (Throwable)
		if (!CTRLINST.isMonitoringEnabled()) {
			return thisJoinPoint.proceed();
		}
		final String signature = this.signatureToLongString(thisJoinPoint.getSignature());
		if (!CTRLINST.isProbeActivated(signature)) {
			return thisJoinPoint.proceed();
		}

		final long traceId = CF_REGISTRY.recallThreadLocalTraceId();

		if (traceId == -1) {
			// Kieker trace Id not registered. Should not happen, since this is a response message!
			LOG.warn("Kieker traceId not registered. Will unset all threadLocal variables and return.");
			return thisJoinPoint.proceed();
		}

		final String sessionId = SESSION_REGISTRY.recallThreadLocalSessionId();
		final ContainerResponse containerResponse = (ContainerResponse) thisJoinPoint.getTarget();
		final MultivaluedMap<String, Object> responseHeader = containerResponse.getHttpHeaders();

		// Pass back trace id, session id, eoi but not ess (use old value before the request)
		final List<Object> responseHeaderList = new ArrayList<Object>();
		responseHeaderList.add(Long.toString(traceId) + "," + sessionId + "," + Integer.toString(CF_REGISTRY.recallThreadLocalEOI()));
		responseHeader.put(JerseyHeaderConstants.OPERATION_EXECUTION_JERSEY_HEADER, responseHeaderList);
		if (LOG.isDebugEnabled()) {
			LOG.debug("Sending response with header = " + responseHeader.toString() + " to the request: " + containerResponse.getContainerRequest().getRequestUri());
		}

		final Object retval = thisJoinPoint.proceed();

		return retval;
	}

	private final void unsetKiekerThreadLocalData() {
		CF_REGISTRY.unsetThreadLocalTraceId();
		CF_REGISTRY.unsetThreadLocalEOI();
		CF_REGISTRY.unsetThreadLocalESS();
	}
}
