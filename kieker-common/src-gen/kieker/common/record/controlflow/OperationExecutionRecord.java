/***************************************************************************
 * Copyright 2023 Kieker Project (http://kieker-monitoring.net)
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

import kieker.common.exception.RecordInstantiationException;
import kieker.common.record.AbstractMonitoringRecord;
import kieker.common.record.io.IValueDeserializer;
import kieker.common.record.io.IValueSerializer;


/**
 * @author Andre van Hoorn, Jan Waller
 * API compatibility: Kieker 1.15.0
 * 
 * @since 0.91
 */
public class OperationExecutionRecord extends AbstractMonitoringRecord  {			
	/** Descriptive definition of the serialization size of the record. */
	public static final int SIZE = TYPE_SIZE_STRING // OperationExecutionRecord.operationSignature
			 + TYPE_SIZE_STRING // OperationExecutionRecord.sessionId
			 + TYPE_SIZE_LONG // OperationExecutionRecord.traceId
			 + TYPE_SIZE_LONG // OperationExecutionRecord.tin
			 + TYPE_SIZE_LONG // OperationExecutionRecord.tout
			 + TYPE_SIZE_STRING // OperationExecutionRecord.hostname
			 + TYPE_SIZE_INT // OperationExecutionRecord.eoi
			 + TYPE_SIZE_INT; // OperationExecutionRecord.ess
	
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
	
	/** user-defined constants. */
	public static final String NO_HOSTNAME = "<default-host>";
	public static final String NO_SESSION_ID = "<no-session-id>";
	public static final String NO_OPERATION_SIGNATURE = "noOperation";
	public static final long NO_TRACE_ID = -1L;
	public static final long NO_TIMESTAMP = -1L;
	public static final int NO_EOI_ESS = -1;
	/** property name array. */
	public static final String[] VALUE_NAMES = {
		"operationSignature",
		"sessionId",
		"traceId",
		"tin",
		"tout",
		"hostname",
		"eoi",
		"ess",
	};
	
	/** default constants. */
	public static final String OPERATION_SIGNATURE = NO_OPERATION_SIGNATURE;
	public static final String SESSION_ID = NO_SESSION_ID;
	public static final long TRACE_ID = NO_TRACE_ID;
	public static final long TIN = NO_TIMESTAMP;
	public static final long TOUT = NO_TIMESTAMP;
	public static final String HOSTNAME = NO_HOSTNAME;
	public static final int EOI = NO_EOI_ESS;
	public static final int ESS = NO_EOI_ESS;
	private static final long serialVersionUID = -7768272829642950711L;
	
	/** property declarations. */
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
	public OperationExecutionRecord(final String operationSignature, final String sessionId, final long traceId, final long tin, final long tout, final String hostname, final int eoi, final int ess) {
		this.operationSignature = operationSignature == null?NO_OPERATION_SIGNATURE:operationSignature;
		this.sessionId = sessionId == null?NO_SESSION_ID:sessionId;
		this.traceId = traceId;
		this.tin = tin;
		this.tout = tout;
		this.hostname = hostname == null?NO_HOSTNAME:hostname;
		this.eoi = eoi;
		this.ess = ess;
	}


	/**
	 * @param deserializer
	 *            The deserializer to use
	 * @throws RecordInstantiationException 
	 *            when the record could not be deserialized
	 */
	public OperationExecutionRecord(final IValueDeserializer deserializer) throws RecordInstantiationException {
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
	public String[] getValueNames() {
		return VALUE_NAMES; // NOPMD
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
	/**
	 * {@inheritDoc}
	 */
	@Override
	public int hashCode() {
		int code = 0;
		code += this.getOperationSignature().hashCode();
		code += this.getSessionId().hashCode();
		code += ((int)this.getTraceId());
		code += ((int)this.getTin());
		code += ((int)this.getTout());
		code += this.getHostname().hashCode();
		code += ((int)this.getEoi());
		code += ((int)this.getEss());
		
		return code;
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
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		String result = "OperationExecutionRecord: ";
		result += "operationSignature = ";
		result += this.getOperationSignature() + ", ";
		
		result += "sessionId = ";
		result += this.getSessionId() + ", ";
		
		result += "traceId = ";
		result += this.getTraceId() + ", ";
		
		result += "tin = ";
		result += this.getTin() + ", ";
		
		result += "tout = ";
		result += this.getTout() + ", ";
		
		result += "hostname = ";
		result += this.getHostname() + ", ";
		
		result += "eoi = ";
		result += this.getEoi() + ", ";
		
		result += "ess = ";
		result += this.getEss() + ", ";
		
		return result;
	}
}
