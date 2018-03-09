/***************************************************************************
 * Copyright 2017 Kieker Project (http://kieker-monitoring.net)
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

package kieker.tools.traceAnalysis.filter.visualization.graph;

/**
 * This is an abstract base for vertex decorations.
 * 
 * @author Holger Knoche
 * 
 * @since 1.5
 */
public abstract class AbstractVertexDecoration {

	/**
	 * Creates formatted output for this decoration.
	 * 
	 * @return See above
	 */
	public abstract String createFormattedOutput();

}
