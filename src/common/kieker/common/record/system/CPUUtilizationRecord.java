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

package kieker.common.record.system;

import kieker.common.record.AbstractMonitoringRecord;
import kieker.common.record.IMonitoringRecord;
import kieker.common.util.Bits;

/**
 * @author Andre van Hoorn, Jan Waller
 * 
 * @since 1.3
 */
public class CPUUtilizationRecord extends AbstractMonitoringRecord implements IMonitoringRecord.Factory {

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

	private static final long serialVersionUID = 229860008090066333L;

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
	 * {@inheritDoc}
	 */
	public Object[] toArray() {
		return new Object[] { this.getTimestamp(), this.getHostname(), this.getCpuID(), this.getUser(), this.getSystem(), this.getWait(), this.getNice(),
			this.getIrq(), this.getTotalUtilization(), this.getIdle(), };
	}

	/**
	 * {@inheritDoc}
	 */
	public byte[] toByteArray() {
		final byte[] arr = new byte[8 + 8 + 8 + 8 + 8 + 8 + 8 + 8 + 8 + 8];
		Bits.putLong(arr, 0, this.getTimestamp());
		Bits.putString(arr, 8, this.getHostname());
		Bits.putString(arr, 8 + 8, this.getCpuID());
		Bits.putDouble(arr, 8 + 8 + 8, this.getUser());
		Bits.putDouble(arr, 8 + 8 + 8 + 8, this.getSystem());
		Bits.putDouble(arr, 8 + 8 + 8 + 8 + 8, this.getWait());
		Bits.putDouble(arr, 8 + 8 + 8 + 8 + 8 + 8, this.getNice());
		Bits.putDouble(arr, 8 + 8 + 8 + 8 + 8 + 8 + 8, this.getIrq());
		Bits.putDouble(arr, 8 + 8 + 8 + 8 + 8 + 8 + 8 + 8, this.getTotalUtilization());
		Bits.putDouble(arr, 8 + 8 + 8 + 8 + 8 + 8 + 8 + 8 + 8, this.getIdle());
		return arr;
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
	 * @deprecated This record uses the {@link kieker.common.record.IMonitoringRecord.Factory} mechanism. Hence, this method is not implemented.
	 */
	@Deprecated
	public final void initFromByteArray(final byte[] values) {
		throw new UnsupportedOperationException();
	}

	/**
	 * {@inheritDoc}
	 */
	public Class<?>[] getValueTypes() {
		return TYPES; // NOPMD
	}

	/**
	 * @return the timestamp
	 */
	public final long getTimestamp() {
		return this.timestamp;
	}

	/**
	 * @return the hostname
	 */
	public final String getHostname() {
		return this.hostname;
	}

	/**
	 * @return the cpuID
	 */
	public final String getCpuID() {
		return this.cpuID;
	}

	/**
	 * @return the user
	 */
	public final double getUser() {
		return this.user;
	}

	/**
	 * @return the system
	 */
	public final double getSystem() {
		return this.system;
	}

	/**
	 * @return the wait
	 */
	public final double getWait() {
		return this.wait;
	}

	/**
	 * @return the nice
	 */
	public final double getNice() {
		return this.nice;
	}

	/**
	 * @return the irq
	 */
	public final double getIrq() {
		return this.irq;
	}

	/**
	 * @return the totalUtilization
	 */
	public final double getTotalUtilization() {
		return this.totalUtilization;
	}

	/**
	 * @return the idle
	 */
	public final double getIdle() {
		return this.idle;
	}
}
