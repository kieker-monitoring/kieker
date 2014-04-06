package kieker.panalysis.concurrent;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import de.chw.concurrent.CircularWorkStealingDeque;

import kieker.panalysis.base.AbstractPipe;

public class ConcurrentWorkStealingPipe extends AbstractPipe<ConcurrentWorkStealingPipe> {

	private final CircularWorkStealingDeque circularWorkStealingDeque = new CircularWorkStealingDeque();
	/*
	 * BETTER use a prioritized list that considers
	 * <ul>
	 * <li>the size of each deque to minimize steals
	 * <li>the core's locality to improve cache access performance
	 * </ul>
	 */
	private List<ConcurrentWorkStealingPipe> allOtherPipes;

	public void copyAllOtherPipes(final List<ConcurrentWorkStealingPipe> samePipesFromAllThreads) {
		this.allOtherPipes = new ArrayList<ConcurrentWorkStealingPipe>(samePipesFromAllThreads);
		this.allOtherPipes.remove(this);
	}

	@Override
	protected void putInternal(final Object token) {
		this.circularWorkStealingDeque.pushBottom(token);
	}

	public void putMultiple(final List<?> items) {
		// TODO Auto-generated method stub
		// BETTER find a way to put multiple elements directly without a loop
	}

	public Object take() {
		throw new IllegalStateException("deprecated");
	}

	@Override
	protected Object tryTakeInternal() {
		final Object record = this.circularWorkStealingDeque.popBottom();
		if (record == CircularWorkStealingDeque.EMPTY) {
			for (final ConcurrentWorkStealingPipe pipe : this.allOtherPipes) {
				final Object stolenElement = pipe.steal();
				if ((stolenElement != CircularWorkStealingDeque.EMPTY) && (stolenElement != CircularWorkStealingDeque.ABORT)) {
					return stolenElement;
				}
			}
			// BETTER improve stealing efficiency by stealing multiple elements at once
			return null; // do not expose internal impl details (here: CircularWorkStealingDeque); instead return null
		}
		return record;
	}

	public List<?> tryTakeMultiple(final int maxItemsToTake) {
		// TODO Auto-generated method stub
		// BETTER find a way to take multiple elements directly without a loop
		return null;
	}

	public Object read() {
		final Object record = this.circularWorkStealingDeque.readBottom();
		if (record == CircularWorkStealingDeque.EMPTY) {
			return null;
		}
		return record;
	}

	public Object steal() {
		return this.circularWorkStealingDeque.steal();
	}

	public List<Object> stealMultiple(int maxItemsToSteal) {
		final List<Object> stolenElements = new LinkedList<Object>();
		while (maxItemsToSteal-- > 0) {
			final Object stolenElement = this.steal();
			if ((stolenElement == CircularWorkStealingDeque.EMPTY) && (stolenElement == CircularWorkStealingDeque.ABORT)) {
				break;
			}
			stolenElements.add(stolenElement);
		}
		// BETTER find a way to steal multiple elements directly without a loop
		return stolenElements;
	}

}
