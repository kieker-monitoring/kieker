package kieker.analysisteetime.util.graph.mapping;

import java.util.function.Function;

import kieker.analysisteetime.util.graph.Element;

/**
 * This function maps a graph element by a passed property key to the
 * corresponding property value and returns it as string.
 *
 * @author Sören Henning
 *
 */
public class DirectPropertyMapper<T extends Element> implements Function<T, String> {

	private String propertyKey;

	public DirectPropertyMapper(final String propertyKey) {
		this.propertyKey = propertyKey;
	}

	public String getPropertyKey() {
		return propertyKey;
	}

	public void setPropertyKey(final String propertyKey) {
		this.propertyKey = propertyKey;
	}

	@Override
	public String apply(final T element) {
		return element.getProperty(propertyKey);
	}

}
