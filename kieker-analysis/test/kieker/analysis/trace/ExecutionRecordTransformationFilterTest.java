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
package kieker.analysis.trace;

import java.util.List;

import org.hamcrest.MatcherAssert;
import org.junit.Assert;
import org.junit.Test;

import kieker.analysis.exception.AnalysisConfigurationException;
import kieker.analysis.trace.execution.ExecutionRecordTransformationStage;
import kieker.common.record.controlflow.OperationExecutionRecord;
import kieker.common.util.signature.ClassOperationSignaturePair;
import kieker.model.repository.SystemModelRepository;
import kieker.model.system.model.Execution;

import kieker.test.common.util.record.BookstoreOperationExecutionRecordFactory;

import teetime.framework.test.StageTester;

/**
 *
 * @author Reiner Jung
 *
 * @since 1.15
 */
public class ExecutionRecordTransformationFilterTest {

	public ExecutionRecordTransformationFilterTest() {
		// default constructor
	}

	/**
	 * Tests if each {@link OperationExecutionRecord} within a valid Bookstore trace is correctly
	 * translated into a corresponding {@link Execution}.
	 *
	 * @throws AnalysisConfigurationException
	 *             If the internally assembled analysis configuration is somehow invalid.
	 * @throws IllegalStateException
	 *             If the internal analysis is in an invalid state.
	 */
	@Test
	public void testAllFieldsComplete() throws IllegalStateException, AnalysisConfigurationException {
		final String sessionId = "8T6NK1Q6";
		final long traceId = 34523; // any number will do

		final List<OperationExecutionRecord> operationExecutionRecords = BookstoreOperationExecutionRecordFactory.genValidBookstoreTraceFullSignature(sessionId,
				traceId);

		final SystemModelRepository systemModelRepository = new SystemModelRepository();
		final ExecutionRecordTransformationStage filter = new ExecutionRecordTransformationStage(systemModelRepository);

		final Execution[] expectedOperationExecutionRecords = new Execution[operationExecutionRecords.size()];

		int i = 0;
		for (final OperationExecutionRecord opExec : operationExecutionRecords) {
			final ClassOperationSignaturePair fqComponentNameSignaturePair = ClassOperationSignaturePair.splitOperationSignatureStr(opExec.getOperationSignature());

			final Execution exec = AbstractTraceAnalysisStage.createExecutionByEntityNames(systemModelRepository,
					opExec.getHostname(), fqComponentNameSignaturePair.getFqClassname(), fqComponentNameSignaturePair.getSignature(),
					traceId, sessionId, opExec.getEoi(), opExec.getEss(), opExec.getTin(), opExec.getTout(), false);

			Assert.assertEquals("tin's differ", opExec.getTin(), exec.getTin());
			Assert.assertEquals("tout's differ", opExec.getTout(), exec.getTout());
			Assert.assertEquals("eoi's differ", opExec.getEoi(), exec.getEoi());
			Assert.assertEquals("ess's differ", opExec.getEss(), exec.getEss());
			Assert.assertEquals("Hostnames differ", opExec.getHostname(), exec.getAllocationComponent().getExecutionContainer().getName());
			Assert.assertEquals("Session ID's differ", opExec.getSessionId(), exec.getSessionId());
			Assert.assertEquals("Trace ID's differ", opExec.getTraceId(), exec.getTraceId());

			final ClassOperationSignaturePair opExecClassOperationSignature = ClassOperationSignaturePair.splitOperationSignatureStr(opExec.getOperationSignature());

			Assert.assertEquals("Class/Component type names differ", opExecClassOperationSignature.getFqClassname(),
					exec.getAllocationComponent().getAssemblyComponent().getType().getFullQualifiedName());
			Assert.assertEquals("Signatures differ", opExecClassOperationSignature.getSignature(), exec.getOperation().getSignature());
			// we're not testing the assembly name here, because therefore, we had to transform the class name
			expectedOperationExecutionRecords[i++] = exec;
		}

		final OperationExecutionRecord[] inputOperations = operationExecutionRecords.toArray(new OperationExecutionRecord[operationExecutionRecords.size()]);

		StageTester.test(filter).and().send(inputOperations).to(filter.getInputPort()).and().start();

		MatcherAssert.assertThat(filter.getOutputPort(), StageTester.produces(expectedOperationExecutionRecords));
	}
}
