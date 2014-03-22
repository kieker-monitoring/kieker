package kieker.panalysis.base;

import java.util.EnumMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public abstract class Source<OutputPort extends Enum<OutputPort>> extends Stage {

	private final Map<OutputPort, Set<PortListener>> outputPortListeners;

	public Source(final long id, final Class<OutputPort> enumType) {
		super(id);
		outputPortListeners = new EnumMap<OutputPort, Set<PortListener>>(enumType);
	}

	public void addOutputPortListener(final OutputPort port, final PortListener portListener) {
		Set<PortListener> portListeners = outputPortListeners.get(port);
		if (portListeners == null) {
			portListeners = new HashSet<PortListener>();
			outputPortListeners.put(port, portListeners);
		}
		portListeners.add(portListener);
	}

	public void deliver(final OutputPort port, final Object record) {
		Set<PortListener> portListeners = outputPortListeners.get(port);
		if (portListeners == null) return; // ignore ports that are not connected
		for (final PortListener portListener : portListeners) {
			portListener.onPortChanged(record);
		}
	}
}
