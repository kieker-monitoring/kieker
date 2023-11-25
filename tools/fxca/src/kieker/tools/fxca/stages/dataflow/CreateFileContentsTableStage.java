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

import kieker.analysis.code.data.CallerCalleeEntry;
import kieker.analysis.generic.Table;
import kieker.tools.fxca.model.FortranModule;
import kieker.tools.fxca.model.FortranOperation;
import kieker.tools.fxca.model.FortranProject;
import kieker.tools.fxca.utils.Pair;

import teetime.stage.basic.AbstractTransformation;

/**
 * Create the call table for a fortran project.
 *
 * @author Reiner Jung
 * @since 2.0.0
 */
public class CreateFileContentsTableStage
		extends AbstractTransformation<FortranProject, Table<String, CallerCalleeEntry>> {

	@Override
	protected void execute(final FortranProject project) throws Exception {
		final Table<String, CallerCalleeEntry> callsTable = new Table<>("calls");

		project.getCalls().forEach(call -> {
			final Pair<FortranModule, FortranOperation> callerPair = call.getFirst();
			final Pair<FortranModule, FortranOperation> calleePair = call.getSecond();

			if (callerPair != null) {
				final FQNOperation caller = this.composeOperation(callerPair);
				if (calleePair != null) {
					final FQNOperation callee = this.composeOperation(calleePair);
					callsTable.getRows().add(new CallerCalleeEntry(caller.path, caller.moduleName, caller.operationName,
							callee.path, callee.moduleName, callee.operationName));
				} else {
					this.logger.warn("Caller {} {} {} has no callee ", caller.path, caller.moduleName,
							caller.operationName);
				}
			} else {
				if (calleePair != null) {
					final FQNOperation callee = this.composeOperation(calleePair);
					this.logger.warn("No caller for callee {} {} {}", callee.path, callee.moduleName,
							callee.operationName);
				} else {
					this.logger.error("Empty call");
				}
			}
		});

		this.outputPort.send(callsTable);
	}

	private FQNOperation composeOperation(final Pair<FortranModule, FortranOperation> operationDescription) {
		final FortranModule module = operationDescription.getFirst();

		final FortranOperation callerOperation = operationDescription.getSecond();

		if (module.isNamedModule()) {
			return new FQNOperation(module.getFileName(), module.getModuleName(), callerOperation.getName());
		} else {
			return new FQNOperation(module.getFileName(), "<no-module>", callerOperation.getName());
		}
	}

	/**
	 * Local fully qualified operation representation.
	 *
	 * @author Reiner Jung
	 * @since 2.0.0
	 */
	private class FQNOperation {
		private final String path;
		private final String moduleName;
		private final String operationName;

		private FQNOperation(final String path, final String moduleName, final String operationName) {
			this.path = path;
			this.moduleName = moduleName;
			this.operationName = operationName;
		}
	}

}
