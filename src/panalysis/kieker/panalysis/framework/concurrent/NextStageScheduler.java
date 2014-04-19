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

package kieker.panalysis.framework.concurrent;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import kieker.panalysis.framework.core.AbstractFilter;
import kieker.panalysis.framework.core.IStage;
import kieker.panalysis.framework.sequential.Pipeline;

/**
 * @author Christian Wulf
 * 
 * @since 1.10
 */
public class NextStageScheduler {

	protected final Map<IStage, Boolean> statesOfStages = new HashMap<IStage, Boolean>();
	private final List<IStage> workList;
	private final Pipeline<?> pipeline;

	public NextStageScheduler(final Pipeline<?> pipeline) {
		this.pipeline = pipeline;
		this.workList = new StageWorkList(pipeline);
		pipeline.start();

		this.workList.addAll(pipeline.getStartStages());

		for (final IStage stage : pipeline.getStages()) {
			this.enable(stage);
		}
	}

	public IStage get() {
		final IStage stage = this.workList.get(0);
		// System.out.println(Thread.currentThread() + " > Executing " + stage);
		return stage;
	}

	public boolean isAnyStageActive() {
		return !this.workList.isEmpty();
	}

	protected void enable(final IStage stage) {
		this.statesOfStages.put(stage, Boolean.TRUE);
	}

	public void disable(final IStage stage) {
		this.statesOfStages.put(stage, Boolean.FALSE);
		System.out.println("Disabled " + stage);
		stage.fireSignalClosingToAllOutputPorts();
	}

	public void determineNextStage(final IStage stage, final boolean executedSuccessfully) {
		// if (stage instanceof DirectoryName2Files) {
		// System.out.println("stage=" + stage + ", executedSuccessfully=" + executedSuccessfully);
		// }
		if (executedSuccessfully && (this.statesOfStages.get(stage) == Boolean.TRUE)) {
			// System.out.println("Next stages: " + stage.getContext().getOutputStages());
			this.workList.addAll(0, stage.getContext().getOutputStages());
		} else {
			this.workList.remove(0); // removes the given stage
			// System.out.println("Removed " + stage);
			if (this.workList.isEmpty()) {
				for (final AbstractFilter<?> startStage : this.pipeline.getStartStages()) {
					if (this.statesOfStages.get(startStage) == Boolean.TRUE) {
						this.workList.add(startStage);
					}
				}
			}
		}
		stage.getContext().getOutputStages().clear();
	}
}
