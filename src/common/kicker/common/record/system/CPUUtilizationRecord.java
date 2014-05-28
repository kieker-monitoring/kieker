/***************************************************************************
 * Copyright 2014 Kicker Project (http://kicker-monitoring.net)
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

package kicker.common.record.system;

import java.nio.BufferOverflowException;
import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;

import kicker.common.record.AbstractMonitoringRecord;
import kicker.common.record.IMonitoringRecord;
import kicker.common.util.registry.IRegistry;

/**
 * @author Andre van Hoorn, Jan Waller
 * 
 * @since 1.3
 */
public class CPUUtilizationRecord extends AbstractMonitoringRecord implements IMonitoringRecord.Factory, IMonitoringRecord.BinaryFactory {
	public static final int SIZE = TYPE_SIZE_LONG + (2 * TYPE_SIZE_STRING) + (7 * TYPE_SIZE_LONG);
	public static final Class<?>[] TYPES = {
		long.class,
		String.class,
		String.class,
		double.class,
		double.class,
		double.class,
		double.class,
		double.class,
		double.class,
		double.class,
	};

	private static final long serialVersionUID = -6855133162344169157L;

	private static final String DEFAULT_VALUE = "N/A";

	/**
	 * Date/time of measurement. The value should be interpreted as the number of nano-seconds elapsed since Jan 1st, 1970 UTC.
	 */
	private final long timestamp;

	/**
	 * Name of the host, the resource belongs to.
	 */
	private final String hostname;

	/**
	 * Identifier which is unique for a CPU on a given host.
	 */
	private final String cpuID;

	/**
	 * Fraction of time during which the CPU was used for user-space processes.
	 * The value should be in the range <code>[0,1]</code>
	 */
	private final double user;

	/**
	 * Fraction of time during which the CPU was used by the kernel. The value
	 * should be in the range <code>[0,1]</code>
	 */
	private final double system;

	/**
	 * Fraction of CPU wait time. The value should be in the range <code>[0,1]</code>
	 */
	private final double wait;

	/**
	 * Fraction of time during which the CPU was used by user space processes
	 * with a high nice value. The value should be in the range <code>[0,1]</code>
	 */
	private final double nice;

	/**
	 * Fraction of time during which the CPU was used by user space processes
	 * with a high nice value. The value should be in the range <code>[0,1]</code>
	 */
	private final double irq;

	/**
	 * Fraction of time during which the CPU was utilized. Typically, this is
	 * the sum of {@link #user}, {@link #system}, {@link #wait}, and {@link #nice}. The value should be in the range <code>[0,1]</code>
	 */
	private final double totalUtilization;

	/**
	 * Fraction of time during which the CPU was idle. The value should be in
	 * the range <code>[0,1]</code>
	 */
	private final double idle;

	/**
	 * Constructs a new {@link CPUUtilizationRecord} with the given values. If
	 * certain {@link String} values shall remain undefined, use the constant {@link #DEFAULT_VALUE}.
	 * 
	 * @param timestamp
	 *            The timestamp for the record.
	 * @param hostname
	 *            The name of the host.
	 * @param cpuID
	 *            The ID of the CPU.
	 * @param user
	 *            The fraction of time during which the CPU was used for user-space processes.
	 * @param system
	 *            The fraction of time during which the CPU was used by the kernel.
	 * @param wait
	 *            The fraction of CPU wait time.
	 * @param nice
	 *            The fraction of time during which the CPU was used by user space processes
	 *            with a high nice value.
	 * @param irq
	 *            The fraction of time during which the CPU was used by user space processes
	 *            with a high nice value.
	 * @param totalUtilization
	 *            The fraction of time during which the CPU was utilized.
	 * @param idle
	 *            The fraction of time during which the CPU was idle
	 */
	public CPUUtilizationRecord(final long timestamp, final String hostname, final String cpuID, final double user, final double system, final double wait,
			final double nice, final double irq, final double totalUtilization, final double idle) {
		this.timestamp = timestamp;
		this.hostname = (hostname == null) ? DEFAULT_VALUE : hostname; // NOCS
		this.cpuID = (cpuID == null) ? DEFAULT_VALUE : cpuID; // NOCS
		this.user = user;
		this.system = system;
		this.wait = wait;
		this.nice = nice;
		this.irq = irq;
		this.totalUtilization = totalUtilization;
		this.idle = idle;
	}

	/**
	 * This constructor converts the given array into a record. It is recommended to use the array which is the result of a call to {@link #toArray()}.
	 * 
	 * @param values
	 *            The values for the record.
	 */
	public CPUUtilizationRecord(final Object[] values) { // NOPMD (direct store of values)
		AbstractMonitoringRecord.checkArray(values, TYPES);
		this.timestamp = (Long) values[0];
		this.hostname = (String) values[1];
		this.cpuID = (String) values[2];
		this.user = (Double) values[3];
		this.system = (Double) values[4];
		this.wait = (Double) values[5];
		this.nice = (Double) values[6];
		this.irq = (Double) values[7];
		this.totalUtilization = (Double) values[8];
		this.idle = (Double) values[9];
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
	public CPUUtilizationRecord(final ByteBuffer buffer, final IRegistry<String> stringRegistry) throws BufferUnderflowException {
		this.timestamp = buffer.getLong();
		this.hostname = stringRegistry.get(buffer.getInt());
		this.cpuID = stringRegistry.get(buffer.getInt());
		this.user = buffer.getDouble();
		this.system = buffer.getDouble();
		this.wait = buffer.getDouble();
		this.nice = buffer.getDouble();
		this.irq = buffer.getDouble();
		this.totalUtilization = buffer.getDouble();
		this.idle = buffer.getDouble();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Object[] toArray() {
		return new Object[] { this.getTimestamp(), this.getHostname(), this.getCpuID(), this.getUser(), this.getSystem(), this.getWait(), this.getNice(),
			this.getIrq(), this.getTotalUtilization(), this.getIdle(), };
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void writeBytes(final ByteBuffer buffer, final IRegistry<String> stringRegistry) throws BufferOverflowException {
		buffer.putLong(this.getTimestamp());
		buffer.putInt(stringRegistry.get(this.getHostname()));
		buffer.putInt(stringRegistry.get(this.getCpuID()));
		buffer.putDouble(this.getUser());
		buffer.putDouble(this.getSystem());
		buffer.putDouble(this.getWait());
		buffer.putDouble(this.getNice());
		buffer.putDouble(this.getIrq());
		buffer.putDouble(this.getTotalUtilization());
		buffer.putDouble(this.getIdle());
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @deprecated This record uses the {@link kicker.common.record.IMonitoringRecord.Factory} mechanism. Hence, this method is not implemented.
	 */
	@Override
	@Deprecated
	public final void initFromArray(final Object[] values) {
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc}
	 * 
	 * @deprecated This record uses the {@link kicker.common.record.IMonitoringRecord.BinaryFactory} mechanism. Hence, this method is not implemented.
	 */
	@Override
	@Deprecated
	public final void initFromBytes(final ByteBuffer buffer, final IRegistry<String> stringRegistry) throws BufferUnderflowException {
		throw new UnsupportedOperationException();
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
}
