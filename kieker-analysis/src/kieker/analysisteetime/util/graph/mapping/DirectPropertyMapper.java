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

	private final String propertyKey;

	private DirectPropertyMapper(final String propertyKey) {
		this.propertyKey = propertyKey;
	}

	@Override
	public String apply(final T element) {
		return element.getProperty(this.propertyKey);
	}

	public static <T extends Element> DirectPropertyMapper<T> of(final String propertyKey) {
		return new DirectPropertyMapper<T>(propertyKey);
	}

}
