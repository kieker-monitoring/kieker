/***************************************************************************
 * Copyright 2022 Kieker Project (http://kieker-monitoring.net)
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

package kieker.analysis.architecture.generic.timestampfilter.components;

import kieker.common.record.flow.IEventRecord;

/**
 * Concrete implementation of {@link AbstractTimestampFilter}. Allows to filter {@link IEventRecord} objects based on their given timestamps.
 *
 * @author Andre van Hoorn, Jan Waller, Lars Bluemke
 *
 * @since 1.2
 */
public class EventRecordTimestampFilter extends AbstractTimestampFilter<IEventRecord> {

	public EventRecordTimestampFilter(final long ignoreBeforeTimestamp, final long ignoreAfterTimestamp) {
		super(ignoreBeforeTimestamp, ignoreAfterTimestamp);
	}

	@Override
	protected long getRecordSpecificTimestamp(final IEventRecord record) {
		return record.getTimestamp();
	}

}
