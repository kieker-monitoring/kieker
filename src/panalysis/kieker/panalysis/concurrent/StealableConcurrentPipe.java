package kieker.panalysis.concurrent;

import java.util.List;

import kieker.panalysis.base.Pipe;

public class StealableConcurrentPipe<T> implements Pipe<T> {

	// private void steal(final Stage<?> stage, final Enum<?> inputPort) {
	// for (final WorkerThread otherThread : this.otherThreads) {
	// final List<TaskBundle> taskBundles = otherThread.onBeingStolen(stage, inputPort);
	// if (taskBundles != null) {
	// this.localStages.get(stage).get(inputPort).putMultiple(taskBundles);
	// break;
	// }
	// }
	// }

	public void put(final T record) {
		// TODO Auto-generated method stub

	}

	public T take() {
		// TODO Auto-generated method stub
		return null;
	}

	public T tryTake() {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean isEmpty() {
		// TODO Auto-generated method stub
		return false;
	}

	public List<T> tryTakeMultiple(final int numItemsToTake) {
		// TODO Auto-generated method stub
		return null;
	}

	public void putMultiple(final List<T> items) {
		// TODO Auto-generated method stub

	}

}
