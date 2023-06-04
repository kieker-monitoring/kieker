package kieker.monitoring.probe.disl.flow.operationExecution;

import java.util.Stack;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import kieker.common.record.controlflow.OperationExecutionRecord;
import kieker.common.record.flow.trace.operation.AfterOperationEvent;
import kieker.monitoring.core.controller.IMonitoringController;
import kieker.monitoring.core.controller.MonitoringController;
import kieker.monitoring.core.registry.ControlFlowRegistry;
import kieker.monitoring.core.registry.SessionRegistry;
import kieker.monitoring.probe.aspectj.operationExecution.AbstractOperationExecutionAspect;
import kieker.monitoring.probe.aspectj.operationExecution.OperationStartData;
import kieker.monitoring.timer.ITimeSource;

import ch.usi.dag.disl.annotation.After;
import ch.usi.dag.disl.annotation.Before;
import ch.usi.dag.disl.dynamiccontext.DynamicContext;
import ch.usi.dag.disl.marker.BodyMarker;
import ch.usi.dag.disl.staticcontext.ClassStaticContext;
import ch.usi.dag.disl.staticcontext.MethodStaticContext;

public class OperationExecutionDiSL {

	private static final Logger LOGGER = LoggerFactory.getLogger(AbstractOperationExecutionAspect.class);

	@ch.usi.dag.disl.annotation.ThreadLocal
	static Stack<OperationStartData> stack;

	@Before(marker = BodyMarker.class, scope = "Main.*")
	public static void beforemain(final MethodStaticContext msc, ClassStaticContext c) {
		final IMonitoringController CTRLINST = MonitoringController.getInstance();
		final ITimeSource TIME = CTRLINST.getTimeSource();
		final String VMNAME = CTRLINST.getHostname();
		final ControlFlowRegistry CFREGISTRY = ControlFlowRegistry.INSTANCE;
		final SessionRegistry SESSIONREGISTRY = SessionRegistry.INSTANCE;

		if (!CTRLINST.isMonitoringEnabled()) {
			return;
		}
		final String operationSignature = msc.thisMethodFullName();
		if (!CTRLINST.isProbeActivated(operationSignature)) {
			return;
		}
		// common fields
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
				LOGGER.error("eoi and/or ess have invalid values: eoi == {} ess == {}", eoi, ess);
				CTRLINST.terminateMonitoring();
			}
		}
		// measure before
		final long tin = TIME.getTime();

		final OperationStartData data = new OperationStartData(entrypoint, sessionId, traceId, tin, hostname, eoi, ess);

		if (stack == null) {
			stack = new Stack<>();
		}
		stack.push(data);
	}

	@After(marker = BodyMarker.class, scope = "Main.*")
	public static void aftermain(final MethodStaticContext msc, ClassStaticContext c) {
		final IMonitoringController CTRLINST = MonitoringController.getInstance();
		final ITimeSource TIME = CTRLINST.getTimeSource();
		final ControlFlowRegistry CFREGISTRY = ControlFlowRegistry.INSTANCE;

		if (!CTRLINST.isMonitoringEnabled()) {
			return;
		}

		final String operationSignature = msc.thisMethodFullName();
		if (!CTRLINST.isProbeActivated(operationSignature)) {
			return;
		}

		final OperationStartData data = stack.pop();

		final long tout = TIME.getTime();
		OperationExecutionRecord record = new OperationExecutionRecord(operationSignature, data.getSessionId(),
				data.getTraceId(), data.getTin(), tout, data.getHostname(), data.getEoi(), data.getEss());
		CTRLINST.newMonitoringRecord(record);
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
