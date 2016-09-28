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

package kieker.examples.userguide.ch3and4bookstore;

import java.nio.BufferOverflowException;
import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;

import kieker.common.record.AbstractMonitoringRecord;
import kieker.common.record.IMonitoringRecord;
import kieker.common.util.registry.IRegistry;

public class MyResponseTimeRecord extends AbstractMonitoringRecord implements IMonitoringRecord.Factory, IMonitoringRecord.BinaryFactory {
	public static final int SIZE = (2 * TYPE_SIZE_STRING) + TYPE_SIZE_LONG;
	public static final Class<?>[] TYPES = { String.class, String.class, long.class, };

	private static final long serialVersionUID = 7837873751833770201L;

	// Attributes storing the actual monitoring data:
	private final String className;
	private final String methodName;
	private final long responseTimeNanos;

	public MyResponseTimeRecord(final String clazz, final String method, final long rtNano) {
		this.className = clazz;
		this.methodName = method;
		this.responseTimeNanos = rtNano;
	}

	public MyResponseTimeRecord(final Object[] values) {
		AbstractMonitoringRecord.checkArray(values, MyResponseTimeRecord.TYPES);

		this.className = (String) values[0];
		this.methodName = (String) values[1];
		this.responseTimeNanos = (Long) values[2];
	}

	public MyResponseTimeRecord(final ByteBuffer buffer, final IRegistry<String> stringRegistry) throws BufferUnderflowException {
		this.className = stringRegistry.get(buffer.getInt());
		this.methodName = stringRegistry.get(buffer.getInt());
		this.responseTimeNanos = buffer.getLong();
	}

	@Override
	@Deprecated
	// Will not be used because the record implements IMonitoringRecord.Factory
	public final void initFromArray(final Object[] values) {
		throw new UnsupportedOperationException();
	}

	@Override
	@Deprecated
	// Will not be used because the record implements IMonitoringRecord.BinaryFactory
	public final void initFromBytes(final ByteBuffer buffer, final IRegistry<String> stringRegistry) throws BufferUnderflowException {
		throw new UnsupportedOperationException();
	}

	@Override
	public Object[] toArray() {
		return new Object[] { this.getClassName(), this.getMethodName(), this.getResponseTimeNanos(), };
	}

	@Override
	public void registerStrings(final IRegistry<String> stringRegistry) {
		stringRegistry.get(this.getClassName());
		stringRegistry.get(this.getMethodName());
	}

	@Override
	public void writeBytes(final ByteBuffer buffer, final IRegistry<String> stringRegistry) throws BufferOverflowException {
		buffer.putInt(stringRegistry.get(this.getClassName()));
		buffer.putInt(stringRegistry.get(this.getMethodName()));
		buffer.putLong(this.getResponseTimeNanos());
	}

	@Override
	public Class<?>[] getValueTypes() {
		return MyResponseTimeRecord.TYPES;
	}

	@Override
	public int getSize() {
		return SIZE;
	}

	public final String getClassName() {
		return this.className;
	}

	public final String getMethodName() {
		return this.methodName;
	}

	public final long getResponseTimeNanos() {
		return this.responseTimeNanos;
	}

}
