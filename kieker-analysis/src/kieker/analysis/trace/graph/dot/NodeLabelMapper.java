/***************************************************************************
 * Copyright 2023 Kieker Project (http://kieker-monitoring.net)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ***************************************************************************/

package kieker.analysis.trace.graph.dot;

import java.util.Collection;
import java.util.function.Function;
import java.util.stream.Collectors;

import kieker.analysis.graph.IVertex;

/**
 * This class is a {@link Function} that maps a {@link IVertex} to a label. The
 * desired format is: container + "::\\n" + "@" + stackDepth + ":" + component +
 * "\\n" + name;
 * {@code <DeploymentContext>::\\n@<StackDepth>:<Component>\\n<FullOperationSignature>}
 *
 * @author Sören Henning
 *
 * @since 1.14
 *
 */
public class NodeLabelMapper implements Function<IVertex, String> {

	public NodeLabelMapper() {
		super();
	}

	@Override
	public String apply(final IVertex vertex) {
		if (vertex.getProperty("artificial") != null) {
			return vertex.getProperty("name").toString();
		}

		final Collection<String> modifiers;

		// BETTER consider check if properties exists

		if (vertex.getProperty("modifiers") instanceof Collection) {
			@SuppressWarnings("unchecked")
			final Collection<String> castedModifiers = (Collection<String>) vertex.getProperty("modifiers");
			modifiers = castedModifiers;
		} else {
			throw new IllegalArgumentException("Vertex property 'modifiers' is not a collection.");
		}

		final Collection<String> parameters;

		if (vertex.getProperty("parameterTypes") instanceof Collection) {
			@SuppressWarnings("unchecked")
			final Collection<String> castedParameters = (Collection<String>) vertex.getProperty("parameterTypes");
			parameters = castedParameters;
		} else {
			throw new IllegalArgumentException("Vertex property 'parameterTypes' is not a collection.");
		}

		// BETTER this could be extracted
		final StringBuilder signature = new StringBuilder();
		signature.append(modifiers.stream().collect(Collectors.joining(" "))).append(' ')
				.append(vertex.getProperty("returnType").toString()).append(' ')
				.append(vertex.getProperty("name").toString()).append('(')
				.append(parameters.stream().collect(Collectors.joining(", "))).append(')');

		final StringBuilder label = new StringBuilder() // NOPMD (.append(<>.toString() + "...") does not make sense)
				.append(vertex.getProperty("deploymentContext").toString()).append("::\\n").append('@')
				.append(vertex.getProperty("stackDepth").toString()).append(':')
				.append(vertex.getProperty("component").toString()).append("\\n").append(signature);

		return label.toString();
	}

}
