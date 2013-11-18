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

package livedemo.entities;

import java.nio.BufferOverflowException;
import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;

import kieker.common.record.IMonitoringRecord;
import kieker.common.record.controlflow.OperationExecutionRecord;
import kieker.common.util.registry.IRegistry;

/**
 * @author Bjoern Weissenfels
 * 
 * @since 1.9
 */
public class Record implements IMonitoringRecord {

	private static final long serialVersionUID = 4652653198700953697L;

	private final OperationExecutionRecord record;

	private final double responseTime; // in milliseconds, rounded to one decimal
	private final String wholeRecord; // semicolon separated values
	private final String shortSignature; // should be ...class.method(...)

	public Record(final OperationExecutionRecord record) {
		this.record = record;
		this.responseTime = this.computeResponseTime();
		this.wholeRecord = this.createWholeRecord();
		this.shortSignature = this.createShortSignature();
	}

	private double computeResponseTime() {
		double resp = this.record.getTout() - this.record.getTin();
		resp = resp / 1000000; // conversion to milliseconds
		final double rounded = Math.round(resp * 10) / 10.0; // rounded to one decimal
		return rounded;
	}

	private String createWholeRecord() {
		final String result = this.record.toString();
		return result;
	}

	private String createShortSignature() {
		String[] array = this.record.getOperationSignature().split("\\(");
		array = array[0].split("\\.");
		final int end = array.length;
		final String result = "..." + array[end - 2] + "." + array[end - 1] + "(...)";
		return result;
	}

	public OperationExecutionRecord getOperationExecutionRecord() {
		return this.record;
	}

	public double getResponseTime() {
		return this.responseTime;
	}

	public String getWholeRecord() {
		return this.wholeRecord;
	}

	public String getShortSignature() {
		return this.shortSignature;
	}

	public int compareTo(final IMonitoringRecord otherRecord) {
		return this.record.compareTo(otherRecord);
	}

	public long getLoggingTimestamp() {
		return this.record.getLoggingTimestamp();
	}

	public void setLoggingTimestamp(final long timestamp) {
		this.record.setLoggingTimestamp(timestamp);
	}

	public Object[] toArray() {
		return this.record.toArray();
	}

	public void writeBytes(final ByteBuffer buffer, final IRegistry<String> stringRegistry) throws BufferOverflowException {
		this.record.writeBytes(buffer, stringRegistry);
	}

	public void initFromBytes(final ByteBuffer buffer, final IRegistry<String> stringRegistry) throws BufferUnderflowException {
		throw new UnsupportedOperationException();
	}

	public void initFromArray(final Object[] values) {
		throw new UnsupportedOperationException();
	}

	public Class<?>[] getValueTypes() {
		return this.record.getValueTypes();
	}

	public int getSize() {
		return this.record.getSize();
	}

}
