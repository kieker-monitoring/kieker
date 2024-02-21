package kieker.monitoring.probe.disl.flow.operationExecution;

import ch.usi.dag.disl.annotation.After;
import ch.usi.dag.disl.annotation.Before;
import ch.usi.dag.disl.annotation.SyntheticLocal;
import ch.usi.dag.disl.marker.BodyMarker;

/**
 * The default instrumentation class for DiSL
 */
public class OperationExecutionDiSL {

	@SyntheticLocal
	private static FullOperationStartData data;

	@Before(marker = BodyMarker.class, scope = "MonitoredClass*.*")
	public static void beforemain(final KiekerStaticContext c) {
		data = OperationExecutionDataGatherer.operationStart(c.operationSignature());
	}

	@After(marker = BodyMarker.class, scope = "MonitoredClass*.*")
	public static void aftermain() {
		//
		// If "data" is "null", monitoring was disabled when "operationStart"
		// was called in the "beforemain" snippet. If it is valid, monitoring
		// was enabled and we should complete it by calling "operationEnd".
		//
		if (data != null) {
			OperationExecutionDataGatherer.operationEnd(data);
		}
	}
}
