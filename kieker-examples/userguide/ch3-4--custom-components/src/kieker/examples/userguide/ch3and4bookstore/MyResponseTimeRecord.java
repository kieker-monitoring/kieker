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

package kieker.examples.userguide.ch3and4bookstore;

import java.nio.BufferOverflowException;
import java.nio.BufferUnderflowException;

import kieker.common.record.AbstractMonitoringRecord;
import kieker.common.record.IMonitoringRecord;
import kieker.common.record.io.IValueDeserializer;
import kieker.common.record.io.IValueSerializer;

public class MyResponseTimeRecord extends AbstractMonitoringRecord {
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

	public MyResponseTimeRecord(final IValueDeserializer deserializer) throws BufferUnderflowException {
		this.className = deserializer.getString();
		this.methodName = deserializer.getString();
		this.responseTimeNanos = deserializer.getLong();
	}

	@Override
	public void serialize(final IValueSerializer serializer) throws BufferOverflowException {
		serializer.putString(this.getClassName());
		serializer.putString(this.getMethodName());
		serializer.putLong(this.getResponseTimeNanos());
	}

	@Override
	public Class<?>[] getValueTypes() {
		return MyResponseTimeRecord.TYPES;
	}

	@Override
	public String[] getValueNames() {
		return new String[] { "className", "methodName", "responseTimeNanos" };
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
