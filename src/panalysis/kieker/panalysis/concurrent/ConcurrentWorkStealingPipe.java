package kieker.panalysis.concurrent;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import de.chw.concurrent.CircularWorkStealingDeque;

import kieker.panalysis.base.AbstractPipe;
import kieker.panalysis.base.ISink;
import kieker.panalysis.base.ISource;
import kieker.panalysis.examples.wordcount.ConcurrentCountWordsAnalysis;

public class ConcurrentWorkStealingPipe extends AbstractPipe<ConcurrentWorkStealingPipe> {

	private final CircularWorkStealingDeque circularWorkStealingDeque = new CircularWorkStealingDeque();
	private List<ConcurrentWorkStealingPipe> allOtherPipes;
	private ISink<?> targetStage;

	public void copyAllOtherPipes(final Set<ConcurrentWorkStealingPipe> samePipesFromAllThreads) {
		this.allOtherPipes = new ArrayList<ConcurrentWorkStealingPipe>(samePipesFromAllThreads);
		this.allOtherPipes.remove(this);
	}

	@Override
	public <O extends Enum<O>, I extends Enum<I>> void connect(final ISource<O> sourceStage, final O sourcePort, final ISink<I> targetStage, final I targetPort) {
		this.targetStage = targetStage;
		super.connect(sourceStage, sourcePort, targetStage, targetPort);
	}

	public void put(final Object record) {
		this.circularWorkStealingDeque.pushBottom(record);
		if (ConcurrentCountWordsAnalysis.START_DIRECTORY_NAME != record) { // FIXME remove if manual tests are finished
			this.targetStage.execute();
		}
	}

	public Object take() {
		final Object record = this.circularWorkStealingDeque.popBottom();
		if (record == CircularWorkStealingDeque.EMPTY) {
			for (final ConcurrentWorkStealingPipe pipe : this.allOtherPipes) {
				final Object stolenElement = pipe.steal();
				if ((stolenElement != CircularWorkStealingDeque.EMPTY) && (stolenElement != CircularWorkStealingDeque.ABORT)) {
					return stolenElement;
				}
			}
			// BETTER improve stealing
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

	public Object tryTake() {
		final Object record = this.circularWorkStealingDeque.popBottom();
		if (record == CircularWorkStealingDeque.EMPTY) {
			return null;
		}
		return record;
	}

	public List<?> tryTakeMultiple(final int maxItemsToTake) {
		// TODO Auto-generated method stub
		// BETTER find a way to take multiple elements directly without a loop
		return null;
	}

	public void putMultiple(final List<?> items) {
		// TODO Auto-generated method stub
		// BETTER find a way to put multiple elements directly without a loop
	}

}
