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

package kieker.tools.traceAnalysis.plugins.executionRecordTransformation;

import kieker.analysis.plugin.annotation.InputPort;
import kieker.analysis.plugin.annotation.OutputPort;
import kieker.analysis.plugin.annotation.Plugin;
import kieker.analysis.plugin.annotation.RepositoryPort;
import kieker.common.configuration.Configuration;
import kieker.common.record.controlflow.OperationExecutionRecord;
import kieker.common.util.ClassOperationSignaturePair;
import kieker.tools.traceAnalysis.plugins.AbstractTraceAnalysisPlugin;
import kieker.tools.traceAnalysis.systemModel.Execution;
import kieker.tools.traceAnalysis.systemModel.repository.SystemModelRepository;

/**
 * Transforms {@link OperationExecutionRecord}s into {@link Execution} objects.<br>
 * 
 * This class has exactly one input port and one output port. It receives objects inheriting from {@link OperationExecutionRecord}. The received object is
 * transformed into an instance of {@link Execution}.
 * 
 * @author Andre van Hoorn
 */
@Plugin(
		outputPorts = @OutputPort(name = ExecutionRecordTransformationFilter.OUTPUT_PORT_NAME, description = "Execution output stream", eventTypes = { Execution.class }),
		repositoryPorts = @RepositoryPort(name = AbstractTraceAnalysisPlugin.SYSTEM_MODEL_REPOSITORY_NAME, repositoryType = SystemModelRepository.class))
public class ExecutionRecordTransformationFilter extends AbstractTraceAnalysisPlugin {
	// private static final Log LOG = LogFactory.getLog(ExecutionRecordTransformationFilter.class);

	public static final String INPUT_PORT_NAME = "newMonitoringRecord";
	public static final String OUTPUT_PORT_NAME = "defaultOutput";

	public ExecutionRecordTransformationFilter(final Configuration configuration) {
		super(configuration);
	}

	@InputPort(
			name = ExecutionRecordTransformationFilter.INPUT_PORT_NAME,
			description = "Input",
			eventTypes = { OperationExecutionRecord.class })
	public boolean newMonitoringRecord(final OperationExecutionRecord execRec) {
		final ClassOperationSignaturePair fqComponentNameSignaturePair = ClassOperationSignaturePair.splitOperationSignatureStr(execRec.getOperationSignature());

		final Execution execution = this.createExecutionByEntityNames(execRec.getHostName(), fqComponentNameSignaturePair.getFqClassname(),
				fqComponentNameSignaturePair.getSignature(),
				execRec.getTraceId(), execRec.getSessionId(), execRec.getEoi(), execRec.getEss(), execRec.getTin(), execRec.getTout(), false);
		super.deliver(ExecutionRecordTransformationFilter.OUTPUT_PORT_NAME, execution);
		return true;
	}

	@Override
	protected Configuration getDefaultConfiguration() {
		return new Configuration();
	}

	@Override
	public Configuration getCurrentConfiguration() {
		final Configuration configuration = new Configuration();
		// filter has no configuration properties
		return configuration;
	}

}
