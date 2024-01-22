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

package kieker.monitoring.probe.aspectj.operationExecution;

import java.util.Stack;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import kieker.common.record.controlflow.OperationExecutionRecord;
import kieker.monitoring.core.controller.IMonitoringController;
import kieker.monitoring.core.controller.MonitoringController;
import kieker.monitoring.core.registry.ControlFlowRegistry;
import kieker.monitoring.core.registry.SessionRegistry;
import kieker.monitoring.probe.aspectj.AbstractAspectJProbe;
import kieker.monitoring.timer.ITimeSource;

/**
 * @author Andre van Hoorn, Jan Waller
 *
 * @since 1.3
 */
@Aspect
public abstract class AbstractOperationExecutionAspect extends AbstractAspectJProbe {
	private static final Logger LOGGER = LoggerFactory.getLogger(AbstractOperationExecutionAspect.class);

	private static final IMonitoringController CTRLINST = MonitoringController.getInstance();
	private static final ITimeSource TIME = CTRLINST.getTimeSource();
	private static final String VMNAME = CTRLINST.getHostname();
	private static final ControlFlowRegistry CFREGISTRY = ControlFlowRegistry.INSTANCE;
	private static final SessionRegistry SESSIONREGISTRY = SessionRegistry.INSTANCE;

	private final ThreadLocal<Stack<OperationStartData>> stack = new ThreadLocal<Stack<OperationStartData>>() {
		@Override
		protected Stack<OperationStartData> initialValue() {
			return new Stack<>();
		}
	};

	/**
	 * The pointcut for the monitored operations. Inheriting classes should extend
	 * the pointcut in order to find the correct executions of the methods (e.g. all
	 * methods or only methods with specific annotations).
	 */
	@Pointcut
	public abstract void monitoredOperation();

	@Before("monitoredOperation() && notWithinKieker()")
	public void beforeOperation(final JoinPoint thisJoinPoint) throws Throwable { // NOCS
		if (!CTRLINST.isMonitoringEnabled()) {
			return;
		}
		final String operationSignature = this.signatureToLongString(thisJoinPoint.getSignature());
		if (!CTRLINST.isProbeActivated(operationSignature)) {
			return;
		}
		// common fields
		final boolean entrypoint;
		final int eoi; // this is executionOrderIndex-th execution in this trace
		final int ess; // this is the height in the dynamic call tree of this execution
		long traceId = CFREGISTRY.recallThreadLocalTraceId(); // traceId, -1 if entry point
		if (traceId == -1) {
			entrypoint = true;
			traceId = CFREGISTRY.getAndStoreUniqueThreadLocalTraceId();
			CFREGISTRY.storeThreadLocalEOI(0);
			CFREGISTRY.storeThreadLocalESS(1); // next operation is ess + 1
			eoi = 0;
			ess = 0;
		} else {
			entrypoint = false;
			eoi = CFREGISTRY.incrementAndRecallThreadLocalEOI(); // ess > 1
			ess = CFREGISTRY.recallAndIncrementThreadLocalESS(); // ess >= 0
			if ((eoi == -1) || (ess == -1)) {
				LOGGER.error("eoi and/or ess have invalid values: eoi == {} ess == {}", eoi, ess);
				CTRLINST.terminateMonitoring();
			}
		}
		// measure before
		final long tin = TIME.getTime();

		final OperationStartData data = new OperationStartData(entrypoint, traceId, tin, eoi, ess);
		this.stack.get().push(data);
	}

	@After("monitoredOperation() && notWithinKieker()")
	public void afterOperation(final JoinPoint thisJoinPoint) {
		if (!CTRLINST.isMonitoringEnabled()) {
			return;
		}

		final String operationSignature = this.signatureToLongString(thisJoinPoint.getSignature());
		if (!CTRLINST.isProbeActivated(operationSignature)) {
			return;
		}

		final OperationStartData data = this.stack.get().pop();

		final long tout = TIME.getTime();
		
		final String hostname = VMNAME;
		final String sessionId = SESSIONREGISTRY.recallThreadLocalSessionId();
		CTRLINST.newMonitoringRecord(
				new OperationExecutionRecord(operationSignature, sessionId,
						data.getTraceId(), data.getTin(), tout, hostname, data.getEoi(), data.getEss()));
		// cleanup
		if (data.isEntrypoint()) {
			CFREGISTRY.unsetThreadLocalTraceId();
			CFREGISTRY.unsetThreadLocalEOI();
			CFREGISTRY.unsetThreadLocalESS();
		} else {
			CFREGISTRY.storeThreadLocalESS(data.getEss()); // next operation is ess
		}
	}
}
