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
package kieker.tools.mktable.stages;

import kieker.analysis.generic.Table;
import kieker.analysis.generic.data.MoveOperationEntry;

import teetime.stage.basic.AbstractFilter;

/**
 * Receives a source to target component table and searches for identical rows, merge them and increase the counter for that row.
 *
 * @author Reiner Jung
 * @since 2.0.0 
 */
public class CompactTableStage extends AbstractFilter<Table<String, MoveOperationEntry>> {

	@Override
	protected void execute(final Table<String, MoveOperationEntry> table) throws Exception {
		final Table<String, MoveOperationEntry> newTable = new Table<>(table.getLabel() + "-compact");

		final MoveOperationEntry entry = table.getRows().get(0);
		MoveOperationCounter counter = new MoveOperationCounter(entry.getSourceComponentName(),
				entry.getTargetComponentName());
		for (final MoveOperationEntry element : table.getRows()) {
			if (element.getSourceComponentName().equals(counter.sourceComponentName)
					&& element.getTargetComponentName().equals(counter.targetComponentName)) {
				counter.increment();
			} else {
				newTable.getRows().add(new MoveOperationEntry(counter.sourceComponentName, counter.targetComponentName,
						String.valueOf(counter.count)));
				counter = new MoveOperationCounter(element.getSourceComponentName(), element.getTargetComponentName());
				counter.increment();
			}
		}
		newTable.getRows().add(new MoveOperationEntry(counter.sourceComponentName, counter.targetComponentName,
				String.valueOf(counter.count)));

		this.outputPort.send(newTable);
	}

	private final class MoveOperationCounter {
		private final String sourceComponentName;
		private final String targetComponentName;
		private long count;

		public MoveOperationCounter(final String sourceComponentName, final String targetComponentName) {
			this.sourceComponentName = sourceComponentName;
			this.targetComponentName = targetComponentName;
			this.count = 0;
		}

		public void increment() {
			this.count++;
		}
	}
}
