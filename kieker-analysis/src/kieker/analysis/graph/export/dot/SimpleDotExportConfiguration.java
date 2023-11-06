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

package kieker.analysis.graph.export.dot;

import kieker.analysis.graph.mapping.DirectPropertyMapper;
import kieker.analysis.graph.util.dot.attributes.DotEdgeAttribute;
import kieker.analysis.graph.util.dot.attributes.DotNodeAttribute;

/**
 * A {@link DotExportConfiguration} that maps the property <em>label</em> of edges
 * and vertices to the <em>label</em> attribute of the corresponding nodes and
 * edges of the dot graph.
 *
 * @author Sören Henning
 *
 * @since 1.14
 */
public class SimpleDotExportConfiguration extends DotExportConfiguration {

	public SimpleDotExportConfiguration() {
		super();
		super.nodeAttributes.put(DotNodeAttribute.LABEL, DirectPropertyMapper.of("label"));
		super.edgeAttributes.put(DotEdgeAttribute.LABEL, DirectPropertyMapper.of("label"));
	}

}
