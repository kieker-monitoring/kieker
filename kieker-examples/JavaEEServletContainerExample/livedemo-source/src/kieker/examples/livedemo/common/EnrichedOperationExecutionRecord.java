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

package kieker.examples.livedemo.common;

import java.nio.BufferOverflowException;
import java.nio.ByteBuffer;

import kieker.common.record.AbstractMonitoringRecord;
import kieker.common.record.controlflow.OperationExecutionRecord;
import kieker.common.record.io.IValueSerializer;

/**
 * A record enriching Kieker's {@link OperationExecutionRecord} with a short signature and some comma seperated values.
 * 
 * @author Bjoern Weissenfels
 * 
 * @since 1.9
 */
public final class EnrichedOperationExecutionRecord extends OperationExecutionRecord {

	public static final int SIZE = (2 * AbstractMonitoringRecord.TYPE_SIZE_STRING) + (3 * AbstractMonitoringRecord.TYPE_SIZE_LONG)
			+ AbstractMonitoringRecord.TYPE_SIZE_STRING + (2 * AbstractMonitoringRecord.TYPE_SIZE_INT) + AbstractMonitoringRecord.TYPE_SIZE_DOUBLE
			+ (2 * AbstractMonitoringRecord.TYPE_SIZE_STRING);

	public static final Class<?>[] TYPES = {
		String.class, // operationSignature
		String.class, // sessionId
		long.class, // traceId
		long.class, // tin
		long.class, // tout
		String.class, // hostname
		int.class, // eoi
		int.class, // ess
		double.class, // responseTime
		String.class, // shortSignature
		String.class, // commaSeperatedValues
	};

	private static final long serialVersionUID = 4652653198700953697L;

	private final double responseTimeMS; // rounded to one decimal
	private final String shortSignature; // should be ...class.method(...)
	private final String commaSeperatedValues;

	public EnrichedOperationExecutionRecord(final String operationSignature, final String sessionId, final long traceId, final long tin, final long tout,
			final String hostname, final int eoi, final int ess, final double responseTimeMS, final String shortSignature, final String commaSeperatedValues) {
		super(operationSignature, sessionId, traceId, tin, tout, hostname, eoi, ess);

		this.responseTimeMS = responseTimeMS;
		this.shortSignature = shortSignature;
		this.commaSeperatedValues = commaSeperatedValues;
	}

	public double getResponseTime() {
		return this.responseTimeMS;
	}

	public String getShortSignature() {
		return this.shortSignature;
	}

	public String getCommaSeperatedValues() {
		return this.commaSeperatedValues;
	}

	public Object[] toArray() {
		return new Object[] {
			this.getOperationSignature(),
			this.getSessionId(),
			this.getTraceId(),
			this.getTin(),
			this.getTout(),
			this.getHostname(),
			this.getEoi(),
			this.getEss(),
			this.getResponseTime(),
			this.getShortSignature(),
			this.getCommaSeperatedValues(), };
	}

	@Override
	public void serialize(IValueSerializer serializer) throws BufferOverflowException {
		super.serialize(serializer);

		serializer.putDouble(this.getResponseTime());
		serializer.putString(this.getShortSignature());
		serializer.putString(this.getCommaSeperatedValues());
	}

	@Override
	public Class<?>[] getValueTypes() {
		return EnrichedOperationExecutionRecord.TYPES; // NOPMD
	}

	@Override
	public int getSize() {
		return EnrichedOperationExecutionRecord.SIZE;
	}

}
