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

import java.util.concurrent.TimeUnit;

import kieker.tools.traceAnalysis.filter.visualization.graph.AbstractVertexDecoration;
import kieker.tools.traceAnalysis.systemModel.Execution;

/**
 * Response time decoration for graph vertices. This decoration extracts response times from executions
 * and keeps track of the minimal, maximal and average response time.
 * 
 * @author Holger Knoche
 */
public class ResponseTimeDecoration extends AbstractVertexDecoration {

	private static final String OUTPUT_TEMPLATE = "min: %dms, avg: %.2fms, max: %dms";

	private static final TimeUnit DISPLAY_TIMEUNIT = TimeUnit.MILLISECONDS;

	private final TimeUnit executionTimeunit;

	private long responseTimeSum;
	private int executionCount;
	private int minimalResponseTime = Integer.MAX_VALUE;
	private int maximalResponseTime;

	/**
	 * Creates a new response time decoration.
	 */
	public ResponseTimeDecoration(final TimeUnit executionTimeunit) {
		this.executionTimeunit = executionTimeunit;
	}

	/**
	 * Registers a given execution for the decorated vertex.
	 * 
	 * @param execution
	 *            The execution to register
	 */
	public void registerExecution(final Execution execution) {
		final int responseTime = (int) DISPLAY_TIMEUNIT.convert(execution.getTout() - execution.getTin(), this.executionTimeunit);

		this.responseTimeSum = this.responseTimeSum + responseTime;
		this.executionCount++;

		if (responseTime < this.minimalResponseTime) {
			this.minimalResponseTime = responseTime;
		}
		if (responseTime > this.maximalResponseTime) {
			this.maximalResponseTime = responseTime;
		}
	}

	/**
	 * Returns the minimal response time (in ms) registered by this decoration.
	 * 
	 * @return See above
	 */
	public int getMinimalResponseTime() {
		return this.minimalResponseTime;
	}

	/**
	 * Returns the maximal response time (in ms) registered by this decoration.
	 * 
	 * @return See above
	 */
	public int getMaximalResponseTime() {
		return this.maximalResponseTime;
	}

	/**
	 * Returns the average response time (in ms) registered by this decoration.
	 * 
	 * @return See above
	 */
	public double getAverageResponseTime() {
		return (this.executionCount == 0) ? 0 : ((double) this.responseTimeSum / (double) this.executionCount); // NOCS (inline ?)
	}

	@Override
	public String createFormattedOutput() {
		return String.format(OUTPUT_TEMPLATE, this.getMinimalResponseTime(), this.getAverageResponseTime(), this.getMaximalResponseTime());
	}

}
