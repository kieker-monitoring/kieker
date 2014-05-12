package kieker.panalysis.framework.core;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public class Context<S extends IStage> {

	private final Map<IPipe<Object>, List<Object>> pipesTakenFrom;
	private final Set<IStage> pipesPutTo = new HashSet<IStage>();

	// statistics values
	private int numPushedElements = 0;
	private int numTakenElements = 0;

	public Context(final List<IInputPort<S, ?>> allInputPorts) {
		this.pipesTakenFrom = this.createPipeMap(allInputPorts);
	}

	@SuppressWarnings("unchecked")
	private Map<IPipe<Object>, List<Object>> createPipeMap(final List<? extends IPort<S, ?>> allPorts) {
		final Map<IPipe<Object>, List<Object>> pipeMap = new HashMap<IPipe<Object>, List<Object>>(allPorts.size());
		for (final IPort<S, ?> outputPort : allPorts) {
			final IPipe<?> associatedPipe = outputPort.getAssociatedPipe();
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

		this.pipesPutTo.add(associatedPipe.getTargetPort().getOwningStage());
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
		}
	}

	@Override
	public String toString() {
		return "{" + "numPushedElements=" + this.numPushedElements + ", " + "numTakenElements=" + this.numTakenElements + "}";
	}

	public Collection<? extends IStage> getOutputStages() {
		return this.pipesPutTo;
	}
}
