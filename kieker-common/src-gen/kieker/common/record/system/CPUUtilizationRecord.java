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
package kieker.common.record.system;

import java.nio.BufferOverflowException;

import kieker.common.exception.RecordInstantiationException;
import kieker.common.record.AbstractMonitoringRecord;
import kieker.common.record.io.IValueDeserializer;
import kieker.common.record.io.IValueSerializer;


/**
 * @author Andre van Hoorn, Jan Waller
 * API compatibility: Kieker 1.15.0
 * 
 * @since 1.3
 */
public class CPUUtilizationRecord extends AbstractMonitoringRecord  {			
	/** Descriptive definition of the serialization size of the record. */
	public static final int SIZE = TYPE_SIZE_LONG // CPUUtilizationRecord.timestamp
			 + TYPE_SIZE_STRING // CPUUtilizationRecord.hostname
			 + TYPE_SIZE_STRING // CPUUtilizationRecord.cpuID
			 + TYPE_SIZE_DOUBLE // CPUUtilizationRecord.user
			 + TYPE_SIZE_DOUBLE // CPUUtilizationRecord.system
			 + TYPE_SIZE_DOUBLE // CPUUtilizationRecord.wait
			 + TYPE_SIZE_DOUBLE // CPUUtilizationRecord.nice
			 + TYPE_SIZE_DOUBLE // CPUUtilizationRecord.irq
			 + TYPE_SIZE_DOUBLE // CPUUtilizationRecord.totalUtilization
			 + TYPE_SIZE_DOUBLE; // CPUUtilizationRecord.idle
	
	public static final Class<?>[] TYPES = {
		long.class, // CPUUtilizationRecord.timestamp
		String.class, // CPUUtilizationRecord.hostname
		String.class, // CPUUtilizationRecord.cpuID
		double.class, // CPUUtilizationRecord.user
		double.class, // CPUUtilizationRecord.system
		double.class, // CPUUtilizationRecord.wait
		double.class, // CPUUtilizationRecord.nice
		double.class, // CPUUtilizationRecord.irq
		double.class, // CPUUtilizationRecord.totalUtilization
		double.class, // CPUUtilizationRecord.idle
	};
	
	/** property name array. */
	public static final String[] VALUE_NAMES = {
		"timestamp",
		"hostname",
		"cpuID",
		"user",
		"system",
		"wait",
		"nice",
		"irq",
		"totalUtilization",
		"idle",
	};
	
	/** default constants. */
	public static final long TIMESTAMP = 0L;
	public static final String HOSTNAME = "";
	public static final String CPU_ID = "";
	public static final double USER = 0.0;
	public static final double SYSTEM = 0.0;
	public static final double WAIT = 0.0;
	public static final double NICE = 0.0;
	public static final double IRQ = 0.0;
	public static final double TOTAL_UTILIZATION = 0.0;
	public static final double IDLE = 0.0;
	private static final long serialVersionUID = -7851990890838902217L;
	
	/** property declarations. */
	private final long timestamp;
	private final String hostname;
	private final String cpuID;
	private final double user;
	private final double system;
	private final double wait;
	private final double nice;
	private final double irq;
	private final double totalUtilization;
	private final double idle;
	
	/**
	 * Creates a new instance of this class using the given parameters.
	 * 
	 * @param timestamp
	 *            timestamp
	 * @param hostname
	 *            hostname
	 * @param cpuID
	 *            cpuID
	 * @param user
	 *            user
	 * @param system
	 *            system
	 * @param wait
	 *            wait
	 * @param nice
	 *            nice
	 * @param irq
	 *            irq
	 * @param totalUtilization
	 *            totalUtilization
	 * @param idle
	 *            idle
	 */
	public CPUUtilizationRecord(final long timestamp, final String hostname, final String cpuID, final double user, final double system, final double wait, final double nice, final double irq, final double totalUtilization, final double idle) {
		this.timestamp = timestamp;
		this.hostname = hostname == null?HOSTNAME:hostname;
		this.cpuID = cpuID == null?CPU_ID:cpuID;
		this.user = user;
		this.system = system;
		this.wait = wait;
		this.nice = nice;
		this.irq = irq;
		this.totalUtilization = totalUtilization;
		this.idle = idle;
	}


	/**
	 * @param deserializer
	 *            The deserializer to use
	 * @throws RecordInstantiationException 
	 *            when the record could not be deserialized
	 */
	public CPUUtilizationRecord(final IValueDeserializer deserializer) throws RecordInstantiationException {
		this.timestamp = deserializer.getLong();
		this.hostname = deserializer.getString();
		this.cpuID = deserializer.getString();
		this.user = deserializer.getDouble();
		this.system = deserializer.getDouble();
		this.wait = deserializer.getDouble();
		this.nice = deserializer.getDouble();
		this.irq = deserializer.getDouble();
		this.totalUtilization = deserializer.getDouble();
		this.idle = deserializer.getDouble();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void serialize(final IValueSerializer serializer) throws BufferOverflowException {
		serializer.putLong(this.getTimestamp());
		serializer.putString(this.getHostname());
		serializer.putString(this.getCpuID());
		serializer.putDouble(this.getUser());
		serializer.putDouble(this.getSystem());
		serializer.putDouble(this.getWait());
		serializer.putDouble(this.getNice());
		serializer.putDouble(this.getIrq());
		serializer.putDouble(this.getTotalUtilization());
		serializer.putDouble(this.getIdle());
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
		
		final CPUUtilizationRecord castedRecord = (CPUUtilizationRecord) obj;
		if (this.getLoggingTimestamp() != castedRecord.getLoggingTimestamp()) {
			return false;
		}
		if (this.getTimestamp() != castedRecord.getTimestamp()) {
			return false;
		}
		if (!this.getHostname().equals(castedRecord.getHostname())) {
			return false;
		}
		if (!this.getCpuID().equals(castedRecord.getCpuID())) {
			return false;
		}
		if (isNotEqual(this.getUser(), castedRecord.getUser())) {
			return false;
		}
		if (isNotEqual(this.getSystem(), castedRecord.getSystem())) {
			return false;
		}
		if (isNotEqual(this.getWait(), castedRecord.getWait())) {
			return false;
		}
		if (isNotEqual(this.getNice(), castedRecord.getNice())) {
			return false;
		}
		if (isNotEqual(this.getIrq(), castedRecord.getIrq())) {
			return false;
		}
		if (isNotEqual(this.getTotalUtilization(), castedRecord.getTotalUtilization())) {
			return false;
		}
		if (isNotEqual(this.getIdle(), castedRecord.getIdle())) {
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
		code += this.getHostname().hashCode();
		code += this.getCpuID().hashCode();
		code += ((int)this.getUser());
		code += ((int)this.getSystem());
		code += ((int)this.getWait());
		code += ((int)this.getNice());
		code += ((int)this.getIrq());
		code += ((int)this.getTotalUtilization());
		code += ((int)this.getIdle());
		
		return code;
	}
	
	public final long getTimestamp() {
		return this.timestamp;
	}
	
	
	public final String getHostname() {
		return this.hostname;
	}
	
	
	public final String getCpuID() {
		return this.cpuID;
	}
	
	
	public final double getUser() {
		return this.user;
	}
	
	
	public final double getSystem() {
		return this.system;
	}
	
	
	public final double getWait() {
		return this.wait;
	}
	
	
	public final double getNice() {
		return this.nice;
	}
	
	
	public final double getIrq() {
		return this.irq;
	}
	
	
	public final double getTotalUtilization() {
		return this.totalUtilization;
	}
	
	
	public final double getIdle() {
		return this.idle;
	}
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		String result = "CPUUtilizationRecord: ";
		result += "timestamp = ";
		result += this.getTimestamp() + ", ";
		
		result += "hostname = ";
		result += this.getHostname() + ", ";
		
		result += "cpuID = ";
		result += this.getCpuID() + ", ";
		
		result += "user = ";
		result += this.getUser() + ", ";
		
		result += "system = ";
		result += this.getSystem() + ", ";
		
		result += "wait = ";
		result += this.getWait() + ", ";
		
		result += "nice = ";
		result += this.getNice() + ", ";
		
		result += "irq = ";
		result += this.getIrq() + ", ";
		
		result += "totalUtilization = ";
		result += this.getTotalUtilization() + ", ";
		
		result += "idle = ";
		result += this.getIdle() + ", ";
		
		return result;
	}
}
