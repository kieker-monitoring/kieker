/***************************************************************************
 * Copyright 2012 Kieker Project (http://kieker-monitoring.net)
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
 * 
 * @author Holger Knoche
 * 
 */

public abstract class AbstractNodeDecorator {

	public static AbstractNodeDecorator createFromName(final String optionName) {
		if (Constants.RESPONSE_TIME_DECORATOR_FLAG.equals(optionName)) {
			return new ResponseTimeNodeDecorator();
		}

		return null;
	}

	public abstract void processMessage(AbstractMessage message, DependencyGraphNode<?> sourceNode, DependencyGraphNode<?> targetNode);

}
