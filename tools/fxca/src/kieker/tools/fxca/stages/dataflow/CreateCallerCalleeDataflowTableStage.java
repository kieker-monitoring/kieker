/***************************************************************************
 * Copyright (C) 2023 Kieker Project (https://kieker-monitoring.net)
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
package kieker.tools.fxca.stages.dataflow;

import kieker.analysis.code.data.DataflowEntry;
import kieker.analysis.generic.Table;
import kieker.tools.fxca.stages.dataflow.data.CallerCalleeDataflow;

import teetime.stage.basic.AbstractTransformation;

/**
 * Create the call table for a fortran project.
 *
 * @author Reiner Jung
 * @since 2.0.0
 */
public class CreateCallerCalleeDataflowTableStage
		extends AbstractTransformation<CallerCalleeDataflow, Table<String, DataflowEntry>> {

	private final Table<String, DataflowEntry> callsTable;

	public CreateCallerCalleeDataflowTableStage() {
		this.callsTable = new Table<>("dataflow-cc");
	}

	@Override
	protected void execute(final CallerCalleeDataflow callerCalleeDataflow) throws Exception {
		this.callsTable.getRows()
				.add(new DataflowEntry(callerCalleeDataflow.getSourceFileName(),
						callerCalleeDataflow.getSourceModuleName(), callerCalleeDataflow.getSourceOperationName(),
						callerCalleeDataflow.getTargetFileName(), callerCalleeDataflow.getTargetModuleName(),
						callerCalleeDataflow.getTargetOperatioName(), callerCalleeDataflow.getDirection()));
	}

	@Override
	protected void onTerminating() {
		this.outputPort.send(this.callsTable);
		super.onTerminating();
	}

}
