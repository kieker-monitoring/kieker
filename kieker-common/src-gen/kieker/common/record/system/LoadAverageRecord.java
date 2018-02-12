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
package kieker.common.record.system;

import java.nio.BufferOverflowException;

import kieker.common.exception.RecordInstantiationException;
import kieker.common.record.AbstractMonitoringRecord;
import kieker.common.record.IMonitoringRecord;
import kieker.common.record.io.IValueDeserializer;
import kieker.common.record.io.IValueSerializer;
import kieker.common.util.registry.IRegistry;


/**
 * @author Teerat Pitakrat
 * API compatibility: Kieker 1.13.0
 * 
 * @since 1.12
 */
public class LoadAverageRecord extends AbstractMonitoringRecord implements IMonitoringRecord.Factory, IMonitoringRecord.BinaryFactory {
	private static final long serialVersionUID = -664763923774505966L;

	/** Descriptive definition of the serialization size of the record. */
	public static final int SIZE = TYPE_SIZE_LONG // LoadAverageRecord.timestamp
			 + TYPE_SIZE_STRING // LoadAverageRecord.hostname
			 + TYPE_SIZE_DOUBLE // LoadAverageRecord.oneMinLoadAverage
			 + TYPE_SIZE_DOUBLE // LoadAverageRecord.fiveMinLoadAverage
			 + TYPE_SIZE_DOUBLE // LoadAverageRecord.fifteenMinLoadAverage
	;
	
	public static final Class<?>[] TYPES = {
		long.class, // LoadAverageRecord.timestamp
		String.class, // LoadAverageRecord.hostname
		double.class, // LoadAverageRecord.oneMinLoadAverage
		double.class, // LoadAverageRecord.fiveMinLoadAverage
		double.class, // LoadAverageRecord.fifteenMinLoadAverage
	};
	
	
	/** default constants. */
	public static final long TIMESTAMP = 0L;
	public static final String HOSTNAME = "";
	public static final double ONE_MIN_LOAD_AVERAGE = 0.0;
	public static final double FIVE_MIN_LOAD_AVERAGE = 0.0;
	public static final double FIFTEEN_MIN_LOAD_AVERAGE = 0.0;
	
	/** property name array. */
	private static final String[] PROPERTY_NAMES = {
		"timestamp",
		"hostname",
		"oneMinLoadAverage",
		"fiveMinLoadAverage",
		"fifteenMinLoadAverage",
	};
	
	/** property declarations. */
	private final long timestamp;
	private final String hostname;
	private final double oneMinLoadAverage;
	private final double fiveMinLoadAverage;
	private final double fifteenMinLoadAverage;
	
	/**
	 * Creates a new instance of this class using the given parameters.
	 * 
	 * @param timestamp
	 *            timestamp
	 * @param hostname
	 *            hostname
	 * @param oneMinLoadAverage
	 *            oneMinLoadAverage
	 * @param fiveMinLoadAverage
	 *            fiveMinLoadAverage
	 * @param fifteenMinLoadAverage
	 *            fifteenMinLoadAverage
	 */
	public LoadAverageRecord(final long timestamp, final String hostname, final double oneMinLoadAverage, final double fiveMinLoadAverage, final double fifteenMinLoadAverage) {
		this.timestamp = timestamp;
		this.hostname = hostname == null?HOSTNAME:hostname;
		this.oneMinLoadAverage = oneMinLoadAverage;
		this.fiveMinLoadAverage = fiveMinLoadAverage;
		this.fifteenMinLoadAverage = fifteenMinLoadAverage;
	}

	/**
	 * This constructor converts the given array into a record.
	 * It is recommended to use the array which is the result of a call to {@link #toArray()}.
	 * 
	 * @param values
	 *            The values for the record.
	 *
	 * @deprecated since 1.13. Use {@link #LoadAverageRecord(IValueDeserializer)} instead.
	 */
	@Deprecated
	public LoadAverageRecord(final Object[] values) { // NOPMD (direct store of values)
		AbstractMonitoringRecord.checkArray(values, TYPES);
		this.timestamp = (Long) values[0];
		this.hostname = (String) values[1];
		this.oneMinLoadAverage = (Double) values[2];
		this.fiveMinLoadAverage = (Double) values[3];
		this.fifteenMinLoadAverage = (Double) values[4];
	}

	/**
	 * This constructor uses the given array to initialize the fields of this record.
	 * 
	 * @param values
	 *            The values for the record.
	 * @param valueTypes
	 *            The types of the elements in the first array.
	 *
	 * @deprecated since 1.13. Use {@link #LoadAverageRecord(IValueDeserializer)} instead.
	 */
	@Deprecated
	protected LoadAverageRecord(final Object[] values, final Class<?>[] valueTypes) { // NOPMD (values stored directly)
		AbstractMonitoringRecord.checkArray(values, valueTypes);
		this.timestamp = (Long) values[0];
		this.hostname = (String) values[1];
		this.oneMinLoadAverage = (Double) values[2];
		this.fiveMinLoadAverage = (Double) values[3];
		this.fifteenMinLoadAverage = (Double) values[4];
	}

	
	/**
	 * @param deserializer
	 *            The deserializer to use
	 * @throws RecordInstantiationException 
	 */
	public LoadAverageRecord(final IValueDeserializer deserializer) throws RecordInstantiationException {
		this.timestamp = deserializer.getLong();
		this.hostname = deserializer.getString();
		this.oneMinLoadAverage = deserializer.getDouble();
		this.fiveMinLoadAverage = deserializer.getDouble();
		this.fifteenMinLoadAverage = deserializer.getDouble();
	}
	
	/**
	 * {@inheritDoc}
	 *
	 * @deprecated since 1.13. Use {@link #serialize(IValueSerializer)} with an array serializer instead.
	 */
	@Override
	@Deprecated
	public Object[] toArray() {
		return new Object[] {
			this.getTimestamp(),
			this.getHostname(),
			this.getOneMinLoadAverage(),
			this.getFiveMinLoadAverage(),
			this.getFifteenMinLoadAverage()
		};
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void registerStrings(final IRegistry<String> stringRegistry) {	// NOPMD (generated code)
		stringRegistry.get(this.getHostname());
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void serialize(final IValueSerializer serializer) throws BufferOverflowException {
		//super.serialize(serializer);
		serializer.putLong(this.getTimestamp());
		serializer.putString(this.getHostname());
		serializer.putDouble(this.getOneMinLoadAverage());
		serializer.putDouble(this.getFiveMinLoadAverage());
		serializer.putDouble(this.getFifteenMinLoadAverage());
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Class<?>[] getValueTypes() {
		return TYPES; // NOPMD
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String[] getValueNames() {
		return PROPERTY_NAMES; // NOPMD
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public int getSize() {
		return SIZE;
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @deprecated This record uses the {@link kieker.common.record.IMonitoringRecord.Factory} mechanism. Hence, this method is not implemented.
	 */
	@Override
	@Deprecated
	public void initFromArray(final Object[] values) {
		throw new UnsupportedOperationException();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean equals(final Object obj) {
		if (obj == null) return false;
		if (obj == this) return true;
		if (obj.getClass() != this.getClass()) return false;
		
		final LoadAverageRecord castedRecord = (LoadAverageRecord) obj;
		if (this.getLoggingTimestamp() != castedRecord.getLoggingTimestamp()) return false;
		if (this.getTimestamp() != castedRecord.getTimestamp()) return false;
		if (!this.getHostname().equals(castedRecord.getHostname())) return false;
		if (isNotEqual(this.getOneMinLoadAverage(), castedRecord.getOneMinLoadAverage())) return false;
		if (isNotEqual(this.getFiveMinLoadAverage(), castedRecord.getFiveMinLoadAverage())) return false;
		if (isNotEqual(this.getFifteenMinLoadAverage(), castedRecord.getFifteenMinLoadAverage())) return false;
		return true;
	}
	
	public final long getTimestamp() {
		return this.timestamp;
	}
	
	
	public final String getHostname() {
		return this.hostname;
	}
	
	
	public final double getOneMinLoadAverage() {
		return this.oneMinLoadAverage;
	}
	
	
	public final double getFiveMinLoadAverage() {
		return this.fiveMinLoadAverage;
	}
	
	
	public final double getFifteenMinLoadAverage() {
		return this.fifteenMinLoadAverage;
	}
	
}
