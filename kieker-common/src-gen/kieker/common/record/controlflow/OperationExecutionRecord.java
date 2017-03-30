package kieker.common.record.controlflow;

import java.nio.BufferOverflowException;
import java.nio.BufferUnderflowException;

import kieker.common.record.AbstractMonitoringRecord;
import kieker.common.record.IMonitoringRecord;
import kieker.common.record.io.IValueDeserializer;
import kieker.common.record.io.IValueSerializer;
import kieker.common.util.registry.IRegistry;

/**
 * @author Andre van Hoorn, Jan Waller
 *
 * @since 0.91
 */
public class OperationExecutionRecord extends AbstractMonitoringRecord implements IMonitoringRecord.Factory, IMonitoringRecord.BinaryFactory {
	private static final long serialVersionUID = -7768272829642950711L;

	/** Descriptive definition of the serialization size of the record. */
	public static final int SIZE = TYPE_SIZE_STRING // OperationExecutionRecord.operationSignature
			+ TYPE_SIZE_STRING // OperationExecutionRecord.sessionId
			+ TYPE_SIZE_LONG // OperationExecutionRecord.traceId
			+ TYPE_SIZE_LONG // OperationExecutionRecord.tin
			+ TYPE_SIZE_LONG // OperationExecutionRecord.tout
			+ TYPE_SIZE_STRING // OperationExecutionRecord.hostname
			+ TYPE_SIZE_INT // OperationExecutionRecord.eoi
			+ TYPE_SIZE_INT // OperationExecutionRecord.ess
	;

	public static final Class<?>[] TYPES = {
		String.class, // OperationExecutionRecord.operationSignature
		String.class, // OperationExecutionRecord.sessionId
		long.class, // OperationExecutionRecord.traceId
		long.class, // OperationExecutionRecord.tin
		long.class, // OperationExecutionRecord.tout
		String.class, // OperationExecutionRecord.hostname
		int.class, // OperationExecutionRecord.eoi
		int.class, // OperationExecutionRecord.ess
	};

	/** user-defined constants */
	public static final String NO_HOSTNAME = "<default-host>";
	public static final String NO_SESSION_ID = "<no-session-id>";
	public static final String NO_OPERATION_SIGNATURE = "noOperation";
	public static final long NO_TRACE_ID = -1L;
	public static final long NO_TIMESTAMP = -1L;
	public static final int NO_EOI_ESS = -1;

	/** default constants */
	public static final String OPERATION_SIGNATURE = NO_OPERATION_SIGNATURE;
	public static final String SESSION_ID = NO_SESSION_ID;
	public static final long TRACE_ID = NO_TRACE_ID;
	public static final long TIN = NO_TIMESTAMP;
	public static final long TOUT = NO_TIMESTAMP;
	public static final String HOSTNAME = NO_HOSTNAME;
	public static final int EOI = NO_EOI_ESS;
	public static final int ESS = NO_EOI_ESS;

	/** property declarations */
	private final String operationSignature;
	private final String sessionId;
	private final long traceId;
	private final long tin;
	private final long tout;
	private final String hostname;
	private final int eoi;
	private final int ess;

	/**
	 * Creates a new instance of this class using the given parameters.
	 *
	 * @param operationSignature
	 *            operationSignature
	 * @param sessionId
	 *            sessionId
	 * @param traceId
	 *            traceId
	 * @param tin
	 *            tin
	 * @param tout
	 *            tout
	 * @param hostname
	 *            hostname
	 * @param eoi
	 *            eoi
	 * @param ess
	 *            ess
	 */
	public OperationExecutionRecord(final String operationSignature, final String sessionId, final long traceId, final long tin, final long tout,
			final String hostname, final int eoi, final int ess) {
		this.operationSignature = operationSignature == null ? NO_OPERATION_SIGNATURE : operationSignature;
		this.sessionId = sessionId == null ? NO_SESSION_ID : sessionId;
		this.traceId = traceId;
		this.tin = tin;
		this.tout = tout;
		this.hostname = hostname == null ? NO_HOSTNAME : hostname;
		this.eoi = eoi;
		this.ess = ess;
	}

	/**
	 * This constructor converts the given array into a record.
	 * It is recommended to use the array which is the result of a call to {@link #toArray()}.
	 *
	 * @param values
	 *            The values for the record.
	 */
	public OperationExecutionRecord(final Object[] values) { // NOPMD (direct store of values)
		AbstractMonitoringRecord.checkArray(values, TYPES);
		this.operationSignature = (String) values[0];
		this.sessionId = (String) values[1];
		this.traceId = (Long) values[2];
		this.tin = (Long) values[3];
		this.tout = (Long) values[4];
		this.hostname = (String) values[5];
		this.eoi = (Integer) values[6];
		this.ess = (Integer) values[7];
	}

	/**
	 * This constructor uses the given array to initialize the fields of this record.
	 *
	 * @param values
	 *            The values for the record.
	 * @param valueTypes
	 *            The types of the elements in the first array.
	 */
	protected OperationExecutionRecord(final Object[] values, final Class<?>[] valueTypes) { // NOPMD (values stored directly)
		AbstractMonitoringRecord.checkArray(values, valueTypes);
		this.operationSignature = (String) values[0];
		this.sessionId = (String) values[1];
		this.traceId = (Long) values[2];
		this.tin = (Long) values[3];
		this.tout = (Long) values[4];
		this.hostname = (String) values[5];
		this.eoi = (Integer) values[6];
		this.ess = (Integer) values[7];
	}

	/**
	 * This constructor converts the given array into a record.
	 *
	 * @param deserializer
	 *            The deserializer to use
	 *
	 * @throws BufferUnderflowException
	 *             if buffer not sufficient
	 */
	public OperationExecutionRecord(final IValueDeserializer deserializer) throws BufferUnderflowException {
		this.operationSignature = deserializer.getString();
		this.sessionId = deserializer.getString();
		this.traceId = deserializer.getLong();
		this.tin = deserializer.getLong();
		this.tout = deserializer.getLong();
		this.hostname = deserializer.getString();
		this.eoi = deserializer.getInt();
		this.ess = deserializer.getInt();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Object[] toArray() {
		return new Object[] {
			this.getOperationSignature(),
			this.getSessionId(),
			this.getTraceId(),
			this.getTin(),
			this.getTout(),
			this.getHostname(),
			this.getEoi(),
			this.getEss()
		};
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void registerStrings(final IRegistry<String> stringRegistry) { // NOPMD (generated code)
		stringRegistry.get(this.getOperationSignature());
		stringRegistry.get(this.getSessionId());
		stringRegistry.get(this.getHostname());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void serialize(final IValueSerializer serializer) throws BufferOverflowException {
		serializer.putString(this.getOperationSignature());
		serializer.putString(this.getSessionId());
		serializer.putLong(this.getTraceId());
		serializer.putLong(this.getTin());
		serializer.putLong(this.getTout());
		serializer.putString(this.getHostname());
		serializer.putInt(this.getEoi());
		serializer.putInt(this.getEss());
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
		if (obj == null) {
			return false;
		}
		if (obj == this) {
			return true;
		}
		if (obj.getClass() != this.getClass()) {
			return false;
		}

		final OperationExecutionRecord castedRecord = (OperationExecutionRecord) obj;
		if (this.getLoggingTimestamp() != castedRecord.getLoggingTimestamp()) {
			return false;
		}
		if (!this.getOperationSignature().equals(castedRecord.getOperationSignature())) {
			return false;
		}
		if (!this.getSessionId().equals(castedRecord.getSessionId())) {
			return false;
		}
		if (this.getTraceId() != castedRecord.getTraceId()) {
			return false;
		}
		if (this.getTin() != castedRecord.getTin()) {
			return false;
		}
		if (this.getTout() != castedRecord.getTout()) {
			return false;
		}
		if (!this.getHostname().equals(castedRecord.getHostname())) {
			return false;
		}
		if (this.getEoi() != castedRecord.getEoi()) {
			return false;
		}
		if (this.getEss() != castedRecord.getEss()) {
			return false;
		}
		return true;
	}

	public final String getOperationSignature() {
		return this.operationSignature;
	}

	public final String getSessionId() {
		return this.sessionId;
	}

	public final long getTraceId() {
		return this.traceId;
	}

	public final long getTin() {
		return this.tin;
	}

	public final long getTout() {
		return this.tout;
	}

	public final String getHostname() {
		return this.hostname;
	}

	public final int getEoi() {
		return this.eoi;
	}

	public final int getEss() {
		return this.ess;
	}
}
