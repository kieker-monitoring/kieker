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
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;

import kieker.panalysis.base.Pipe;
import kieker.panalysis.base.Stage;
import kieker.panalysis.base.TaskBundle;

/**
 * @author Christian Wulf
 * 
 * @since 1.10
 */
public class WorkerThread extends Thread {

	// BETTER move to StealableConcurrentPipe
	private final PriorityQueue<WorkerThread> otherThreads;

	private final Map<Stage<?>, Map<Enum<?>, Pipe<TaskBundle>>> localStages = new HashMap<Stage<?>, Map<Enum<?>, Pipe<TaskBundle>>>();

	private final int numTaskBundlesToSteal = 10;
	/** represents a thread-local pipeline copy */
	private final PrioritizedStageCollection stages;

	public WorkerThread(final PriorityQueue<WorkerThread> otherThreads, final PrioritizedStageCollection stages) {
		this.otherThreads = otherThreads;
		this.stages = stages;
	}

	@Override
	public void run() {
		this.initDatastructures();
		while (true) {
			final Stage<?> stage = this.stages.get();

			this.startStageExecution();
			stage.execute();
			this.finishStageExecution();
		}
	}

	public List<TaskBundle> onBeingStolen(final Stage<?> stage, final Enum<?> inputPort) {
		final Pipe<TaskBundle> pipe = this.localStages.get(stage).get(inputPort);
		synchronized (pipe) {
			final List<TaskBundle> taskBundles = pipe.tryTakeMultiple(this.numTaskBundlesToSteal);
			return taskBundles;
		}
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

}
