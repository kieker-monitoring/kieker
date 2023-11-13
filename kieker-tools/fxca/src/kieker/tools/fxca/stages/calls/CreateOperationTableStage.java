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
package kieker.tools.fxca.stages.calls;

import kieker.analysis.code.data.FileOperationEntry;
import kieker.analysis.generic.Table;
import kieker.tools.fxca.model.FortranProject;

import teetime.stage.basic.AbstractTransformation;

/**
 * Create a table containing operations.
 *
 * @author Henning Schnoor -- initial contribution
 * @author Reiner Jung
 *
 * @since 2.0.0
 */
public class CreateOperationTableStage
		extends AbstractTransformation<FortranProject, Table<String, FileOperationEntry>> {

	@Override
	protected void execute(final FortranProject project) throws Exception {
		final Table<String, FileOperationEntry> callsTable = new Table<>("operation");

		project.getModules().values().forEach(module -> {
			final String path = module.getFileName();
			module.getOperations().values().forEach(operation -> {
				callsTable.getRows().add(new FileOperationEntry(path, operation.getName()));
			});
		});

		this.outputPort.send(callsTable);
	}

}
