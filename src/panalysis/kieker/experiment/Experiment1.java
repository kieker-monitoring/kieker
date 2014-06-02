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

/**
 * @author Nils Christian Ehmke
 * 
 * @since 1.10
 */
public class Experiment1 {

	private static final int NUMBER_OF_WARMUP_RUNS_PER_EXPERIMENT = 100;
	private static final int NUMBER_OF_MEASURED_RUNS_PER_EXPERIMENT = 100;

	private static final int NUMBER_OF_OBJECTS_TO_SEND = 100000;

	private static final int NUMBER_OF_MINIMAL_FILTERS = 10;
	private static final int NUMBER_OF_MAXIMAL_FILTERS = 1000;
	private static final int NUMBER_OF_FILTERS_PER_STEP = 10;

	private static final IAnalysis[] analyses = { new TeeTimeAnalysis(), new KiekerAnalysis() };

	private static final Collection<Long> measuredTimes = new ArrayList<Long>();

	public static void main(final String[] args) throws IOException {
		for (final IAnalysis analysis : analyses) {
			for (int numberOfFilters = NUMBER_OF_MINIMAL_FILTERS; numberOfFilters <= NUMBER_OF_MAXIMAL_FILTERS; numberOfFilters += NUMBER_OF_FILTERS_PER_STEP) {
				analysis.initialize(numberOfFilters, NUMBER_OF_OBJECTS_TO_SEND);

				// Warmup
				for (int run = 0; run < NUMBER_OF_WARMUP_RUNS_PER_EXPERIMENT; run++) {
					analysis.execute();
				}

				// Actual measurement
				for (int run = 0; run < NUMBER_OF_MEASURED_RUNS_PER_EXPERIMENT; run++) {
					final long tin = System.nanoTime();

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

		public void initialize(int numberOfFilters, int numberOfObjectsToSend);

		public String getName();

		public void execute();

	}

	private static final class TeeTimeAnalysis implements IAnalysis {

		public void initialize(final int numberOfFilters, final int numberOfObjectsToSend) {}

		public String getName() {
			return "TeeTime";
		}

		public void execute() {}

	}

	private static final class KiekerAnalysis implements IAnalysis {

		public void initialize(final int numberOfFilters, final int numberOfObjectsToSend) {}

		public String getName() {
			return "Kieker";
		}

		public void execute() {}

	}

}
