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

import java.util.HashMap;
import java.util.Map;

import kieker.analysis.plugin.port.InputPort;
import kieker.analysis.plugin.port.OutputPort;
import kieker.analysis.plugin.port.Plugin;
import kieker.analysis.repository.AbstractRepository;
import kieker.common.configuration.Configuration;
import kieker.common.logging.Log;
import kieker.common.logging.LogFactory;
import kieker.common.record.IMonitoringRecord;
import kieker.common.record.OperationExecutionRecord;
import kieker.tools.traceAnalysis.plugins.AbstractTraceAnalysisPlugin;
import kieker.tools.traceAnalysis.systemModel.Execution;
import kieker.tools.traceAnalysis.systemModel.Signature;

/**
 * Transforms {@link OperationExecutionRecord}s into {@link Execution} objects.<br>
 * 
 * This class has exactly one input port and one output port. It receives objects inheriting from {@link OperationExecutionRecord}. The received object is
 * transformed into an instance of {@link Execution}.
 * 
 * @author Andre van Hoorn
 */
@Plugin(
		outputPorts = {
			@OutputPort(name = ExecutionRecordTransformationFilter.OUTPUT_PORT_NAME, description = "Execution output stream", eventTypes = { Execution.class })
		})
public class ExecutionRecordTransformationFilter extends AbstractTraceAnalysisPlugin {

	public static final String INPUT_PORT_NAME = "newMonitoringRecord";
	public static final String OUTPUT_PORT_NAME = "defaultOutput";
	private static final Log LOG = LogFactory.getLog(ExecutionRecordTransformationFilter.class);

	public ExecutionRecordTransformationFilter(final Configuration configuration, final Map<String, AbstractRepository> repositories) {
		super(configuration, repositories);
	}

	@InputPort(description = "Input", eventTypes = { IMonitoringRecord.class })
	public boolean newMonitoringRecord(final Object data) {
		final IMonitoringRecord record = (IMonitoringRecord) data;
		if (!(record instanceof OperationExecutionRecord)) {
			ExecutionRecordTransformationFilter.LOG.error("Can only process records of type" + OperationExecutionRecord.class.getName() + " but received"
					+ record.getClass().getName());
			return false;
		}
		final OperationExecutionRecord execRec = (OperationExecutionRecord) record;

		/*
		 * FIXME: split execRec.getOperationName() of format op1(Type, Type2)
		 * into op1 and a String array { "Type1", "Type2"}.
		 */
		final Signature signature = new Signature(execRec.getOperationName(), new String[] {}, Signature.NO_RETURN_TYPE, new String[] { "params" });

		final Execution execution = this.createExecutionByEntityNames(execRec.getHostName(), execRec.getClassName(), signature,
				execRec.getTraceId(), execRec.getSessionId(), execRec.getEoi(), execRec.getEss(), execRec.getTin(), execRec.getTout());
		super.deliver(ExecutionRecordTransformationFilter.OUTPUT_PORT_NAME, execution);
		return true;
	}

	@Override
	public boolean execute() {
		return true;
	}

	@Override
	public void terminate(final boolean error) {
		// nothing to do
	}

	@Override
	protected Configuration getDefaultConfiguration() {
		return new Configuration();
	}

	@Override
	public Configuration getCurrentConfiguration() {
		final Configuration configuration = new Configuration();

		// TODO: Save the current configuration

		return configuration;
	}

	@Override
	protected Map<String, AbstractRepository> getDefaultRepositories() {
		return new HashMap<String, AbstractRepository>();
	}
}
