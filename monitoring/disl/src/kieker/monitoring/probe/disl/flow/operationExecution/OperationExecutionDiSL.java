/***************************************************************************
 * Copyright 2023 Kieker Project (http://kieker-monitoring.net)
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

package kieker.monitoring.probe.disl.flow.operationExecution;

import ch.usi.dag.disl.annotation.After;
import ch.usi.dag.disl.annotation.Before;
import ch.usi.dag.disl.annotation.SyntheticLocal;
import ch.usi.dag.disl.marker.BodyMarker;

import kieker.monitoring.probe.utilities.FullOperationStartData;
import kieker.monitoring.probe.utilities.OperationExecutionDataGatherer;

/**
 * The default instrumentation class for DiSL.
 * 
 * @author Lubomir Bulej, David Georg Reichelt
 */
public class OperationExecutionDiSL { // NOPMD (Not a utility class due to DiSLs internal structure)

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
