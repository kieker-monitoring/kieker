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

package kieker.analysis.trace.aggregation;

import kieker.model.analysismodel.trace.Trace;

import teetime.framework.CompositeStage;
import teetime.framework.InputPort;
import teetime.framework.OutputPort;
import teetime.stage.basic.ITransformation;

/**
 * @author Sören Henning
 *
 * @since 1.14
 */
public class TraceAggregatorStage extends CompositeStage implements ITransformation<Trace, Trace> {

	private final InputPort<Trace> inputPort;
	private final OutputPort<Trace> outputPort;

	public TraceAggregatorStage() {
		final AggregatedTraceCreatorStage aggregatedTraceCreator = new AggregatedTraceCreatorStage(true);
		// BETTER Statistics stages here
		final AggregatedTraceUnwrapperStage aggregatedTraceUnwrapper = new AggregatedTraceUnwrapperStage();

		this.inputPort = aggregatedTraceCreator.getInputPort();
		this.outputPort = aggregatedTraceUnwrapper.getOutputPort();

		this.connectPorts(aggregatedTraceCreator.getOutputPort(), aggregatedTraceUnwrapper.getInputPort());
	}

	@Override
	public InputPort<Trace> getInputPort() {
		return this.inputPort;
	}

	@Override
	public OutputPort<Trace> getOutputPort() {
		return this.outputPort;
	}

}
