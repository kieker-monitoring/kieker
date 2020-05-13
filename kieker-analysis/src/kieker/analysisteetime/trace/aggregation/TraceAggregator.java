/***************************************************************************
 * Copyright 2020 Kieker Project (http://kieker-monitoring.net)
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

import java.util.HashMap;
import java.util.Map;

import com.google.common.base.Equivalence;

import kieker.analysisteetime.model.analysismodel.trace.OperationCall;
import kieker.analysisteetime.model.analysismodel.trace.Trace;
import kieker.analysisteetime.model.analysismodel.trace.TraceFactory;

/**
 * This class creates aggregated traces from normal {@link Trace}s. Traces are aggregated if they
 * are considered equal using the {@link TraceEquivalence}.
 *
 * @author Sören Henning
 *
 * @since 1.14
 */
public class TraceAggregator {

	private final TraceFactory traceFactory = TraceFactory.eINSTANCE;
	private final boolean considerFailed;
	private final TraceEquivalence traceEquivalence;
	// BETTER use own class for aggregated traces
	private final Map<Equivalence.Wrapper<Trace>, Trace> aggregatedTraces = new HashMap<>(); // NOPMD (class not designed for concurrent access)

	public TraceAggregator(final boolean considerFailed) {
		this.considerFailed = considerFailed;
		this.traceEquivalence = new TraceEquivalence(considerFailed);
	}

	public AggregatedTraceWrapper handleTrace(final Trace trace) {
		final Equivalence.Wrapper<Trace> equalityWrappedTrace = this.traceEquivalence.wrap(trace);
		Trace aggregatedTrace = this.aggregatedTraces.get(equalityWrappedTrace);
		boolean isFirst = false;
		if (aggregatedTrace == null) {
			aggregatedTrace = this.createAggregatedTrace(trace);
			this.aggregatedTraces.put(equalityWrappedTrace, aggregatedTrace);
			isFirst = true;
		}
		return new AggregatedTraceWrapper(aggregatedTrace, trace, isFirst);
	}

	private Trace createAggregatedTrace(final Trace trace) {
		final OperationCall aggregatedRootOperationCall = this.createAggregatedOperationCall(trace.getRootOperationCall());
		final Trace aggregatedTrace = this.traceFactory.createTrace();
		aggregatedTrace.setRootOperationCall(aggregatedRootOperationCall);
		return aggregatedTrace;
	}

	private OperationCall createAggregatedOperationCall(final OperationCall call) {
		final OperationCall aggregatedCall = this.traceFactory.createOperationCall();
		if (this.considerFailed) {
			aggregatedCall.setFailed(call.isFailed());
			aggregatedCall.setFailedCause(call.getFailedCause());
		}
		call.getChildren().forEach(c -> aggregatedCall.getChildren().add(this.createAggregatedOperationCall(c)));
		return aggregatedCall;
	}

}
