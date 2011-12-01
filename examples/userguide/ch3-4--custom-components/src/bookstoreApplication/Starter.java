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

import kieker.analysis.AnalysisController;

public class Starter {

	public static void main(final String[] args) throws Exception {
		/*
		 * Spawn a thread that performs asynchronous requests
		 * to a bookstore.
		 */
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

		/* Start an analysis of the response times */
		final AnalysisController analyisController = new AnalysisController();
		final MyPipeReader reader =
				new MyPipeReader("somePipe");
		final MyResponseTimeConsumer consumer =
				new MyResponseTimeConsumer();
		analyisController.setReader(reader);
		analyisController.registerPlugin(consumer);

		reader.getDefaultOutputPort().subscribe(consumer.getDefaultInputPort());
		analyisController.run();
	}
}
