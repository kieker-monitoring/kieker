package kieker.panalysis.framework.concurrent;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import kieker.panalysis.framework.core.IStage;

public class StageWorkList {

	private final List<IStage> array;
	private final int accessesDeviceId;

	public StageWorkList(final int accessesDeviceId, final int initialCapacity) {
		this.accessesDeviceId = accessesDeviceId;
		this.array = new ArrayList<IStage>(initialCapacity);
	}

	public void pushAll(final Collection<? extends IStage> stages) {
		this.addAll(0, stages);
	}

	public void push(final IStage stage) {
		this.add(0, stage);
	}

	public IStage pop() {
		return this.array.remove(0);
	}

	public IStage read() {
		return this.array.get(0);
	}

	public boolean isEmpty() {
		return this.array.isEmpty();
	}

	private boolean isValid(final IStage stage) {
		return (stage.getAccessesDeviceId() == this.accessesDeviceId);
	}

	private void add(final int index, final IStage stage) {
		if (this.isValid(stage)) {
			this.array.add(index, stage);
		}
	}

	private boolean addAll(final int index, final Collection<? extends IStage> collection) {
		final Collection<IStage> filteredCollection = new ArrayList<IStage>(collection.size());
		for (final IStage stage : collection) {
			if (this.isValid(stage)) {
				filteredCollection.add(stage);
			}
		}
		return this.array.addAll(index, filteredCollection);
	}

}
