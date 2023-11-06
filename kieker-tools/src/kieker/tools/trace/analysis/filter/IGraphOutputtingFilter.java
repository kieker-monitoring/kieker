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

package kieker.tools.trace.analysis.filter;

import kieker.tools.trace.analysis.filter.visualization.graph.AbstractGraph;

/**
 * Interface for graph-outputting filters.
 *
 * @author Holger Knoche
 *
 * @param <G>
 *            The type of the output graph
 *
 * @since 1.6
 * @deprecated 1.15 has no ressemblence in the teetime port
 */
@Deprecated
public interface IGraphOutputtingFilter<G extends AbstractGraph<?, ?, ?>> {

	/**
	 * The default name for graph output ports.
	 */
	public static final String OUTPUT_PORT_NAME_GRAPH = "graphs";

	/**
	 * Returns the name of the port this filter uses to emit the graph.
	 *
	 * @return See above
	 *
	 * @since 1.6
	 */
	public String getGraphOutputPortName();

}
