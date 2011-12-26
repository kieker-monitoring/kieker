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

package bookstoreApplication;

import java.util.Properties;

import kieker.analysis.AnalysisController;
import kieker.common.configuration.Configuration;
import kieker.analysis.exception.MonitoringRecordConsumerException;
import kieker.analysis.plugin.AbstractPlugin;
import kieker.analysis.reader.IMonitoringReader;
import kieker.analysis.exception.MonitoringReaderException;
import kieker.analysis.reader.filesystem.FSReader;

public class BookstoreAnalysisStarter {

	public static void main(final String[] args)
			throws MonitoringReaderException, MonitoringRecordConsumerException {

		if (args.length == 0) {
			return;
		}

		/* Create Kieker.Analysis instance */
		final AnalysisController analysisInstance = new AnalysisController();
		/* Register our own consumer; set the max. response time to 1.9 ms */
		final Consumer consumer = new Consumer(1900000);
		analysisInstance.registerPlugin(consumer);

		/* Set filesystem monitoring log input directory for our analysis */
		final Configuration configuration = new Configuration();
		configuration.setProperty(FSReader.CONFIG_INPUTDIRS, args[0]);

		final FSReader reader = new FSReader(configuration);
		analysisInstance.setReader(reader);

		/* Connect the output of the reader with the input of the plugin. */
		AbstractPlugin.connect(reader, FSReader.OUTPUT_PORT_NAME, consumer, Consumer.INPUT_PORT_NAME);

		/* Start the analysis */
		analysisInstance.run();
	}
}
