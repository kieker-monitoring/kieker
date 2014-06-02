package kieker.panalysis.framework.concurrent;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import kieker.panalysis.framework.core.IOutputPort;
import kieker.panalysis.framework.core.IStage;

public class StageWorkList implements IStageWorkList {

	// private static final Log LOG = LogFactory.getLog(StageWorkList.class);

	private final List<IStage> workList;
	private final int accessesDeviceId;

	public StageWorkList(final int accessesDeviceId, final int initialCapacity) {
		this.accessesDeviceId = accessesDeviceId;
		this.workList = new ArrayList<IStage>(initialCapacity);
	}

	@Override
	public void pushAll(final Collection<? extends IStage> stages) {
		this.addAll(0, stages);
	}

	@Override
	public IStage pop() {
		return this.workList.remove(0);
	}

	@Override
	public IStage read() {
		return this.workList.get(0);
	}

	@Override
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

	private boolean addAll(final int index, final Collection<? extends IStage> collection) {
		final Collection<IStage> filteredCollection = new ArrayList<IStage>(collection.size());
		for (final IStage stage : collection) {
			this.push(filteredCollection, stage);
		}
		return this.workList.addAll(index, filteredCollection);
	}

	private void push(final Collection<IStage> filteredCollection, final IStage stage) {
		if (this.isValid(stage)) {
			filteredCollection.add(stage);
		}
	}

	@Override
	public String toString() {
		return this.workList.toString();
	}

	public void pushAll(final IOutputPort<?, ?>[] outputPorts) {
		final Collection<IStage> filteredCollection = new ArrayList<IStage>(outputPorts.length);
		for (final IOutputPort<?, ?> outputPort : outputPorts) {
			if (outputPort != null) {
				final IStage targetStage = outputPort.getAssociatedPipe().getTargetPort().getOwningStage();
				this.push(filteredCollection, targetStage);
			}
		}
		this.workList.addAll(0, filteredCollection);
	}

}
