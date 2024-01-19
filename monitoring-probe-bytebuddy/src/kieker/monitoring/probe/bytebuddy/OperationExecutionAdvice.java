package kieker.monitoring.probe.bytebuddy;

import java.util.Stack;

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
	
	private static final ThreadLocal<Stack<OperationStartData>> stack = new ThreadLocal<Stack<OperationStartData>>() {
		@Override
		protected Stack<OperationStartData> initialValue() {
			return new Stack<>();
		}
	};
	
	@Advice.OnMethodEnter
    public static void enter(@Advice.Origin String operationSignature) {
		final IMonitoringController CTRLINST = MonitoringController.getInstance();
		final ITimeSource TIME = CTRLINST.getTimeSource();
		final TraceRegistry TRACEREGISTRY = TraceRegistry.INSTANCE;
		
		if (!CTRLINST.isMonitoringEnabled()) {
			return;
		}
		if (!CTRLINST.isProbeActivated(operationSignature)) {
			return;
		}
		// common fields
		TraceMetadata trace = TRACEREGISTRY.getTrace();
		final boolean newTrace = trace == null;
		if (newTrace) {
			trace = TRACEREGISTRY.registerTrace();
			CTRLINST.newMonitoringRecord(trace);
		}
		final long traceId = trace.getTraceId();
//		final String clazz = thisObject.getClass().getName();
		// measure before execution
		CTRLINST.newMonitoringRecord(new BeforeOperationEvent(TIME.getTime(), traceId, trace.getNextOrderId(), operationSignature, null));
    }
	
	@Advice.OnMethodExit
    public static void exit(@Advice.Origin String operationSignature) {
		final IMonitoringController CTRLINST = MonitoringController.getInstance();
		final ITimeSource TIME = CTRLINST.getTimeSource();
		final TraceRegistry TRACEREGISTRY = TraceRegistry.INSTANCE;
		
		if (!CTRLINST.isMonitoringEnabled()) {
			return;
		}
		if (!CTRLINST.isProbeActivated(operationSignature)) {
			return;
		}
		// common fields
		TraceMetadata trace = TRACEREGISTRY.getTrace();
		final long traceId = trace.getTraceId();
		
		CTRLINST.newMonitoringRecord(new AfterOperationEvent(TIME.getTime(), traceId, trace.getNextOrderId(), operationSignature, null));
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
