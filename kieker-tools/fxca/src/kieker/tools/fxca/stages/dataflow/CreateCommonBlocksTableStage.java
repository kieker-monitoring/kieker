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

import kieker.analysis.code.data.GlobalDataEntry;
import kieker.analysis.generic.Table;
import kieker.tools.fxca.stages.dataflow.data.CommonBlockEntry;

import teetime.stage.basic.AbstractTransformation;

/**
 * Create the call table for a fortran project.
 *
 * @author Reiner Jung
 * @since 2.0.0
 */
public class CreateCommonBlocksTableStage
		extends AbstractTransformation<CommonBlockEntry, Table<String, GlobalDataEntry>> {

	private static final String COMMON_BLOCKS = "common-blocks";

	private final Table<String, GlobalDataEntry> commonBlocksTable = new Table<>(
			CreateCommonBlocksTableStage.COMMON_BLOCKS);

	@Override
	protected void execute(final CommonBlockEntry entry) throws Exception {
		final String files = entry.getModules().stream().map(module -> module.getFileName())
				.reduce((list, element) -> list + "," + element).get();
		final String modules = entry.getModules().stream().map(module -> module.getModuleName())
				.reduce((list, element) -> list + "," + element).get();
		if (entry.getVariables().isEmpty()) {
			this.logger.error("Internal error: Common block {} without variables.", entry.getName());
		} else {
			final String variables = entry.getVariables().stream().reduce((list, element) -> list + "," + element)
					.get();
			this.commonBlocksTable.getRows().add(new GlobalDataEntry(entry.getName(), files, modules, variables));
		}
	}

	@Override
	protected void onTerminating() {
		this.outputPort.send(this.commonBlocksTable);
		super.onTerminating();
	}

}
