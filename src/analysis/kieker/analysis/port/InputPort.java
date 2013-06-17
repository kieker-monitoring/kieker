package kieker.analysis.port;

import java.lang.reflect.Method;

import kieker.analysis.exception.AnalysisConfigurationException;

public final class InputPort {

	private final Class<?>[] eventTypes;
	private final Object parent;
	private Method method;

	public InputPort(final Class<?>[] eventTypes, final Object parent, final String methodName) throws AnalysisConfigurationException {
		this.eventTypes = eventTypes;
		this.parent = parent;

		Method potentialMethod = null;
		for (final Method method : parent.getClass().getMethods()) {
			if (method.getName().equals(methodName)) {
				potentialMethod = method;
				break;
			}
		}

		if (potentialMethod != null) {
			this.method = potentialMethod;
		} else {
			throw new AnalysisConfigurationException("Method not found");
		}
	}

	public void newData(final Object data) {
		try {
			this.method.invoke(this.parent, data);
		} catch (final Exception ex) {
			ex.printStackTrace();
		}
	}

	public Class<?>[] getEventTypes() {
		return this.eventTypes;
	}

}
