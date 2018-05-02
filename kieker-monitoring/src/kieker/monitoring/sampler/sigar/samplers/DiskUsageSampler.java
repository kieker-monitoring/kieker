/***************************************************************************
 * Copyright 2018 Kieker Project (http://kieker-monitoring.net)
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

package kieker.monitoring.sampler.sigar.samplers;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

import org.hyperic.sigar.DiskUsage;
import org.hyperic.sigar.FileSystem;
import org.hyperic.sigar.SigarException;
import org.hyperic.sigar.SigarProxy;

import kieker.common.record.system.DiskUsageRecord;
import kieker.monitoring.core.controller.IMonitoringController;
import kieker.monitoring.core.signaturePattern.SignatureFactory;
import kieker.monitoring.timer.ITimeSource;

/**
 * Logs disk usage of the system, retrieved as {@link DiskUsageRecord} via
 * {@link kieker.monitoring.core.controller.IMonitoringController#newMonitoringRecord(kieker.common.record.IMonitoringRecord)} .
 *
 * @author Teerat Pitakrat
 *
 * @since 1.12
 */
public final class DiskUsageSampler extends AbstractSigarSampler {

	private final ConcurrentHashMap<String, DiskUsageStatistic> diskUsageStatisticMap = new ConcurrentHashMap<String, DiskUsageSampler.DiskUsageStatistic>();

	/**
	 * Constructs a new {@link AbstractSigarSampler} with given {@link SigarProxy} instance used to retrieve the sensor data. Users
	 * should use the factory method {@link kieker.monitoring.sampler.sigar.SigarSamplerFactory#createSensorDiskUsage()} to acquire an
	 * instance rather than calling this constructor directly.
	 *
	 * @param sigar
	 *            The sigar proxy which will be used to retrieve the data.
	 */
	public DiskUsageSampler(final SigarProxy sigar) {
		super(sigar);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void sample(final IMonitoringController monitoringController) throws SigarException {
		if (!monitoringController.isMonitoringEnabled()) {
			return;
		}
		if (!monitoringController.isProbeActivated(SignatureFactory.createDiskUsageSignature())) {
			return;
		}

		for (final FileSystem fileSystem : this.sigar.getFileSystemList()) {
			if (fileSystem.getType() == FileSystem.TYPE_LOCAL_DISK) {
				final String deviceName = fileSystem.getDevName();
				final ITimeSource timesource = monitoringController.getTimeSource();
				final TimeUnit timeUnit = timesource.getTimeUnit();
				final DiskUsageStatistic currentDiskUsageStatistic = this.getCurrentDiskUsageStatistic(timesource, deviceName);

				if (!this.diskUsageStatisticMap.containsKey(deviceName)) {
					this.diskUsageStatisticMap.putIfAbsent(deviceName, currentDiskUsageStatistic);
				} else {
					final DiskUsageStatistic lastObservedDiskUsageStatistic = this.diskUsageStatisticMap.get(deviceName);
					final long timeDifference = currentDiskUsageStatistic.getTimestamp() - lastObservedDiskUsageStatistic.getTimestamp();
					if (timeDifference <= 0) {
						throw new IllegalStateException("Timestamp of new observation should be strictly larger than the previous one.");
					}
					final double currentQueue = currentDiskUsageStatistic.getQueue();
					final long readBytesDifference = currentDiskUsageStatistic.getReadBytes() - lastObservedDiskUsageStatistic.getReadBytes();
					final long readsDifference = currentDiskUsageStatistic.getReads() - lastObservedDiskUsageStatistic.getReads();
					final double currentServiceTime = currentDiskUsageStatistic.getServiceTime();
					final long writeBytesDifference = currentDiskUsageStatistic.getWriteBytes() - lastObservedDiskUsageStatistic.getWriteBytes();
					final long writesDifference = currentDiskUsageStatistic.getWrites() - lastObservedDiskUsageStatistic.getWrites();

					final double readBytesPerSecond = readBytesDifference / (double) TimeUnit.SECONDS.convert(timeDifference, timeUnit);
					final double readsPerSecond = readsDifference / (double) TimeUnit.SECONDS.convert(timeDifference, timeUnit);
					final double writeBytesPerSecond = writeBytesDifference / (double) TimeUnit.SECONDS.convert(timeDifference, timeUnit);
					final double writesPerSecond = writesDifference / (double) TimeUnit.SECONDS.convert(timeDifference, timeUnit);

					final DiskUsageRecord diskUsageRecord = new DiskUsageRecord(currentDiskUsageStatistic.getTimestamp(), monitoringController.getHostname(),
							deviceName, currentQueue, readBytesPerSecond, readsPerSecond, currentServiceTime, writeBytesPerSecond, writesPerSecond);
					monitoringController.newMonitoringRecord(diskUsageRecord);

					this.diskUsageStatisticMap.put(deviceName, currentDiskUsageStatistic);
				}
			}
		}
	}

	private DiskUsageStatistic getCurrentDiskUsageStatistic(final ITimeSource timesource, final String deviceName) throws SigarException {
		final DiskUsage diskUsage = this.sigar.getDiskUsage(deviceName);
		final long currentTimestamp = timesource.getTime();

		final double queue = diskUsage.getQueue();
		final long readBytes = diskUsage.getReadBytes();
		final long reads = diskUsage.getReads();
		final double serviceTime = diskUsage.getServiceTime();
		final long writeBytes = diskUsage.getWriteBytes();
		final long writes = diskUsage.getWrites();

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

		public DiskUsageStatistic(final long timestamp, final double queue, final long readBytes, final long reads, final double serviceTime, final long writeBytes,
				final long writes) {
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
