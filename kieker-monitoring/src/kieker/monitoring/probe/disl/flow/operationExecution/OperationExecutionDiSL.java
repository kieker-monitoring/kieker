package kieker.monitoring.probe.disl.flow.operationExecution;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ch.usi.dag.disl.annotation.After;
import ch.usi.dag.disl.annotation.Before;
import ch.usi.dag.disl.annotation.SyntheticLocal;
import ch.usi.dag.disl.marker.BodyMarker;
import kieker.monitoring.probe.aspectj.operationExecution.AbstractOperationExecutionAspect;

public class OperationExecutionDiSL {

	private static final Logger LOGGER = LoggerFactory.getLogger(AbstractOperationExecutionAspect.class);

	@SyntheticLocal
	static DiSLOperationStartData data;

	@Before(marker = BodyMarker.class, scope = "MonitoredClass*.*")
	public static void beforemain(final KiekerStaticContext c) {
		data = KiekerDiSLAnalysis.operationStart(c.operationSignature());
	}

	@After(marker = BodyMarker.class, scope = "MonitoredClass*.*")
	public static void aftermain() {
		//
		// If "data" is "null", monitoring was disabled when "operationStart"
		// was called in the "beforemain" snippet. If it is valid, monitoring
		// was enabled and we should complete it by calling "operationEnd".
		//
		if (data != null) {
			KiekerDiSLAnalysis.operationEnd(data);
		}
	}
}
