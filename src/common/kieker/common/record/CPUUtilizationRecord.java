/***************************************************************************
 * Copyright 2011 by
 *  + Christian-Albrechts-University of Kiel
 *    + Department of Computer Science
 *      + Software Engineering Group 
 *  and others.
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

/**
 * 
 */
package kieker.common.record;


/**
 * @author Andre van Hoorn
 * 
 */
public class CPUUtilizationRecord extends AbstractMonitoringRecord {

	private static final String DEFAULT_VALUE = "N/A";

	public static final double UNDEFINED_DOUBLE = -1;

	/**
	 * Date/time of measurement. The value should be interpreted as the number
	 * of nano-seconds elapsed since Jan 1st, 1970 UTC.
	 */
	private volatile long timestamp = -1;

	/**
	 * Name of the host, the resource belongs to.
	 */
	private volatile String hostName = CPUUtilizationRecord.DEFAULT_VALUE;

	/**
	 * Identifier which is unique for a CPU on a given host.
	 */
	private volatile String cpuID = CPUUtilizationRecord.DEFAULT_VALUE;

	/**
	 * Fraction of time during which the CPU was used for user-space processes.
	 * The value should be in the range <code>[0,1]</code>
	 */
	private volatile double user = CPUUtilizationRecord.UNDEFINED_DOUBLE;

	/**
	 * Fraction of time during which the CPU was used by the kernel. The value
	 * should be in the range <code>[0,1]</code>
	 */
	private volatile double system = CPUUtilizationRecord.UNDEFINED_DOUBLE;

	/**
	 * Fraction of CPU wait time. The value should be in the range
	 * <code>[0,1]</code>
	 */
	private volatile double wait = CPUUtilizationRecord.UNDEFINED_DOUBLE;

	/**
	 * Fraction of time during which the CPU was used by user space processes
	 * with a high nice value. The value should be in the range
	 * <code>[0,1]</code>
	 */
	private volatile double nice = CPUUtilizationRecord.UNDEFINED_DOUBLE;

	/**
	 * Fraction of time during which the CPU was used by user space processes
	 * with a high nice value. The value should be in the range
	 * <code>[0,1]</code>
	 */
	private volatile double irq = CPUUtilizationRecord.UNDEFINED_DOUBLE;

	/**
	 * Fraction of time during which the CPU was utilized. Typically, this is
	 * the sum of {@link #user}, {@link #system}, {@link #wait}, and
	 * {@link #nice}. The value should be in the range <code>[0,1]</code>
	 */
	private volatile double totalUtilization =
			CPUUtilizationRecord.UNDEFINED_DOUBLE;

	/**
	 * Fraction of time during which the CPU was idle. The value should be in
	 * the range <code>[0,1]</code>
	 */
	private volatile double idle = CPUUtilizationRecord.UNDEFINED_DOUBLE;

	/**
	 * 
	 */
	private static final long serialVersionUID = 17677676L;

	/*
	 * {@inheritdoc}
	 */
	@Override
	public void initFromArray(final Object[] values)
			throws IllegalArgumentException {
		try {
			if (values.length != CPUUtilizationRecord.VALUE_TYPES.length) {
				throw new IllegalArgumentException("Expecting vector with "
						+ CPUUtilizationRecord.VALUE_TYPES.length
						+ " elements but found:" + values.length);
			}

			this.timestamp = (Long) values[0];
			this.hostName = (String) values[1];
			this.cpuID = (String) values[2];
			this.user = (Double) values[3];
			this.system = (Double) values[4];
			this.wait = (Double) values[5];
			this.nice = (Double) values[6];
			this.irq = (Double) values[7];
			this.totalUtilization = (Double) values[8];
			this.idle = (Double) values[9];

		} catch (final Exception exc) {
			throw new IllegalArgumentException("Failed to init", exc);
		}
		return;
	}

	/**
	 * 
	 */
	public CPUUtilizationRecord() {
	}

	/**
	 * Constructs a new {@link CPUUtilizationRecord} with the given values. If
	 * certain values shall remain undefined, use the constant
	 * {@link #UNDEFINED_DOUBLE}.
	 * 
	 * @param timestamp
	 * @param hostName
	 * @param cpuID
	 * @param user
	 * @param system
	 * @param wait
	 * @param nice
	 * @param irq
	 * @param totalUtilization
	 * @param idle
	 */
	public CPUUtilizationRecord(final long timestamp, final String hostName,
			final String cpuID, final double user, final double system,
			final double wait, final double nice, final double irq,
			final double totalUtilization, final double idle) {
		this.timestamp = timestamp;
		this.hostName = hostName;
		this.cpuID = cpuID;
		this.user = user;
		this.system = system;
		this.wait = wait;
		this.nice = nice;
		this.irq = irq;
		this.totalUtilization = totalUtilization;
		this.idle = idle;
	}

	/*
	 * {@inheritdoc}
	 */
	@Override
	public Object[] toArray() {
		return new Object[] { this.timestamp, this.hostName, this.cpuID, this.user,
				this.system, this.wait, this.nice, this.irq, this.totalUtilization, this.idle };
	}

	private final static Class<?>[] VALUE_TYPES = { long.class, String.class,
			String.class, double.class, double.class, double.class,
			double.class, double.class, double.class, double.class };

	/*
	 * {@inheritdoc}
	 */
	@Override
	public Class<?>[] getValueTypes() {
		return CPUUtilizationRecord.VALUE_TYPES;
	}

	/**
	 * @return the timestamp
	 */
	public final long getTimestamp() {
		return this.timestamp;
	}

	/**
	 * @param timestamp
	 *            the timestamp to set
	 */
	public final void setTimestamp(final long timestamp) {
		this.timestamp = timestamp;
	}

	/**
	 * @return the hostName
	 */
	public final String getHostName() {
		return this.hostName;
	}

	/**
	 * @param hostName
	 *            the hostName to set
	 */
	public final void setHostName(final String hostName) {
		this.hostName = hostName;
	}

	/**
	 * @return the cpuID
	 */
	public final String getCpuID() {
		return this.cpuID;
	}

	/**
	 * @param cpuID
	 *            the cpuID to set
	 */
	public final void setCpuID(final String cpuID) {
		this.cpuID = cpuID;
	}

	/**
	 * @return the user
	 */
	public final double getUser() {
		return this.user;
	}

	/**
	 * @param user
	 *            the user to set
	 */
	public final void setUser(final double user) {
		this.user = user;
	}

	/**
	 * @return the system
	 */
	public final double getSystem() {
		return this.system;
	}

	/**
	 * @param system
	 *            the system to set
	 */
	public final void setSystem(final double system) {
		this.system = system;
	}

	/**
	 * @return the wait
	 */
	public final double getWait() {
		return this.wait;
	}

	/**
	 * @param wait
	 *            the wait to set
	 */
	public final void setWait(final double wait) {
		this.wait = wait;
	}

	/**
	 * @return the nice
	 */
	public final double getNice() {
		return this.nice;
	}

	/**
	 * @param nice
	 *            the nice to set
	 */
	public final void setNice(final double nice) {
		this.nice = nice;
	}

	/**
	 * @return the irq
	 */
	public final double getIrq() {
		return this.irq;
	}

	/**
	 * @param irq
	 *            the irq to set
	 */
	public final void setIrq(final double irq) {
		this.irq = irq;
	}

	/**
	 * @return the totalUtilization
	 */
	public final double getTotalUtilization() {
		return this.totalUtilization;
	}

	/**
	 * @param totalUtilization
	 *            the totalUtilization to set
	 */
	public final void setTotalUtilization(final double totalUtilization) {
		this.totalUtilization = totalUtilization;
	}

	/**
	 * @return the idle
	 */
	public final double getIdle() {
		return this.idle;
	}

	/**
	 * @param idle
	 *            the idle to set
	 */
	public final void setIdle(final double idle) {
		this.idle = idle;
	}
}
