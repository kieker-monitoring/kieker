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
package kieker.panalysis.examples.throughput;

import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

import kieker.panalysis.framework.concurrent.StageTerminationPolicy;
import kieker.panalysis.framework.concurrent.WorkerThread;
import kieker.panalysis.framework.core.Analysis;
import kieker.panalysis.framework.core.IPipeline;
import kieker.panalysis.framework.core.IStage;
import kieker.panalysis.framework.core.Pipeline;
import kieker.panalysis.framework.sequential.MethodCallPipe;
import kieker.panalysis.framework.sequential.QueuePipe;
import kieker.panalysis.stage.CollectorSink;
import kieker.panalysis.stage.NoopFilter;
import kieker.panalysis.stage.StartTimestampFilter;
import kieker.panalysis.stage.StopTimestampFilter;
import kieker.panalysis.stage.basic.ObjectProducer;

/**
 * @author Christian Wulf
 * 
 * @since 1.10
 */
public class ThroughputTimestampAnalysis extends Analysis {

	private static final int SECONDS = 1000;

	private WorkerThread workerThread;

	private int numNoopFilters;

	private int numInputObjects;

	private Callable<TimestampObject> inputObjectCreator;

	private Collection<TimestampObject> timestampObjects;

	private boolean shouldUseQueue;

	@Override
	public void init() {
		super.init();
		final IPipeline pipeline = this.buildPipeline(this.numNoopFilters);

		this.workerThread = new WorkerThread(pipeline, 0);
		this.workerThread.terminate(StageTerminationPolicy.TERMINATE_STAGE_AFTER_UNSUCCESSFUL_EXECUTION);
	}

	/**
	 * @param numNoopFilters
	 * @since 1.10
	 */
	private IPipeline buildPipeline(final int numNoopFilters) {
		@SuppressWarnings("unchecked")
		final NoopFilter<TimestampObject>[] noopFilters = new NoopFilter[numNoopFilters];
		// create stages
		final ObjectProducer<TimestampObject> objectProducer = new ObjectProducer<TimestampObject>(this.numInputObjects, this.inputObjectCreator);
		final StartTimestampFilter startTimestampFilter = new StartTimestampFilter();
		for (int i = 0; i < noopFilters.length; i++) {
			noopFilters[i] = new NoopFilter<TimestampObject>();
		}
		final StopTimestampFilter stopTimestampFilter = new StopTimestampFilter();
		final CollectorSink<TimestampObject> collectorSink = new CollectorSink<TimestampObject>(this.timestampObjects);

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

		final Pipeline pipeline = new Pipeline();
		pipeline.setStartStages(startStages);
		pipeline.setStages(stages);
		return pipeline;
	}

	@Override
	public void start() {
		super.start();

		this.workerThread.start();
		try {
			this.workerThread.join(60 * SECONDS);
		} catch (final InterruptedException e) {
			e.printStackTrace();
		}

		final long schedulingOverheadInNs = this.workerThread.computeSchedulingOverheadInNs();
		final int size = this.workerThread.getSchedulingOverheadsInNs().size();
		System.out.println("scheduling overhead times: " + size);
		System.out.println("SchedulingOverhead: " + TimeUnit.NANOSECONDS.toMillis(schedulingOverheadInNs) + " ms");
		System.out.println("avg overhead of iteration: "
				+ TimeUnit.NANOSECONDS.toMillis(schedulingOverheadInNs / (size / 2)) + " ms");
		System.out.println("ExecutedUnsuccessfullyCount: " + this.workerThread.getExecutedUnsuccessfullyCount());
	}

	public int getNumNoopFilters() {
		return this.numNoopFilters;
	}

	public void setNumNoopFilters(final int numNoopFilters) {
		this.numNoopFilters = numNoopFilters;
	}

	/**
	 * @since 1.10
	 */
	public void setInput(final int numInputObjects, final Callable<TimestampObject> inputObjectCreator) {
		this.numInputObjects = numInputObjects;
		this.inputObjectCreator = inputObjectCreator;
	}

	public Collection<TimestampObject> getTimestampObjects() {
		return this.timestampObjects;
	}

	public void setTimestampObjects(final Collection<TimestampObject> timestampObjects) {
		this.timestampObjects = timestampObjects;
	}

	public boolean isShouldUseQueue() {
		return this.shouldUseQueue;
	}

	public void setShouldUseQueue(final boolean shouldUseQueue) {
		this.shouldUseQueue = shouldUseQueue;
	}
}
