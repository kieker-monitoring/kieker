/***************************************************************************
 * Copyright (C) 2023 OceanDSL (https://oceandsl.uni-kiel.de)
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
package kieker.tools.delta.stages;

import java.util.HashMap;
import java.util.Map;

import kieker.analysis.generic.Table;
import kieker.analysis.generic.data.MoveOperationEntry;
import kieker.tools.restructuring.restructuremodel.CutOperation;
import kieker.tools.restructuring.restructuremodel.MoveOperation;
import kieker.tools.restructuring.restructuremodel.PasteOperation;
import kieker.tools.restructuring.restructuremodel.TransformationModel;

import teetime.stage.basic.AbstractTransformation;

/**
 * Process transformation model and generate a table of {@link MoveOperationEntry}.
 *
 * @author Reiner Jung
 * @since 2.0.0
 */
public class CompileRestructureTableStage
		extends AbstractTransformation<TransformationModel, Table<String, MoveOperationEntry>> {

	private final Table<String, MoveOperationEntry> table;
	private final Map<String, CutOperation> rememberCutOperation = new HashMap<>();

	public CompileRestructureTableStage(final String name) {
		this.table = new Table<>(name);
	}

	@Override
	protected void execute(final TransformationModel element) throws Exception {
		element.getTransformations().forEach(action -> {
			if (action instanceof CutOperation) {
				final CutOperation cut = (CutOperation) action;
				this.rememberCutOperation.put(cut.getOperationName(), cut);
			} else if (action instanceof MoveOperation) {
				final MoveOperation move = (MoveOperation) action;
				final CutOperation cut = move.getCutOperation();
				final PasteOperation paste = move.getPasteOperation();

				this.table.getRows().add(new MoveOperationEntry(cut.getComponentName(), paste.getComponentName(),
						cut.getOperationName()));
			} else if (action instanceof PasteOperation) {
				final PasteOperation paste = (PasteOperation) action;
				if (this.rememberCutOperation.containsKey(paste.getOperationName())) {
					final CutOperation cut = this.rememberCutOperation.get(paste.getOperationName());

					this.table.getRows().add(new MoveOperationEntry(cut.getComponentName(), paste.getComponentName(),
							cut.getOperationName()));
				} else {
					this.logger.error("Have paste without cut. {} {}", paste.getComponentName(),
							paste.getOperationName());
				}
			} else {
				this.logger.debug("Got a {}", action.getClass());
			}
		});
		this.outputPort.send(this.table);
	}

}
