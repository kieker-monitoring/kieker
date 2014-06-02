package kieker.panalysis.framework.core;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class Context<S extends IStage> {

	private final Map<IPipe<Object>, List<Object>> pipesTakenFrom;
	// private final Set<IStage> pipesPutTo = new HashSet<IStage>();

	private final IOutputPort<S, ?>[] outputPorts;

	// statistics values
	private int numPushedElements = 0;
	private int numTakenElements = 0;

	@SuppressWarnings("unchecked")
	public Context(final IStage owningStage, final List<IInputPort<S, ?>> allTargetPorts) {
		this.pipesTakenFrom = this.createPipeMap(allTargetPorts);
		this.outputPorts = new IOutputPort[owningStage.getOutputPorts().size()];
	}

	@SuppressWarnings("unchecked")
	private Map<IPipe<Object>, List<Object>> createPipeMap(final List<? extends IPort<S, ?>> targetPorts) {
		final Map<IPipe<Object>, List<Object>> pipeMap = new HashMap<IPipe<Object>, List<Object>>(targetPorts.size());
		for (final IPort<S, ?> targetPort : targetPorts) {
			final IPipe<?> associatedPipe = targetPort.getAssociatedPipe();
			pipeMap.put((IPipe<Object>) associatedPipe, new LinkedList<Object>());
		}
		return pipeMap;
	}

	/**
	 * @since 1.10
	 */
	public <T> void put(final IOutputPort<S, T> port, final T object) {
		final IPipe<T> associatedPipe = port.getAssociatedPipe();
		if (associatedPipe == null) {
			return; // ignore unconnected port
			// BETTER return a NullObject rather than checking for null
		}
		associatedPipe.put(object);

		this.outputPorts[port.getIndex()] = port;
		// this.pipesPutTo.add(associatedPipe.getTargetPort().getOwningStage());
		this.numPushedElements++;
	}

	/**
	 * 
	 * @param inputPort
	 * @return
	 * @since 1.10
	 */
	public <T> T tryTake(final IInputPort<S, T> inputPort) {
		final IPipe<T> associatedPipe = inputPort.getAssociatedPipe();
		final T token = associatedPipe.tryTake();
		if (token != null) {
			this.logTransaction(associatedPipe, token);
		}
		return token;
	}

	/**
	 * 
	 * @param inputPort
	 * @return
	 * @since 1.10
	 */
	public <T> T take(final IInputPort<S, T> inputPort) {
		final IPipe<T> associatedPipe = inputPort.getAssociatedPipe();
		final T token = associatedPipe.take();
		if (token != null) {
			this.logTransaction(associatedPipe, token);
		}
		return token;
	}

	private <T> void logTransaction(final IPipe<T> associatedPipe, final T token) {
		final List<Object> tokenList = this.pipesTakenFrom.get(associatedPipe);
		tokenList.add(token);

		this.numTakenElements++;
	}

	/**
	 * 
	 * @param inputPort
	 * @return
	 * 
	 * @since 1.10
	 */
	public <T> T read(final IInputPort<S, T> inputPort) {
		final IPipe<? extends T> associatedPipe = inputPort.getAssociatedPipe();
		return associatedPipe.read();
	}

	void clear() {
		for (final List<Object> takenElements : this.pipesTakenFrom.values()) {
			takenElements.clear();
		}
	}

	void rollback() {
		for (final Entry<IPipe<Object>, List<Object>> entry : this.pipesTakenFrom.entrySet()) {
			final IPipe<Object> associatedPipe = entry.getKey();
			final List<Object> takenElements = entry.getValue();

			for (int i = takenElements.size() - 1; i >= 0; i--) {
				final Object element = takenElements.get(i);
				associatedPipe.put(element);
			}

			this.numTakenElements -= takenElements.size();
		}
	}

	@Override
	public String toString() {
		return "{" + "numTakenElements=" + this.numTakenElements + ", " + "numPushedElements=" + this.numPushedElements + "}";
	}

	/**
	 * @return <code>true</code> iff all input ports are empty, otherwise <code>false</code>.
	 */
	public boolean inputPortsAreEmpty() {
		for (final IPipe<Object> pipe : this.pipesTakenFrom.keySet()) {
			if (!pipe.isEmpty()) {
				return false;
			}
		}
		return true;
	}

	/**
	 * @since 1.10
	 */
	public void clearSucessors() {
		for (int i = 0; i < this.outputPorts.length; i++) {
			this.outputPorts[i] = null;
		}
	}

	/**
	 * @return
	 * @since 1.10
	 */
	public IOutputPort<S, ?>[] getOutputStages() {
		return this.outputPorts;
	}
}
