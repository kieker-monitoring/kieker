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

package kieker.examples.userguide.ch3and4bookstore;

import java.util.concurrent.TimeUnit;

import kieker.analysis.AnalysisController;
import kieker.common.configuration.Configuration;

public final class Starter {

	private Starter() {}

	public static void main(final String[] args) throws Exception {
		/*
		 * Spawn a thread that performs asynchronous requests
		 * to a bookstore.
		 */
		new Thread(new Runnable() {

			public void run() {
				final Bookstore bookstore = new Bookstore();
				for (int i = 0; i < 5; i++) {
					System.out.println("Bookstore.main: Starting request " + i);
					bookstore.searchBook();
				}
			}
		}).start();

		/* Create a new analysis controller for our response time analysis. */
		final AnalysisController analysisController = new AnalysisController();

		/* Configure and register the reader */
		final Configuration readerConfiguration = new Configuration();
		readerConfiguration.setProperty(MyPipeReader.CONFIG_PROPERTY_NAME_PIPE_NAME, "somePipe");
		final MyPipeReader reader = new MyPipeReader(readerConfiguration);
		analysisController.registerReader(reader);

		/* Configure, register, and connect the response time filter */
		final Configuration filterConfiguration = new Configuration();
		final long rtThresholdNanos = TimeUnit.NANOSECONDS.convert(1900, TimeUnit.MICROSECONDS);
		filterConfiguration.setProperty( // configure threshold of 1.9 milliseconds:
				MyResponseTimeFilter.CONFIG_PROPERTY_NAME_RT_TS_NANOS, Long.toString(rtThresholdNanos));
		final MyResponseTimeFilter filter = new MyResponseTimeFilter(filterConfiguration);
		analysisController.registerFilter(filter);
		analysisController.connect(reader, MyPipeReader.OUTPUT_PORT_NAME,
				filter, MyResponseTimeFilter.INPUT_PORT_NAME_RESPONSE_TIMES);

		/* Configure, register, and connect the filter printing *valid* response times */
		final Configuration validOutputConfiguration = new Configuration();
		validOutputConfiguration.setProperty(MyResponseTimeOutputPrinter.CONFIG_PROPERTY_NAME_VALID_OUTPUT, Boolean.toString(true));
		final MyResponseTimeOutputPrinter validPrinter = new MyResponseTimeOutputPrinter(validOutputConfiguration);
		analysisController.registerFilter(validPrinter);
		analysisController.connect(filter, MyResponseTimeFilter.OUTPUT_PORT_NAME_RT_VALID, validPrinter, MyResponseTimeOutputPrinter.INPUT_PORT_NAME_EVENTS);

		/* Configure, register, and connect the filter printing *invalid* response times */
		final Configuration invalidOutputConfiguration = new Configuration();
		invalidOutputConfiguration.setProperty(MyResponseTimeOutputPrinter.CONFIG_PROPERTY_NAME_VALID_OUTPUT, Boolean.toString(false));
		final MyResponseTimeOutputPrinter invalidPrinter = new MyResponseTimeOutputPrinter(invalidOutputConfiguration);
		analysisController.registerFilter(invalidPrinter);
		analysisController.connect(filter, MyResponseTimeFilter.OUTPUT_PORT_NAME_RT_EXCEED, invalidPrinter, MyResponseTimeOutputPrinter.INPUT_PORT_NAME_EVENTS);

		/* Start the analysis. */
		analysisController.run();
	}
}
