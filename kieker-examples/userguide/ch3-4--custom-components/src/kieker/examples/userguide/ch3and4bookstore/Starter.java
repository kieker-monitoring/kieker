/***************************************************************************
 * Copyright 2022 Kieker Project (http://kieker-monitoring.net)
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

package kieker.examples.userguide.ch3and4bookstore;

import java.io.File;
import java.util.concurrent.TimeUnit;

import kieker.analysis.AnalysisController;
import kieker.analysis.IAnalysisController;
import kieker.analysis.analysisComponent.AbstractAnalysisComponent;
import kieker.common.configuration.Configuration;

public final class Starter {

	private Starter() {}

	public static void main(final String[] args) throws Exception {
		// Spawn a thread that performs asynchronous requests
		// to a bookstore.

		new Thread(new Runnable() {

			@Override
			public void run() {
				final Bookstore bookstore = new Bookstore();
				for (int i = 0; i < 5; i++) {
					System.out.println("Bookstore.main: Starting request " + i);
					bookstore.searchBook();
				}
			}
		}).start();

		// Create a new analysis controller for our response time analysis.
		final IAnalysisController analysisController = new AnalysisController();

		// Configure and register the reader
		final Configuration readerConfig = new Configuration();
		readerConfig.setProperty(MyPipeReader.CONFIG_PROPERTY_NAME_PIPE_NAME, "somePipe");
		final MyPipeReader reader = new MyPipeReader(readerConfig, analysisController);

		// Configure, register, and connect the response time filter
		final Configuration filterConfig = new Configuration();
		final long rtThresholdNanos =
				TimeUnit.NANOSECONDS.convert(1900, TimeUnit.MICROSECONDS);
		filterConfig.setProperty( // configure threshold of 1.9 milliseconds:
				MyResponseTimeFilter.CONFIG_PROPERTY_NAME_TS_NANOS,
				Long.toString(rtThresholdNanos));
		final MyResponseTimeFilter filter = new MyResponseTimeFilter(filterConfig, analysisController);
		analysisController.connect(reader, MyPipeReader.OUTPUT_PORT_NAME, filter, MyResponseTimeFilter.INPUT_PORT_NAME_RESPONSE_TIMES);

		// Configure, register, and connect the filter printing *valid* response times
		final Configuration validOutputConfig = new Configuration();
		validOutputConfig.setProperty(MyResponseTimeOutputPrinter.CONFIG_PROPERTY_NAME_VALID_OUTPUT, Boolean.toString(true));
		validOutputConfig.setProperty(AbstractAnalysisComponent.CONFIG_NAME, "Print valid");
		final MyResponseTimeOutputPrinter validPrinter = new MyResponseTimeOutputPrinter(validOutputConfig, analysisController);
		analysisController.connect(filter, MyResponseTimeFilter.OUTPUT_PORT_NAME_RT_VALID, validPrinter, MyResponseTimeOutputPrinter.INPUT_PORT_NAME_EVENTS);

		// Configure, register, and connect the filter printing *invalid* response times
		final Configuration invalidOutputConfig = new Configuration();
		invalidOutputConfig.setProperty(MyResponseTimeOutputPrinter.CONFIG_PROPERTY_NAME_VALID_OUTPUT, Boolean.toString(false));
		invalidOutputConfig.setProperty(AbstractAnalysisComponent.CONFIG_NAME, "Print invalid");
		final MyResponseTimeOutputPrinter invalidPrinter = new MyResponseTimeOutputPrinter(invalidOutputConfig, analysisController);
		analysisController.connect(filter, MyResponseTimeFilter.OUTPUT_PORT_NAME_RT_EXCEED, invalidPrinter, MyResponseTimeOutputPrinter.INPUT_PORT_NAME_EVENTS);

		analysisController.saveToFile(new File("out.kax"));

		// Start the analysis.
		analysisController.run();
	}
}
