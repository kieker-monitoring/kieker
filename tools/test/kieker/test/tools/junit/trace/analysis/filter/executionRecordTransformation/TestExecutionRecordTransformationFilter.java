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

package kieker.test.tools.junit.trace.analysis.filter.executionRecordTransformation;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import kieker.analysis.AnalysisController;
import kieker.analysis.IAnalysisController;
import kieker.analysis.exception.AnalysisConfigurationException;
import kieker.analysis.plugin.reader.list.ListReader;
import kieker.common.configuration.Configuration;
import kieker.common.record.controlflow.OperationExecutionRecord;
import kieker.common.util.signature.ClassOperationSignaturePair;
import kieker.tools.trace.analysis.filter.AbstractTraceAnalysisFilter;
import kieker.tools.trace.analysis.filter.executionRecordTransformation.ExecutionRecordTransformationFilter;
import kieker.tools.trace.analysis.systemModel.Execution;
import kieker.tools.trace.analysis.systemModel.repository.SystemModelRepository;

import kieker.test.common.junit.AbstractKiekerTest;
import kieker.test.common.util.record.BookstoreOperationExecutionRecordFactory;
import kieker.test.tools.util.bookstore.filter.ExecutionSinkClass;

/**
 * @author Andre van Hoorn
 *
 * @since 1.5
 * @deprecated 1.15 ported to teetime
 */
@Deprecated
public class TestExecutionRecordTransformationFilter extends AbstractKiekerTest {

	/**
	 * Default constructor.
	 */
	public TestExecutionRecordTransformationFilter() {
		// empty default constructor
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
	public void testAllFieldsComplete() throws AnalysisConfigurationException {
		final String sessionId = "8T6NK1Q6";
		final long traceId = 34523; // any number will do

		final List<OperationExecutionRecord> opExecs = BookstoreOperationExecutionRecordFactory.genValidBookstoreTraceFullSignature(sessionId, traceId);

		final ExecRecordTransformationFilterChecker tester = new ExecRecordTransformationFilterChecker(opExecs);

		final List<Execution> expectedExecs = new ArrayList<>(opExecs.size());
		for (final OperationExecutionRecord opExec : opExecs) {
			final ClassOperationSignaturePair fqComponentNameSignaturePair = ClassOperationSignaturePair.splitOperationSignatureStr(opExec.getOperationSignature());

			final Execution exec = AbstractTraceAnalysisFilter.createExecutionByEntityNames(tester.getSystemModelRepository(),
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
			expectedExecs.add(exec);
		}

		tester.doTestFilter(expectedExecs);
	}
}

/**
 *
 * @author Andre van Hoorn
 *
 * @since 1.5
 */
class ExecRecordTransformationFilterChecker { // NOPMD (subclass of TestCase)
	private final IAnalysisController analysisController = new AnalysisController();

	private final SystemModelRepository systemModelRepository = new SystemModelRepository(new Configuration(), this.analysisController);
	private final ListReader<Object> listReader = new ListReader<>(new Configuration(), this.analysisController);
	private final ExecutionRecordTransformationFilter execRecFilter = new ExecutionRecordTransformationFilter(new Configuration(), this.analysisController);
	private final ExecutionSinkClass sinkPlugin = new ExecutionSinkClass(new Configuration(), this.analysisController);

	public ExecRecordTransformationFilterChecker(final List<OperationExecutionRecord> records) throws AnalysisConfigurationException {
		for (final OperationExecutionRecord record : records) { // the reader will provide these records via its output port
			this.listReader.addObject(record);
		}

		this.analysisController.connect(this.listReader, ListReader.OUTPUT_PORT_NAME, this.execRecFilter,
				ExecutionRecordTransformationFilter.INPUT_PORT_NAME_RECORDS);
		this.analysisController.connect(this.execRecFilter, ExecutionRecordTransformationFilter.OUTPUT_PORT_NAME_EXECUTIONS,
				this.sinkPlugin, ExecutionSinkClass.INPUT_PORT_NAME);
		this.analysisController.connect(this.execRecFilter, AbstractTraceAnalysisFilter.REPOSITORY_PORT_NAME_SYSTEM_MODEL, this.systemModelRepository);
	}

	public SystemModelRepository getSystemModelRepository() {
		return this.systemModelRepository;
	}

	/**
	 * Checks whether the expected {@link Execution}s were generated by the
	 * filter.
	 *
	 * @param expectedExecutions
	 * @throws AnalysisConfigurationException
	 *             If the internally assembled analysis configuration is somehow invalid.
	 * @throws IllegalStateException
	 *             If the internally assembled analysis is in an invalid state.
	 */
	public void doTestFilter(final List<Execution> expectedExecutions) throws AnalysisConfigurationException {
		this.analysisController.run();

		Assert.assertEquals("Expected sink to contain 1 Execution", expectedExecutions.size(), this.sinkPlugin.getExecutions().size());

		final List<Execution> generatedExecutions = this.sinkPlugin.getExecutions();

		Assert.assertEquals("Unexpected number of generated executions", expectedExecutions.size(), generatedExecutions.size());

		// note that we assume that the records are processed in FIFO order by the filter
		Assert.assertEquals("Lists of expected and generated executions not equal", expectedExecutions, generatedExecutions);
	}
}
