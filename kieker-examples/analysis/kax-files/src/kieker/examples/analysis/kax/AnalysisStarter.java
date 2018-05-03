/***************************************************************************
 * Copyright 2018 Kieker Project (http://kieker-monitoring.net)
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

package kieker.examples.analysis.kax;

import java.io.File;
import java.io.IOException;

import kieker.analysis.AnalysisController;
import kieker.analysis.IAnalysisController;
import kieker.analysis.exception.AnalysisConfigurationException;

public final class AnalysisStarter {

	private AnalysisStarter() {}

	public static void main(final String[] args) throws InterruptedException, IllegalStateException, AnalysisConfigurationException, IOException {
		// Load the analysis
		final IAnalysisController analysisController = new AnalysisController(new File("config/analysis.kax"));

		// Start the analysis
		analysisController.run();
	}

}
