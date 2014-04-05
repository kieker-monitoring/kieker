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

import java.util.List;

import kieker.panalysis.base.IStage;

/**
 * @author Christian Wulf
 * 
 * @since 1.10
 */
public class WorkerThread extends Thread {

	// private final Map<IStage<?>, Map<Enum<?>, IPipe>> localStages = new HashMap<IStage<?>, Map<Enum<?>, IPipe>>();

	/** represents a thread-local pipeline copy */
	private PrioritizedStageCollection<IStage> stages;
	private long duration;

	public void setStages(final List<IStage> stages) {
		this.stages = new PrioritizedStageCollection<IStage>(stages);
	}

	@Override
	public void run() {
		this.initDatastructures();

		final long start = System.currentTimeMillis();

		while (true) {
			final IStage stage = this.stages.get();

			this.startStageExecution();
			stage.execute();
			this.finishStageExecution();
		}

		// final long end = System.currentTimeMillis();
		// this.duration = end - start;
	}

	private void initDatastructures() {
		// TODO Auto-generated method stub

	}

	private void startStageExecution() {
		// TODO Auto-generated method stub

	}

	private void finishStageExecution() {
		// TODO Auto-generated method stub

	}

	public long getDuration() {
		return this.duration;
	}

	public List<IStage> getStages() {
		return this.stages.getElements();
	}

}
