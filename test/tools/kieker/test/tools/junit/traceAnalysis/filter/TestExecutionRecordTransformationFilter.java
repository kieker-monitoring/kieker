/***************************************************************************
 * Copyright 2011 by
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

package kieker.test.tools.junit.traceAnalysis.filter;

import java.util.ArrayList;
import java.util.List;

import junit.framework.Assert;
import junit.framework.TestCase;
import kieker.analysis.AnalysisController;
import kieker.common.configuration.Configuration;
import kieker.common.record.controlflow.OperationExecutionRecord;
import kieker.common.util.ClassOperationSignaturePair;
import kieker.test.analysis.junit.plugin.ExecutionSinkClass;
import kieker.test.analysis.junit.plugin.ListReader;
import kieker.test.common.junit.record.BookstoreOperationExecutionRecordFactory;
import kieker.tools.traceAnalysis.filter.AbstractTraceAnalysisFilter;
import kieker.tools.traceAnalysis.filter.executionRecordTransformation.ExecutionRecordTransformationFilter;
import kieker.tools.traceAnalysis.systemModel.Execution;
import kieker.tools.traceAnalysis.systemModel.repository.SystemModelRepository;

/**
 * 
 * @author Andre van Hoorn
 * 
 */
public class TestExecutionRecordTransformationFilter extends TestCase {

	/**
	 * Tests if each {@link OperationExecutionRecord} within a valid Bookstore trace is correctly
	 * translated into a corresponding {@link Execution}.
	 */
	public void testAllFieldsComplete() {
		final String sessionId = "8T6NK1Q6";
		final long traceId = 34523; // any number will do // NOCS (MagicNumberCheck)

		final List<OperationExecutionRecord> opExecs = BookstoreOperationExecutionRecordFactory.genValidBookstoreTraceFullSignature(sessionId, traceId);

		final ExecRecordTransformationFilterChecker tester = new ExecRecordTransformationFilterChecker(opExecs);

		final List<Execution> expectedExecs = new ArrayList<Execution>(opExecs.size());
		for (final OperationExecutionRecord opExec : opExecs) {
			final ClassOperationSignaturePair fqComponentNameSignaturePair = ClassOperationSignaturePair.splitOperationSignatureStr(opExec.getOperationSignature());

			final Execution exec = AbstractTraceAnalysisFilter.createExecutionByEntityNames(tester.getSystemModelRepository(),
					opExec.getHostname(), fqComponentNameSignaturePair.getFqClassname(), fqComponentNameSignaturePair.getSignature(),
					traceId, sessionId, opExec.getEoi(), opExec.getEss(), opExec.getTin(), opExec.getTout(), false);

			/**
			 * Some initial primitive checks which actually test createExecutionByEntityNames here,
			 * but errors here would make the test invalid
			 */
			{
				Assert.assertEquals("tin's differ", opExec.getTin(), exec.getTin());
				Assert.assertEquals("tout's differ", opExec.getTout(), exec.getTout());
				Assert.assertEquals("eoi's differ", opExec.getEoi(), exec.getEoi());
				Assert.assertEquals("ess's differ", opExec.getEss(), exec.getEss());
				Assert.assertEquals("Hostnames differ", opExec.getHostname(), exec.getAllocationComponent().getExecutionContainer().getName());
				Assert.assertEquals("Session ID's differ", opExec.getSessionId(), exec.getSessionId());
				Assert.assertEquals("Trace ID's differ", opExec.getTraceId(), exec.getTraceId());
				final ClassOperationSignaturePair opExecClassOperationSignature =
						ClassOperationSignaturePair.splitOperationSignatureStr(opExec.getOperationSignature());
				Assert.assertEquals("Class/Component type names differ", opExecClassOperationSignature.getFqClassname(),
						exec.getAllocationComponent().getAssemblyComponent().getType().getFullQualifiedName());
				// TODO: as long as Signature's equal not implemented, compare the strings
				Assert.assertEquals("Signatures differ", opExecClassOperationSignature.getSignature().toString(),
						exec.getOperation().getSignature().toString());
				// we're not testing the assembly name here, because therefore, we had to transform the class name
			}

			expectedExecs.add(exec);
		}

		tester.doTestFilter(expectedExecs);
	}
}

/**
 * 
 * @author Andre van Hoorn
 * 
 */
class ExecRecordTransformationFilterChecker {
	private final SystemModelRepository systemModelRepository = new SystemModelRepository(new Configuration());
	private final AnalysisController analysisController = new AnalysisController();
	final ListReader listReader = new ListReader(new Configuration());
	final ExecutionRecordTransformationFilter execRecFilter = new ExecutionRecordTransformationFilter(new Configuration());
	final ExecutionSinkClass sinkPlugin = new ExecutionSinkClass(new Configuration());

	public ExecRecordTransformationFilterChecker(final List<OperationExecutionRecord> records) {
		for (final OperationExecutionRecord record : records) { // the reader will provide these records via its output port
			this.listReader.addObject(record);
		}
		this.analysisController.registerReader(this.listReader);
		this.analysisController.registerFilter(this.execRecFilter);
		this.analysisController.registerFilter(this.sinkPlugin);
		this.analysisController.registerRepository(this.systemModelRepository);
		this.analysisController.connect(this.listReader, ListReader.OUTPUT_PORT_NAME, this.execRecFilter, ExecutionRecordTransformationFilter.INPUT_PORT_NAME_RECORDS);
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
	 */
	public void doTestFilter(final List<Execution> expectedExecutions) {
		this.analysisController.run();

		Assert.assertEquals("Expected sink to contain 1 Execution", expectedExecutions.size(), this.sinkPlugin.getExecutions().size());

		final List<Execution> generatedExecutions = this.sinkPlugin.getExecutions();

		Assert.assertEquals("Unexpected number of generated executions", expectedExecutions.size(), generatedExecutions.size());

		// note that we assume that the records are processed in FIFO order by the filter
		Assert.assertEquals("Lists of expected and generated executions not equal",
				expectedExecutions, generatedExecutions);
	}
}
