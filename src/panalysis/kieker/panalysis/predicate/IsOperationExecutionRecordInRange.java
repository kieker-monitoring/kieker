/***************************************************************************
 * Copyright 2014 Kieker Project (http://kieker-monitoring.net)
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
package kieker.panalysis.predicate;

import kieker.common.record.controlflow.OperationExecutionRecord;

/**
 * @author Christian Wulf
 * 
 * @since 1.10
 */
public class IsOperationExecutionRecordInRange extends IsTimestampInRange<OperationExecutionRecord> {

	/**
	 * @since 1.10
	 */
	public IsOperationExecutionRecordInRange(final long min, final long max) {
		super(min, max);
	}

	public boolean apply(final OperationExecutionRecord record) {
		final long tinTimestamp = record.getTin();
		final boolean isTinInRange = this.isInRange(tinTimestamp);
		final long toutTimestamp = record.getTout();
		final boolean isToutInRange = this.isInRange(toutTimestamp);
		return isTinInRange && isToutInRange;
	}

}
