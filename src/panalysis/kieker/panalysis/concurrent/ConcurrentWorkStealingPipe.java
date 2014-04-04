package kieker.panalysis.concurrent;

import java.util.List;

import de.chw.concurrent.CircularWorkStealingDeque;

import kieker.panalysis.base.AbstractPipe;

public class ConcurrentWorkStealingPipe extends AbstractPipe {

	private final CircularWorkStealingDeque circularWorkStealingDeque = new CircularWorkStealingDeque();

	public void put(final Object record) {
		this.circularWorkStealingDeque.pushBottom(record);
	}

	public Object take() {
		return this.circularWorkStealingDeque.popBottom();
	}

	public Object tryTake() {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean isEmpty() {
		// TODO Auto-generated method stub
		return false;
	}

	public List<?> tryTakeMultiple(final int numItemsToTake) {
		// TODO Auto-generated method stub
		return null;
	}

	public void putMultiple(final List<?> items) {
		// TODO Auto-generated method stub

	}

}
