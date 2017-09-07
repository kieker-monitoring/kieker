/***************************************************************************
 * Copyright 2017 iObserve Project
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
package kieker.common.record.flow.thread;


import kieker.common.record.flow.AbstractEvent;
import kieker.common.record.io.IValueDeserializer;

import kieker.common.record.flow.IThreadBasedRecord;
import kieker.common.record.flow.IOperationSignature;
import kieker.common.record.flow.IClassSignature;

/**
 * @author Christian Wulf
 * API compatibility: Kieker 1.13.0
 * 
 * @since 1.13
 */
public abstract class AbstractThreadBasedEvent extends AbstractEvent implements IThreadBasedRecord, IOperationSignature, IClassSignature {
	private static final long serialVersionUID = -4756765331556509113L;

	
	
	/** default constants. */
	public static final long THREAD_ID = -1L;
	public static final int ORDER_INDEX = -1;
	public static final String OPERATION_SIGNATURE = "";
	public static final String CLASS_SIGNATURE = "";
	
		
	/** property declarations. */
	private final long threadId;
	private final int orderIndex;
	private final String operationSignature;
	private final String classSignature;
	
	/**
	 * Creates a new instance of this class using the given parameters.
	 * 
	 * @param timestamp
	 *            timestamp
	 * @param threadId
	 *            threadId
	 * @param orderIndex
	 *            orderIndex
	 * @param operationSignature
	 *            operationSignature
	 * @param classSignature
	 *            classSignature
	 */
	public AbstractThreadBasedEvent(final long timestamp, final long threadId, final int orderIndex, final String operationSignature, final String classSignature) {
		super(timestamp);
		this.threadId = threadId;
		this.orderIndex = orderIndex;
		this.operationSignature = operationSignature == null?OPERATION_SIGNATURE:operationSignature;
		this.classSignature = classSignature == null?CLASS_SIGNATURE:classSignature;
	}


	/**
	 * This constructor uses the given array to initialize the fields of this record.
	 * 
	 * @param values
	 *            The values for the record.
	 * @param valueTypes
	 *            The types of the elements in the first array.
	 *
	 * @deprecated since 1.13. Use {@link #AbstractThreadBasedEvent(IValueDeserializer)} instead.
	 */
	@Deprecated
	protected AbstractThreadBasedEvent(final Object[] values, final Class<?>[] valueTypes) { // NOPMD (values stored directly)
		super(values, valueTypes);
		this.threadId = (Long) values[1];
		this.orderIndex = (Integer) values[2];
		this.operationSignature = (String) values[3];
		this.classSignature = (String) values[4];
	}

	
	/**
	 * @param deserializer
	 *            The deserializer to use
	 */
	public AbstractThreadBasedEvent(final IValueDeserializer deserializer) {
		super(deserializer);
		this.threadId = deserializer.getLong();
		this.orderIndex = deserializer.getInt();
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
		if (obj == null) return false;
		if (obj == this) return true;
		if (obj.getClass() != this.getClass()) return false;
		
		final AbstractThreadBasedEvent castedRecord = (AbstractThreadBasedEvent) obj;
		if (this.getLoggingTimestamp() != castedRecord.getLoggingTimestamp()) return false;
		if (this.getTimestamp() != castedRecord.getTimestamp()) return false;
		if (this.getThreadId() != castedRecord.getThreadId()) return false;
		if (this.getOrderIndex() != castedRecord.getOrderIndex()) return false;
		if (!this.getOperationSignature().equals(castedRecord.getOperationSignature())) return false;
		if (!this.getClassSignature().equals(castedRecord.getClassSignature())) return false;
		return true;
	}
	
	public final long getThreadId() {
		return this.threadId;
	}
	
	
	public final int getOrderIndex() {
		return this.orderIndex;
	}
	
	
	public final String getOperationSignature() {
		return this.operationSignature;
	}
	
	
	public final String getClassSignature() {
		return this.classSignature;
	}
	
}
