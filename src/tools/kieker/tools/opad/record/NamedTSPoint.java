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

package kieker.tools.opad.record;

import java.nio.BufferOverflowException;
import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;

import kieker.common.record.AbstractMonitoringRecord;
import kieker.common.record.IMonitoringRecord;
import kieker.common.util.registry.IRegistry;

/**
 * 
 * @author Tillmann Carlos Bielefeld
 * 
 */
public class NamedTSPoint extends AbstractMonitoringRecord implements IMonitoringRecord.Factory, INamedElement, IDoubleValue {

	public static final int SIZE = 20;
	public static final Class<?>[] TYPES = {
		long.class, // timestamp
		double.class, // value
		String.class, // name
	};

	private static final long serialVersionUID = 436L;

	private final long timestamp;
	private final double value;
	private final String name;

	public NamedTSPoint(final long timestamp, final double value, final String name) {
		super();
		this.timestamp = timestamp;
		this.value = value;
		this.name = name;
	}

	public Object[] toArray() {
		// TODO Auto-generated method stub
		return null;
	}

	@Deprecated
	public void initFromArray(final Object[] values) {
		throw new UnsupportedOperationException();
	}

	public Class<?>[] getValueTypes() {
		return TYPES;
	}

	public long getTimestamp() {
		return this.timestamp;
	}

	public String getName() {
		return this.name;
	}

	public double getValue() {
		return this.value;
	}

	public void writeBytes(final ByteBuffer buffer, final IRegistry<String> stringRegistry) throws BufferOverflowException {
		buffer.putLong(this.getTimestamp());
		buffer.putDouble(this.getValue());
		buffer.putInt(stringRegistry.get(this.getName()));
	}

	public void initFromBytes(final ByteBuffer buffer, final IRegistry<String> stringRegistry) throws BufferUnderflowException {
		throw new UnsupportedOperationException(); // TODO: FIX
	}

	public int getSize() {
		return SIZE;
	}
}
