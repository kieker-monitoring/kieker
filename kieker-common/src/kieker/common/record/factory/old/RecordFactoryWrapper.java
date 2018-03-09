/***************************************************************************
 * Copyright 2017 Kieker Project (http://kieker-monitoring.net)
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

package kieker.common.record.factory.old;

import kieker.common.exception.MonitoringRecordException;
import kieker.common.exception.RecordInstantiationException;
import kieker.common.record.AbstractMonitoringRecord;
import kieker.common.record.IMonitoringRecord;
import kieker.common.record.factory.IRecordFactory;
import kieker.common.record.io.IValueDeserializer;

/**
 * Represents a record factory for a record that does not have a dedicated record factory.
 *
 * @author Christian Wulf
 *
 * @since 1.11
 */
public final class RecordFactoryWrapper implements IRecordFactory<IMonitoringRecord> {

	private final String recordClassName;

	/**
	 * Create a record factory wrapper for the given record type.
	 *
	 * @param recordClassName
	 *            fully qualified class name of a record type
	 */
	public RecordFactoryWrapper(final String recordClassName) {
		this.recordClassName = recordClassName;
	}

	@Override
	public IMonitoringRecord create(final IValueDeserializer deserializer) {
		try {
			return AbstractMonitoringRecord.createFromDeserializer(this.recordClassName, deserializer);
		} catch (final MonitoringRecordException e) {
			throw new RecordInstantiationException(e);
		}
	}

	@SuppressWarnings("deprecation")
	@Override
	public IMonitoringRecord create(final Object[] values) {
		try {
			return AbstractMonitoringRecord.createFromArray(this.recordClassName, values);
		} catch (final MonitoringRecordException e) {
			throw new RecordInstantiationException(e);
		}
	}

	@Override
	public int getRecordSizeInBytes() {
		return IRecordFactory.UNKNOWN_RECORD_SIZE;
	}
}
