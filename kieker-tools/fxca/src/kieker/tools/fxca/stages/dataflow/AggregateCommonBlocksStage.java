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

import java.util.HashMap;
import java.util.Map;

import kieker.tools.fxca.stages.dataflow.data.CommonBlockEntry;

import teetime.stage.basic.AbstractFilter;

/**
 * Aggregate common block entries.
 *
 * @author Reiner Jung
 * @since 2.0.0
 */
public class AggregateCommonBlocksStage extends AbstractFilter<CommonBlockEntry> {

	private final Map<String, CommonBlockEntry> entries = new HashMap<>();

	@Override
	protected void execute(final CommonBlockEntry entry) throws Exception {
		final CommonBlockEntry existingEntry = this.entries.get(entry.getName());
		if (existingEntry != null) {
			existingEntry.merge(entry);
		} else {
			this.entries.put(entry.getName(), entry);
		}
	}

	@Override
	protected void onTerminating() {
		this.entries.values().forEach(entry -> this.outputPort.send(entry));
		super.onTerminating();
	}

}
