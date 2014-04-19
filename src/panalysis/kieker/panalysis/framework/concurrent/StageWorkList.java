package kieker.panalysis.framework.concurrent;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;

import kieker.panalysis.framework.core.IStage;
import kieker.panalysis.framework.sequential.Pipeline;

public class StageWorkList extends LinkedList<IStage> {

	private static final long serialVersionUID = 5025916150961442772L;
	private final Pipeline<?> pipeline;

	public StageWorkList(final Pipeline<?> pipeline) {
		this.pipeline = pipeline;
	}

	private boolean isValid(final IStage stage) {
		return (stage.getOwningPipeline() == this.pipeline);
	}

	@Override
	public boolean add(final IStage stage) {
		if (this.isValid(stage)) {
			return super.add(stage);
		}
		return false;
	}

	@Override
	public void add(final int index, final IStage stage) {
		if (this.isValid(stage)) {
			super.add(index, stage);
		}
	}

	@Override
	public void addFirst(final IStage stage) {
		if (this.isValid(stage)) {
			super.addFirst(stage);
		}
	}

	@Override
	public void addLast(final IStage stage) {
		if (this.isValid(stage)) {
			super.addLast(stage);
		}
	}

	@Override
	public boolean addAll(final int index, final Collection<? extends IStage> collection) {
		final Collection<IStage> filteredCollection = new ArrayList<IStage>(collection.size());
		for (final IStage stage : collection) {
			if (this.isValid(stage)) {
				filteredCollection.add(stage);
			}
		}
		return super.addAll(index, filteredCollection);
	}

}
