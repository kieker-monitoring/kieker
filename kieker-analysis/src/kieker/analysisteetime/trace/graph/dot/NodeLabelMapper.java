package kieker.analysisteetime.trace.graph.dot;

import java.util.Collection;
import java.util.function.Function;
import java.util.stream.Collectors;

import kieker.analysisteetime.util.graph.Vertex;

/**
 * This class is a {@link Function} that maps a {@link Vertex} to a label. The
 * desired format is: container + "::\\n" + "@" + stackDepth + ":" + component + "\\n" + name;
 * {@code <DeploymentContext>::\\n@<StackDepth>:<Component>\\n<FullOperationSignature>}
 *
 * @author Sören Henning
 *
 * @since 1.13
 *
 */
public class NodeLabelMapper implements Function<Vertex, String> {

	public NodeLabelMapper() {
		super();
	}

	@Override
	public String apply(final Vertex vertex) {

		if (vertex.getProperty("artificial") != null) {
			return vertex.getProperty("name").toString();
		}

		final Collection<String> modifiers;
		final Collection<String> parameters;

		// BETTER consider check if properties exists

		if (vertex.getProperty("modifiers") instanceof Collection) {
			@SuppressWarnings("unchecked")
			final Collection<String> castedModifiers = (Collection<String>) vertex.getProperty("modifiers");
			modifiers = castedModifiers;
		} else {
			throw new IllegalArgumentException("Vertex property 'modifiers' is not a collection.");
		}
		if (vertex.getProperty("parameterTypes") instanceof Collection) {
			@SuppressWarnings("unchecked")
			final Collection<String> castedParameters = (Collection<String>) vertex.getProperty("parameterTypes");
			parameters = castedParameters;
		} else {
			throw new IllegalArgumentException("Vertex property 'parameterTypes' is not a collection.");
		}

		final String temp = modifiers.stream().collect(Collectors.joining(" "));

		// TODO this could be extracted
		final StringBuilder signature = new StringBuilder();

		final String temp2 = parameters.stream().collect(Collectors.joining(" "));

		signature.append(modifiers.stream().collect(Collectors.joining(" ")));
		signature.append(' ');
		signature.append(vertex.getProperty("returnType").toString());
		signature.append(' ');
		signature.append(vertex.getProperty("name").toString());
		signature.append('(');
		signature.append(parameters.stream().collect(Collectors.joining(", ")));
		signature.append(')');

		final StringBuilder label = new StringBuilder();
		label.append(vertex.getProperty("deploymentContext").toString());
		label.append("::\\n");
		label.append('@');
		label.append(vertex.getProperty("stackDepth").toString());
		label.append(':');
		label.append(vertex.getProperty("component").toString());
		label.append("\\n");
		label.append(signature);

		return label.toString();
	}

}
