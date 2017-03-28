package kieker.common.record.flow.trace.operation;

import java.nio.BufferUnderflowException;

import kieker.common.record.flow.IOperationRecord;
import kieker.common.record.flow.trace.AbstractTraceEvent;
import kieker.common.record.io.IValueDeserializer;

/**
 * @author Jan Waller
 *
 * @since 1.5
 */
public abstract class AbstractOperationEvent extends AbstractTraceEvent implements IOperationRecord {
	private static final long serialVersionUID = -4876224316055177674L;

	/** user-defined constants */

	/** default constants */
	public static final String OPERATION_SIGNATURE = "";
	public static final String CLASS_SIGNATURE = "";

	/** property declarations */
	private final String operationSignature;
	private final String classSignature;

	/**
	 * Creates a new instance of this class using the given parameters.
	 *
	 * @param timestamp
	 *            timestamp
	 * @param traceId
	 *            traceId
	 * @param orderIndex
	 *            orderIndex
	 * @param operationSignature
	 *            operationSignature
	 * @param classSignature
	 *            classSignature
	 */
	public AbstractOperationEvent(final long timestamp, final long traceId, final int orderIndex, final String operationSignature, final String classSignature) {
		super(timestamp, traceId, orderIndex);
		this.operationSignature = operationSignature == null ? OPERATION_SIGNATURE : operationSignature;
		this.classSignature = classSignature == null ? CLASS_SIGNATURE : classSignature;
	}

	/**
	 * This constructor uses the given array to initialize the fields of this record.
	 *
	 * @param values
	 *            The values for the record.
	 * @param valueTypes
	 *            The types of the elements in the first array.
	 */
	protected AbstractOperationEvent(final Object[] values, final Class<?>[] valueTypes) { // NOPMD (values stored directly)
		super(values, valueTypes);
		this.operationSignature = (String) values[3];
		this.classSignature = (String) values[4];
	}

	/**
	 * This constructor converts the given array into a record.
	 *
	 * @param deserializer
	 *            The value deserializer to use.
	 *
	 * @throws BufferUnderflowException
	 *             if buffer not sufficient
	 */
	public AbstractOperationEvent(final IValueDeserializer deserializer) throws BufferUnderflowException {
		super(deserializer);

		this.operationSignature = deserializer.getString();
		this.classSignature = deserializer.getString();
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

		final AbstractOperationEvent castedRecord = (AbstractOperationEvent) obj;
		if (this.getLoggingTimestamp() != castedRecord.getLoggingTimestamp()) {
			return false;
		}
		if (this.getTimestamp() != castedRecord.getTimestamp()) {
			return false;
		}
		if (this.getTraceId() != castedRecord.getTraceId()) {
			return false;
		}
		if (this.getOrderIndex() != castedRecord.getOrderIndex()) {
			return false;
		}
		if (!this.getOperationSignature().equals(castedRecord.getOperationSignature())) {
			return false;
		}
		if (!this.getClassSignature().equals(castedRecord.getClassSignature())) {
			return false;
		}
		return true;
	}

	@Override
	public final String getOperationSignature() {
		return this.operationSignature;
	}

	@Override
	public final String getClassSignature() {
		return this.classSignature;
	}
}
