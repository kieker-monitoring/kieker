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
import kieker.panalysis.stage.basic.RepeaterSource;
import kieker.panalysis.stage.basic.distributor.Distributor;
import kieker.panalysis.stage.basic.merger.Merger;

/**
 * @author Christian Wulf
 * 
 * @since 1.10
 */
public class CountWordsAnalysis extends Analysis {

	private static final int NUM_TOKENS_TO_REPEAT = 1000;

	private IPipeline pipeline;

	@Override
	public void init() {
		super.init();

		this.pipeline = this.buildNonIoPipeline();
	}

	private IPipeline buildNonIoPipeline() {
		// create stages
		final RepeaterSource<String> repeaterSource = RepeaterSource.create(".", NUM_TOKENS_TO_REPEAT);
		final DirectoryName2Files findFilesStage = new DirectoryName2Files();
		final Distributor<File> distributor = new Distributor<File>();
		final CountWordsStage countWordsStage0 = new CountWordsStage();
		final CountWordsStage countWordsStage1 = new CountWordsStage();
		final Merger<Pair<File, Integer>> merger = new Merger<Pair<File, Integer>>();
		final OutputWordsCountSink outputWordsCountStage = new OutputWordsCountSink();

		// add each stage to a stage list
		final List<IStage> stages = new LinkedList<IStage>();
		stages.add(repeaterSource);
		stages.add(findFilesStage);
		stages.add(distributor);
		stages.add(countWordsStage0);
		stages.add(countWordsStage1);
		stages.add(merger);
		stages.add(outputWordsCountStage);

		final IPipe<?>[] pipes = new IPipe[7];
		// connect stages by pipes
		pipes[0] = this.connectWithSequentialPipe(repeaterSource.OUTPUT, findFilesStage.DIRECTORY_NAME);
		pipes[1] = this.connectWithSequentialPipe(findFilesStage.FILE, distributor.OBJECT);
		pipes[2] = this.connectWithSequentialPipe(distributor.getNewOutputPort(), countWordsStage0.FILE);
		pipes[3] = this.connectWithSequentialPipe(distributor.getNewOutputPort(), countWordsStage1.FILE);
		pipes[4] = this.connectWithSequentialPipe(countWordsStage0.WORDSCOUNT, merger.getNewInputPort());
		pipes[5] = this.connectWithSequentialPipe(countWordsStage1.WORDSCOUNT, merger.getNewInputPort());
		pipes[6] = this.connectWithSequentialPipe(merger.OBJECT, outputWordsCountStage.FILE_WORDCOUNT_TUPLE);

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
				for (final IPipe<?> pipe : pipes) {
					pipe.onPipelineStarts();
				}
			}

			public void fireStopNotification() {
				// notify each stage
				for (final IStage stage : stages) {
					stage.onPipelineStops();
				}
				// notify each pipe
				for (final IPipe<?> pipe : pipes) {
					pipe.onPipelineStops();
				}
			}
		};

		return pipeline;
	}

	private <A extends ISource, B extends ISink<B>, T> IPipe<T> connectWithSequentialPipe(final IOutputPort<A, T> sourcePort,
			final IInputPort<B, T> targetPort) {
		final IPipe<T> pipe = new MethodCallPipe<T>();
		pipe.setSourcePort(sourcePort);
		pipe.setTargetPort(targetPort);
		return pipe;
	}

	@Override
	public void start() {
		super.start();
		try {
			this.pipeline.fireStartNotification();
		} catch (final Exception e) {
			e.printStackTrace();
		}

		this.pipeline.getStartStages().get(0).execute();

		try {
			this.pipeline.fireStopNotification();
		} catch (final Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(final String[] args) {
		final CountWordsAnalysis analysis = new CountWordsAnalysis();
		analysis.init();
		final long start = System.currentTimeMillis();
		analysis.start();
		final long end = System.currentTimeMillis();
		// analysis.terminate();
		final long duration = end - start;
		System.out.println("duration: " + duration + " ms"); // NOPMD (Just for example purposes)

		for (final IStage stage : analysis.pipeline.getStages()) {
			if (stage instanceof AbstractFilter<?>) {
				System.out.println(stage.getClass().getName() + ": " + ((AbstractFilter<?>) stage).getOverallDuration()); // NOPMD (Just for example purposes)
			}
		}

		final DirectoryName2Files findFilesStage = (DirectoryName2Files) analysis.pipeline.getStages().get(1);
		System.out.println("findFilesStage: " + findFilesStage.getNumFiles()); // NOPMD (Just for example purposes)

		final OutputWordsCountSink outputWordsCountStage = (OutputWordsCountSink) analysis.pipeline.getStages().get(6);
		System.out.println("outputWordsCountStage: " + outputWordsCountStage.getNumFiles()); // NOPMD (Just for example purposes)
	}
}
