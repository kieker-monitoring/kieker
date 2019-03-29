/***************************************************************************
 * Copyright 2017 Kieker Project (http://kieker-monitoring.net)
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

package kieker.monitoring.sampler.oshi.samplers;

import oshi.hardware.CentralProcessor.TickType;

/**
 * Converts and stores CPU percentages (of i.e., User + Sys + Nice + Wait) The
 * conversion is made by collecting the ticks at two different times,
 * calculating the deltas and by calculating the relative values. The last
 * calculated relative values (percentages) are stored in lastConversion.
 * Specific metrics can be accessed by the given getters.
 *
 * @author Matteo Sassano
 * @since 1.14
 *
 */
public class CPUsDetailedPercConverter {

	private final int coreIndex;

	/**
	 * Tick list at t0. Ticks are stored in the OSHI format. (TickType...)
	 */
	private long[] processorLoadTicksT0;

	/**
	 * Tick list at t1 Ticks are stored in the OSHI format. (TickType...)
	 */
	private long[] processorLoadTicksT1;

	/**
	 * Timestamp of the first (second last) measurement.
	 */
	private long t0;

	/**
	 * Timestamp of the second (last) measurement.
	 */
	private long t1;

	/**
	 * Holds the last CPU core percentages.
	 */
	private double[] lastConversion;

	/**
	 * CPUsDetailedPercConverter constructor.
	 *
	 * @param coreIndex
	 *            holds the CPU core index
	 */
	public CPUsDetailedPercConverter(final int coreIndex) {
		this.coreIndex = coreIndex;
	}

	/**
	 * Stores a new measurement spot for a given CPU core. The new measurement spot
	 * must be measured at least one second after the last one. Otherwise, the
	 * invocation of this method will be ignored. In the normal case, passing new
	 * measurements leads to a shift of the last ones to processorLoadTicksT0
	 * measurement and the new ones are stored in processorLoadTicksT1
	 *
	 * @param processorLoadTicks
	 *            the new measurements at the current time
	 */
	public void passNewProcessorLoadTicks(final long[] processorLoadTicks) {
		if (processorLoadTicks == null) {
			return;
		}
		if (processorLoadTicks.length == 0) {
			return;
		}
		if ((this.t0 == 0) || (this.processorLoadTicksT0 == null)) {
			this.t0 = System.currentTimeMillis();
			this.processorLoadTicksT0 = processorLoadTicks.clone();
			return;
		}
		final long currentTime = System.currentTimeMillis();
		if ((currentTime - this.t0) < 1000) {
			return;
		}
		this.t0 = this.t1;
		this.t1 = currentTime;
		this.processorLoadTicksT0 = this.processorLoadTicksT1;
		this.processorLoadTicksT1 = processorLoadTicks.clone();
	}

	/**
	 * Calculates the percentages of system, nice, user,... by utilizing the
	 * difference of two measurement spots. (processorLoadTicksT0 and
	 * processorLoadTicksT1). The second measurement spot must be measured at least
	 * one second later than the first one.
	 *
	 * If the second measurement spot is not available yet, the average percentages
	 * starting from the boot start will be calculated.
	 *
	 * If neither the first nor the second measurement spot is available, nothing
	 * will be calculated.
	 *
	 * The results are stored in lastConvesion
	 */
	public void convertToPercentage() {
		if (this.processorLoadTicksT0 == null) {
			return;
		}
		if (this.processorLoadTicksT1 == null) {
			this.initialConvertToPercentage();
		} else {
			this.convertWithT1Ticks();
		}
	}

	/**
	 * Internal function: Calculates the average percentages of system, nice,
	 * user,... from the boot start. The results are stored in lastConvesion
	 */
	private void initialConvertToPercentage() {
		this.lastConversion = new double[this.processorLoadTicksT0.length];
		long allTicks = 0;
		for (final long tick : this.processorLoadTicksT0) {
			allTicks = allTicks + tick;
		}
		for (int i = 0; i < this.lastConversion.length; i++) {
			if (allTicks != 0) {
				this.lastConversion[i] = this.processorLoadTicksT0[i] / (double) allTicks;
			}
		}
	}

	/**
	 * Internal function: Calculates the percentages of system, nice, user,... by
	 * utilizing the difference of two measurement spots. (processorLoadTicksT0 and
	 * processorLoadTicksT1). The second measurement spot must be measured at least
	 * one second later than the first one. The results are stored in lastConvesion
	 */
	private void convertWithT1Ticks() {
		this.lastConversion = new double[this.processorLoadTicksT0.length];
		final long[] deltas = new long[this.processorLoadTicksT0.length];
		long allTicks = 0;
		for (int i = 0; i < deltas.length; i++) {
			final long delta = this.processorLoadTicksT1[i] - this.processorLoadTicksT0[i];
			deltas[i] = delta;
			allTicks = allTicks + delta;
		}
		for (int i = 0; i < this.lastConversion.length; i++) {
			if (allTicks != 0) {
				this.lastConversion[i] = deltas[i] / (double) allTicks;
			}
		}
	}

	/**
	 * @return the system utilization of the given core.
	 */
	public double getSystemPerc() {
		if ((this.lastConversion == null) || (this.lastConversion.length == 0)) {
			return 0;
		}
		if (this.lastConversion.length <= TickType.SYSTEM.getIndex()) {
			return 0;
		}
		return this.lastConversion[TickType.SYSTEM.getIndex()];
	}

	/**
	 * @return the nice of the given core.
	 */
	public double getNicePerc() {
		if ((this.lastConversion == null) || (this.lastConversion.length == 0)) {
			return 0;
		}
		if (this.lastConversion.length <= TickType.NICE.getIndex()) {
			return 0;
		}
		return this.lastConversion[TickType.NICE.getIndex()];
	}

	/**
	 * @return the I/O wait of the given core.
	 */
	public double getWaitPerc() {
		if ((this.lastConversion == null) || (this.lastConversion.length == 0)) {
			return 0;
		}
		if (this.lastConversion.length <= TickType.IOWAIT.getIndex()) {
			return 0;
		}
		return this.lastConversion[TickType.IOWAIT.getIndex()];
	}

	/**
	 * @return the user utilization of the given core.
	 */
	public double getUserPerc() {
		if ((this.lastConversion == null) || (this.lastConversion.length == 0)) {
			return 0;
		}
		if (this.lastConversion.length <= TickType.USER.getIndex()) {
			return 0;
		}
		return this.lastConversion[TickType.USER.getIndex()];
	}

	/**
	 * @return the IRQ of the given core.
	 */
	public double getIrqPerc() {
		if ((this.lastConversion == null) || (this.lastConversion.length == 0)) {
			return 0;
		}
		if (this.lastConversion.length <= TickType.IRQ.getIndex()) {
			return 0;
		}
		if (this.lastConversion.length <= TickType.SOFTIRQ.getIndex()) {
			return 0;
		}
		return this.lastConversion[TickType.IRQ.getIndex()] + this.lastConversion[TickType.SOFTIRQ.getIndex()];
	}

	/**
	 * @return the IDLE of the given core.
	 */
	public double getIdlePerc() {
		if ((this.lastConversion == null) || (this.lastConversion.length == 0)) {
			return 0;
		}
		if (this.lastConversion.length <= TickType.IDLE.getIndex()) {
			return 0;
		}
		return this.lastConversion[TickType.IDLE.getIndex()];
	}

	/**
	 * @return the combined utilization of the given core.
	 */
	public double getCombinedPerc() {
		if ((this.lastConversion == null) || (this.lastConversion.length == 0)) {
			return 0;
		}
		double combinedUsage = 0;
		for (int i = 0; i < this.lastConversion.length; i++) {
			if (i != TickType.IDLE.getIndex()) {
				combinedUsage = combinedUsage + this.lastConversion[i];
			}
		}
		return combinedUsage;
	}

	/**
	 * @return the core index related to the converter.
	 */
	public int getCoreIndex() {
		return this.coreIndex;
	}
}
