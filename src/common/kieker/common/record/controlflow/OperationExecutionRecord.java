/***************************************************************************
 * Copyright 2013 Kieker Project (http://kieker-monitoring.net)
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

package kieker.common.record.controlflow;

import java.nio.BufferOverflowException;
import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;

import kieker.common.record.AbstractMonitoringRecord;
import kieker.common.record.IMonitoringRecord;
import kieker.common.util.registry.IRegistry;

/**
 * @author Andre van Hoorn, Jan Waller
 * 
 * @since 0.91
 */
public class OperationExecutionRecord extends AbstractMonitoringRecord implements IMonitoringRecord.Factory, IMonitoringRecord.BinaryFactory {
	public static final int SIZE = (Integer.SIZE + Integer.SIZE + Long.SIZE + Long.SIZE + Long.SIZE + Integer.SIZE + Integer.SIZE + Integer.SIZE) / 8;
	public static final Class<?>[] TYPES = {
		String.class, // operationSignature
		String.class, // sessionId
		long.class, // traceId
		long.class, // tin
		long.class, // tout
		String.class, // hostname
		int.class, // eoi
		int.class, // ess
	};

	/**
	 * Constant to be used if no hostname required.
	 */
	public static final String NO_HOSTNAME = "<default-host>";

	/**
	 * Constant to be used if no session ID required.
	 */
	public static final String NO_SESSION_ID = "<no-session-id>";

	/**
	 * Constant to be used if no trace ID required.
	 */
	public static final long NO_TRACEID = -1;

	/**
	 * Constant to be used if no timestamp required.
	 */
	public static final long NO_TIMESTAMP = -1;

	/**
	 * Constant to be used if no eoi or ess required.
	 */
	public static final int NO_EOI_ESS = -1;

	/**
	 * This field should not be exported, because it makes little sense to have no associated operation.
	 */
	private static final String NO_OPERATION_SIGNATURE = "noOperation";

	private static final long serialVersionUID = 733578834225565988L;

	private final String hostname;
	private final String operationSignature;
	private final String sessionId;
	private final long traceId;
	private final long tin;
	private final long tout;
	private final int eoi;
	private final int ess;

	/**
	 * Creates a new {@link OperationExecutionRecord} with the given parameters.
	 * 
	 * @param operationSignature
	 *            an operation string, as defined in {@link kieker.common.util.signature.ClassOperationSignaturePair#splitOperationSignatureStr(String)}.
	 * @param sessionId
	 *            the session ID; must not be null, use {@link #NO_SESSION_ID} if no session ID desired.
	 * @param traceId
	 *            the trace ID; use {@link #NO_TRACEID} if no trace ID desired.
	 * @param tin
	 *            the execution start timestamp; use {@link #NO_TIMESTAMP} if no timestamp desired.
	 * @param tout
	 *            the execution stop timestamp; use {@link #NO_TIMESTAMP} if no timestamp desired.
	 * @param hostname
	 *            the host name; must not be null, use {@link #NO_HOSTNAME} if no host name desired.
	 * @param eoi
	 *            the execution order index (eoi); use {@link #NO_EOI_ESS} if no eoi desired.
	 * @param ess
	 *            the execution stack size (ess); use {@link #NO_EOI_ESS} if no ess desired.
	 */
	public OperationExecutionRecord(final String operationSignature, final String sessionId, final long traceId, final long tin, final long tout,
			final String hostname, final int eoi, final int ess) {
		this.operationSignature = (operationSignature == null) ? NO_OPERATION_SIGNATURE : operationSignature; // NOCS
		this.traceId = traceId;
		this.tin = tin;
		this.tout = tout;
		this.sessionId = (sessionId == null) ? NO_SESSION_ID : sessionId; // NOCS
		this.hostname = (hostname == null) ? NO_HOSTNAME : hostname; // NOCS
		this.eoi = eoi;
		this.ess = ess;
	}

	/**
	 * This constructor converts the given array into a record. It is recommended to use the array which is the result of a call to {@link #toArray()}.
	 * 
	 * @param values
	 *            The values for the record.
	 */
	public OperationExecutionRecord(final Object[] values) { // NOPMD (values stored directly)
		AbstractMonitoringRecord.checkArray(values, TYPES); // throws IllegalArgumentException
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
	 * @param buffer
	 *            The bytes for the record.
	 * 
	 * @throws BufferUnderflowException
	 *             if buffer not sufficient
	 */
	public OperationExecutionRecord(final ByteBuffer buffer, final IRegistry<String> stringRegistry) throws BufferUnderflowException {
		this.operationSignature = stringRegistry.get(buffer.getInt());
		this.sessionId = stringRegistry.get(buffer.getInt());
		this.traceId = buffer.getLong();
		this.tin = buffer.getLong();
		this.tout = buffer.getLong();
		this.hostname = stringRegistry.get(buffer.getInt());
		this.eoi = buffer.getInt();
		this.ess = buffer.getInt();
	}

	/**
	 * {@inheritDoc}
	 */
	public Object[] toArray() {
		return new Object[] {
			this.getOperationSignature(),
			this.getSessionId(),
			this.getTraceId(),
			this.getTin(),
			this.getTout(),
			this.getHostname(),
			this.getEoi(),
			this.getEss(), };
	}

	/**
	 * {@inheritDoc}
	 */
	public void writeBytes(final ByteBuffer buffer, final IRegistry<String> stringRegistry) throws BufferOverflowException {
		buffer.putInt(stringRegistry.get(this.getOperationSignature()));
		buffer.putInt(stringRegistry.get(this.getSessionId()));
		buffer.putLong(this.getTraceId());
		buffer.putLong(this.getTin());
		buffer.putLong(this.getTout());
		buffer.putInt(stringRegistry.get(this.getHostname()));
		buffer.putInt(this.getEoi());
		buffer.putInt(this.getEss());
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @deprecated This record uses the {@link kieker.common.record.IMonitoringRecord.Factory} mechanism. Hence, this method is not implemented.
	 */
	@Deprecated
	public final void initFromArray(final Object[] values) {
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @deprecated This record uses the {@link kieker.common.record.IMonitoringRecord.BinaryFactory} mechanism. Hence, this method is not implemented.
	 */
	@Deprecated
	public final void initFromBytes(final ByteBuffer buffer, final IRegistry<String> stringRegistry) throws BufferUnderflowException {
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc}
	 */
	public Class<?>[] getValueTypes() {
		return TYPES; // NOPMD
	}

	/**
	 * {@inheritDoc}
	 */
	public int getSize() {
		return SIZE;
	}

	/**
	 * @return the hostname
	 */
	public final String getHostname() {
		return this.hostname;
	}

	/**
	 * @return the operationSignature
	 */
	public final String getOperationSignature() {
		return this.operationSignature;
	}

	/**
	 * @return the sessionId
	 */
	public final String getSessionId() {
		return this.sessionId;
	}

	/**
	 * @return the traceId
	 */
	public final long getTraceId() {
		return this.traceId;
	}

	/**
	 * @return the tin
	 */
	public final long getTin() {
		return this.tin;
	}

	/**
	 * @return the tout
	 */
	public final long getTout() {
		return this.tout;
	}

	/**
	 * @return the eoi
	 */
	public final int getEoi() {
		return this.eoi;
	}

	/**
	 * @return the ess
	 */
	public final int getEss() {
		return this.ess;
	}
}
