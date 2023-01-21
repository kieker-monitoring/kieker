/***************************************************************************
 * Copyright 2022 Kieker Project (http://kieker-monitoring.net)
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

package kieker.analysis.architecture.dependency.export.dot;

import java.util.Collection;

import kieker.analysis.architecture.dependency.IVertexTypeMapper;
import kieker.analysis.architecture.dependency.PropertyConstants;
import kieker.analysis.architecture.dependency.VertexType;
import kieker.analysis.architecture.recovery.signature.NameBuilder;
import kieker.analysis.generic.graph.IEdge;
import kieker.analysis.generic.graph.IElement;
import kieker.analysis.generic.graph.INode;
import kieker.analysis.generic.sink.graph.dot.DotExportBuilder;
import kieker.analysis.generic.sink.graph.dot.DotExportMapper;
import kieker.analysis.generic.sink.graph.dot.attributes.DotClusterAttribute;
import kieker.analysis.generic.sink.graph.dot.attributes.DotEdgeAttribute;
import kieker.analysis.generic.sink.graph.dot.attributes.DotGraphAttribute;
import kieker.analysis.generic.sink.graph.dot.attributes.DotNodeAttribute;

/**
 * @author Sören Henning
 *
 * @since 1.14
 */
public class DotExportDependencyGraphFactory {

	private static final String ENTRY_LABEL = "'Entry'";

	private final NameBuilder nameBuilder;
	private final IVertexTypeMapper vertexTypeMapper;

	/**
	 * Uses TO_STRING of {@link kieker.analysis.architecture.dependency.IVertexTypeMapper} as second default argument.
	 *
	 * @param nameBuilder
	 *            label for the builder
	 */
	public DotExportDependencyGraphFactory(final NameBuilder nameBuilder) {
		this(nameBuilder, IVertexTypeMapper.TO_STRING);
	}

	public DotExportDependencyGraphFactory(final NameBuilder nameBuilder, final IVertexTypeMapper vertexTypeMapper) {
		this.nameBuilder = nameBuilder;
		this.vertexTypeMapper = vertexTypeMapper;
	}

	private DotExportBuilder<INode, IEdge> createBaseBuilder() {
		final DotExportBuilder<INode, IEdge> builder = new DotExportBuilder<>();

		builder.addGraphAttribute(DotGraphAttribute.RANKDIR, g -> "LR");
		builder.addDefaultEdgeAttribute(DotEdgeAttribute.STYLE, g -> "solid");
		builder.addDefaultEdgeAttribute(DotEdgeAttribute.ARROWHEAD, g -> "open");
		builder.addDefaultEdgeAttribute(DotEdgeAttribute.COLOR, g -> "#000000");

		builder.addDefaultNodeAttribute(DotNodeAttribute.STYLE, g -> "filled");
		builder.addDefaultNodeAttribute(DotNodeAttribute.COLOR, g -> "#000000");
		builder.addDefaultNodeAttribute(DotNodeAttribute.FILLCOLOR, g -> "white");

		builder.addEdgeAttribute(DotEdgeAttribute.LABEL, e -> this.getProperty(e, PropertyConstants.CALLS).toString());

		builder.addClusterAttribute(DotClusterAttribute.STYLE, v -> "filled");
		builder.addClusterAttribute(DotClusterAttribute.FILLCOLOR, v -> "white");

		return builder;
	}

	public DotExportMapper<INode, IEdge> createForTypeLevelOperationDependencyGraph() {
		final DotExportBuilder<INode, IEdge> builder = this.createBaseBuilder();

		builder.addDefaultNodeAttribute(DotNodeAttribute.SHAPE, v -> "oval");

		builder.addNodeAttribute(DotNodeAttribute.LABEL, v -> {
			final VertexType type = this.getProperty(v, PropertyConstants.TYPE, VertexType.class);
			if (type == VertexType.ENTRY) {
				return ENTRY_LABEL;
			} else {
				return new StringBuilder().append(this.createOperationLabelFromVertex(v)).append(System.lineSeparator())
						.append(this.createStatisticsFromVertex(v)).toString();
			}
		});

		builder.addClusterAttribute(DotClusterAttribute.LABEL, v -> this.createComponentLabelFromVertex(v).toString());

		return builder.build();
	}

	public DotExportMapper<INode, IEdge> createForTypeLevelComponentDependencyGraph() {
		final DotExportBuilder<INode, IEdge> builder = this.createBaseBuilder();

		builder.addDefaultNodeAttribute(DotNodeAttribute.SHAPE, v -> "box");

		builder.addNodeAttribute(DotNodeAttribute.LABEL, v -> {
			final VertexType type = this.getProperty(v, PropertyConstants.TYPE, VertexType.class);
			if (type == VertexType.ENTRY) {
				return ENTRY_LABEL;
			} else {
				return new StringBuilder().append(this.createComponentLabelFromVertex(v)).append(System.lineSeparator())
						.append(this.createStatisticsFromVertex(v)).toString();
			}
		});

		return builder.build();
	}

	public DotExportMapper<INode, IEdge> createForAssemblyLevelOperationDependencyGraph() {
		final DotExportBuilder<INode, IEdge> builder = this.createBaseBuilder();

		builder.addDefaultNodeAttribute(DotNodeAttribute.SHAPE, v -> "oval");

		builder.addNodeAttribute(DotNodeAttribute.LABEL, v -> {
			final VertexType type = this.getProperty(v, PropertyConstants.TYPE, VertexType.class);
			if (type == VertexType.ENTRY) {
				return ENTRY_LABEL;
			} else {
				return new StringBuilder().append(this.createOperationLabelFromVertex(v)).append("\\n")
						.append(this.createStatisticsFromVertex(v)).toString();
			}
		});

		builder.addClusterAttribute(DotClusterAttribute.LABEL, v -> this.createComponentLabelFromVertex(v).toString());

		return builder.build();
	}

	public DotExportMapper<INode, IEdge> createForAssemblyLevelComponentDependencyGraph() {
		final DotExportBuilder<INode, IEdge> builder = this.createBaseBuilder();

		builder.addDefaultNodeAttribute(DotNodeAttribute.SHAPE, v -> "box");

		builder.addNodeAttribute(DotNodeAttribute.LABEL, v -> {
			final VertexType type = this.getProperty(v, PropertyConstants.TYPE, VertexType.class);
			if (type == VertexType.ENTRY) {
				return ENTRY_LABEL;
			} else {
				return new StringBuilder().append(this.createComponentLabelFromVertex(v)).append(System.lineSeparator())
						.append(this.createStatisticsFromVertex(v)).toString();
			}
		});

		return builder.build();
	}

	public DotExportMapper<INode, IEdge> createForDeploymentLevelOperationDependencyGraph() {
		final DotExportBuilder<INode, IEdge> builder = this.createBaseBuilder();

		builder.addDefaultNodeAttribute(DotNodeAttribute.SHAPE, v -> "oval");

		builder.addNodeAttribute(DotNodeAttribute.LABEL, v -> {
			final VertexType type = this.getProperty(v, PropertyConstants.TYPE, VertexType.class);
			if (type == VertexType.ENTRY) {
				return ENTRY_LABEL;
			} else {
				return new StringBuilder().append(this.createOperationLabelFromVertex(v)).append(System.lineSeparator())
						.append(this.createStatisticsFromVertex(v)).toString();
			}
		});

		builder.addClusterAttribute(DotClusterAttribute.LABEL, v -> {
			final VertexType type = this.getProperty(v, PropertyConstants.TYPE, VertexType.class);
			switch (type) {
			case DEPLOYMENT_CONTEXT:
				return this.createContextLabelFromVertex(v).toString();
			case DEPLOYED_COMPONENT:
				return this.createComponentLabelFromVertex(v).toString();
			default:
				throw new IllegalArgumentException(
						"Type '" + type.toString() + "' is not supported for this dependency graph.");
			}
		});

		return builder.build();
	}

	public DotExportMapper<INode, IEdge> createForDeploymentLevelComponentDependencyGraph() {
		final DotExportBuilder<INode, IEdge> builder = this.createBaseBuilder();

		builder.addDefaultNodeAttribute(DotNodeAttribute.SHAPE, v -> "box");

		builder.addNodeAttribute(DotNodeAttribute.LABEL, v -> {
			final VertexType type = this.getProperty(v, PropertyConstants.TYPE, VertexType.class);
			if (type == VertexType.ENTRY) {
				return ENTRY_LABEL;
			} else {
				return new StringBuilder().append(this.createComponentLabelFromVertex(v)).append(System.lineSeparator())
						.append(this.createStatisticsFromVertex(v)).toString();
			}
		});

		builder.addClusterAttribute(DotClusterAttribute.LABEL, v -> this.createContextLabelFromVertex(v).toString());

		return builder.build();
	}

	public DotExportMapper<INode, IEdge> createForDeploymentLevelContextDependencyGraph() {
		final DotExportBuilder<INode, IEdge> builder = this.createBaseBuilder();

		builder.addDefaultNodeAttribute(DotNodeAttribute.SHAPE, v -> "box3d");

		builder.addNodeAttribute(DotNodeAttribute.LABEL, v -> {
			final VertexType type = this.getProperty(v, PropertyConstants.TYPE, VertexType.class);
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

	private StringBuilder createOperationLabelFromVertex(final INode vertex) {
		@SuppressWarnings("unchecked")
		final Collection<String> modifiers = this.getProperty(vertex, PropertyConstants.MODIFIERS, Collection.class);
		final String returnType = this.getProperty(vertex, PropertyConstants.RETURN_TYPE, String.class);
		final String name = this.getProperty(vertex, PropertyConstants.NAME, String.class);
		@SuppressWarnings("unchecked")
		final Collection<String> parameterTypes = this.getProperty(vertex, PropertyConstants.PARAMETER_TYPES,
				Collection.class);

		return new StringBuilder(
				this.nameBuilder.getOperationNameBuilder().build(modifiers, returnType, name, parameterTypes));
	}

	private StringBuilder createComponentLabelFromVertex(final INode vertex) {
		final VertexType type = this.getProperty(vertex, PropertyConstants.TYPE, VertexType.class);
		final String name = this.getProperty(vertex, PropertyConstants.NAME, String.class);
		final String packageName = this.getProperty(vertex, PropertyConstants.PACKAGE_NAME, String.class);

		return new StringBuilder().append(this.createType(type)).append(System.lineSeparator())
				.append(this.nameBuilder.getComponentNameBuilder().build(packageName, name));
	}

	private StringBuilder createContextLabelFromVertex(final INode vertex) {
		final VertexType type = this.getProperty(vertex, PropertyConstants.TYPE, VertexType.class);
		final String name = this.getProperty(vertex, PropertyConstants.NAME, String.class);

		return new StringBuilder().append(this.createType(type)).append(System.lineSeparator()).append(name);
	}

	private StringBuilder createStatisticsFromVertex(final INode vertex) {
		final String timeUnit = this.getProperty(vertex, PropertyConstants.TIME_UNIT).toString();
		final String minResponseTime = this.getProperty(vertex, PropertyConstants.MIN_REPSONSE_TIME).toString();
		final String maxResponseTime = this.getProperty(vertex, PropertyConstants.MAX_REPSONSE_TIME).toString();
		final String totalResponseTime = this.getProperty(vertex, PropertyConstants.TOTAL_RESPONSE_TIME).toString();
		final String meanResponseTime = this.getProperty(vertex, PropertyConstants.MEAN_REPSONSE_TIME).toString();
		final String medianResponseTime = this.getProperty(vertex, PropertyConstants.MEDIAN_REPSONSE_TIME).toString();
		return this.createStatistics(timeUnit, minResponseTime, maxResponseTime, totalResponseTime, meanResponseTime,
				medianResponseTime);
	}

	private StringBuilder createStatistics(final String timeUnit, final String minResponseTime,
			final String maxResponseTime, final String totalResponseTime, final String meanResponseTime,
			final String medianResponseTime) {
		return new StringBuilder()
				.append("min: ").append(minResponseTime).append(' ').append(timeUnit).append(", ")
				.append("max: ").append(maxResponseTime).append(' ').append(timeUnit).append(", ")
				.append("total: ").append(totalResponseTime).append(' ').append(timeUnit).append(',').append(System.lineSeparator())
				.append("avg: ").append(meanResponseTime).append(' ').append(timeUnit).append(", ")
				.append("median: ").append(medianResponseTime).append(' ').append(timeUnit);
	}

	// BETTER This could be moved to the graph library
	private Object getProperty(final IElement element, final String key) {
		final Object object = element.getProperty(key);
		if (object == null) {
			throw new IllegalArgumentException("There is no key '" + key + "' for element '" + element + "'");
		}
		return object;
	}

	// BETTER This could be moved to the graph library
	private <T> T getProperty(final IElement element, final String key, final Class<T> clazz) {
		final Object object = this.getProperty(element, key);
		if (!clazz.isInstance(object)) {
			throw new IllegalArgumentException("Object with key '" + key + "' is not of type '" + clazz + "'");
		}
		return clazz.cast(object);
	}

}
