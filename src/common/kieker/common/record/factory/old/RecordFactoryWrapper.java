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

package kieker.common.record.factory.old;

import java.nio.ByteBuffer;

import kieker.common.exception.MonitoringRecordException;
import kieker.common.exception.RecordInstantiationException;
import kieker.common.record.AbstractMonitoringRecord;
import kieker.common.record.IMonitoringRecord;
import kieker.common.record.factory.IRecordFactory;
import kieker.common.util.registry.IRegistry;

/**
 * @author Christian Wulf
 *
 * @since 1.11
 */
public final class RecordFactoryWrapper implements IRecordFactory<IMonitoringRecord> {

	public static final int UNKNOWN_SIZE = -1;

	private final String recordClassName;

	public RecordFactoryWrapper(final String recordClassName) {
		this.recordClassName = recordClassName;
	}

	@SuppressWarnings("deprecation")
	@Override
	public IMonitoringRecord create(final ByteBuffer buffer, final IRegistry<String> stringRegistry) {
		try {
			return AbstractMonitoringRecord.createFromByteBuffer(this.recordClassName, buffer, stringRegistry);
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

	public int getRecordSizeInBytes() {
		return UNKNOWN_SIZE;
	}
}
