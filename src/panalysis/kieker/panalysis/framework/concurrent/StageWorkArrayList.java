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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import kieker.panalysis.framework.core.IOutputPort;
import kieker.panalysis.framework.core.IPipe;
import kieker.panalysis.framework.core.IPipeCommand;
import kieker.panalysis.framework.core.IPipeline;
import kieker.panalysis.framework.core.IStage;

/**
 * @author Christian Wulf
 * 
 * @since 1.10
 */
public class StageWorkArrayList implements IStageWorkList {

	private static class SchedulableStage {
		public SchedulableStage() {}

		public IStage stage;
		public int numToBeExecuted;
	}

	private final IPipeline pipeline;
	private final int accessesDeviceId;

	/** sorted array where the last stage has highest priority */
	private final SchedulableStage[] stages;
	private int firstIndex = Integer.MAX_VALUE;
	private int lastIndex = -1;

	/**
	 * @since 1.10
	 */
	public StageWorkArrayList(final IPipeline pipeline, final int accessesDeviceId) {
		this.pipeline = pipeline;
		this.accessesDeviceId = accessesDeviceId;
		final List<IStage> localStages = this.init();

		this.stages = new SchedulableStage[localStages.size()];
		for (int i = 0; i < localStages.size(); i++) {
			final SchedulableStage schedulableStage = new SchedulableStage();
			schedulableStage.stage = localStages.get(i);
			schedulableStage.numToBeExecuted = 0;
			this.stages[i] = schedulableStage;
		}
	}

	private List<IStage> init() {
		this.setDepthForEachStage();

		final List<IStage> stageList = new ArrayList<IStage>(this.pipeline.getStages());
		final Comparator<? super IStage> depthComparator = new Comparator<IStage>() {
			public int compare(final IStage o1, final IStage o2) {
				if (o1.getDepth() == o2.getDepth()) {
					return 0;
				} else if (o1.getDepth() < o2.getDepth()) {
					return -1;
				} else {
					return 1;
				}
			}
		};

		Collections.sort(stageList, depthComparator);

		for (int i = 0; i < stageList.size(); i++) {
			stageList.get(i).setSchedulingIndex(i);
		}

		return stageList;
	}

	private void setDepthForEachStage() {
		final IPipeCommand setDepthCommand = new IPipeCommand() {
			public void execute(final IPipe<?> pipe) throws Exception {
				final IStage sourceStage = pipe.getSourcePort().getOwningStage();
				final IStage owningStage = pipe.getTargetPort().getOwningStage();
				if (owningStage.getDepth() == IStage.DEPTH_NOT_SET) {
					owningStage.setDepth(sourceStage.getDepth() + 1);
					owningStage.notifyOutputPipes(this);
				}
			}
		};

		for (final IStage startStage : this.pipeline.getStartStages()) {
			startStage.setDepth(0);
		}

		for (final IStage startStage : this.pipeline.getStartStages()) {
			try {
				startStage.notifyOutputPipes(setDepthCommand);
			} catch (final Exception e) {
				throw new IllegalStateException("may not happen", e);
			}
		}
	}

	public void pushAll(final Collection<? extends IStage> stages) {
		for (final IStage stage : stages) {
			this.push(stage);
		}
	}

	public void pushAll(final IOutputPort<?, ?>[] outputPorts) {
		for (final IOutputPort<?, ?> outputPort : outputPorts) {
			if (outputPort != null) {
				final IStage targetStage = outputPort.getAssociatedPipe().getTargetPort().getOwningStage();
				this.push(targetStage);
			}
		}
	}

	private void push(final IStage stage) {
		if (this.isValid(stage)) {
			this.firstIndex = Math.min(stage.getSchedulingIndex(), this.firstIndex);
			this.lastIndex = Math.max(stage.getSchedulingIndex(), this.lastIndex);
			this.stages[stage.getSchedulingIndex()].numToBeExecuted++;
		}
	}

	private boolean isValid(final IStage stage) {
		final boolean isValid = (stage.getAccessesDeviceId() == this.accessesDeviceId);
		if (!isValid) {
			// LOG.warn("Invalid stage: stage.accessesDeviceId = " + stage.getAccessesDeviceId() + ", accessesDeviceId = " + this.accessesDeviceId + ", stage = " +
			// stage);
		}
		return isValid;
	}

	public IStage pop() {
		final SchedulableStage schedulableStage = this.stages[this.lastIndex];
		// schedulableStage.numToBeExecuted--;
		schedulableStage.numToBeExecuted = 0;
		cond:
		if (schedulableStage.numToBeExecuted == 0)
		{
			for (int i = this.lastIndex - 1; i >= this.firstIndex; i--) {
				if (this.stages[i].numToBeExecuted > 0) {
					this.lastIndex = i;
					break cond;
				}
			}
			this.firstIndex = Integer.MAX_VALUE;
			this.lastIndex = -1;
		}
		return schedulableStage.stage;
	}

	public IStage read() {
		final SchedulableStage schedulableStage = this.stages[this.lastIndex];
		return schedulableStage.stage;
	}

	public boolean isEmpty() {
		return this.lastIndex == -1;
	}

}
