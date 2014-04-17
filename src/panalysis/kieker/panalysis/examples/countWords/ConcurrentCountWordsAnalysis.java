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

package kieker.panalysis.examples.countWords;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.chw.util.Pair;

import kieker.panalysis.framework.concurrent.ConcurrentWorkStealingPipe;
import kieker.panalysis.framework.concurrent.SingleProducerSingleConsumerPipe;
import kieker.panalysis.framework.concurrent.TerminationPolicy;
import kieker.panalysis.framework.concurrent.WorkerThread;
import kieker.panalysis.framework.core.AbstractFilter;
import kieker.panalysis.framework.core.Analysis;
import kieker.panalysis.framework.core.IInputPort;
import kieker.panalysis.framework.core.IOutputPort;
import kieker.panalysis.framework.core.ISink;
import kieker.panalysis.framework.core.ISource;
import kieker.panalysis.framework.core.IStage;
import kieker.panalysis.framework.sequential.MethodCallPipe;
import kieker.panalysis.framework.sequential.Pipeline;
import kieker.panalysis.stage.basic.Distributor;
import kieker.panalysis.stage.basic.Merger;
import kieker.panalysis.stage.basic.RepeaterSource;

/**
 * @author Christian Wulf
 * 
 * @since 1.10
 */
public class ConcurrentCountWordsAnalysis extends Analysis {

	public static final String START_DIRECTORY_NAME = ".";

	private static final int SECONDS = 1000;

	private WorkerThread[] threads;

	@Override
	public void init() {
		super.init();

		final Pipeline<?> mainThreadPipeline = this.mainThreadPipeline();
		final IStage lastStage = mainThreadPipeline.getStages().get(mainThreadPipeline.getStages().size() - 1);
		final Distributor<?> distributor = (Distributor<?>) lastStage;

		int numThreads = Runtime.getRuntime().availableProcessors();
		numThreads = 4; // only for testing purposes

		this.threads = new WorkerThread[numThreads + 1];

		this.threads[0] = new WorkerThread();
		this.threads[0].setPipeline(mainThreadPipeline);
		this.threads[0].setName("startThread");

		final Map<Integer, List<ConcurrentWorkStealingPipe<?>>> pipeGroups = new HashMap<Integer, List<ConcurrentWorkStealingPipe<?>>>();

		for (int i = 1; i < this.threads.length; i++) {
			final Pipeline<ConcurrentWorkStealingPipe<?>> pipeline = Pipeline.create(pipeGroups);
			this.buildPipeline(mainThreadPipeline, pipeline);

			final WorkerThread thread = new WorkerThread();
			thread.setPipeline(pipeline);
			this.threads[i] = thread;

			for (final AbstractFilter<?> startStage : pipeline.getStartStages()) {
				// for (final IInputPort<?, ?> inputPort : startStage.getInputPorts())
				final IInputPort<?, ?> inputPort = startStage.getInputPorts().get(0);
				{
					new SingleProducerSingleConsumerPipe<Object>()
							.source((IOutputPort<? extends ISource, Object>) distributor.getNewOutputPort())
							.target((IInputPort<? extends ISink, Object>) inputPort);
				}
			}
		}

		for (final WorkerThread thread : this.threads) {
			thread.getPipeline().connectPipeGroups();
		}

	}

	private Pipeline<?> mainThreadPipeline() {
		final RepeaterSource<String> repeaterSource = RepeaterSource.create(START_DIRECTORY_NAME, 4000);
		final DirectoryName2Files findFilesStage = new DirectoryName2Files();
		final Distributor<File> distributor = new Distributor<File>();

		new MethodCallPipe<String>().source(repeaterSource.OUTPUT).target(findFilesStage.DIRECTORY_NAME);
		new MethodCallPipe<File>().source(findFilesStage.FILE).target(distributor.OBJECT);

		final Pipeline<?> pipeline = Pipeline.create();
		pipeline.addStage(repeaterSource);
		pipeline.addStage(findFilesStage);
		pipeline.addStage(distributor);

		pipeline.setStartStages(repeaterSource);

		return pipeline;
	}

	private void buildPipeline(final Pipeline<?> mainThreadDistributor, final Pipeline<ConcurrentWorkStealingPipe<?>> pipeline) {
		// create stages
		final Distributor<File> distributor = pipeline.addStage(new Distributor<File>());
		final CountWordsStage countWordsStage0 = pipeline.addStage(new CountWordsStage());
		final CountWordsStage countWordsStage1 = pipeline.addStage(new CountWordsStage());
		final Merger<Pair<File, Integer>> merger = pipeline.addStage(new Merger<Pair<File, Integer>>());
		final OutputWordsCountSink outputWordsCountStage = pipeline.addStage(new OutputWordsCountSink());
		// TODO consider to use: pipeline.add(stage).asStartStage().assignUniqueId()

		pipeline.setStartStages(distributor);

		// connect stages by pipes
		pipeline.add(new ConcurrentWorkStealingPipe<File>()
				.source(distributor.getNewOutputPort())
				.target(countWordsStage0, countWordsStage0.FILE)
				).toGroup(2);

		pipeline.add(new ConcurrentWorkStealingPipe<File>()
				.source(distributor.getNewOutputPort())
				.target(countWordsStage1, countWordsStage1.FILE)
				).toGroup(3);

		pipeline.add(new ConcurrentWorkStealingPipe<Pair<File, Integer>>()
				.source(countWordsStage0.WORDSCOUNT)
				.target(merger, merger.getNewInputPort())
				).toGroup(4);

		pipeline.add(new ConcurrentWorkStealingPipe<Pair<File, Integer>>()
				.source(countWordsStage1.WORDSCOUNT)
				.target(merger, merger.getNewInputPort())
				).toGroup(5);

		pipeline.add(new ConcurrentWorkStealingPipe<Pair<File, Integer>>()
				.source(merger.OBJECT)
				.target(outputWordsCountStage, outputWordsCountStage.FILE_WORDCOUNT_TUPLE)
				).toGroup(6);
	}

	@Override
	public void start() {
		super.start();

		this.threads[0].terminate(TerminationPolicy.TERMINATE_STAGE_AFTER_EXECUTION);

		for (final WorkerThread thread : this.threads) {
			thread.start();
		}

		try {
			this.threads[0].join(60 * SECONDS);
		} catch (final InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		System.out.println("Waiting for the worker threads to terminate..."); // NOPMD (Just for example purposes)
		for (int i = 1; i < this.threads.length; i++) {
			final WorkerThread thread = this.threads[i];
			thread.setTerminationPolicy(TerminationPolicy.TERMINATE_STAGE_AFTER_UNSUCCESSFUL_EXECUTION);
			try {
				thread.join(60 * SECONDS);
			} catch (final InterruptedException e) {
				throw new IllegalStateException();
			}
		}

		System.out.println("Analysis finished."); // NOPMD (Just for example purposes)
	}

	/**
	 * @since 1.10
	 */
	public static void main(final String[] args) {
		final ConcurrentCountWordsAnalysis analysis = new ConcurrentCountWordsAnalysis();
		analysis.init();
		final long start = System.currentTimeMillis();
		analysis.start();
		final long end = System.currentTimeMillis();
		// analysis.terminate();
		final long duration = end - start;
		System.out.println("duration: " + duration + " ms"); // NOPMD (Just for example purposes)

		analysis.analyzeThreads();
	}

	private void analyzeThreads() {
		long maxDuration = -1;
		WorkerThread maxThread = null;

		// FIXME resolve bug; see analysis results below;
		// solution: use a generic distributor to distribute between the threads' start stages
		// {RepeaterSource: numPushedElements=4000, numTakenElements=0}
		// {DirectoryName2Files: numPushedElements=59985, numTakenElements=3999}

		for (int i = 1; i < this.threads.length; i++) {
			final WorkerThread thread = this.threads[i];

			for (final IStage stage : thread.getPipeline().getStages()) {
				System.out.println(stage); // NOPMD (Just for example purposes)
			}

			// System.out.println("findFilesStage: " + ((DirectoryName2Files) thread.getStages().get(0)).getNumFiles()); // NOPMD (Just for example purposes)

			final OutputWordsCountSink sink = (OutputWordsCountSink) thread.getPipeline().getStages().get(4);
			System.out.println("outputWordsCountStage: " + sink.getNumFiles()); // NOPMD (Just for example purposes)

			final long duration = thread.getDuration();
			if (duration > maxDuration) {
				maxDuration = duration;
				maxThread = thread;
			}
		}

		System.out.println("maxThread: " + maxThread.toString() + " takes " + maxDuration + " ms"); // NOPMD (Just for example purposes)
	}
}
