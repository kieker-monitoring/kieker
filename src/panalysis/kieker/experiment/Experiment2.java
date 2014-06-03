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
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

import kieker.analysis.AnalysisController;
import kieker.analysis.CollectorSink;
import kieker.analysis.EmptyPassOnFilter;
import kieker.analysis.IAnalysisController;
import kieker.analysis.ObjectProducer;
import kieker.analysis.StartTimestampFilter;
import kieker.analysis.StopTimestampFilter;
import kieker.common.configuration.Configuration;
import kieker.panalysis.examples.throughput.TimestampObject;
import kieker.panalysis.framework.concurrent.WorkerThread;
import kieker.panalysis.framework.core.Analysis;
import kieker.panalysis.framework.core.IStage;
import kieker.panalysis.framework.core.Pipeline;
import kieker.panalysis.framework.sequential.MethodCallPipe;
import kieker.panalysis.framework.sequential.QueuePipe;
import kieker.panalysis.stage.NoopFilter;
import kieker.panalysis.util.StatisticsUtil;

/**
 * @author Nils Christian Ehmke
 * 
 * @since 1.10
 */
public class Experiment2 {

	private static final int NUMBER_OF_WARMUP_RUNS_PER_EXPERIMENT = 5;
	private static final int NUMBER_OF_MEASURED_RUNS_PER_EXPERIMENT = 50;

	private static final int NUMBER_OF_OBJECTS_TO_SEND = 10000;

	private static final int NUMBER_OF_MINIMAL_FILTERS = 50;
	private static final int NUMBER_OF_MAXIMAL_FILTERS = 1000;
	private static final int NUMBER_OF_FILTERS_PER_STEP = 50;

	private static final IAnalysis[] analyses = { new TeeTimeAnalysis(true), new TeeTimeAnalysis(false), new KiekerAnalysis() };

	private static final List<Long> measuredTimes = new ArrayList<Long>();

	public static void main(final String[] args) throws Exception {
		System.setProperty("kieker.common.logging.Log", "NONE");

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
					Experiment2.addMeasuredTime((tout - tin));
				}

				Experiment2.writeAndClearMeasuredTime(analysis.getName(), numberOfFilters);
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

		final Map<Double, Long> quintiles = StatisticsUtil.calculateQuintiles(measuredTimes);
		for (final Long value : quintiles.values()) {
			fileWriter.write(Long.toString(value));
			fileWriter.write(";");
		}

		fileWriter.write(Long.toString(StatisticsUtil.calculateAverage(measuredTimes)));
		fileWriter.write(";");

		fileWriter.write(Long.toString(StatisticsUtil.calculateConfidenceWidth(measuredTimes)));

		fileWriter.write("\n");
		fileWriter.close();

		measuredTimes.clear();
	}

	private static interface IAnalysis {

		public String getName();

		public void execute() throws Exception;

		public void initialize(int numberOfFilters, int numberOfObjectsToSend) throws Exception;

	}

	private static final class TeeTimeAnalysis extends Analysis implements IAnalysis {

		private static final int SECONDS = 1000;

		private Pipeline pipeline;
		private WorkerThread workerThread;
		private final boolean shouldUseQueue;

		public TeeTimeAnalysis(final boolean shouldUseQueue) {
			this.shouldUseQueue = shouldUseQueue;
		}

		public void initialize(final int numberOfFilters, final int numberOfObjectsToSend) throws Exception {
			@SuppressWarnings("unchecked")
			final NoopFilter<TimestampObject>[] noopFilters = new NoopFilter[numberOfFilters];
			// create stages
			final kieker.panalysis.stage.basic.ObjectProducer<TimestampObject> objectProducer = new kieker.panalysis.stage.basic.ObjectProducer<TimestampObject>(
					numberOfObjectsToSend, new Callable<TimestampObject>() {
						public TimestampObject call() throws Exception {
							return new TimestampObject();
						}
					});
			final kieker.panalysis.stage.StartTimestampFilter startTimestampFilter = new kieker.panalysis.stage.StartTimestampFilter();
			for (int i = 0; i < noopFilters.length; i++) {
				noopFilters[i] = new NoopFilter<TimestampObject>();
			}
			final kieker.panalysis.stage.StopTimestampFilter stopTimestampFilter = new kieker.panalysis.stage.StopTimestampFilter();
			final kieker.panalysis.stage.CollectorSink<TimestampObject> collectorSink = new kieker.panalysis.stage.CollectorSink<TimestampObject>(
					this.timestampObjects);

			// add each stage to a stage list
			final List<IStage> startStages = new LinkedList<IStage>();
			startStages.add(objectProducer);

			final List<IStage> stages = new LinkedList<IStage>();
			stages.add(objectProducer);
			if (this.shouldUseQueue) {
				stages.add(startTimestampFilter);
				stages.addAll(Arrays.asList(noopFilters));
				stages.add(stopTimestampFilter);
				stages.add(collectorSink);

				// connect stages by pipes
				QueuePipe.connect(objectProducer.outputPort, startTimestampFilter.inputPort);
				QueuePipe.connect(startTimestampFilter.outputPort, noopFilters[0].inputPort);
				for (int i = 1; i < noopFilters.length; i++) {
					QueuePipe.connect(noopFilters[i - 1].outputPort, noopFilters[i].inputPort);
				}
				QueuePipe.connect(noopFilters[noopFilters.length - 1].outputPort, stopTimestampFilter.inputPort);
				QueuePipe.connect(stopTimestampFilter.outputPort, collectorSink.objectInputPort);
			} else {
				// connect stages by pipes
				MethodCallPipe.connect(objectProducer.outputPort, startTimestampFilter.inputPort);
				MethodCallPipe.connect(startTimestampFilter.outputPort, noopFilters[0].inputPort);
				for (int i = 1; i < noopFilters.length; i++) {
					MethodCallPipe.connect(noopFilters[i - 1].outputPort, noopFilters[i].inputPort);
				}
				MethodCallPipe.connect(noopFilters[noopFilters.length - 1].outputPort, stopTimestampFilter.inputPort);
				MethodCallPipe.connect(stopTimestampFilter.outputPort, collectorSink.objectInputPort);
			}

			this.pipeline = new Pipeline();
			this.pipeline.setStartStages(startStages);
			this.pipeline.setStages(stages);
		}

		public String getName() {
			return "TeeTime" + (this.shouldUseQueue ? "-Queues" : "-NoQueues");
		}

		public void execute() {
			super.start();

			this.workerThread.start();
			try {
				this.workerThread.join(60 * SECONDS);
			} catch (final InterruptedException e) {
				e.printStackTrace();
			}
		}

	}

	private static final class KiekerAnalysis implements IAnalysis {

		private IAnalysisController ac;

		public KiekerAnalysis() {}

		public void initialize(final int numberOfFilters, final int numberOfObjectsToSend) throws Exception {
			this.ac = new AnalysisController();

			final Configuration producerConfig = new Configuration();
			producerConfig.setProperty(ObjectProducer.CONFIG_PROPERTY_NAME_OBJECTS_TO_CREATE, Long.toString(numberOfObjectsToSend));
			final ObjectProducer<TimestampObject> producer = new ObjectProducer<TimestampObject>(producerConfig, this.ac, new Callable<TimestampObject>() {
				public TimestampObject call() throws Exception {
					return new TimestampObject();
				}
			});

			final StartTimestampFilter startTimestampFilter = new StartTimestampFilter(new Configuration(), this.ac);
			EmptyPassOnFilter predecessor = new EmptyPassOnFilter(new Configuration(), this.ac);
			this.ac.connect(producer, ObjectProducer.OUTPUT_PORT_NAME, startTimestampFilter, StartTimestampFilter.INPUT_PORT_NAME);
			this.ac.connect(startTimestampFilter, StartTimestampFilter.OUTPUT_PORT_NAME, predecessor, EmptyPassOnFilter.INPUT_PORT_NAME);
			for (int idx = 0; idx < (numberOfFilters - 1); idx++) {
				final EmptyPassOnFilter newPredecessor = new EmptyPassOnFilter(new Configuration(), this.ac);
				this.ac.connect(predecessor, EmptyPassOnFilter.OUTPUT_PORT_NAME, newPredecessor, EmptyPassOnFilter.INPUT_PORT_NAME);
				predecessor = newPredecessor;
			}
			final StopTimestampFilter stopTimestampFilter = new StopTimestampFilter(new Configuration(), this.ac);
			final CollectorSink<TimestampObject> collectorSink = new CollectorSink<TimestampObject>(new Configuration(), this.ac, this.timestampObjects);

			this.ac.connect(predecessor, EmptyPassOnFilter.OUTPUT_PORT_NAME, stopTimestampFilter, StopTimestampFilter.INPUT_PORT_NAME);
			this.ac.connect(stopTimestampFilter, StopTimestampFilter.OUTPUT_PORT_NAME, collectorSink, CollectorSink.INPUT_PORT_NAME);
		}

		public String getName() {
			return "Kieker";
		}

		public void execute() throws Exception {
			this.ac.run();
		}

	}

}
