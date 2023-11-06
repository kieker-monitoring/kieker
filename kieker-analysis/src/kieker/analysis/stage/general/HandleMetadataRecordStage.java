/***************************************************************************
 * Copyright 2023 Kieker Project (http://kieker-monitoring.net)
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
package kieker.analysis.stage.general;

import java.util.Map;

import kieker.common.record.IMonitoringRecord;
import kieker.common.record.misc.KiekerMetadataRecord;

import teetime.stage.basic.AbstractFilter;

/**
 * Receive all events and filter out the metadata records and store its information in a metadata model.
 * Relay all other events.
 *
 * @author Reiner Jung
 * @since 1.15
 */
public class HandleMetadataRecordStage extends AbstractFilter<IMonitoringRecord> {

	private final Map<String, KiekerMetadataRecord> metadataMap;

	public HandleMetadataRecordStage(final Map<String, KiekerMetadataRecord> metadataMap) {
		this.metadataMap = metadataMap;
	}

	@Override
	protected void execute(final IMonitoringRecord element) throws Exception {
		if (element instanceof KiekerMetadataRecord) {
			final KiekerMetadataRecord metadata = (KiekerMetadataRecord) element;
			this.metadataMap.put(metadata.getHostname(), metadata);
		} else {
			this.outputPort.send(element);
		}
	}

}
