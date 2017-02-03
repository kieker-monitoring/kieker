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
package kieker.analysisteetime.trace.graph.dot;

import kieker.analysisteetime.util.graph.export.dot.DotExportConfiguration;
import kieker.analysisteetime.util.graph.export.dot.DotFileWriterStage;
import kieker.analysisteetime.util.graph.util.dot.attributes.DotEdgeAttribute;
import kieker.analysisteetime.util.graph.util.dot.attributes.DotNodeAttribute;

/**
 * @author Sören Henning
 *
 * @since 1.13
 */
public class DotTraceGraphFileWriterStage extends DotFileWriterStage {

	public DotTraceGraphFileWriterStage(final String outputDirectory, final DotExportConfiguration exportConfiguration) {
		super(outputDirectory, exportConfiguration);
	}

	public static DotTraceGraphFileWriterStage create(final String outputDirectory) {
		final DotExportConfiguration exportConfiguration = new DotExportConfiguration();
		exportConfiguration.getDefaultNodeAttributes().put(DotNodeAttribute.SHAPE, g -> "none");
		exportConfiguration.getEdgeAttributes().put(DotEdgeAttribute.LABEL, e -> e.getProperty("orderIndex").toString() + '.');
		exportConfiguration.getNodeAttributes().put(DotNodeAttribute.LABEL, new NodeLabelMapper());

		return new DotTraceGraphFileWriterStage(outputDirectory, exportConfiguration);
	}

}
