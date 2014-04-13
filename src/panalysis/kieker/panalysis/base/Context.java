package kieker.panalysis.base;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class Context<S extends IStage> {

	private final Map<IPipe<Object, ?>, List<Object>> pipesTakenFrom;

	// private final Map<IOutputPort<S, ?>, List<Object>> pipesPutTo;

	// statistics values
	private int numPushedElements = 0;
	private int numTakenElements = 0;

	@SuppressWarnings("unchecked")
	public Context(final List<IInputPort<S, ?>> allInputPorts, final List<IOutputPort<S, ?>> allOutputPorts) {
		final Map<IPipe<Object, ?>, List<Object>> map = new HashMap<IPipe<Object, ?>, List<Object>>(allInputPorts.size());
		for (final IInputPort<S, ?> inputPort : allInputPorts) {
			final IPipe<?, ?> associatedPipe = inputPort.getAssociatedPipe();
			map.put((IPipe<Object, ?>) associatedPipe, new ArrayList<Object>());
		}
		this.pipesTakenFrom = map;

		// this.pipesPutTo = new HashMap<IOutputPort<S, Object>, List<Object>>(allOutputPorts.size());
		// for (final IOutputPort<S, Object> outputPort : allOutputPorts) {
		// this.pipesPutTo.put(outputPort, new LinkedList<Object>());
		// }
	}

	/**
	 * @since 1.10
	 */
	public <T> void put(final IOutputPort<S, T> port, final T object) {
		final IPipe<T, ?> associatedPipe = port.getAssociatedPipe();
		if (associatedPipe == null) {
			return; // ignore unconnected port
			// BETTER return a NullObject rather than checking for null
		}
		associatedPipe.put(object);
		// this.pipesPutTo.get(associatedPipe).add(object);
		this.numPushedElements++;
	}

	/**
	 * @since 1.10
	 */
	public <T> T tryTake(final IInputPort<S, T> inputPort) {
		final IPipe<T, ?> associatedPipe = inputPort.getAssociatedPipe();
		final T token = associatedPipe.tryTake();
		if (token != null) {
			final List<Object> tokenList = this.pipesTakenFrom.get(associatedPipe);
			tokenList.add(token);

			this.numTakenElements++;
		}
		return token;
	}

	/**
	 * @since 1.10
	 */
	public <T> T read(final IInputPort<S, T> inputPort) {
		final IPipe<? extends T, ?> associatedPipe = inputPort.getAssociatedPipe();
		return associatedPipe.read();
	}

	void clear() {
		for (final List<Object> takenElements : this.pipesTakenFrom.values()) {
			takenElements.clear();
		}

		// for (final List<?> putElements : this.pipesPutTo.values()) {
		// putElements.clear();
		// }
	}

	void rollback() {
		for (final Entry<IPipe<Object, ?>, List<Object>> entry : this.pipesTakenFrom.entrySet()) {
			final IPipe<Object, ?> associatedPipe = entry.getKey();
			final List<Object> takenElements = entry.getValue();

			// FIXME to preserve order, the elements need to be re-put in reverse order
			for (final Object element : takenElements) {
				associatedPipe.put(element);
			}
		}

		// for (final Entry<IOutputPort<S, ? extends O>, List<O>> putElements : this.pipesPutTo.entrySet()) {
		// entry
		// }
	}
}
