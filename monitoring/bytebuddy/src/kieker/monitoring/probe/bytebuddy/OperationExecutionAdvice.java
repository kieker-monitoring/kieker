/***************************************************************************
 * Copyright 2022 Kieker Project (http://kieker-monitoring.net)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ***************************************************************************/

package kieker.monitoring.probe.bytebuddy;

import kieker.common.record.controlflow.OperationExecutionRecord;
import kieker.monitoring.core.controller.IMonitoringController;
import kieker.monitoring.core.controller.MonitoringController;
import kieker.monitoring.core.registry.ControlFlowRegistry;
import kieker.monitoring.core.registry.SessionRegistry;
import kieker.monitoring.probe.utilities.OperationStartData;
import kieker.monitoring.timer.ITimeSource;

import net.bytebuddy.asm.Advice;

/**
 * Advice for bytebuddy instrumentation for the OperationExecutionRecord.
 * 
 * @author David Georg Reichelt
 */
public class OperationExecutionAdvice {

	@Advice.OnMethodEnter
	public static OperationStartData enter(
			@Advice.Origin final String operationSignature,
			@Advice.FieldValue(value = "CTRLINST", readOnly = false) IMonitoringController CTRLINST,
			@Advice.FieldValue(value = "TIME", readOnly = false) ITimeSource TIME,
			@Advice.FieldValue(value = "CFREGISTRY", readOnly = false) ControlFlowRegistry CFREGISTRY) {
		if (CTRLINST == null) {
			CTRLINST = MonitoringController.getInstance();
			TIME = CTRLINST.getTimeSource();
			CFREGISTRY = ControlFlowRegistry.INSTANCE;
		}

		if (!CTRLINST.isMonitoringEnabled()) {
			return null;
		}
		if (!CTRLINST.isProbeActivated(operationSignature)) {
			return null;
		}
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
				System.err.println("eoi and/or ess have invalid values: eoi == " + eoi + " ess == " + ess);
				CTRLINST.terminateMonitoring();
			}
		}
		// measure before
		final long tin = TIME.getTime();

		final OperationStartData data = new OperationStartData(entrypoint, traceId, tin, eoi, ess);
		return data;
	}

	@Advice.OnMethodExit
	public static void exit(
			@Advice.Enter final OperationStartData startData,
			@Advice.Origin final String operationSignature,
			@Advice.FieldValue(value = "CTRLINST", readOnly = true) final IMonitoringController CTRLINST,
			@Advice.FieldValue(value = "TIME", readOnly = true) final ITimeSource TIME,
			@Advice.FieldValue(value = "VMNAME", readOnly = false) String VMNAME,
			@Advice.FieldValue(value = "CFREGISTRY", readOnly = true) final ControlFlowRegistry CFREGISTRY,
			@Advice.FieldValue(value = "SESSIONREGISTRY", readOnly = false) SessionRegistry SESSIONREGISTRY) {
		if (startData == null) {
			return;
		}

		if (!CTRLINST.isMonitoringEnabled()) {
			return;
		}
		if (!CTRLINST.isProbeActivated(operationSignature)) {
			return;
		}

		if (VMNAME == null) {
			VMNAME = CTRLINST.getHostname();
			SESSIONREGISTRY = SessionRegistry.INSTANCE;
		}

		final String sessionId = SESSIONREGISTRY.recallThreadLocalSessionId();

		final long tout = TIME.getTime();
		CTRLINST.newMonitoringRecord(
				new OperationExecutionRecord(operationSignature, sessionId,
						startData.getTraceId(), startData.getTin(), tout, VMNAME, startData.getEoi(), startData.getEss()));
		// cleanup
		if (startData.isEntrypoint()) {
			CFREGISTRY.unsetThreadLocalTraceId();
			CFREGISTRY.unsetThreadLocalEOI();
			CFREGISTRY.unsetThreadLocalESS();
		} else {
			CFREGISTRY.storeThreadLocalESS(startData.getEss()); // next operation is ess
		}
	}

	// @RuntimeType
	// public static Object intercept(@Origin Method method, @SuperCall Callable<?> callable) {
	// System.out.println("Intercepting...");
	// long start = System.currentTimeMillis();
	// try {
	// return callable.call();
	// } catch (Exception e) {
	// throw new RuntimeException(e);
	// } finally {
	// System.out.println(method + " took " + (System.currentTimeMillis() - start));
	// }
	// }
}
