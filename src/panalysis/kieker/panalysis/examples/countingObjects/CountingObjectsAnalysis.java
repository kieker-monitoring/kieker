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
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import kieker.panalysis.examples.countWords.DirectoryName2Files;
import kieker.panalysis.framework.core.AbstractFilter;
import kieker.panalysis.framework.core.Analysis;
import kieker.panalysis.framework.core.IPipeline;
import kieker.panalysis.framework.core.IStage;
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

	@Override
	public void init() {
		super.init();

		this.pipeline = this.buildNonIoPipeline();
	}

	private IPipeline buildNonIoPipeline() {
		// create stages
		final RepeaterSource<String> repeaterSource = RepeaterSource.create(".", 1);
		final DirectoryName2Files findFilesStage = new DirectoryName2Files();
		final CycledCountingFilter<File> cycledCountingFilter = CycledCountingFilter.create(new MethodCallPipe<Long>(0L));
		final TypeLoggerFilter<File> typeLoggerFilter = TypeLoggerFilter.create();

		// add each stage to a stage list
		final List<IStage> stages = new LinkedList<IStage>();
		stages.add(repeaterSource);
		stages.add(findFilesStage);
		stages.add(cycledCountingFilter);
		stages.add(typeLoggerFilter);

		// connect stages by pipes
		MethodCallPipe.connect(repeaterSource.OUTPUT, findFilesStage.DIRECTORY_NAME);
		MethodCallPipe.connect(findFilesStage.FILE, cycledCountingFilter.INPUT_OBJECT);
		MethodCallPipe.connect(cycledCountingFilter.RELAYED_OBJECT, typeLoggerFilter.INPUT_OBJECT);

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

		for (final IStage stage : analysis.pipeline.getStages()) {
			if (stage instanceof AbstractFilter<?>) {
				System.out.println(stage.getClass().getName() + ": " + ((AbstractFilter<?>) stage).getOverallDuration()); // NOPMD (Just for example purposes)
			}
		}

		@SuppressWarnings("unchecked")
		final CycledCountingFilter<File> cycledCountingFilter = (CycledCountingFilter<File>) analysis.pipeline.getStages().get(2);
		System.out.println("count: " + cycledCountingFilter.getCurrentCount()); // NOPMD (Just for example purposes)
	}
}
