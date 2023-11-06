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

package kieker.monitoring.sampler.oshi.samplers;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

import kieker.common.record.system.DiskUsageRecord;
import kieker.monitoring.core.controller.IMonitoringController;
import kieker.monitoring.core.signaturePattern.SignatureFactory;
import kieker.monitoring.timer.ITimeSource;

import oshi.hardware.HWDiskStore;
import oshi.hardware.HardwareAbstractionLayer;

/**
 * Logs disk usage of the system, retrieved as {@link DiskUsageRecord} via
 * {@link kieker.monitoring.core.controller.IMonitoringController#newMonitoringRecord(kieker.common.record.IMonitoringRecord)}
 * .
 *
 * @author Matteo Sassano
 * @since 1.14
 *
 */
public final class DiskUsageSampler extends AbstractOshiSampler {

	private final ConcurrentHashMap<String, DiskUsageStatistic> diskUsageStatisticMap = new ConcurrentHashMap<>();

	/**
	 * Constructs a new {@link AbstractOshiSampler} with given
	 * {@link HardwareAbstractionLayer} instance used to retrieve the sensor data.
	 * Users should use the factory method
	 * {@link kieker.monitoring.sampler.oshi.OshiSamplerFactory#createSensorDiskUsage()}
	 * to acquire an instance rather than calling this constructor directly.
	 *
	 * @param hardwareAbstractionLayer
	 *            The {@link HardwareAbstractionLayer} which will be used to
	 *            retrieve the data.
	 */
	public DiskUsageSampler(final HardwareAbstractionLayer hardwareAbstractionLayer) {
		super(hardwareAbstractionLayer);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void sample(final IMonitoringController monitoringController) {
		if (!monitoringController.isMonitoringEnabled() || !monitoringController.isProbeActivated(SignatureFactory.createDiskUsageSignature())) {
			return;
		}
		final HWDiskStore[] hwDistStores = this.hardwareAbstractionLayer.getDiskStores();
		for (final HWDiskStore hwDistStore : hwDistStores) {
			final String deviceName = hwDistStore.getName();
			final ITimeSource timesource = monitoringController.getTimeSource();
			final TimeUnit timeUnit = timesource.getTimeUnit();
			final DiskUsageStatistic currentDiskUsageStatistic = this.getCurrentDiskUsageStatistic(timesource,
					deviceName, hwDistStore);

			if (!this.diskUsageStatisticMap.containsKey(deviceName)) {
				this.diskUsageStatisticMap.putIfAbsent(deviceName, currentDiskUsageStatistic);
			} else {
				final DiskUsageStatistic lastObservedDiskUsageStatistic = this.diskUsageStatisticMap.get(deviceName);
				final long timeDifference = currentDiskUsageStatistic.getTimestamp()
						- lastObservedDiskUsageStatistic.getTimestamp();
				if (timeDifference <= 0) {
					throw new IllegalStateException(
							"Timestamp of new observation should be strictly larger than the previous one.");
				}
				final double currentQueue = currentDiskUsageStatistic.getQueue();
				final long readBytesDifference = currentDiskUsageStatistic.getReadBytes()
						- lastObservedDiskUsageStatistic.getReadBytes();
				final long readsDifference = currentDiskUsageStatistic.getReads()
						- lastObservedDiskUsageStatistic.getReads();
				final double currentServiceTime = currentDiskUsageStatistic.getServiceTime();
				final long writeBytesDifference = currentDiskUsageStatistic.getWriteBytes()
						- lastObservedDiskUsageStatistic.getWriteBytes();
				final long writesDifference = currentDiskUsageStatistic.getWrites()
						- lastObservedDiskUsageStatistic.getWrites();

				final double readBytesPerSecond = readBytesDifference
						/ (double) TimeUnit.SECONDS.convert(timeDifference, timeUnit);
				final double readsPerSecond = readsDifference
						/ (double) TimeUnit.SECONDS.convert(timeDifference, timeUnit);
				final double writeBytesPerSecond = writeBytesDifference
						/ (double) TimeUnit.SECONDS.convert(timeDifference, timeUnit);
				final double writesPerSecond = writesDifference
						/ (double) TimeUnit.SECONDS.convert(timeDifference, timeUnit);

				final DiskUsageRecord diskUsageRecord = new DiskUsageRecord(currentDiskUsageStatistic.getTimestamp(),
						monitoringController.getHostname(), deviceName, currentQueue, readBytesPerSecond,
						readsPerSecond, currentServiceTime, writeBytesPerSecond, writesPerSecond);
				monitoringController.newMonitoringRecord(diskUsageRecord);

				this.diskUsageStatisticMap.put(deviceName, currentDiskUsageStatistic);
			}

		}
	}

	private DiskUsageStatistic getCurrentDiskUsageStatistic(final ITimeSource timesource, final String deviceName,
			final HWDiskStore hwDiskStore) {
		final long currentTimestamp = timesource.getTime();
		final double queue = hwDiskStore.getCurrentQueueLength();
		final long readBytes = hwDiskStore.getReadBytes();
		final long reads = hwDiskStore.getReads();
		final double serviceTime = hwDiskStore.getTimeStamp();
		final long writeBytes = hwDiskStore.getWriteBytes();
		final long writes = hwDiskStore.getWrites();

		return new DiskUsageStatistic(currentTimestamp, queue, readBytes, reads, serviceTime, writeBytes, writes);
	}

	/**
	 * An inner class which stores disk usage for each observation.
	 *
	 * @author Teerat Pitakrat
	 *
	 * @since 1.12
	 */
	static class DiskUsageStatistic {
		private final long timestamp;
		private final double queue;
		private final long readBytes;
		private final long reads;
		private final double serviceTime;
		private final long writeBytes;
		private final long writes;

		public DiskUsageStatistic(final long timestamp, final double queue, final long readBytes, final long reads,
				final double serviceTime, final long writeBytes, final long writes) {
			this.timestamp = timestamp;
			this.queue = queue;
			this.readBytes = readBytes;
			this.reads = reads;
			this.serviceTime = serviceTime;
			this.writeBytes = writeBytes;
			this.writes = writes;
		}

		public long getTimestamp() {
			return this.timestamp;
		}

		public double getQueue() {
			return this.queue;
		}

		public long getReadBytes() {
			return this.readBytes;
		}

		public long getReads() {
			return this.reads;
		}

		public double getServiceTime() {
			return this.serviceTime;
		}

		public long getWriteBytes() {
			return this.writeBytes;
		}

		public long getWrites() {
			return this.writes;
		}
	}
}
