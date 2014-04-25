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

import kieker.panalysis.framework.concurrent.ConcurrentPipeline;
import kieker.panalysis.framework.concurrent.ConcurrentWorkStealingPipe;
import kieker.panalysis.framework.concurrent.TerminationPolicy;
import kieker.panalysis.framework.concurrent.WorkerThread;
import kieker.panalysis.framework.core.Analysis;
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
		final Distributor<File> distributor = (Distributor<File>) lastStage;

		int numThreads = Runtime.getRuntime().availableProcessors();
		numThreads = 1; // only for testing purposes

		this.threads = new WorkerThread[numThreads + 1];

		this.threads[0] = new WorkerThread();
		this.threads[0].setPipeline(mainThreadPipeline);
		this.threads[0].setName("startThread");

		final Map<Integer, List<ConcurrentWorkStealingPipe<?>>> pipeGroups = new HashMap<Integer, List<ConcurrentWorkStealingPipe<?>>>();

		for (int i = 1; i < this.threads.length; i++) {
			final Pipeline<ConcurrentWorkStealingPipe<?>> pipeline = Pipeline.create(pipeGroups);
			this.buildPipeline(distributor, pipeline);

			final WorkerThread thread = new WorkerThread();
			thread.setPipeline(pipeline);
			this.threads[i] = thread;
		}

		for (final WorkerThread thread : this.threads) {
			thread.getPipeline().connectPipeGroups();
		}

	}

	private Pipeline<?> mainThreadPipeline() {
		final RepeaterSource<String> repeaterSource = RepeaterSource.create(START_DIRECTORY_NAME, 40);
		final DirectoryName2Files findFilesStage = new DirectoryName2Files();
		final Distributor<File> distributor = new Distributor<File>();

		new MethodCallPipe<String>().setSourcePort(repeaterSource.OUTPUT).setTargetPort(findFilesStage.DIRECTORY_NAME);
		new MethodCallPipe<File>().setSourcePort(findFilesStage.FILE).setTargetPort(distributor.OBJECT);

		final Pipeline<?> pipeline = Pipeline.create();
		pipeline.addStage(repeaterSource);
		pipeline.addStage(findFilesStage);
		pipeline.addStage(distributor);

		pipeline.setStartStages(repeaterSource);

		repeaterSource.START.setAssociatedPipe(new MethodCallPipe<Boolean>(Boolean.TRUE));

		return pipeline;
	}

	private void buildPipeline(final Distributor<File> readerDistributor, final ConcurrentPipeline pipeline) {
		// create stages
		final Distributor<File> distributor = new Distributor<File>();
		final CountWordsStage countWordsStage0 = new CountWordsStage();
		final CountWordsStage countWordsStage1 = new CountWordsStage();
		final Merger<Pair<File, Integer>> merger = new Merger<Pair<File, Integer>>();
		final OutputWordsCountSink outputWordsCountStage = new OutputWordsCountSink();

		pipeline.setStartStages(distributor);

		// connect stages by pipes
		pipeline.connect(readerDistributor.getNewOutputPort(), distributor.OBJECT);
		pipeline.connect(distributor.getNewOutputPort(), countWordsStage0.FILE);
		pipeline.connect(distributor.getNewOutputPort(), countWordsStage1.FILE);
		pipeline.connect(countWordsStage0.WORDSCOUNT, merger.getNewInputPort());
		pipeline.connect(countWordsStage1.WORDSCOUNT, merger.getNewInputPort());
		pipeline.connect(merger.OBJECT, outputWordsCountStage.FILE_WORDCOUNT_TUPLE);

		pipeline.init();
	}

	@Override
	public void start() {
		super.start();

		this.threads[0].terminate(TerminationPolicy.TERMINATE_STAGE_AFTER_UNSUCCESSFUL_EXECUTION);
		// this.threads[0].start();

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
