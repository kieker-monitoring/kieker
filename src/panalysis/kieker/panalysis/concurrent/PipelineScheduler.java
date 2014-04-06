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

package kieker.panalysis.concurrent;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import kieker.panalysis.base.IStage;

/**
 * @author Christian Wulf
 * 
 * @since 1.10
 */
public class PipelineScheduler {

	protected final Map<IStage, Boolean> statesOfStages = new HashMap<IStage, Boolean>();
	private Iterator<IStage> iterator;

	public PipelineScheduler(final List<IStage> stages) {
		for (final IStage stage : stages) {
			this.statesOfStages.put(stage, Boolean.TRUE);
		}
	}

	public IStage get() {
		if ((this.iterator == null) || !this.iterator.hasNext()) { // BETTER use a cycling iterator
			this.iterator = this.statesOfStages.keySet().iterator();
		}
		final IStage stage = this.iterator.next();
		final Boolean isEnabled = this.statesOfStages.get(stage);
		if (Boolean.FALSE.equals(isEnabled)) {
			return this.get(); // return the next enabled stage
		}
		return stage;
	}

	public boolean isAnyStageActive() {
		for (final Entry<IStage, Boolean> entry : this.statesOfStages.entrySet()) {
			final IStage stage = entry.getKey();
			final Boolean state = entry.getValue();
			if (Boolean.TRUE.equals(state)) {
				// System.out.println(stage + " is active.");
				return true;
			}
		}
		return false;
	}

	public void disable(final IStage stage) {
		this.statesOfStages.put(stage, Boolean.FALSE);
		System.out.println("Disabled " + stage);
		stage.fireSignalClosingToAllOutputPorts();
	}

	// TODO implement prioritized get and set
}
