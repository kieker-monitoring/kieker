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

package kieker.tools.traceAnalysis.filter.visualization.dependencyGraph;

import kieker.tools.traceAnalysis.filter.visualization.graph.AbstractVertexDecoration;
import kieker.tools.traceAnalysis.systemModel.Execution;

/**
 * @author Holger Knoche
 */
public class ResponseTimeDecoration extends AbstractVertexDecoration {

	private static final String OUTPUT_TEMPLATE = "min: %dms, avg: %.2fms, max: %dms";

	// TODO Use TimeUnit instead (currently, we use milliseconds)
	private long responseTimeSum = 0;
	private int executionCount = 0;
	private int minimalResponseTime = Integer.MAX_VALUE;
	private int maximalResponseTime = 0;

	public ResponseTimeDecoration() {
		// empty default constructor
	}

	public void registerExecution(final Execution execution) {
		final int responseTime = (int) ((execution.getTout() / 1000000) - (execution.getTin() / 1000000));

		this.responseTimeSum = this.responseTimeSum + responseTime;
		this.executionCount++;

		if (responseTime < this.minimalResponseTime) {
			this.minimalResponseTime = responseTime;
		}
		if (responseTime > this.maximalResponseTime) {
			this.maximalResponseTime = responseTime;
		}
	}

	public int getMinimalResponseTime() {
		return this.minimalResponseTime;
	}

	public int getMaximalResponseTime() {
		return this.maximalResponseTime;
	}

	public double getAverageResponseTime() {
		return (this.executionCount == 0) ? 0 : ((double) this.responseTimeSum / (double) this.executionCount); // NOCS (inline ?)
	}

	@Override
	public String createFormattedOutput() {
		return String.format(OUTPUT_TEMPLATE, this.getMinimalResponseTime(), this.getAverageResponseTime(), this.getMaximalResponseTime());
	}

}
