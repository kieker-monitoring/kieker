/***************************************************************************
 * Copyright 2015 Kieker Project (http://kieker-monitoring.net)
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

package kieker.analysisteetime.dependencygraphs.dot;

import java.util.Collection;

import kieker.analysisteetime.dependencygraphs.PropertyKeys;
import kieker.analysisteetime.dependencygraphs.vertextypes.VertexType;
import kieker.analysisteetime.dependencygraphs.vertextypes.VertexTypeMapper;
import kieker.analysisteetime.signature.NameBuilder;
import kieker.analysisteetime.util.graph.Element;
import kieker.analysisteetime.util.graph.Vertex;
import kieker.analysisteetime.util.graph.export.dot.DotExportConfiguration;
import kieker.analysisteetime.util.graph.util.dot.attributes.DotClusterAttribute;
import kieker.analysisteetime.util.graph.util.dot.attributes.DotEdgeAttribute;
import kieker.analysisteetime.util.graph.util.dot.attributes.DotGraphAttribute;
import kieker.analysisteetime.util.graph.util.dot.attributes.DotNodeAttribute;

/**
 * @author Sören Henning
 *
 * @since 1.13
 */
public class DotExportConfigurationFactory {

	private static final String ENTRY_LABEL = "'Entry'";

	private final VertexTypeMapper vertexTypeMapper;
	private final NameBuilder nameBuilder;

	public DotExportConfigurationFactory(final NameBuilder nameBuilder, final VertexTypeMapper vertexTypeMapper) {
		this.vertexTypeMapper = vertexTypeMapper;
		this.nameBuilder = nameBuilder;
	}

	private DotExportConfiguration.Builder createBaseBuilder() {
		final DotExportConfiguration.Builder builder = new DotExportConfiguration.Builder();

		builder.addGraphAttribute(DotGraphAttribute.RANKDIR, g -> "LR");
		builder.addDefaultEdgeAttribute(DotEdgeAttribute.STYLE, g -> "solid");
		builder.addDefaultEdgeAttribute(DotEdgeAttribute.ARROWHEAD, g -> "open");
		builder.addDefaultEdgeAttribute(DotEdgeAttribute.COLOR, g -> "#000000");

		builder.addDefaultNodeAttribute(DotNodeAttribute.STYLE, g -> "filled");
		builder.addDefaultNodeAttribute(DotNodeAttribute.COLOR, g -> "#000000");
		builder.addDefaultNodeAttribute(DotNodeAttribute.FILLCOLOR, g -> "white");

		builder.addEdgeAttribute(DotEdgeAttribute.LABEL, e -> this.getProperty(e, PropertyKeys.CALLS).toString());

		builder.addClusterAttribute(DotClusterAttribute.STYLE, v -> "filled");
		builder.addClusterAttribute(DotClusterAttribute.FILLCOLOR, v -> "white");

		return builder;
	}

	public DotExportConfiguration createForTypeLevelOperationDependencyGraph() {
		final DotExportConfiguration.Builder builder = this.createBaseBuilder();

		builder.addDefaultNodeAttribute(DotNodeAttribute.SHAPE, v -> "oval");

		builder.addNodeAttribute(DotNodeAttribute.LABEL,
				v -> {
					final VertexType type = this.getProperty(v, PropertyKeys.TYPE, VertexType.class);
					if (type == VertexType.ENTRY) {
						return ENTRY_LABEL;
					} else {
						return new StringBuilder().append(this.createOperationLabelFromVertex(v)).append("\\n").append(this.createStatisticsFromVertex(v))
								.toString();
					}
				});

		builder.addClusterAttribute(DotClusterAttribute.LABEL, v -> this.createComponentLabelFromVertex(v).toString());

		return builder.build();
	}

	public DotExportConfiguration createForTypeLevelComponentDependencyGraph() {
		final DotExportConfiguration.Builder builder = this.createBaseBuilder();

		builder.addDefaultNodeAttribute(DotNodeAttribute.SHAPE, v -> "box");

		builder.addNodeAttribute(DotNodeAttribute.LABEL,
				v -> {
					final VertexType type = this.getProperty(v, PropertyKeys.TYPE, VertexType.class);
					if (type == VertexType.ENTRY) {
						return ENTRY_LABEL;
					} else {
						return new StringBuilder().append(this.createComponentLabelFromVertex(v)).append("\\n").append(this.createStatisticsFromVertex(v))
								.toString();
					}
				});

		return builder.build();
	}

	public DotExportConfiguration createForAssemblyLevelOperationDependencyGraph() {
		final DotExportConfiguration.Builder builder = this.createBaseBuilder();

		builder.addDefaultNodeAttribute(DotNodeAttribute.SHAPE, v -> "oval");

		builder.addNodeAttribute(DotNodeAttribute.LABEL,
				v -> {
					final VertexType type = this.getProperty(v, PropertyKeys.TYPE, VertexType.class);
					if (type == VertexType.ENTRY) {
						return ENTRY_LABEL;
					} else {
						return new StringBuilder().append(this.createOperationLabelFromVertex(v)).append("\\n").append(this.createStatisticsFromVertex(v))
								.toString();
					}
				});

		builder.addClusterAttribute(DotClusterAttribute.LABEL, v -> this.createComponentLabelFromVertex(v).toString());

		return builder.build();
	}

	public DotExportConfiguration createForAssemblyLevelComponentDependencyGraph() {
		final DotExportConfiguration.Builder builder = this.createBaseBuilder();

		builder.addDefaultNodeAttribute(DotNodeAttribute.SHAPE, v -> "box");

		builder.addNodeAttribute(DotNodeAttribute.LABEL,
				v -> {
					final VertexType type = this.getProperty(v, PropertyKeys.TYPE, VertexType.class);
					if (type == VertexType.ENTRY) {
						return ENTRY_LABEL;
					} else {
						return new StringBuilder().append(this.createComponentLabelFromVertex(v)).append("\\n").append(this.createStatisticsFromVertex(v))
								.toString();
					}
				});

		return builder.build();
	}

	public DotExportConfiguration createForDeploymentLevelOperationDependencyGraph() {
		final DotExportConfiguration.Builder builder = this.createBaseBuilder();

		builder.addDefaultNodeAttribute(DotNodeAttribute.SHAPE, v -> "oval");

		builder.addNodeAttribute(DotNodeAttribute.LABEL,
				v -> {
					final VertexType type = this.getProperty(v, PropertyKeys.TYPE, VertexType.class);
					if (type == VertexType.ENTRY) {
						return ENTRY_LABEL;
					} else {
						return new StringBuilder().append(this.createOperationLabelFromVertex(v)).append("\\n").append(this.createStatisticsFromVertex(v))
								.toString();
					}
				});

		builder.addClusterAttribute(DotClusterAttribute.LABEL, v -> {
			final VertexType type = this.getProperty(v, PropertyKeys.TYPE, VertexType.class);
			switch (type) {
			case DEPLOYMENT_CONTEXT:
				return this.createContextLabelFromVertex(v).toString();
			case DEPLOYED_COMPONENT:
				return this.createComponentLabelFromVertex(v).toString();
			default:
				throw new IllegalArgumentException("Type '" + type.toString() + "' is not supported for this dependency graph.");
			}
		});

		return builder.build();
	}

	public DotExportConfiguration createForDeploymentLevelComponentDependencyGraph() {
		final DotExportConfiguration.Builder builder = this.createBaseBuilder();

		builder.addDefaultNodeAttribute(DotNodeAttribute.SHAPE, v -> "box");

		builder.addNodeAttribute(DotNodeAttribute.LABEL,
				v -> {
					final VertexType type = this.getProperty(v, PropertyKeys.TYPE, VertexType.class);
					if (type == VertexType.ENTRY) {
						return ENTRY_LABEL;
					} else {
						return new StringBuilder().append(this.createComponentLabelFromVertex(v)).append("\\n").append(this.createStatisticsFromVertex(v))
								.toString();
					}
				});

		builder.addClusterAttribute(DotClusterAttribute.LABEL, v -> this.createContextLabelFromVertex(v).toString());

		return builder.build();
	}

	public DotExportConfiguration createForDeploymentLevelContextDependencyGraph() {
		final DotExportConfiguration.Builder builder = this.createBaseBuilder();

		builder.addDefaultNodeAttribute(DotNodeAttribute.SHAPE, v -> "box3d");

		builder.addNodeAttribute(DotNodeAttribute.LABEL,
				v -> {
					final VertexType type = this.getProperty(v, PropertyKeys.TYPE, VertexType.class);
					if (type == VertexType.ENTRY) {
						return ENTRY_LABEL;
					} else {
						return this.createContextLabelFromVertex(v).toString();
					}
				});

		return builder.build();
	}

	private StringBuilder createType(final VertexType type) {
		return new StringBuilder().append("<<").append(this.vertexTypeMapper.apply(type)).append(">>");
	}

	private StringBuilder createOperationLabelFromVertex(final Vertex vertex) {

		@SuppressWarnings("unchecked")
		final Collection<String> modifiers = this.getProperty(vertex, PropertyKeys.MODIFIERS, Collection.class);
		final String returnType = this.getProperty(vertex, PropertyKeys.RETURN_TYPE, String.class);
		final String name = this.getProperty(vertex, PropertyKeys.NAME, String.class);
		@SuppressWarnings("unchecked")
		final Collection<String> parameterTypes = this.getProperty(vertex, PropertyKeys.PARAMETER_TYPES, Collection.class);

		return new StringBuilder(this.nameBuilder.getOperationNameBuilder().build(modifiers, returnType, name, parameterTypes));
	}

	private StringBuilder createComponentLabelFromVertex(final Vertex vertex) {
		final VertexType type = this.getProperty(vertex, PropertyKeys.TYPE, VertexType.class);
		final String name = this.getProperty(vertex, PropertyKeys.NAME, String.class);
		final String packageName = this.getProperty(vertex, PropertyKeys.PACKAGE_NAME, String.class);

		return new StringBuilder().append(this.createType(type)).append("\\n").append(this.nameBuilder.getComponentNameBuilder().build(packageName, name));
	}

	private StringBuilder createContextLabelFromVertex(final Vertex vertex) {
		final VertexType type = this.getProperty(vertex, PropertyKeys.TYPE, VertexType.class);
		final String name = this.getProperty(vertex, PropertyKeys.NAME, String.class);

		return new StringBuilder().append(this.createType(type)).append("\\n").append(name);
	}

	private StringBuilder createStatisticsFromVertex(final Vertex vertex) {
		final String timeUnit = this.getProperty(vertex, PropertyKeys.TIME_UNIT).toString();
		final String minResponseTime = this.getProperty(vertex, PropertyKeys.MIN_REPSONSE_TIME).toString();
		final String maxResponseTime = this.getProperty(vertex, PropertyKeys.MAX_REPSONSE_TIME).toString();
		final String totalResponseTime = this.getProperty(vertex, PropertyKeys.TOTAL_RESPONSE_TIME).toString();
		final String meanResponseTime = this.getProperty(vertex, PropertyKeys.MEAN_REPSONSE_TIME).toString();
		final String medianResponseTime = this.getProperty(vertex, PropertyKeys.MEDIAN_REPSONSE_TIME).toString();
		return this.createStatistics(timeUnit, minResponseTime, maxResponseTime, totalResponseTime, meanResponseTime, medianResponseTime);
	}

	private StringBuilder createStatistics(final String timeUnit, final String minResponseTime, final String maxResponseTime, final String totalResponseTime,
			final String meanResponseTime, final String medianResponseTime) {
		final StringBuilder builder = new StringBuilder();
		builder.append("min: ").append(minResponseTime).append(' ').append(timeUnit).append(", ");
		builder.append("max: ").append(maxResponseTime).append(' ').append(timeUnit).append(", ");
		builder.append("total: ").append(totalResponseTime).append(' ').append(timeUnit).append(",\\n");
		builder.append("avg: ").append(meanResponseTime).append(' ').append(timeUnit).append(", ");
		builder.append("median: ").append(medianResponseTime).append(' ').append(timeUnit);
		return builder;
	}

	// BETTER This could be moved to the graph library
	private Object getProperty(final Element element, final String key) {
		final Object object = element.getProperty(key);
		if (object == null) {
			throw new IllegalArgumentException("There is no key '" + key + "' for element '" + element + "'");
		}
		return object;
	}

	// BETTER This could be moved to the graph library
	private <T> T getProperty(final Element element, final String key, final Class<T> clazz) {
		final Object object = this.getProperty(element, key);
		if (!clazz.isInstance(object)) {
			throw new IllegalArgumentException("Object with key '" + key + "' is not of type '" + clazz + "'");
		}
		return clazz.cast(object);
	}

}
