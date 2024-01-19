package kieker.monitoring.probe.bytebuddy;

import java.util.Stack;

import kieker.common.record.controlflow.OperationExecutionRecord;
import kieker.common.record.flow.trace.TraceMetadata;
import kieker.common.record.flow.trace.operation.AfterOperationEvent;
import kieker.common.record.flow.trace.operation.BeforeOperationEvent;
import kieker.monitoring.core.controller.IMonitoringController;
import kieker.monitoring.core.controller.MonitoringController;
import kieker.monitoring.core.registry.ControlFlowRegistry;
import kieker.monitoring.core.registry.SessionRegistry;
import kieker.monitoring.core.registry.TraceRegistry;
import kieker.monitoring.probe.aspectj.operationExecution.OperationStartData;
import kieker.monitoring.timer.ITimeSource;
import net.bytebuddy.asm.Advice;

public class OperationExecutionAdvice {
	
	@Advice.OnMethodEnter
    public static OperationStartData enter(@Advice.Origin String operationSignature) {
		final IMonitoringController CTRLINST = MonitoringController.getInstance();
		final ITimeSource TIME = CTRLINST.getTimeSource();
		final String VMNAME = CTRLINST.getHostname();
		final ControlFlowRegistry CFREGISTRY = ControlFlowRegistry.INSTANCE;
		final SessionRegistry SESSIONREGISTRY = SessionRegistry.INSTANCE;
		
		if (!CTRLINST.isMonitoringEnabled()) {
			return null;
		}
		if (!CTRLINST.isProbeActivated(operationSignature)) {
			return null;
		}
		final boolean entrypoint;
		final String hostname = VMNAME;
		final String sessionId = SESSIONREGISTRY.recallThreadLocalSessionId();
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
				System.err.println("eoi and/or ess have invalid values: eoi == "+eoi+" ess == " +ess);
				CTRLINST.terminateMonitoring();
			}
		}
		// measure before
		final long tin = TIME.getTime();

		final OperationStartData data = new OperationStartData(entrypoint, sessionId, traceId, tin, hostname, eoi, ess);
		return data;
    }
	
	@Advice.OnMethodExit
    public static void exit(
    		@Advice.Enter OperationStartData startData,
    		@Advice.Origin String operationSignature) {
		final IMonitoringController CTRLINST = MonitoringController.getInstance();
		final ITimeSource TIME = CTRLINST.getTimeSource();
		final ControlFlowRegistry CFREGISTRY = ControlFlowRegistry.INSTANCE;
		
		if (startData == null) {
			return;
		}
		
		if (!CTRLINST.isMonitoringEnabled()) {
			return;
		}
		if (!CTRLINST.isProbeActivated(operationSignature)) {
			return;
		}
		
		final long tout = TIME.getTime();
		CTRLINST.newMonitoringRecord(
				new OperationExecutionRecord(operationSignature, startData.getSessionId(),
						startData.getTraceId(), startData.getTin(), tout, startData.getHostname(), startData.getEoi(), startData.getEss()));
		// cleanup
		if (startData.isEntrypoint()) {
			CFREGISTRY.unsetThreadLocalTraceId();
			CFREGISTRY.unsetThreadLocalEOI();
			CFREGISTRY.unsetThreadLocalESS();
		} else {
			CFREGISTRY.storeThreadLocalESS(startData.getEss()); // next operation is ess
		}
	}
    		
	
//	@RuntimeType
//	public static Object intercept(@Origin Method method, @SuperCall Callable<?> callable) {
//		System.out.println("Intercepting...");
//		long start = System.currentTimeMillis();
//		try {
//			return callable.call();
//		} catch (Exception e) {
//			throw new RuntimeException(e);
//		} finally {
//			System.out.println(method + " took " + (System.currentTimeMillis() - start));
//		}
//	}
}
