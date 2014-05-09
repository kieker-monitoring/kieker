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
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import de.chw.util.CyclicIterator;

import kieker.panalysis.framework.core.AbstractFilter;
import kieker.panalysis.framework.core.IPipeline;
import kieker.panalysis.framework.core.IStage;

/**
 * @author Christian Wulf
 * 
 * @since 1.10
 */
public class PipelineScheduler {

	protected final Map<IStage, Boolean> statesOfStages = new HashMap<IStage, Boolean>();
	protected final List<IStage> activeStages = new LinkedList<IStage>();

	private final Iterator<IStage> iterator;

	public PipelineScheduler(final IPipeline pipeline) throws Exception {
		pipeline.fireStartNotification();

		final List<IStage> sortedStages = this.sortList(pipeline);

		for (final IStage stage : sortedStages) {
			this.enable(stage);
		}
		this.iterator = new CyclicIterator<IStage>(this.activeStages);
	}

	private List<IStage> sortList(final IPipeline pipeline) {
		final List<? extends AbstractFilter<?>> startStages = pipeline.getStartStages();

		final List<IStage> list = new LinkedList<IStage>(pipeline.getStages());
		list.removeAll(startStages);
		list.addAll(0, startStages);
		return list;
	}

	public IStage get() {
		final IStage stage = this.iterator.next();
		// final Boolean isEnabled = this.statesOfStages.get(stage);
		// if (Boolean.FALSE.booleanValue() == isEnabled.booleanValue()) {
		// return this.get(); // return the next enabled stage
		// }
		return stage;
	}

	public boolean isAnyStageActive() {
		// for (final Entry<IStage, Boolean> entry : this.statesOfStages.entrySet()) {
		// // final IStage stage = entry.getKey();
		// final Boolean state = entry.getValue();
		// if (Boolean.TRUE.equals(state)) {
		// // System.out.println(stage + " is active.");
		// return true;
		// }
		// }
		return this.activeStages.size() > 0;
	}

	public void disable(final IStage stage) {
		this.statesOfStages.put(stage, Boolean.FALSE);
		this.activeStages.remove(stage);
		System.out.println("Disabled " + stage);
		stage.fireSignalClosingToAllOutputPorts();
	}

	protected void enable(final IStage stage) {
		this.statesOfStages.put(stage, Boolean.TRUE);
		this.activeStages.add(stage);
	}

}
