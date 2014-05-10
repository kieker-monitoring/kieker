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
import java.util.Map;

import kieker.panalysis.framework.core.IPipeline;
import kieker.panalysis.framework.core.IStage;

/**
 * @author Christian Wulf
 * 
 * @since 1.10
 */
public class NextStageScheduler {

	protected final Map<IStage, Boolean> statesOfStages = new HashMap<IStage, Boolean>();
	private final StageWorkList workList;
	private final IPipeline pipeline;

	public NextStageScheduler(final IPipeline pipeline, final int accessesDeviceId) throws Exception {
		this.pipeline = pipeline;
		this.workList = new StageWorkList(accessesDeviceId);
		this.workList.ensureCapacity(pipeline.getStages().size());

		pipeline.fireStartNotification();

		this.workList.addAll(pipeline.getStartStages());
		// this.workList.addAll(pipeline.getStages());

		for (final IStage stage : pipeline.getStages()) {
			this.enable(stage);
		}
	}

	public IStage get() {
		final IStage stage = this.workList.remove(0);
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
		// if (!Thread.currentThread().getName().equals("startThread")) {
		System.out.println("Disabled " + stage);
		// }
		stage.fireSignalClosingToAllOutputPorts();
	}

	public void determineNextStage(final IStage stage, final boolean executedSuccessfully) {
		this.workList.addAll(0, stage.getContext().getOutputStages()); // FIXME do not add the stage again if it has a cyclic pipe
		stage.getContext().getOutputStages().clear();

		if (this.statesOfStages.get(stage) == Boolean.TRUE) {
			this.workList.add(stage); // re-insert at the tail
			// System.out.println("added again: " + stage);
		}
	}

	public void cleanUp() {
		this.pipeline.fireStopNotification();
	}
}
