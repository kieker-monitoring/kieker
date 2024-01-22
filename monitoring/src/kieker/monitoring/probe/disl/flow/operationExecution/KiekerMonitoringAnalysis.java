package kieker.monitoring.probe.disl.flow.operationExecution;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import kieker.common.record.controlflow.OperationExecutionRecord;
import kieker.monitoring.core.controller.IMonitoringController;
import kieker.monitoring.core.controller.MonitoringController;
import kieker.monitoring.core.registry.ControlFlowRegistry;
import kieker.monitoring.core.registry.SessionRegistry;
import kieker.monitoring.timer.ITimeSource;

public class KiekerMonitoringAnalysis {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(KiekerMonitoringAnalysis.class);
	
	public static FullOperationStartData operationStart(final String operationSignature) {
		final IMonitoringController CTRLINST = MonitoringController.getInstance();
		if (!CTRLINST.isMonitoringEnabled()) {
			return null;
		}
		if (!CTRLINST.isProbeActivated(operationSignature)) {
			return null;
		}
		final ITimeSource TIME = CTRLINST.getTimeSource();
		final ControlFlowRegistry CFREGISTRY = ControlFlowRegistry.INSTANCE;
		
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

		final FullOperationStartData data = new FullOperationStartData(entrypoint, traceId, tin, eoi, ess, operationSignature);
		return data;
	}

	public static void operationEnd(FullOperationStartData startData) {
		final IMonitoringController CTRLINST = MonitoringController.getInstance();
		if (!CTRLINST.isMonitoringEnabled()) {
			return;
		}

		if (!CTRLINST.isProbeActivated(startData.getOperationSignature())) {
			return;
		}

		final ControlFlowRegistry CFREGISTRY = ControlFlowRegistry.INSTANCE;
		final ITimeSource TIME = CTRLINST.getTimeSource();
		final String VMNAME = CTRLINST.getHostname();
		final SessionRegistry SESSIONREGISTRY = SessionRegistry.INSTANCE;
		
		final long tout = TIME.getTime();
		
		final String sessionId = SESSIONREGISTRY.recallThreadLocalSessionId();
		OperationExecutionRecord record = new OperationExecutionRecord(startData.getOperationSignature(), sessionId,
				startData.getTraceId(), startData.getTin(), tout, VMNAME, startData.getEoi(), startData.getEss());
		CTRLINST.newMonitoringRecord(record);
		// cleanup
		if (startData.isEntrypoint()) {
			CFREGISTRY.unsetThreadLocalTraceId();
			CFREGISTRY.unsetThreadLocalEOI();
			CFREGISTRY.unsetThreadLocalESS();
		} else {
			CFREGISTRY.storeThreadLocalESS(startData.getEss()); // next operation is ess
		}
	}
}
