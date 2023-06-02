package kieker.monitoring.probe.disl.flow.operationExecution;

import kieker.monitoring.core.controller.MonitoringController;

import ch.usi.dag.disl.annotation.After;
import ch.usi.dag.disl.annotation.Before;
import ch.usi.dag.disl.marker.BodyMarker;

public class OperationExecutionDiSL {
	
	@Before(marker = BodyMarker.class, scope = "Main.*")
	public static void beforemain() {
		if (!MonitoringController.getInstance().isMonitoringEnabled()) {
			return;
		}
		System.out.println("Instrumentation: Before (Kieker2)");
	}

	@After(marker = BodyMarker.class, scope = "Main.*")
	public static void aftermain() {
		System.out.println("Instrumentation: After (Kieker2)");
	}
}
