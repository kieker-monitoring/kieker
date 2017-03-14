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

		// TODO

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

}
