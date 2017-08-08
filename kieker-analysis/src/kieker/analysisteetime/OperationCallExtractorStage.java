/***************************************************************************
 * Copyright 2015 Kieker Project (http://kieker-monitoring.net)
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

package kieker.analysisteetime;

import kieker.analysisteetime.model.analysismodel.trace.OperationCall;
import kieker.analysisteetime.model.analysismodel.trace.Trace;
import kieker.analysisteetime.trace.traversal.OperationCallVisitor;
import kieker.analysisteetime.trace.traversal.TraceTraverser;

import teetime.stage.basic.AbstractTransformation;

/**
 * This stage accepts {@link Trace}s at its input port and sends all {@link OperationCall}s in this Traces to its output port.
 *
 * @author Sören Henning
 *
 * @since 1.13
 *
 */
public class OperationCallExtractorStage extends AbstractTransformation<Trace, OperationCall> {

	private final TraceTraverser traceTraverser = new TraceTraverser();
	private final Extractor extractor = new Extractor();

	public OperationCallExtractorStage() {}

	@Override
	protected void execute(final Trace trace) {
		this.traceTraverser.traverse(trace, this.extractor);
	}

	private class Extractor extends OperationCallVisitor {

		@Override
		public void visit(final OperationCall operationCall) {
			OperationCallExtractorStage.this.outputPort.send(operationCall);
		}

	}

}
