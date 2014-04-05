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

package kieker.panalysis.examples.wordcount;

import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Set;

import kieker.panalysis.Distributor;
import kieker.panalysis.Merger;
import kieker.panalysis.RepeaterSource;
import kieker.panalysis.base.Analysis;
import kieker.panalysis.base.Pipeline;
import kieker.panalysis.concurrent.ConcurrentWorkStealingPipe;
import kieker.panalysis.concurrent.WorkerThread;

/**
 * @author Christian Wulf
 * 
 * @since 1.10
 */
public class ConcurrentCountWordsAnalysis extends Analysis {

	public static final String START_DIRECTORY_NAME = ".";

	private RepeaterSource repeaterSource;

	private WorkerThread[] threads;

	public ConcurrentCountWordsAnalysis() {
		// No code necessary
	}

	@Override
	public void init() {
		super.init();

		int numThreads = Runtime.getRuntime().availableProcessors();

		final ConcurrentWorkStealingPipe[][] pipes = new ConcurrentWorkStealingPipe[7][numThreads];
		for (int i = 0; i < pipes.length; i++) {
			for (int j = 0; j < pipes[i].length; j++) {
				final ConcurrentWorkStealingPipe pipe = new ConcurrentWorkStealingPipe();
				pipes[i][j] = pipe;
			}
		}

		for (final ConcurrentWorkStealingPipe[] pipe : pipes) {
			for (final ConcurrentWorkStealingPipe element : pipe) {
				final Set<ConcurrentWorkStealingPipe> pipesAsSet = new LinkedHashSet<ConcurrentWorkStealingPipe>(Arrays.asList(pipe));
				element.copyAllOtherPipes(pipesAsSet);
			}
		}

		this.repeaterSource = new RepeaterSource(START_DIRECTORY_NAME, 2000);
		this.repeaterSource.setId(99);

		numThreads = 2;
		this.createThreads(pipes, numThreads);
	}

	private void createThreads(final ConcurrentWorkStealingPipe[][] pipes, final int numThreads) {
		this.threads = new WorkerThread[numThreads];
		for (int i = 0; i < this.threads.length; i++) {

			final DirectoryName2Files findFilesStage = new DirectoryName2Files();
			final Distributor distributor = new Distributor();
			final CountWordsStage countWordsStage0 = new CountWordsStage();
			final CountWordsStage countWordsStage1 = new CountWordsStage();
			final Merger merger = new Merger();
			final OutputWordsCountSink outputWordsCountStage = new OutputWordsCountSink();

			final Pipeline pipeline = new Pipeline();
			pipeline.addStage(findFilesStage);
			pipeline.addStage(distributor);
			pipeline.addStage(countWordsStage0);
			pipeline.addStage(countWordsStage1);
			pipeline.addStage(merger);
			pipeline.addStage(outputWordsCountStage);

			int pipeIndex = 0;
			pipes[pipeIndex++][i].connect(this.repeaterSource, RepeaterSource.OUTPUT_PORT.OUTPUT, findFilesStage,
					DirectoryName2Files.INPUT_PORT.DIRECTORY_NAME);
			pipes[pipeIndex++][i].connect(findFilesStage, DirectoryName2Files.OUTPUT_PORT.FILE, distributor, Distributor.INPUT_PORT.OBJECT);

			pipes[pipeIndex++][i].connect(distributor, Distributor.OUTPUT_PORT.OUTPUT0, countWordsStage0,
					CountWordsStage.INPUT_PORT.FILE);
			pipes[pipeIndex++][i].connect(distributor, Distributor.OUTPUT_PORT.OUTPUT1, countWordsStage1,
					CountWordsStage.INPUT_PORT.FILE);

			pipes[pipeIndex++][i].connect(countWordsStage0, CountWordsStage.OUTPUT_PORT.WORDSCOUNT, merger,
					Merger.INPUT_PORT.INPUT0);
			pipes[pipeIndex++][i].connect(countWordsStage1, CountWordsStage.OUTPUT_PORT.WORDSCOUNT, merger,
					Merger.INPUT_PORT.INPUT1);

			pipes[pipeIndex++][i].connect(merger, Merger.OUTPUT_PORT.OBJECT, outputWordsCountStage,
					OutputWordsCountSink.INPUT_PORT.FILE_WORDCOUNT_TUPLE);

			final WorkerThread thread = new WorkerThread();
			thread.setStages(pipeline.getStages());

			this.threads[i] = thread;
		}
	}

	@Override
	public void start() {
		super.start();

		this.repeaterSource.execute();

		for (final WorkerThread thread : this.threads) {
			thread.start();
		}

		for (final WorkerThread thread : this.threads) {
			try {
				thread.join(60 * 1000);
			} catch (final InterruptedException e) {
				throw new IllegalStateException();
			}
		}
	}

	public static void main(final String[] args) {
		final ConcurrentCountWordsAnalysis analysis = new ConcurrentCountWordsAnalysis();
		analysis.init();
		final long start = System.currentTimeMillis();
		analysis.start();
		final long end = System.currentTimeMillis();
		// analysis.terminate();
		final long duration = end - start;
		System.out.println("duration: " + duration + " ms");// NOPMD (Just for example purposes)

		ConcurrentCountWordsAnalysis.analyzeThreads(analysis);
	}

	private static void analyzeThreads(final ConcurrentCountWordsAnalysis analysis) {
		long maxDuration = -1;
		WorkerThread maxThread = null;

		for (final WorkerThread thread : analysis.threads) {
			// System.out.println("findFilesStage: " + ((DirectoryName2Files) thread.getStages().get(0)).getNumFiles()); // NOPMD (Just for example purposes)
			System.out.println("outputWordsCountStage: " + ((OutputWordsCountSink) thread.getStages().get(5)).getNumFiles()); // NOPMD (Just for example purposes)
			final long duration = thread.getDuration();
			if (duration > maxDuration) {
				maxDuration = duration;
				maxThread = thread;
			}
		}

		// System.out.println("maxThread: " + maxThread.toString() + " takes " + maxDuration + " ms");
	}
}
