/***************************************************************************
 * Copyright 2014 Kicker Project (http://kicker-monitoring.net)
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

package kicker.common.record.flow;

import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;

import kicker.common.record.AbstractMonitoringRecord;
import kicker.common.record.IMonitoringRecord;
import kicker.common.util.registry.IRegistry;

/**
 * @author Jan Waller
 * 
 * @since 1.5
 */
public abstract class AbstractEvent extends AbstractMonitoringRecord implements IMonitoringRecord.Factory, IMonitoringRecord.BinaryFactory, IEventRecord {
	private static final long serialVersionUID = 1L;

	private final long timestamp;

	/**
	 * This constructor uses the given parameter to initialize the field of this record.
	 * 
	 * @param timestamp
	 *            The timestamp of this record.
	 */
	public AbstractEvent(final long timestamp) {
		this.timestamp = timestamp;
	}

	/**
	 * This constructor uses the given array to initialize the fields of this record.
	 * 
	 * @param values
	 *            The values for the record.
	 * @param valueTypes
	 *            The types of the elements in the first array.
	 */
	protected AbstractEvent(final Object[] values, final Class<?>[] valueTypes) { // NOPMD (values stored directly)
		AbstractMonitoringRecord.checkArray(values, valueTypes);
		this.timestamp = (Long) values[0];
	}

	/**
	 * This constructor converts the given array into a record.
	 * 
	 * @param buffer
	 *            The bytes for the record.
	 * 
	 * @throws BufferUnderflowException
	 *             if buffer not sufficient
	 */
	public AbstractEvent(final ByteBuffer buffer, final IRegistry<String> stringRegistry) throws BufferUnderflowException { // NOPMD
		this.timestamp = buffer.getLong();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @deprecated This record uses the {@link kicker.common.record.IMonitoringRecord.Factory} mechanism. Hence, this method is not implemented.
	 */
	@Override
	@Deprecated
	public final void initFromArray(final Object[] values) {
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @deprecated This record uses the {@link kicker.common.record.IMonitoringRecord.BinaryFactory} mechanism. Hence, this method is not implemented.
	 */
	@Override
	@Deprecated
	public void initFromBytes(final ByteBuffer buffer, final IRegistry<String> stringRegistry) throws BufferUnderflowException {
		throw new UnsupportedOperationException();
	}

	@Override
	public final long getTimestamp() {
		return this.timestamp;
	}
}
