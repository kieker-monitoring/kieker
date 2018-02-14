/***************************************************************************
 * Copyright 2018 Kieker Project (http://kieker-monitoring.net)
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

package kieker.analysisteetime.trace.aggregation;

import kieker.analysisteetime.model.analysismodel.trace.Trace;

/**
 * This is wrapper class for aggregated traces that also contains a specific trace to which the
 * aggregated trace belongs to and the information whether this is the first time this aggregated
 * trace was created.
 *
 * This class is internally used to be created from {@link TraceAggregator} and processed from statistics
 * stages.
 *
 * @author Sören Henning
 *
 * @since 1.14
 */
public final class AggregatedTraceWrapper {

	private final Trace aggregatedTrace; // BETTER use own class for aggregated traces
	private final Trace trace;
	private final boolean isFirst; // NOPMD (attribute name is intended)

	public AggregatedTraceWrapper(final Trace aggregatedTrace, final Trace trace, final boolean isFirst) {
		this.aggregatedTrace = aggregatedTrace;
		this.trace = trace;
		this.isFirst = isFirst;
	}

	public Trace getAggregatedTrace() {
		return this.aggregatedTrace;
	}

	public Trace getTrace() {
		return this.trace;
	}

	public boolean isFirst() {
		return this.isFirst;
	}
}
