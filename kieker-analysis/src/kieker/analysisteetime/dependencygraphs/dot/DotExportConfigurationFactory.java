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

import kieker.analysisteetime.dependencygraphs.PropertyKeys;
import kieker.analysisteetime.dependencygraphs.vertextypes.VertexType;
import kieker.analysisteetime.dependencygraphs.vertextypes.VertexTypeMapper;
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

	private final VertexTypeMapper vertexTypeMapper = VertexTypeMapper.DEFAULT; // TODO Temp

	private DotExportConfiguration.Builder createBaseBuilder() {
		final DotExportConfiguration.Builder builder = new DotExportConfiguration.Builder();

		builder.addGraphAttribute(DotGraphAttribute.RANKDIR, g -> "LR");
		builder.addDefaultEdgeAttribute(DotEdgeAttribute.STYLE, g -> "solid");
		builder.addDefaultEdgeAttribute(DotEdgeAttribute.ARROWHEAD, g -> "open");
		builder.addDefaultEdgeAttribute(DotEdgeAttribute.COLOR, g -> "#000000");

		builder.addDefaultNodeAttribute(DotNodeAttribute.STYLE, g -> "filled");
		builder.addDefaultNodeAttribute(DotNodeAttribute.COLOR, g -> "#000000");
		builder.addDefaultNodeAttribute(DotNodeAttribute.FILLCOLOR, g -> "white");

		builder.addNodeAttribute(DotNodeAttribute.LABEL, v -> "node label"); // TODO TEMP

		builder.addEdgeAttribute(DotEdgeAttribute.LABEL, e -> e.getProperty(PropertyKeys.CALLS));

		builder.addClusterAttribute(DotClusterAttribute.STYLE, v -> "filled");
		builder.addClusterAttribute(DotClusterAttribute.FILLCOLOR, v -> "white");

		return builder;
	}

	public DotExportConfiguration createForTypeLevelOperationDependencyGraph() {
		final DotExportConfiguration.Builder builder = this.createBaseBuilder();

		builder.addDefaultNodeAttribute(DotNodeAttribute.SHAPE, v -> "oval");

		// TODO

		return builder.build();
	}

	public DotExportConfiguration createForTypeLevelComponentDependencyGraph() {
		final DotExportConfiguration.Builder builder = this.createBaseBuilder();

		builder.addDefaultNodeAttribute(DotNodeAttribute.SHAPE, v -> "box");

		// TODO

		return builder.build();
	}

	public DotExportConfiguration createForAssemblyLevelOperationDependencyGraph() {
		final DotExportConfiguration.Builder builder = this.createBaseBuilder();

		builder.addDefaultNodeAttribute(DotNodeAttribute.SHAPE, v -> "oval");

		// TODO

		return builder.build();
	}

	public DotExportConfiguration createForAssemblyLevelComponentDependencyGraph() {
		final DotExportConfiguration.Builder builder = this.createBaseBuilder();

		builder.addDefaultNodeAttribute(DotNodeAttribute.SHAPE, v -> "box");

		// TODO

		return builder.build();
	}

	public DotExportConfiguration createForDeploymentLevelOperationDependencyGraph() {
		final DotExportConfiguration.Builder builder = this.createBaseBuilder();

		builder.addDefaultNodeAttribute(DotNodeAttribute.SHAPE, v -> "oval");

		builder.addNodeAttribute(DotNodeAttribute.LABEL, vertex -> {
			final String name = vertex.getProperty(PropertyKeys.NAME); // TODO use das neue ding
			final StringBuilder statistics = this.createStatisticsFromVertex(vertex);

			return name + '\n' + statistics;
		});

		builder.addClusterAttribute(DotClusterAttribute.LABEL, v -> {
			final Object uncastedType = v.getProperty(PropertyKeys.TYPE);
			final VertexType type = VertexType.class.cast(uncastedType);

			switch (type) {
			case DEPLOYMENT_CONTEXT:
				final String contextName = v.getProperty(PropertyKeys.NAME); // TODO use das neue ding
				return this.createType(type) + '\n' + contextName;
			case DEPLOYED_COMPONENT:
				final String componentName = v.getProperty(PropertyKeys.NAME); // TODO use das neue ding
				return this.createType(type) + '\n' + componentName;
			default:
				throw new IllegalArgumentException(); // TODO
			}
		});

		return builder.build();
	}

	public DotExportConfiguration createForDeploymentLevelComponentDependencyGraph() {
		final DotExportConfiguration.Builder builder = this.createBaseBuilder();

		builder.addDefaultNodeAttribute(DotNodeAttribute.SHAPE, v -> "box");

		// TODO

		return builder.build();
	}

	public DotExportConfiguration createForDeploymentLevelContextDependencyGraph() {
		final DotExportConfiguration.Builder builder = this.createBaseBuilder();

		builder.addDefaultNodeAttribute(DotNodeAttribute.SHAPE, v -> "box3d");

		// TODO

		return builder.build();
	}

	private String createType(final VertexType type) {
		return "<<" + this.vertexTypeMapper.apply(type) + ">>";
	}

	private StringBuilder createStatisticsFromVertex(final Vertex vertex) {
		final String timeUnit = vertex.getProperty(PropertyKeys.TIME_UNIT);
		final String minResponseTime = vertex.getProperty(PropertyKeys.MIN_REPSONSE_TIME);
		final String maxResponseTime = vertex.getProperty(PropertyKeys.MAX_REPSONSE_TIME);
		final String totalResponseTime = vertex.getProperty(PropertyKeys.TOTAL_REPSONSE_TIME);
		final String meanResponseTime = vertex.getProperty(PropertyKeys.MEAN_REPSONSE_TIME);
		final String medianResponseTime = vertex.getProperty(PropertyKeys.MEDIAN_REPSONSE_TIME);
		return this.createStatistics(timeUnit, minResponseTime, maxResponseTime, totalResponseTime, meanResponseTime, medianResponseTime);
	}

	private StringBuilder createStatistics(final String timeUnit, final String minResponseTime, final String maxResponseTime, final String totalResponseTime,
			final String meanResponseTime, final String medianResponseTime) {
		final StringBuilder builder = new StringBuilder();
		builder.append("min: ").append(minResponseTime).append(' ').append(timeUnit).append(", ");
		builder.append("max: ").append(maxResponseTime).append(' ').append(timeUnit).append(", ");
		builder.append("total: ").append(totalResponseTime).append(' ').append(timeUnit).append(",\n");
		builder.append("avg: ").append(meanResponseTime).append(' ').append(timeUnit).append(", ");
		builder.append("median: ").append(medianResponseTime).append(' ').append(timeUnit);
		return builder;
	}

}
