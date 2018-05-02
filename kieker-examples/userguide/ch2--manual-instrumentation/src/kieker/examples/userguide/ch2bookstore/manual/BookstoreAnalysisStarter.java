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

package kieker.examples.userguide.ch2bookstore.manual;

import kieker.analysis.AnalysisController;
import kieker.analysis.IAnalysisController;
import kieker.analysis.plugin.filter.forward.TeeFilter;
import kieker.analysis.plugin.reader.filesystem.FSReader;
import kieker.common.configuration.Configuration;

public final class BookstoreAnalysisStarter {

	private BookstoreAnalysisStarter() {}

	public static void main(final String[] args) {

		if (args.length == 0) {
			System.err.println("Please provide a monitoring log directory as argument");
			System.exit(1);
		}

		try {
			// Create Kieker.Analysis instance
			final IAnalysisController analysisInstance = new AnalysisController();

			// Set filesystem monitoring log input directory for our analysis
			final Configuration fsReaderConfig = new Configuration();
			fsReaderConfig.setProperty(FSReader.CONFIG_PROPERTY_NAME_INPUTDIRS, args[0]);
			final FSReader reader = new FSReader(fsReaderConfig, analysisInstance);

			// Create and register a simple output writer.
			final Configuration teeFilterConfig = new Configuration();
			teeFilterConfig.setProperty(TeeFilter.CONFIG_PROPERTY_NAME_STREAM,
					TeeFilter.CONFIG_PROPERTY_VALUE_STREAM_STDOUT);
			final TeeFilter teeFilter = new TeeFilter(teeFilterConfig, analysisInstance);

			// Connect the output of the reader with the input of the filter.
			analysisInstance.connect(reader, FSReader.OUTPUT_PORT_NAME_RECORDS,
					teeFilter, TeeFilter.INPUT_PORT_NAME_EVENTS);

			// Start the analysis
			analysisInstance.run();
		} catch (final Exception e) {
			System.out.println("An error occurred: " + e.getMessage());
			System.exit(1);
		}
	}
}
