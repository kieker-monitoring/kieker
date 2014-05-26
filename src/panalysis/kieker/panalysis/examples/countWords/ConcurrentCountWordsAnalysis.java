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
import kieker.panalysis.framework.concurrent.SingleProducerSingleConsumerPipe;
import kieker.panalysis.framework.concurrent.StageTerminationPolicy;
import kieker.panalysis.framework.concurrent.WorkerThread;
import kieker.panalysis.framework.core.Analysis;
import kieker.panalysis.framework.core.IInputPort;
import kieker.panalysis.framework.core.IOutputPort;
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

	private static final int NUM_TOKENS_TO_REPEAT = 1000;
	private static final String START_DIRECTORY_NAME = ".";
	private static final int SECONDS = 1000;

	private static final int MAX_NUM_THREADS = 2;

	private WorkerThread[] ioThreads;
	private WorkerThread[] nonIoThreads;

	ConcurrentWorkStealingPipeFactory<?>[] pipeFactories;
	public static String distributorId;

	@Override
	public void init() {
		super.init();

		this.ioThreads = new WorkerThread[2];

		final IPipeline readerThreadPipeline = this.readerThreadPipeline();
		@SuppressWarnings("unchecked")
		final Distributor<File> distributor = (Distributor<File>) readerThreadPipeline.getStages().get(readerThreadPipeline.getStages().size() - 1);
		this.ioThreads[0] = new WorkerThread(readerThreadPipeline, 1);
		this.ioThreads[0].setName("startThread");
		this.ioThreads[0].terminate(StageTerminationPolicy.TERMINATE_STAGE_AFTER_UNSUCCESSFUL_EXECUTION);

		final IPipeline printingThreadPipeline = this.printingThreadPipeline();
		@SuppressWarnings("unchecked")
		final Merger<Pair<File, Integer>> merger = (Merger<Pair<File, Integer>>) printingThreadPipeline.getStages().get(0);
		this.ioThreads[1] = new WorkerThread(printingThreadPipeline, 2);
		this.ioThreads[1].setName("printingThread");
		this.ioThreads[1].setTerminationPolicy(StageTerminationPolicy.TERMINATE_STAGE_AFTER_UNSUCCESSFUL_EXECUTION);

		this.createPipeFactories();

		int numThreads = Runtime.getRuntime().availableProcessors();
		numThreads = Math.min(MAX_NUM_THREADS, numThreads); // only for testing purposes

		this.nonIoThreads = new WorkerThread[numThreads];
		for (int i = 0; i < this.nonIoThreads.length; i++) {
			final IPipeline pipeline = this.buildNonIoPipeline(distributor, merger);
			this.nonIoThreads[i] = new WorkerThread(pipeline, 0);
			this.nonIoThreads[i].setTerminationPolicy(StageTerminationPolicy.TERMINATE_STAGE_AFTER_UNSUCCESSFUL_EXECUTION);
		}
	}

	@Override
	public void start() {
		super.start();

		for (final WorkerThread thread : this.ioThreads) {
			thread.start();
		}

		for (final WorkerThread thread : this.nonIoThreads) {
			thread.setTerminationPolicy(StageTerminationPolicy.TERMINATE_STAGE_AFTER_UNSUCCESSFUL_EXECUTION);
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

	private void createPipeFactories() {
		this.pipeFactories = new ConcurrentWorkStealingPipeFactory[4];
		this.pipeFactories[0] = new ConcurrentWorkStealingPipeFactory<File>();
		this.pipeFactories[1] = new ConcurrentWorkStealingPipeFactory<File>();
		this.pipeFactories[2] = new ConcurrentWorkStealingPipeFactory<Pair<File, Integer>>();
		this.pipeFactories[3] = new ConcurrentWorkStealingPipeFactory<Pair<File, Integer>>();
	}

	private IPipeline readerThreadPipeline() {
		// create stages
		final RepeaterSource<String> repeaterSource = RepeaterSource.create(START_DIRECTORY_NAME, NUM_TOKENS_TO_REPEAT);
		repeaterSource.setAccessesDeviceId(1);
		final DirectoryName2Files directoryName2Files = new DirectoryName2Files();
		directoryName2Files.setAccessesDeviceId(1);
		final Distributor<File> distributor = new Distributor<File>();
		distributor.setAccessesDeviceId(1);

		// add each stage to a stage list
		final List<IStage> stages = new LinkedList<IStage>();
		stages.add(repeaterSource);
		stages.add(directoryName2Files);
		stages.add(distributor);

		// connect stages by pipes
		QueuePipe.connect(repeaterSource.OUTPUT, directoryName2Files.DIRECTORY_NAME);
		QueuePipe.connect(directoryName2Files.fileOutputPort, distributor.genericInputPort);

		repeaterSource.START.setAssociatedPipe(new MethodCallPipe<Boolean>(Boolean.TRUE));

		final IPipeline pipeline = new IPipeline() {
			@SuppressWarnings("unchecked")
			public List<? extends IStage> getStartStages() {
				return Arrays.asList(repeaterSource);
			}

			public List<IStage> getStages() {
				return stages;
			}

			public void fireStartNotification() throws Exception {
				for (final IStage stage : this.getStartStages()) {
					stage.notifyPipelineStarts();
				}
			}

			public void fireStopNotification() {
				for (final IStage stage : this.getStartStages()) {
					stage.notifyPipelineStops();
				}
			}
		};

		return pipeline;
	}

	private IPipeline buildNonIoPipeline(final Distributor<File> readerDistributor, final Merger<Pair<File, Integer>> printingMerger) {
		// create stages
		final Distributor<File> distributor = new Distributor<File>();
		ConcurrentCountWordsAnalysis.distributorId = distributor.getId();
		final CountWordsStage countWordsStage0 = new CountWordsStage();
		final CountWordsStage countWordsStage1 = new CountWordsStage();
		final Merger<Pair<File, Integer>> merger = new Merger<Pair<File, Integer>>();

		// add each stage to a stage list
		final List<IStage> stages = new LinkedList<IStage>();
		stages.add(distributor);
		stages.add(countWordsStage0);
		stages.add(countWordsStage1);
		stages.add(merger);

		// connect stages by pipes
		SingleProducerSingleConsumerPipe.connect(readerDistributor.getNewOutputPort(), distributor.genericInputPort);

		this.connectWithStealAwarePipe(this.pipeFactories[0], distributor.getNewOutputPort(), countWordsStage0.FILE);
		this.connectWithStealAwarePipe(this.pipeFactories[1], distributor.getNewOutputPort(), countWordsStage1.FILE);
		this.connectWithStealAwarePipe(this.pipeFactories[2], countWordsStage0.WORDSCOUNT, merger.getNewInputPort());
		this.connectWithStealAwarePipe(this.pipeFactories[3], countWordsStage1.WORDSCOUNT, merger.getNewInputPort());

		SingleProducerSingleConsumerPipe.connect(merger.outputPort, printingMerger.getNewInputPort());

		final IPipeline pipeline = new IPipeline() {
			@SuppressWarnings("unchecked")
			public List<? extends IStage> getStartStages() {
				return Arrays.asList(distributor);
			}

			public List<IStage> getStages() {
				return stages;
			}

			public void fireStartNotification() throws Exception {
				for (final IStage stage : this.getStartStages()) {
					stage.notifyPipelineStarts();
				}
			}

			public void fireStopNotification() {
				for (final IStage stage : this.getStartStages()) {
					stage.notifyPipelineStops();
				}
			}
		};

		return pipeline;
	}

	private IPipeline printingThreadPipeline() {
		// create stages
		final Merger<Pair<File, Integer>> merger = new Merger<Pair<File, Integer>>();
		merger.setAccessesDeviceId(2);
		final OutputWordsCountSink outputWordsCountStage = new OutputWordsCountSink();
		outputWordsCountStage.setAccessesDeviceId(2);

		// add each stage to a stage list
		final List<IStage> stages = new LinkedList<IStage>();
		stages.add(merger);
		stages.add(outputWordsCountStage);

		// connect stages by pipes
		QueuePipe.connect(merger.outputPort, outputWordsCountStage.fileWordcountTupleInputPort);

		final IPipeline pipeline = new IPipeline() {
			@SuppressWarnings("unchecked")
			public List<? extends IStage> getStartStages() {
				return Arrays.asList(merger);
			}

			public List<IStage> getStages() {
				return stages;
			}

			public void fireStartNotification() throws Exception {
				for (final IStage stage : this.getStartStages()) {
					stage.notifyPipelineStarts();
				}
			}

			public void fireStopNotification() {
				for (final IStage stage : this.getStartStages()) {
					stage.notifyPipelineStops();
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
		//
		// --- Thread[startThread,5,] ---
		// {RepeaterSource: numPushedElements={numPushedElements=1000, numTakenElements=1}}
		// {DirectoryName2Files: numPushedElements={numPushedElements=16000, numTakenElements=1000}}
		// {Distributor: numPushedElements={numPushedElements=16000, numTakenElements=16000}}
		// --- Thread[Thread-2,5,] ---
		// {Distributor: numPushedElements={numPushedElements=16008, numTakenElements=16008}}
		//
		// cause: the non-io distributor is executed faster than the io distributor

		for (final WorkerThread thread : this.ioThreads) {
			System.out.println("--- " + thread + " ---"); // NOPMD (Just for example purposes)
			for (final IStage stage : thread.getPipeline().getStages()) {
				System.out.println(stage); // NOPMD (Just for example purposes)
			}
		}

		for (final WorkerThread thread : this.nonIoThreads) {
			System.out.println("--- " + thread + " ---"); // NOPMD (Just for example purposes)
			for (final IStage stage : thread.getPipeline().getStages()) {
				System.out.println(stage); // NOPMD (Just for example purposes)
			}

			final long duration = thread.getDuration();
			if (duration > maxDuration) {
				maxDuration = duration;
				maxThread = thread;
			}
		}

		System.out.println("maxThread: " + maxThread.toString() + " takes " + maxDuration + " ms"); // NOPMD (Just for example purposes)
	}
}
