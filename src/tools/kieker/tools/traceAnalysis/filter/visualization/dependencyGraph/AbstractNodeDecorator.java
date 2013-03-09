/***************************************************************************
 * Copyright 2013 Kieker Project (http://kieker-monitoring.net)
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

package kieker.tools.traceAnalysis.filter.visualization.dependencyGraph;

import kieker.tools.traceAnalysis.Constants;
import kieker.tools.traceAnalysis.systemModel.AbstractMessage;

/**
 * Abstract superclass for all node decorators.
 * 
 * @author Holger Knoche
 * 
 */
public abstract class AbstractNodeDecorator {

	/**
	 * Creates a node decorator from its option name.
	 * 
	 * @param optionName
	 *            The option name to create a decorator for
	 * @return An appropriate node decorator or {@code null} if none can be determined
	 */
	public static AbstractNodeDecorator createFromName(final String optionName) {
		if (Constants.RESPONSE_TIME_DECORATOR_FLAG.equals(optionName)) {
			return new ResponseTimeNodeDecorator();
		}

		return null;
	}

	/**
	 * Processes a message sent from the given source to the given target node.
	 * 
	 * @param message
	 *            The sent message
	 * @param sourceNode
	 *            The source node sending the message
	 * @param targetNode
	 *            The target node receiving the message
	 */
	public abstract void processMessage(AbstractMessage message, DependencyGraphNode<?> sourceNode, DependencyGraphNode<?> targetNode);

}
