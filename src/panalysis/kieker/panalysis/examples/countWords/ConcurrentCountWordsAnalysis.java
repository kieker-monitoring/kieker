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
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import de.chw.util.Pair;

import kieker.panalysis.framework.concurrent.ConcurrentWorkStealingPipe;
import kieker.panalysis.framework.concurrent.ConcurrentWorkStealingPipeFactory;
import kieker.panalysis.framework.concurrent.TerminationPolicy;
import kieker.panalysis.framework.concurrent.WorkerThread;
import kieker.panalysis.framework.core.AbstractFilter;
import kieker.panalysis.framework.core.Analysis;
import kieker.panalysis.framework.core.IInputPort;
import kieker.panalysis.framework.core.IOutputPort;
import kieker.panalysis.framework.core.IPipe;
import kieker.panalysis.framework.core.IPipeline;
import kieker.panalysis.framework.core.ISink;
import kieker.panalysis.framework.core.ISource;
import kieker.panalysis.framework.core.IStage;
import kieker.panalysis.framework.sequential.MethodCallPipe;
import kieker.panalysis.framework.sequential.QueuePipe;
import kieker.panalysis.stage.basic.RepeaterSource;
import kieker.panalysis.stage.basic.distributor.Distributor;
import kieker.panalysis.stage.basic.merger.Merger;

/**
 * @author Christian Wulf
 * 
 * @since 1.10
 */
public class ConcurrentCountWordsAnalysis extends Analysis {

	private static final int MAX_NUM_THREADS = 1;
	private static final int NUM_TOKENS_TO_REPEAT = 1000;

	public static final String START_DIRECTORY_NAME = ".";

	private static final int SECONDS = 1000;

	private WorkerThread[] ioThreads;
	private WorkerThread[] nonIoThreads;

	ConcurrentWorkStealingPipeFactory<?>[] pipeFactories;

	@Override
	public void init() {
		super.init();

		this.ioThreads = new WorkerThread[1];

		final IPipeline readerThreadPipeline = this.readerThreadPipeline();
		this.ioThreads[0] = new WorkerThread(readerThreadPipeline, 1);
		this.ioThreads[0].setName("startThread");

		@SuppressWarnings("unchecked")
		final Distributor<File> distributor = (Distributor<File>) readerThreadPipeline.getStages().get(readerThreadPipeline.getStages().size() - 1);

		this.createPipeFactories();

		int numThreads = Runtime.getRuntime().availableProcessors();
		numThreads = Math.min(MAX_NUM_THREADS, numThreads); // only for testing purposes

		this.nonIoThreads = new WorkerThread[numThreads];
		for (int i = 0; i < this.nonIoThreads.length; i++) {
			final IPipeline pipeline = this.buildNonIoPipeline(distributor);
			this.nonIoThreads[i] = new WorkerThread(pipeline, 0);
		}
	}

	private void createPipeFactories() {
		this.pipeFactories = new ConcurrentWorkStealingPipeFactory[5];
		this.pipeFactories[0] = new ConcurrentWorkStealingPipeFactory<File>();
		this.pipeFactories[1] = new ConcurrentWorkStealingPipeFactory<File>();
		this.pipeFactories[2] = new ConcurrentWorkStealingPipeFactory<Pair<File, Integer>>();
		this.pipeFactories[3] = new ConcurrentWorkStealingPipeFactory<Pair<File, Integer>>();
		this.pipeFactories[4] = new ConcurrentWorkStealingPipeFactory<Pair<File, Integer>>();
	}

	private IPipeline readerThreadPipeline() {
		// create stages
		final RepeaterSource<String> repeaterSource = RepeaterSource.create(START_DIRECTORY_NAME, NUM_TOKENS_TO_REPEAT);
		repeaterSource.setAccessesDeviceId(1);
		final DirectoryName2Files findFilesStage = new DirectoryName2Files();
		findFilesStage.setAccessesDeviceId(1);
		final Distributor<File> distributor = new Distributor<File>();
		distributor.setAccessesDeviceId(1);

		// add each stage to a stage list
		final List<IStage> stages = new LinkedList<IStage>();
		stages.add(repeaterSource);
		stages.add(findFilesStage);
		stages.add(distributor);

		// connect stages by pipes
		this.connectWithSequentialPipe(repeaterSource.OUTPUT, findFilesStage.DIRECTORY_NAME);
		this.connectWithSequentialPipe(findFilesStage.FILE, distributor.OBJECT);

		repeaterSource.START.setAssociatedPipe(new MethodCallPipe<Boolean>(Boolean.TRUE));

		final IPipeline pipeline = new IPipeline() {
			@SuppressWarnings("unchecked")
			public List<? extends AbstractFilter<?>> getStartStages() {
				return Arrays.asList(repeaterSource);
			}

			public List<IStage> getStages() {
				return stages;
			}

			public void fireStartNotification() throws Exception {
				// notify each stage
				for (final IStage stage : stages) {
					stage.onPipelineStarts();
				}
				// notify each pipe
				for (final ConcurrentWorkStealingPipeFactory<?> pipeFactory : ConcurrentCountWordsAnalysis.this.pipeFactories) {
					for (final ConcurrentWorkStealingPipe<?> pipe : pipeFactory.getPipes()) {
						pipe.onPipelineStarts();
					}
				}
			}

			public void fireStopNotification() {
				// notify each stage
				for (final IStage stage : stages) {
					stage.onPipelineStops();
				}
				// notify each pipe
				for (final ConcurrentWorkStealingPipeFactory<?> pipeFactory : ConcurrentCountWordsAnalysis.this.pipeFactories) {
					for (final ConcurrentWorkStealingPipe<?> pipe : pipeFactory.getPipes()) {
						pipe.onPipelineStops();
					}
				}
			}
		};

		return pipeline;
	}

	private <A extends ISource, B extends ISink<B>, T> void connectWithSequentialPipe(final IOutputPort<A, T> sourcePort, final IInputPort<B, T> targetPort) {
		final IPipe<T> pipe = new QueuePipe<T>();
		pipe.setSourcePort(sourcePort);
		pipe.setTargetPort(targetPort);
	}

	private IPipeline buildNonIoPipeline(final Distributor<File> readerDistributor) {
		// create stages
		final Distributor<File> distributor = new Distributor<File>();
		final CountWordsStage countWordsStage0 = new CountWordsStage();
		final CountWordsStage countWordsStage1 = new CountWordsStage();
		final Merger<Pair<File, Integer>> merger = new Merger<Pair<File, Integer>>();
		final OutputWordsCountSink outputWordsCountStage = new OutputWordsCountSink();

		// add each stage to a stage list
		final List<IStage> stages = new LinkedList<IStage>();
		stages.add(distributor);
		stages.add(countWordsStage0);
		stages.add(countWordsStage1);
		stages.add(merger);
		stages.add(outputWordsCountStage);

		// connect stages by pipes
		// this.connectWithConcurrentPipe(readerDistributor.getNewOutputPort(), distributor.OBJECT);
		final ConcurrentWorkStealingPipeFactory<File> pf = new ConcurrentWorkStealingPipeFactory<File>();
		this.connectWithStealAwarePipe(pf, readerDistributor.getNewOutputPort(), distributor.OBJECT);

		this.connectWithStealAwarePipe(this.pipeFactories[0], distributor.getNewOutputPort(), countWordsStage0.FILE);
		this.connectWithStealAwarePipe(this.pipeFactories[1], distributor.getNewOutputPort(), countWordsStage1.FILE);
		this.connectWithStealAwarePipe(this.pipeFactories[2], countWordsStage0.WORDSCOUNT, merger.getNewInputPort());
		this.connectWithStealAwarePipe(this.pipeFactories[3], countWordsStage1.WORDSCOUNT, merger.getNewInputPort());
		this.connectWithStealAwarePipe(this.pipeFactories[4], merger.OBJECT, outputWordsCountStage.FILE_WORDCOUNT_TUPLE);

		final IPipeline pipeline = new IPipeline() {
			@SuppressWarnings("unchecked")
			public List<? extends AbstractFilter<?>> getStartStages() {
				return Arrays.asList(distributor);
			}

			public List<IStage> getStages() {
				return stages;
			}

			public void fireStartNotification() throws Exception {
				// notify each stage
				for (final IStage stage : stages) {
					stage.onPipelineStarts();
				}
				// notify each pipe
				for (final ConcurrentWorkStealingPipeFactory<?> pipeFactory : ConcurrentCountWordsAnalysis.this.pipeFactories) {
					for (final ConcurrentWorkStealingPipe<?> pipe : pipeFactory.getPipes()) {
						pipe.onPipelineStarts();
					}
				}
			}

			public void fireStopNotification() {
				// notify each stage
				for (final IStage stage : stages) {
					stage.onPipelineStops();
				}
				// notify each pipe
				for (final ConcurrentWorkStealingPipeFactory<?> pipeFactory : ConcurrentCountWordsAnalysis.this.pipeFactories) {
					for (final ConcurrentWorkStealingPipe<?> pipe : pipeFactory.getPipes()) {
						pipe.onPipelineStops();
					}
				}
			}
		};

		return pipeline;
	}

	private <A extends ISource, B extends ISink<B>, T> void connectWithStealAwarePipe(final ConcurrentWorkStealingPipeFactory<?> pipeFactory,
			final IOutputPort<A, T> sourcePort, final IInputPort<B, T> targetPort) {
		@SuppressWarnings("unchecked")
		final ConcurrentWorkStealingPipe<T> pipe = (ConcurrentWorkStealingPipe<T>) pipeFactory.create();
		pipe.setSourcePort(sourcePort);
		pipe.setTargetPort(targetPort);
	}

	@Override
	public void start() {
		super.start();

		for (final WorkerThread thread : this.ioThreads) {
			// thread.setTerminationPolicy(TerminationPolicy.TERMINATE_STAGE_AFTER_UNSUCCESSFUL_EXECUTION);
			thread.terminate(TerminationPolicy.TERMINATE_STAGE_AFTER_UNSUCCESSFUL_EXECUTION);
			thread.start();
		}

		for (final WorkerThread thread : this.nonIoThreads) {
			thread.setTerminationPolicy(TerminationPolicy.TERMINATE_STAGE_AFTER_UNSUCCESSFUL_EXECUTION);
			thread.start();
		}

		System.out.println("Waiting for the I/O worker threads to terminate..."); // NOPMD (Just for example purposes)
		for (final WorkerThread thread : this.ioThreads) {
			try {
				thread.join(60 * SECONDS);
			} catch (final InterruptedException e) {
				throw new IllegalStateException();
			}
		}

		System.out.println("Waiting for the non I/O worker threads to terminate..."); // NOPMD (Just for example purposes)
		for (final WorkerThread thread : this.nonIoThreads) {
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

		for (final WorkerThread thread : this.nonIoThreads) {
			System.out.println("--- " + thread + " ---"); // NOPMD (Just for example purposes)
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
