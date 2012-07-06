/***************************************************************************
 * Copyright 2012 by
 *  + Christian-Albrechts-University of Kiel
 *    + Department of Computer Science
 *      + Software Engineering Group 
 *  and others.
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

package kieker.tools.traceAnalysis.filter;

import kieker.tools.traceAnalysis.filter.visualization.graph.AbstractGraph;

/**
 * Interface for graph-producing filters.
 * 
 * @author Holger Knoche
 * 
 * @param <G>
 *            The type of the produced graph
 */
public interface IGraphOutputtingFilter<G extends AbstractGraph<?, ?, ?>> {

	public static final String GRAPH_OUTPUT_PORT_NAME = "graphOutput";

	/**
	 * Returns the name of the port this filter uses to emit the graph.
	 * 
	 * @return See above
	 */
	public String getGraphOutputPortName();

}
