/***************************************************************************
 * Copyright 2011 by
 *  + Christian-Albrechts-University of Kiel
 *    + Department of Computer Science
 *      + Software Engineering Group 
 *  and others.
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
import kieker.analysis.exception.MonitoringReaderException;
import kieker.analysis.exception.MonitoringRecordConsumerException;
import kieker.analysis.plugin.filter.forward.TeeFilter;
import kieker.analysis.plugin.reader.filesystem.FSReader;
import kieker.common.configuration.Configuration;

public final class BookstoreAnalysisStarter {

	private BookstoreAnalysisStarter() {}

	public static void main(final String[] args)
			throws MonitoringReaderException, MonitoringRecordConsumerException {

		if (args.length == 0) {
			return;
		}

		/* Create Kieker.Analysis instance */
		final AnalysisController analysisInstance = new AnalysisController();
		/* Create and register a simple output writer. */
		final TeeFilter teeFilter = new TeeFilter(new Configuration());
		analysisInstance.registerFilter(teeFilter);

		/* Set filesystem monitoring log input directory for our analysis */
		final Configuration configuration = new Configuration();
		configuration.setProperty(FSReader.CONFIG_PROPERTY_NAME_INPUTDIRS, args[0]);

		final FSReader reader = new FSReader(configuration);
		analysisInstance.registerReader(reader);

		/* Connect the output of the reader with the input of the filter. */
		analysisInstance.connect(reader, FSReader.OUTPUT_PORT_NAME_RECORDS, teeFilter, TeeFilter.INPUT_PORT_NAME_EVENTS);

		/* Start the analysis */
		analysisInstance.run();
	}
}
