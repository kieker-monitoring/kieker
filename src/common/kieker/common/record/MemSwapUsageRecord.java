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
public final class MemSwapUsageRecord extends AbstractMonitoringRecord implements IMonitoringRecord.Factory {
	private static final long serialVersionUID = 320361406631781156L;
	private static final Class<?>[] TYPES = {
		long.class,
		String.class,
		long.class,
		long.class,
		long.class,
		long.class,
		long.class,
		long.class,
	};
	private static final String DEFAULT_VALUE = "N/A";
	private static final long UNDEFINED_LONG = -1;

	private volatile long memUsed = MemSwapUsageRecord.UNDEFINED_LONG;
	private volatile long memFree = MemSwapUsageRecord.UNDEFINED_LONG;
	private volatile long swapTotal = MemSwapUsageRecord.UNDEFINED_LONG;
	private volatile long swapUsed = MemSwapUsageRecord.UNDEFINED_LONG;
	private volatile long swapFree = MemSwapUsageRecord.UNDEFINED_LONG;

	/**
	 * Date/time of measurement. The value should be interpreted as the number
	 * of nano-seconds elapsed since Jan 1st, 1970 UTC.
	 */
	private volatile long timestamp = -1;

	/**
	 * Name of the host, the resource belongs to.
	 */
	private volatile String hostName = MemSwapUsageRecord.DEFAULT_VALUE;

	private volatile long memTotal = MemSwapUsageRecord.UNDEFINED_LONG;

	/**
	 * 
	 */
	public MemSwapUsageRecord() {
		// nothing to do
	}

	/**
	 * Constructs a new {@link MemSwapUsageRecord} with the given values. If
	 * certain values shall remain undefined, use the constants {@link #UNDEFINED_DOUBLE} and {@link #UNDEFINED_LONG}.
	 * 
	 */
	public MemSwapUsageRecord(final long timestamp, final String hostName, final long memTotal, final long memUsed, final long memFree, final long swapTotal,
			final long swapUsed, final long swapFree) {
		this.timestamp = timestamp;
		this.hostName = hostName;
		this.memTotal = memTotal;
		this.memUsed = memUsed;
		this.memFree = memFree;
		this.swapTotal = swapTotal;
		this.swapUsed = swapUsed;
		this.swapFree = swapFree;
	}

	public MemSwapUsageRecord(final Object[] values) {
		final Object[] myValues = values.clone();
		AbstractMonitoringRecord.checkArray(myValues, MemSwapUsageRecord.TYPES);
		try {
			this.timestamp = (Long) myValues[0]; // NOCS
			this.hostName = (String) myValues[1]; // NOCS
			this.memTotal = (Long) myValues[2]; // NOCS
			this.memUsed = (Long) myValues[3]; // NOCS
			this.memFree = (Long) myValues[4]; // NOCS
			this.swapTotal = (Long) myValues[5]; // NOCS
			this.swapUsed = (Long) myValues[6]; // NOCS
			this.swapFree = (Long) myValues[7]; // NOCS
		} catch (final Exception exc) { // NOCS (IllegalCatchCheck) // NOPMD
			throw new IllegalArgumentException("Failed to init", exc);
		}
	}

	/*
	 * {@inheritdoc}
	 */
	@Override
	public Object[] toArray() {
		return new Object[] { this.timestamp, this.hostName, this.memTotal, this.memUsed, this.memFree, this.swapTotal, this.swapUsed, this.swapFree, };
	}

	@Override
	@Deprecated
	public void initFromArray(final Object[] values) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Class<?>[] getValueTypes() {
		return MemSwapUsageRecord.TYPES.clone();
	}

	/**
	 * @return the memTotal
	 */
	public final long getMemTotal() {
		return this.memTotal;
	}

	/**
	 * @param memTotal
	 *            the memTotal to set
	 */
	public final void setMemTotal(final long memTotal) {
		this.memTotal = memTotal;
	}

	/**
	 * @return the memUsed
	 */
	public final long getMemUsed() {
		return this.memUsed;
	}

	/**
	 * @param memUsed
	 *            the memUsed to set
	 */
	public final void setMemUsed(final long memUsed) {
		this.memUsed = memUsed;
	}

	/**
	 * @return the memFree
	 */
	public final long getMemFree() {
		return this.memFree;
	}

	/**
	 * @param memFree
	 *            the memFree to set
	 */
	public final void setMemFree(final long memFree) {
		this.memFree = memFree;
	}

	/**
	 * @return the swapTotal
	 */
	public final long getSwapTotal() {
		return this.swapTotal;
	}

	/**
	 * @param swapTotal
	 *            the swapTotal to set
	 */
	public final void setSwapTotal(final long swapTotal) {
		this.swapTotal = swapTotal;
	}

	/**
	 * @return the swapUsed
	 */
	public final long getSwapUsed() {
		return this.swapUsed;
	}

	/**
	 * @param swapUsed
	 *            the swapUsed to set
	 */
	public final void setSwapUsed(final long swapUsed) {
		this.swapUsed = swapUsed;
	}

	/**
	 * @return the swapFree
	 */
	public final long getSwapFree() {
		return this.swapFree;
	}

	/**
	 * @param swapFree
	 *            the swapFree to set
	 */
	public final void setSwapFree(final long swapFree) {
		this.swapFree = swapFree;
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
}
