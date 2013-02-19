/***************************************************************************
 * Copyright 2012 Kieker Project (http://kieker-monitoring.net)
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

package kieker.common.record.flow;

import kieker.common.record.AbstractMonitoringRecord;
import kieker.common.record.IMonitoringRecord;

/**
 * @author Jan Waller
 */
public abstract class AbstractEvent extends AbstractMonitoringRecord implements IMonitoringRecord.Factory, IEventRecord {
	private static final long serialVersionUID = 1L;

	private final long timestamp;

	public AbstractEvent(final long timestamp) {
		this.timestamp = timestamp;
	}

	protected AbstractEvent(final Object[] values, final Class<?>[] valueTypes) { // NOPMD (values stored directly)
		AbstractMonitoringRecord.checkArray(values, valueTypes);
		this.timestamp = (Long) values[0];
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @deprecated This record uses the {@link kieker.common.record.IMonitoringRecord.Factory} mechanism. Hence, this method is not implemented.
	 */
	@Deprecated
	public final void initFromArray(final Object[] values) {
		throw new UnsupportedOperationException();
	}

	public final long getTimestamp() {
		return this.timestamp;
	}
}
