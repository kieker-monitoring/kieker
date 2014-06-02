/***************************************************************************
 * Copyright 2014 Kieker Project (http://kieker-monitoring.net)
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
package kieker.experiment;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.Callable;

import kieker.analysis.AnalysisController;
import kieker.analysis.EmptyPassOnFilter;
import kieker.analysis.IAnalysisController;
import kieker.analysis.ObjectProducer;
import kieker.common.configuration.Configuration;

/**
 * @author Nils Christian Ehmke
 * 
 * @since 1.10
 */
public class Experiment1 {

	private static final int NUMBER_OF_WARMUP_RUNS_PER_EXPERIMENT = 100;
	private static final int NUMBER_OF_MEASURED_RUNS_PER_EXPERIMENT = 100;

	private static final int NUMBER_OF_OBJECTS_TO_SEND = 10000;

	private static final int NUMBER_OF_MINIMAL_FILTERS = 10;
	private static final int NUMBER_OF_MAXIMAL_FILTERS = 1000;
	private static final int NUMBER_OF_FILTERS_PER_STEP = 10;

	private static final IAnalysis[] analyses = { new TeeTimeAnalysis(), new KiekerAnalysis() };

	private static final Collection<Long> measuredTimes = new ArrayList<Long>();

	public static void main(final String[] args) throws Exception {
		for (final IAnalysis analysis : analyses) {
			for (int numberOfFilters = NUMBER_OF_MINIMAL_FILTERS; numberOfFilters <= NUMBER_OF_MAXIMAL_FILTERS; numberOfFilters += NUMBER_OF_FILTERS_PER_STEP) {
				// Warmup
				for (int run = 0; run < NUMBER_OF_WARMUP_RUNS_PER_EXPERIMENT; run++) {
					analysis.initialize(numberOfFilters, NUMBER_OF_OBJECTS_TO_SEND);
					analysis.execute();
				}

				// Actual measurement
				for (int run = 0; run < NUMBER_OF_MEASURED_RUNS_PER_EXPERIMENT; run++) {
					final long tin = System.nanoTime();

					analysis.initialize(numberOfFilters, NUMBER_OF_OBJECTS_TO_SEND);
					analysis.execute();

					final long tout = System.nanoTime();
					Experiment1.addMeasuredTime((tout - tin));
				}

				Experiment1.writeAndClearMeasuredTime(analysis.getName(), numberOfFilters);
			}
		}
	}

	private static void addMeasuredTime(final long time) {
		measuredTimes.add(new Long(time));
	}

	private static void writeAndClearMeasuredTime(final String analysisName, final int numberOfFilters) throws IOException {
		final FileWriter fileWriter = new FileWriter(analysisName + ".csv", true);
		fileWriter.write(Integer.toString(numberOfFilters));
		fileWriter.write(";");
		// ...
		fileWriter.write("\n");
		fileWriter.close();

		measuredTimes.clear();
	}

	private static interface IAnalysis {

		public void initialize(int numberOfFilters, int numberOfObjectsToSend) throws Exception;

		public String getName();

		public void execute() throws Exception;

	}

	private static final class TeeTimeAnalysis implements IAnalysis {

		public void initialize(final int numberOfFilters, final int numberOfObjectsToSend) {}

		public String getName() {
			return "TeeTime";
		}

		public void execute() {}

	}

	private static final class KiekerAnalysis implements IAnalysis {

		private IAnalysisController ac;

		public void initialize(final int numberOfFilters, final int numberOfObjectsToSend) throws Exception {
			this.ac = new AnalysisController();

			final Configuration producerConfig = new Configuration();
			producerConfig.setProperty(ObjectProducer.CONFIG_PROPERTY_NAME_OBJECTS_TO_CREATE, Long.toString(numberOfObjectsToSend));
			final ObjectProducer<Object> producer = new ObjectProducer<Object>(producerConfig, this.ac, new Callable<Object>() {
				public Object call() throws Exception {
					return new Object();
				}
			});

			EmptyPassOnFilter predecessor = new EmptyPassOnFilter(new Configuration(), this.ac);
			this.ac.connect(producer, ObjectProducer.OUTPUT_PORT_NAME, predecessor, EmptyPassOnFilter.INPUT_PORT_NAME);
			for (int idx = 0; idx < (numberOfFilters - 1); idx++) {
				final EmptyPassOnFilter newPredecessor = new EmptyPassOnFilter(new Configuration(), this.ac);
				this.ac.connect(predecessor, EmptyPassOnFilter.OUTPUT_PORT_NAME, newPredecessor, EmptyPassOnFilter.INPUT_PORT_NAME);
				predecessor = newPredecessor;
			}
		}

		public String getName() {
			return "Kieker";
		}

		public void execute() throws Exception {
			this.ac.run();
		}

	}

}
