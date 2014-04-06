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

import kieker.panalysis.Distributor;
import kieker.panalysis.Merger;
import kieker.panalysis.MethodCallPipe;
import kieker.panalysis.RepeaterSource;
import kieker.panalysis.base.Analysis;
import kieker.panalysis.base.Pipeline;

/**
 * @author Christian Wulf
 * 
 * @since 1.10
 */
public class CountWordsAnalysis extends Analysis {

	private Pipeline<MethodCallPipe> pipeline;

	private RepeaterSource repeaterSource;
	private DirectoryName2Files findFilesStage;
	private Distributor distributor;
	private CountWordsStage countWordsStage0;
	private CountWordsStage countWordsStage1;
	private Merger merger;
	private OutputWordsCountSink outputWordsCountStage;

	public CountWordsAnalysis() {
		// No code necessary
	}

	@Override
	public void init() {
		super.init();

		this.repeaterSource = new RepeaterSource(".", 4000);
		this.findFilesStage = new DirectoryName2Files();
		this.distributor = new Distributor();
		this.countWordsStage0 = new CountWordsStage();
		this.countWordsStage1 = new CountWordsStage();
		this.merger = new Merger();
		this.outputWordsCountStage = new OutputWordsCountSink();

		this.pipeline = new Pipeline<MethodCallPipe>();
		this.pipeline.addStage(this.repeaterSource);
		this.pipeline.addStage(this.findFilesStage);
		this.pipeline.addStage(this.distributor);
		this.pipeline.addStage(this.countWordsStage0);
		this.pipeline.addStage(this.countWordsStage1);
		this.pipeline.addStage(this.merger);
		this.pipeline.addStage(this.outputWordsCountStage);

		this.pipeline.setStartStages(this.repeaterSource);

		new MethodCallPipe().connect(this.repeaterSource, RepeaterSource.OUTPUT_PORT.OUTPUT, this.findFilesStage,
				DirectoryName2Files.INPUT_PORT.DIRECTORY_NAME);
		new MethodCallPipe().connect(this.findFilesStage, DirectoryName2Files.OUTPUT_PORT.FILE, this.distributor, Distributor.INPUT_PORT.OBJECT);

		new MethodCallPipe().connect(this.distributor, Distributor.OUTPUT_PORT.OUTPUT0, this.countWordsStage0,
				CountWordsStage.INPUT_PORT.FILE);
		new MethodCallPipe().connect(this.distributor, Distributor.OUTPUT_PORT.OUTPUT1, this.countWordsStage1,
				CountWordsStage.INPUT_PORT.FILE);

		new MethodCallPipe().connect(this.countWordsStage0, CountWordsStage.OUTPUT_PORT.WORDSCOUNT, this.merger,
				Merger.INPUT_PORT.INPUT0);
		new MethodCallPipe().connect(this.countWordsStage1, CountWordsStage.OUTPUT_PORT.WORDSCOUNT, this.merger,
				Merger.INPUT_PORT.INPUT1);

		new MethodCallPipe().connect(this.merger, Merger.OUTPUT_PORT.OBJECT, this.outputWordsCountStage,
				OutputWordsCountSink.INPUT_PORT.FILE_WORDCOUNT_TUPLE);

	}

	@Override
	public void start() {
		super.start();
		this.pipeline.start();
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

		System.out.println("repeaterSource: " + (analysis.repeaterSource.getOverallDuration() - // NOPMD (Just for example purposes)
				analysis.findFilesStage.getOverallDuration()) + " ms");
		System.out.println("findFilesStage: " + (analysis.findFilesStage.getOverallDuration() - // NOPMD (Just for example purposes)
				analysis.countWordsStage0.getOverallDuration()) + " ms");
		System.out.println("countWordsStage0: " + (analysis.countWordsStage0.getOverallDuration() - // NOPMD (Just for example purposes)
				analysis.outputWordsCountStage.getOverallDuration()) + " ms");
		System.out.println("countWordsStage1: " + (analysis.countWordsStage1.getOverallDuration() - // NOPMD (Just for example purposes)
				analysis.outputWordsCountStage.getOverallDuration()) + " ms");
		System.out.println("outputWordsCountStage: " + analysis.outputWordsCountStage.getOverallDuration() + " ms"); // NOPMD (Just for example purposes)

		System.out.println("findFilesStage: " + analysis.findFilesStage.getNumFiles()); // NOPMD (Just for example purposes)
		System.out.println("outputWordsCountStage: " + analysis.outputWordsCountStage.getNumFiles()); // NOPMD (Just for example purposes)
	}
}
