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

import java.nio.file.Path;

import kieker.analysis.graph.export.dot.DotExportConfiguration;
import kieker.analysis.graph.export.dot.DotFileWriterStage;
import kieker.analysis.graph.util.dot.attributes.DotEdgeAttribute;
import kieker.analysis.graph.util.dot.attributes.DotNodeAttribute;

/**
 * @author Sören Henning
 *
 * @since 1.14
 */
public class DotTraceGraphFileWriterStage extends DotFileWriterStage { // NOPMD (class serves only as constructor)

	public DotTraceGraphFileWriterStage(final Path outputDirectory, final DotExportConfiguration exportConfiguration) {
		super(outputDirectory, exportConfiguration);
	}

	public static DotTraceGraphFileWriterStage create(final Path outputDirectory) {
		final DotExportConfiguration.Builder exportConfigurationBuilder = new DotExportConfiguration.Builder();
		exportConfigurationBuilder.addDefaultNodeAttribute(DotNodeAttribute.SHAPE, g -> "none");
		exportConfigurationBuilder.addEdgeAttribute(DotEdgeAttribute.LABEL,
				e -> e.getProperty("orderIndex").toString() + '.');
		exportConfigurationBuilder.addNodeAttribute(DotNodeAttribute.LABEL, new NodeLabelMapper());
		final DotExportConfiguration exportConfiguration = exportConfigurationBuilder.build();

		return new DotTraceGraphFileWriterStage(outputDirectory, exportConfiguration);
	}

}
