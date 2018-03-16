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

import teetime.stage.basic.AbstractTransformation;

/**
 * This class is a TeeTime stage that accepts {@link AggregatedTraceWrapper} elements at its input port
 * and send sends their containing aggregated trace to the stage�s output port if
 * {@link AggregatedTraceWrapper#isFirst()} returns true.
 *
 * @author Sören Henning
 *
 * @since 1.14
 */
public class AggregatedTraceUnwrapperStage extends AbstractTransformation<AggregatedTraceWrapper, Trace> {

	public AggregatedTraceUnwrapperStage() {
		super();
	}

	@Override
	protected void execute(final AggregatedTraceWrapper wrapper) {
		if (wrapper.isFirst()) {
			final Trace trace = wrapper.getTrace();
			this.outputPort.send(trace);
		}
	}

}
