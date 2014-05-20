package kieker.panalysis.framework.concurrent;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import kieker.panalysis.framework.core.IStage;

public class StageWorkList {

	// private static final Log LOG = LogFactory.getLog(StageWorkList.class);

	private final List<IStage> workList;
	private final int accessesDeviceId;

	public StageWorkList(final int accessesDeviceId, final int initialCapacity) {
		this.accessesDeviceId = accessesDeviceId;
		this.workList = new ArrayList<IStage>(initialCapacity);
	}

	public void pushAll(final Collection<? extends IStage> stages) {
		this.addAll(0, stages);
	}

	public void push(final IStage stage) {
		this.add(0, stage);
	}

	public IStage pop() {
		return this.workList.remove(0);
	}

	public IStage read() {
		return this.workList.get(0);
	}

	public boolean isEmpty() {
		return this.workList.isEmpty();
	}

	private boolean isValid(final IStage stage) {
		final boolean isValid = (stage.getAccessesDeviceId() == this.accessesDeviceId);
		if (!isValid) {
			// LOG.warn("Invalid stage: stage.accessesDeviceId = " + stage.getAccessesDeviceId() + ", accessesDeviceId = " + this.accessesDeviceId + ", stage = " +
			// stage);
		}
		return isValid;
	}

	private void add(final int index, final IStage stage) {
		if (this.isValid(stage)) {
			this.workList.add(index, stage);
		}
	}

	private boolean addAll(final int index, final Collection<? extends IStage> collection) {
		final Collection<IStage> filteredCollection = new ArrayList<IStage>(collection.size());
		for (final IStage stage : collection) {
			if (this.isValid(stage)) {
				filteredCollection.add(stage);
			}
		}
		return this.workList.addAll(index, filteredCollection);
	}

	@Override
	public String toString() {
		return this.workList.toString();
	}

}
