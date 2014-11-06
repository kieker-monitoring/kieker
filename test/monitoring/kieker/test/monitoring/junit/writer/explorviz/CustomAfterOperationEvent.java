/***************************************************************************
 * Copyright 2014 Kieker Project (http://kieker-monitoring.net)
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

package kieker.test.monitoring.junit.writer.explorviz;

import java.nio.BufferOverflowException;
import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;

import kieker.common.record.IMonitoringRecord;
import kieker.common.util.registry.IRegistry;

/**
 * @author Micky Singh Multani
 * 
 * @since 1.11
 */
public class CustomAfterOperationEvent implements IMonitoringRecord {
	private static final long serialVersionUID = -6594854001439034288L;

	private final long timestamp;
	private final long traceId;
	private final int orderIndex;

	public CustomAfterOperationEvent(final long timestamp, final long traceId, final int orderIndex) {
		this.timestamp = timestamp;
		this.traceId = traceId;
		this.orderIndex = orderIndex;
	}

	public long getTimestamp() {
		return this.timestamp;
	}

	public long getTraceId() {
		return this.traceId;
	}

	public int getOrderIndex() {
		return this.orderIndex;
	}

	@Override
	public Object[] toArray() {
		return new Object[] {
			this.getTimestamp(),
			this.getTraceId(),
			this.getOrderIndex()
		};
	}

	@Override
	public int compareTo(final IMonitoringRecord o) {
		return 0;
	}

	@Override
	public long getLoggingTimestamp() {
		return 0;
	}

	@Override
	public void setLoggingTimestamp(final long timestamp) {}

	@Override
	public void writeBytes(final ByteBuffer buffer, final IRegistry<String> stringRegistry) throws BufferOverflowException {}

	@Override
	public void initFromBytes(final ByteBuffer buffer, final IRegistry<String> stringRegistry) throws BufferUnderflowException {}

	@Override
	public void initFromArray(final Object[] values) {}

	@Override
	public Class<?>[] getValueTypes() {
		return null;
	}

	@Override
	public int getSize() {
		return 0;
	}

}
