package kieker.monitoring.probe.disl.flow.operationExecution;

import java.util.Stack;

import kieker.common.record.controlflow.OperationExecutionRecord;
import kieker.common.record.flow.trace.operation.AfterOperationEvent;
import kieker.monitoring.core.controller.IMonitoringController;
import kieker.monitoring.core.controller.MonitoringController;
import kieker.monitoring.probe.aspectj.operationExecution.OperationStartData;
import kieker.monitoring.timer.ITimeSource;

import ch.usi.dag.disl.annotation.After;
import ch.usi.dag.disl.annotation.Before;
import ch.usi.dag.disl.dynamiccontext.DynamicContext;
import ch.usi.dag.disl.marker.BodyMarker;
import ch.usi.dag.disl.staticcontext.ClassStaticContext;
import ch.usi.dag.disl.staticcontext.MethodStaticContext;

public class OperationExecutionDiSL {

	@ch.usi.dag.disl.annotation.ThreadLocal
	static Stack<OperationStartData> stack;

	@Before(marker = BodyMarker.class, scope = "Main.*")
	public static void beforemain(final MethodStaticContext msc, ClassStaticContext c) {
		final IMonitoringController CTRLINST = MonitoringController.getInstance();
		final ITimeSource TIME = CTRLINST.getTimeSource();

		if (!CTRLINST.isMonitoringEnabled()) {
			return;
		}

		System.out.println("Enabled: " + c.getInternalName() + " " + msc.thisMethodFullName());
//		final String clazz = c.getInternalName();
		final String operationSignature = msc.thisMethodFullName();
//		final String operationSignature = clazz + "." + methodFullName;
		System.out.println("Signature: " + operationSignature);

		if (!CTRLINST.isProbeActivated(operationSignature)) {
			return;
		}

		final long tin = TIME.getTime();

		final OperationStartData data = new OperationStartData(true, "1", 1, tin, "", 0, 0);

		if (stack == null) {
			stack = new Stack<>();
		}

		stack.push(data);
	}

	@After(marker = BodyMarker.class, scope = "Main.*")
	public static void aftermain(final MethodStaticContext msc, ClassStaticContext c) {
		final IMonitoringController CTRLINST = MonitoringController.getInstance();
		final ITimeSource TIME = CTRLINST.getTimeSource();

//		final String clazz = c.getInternalName();
		final String operationSignature = msc.thisMethodFullName();
//		final String operationSignature = clazz + "." + methodFullName;

		if (!CTRLINST.isMonitoringEnabled() || !CTRLINST.isProbeActivated(operationSignature)) {
			return;
		}

		final OperationStartData data = stack.pop();

		final long tout = TIME.getTime();

		CTRLINST.newMonitoringRecord(
				new OperationExecutionRecord(operationSignature, data.getSessionId(),
						data.getTraceId(), data.getTin(), tout, data.getHostname(), data.getEoi(), data.getEss()));
	}
}
