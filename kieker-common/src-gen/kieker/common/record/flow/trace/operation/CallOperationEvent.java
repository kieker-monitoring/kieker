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
package kieker.common.record.flow.trace.operation;

import java.nio.BufferOverflowException;

import kieker.common.exception.RecordInstantiationException;
import kieker.common.record.flow.trace.operation.AbstractOperationEvent;
import kieker.common.record.io.IValueDeserializer;
import kieker.common.record.io.IValueSerializer;

import kieker.common.record.flow.ICallRecord;

/**
 * @author Andre van Hoorn, Holger Knoche, Jan Waller
 * API compatibility: Kieker 2.0.0
 * 
 * @since 1.5
 */
public class CallOperationEvent extends AbstractOperationEvent implements ICallRecord {			
	/** Descriptive definition of the serialization size of the record. */
	public static final int SIZE = TYPE_SIZE_LONG // IEventRecord.timestamp
			 + TYPE_SIZE_LONG // ITraceRecord.traceId
			 + TYPE_SIZE_INT // ITraceRecord.orderIndex
			 + TYPE_SIZE_STRING // IOperationSignature.operationSignature
			 + TYPE_SIZE_STRING // IClassSignature.classSignature
			 + TYPE_SIZE_STRING // ICallRecord.calleeOperationSignature
			 + TYPE_SIZE_STRING; // ICallRecord.calleeClassSignature
	
	public static final Class<?>[] TYPES = {
		long.class, // IEventRecord.timestamp
		long.class, // ITraceRecord.traceId
		int.class, // ITraceRecord.orderIndex
		String.class, // IOperationSignature.operationSignature
		String.class, // IClassSignature.classSignature
		String.class, // ICallRecord.calleeOperationSignature
		String.class, // ICallRecord.calleeClassSignature
	};
	
	/** property name array. */
	public static final String[] VALUE_NAMES = {
		"timestamp",
		"traceId",
		"orderIndex",
		"operationSignature",
		"classSignature",
		"calleeOperationSignature",
		"calleeClassSignature",
	};
	
	/** default constants. */
	public static final String CALLEE_OPERATION_SIGNATURE = "";
	public static final String CALLEE_CLASS_SIGNATURE = "";
	private static final long serialVersionUID = 2408771186779452968L;
	
	/** property declarations. */
	private final String calleeOperationSignature;
	private final String calleeClassSignature;
	
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
	 * @param calleeOperationSignature
	 *            calleeOperationSignature
	 * @param calleeClassSignature
	 *            calleeClassSignature
	 */
	public CallOperationEvent(final long timestamp, final long traceId, final int orderIndex, final String operationSignature, final String classSignature, final String calleeOperationSignature, final String calleeClassSignature) {
		super(timestamp, traceId, orderIndex, operationSignature, classSignature);
		this.calleeOperationSignature = calleeOperationSignature == null?CALLEE_OPERATION_SIGNATURE:calleeOperationSignature;
		this.calleeClassSignature = calleeClassSignature == null?CALLEE_CLASS_SIGNATURE:calleeClassSignature;
	}


	/**
	 * @param deserializer
	 *            The deserializer to use
	 * @throws RecordInstantiationException 
	 *            when the record could not be deserialized
	 */
	public CallOperationEvent(final IValueDeserializer deserializer) throws RecordInstantiationException {
		super(deserializer);
		this.calleeOperationSignature = deserializer.getString();
		this.calleeClassSignature = deserializer.getString();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void serialize(final IValueSerializer serializer) throws BufferOverflowException {
		serializer.putLong(this.getTimestamp());
		serializer.putLong(this.getTraceId());
		serializer.putInt(this.getOrderIndex());
		serializer.putString(this.getOperationSignature());
		serializer.putString(this.getClassSignature());
		serializer.putString(this.getCalleeOperationSignature());
		serializer.putString(this.getCalleeClassSignature());
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
		
		final CallOperationEvent castedRecord = (CallOperationEvent) obj;
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
		if (!this.getCalleeOperationSignature().equals(castedRecord.getCalleeOperationSignature())) {
			return false;
		}
		if (!this.getCalleeClassSignature().equals(castedRecord.getCalleeClassSignature())) {
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
		code += ((int)this.getTimestamp());
		code += ((int)this.getTraceId());
		code += ((int)this.getOrderIndex());
		code += this.getOperationSignature().hashCode();
		code += this.getClassSignature().hashCode();
		code += this.getCalleeOperationSignature().hashCode();
		code += this.getCalleeClassSignature().hashCode();
		
		return code;
	}
	
	public final String getCallerOperationSignature() {
		return this.getOperationSignature();
	}
	
	
	public final String getCallerClassSignature() {
		return this.getClassSignature();
	}
	
	
	public final String getCalleeOperationSignature() {
		return this.calleeOperationSignature;
	}
	
	
	public final String getCalleeClassSignature() {
		return this.calleeClassSignature;
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		String result = "CallOperationEvent: ";
		result += "timestamp = ";
		result += this.getTimestamp() + ", ";
		
		result += "traceId = ";
		result += this.getTraceId() + ", ";
		
		result += "orderIndex = ";
		result += this.getOrderIndex() + ", ";
		
		result += "operationSignature = ";
		result += this.getOperationSignature() + ", ";
		
		result += "classSignature = ";
		result += this.getClassSignature() + ", ";
		
		result += "calleeOperationSignature = ";
		result += this.getCalleeOperationSignature() + ", ";
		
		result += "calleeClassSignature = ";
		result += this.getCalleeClassSignature() + ", ";
		
		return result;
	}
}
