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

package kieker.panalysis.examples.countingObjects;

import kieker.panalysis.MethodCallPipe;
import kieker.panalysis.RepeaterSource;
import kieker.panalysis.TeeFilter;
import kieker.panalysis.base.Analysis;
import kieker.panalysis.base.Pipeline;
import kieker.panalysis.composite.CycledCountingFilter;
import kieker.panalysis.examples.wordcount.DirectoryName2Files;

/**
 * @author Christian Wulf
 * 
 * @since 1.10
 */
public class CountingObjectsAnalysis extends Analysis {

	private RepeaterSource repeaterSource;
	private DirectoryName2Files findFilesStage;
	private CycledCountingFilter cycledCountingFilter;
	private Pipeline<MethodCallPipe> pipeline;
	private TeeFilter teeFilter;

	@Override
	public void init() {
		super.init();

		this.repeaterSource = new RepeaterSource(".", 1);
		this.findFilesStage = new DirectoryName2Files();
		this.cycledCountingFilter = new CycledCountingFilter(new MethodCallPipe(0L));
		this.teeFilter = new TeeFilter();

		new MethodCallPipe().connect(this.repeaterSource, RepeaterSource.OUTPUT_PORT.OUTPUT, this.findFilesStage,
				DirectoryName2Files.INPUT_PORT.DIRECTORY_NAME);
		new MethodCallPipe().connect(this.findFilesStage, DirectoryName2Files.OUTPUT_PORT.FILE, this.cycledCountingFilter,
				CycledCountingFilter.INPUT_PORT.INPUT_OBJECT);
		new MethodCallPipe().connect(this.cycledCountingFilter, CycledCountingFilter.OUTPUT_PORT.RELAYED_OBJECT, this.teeFilter,
				TeeFilter.INPUT_PORT.INPUT_OBJECT);

		this.pipeline = new Pipeline<MethodCallPipe>();
		this.pipeline.addStage(this.repeaterSource);
		this.pipeline.addStage(this.findFilesStage);
		this.pipeline.addStage(this.cycledCountingFilter);
		this.pipeline.addStage(this.teeFilter);

		this.pipeline.setStartStages(this.repeaterSource);
	}

	@Override
	public void start() {
		super.start();
		this.pipeline.start();
	}

	public static void main(final String[] args) {
		final CountingObjectsAnalysis analysis = new CountingObjectsAnalysis();
		analysis.init();
		final long start = System.currentTimeMillis();
		analysis.start();
		final long end = System.currentTimeMillis();
		// analysis.terminate();
		final long duration = end - start;
		System.out.println("duration: " + duration + " ms"); // NOPMD (Just for example purposes)

		final Long currentCount = analysis.cycledCountingFilter.getCurrentCount();
		System.out.println("count: " + currentCount); // NOPMD (Just for example purposes)
	}
}
