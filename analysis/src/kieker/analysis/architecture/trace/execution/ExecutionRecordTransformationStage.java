/***************************************************************************
 * Copyright 2022 Kieker Project (http://kieker-monitoring.net)
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
package kieker.analysis.architecture.trace.execution;

import kieker.analysis.architecture.trace.AbstractTraceAnalysisStage;
import kieker.common.record.controlflow.OperationExecutionRecord;
import kieker.common.util.signature.ClassOperationSignaturePair;
import kieker.common.util.signature.Signature;
import kieker.model.repository.SystemModelRepository;
import kieker.model.system.model.Execution;

import teetime.framework.OutputPort;

/**
 * Transforms {@link OperationExecutionRecord}s into {@link Execution}
 * objects.<br>
 *
 * This class has exactly one input port and one output port. It receives
 * objects inheriting from {@link OperationExecutionRecord}. The received object
 * is transformed into an instance of {@link Execution}.
 *
 * @author Andre van Hoorn
 * @author Reiner Jung -- teetime port
 *
 * @since 1.15
 */
public class ExecutionRecordTransformationStage extends AbstractTraceAnalysisStage<OperationExecutionRecord> {

	private final OutputPort<Execution> outputPort = this.createOutputPort(Execution.class);

	/**
	 * Creates a new instance of this class using the given parameters.
	 *
	 * @param repository
	 *            system model repository
	 */
	public ExecutionRecordTransformationStage(final SystemModelRepository repository) {
		super(repository);
	}

	/**
	 * This method represents the input port, processing incoming operation
	 * execution records.
	 *
	 * @param operationExecutionRecord
	 *            The next operation execution record.
	 */
	@Override
	protected void execute(final OperationExecutionRecord operationExecutionRecord) throws Exception {
		final String operationSignature = operationExecutionRecord.getOperationSignature();
		final boolean isConstructor = operationSignature.contains(Signature.CONSTRUCTOR_METHOD_NAME);

		final ClassOperationSignaturePair fqComponentNameSignaturePair = ClassOperationSignaturePair
				.splitOperationSignatureStr(operationExecutionRecord.getOperationSignature(), isConstructor);

		final Execution execution = this.createExecutionByEntityNames(operationExecutionRecord.getHostname(),
				fqComponentNameSignaturePair.getFqClassname(), fqComponentNameSignaturePair.getSignature(),
				operationExecutionRecord.getTraceId(), operationExecutionRecord.getSessionId(), operationExecutionRecord.getEoi(),
				operationExecutionRecord.getEss(), operationExecutionRecord.getTin(),
				operationExecutionRecord.getTout(), false);
		this.outputPort.send(execution);
	}

	public OutputPort<Execution> getOutputPort() {
		return this.outputPort;
	}

	@Override
	protected void onTerminating() {
		this.logger.debug("Terminating {}", this.getClass().getCanonicalName());
		super.onTerminating();
	}

}
