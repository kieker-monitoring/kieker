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

import java.io.File;

import kieker.panalysis.examples.countWords.DirectoryName2Files;
import kieker.panalysis.framework.core.Analysis;
import kieker.panalysis.framework.core.IPipeline;
import kieker.panalysis.framework.sequential.MethodCallPipe;
import kieker.panalysis.stage.TypeLoggerFilter;
import kieker.panalysis.stage.basic.RepeaterSource;
import kieker.panalysis.stage.composite.CycledCountingFilter;

/**
 * @author Christian Wulf
 * 
 * @since 1.10
 */
public class CountingObjectsAnalysis extends Analysis {

	private IPipeline pipeline;

	private RepeaterSource<String> repeaterSource;
	private DirectoryName2Files findFilesStage;
	private CycledCountingFilter<File> cycledCountingFilter;
	private TypeLoggerFilter<File> teeFilter;

	@Override
	public void init() {
		super.init();

		this.repeaterSource = RepeaterSource.create(".", 1);
		this.findFilesStage = new DirectoryName2Files();
		this.cycledCountingFilter = CycledCountingFilter.create(new MethodCallPipe<Long>(0L));
		this.teeFilter = TypeLoggerFilter.create();

		this.pipeline = Pipeline.create();
		this.pipeline.addStage(this.repeaterSource);
		this.pipeline.addStage(this.findFilesStage);
		this.pipeline.addStage(this.cycledCountingFilter);
		this.pipeline.addStage(this.teeFilter);

		this.pipeline.connect(this.repeaterSource.OUTPUT, this.findFilesStage.DIRECTORY_NAME);
		this.pipeline.connect(this.findFilesStage.FILE, this.cycledCountingFilter.INPUT_OBJECT);
		this.pipeline.connect(this.cycledCountingFilter.RELAYED_OBJECT, this.teeFilter.INPUT_OBJECT);

		this.pipeline.setStartStages(this.repeaterSource);
	}

	@Override
	public void start() {
		super.start();
		this.pipeline.fireStartNotification();
	}

	/**
	 * @since 1.10
	 * @param args
	 */
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
