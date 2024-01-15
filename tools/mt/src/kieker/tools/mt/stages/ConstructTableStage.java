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
package kieker.tools.mt.stages;

import kieker.analysis.generic.Table;
import kieker.analysis.generic.clustering.Clustering;
import kieker.analysis.generic.data.MoveOperationEntry;

import teetime.stage.basic.AbstractTransformation;

/**
 * Reconstruct table from clustering result.
 *
 * @author Reiner Jung
 * @since 2.0.0
 */
public class ConstructTableStage
		extends AbstractTransformation<Clustering<MoveOperationEntry>, Table<String, MoveOperationEntry>> {

	private final String label;

	public ConstructTableStage(final String label) {
		this.label = label;
	}

	@Override
	protected void execute(final Clustering<MoveOperationEntry> clustering) throws Exception {
		final Table<String, MoveOperationEntry> table = new Table<>(this.label);
		clustering.getClusters().forEach(cluster -> {
			cluster.forEach(entry -> {
				table.getRows().add(entry);
			});
		});
		this.outputPort.send(table);
	}

}
