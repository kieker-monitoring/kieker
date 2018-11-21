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

import kieker.tools.trace.analysis.filter.visualization.graph.AbstractVertexDecoration;
import kieker.tools.trace.analysis.systemModel.Execution;

/**
 * Response time decoration for graph vertices. This decoration extracts response times from executions and keeps track of the minimal, maximal and average response
 * time.
 * 
 * @author Holger Knoche
 * 
 * @since 1.5
 */
public class ResponseTimeDecoration extends AbstractVertexDecoration {

	private final TimeUnit executionTimeunit;
	private final TimeUnit displayTimeunit;
	private final String timeUnitShortname;

	private long responseTimeSum;
	private int executionCount;
	private long minimalResponseTime = Integer.MAX_VALUE;
	private long maximalResponseTime;

	/**
	 * Creates a new response time decoration.
	 * 
	 * @param executionTimeunit
	 *            The time unit which tells how to interpret the times of the executions.
	 */
	public ResponseTimeDecoration(final TimeUnit executionTimeunit, final TimeUnit displayTimeunit) {
		this.executionTimeunit = executionTimeunit;
		this.displayTimeunit = displayTimeunit;
		switch (displayTimeunit) {
		case NANOSECONDS:
			this.timeUnitShortname = "ns";
			break;
		case MICROSECONDS:
			this.timeUnitShortname = "us";
			break;
		case MILLISECONDS:
			this.timeUnitShortname = "ms";
			break;
		case SECONDS:
			this.timeUnitShortname = "s";
			break;
		default:
			this.timeUnitShortname = "??";
			break;
		}
	}

	/**
	 * Registers a given execution for the decorated vertex.
	 * 
	 * @param execution
	 *            The execution to register
	 */
	public void registerExecution(final Execution execution) {
		final long responseTime = this.displayTimeunit.convert(execution.getTout() - execution.getTin(), this.executionTimeunit);

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
	public long getMinimalResponseTime() {
		return this.minimalResponseTime;
	}

	/**
	 * Returns the maximal response time (in ms) registered by this decoration.
	 * 
	 * @return See above
	 */
	public long getMaximalResponseTime() {
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

	public long getTotalResponseTime() {
		return this.responseTimeSum;
	}

	@Override
	public String createFormattedOutput() {
		final StringBuilder sb = new StringBuilder(30);
		sb.append("min: ")
		  .append(this.getMinimalResponseTime())
		  .append(this.timeUnitShortname)
		  .append(", avg: ")
		  .append(Math.round(this.getAverageResponseTime()))
		  .append(this.timeUnitShortname)
		  .append(", max: ")
		  .append(this.getMaximalResponseTime())
		  .append(this.timeUnitShortname)
		  .append(",\\ntotal: ")
		  .append(this.getTotalResponseTime())
		  .append(this.timeUnitShortname);
		return sb.toString();
	}

}
