/***************************************************************************
 * Copyright 2015 Kieker Project (http://kieker-monitoring.net)
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

package kieker.analysisteetime.recordreading;

import kieker.common.record.IMonitoringRecord;
import kieker.common.record.controlflow.OperationExecutionRecord;
import kieker.common.record.flow.trace.TraceMetadata;
import kieker.common.record.flow.trace.operation.AfterOperationEvent;
import kieker.common.record.flow.trace.operation.BeforeOperationEvent;
import kieker.common.record.misc.KiekerMetadataRecord;

import teetime.stage.basic.AbstractTransformation;

/**
 * @author Nils Christian Ehmke
 */
public final class AllowedRecordsFilter extends AbstractTransformation<IMonitoringRecord, IMonitoringRecord> {

	private int ignoredRecords;

	@Override
	protected void execute(final IMonitoringRecord element) {
		if (element instanceof TraceMetadata || element instanceof BeforeOperationEvent || element instanceof AfterOperationEvent
				|| element instanceof KiekerMetadataRecord || element instanceof OperationExecutionRecord) {
			super.getOutputPort().send(element);
		} else {
			ignoredRecords++;
		}
	}

	public int getIgnoredRecords() {
		return ignoredRecords;
	}

}
