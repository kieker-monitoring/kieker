/***************************************************************************
 * Copyright 2023 Kieker Project (http://kieker-monitoring.net)
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

package kieker.analysis.architecture.trace.aggregation;

import kieker.model.analysismodel.trace.Trace;

import teetime.stage.basic.AbstractTransformation;

/**
 * This class is a TeeTime stage that transforms a {@link Trace} to an {@link AggregatedTraceWrapper}.
 *
 * It uses an {@link TraceAggregator} under the hood and calls {@link TraceAggregator#handleTrace(Trace)}
 * for every incoming trace.
 *
 * @author Sören Henning
 *
 * @since 1.14
 */
public class AggregatedTraceCreatorStage extends AbstractTransformation<Trace, AggregatedTraceWrapper> {

	private final TraceAggregator aggregatedTraceCreator;

	public AggregatedTraceCreatorStage(final boolean considerFailed) {
		this.aggregatedTraceCreator = new TraceAggregator(considerFailed);
	}

	@Override
	protected void execute(final Trace trace) {
		final AggregatedTraceWrapper aggregatedTrace = this.aggregatedTraceCreator.handleTrace(trace);
		this.outputPort.send(aggregatedTrace);
	}

}
