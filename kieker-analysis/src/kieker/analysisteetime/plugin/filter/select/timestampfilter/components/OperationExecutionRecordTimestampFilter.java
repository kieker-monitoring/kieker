/***************************************************************************
 * Copyright 2018 Kieker Project (http://kieker-monitoring.net)
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

package kieker.analysisteetime.plugin.filter.select.timestampfilter.components;

import kieker.common.record.controlflow.OperationExecutionRecord;

/**
 * Concrete implementation of {@link AbstractTimestampFilter}. Allows to filter {@link OperationExecutionRecord} objects based on their given timestamps.
 * This stage receives trace events to be selected by a specific timestamp selector (based on tin and tout).
 *
 * @author Andre van Hoorn, Jan Waller, Lars Bluemke
 *
 * @since 1.2
 */
public class OperationExecutionRecordTimestampFilter extends AbstractTimestampFilter<OperationExecutionRecord> {

	public OperationExecutionRecordTimestampFilter(final long ignoreBeforeTimestamp, final long ignoreAfterTimestamp) {
		super(ignoreBeforeTimestamp, ignoreAfterTimestamp);
	}

	@Override
	protected void execute(final OperationExecutionRecord execution) {
		if (this.inRange(execution.getTin()) && this.inRange(execution.getTout())) {
			this.recordWithinTimePeriodOutputPort.send(execution);
		} else {
			this.recordOutsideTimePeriodOutputPort.send(execution);
		}
	}

	@Override
	protected long getRecordSpecificTimestamp(final OperationExecutionRecord execution) {
		// As this particular stage is using two timestamps instead of just one the execute method was overwritten anyway so there is no need for this method.
		throw new UnsupportedOperationException();
	}

}
