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

package kieker.tools.trace.analysis.filter.visualization.dependencyGraph;

import java.util.concurrent.TimeUnit;

import kieker.tools.trace.analysis.filter.visualization.graph.Color;
import kieker.tools.trace.analysis.systemModel.AbstractMessage;

/**
 * Decorator to set the color of graph nodes depending on graph nodes execution time.
 * 
 * @author Henry Grow
 * 
 * @since 1.9
 */
public class ResponseTimeColorNodeDecorator extends AbstractNodeDecorator {

	private static final TimeUnit DISPLAY_TIMEUNIT = TimeUnit.MILLISECONDS;

	private static final Color COLOR = Color.RED;

	private final int threshold;

	/**
	 * Creates a new response time decorator.
	 * 
	 * @param threshold
	 *            The threshold for the execution time to color the graph nodes
	 */
	public ResponseTimeColorNodeDecorator(final int threshold) {
		this.threshold = threshold;
	}

	@Override
	public void processMessage(final AbstractMessage message, final DependencyGraphNode<?> sourceNode, final DependencyGraphNode<?> targetNode,
			final TimeUnit timeunit) {
		// Ignore internal executions
		if (sourceNode.equals(targetNode)) {
			return;
		}

		final long responseTime = message.getReceivingExecution().getTout() - message.getReceivingExecution().getTin();

		final int convertedResponseTime = (int) DISPLAY_TIMEUNIT.convert(responseTime, timeunit);

		if (convertedResponseTime > this.threshold) {
			targetNode.setColor(COLOR);
		}

	}

}
