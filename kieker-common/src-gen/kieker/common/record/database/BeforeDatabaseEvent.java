package kieker.common.record.database;

import java.nio.BufferOverflowException;
import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;

import kieker.common.record.AbstractMonitoringRecord;
import kieker.common.record.IMonitoringRecord;
import kieker.common.util.registry.IRegistry;

import kieker.common.record.flow.IEventRecord;
import kieker.common.record.flow.IOperationRecord;
import kieker.common.record.flow.ITraceRecord;
import kieker.common.record.io.IValueDeserializer;
import kieker.common.record.io.IValueSerializer;

/**
 * @author Christian Zirkelbach (czi@informatik.uni-kiel.de) API compatibility: Kieker 1.10.0
 * 
 * @since 1.14
 */
public class BeforeDatabaseEvent extends AbstractMonitoringRecord implements IMonitoringRecord.Factory,
		IMonitoringRecord.BinaryFactory, IEventRecord, IOperationRecord, ITraceRecord {
	private static final long serialVersionUID = -6261230821406425942L;

	/** Descriptive definition of the serialization size of the record. */
	public static final int SIZE = TYPE_SIZE_LONG // IEventRecord.timestamp
			+ TYPE_SIZE_STRING // IOperationSignature.operationSignature
			+ TYPE_SIZE_STRING // IClassSignature.classSignature
			+ TYPE_SIZE_LONG // ITraceRecord.traceId
			+ TYPE_SIZE_INT // ITraceRecord.orderIndex
			+ TYPE_SIZE_STRING // BeforeDatabaseEvent.parameters
			+ TYPE_SIZE_STRING // BeforeDatabaseEvent.technology
	;

	public static final Class<?>[] TYPES = { long.class, // IEventRecord.timestamp
			String.class, // IOperationSignature.operationSignature
			String.class, // IClassSignature.classSignature
			long.class, // ITraceRecord.traceId
			int.class, // ITraceRecord.orderIndex
			String.class, // BeforeDatabaseEvent.parameters
			String.class, // BeforeDatabaseEvent.technology
	};

	/** default constants. */
	public static final long TIMESTAMP = 0L;
	public static final String OPERATION_SIGNATURE = "";
	public static final String CLASS_SIGNATURE = "";
	public static final long TRACE_ID = -1L;
	public static final int ORDER_INDEX = -1;
	public static final String PARAMETERS = "";
	public static final String TECHNOLOGY = "<default-technology>";

	/** property name array. */
	private static final String[] PROPERTY_NAMES = { "timestamp", "operationSignature", "classSignature", "traceId",
			"orderIndex", "parameters", "technology", };

	/** property declarations. */
	private final long timestamp;
	private final String operationSignature;
	private final String classSignature;
	private final long traceId;
	private final int orderIndex;
	private final String parameters;
	private final String technology;

	/**
	 * Creates a new instance of this class using the given parameters.
	 * 
	 * @param timestamp
	 *            timestamp
	 * @param operationSignature
	 *            operationSignature
	 * @param classSignature
	 *            classSignature
	 * @param traceId
	 *            traceId
	 * @param orderIndex
	 *            orderIndex
	 * @param parameters
	 *            parameters
	 * @param technology
	 *            technology
	 */
	public BeforeDatabaseEvent(final long timestamp, final String operationSignature, final String classSignature,
			final long traceId, final int orderIndex, final String parameters, final String technology) {
		this.timestamp = timestamp;
		this.operationSignature = operationSignature == null ? OPERATION_SIGNATURE : operationSignature;
		this.classSignature = classSignature == null ? CLASS_SIGNATURE : classSignature;
		this.traceId = traceId;
		this.orderIndex = orderIndex;
		this.parameters = parameters == null ? "" : parameters;
		this.technology = technology == null ? TECHNOLOGY : technology;
	}

	/**
	 * This constructor converts the given array into a record. It is recommended to
	 * use the array which is the result of a call to {@link #toArray()}.
	 * 
	 * @param values
	 *            The values for the record.
	 *
	 * @deprecated since 1.13. Use {@link #BeforeDatabaseEvent(IValueDeserializer)}
	 *             instead.
	 */
	@Deprecated
	public BeforeDatabaseEvent(final Object[] values) { // NOPMD (direct store of values)
		AbstractMonitoringRecord.checkArray(values, TYPES);
		this.timestamp = (Long) values[0];
		this.operationSignature = (String) values[1];
		this.classSignature = (String) values[2];
		this.traceId = (Long) values[3];
		this.orderIndex = (Integer) values[4];
		this.parameters = (String) values[5];
		this.technology = (String) values[6];
	}

	/**
	 * This constructor uses the given array to initialize the fields of this
	 * record.
	 * 
	 * @param values
	 *            The values for the record.
	 * @param valueTypes
	 *            The types of the elements in the first array.
	 *
	 * @deprecated since 1.13. Use {@link #BeforeDatabaseEvent(IValueDeserializer)}
	 *             instead.
	 */
	@Deprecated
	protected BeforeDatabaseEvent(final Object[] values, final Class<?>[] valueTypes) { // NOPMD (values stored
																						// directly)
		AbstractMonitoringRecord.checkArray(values, valueTypes);
		this.timestamp = (Long) values[0];
		this.operationSignature = (String) values[1];
		this.classSignature = (String) values[2];
		this.traceId = (Long) values[3];
		this.orderIndex = (Integer) values[4];
		this.parameters = (String) values[5];
		this.technology = (String) values[6];
	}

	/**
	 * This constructor converts the given buffer into a record.
	 * 
	 * @param buffer
	 *            The bytes for the record
	 * @param stringRegistry
	 *            The string registry for deserialization
	 * 
	 * @throws BufferUnderflowException
	 *             if buffer not sufficient
	 *
	 * @deprecated since 1.13. Use {@link #BeforeDatabaseEvent(IValueDeserializer)}
	 *             instead.
	 */
	@Deprecated
	public BeforeDatabaseEvent(final ByteBuffer buffer, final IRegistry<String> stringRegistry)
			throws BufferUnderflowException {
		this.timestamp = buffer.getLong();
		this.operationSignature = stringRegistry.get(buffer.getInt());
		this.classSignature = stringRegistry.get(buffer.getInt());
		this.traceId = buffer.getLong();
		this.orderIndex = buffer.getInt();
		this.parameters = stringRegistry.get(buffer.getInt());
		this.technology = stringRegistry.get(buffer.getInt());
	}

	/**
	 * {@inheritDoc}
	 *
	 * @deprecated since 1.13. Use {@link #serialize(IValueSerializer)} with an
	 *             array serializer instead.
	 */
	@Override
	@Deprecated
	public Object[] toArray() {
		return new Object[] { this.getTimestamp(), this.getOperationSignature(), this.getClassSignature(),
				this.getTraceId(), this.getOrderIndex(), this.getParameters(), this.getTechnology() };
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void registerStrings(final IRegistry<String> stringRegistry) { // NOPMD (generated code)
		stringRegistry.get(this.getOperationSignature());
		stringRegistry.get(this.getClassSignature());
		stringRegistry.get(this.getParameters());
		stringRegistry.get(this.getTechnology());
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
	 * @deprecated This record uses the
	 *             {@link kieker.common.record.IMonitoringRecord.Factory} mechanism.
	 *             Hence, this method is not implemented.
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
		if (obj == null)
			return false;
		if (obj == this)
			return true;
		if (obj.getClass() != this.getClass())
			return false;

		final BeforeDatabaseEvent castedRecord = (BeforeDatabaseEvent) obj;
		if (this.getLoggingTimestamp() != castedRecord.getLoggingTimestamp())
			return false;
		if (this.getTimestamp() != castedRecord.getTimestamp())
			return false;
		if (!this.getOperationSignature().equals(castedRecord.getOperationSignature()))
			return false;
		if (!this.getClassSignature().equals(castedRecord.getClassSignature()))
			return false;
		if (this.getTraceId() != castedRecord.getTraceId())
			return false;
		if (this.getOrderIndex() != castedRecord.getOrderIndex())
			return false;
		if (!this.getParameters().equals(castedRecord.getParameters()))
			return false;
		if (!this.getTechnology().equals(castedRecord.getTechnology()))
			return false;
		return true;
	}

	public final long getTimestamp() {
		return this.timestamp;
	}

	public final String getOperationSignature() {
		return this.operationSignature;
	}

	public final String getClassSignature() {
		return this.classSignature;
	}

	public final long getTraceId() {
		return this.traceId;
	}

	public final int getOrderIndex() {
		return this.orderIndex;
	}

	public final String getParameters() {
		return this.parameters;
	}

	public final String getTechnology() {
		return this.technology;
	}

	/**
	 * @param deserializer
	 *            The deserializer to use
	 */
	public BeforeDatabaseEvent(final IValueDeserializer deserializer) {
		this.timestamp = deserializer.getLong();
		this.operationSignature = deserializer.getString();
		this.classSignature = deserializer.getString();
		this.traceId = deserializer.getLong();
		this.orderIndex = deserializer.getInt();
		this.parameters = deserializer.getString();
		this.technology = deserializer.getString();
	}

	@Override
	public void serialize(IValueSerializer serializer) throws BufferOverflowException {
		serializer.putLong(this.getTimestamp());
		serializer.putLong(this.getTraceId());
		serializer.putInt(this.getOrderIndex());
		serializer.putLong(this.getTraceId());
		serializer.putInt(this.getOrderIndex());
		serializer.putString(this.getTechnology());
	}

}