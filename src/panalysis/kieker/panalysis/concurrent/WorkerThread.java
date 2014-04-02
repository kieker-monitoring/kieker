package kieker.panalysis.concurrent;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;

import kieker.panalysis.base.Pipe;
import kieker.panalysis.base.Stage;
import kieker.panalysis.base.TaskBundle;

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
