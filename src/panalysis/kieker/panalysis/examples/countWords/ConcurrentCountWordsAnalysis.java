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

import kieker.panalysis.base.Analysis;
import kieker.panalysis.base.IStage;
import kieker.panalysis.base.Pipeline;
import kieker.panalysis.base.TerminationPolicy;
import kieker.panalysis.concurrent.ConcurrentWorkStealingPipe;
import kieker.panalysis.concurrent.WorkerThread;
import kieker.panalysis.stage.Distributor;
import kieker.panalysis.stage.Merger;
import kieker.panalysis.stage.RepeaterSource;

/**
 * @author Christian Wulf
 * 
 * @since 1.10
 */
public class ConcurrentCountWordsAnalysis extends Analysis {

	public static final String START_DIRECTORY_NAME = ".";

	private static final int SECONDS = 1000;

	private RepeaterSource<String> repeaterSource;
	private WorkerThread[] threads;

	@Override
	public void init() {
		super.init();

		this.repeaterSource = RepeaterSource.create(START_DIRECTORY_NAME, 4000);
		this.repeaterSource.setId(99);

		int numThreads = Runtime.getRuntime().availableProcessors();
		numThreads = 1; // only for testing purposes

		this.threads = new WorkerThread[numThreads];
		final Map<Integer, List<ConcurrentWorkStealingPipe<?>>> pipeGroups = new HashMap<Integer, List<ConcurrentWorkStealingPipe<?>>>();

		for (int i = 0; i < this.threads.length; i++) {
			final Pipeline<ConcurrentWorkStealingPipe<?>> pipeline = Pipeline.create(pipeGroups);
			this.buildPipeline(pipeline);

			final WorkerThread thread = new WorkerThread();
			thread.setPipeline(pipeline);
			this.threads[i] = thread;
		}

		for (final WorkerThread thread : this.threads) {
			thread.getPipeline().connectPipeGroups();
		}

	}

	private void buildPipeline(final Pipeline<ConcurrentWorkStealingPipe<?>> pipeline) {
		// create stages
		final RepeaterSource<String> repeater = this.repeaterSource;
		final DirectoryName2Files findFilesStage = pipeline.addStage(new DirectoryName2Files());
		final int numBranches = 2;
		final Distributor<File> distributor = pipeline.addStage(new Distributor<File>(numBranches));
		final CountWordsStage countWordsStage0 = pipeline.addStage(new CountWordsStage());
		final CountWordsStage countWordsStage1 = pipeline.addStage(new CountWordsStage());
		final Merger<Pair<File, Integer>> merger = pipeline.addStage(new Merger<Pair<File, Integer>>(numBranches));
		final OutputWordsCountSink outputWordsCountStage = pipeline.addStage(new OutputWordsCountSink());
		// TODO consider to use: pipeline.add(stage).asStartStage().assignUniqueId()

		pipeline.setStartStages(findFilesStage);

		// connect stages by pipes
		pipeline.add(new ConcurrentWorkStealingPipe<String>()
				.source(repeater.OUTPUT)
				.target(findFilesStage, findFilesStage.DIRECTORY_NAME)
				).toGroup(0);

		pipeline.add(new ConcurrentWorkStealingPipe<File>()
				.source(findFilesStage.FILE)
				.target(distributor, distributor.OBJECT)
				).toGroup(1);

		pipeline.add(new ConcurrentWorkStealingPipe<File>()
				.source(distributor.getOutputPort(0))
				.target(countWordsStage0, countWordsStage0.FILE)
				).toGroup(2);

		pipeline.add(new ConcurrentWorkStealingPipe<File>()
				.source(distributor.getOutputPort(1))
				.target(countWordsStage1, countWordsStage1.FILE)
				).toGroup(3);

		pipeline.add(new ConcurrentWorkStealingPipe<Pair<File, Integer>>()
				.source(countWordsStage0.WORDSCOUNT)
				.target(merger, merger.getInputPort(0))
				).toGroup(4);

		pipeline.add(new ConcurrentWorkStealingPipe<Pair<File, Integer>>()
				.source(countWordsStage1.WORDSCOUNT)
				.target(merger, merger.getInputPort(1))
				).toGroup(5);

		pipeline.add(new ConcurrentWorkStealingPipe<Pair<File, Integer>>()
				.source(merger.OBJECT)
				.target(outputWordsCountStage, outputWordsCountStage.FILE_WORDCOUNT_TUPLE)
				).toGroup(6);
	}

	@Override
	public void start() {
		super.start();

		for (final WorkerThread thread : this.threads) {
			thread.start();
		}

		this.repeaterSource.execute();

		System.out.println("Waiting for the worker threads to terminate..."); // NOPMD (Just for example purposes)
		for (final WorkerThread thread : this.threads) {
			thread.terminate(TerminationPolicy.TERMINATE_STAGE_AFTER_UNSUCCESSFUL_EXECUTION);
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

		System.out.println(this.repeaterSource); // NOPMD (Just for example purposes)
		System.out.println(this.repeaterSource.OUTPUT.getAssociatedPipe()); // NOPMD (Just for example purposes)

		// FIXME resolve bug; see analysis results below;
		// solution: use a generic distributor to distribute between the threads' start stages
		// {RepeaterSource: numPushedElements=4000, numTakenElements=0}
		// {DirectoryName2Files: numPushedElements=59985, numTakenElements=3999}

		for (final WorkerThread thread : this.threads) {
			for (final IStage stage : thread.getPipeline().getStages()) {
				System.out.println(stage); // NOPMD (Just for example purposes)
			}

			// System.out.println("findFilesStage: " + ((DirectoryName2Files) thread.getStages().get(0)).getNumFiles()); // NOPMD (Just for example purposes)

			final OutputWordsCountSink sink = (OutputWordsCountSink) thread.getPipeline().getStages().get(5);
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
