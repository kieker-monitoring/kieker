/***************************************************************************
 * Copyright 2020 Kieker Project (http://kieker-monitoring.net)
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

package kieker.tools.trace.analysis.filter.visualization.dependencyGraph;

import kieker.tools.trace.analysis.filter.visualization.AbstractGraphFormatter;
import kieker.tools.trace.analysis.systemModel.AllocationComponent;

/**
 * Abstract superclass for formatters for component-based dependency graphs.
 *
 * @author Holger Knoche
 *
 * @param <G>
 *            The graph type this formatter is for
 *
 * @since 1.6
 */
public abstract class AbstractComponentDependencyGraphFormatter<G extends AbstractDependencyGraph<?>> extends AbstractDependencyGraphFormatter<G> {

	/**
	 * Utility function to create a label for component nodes.
	 *
	 * @param node
	 *            The component node to create the label for
	 * @param useShortLabels
	 *            Indicates whether short labels (i.e. without package names) should be used
	 * @param stereotype
	 *            The stereotype to use in the label
	 * @return A formatted component node label
	 */
	protected static String createComponentNodeLabel(final DependencyGraphNode<AllocationComponent> node, final boolean useShortLabels, final String stereotype) {
		final StringBuilder builder = new StringBuilder();
		final AllocationComponent component = node.getEntity();

		builder.append(stereotype).append("\\n")
		       .append(component.getAssemblyComponent().getName()).append(':');

		if (useShortLabels) {
			builder.append("..");
		} else {
			builder.append(component.getAssemblyComponent().getType().getPackageName()).append('.');
		}

		builder.append(component.getAssemblyComponent().getType().getTypeName());
		AbstractGraphFormatter.formatDecorations(builder, node);

		return builder.toString();
	}
}
