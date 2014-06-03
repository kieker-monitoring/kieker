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
package kieker.panalysis.framework.core;

import java.util.List;

/**
 * @author Christian Wulf
 * 
 * @since 1.10
 */
public class Pipeline implements IPipeline {

	private List<? extends IStage> startStages;
	private List<IStage> stages;

	public List<? extends IStage> getStartStages() {
		return this.startStages;
	}

	public List<IStage> getStages() {
		return this.stages;
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

	public void setStartStages(final List<? extends IStage> startStages) {
		this.startStages = startStages;
	}

	public void setStages(final List<IStage> stages) {
		this.stages = stages;
	}

}
