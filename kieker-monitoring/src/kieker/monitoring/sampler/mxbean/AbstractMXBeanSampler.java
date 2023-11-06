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

package kieker.monitoring.sampler.mxbean;

import java.lang.management.ManagementFactory;

import kieker.common.record.IMonitoringRecord;
import kieker.monitoring.core.controller.IMonitoringController;
import kieker.monitoring.core.sampler.ISampler;

/**
 * This is an abstract base for all sampler using the MXBean interface to access information from the JVM.
 *
 * @author Nils Christian Ehmke
 *
 * @since 1.10
 */
public abstract class AbstractMXBeanSampler implements ISampler {

	private static final String VM_NAME = ManagementFactory.getRuntimeMXBean().getName();

	/**
	 * Empty constructor.
	 */
	public AbstractMXBeanSampler() {
		// Empty default constructor
	}

	/**
	 * Perform one measurement with potential multiple records.
	 *
	 * @param monitoringController
	 *            The monitoring controller for this probe.
	 *
	 * @throws Exception
	 *             depending on the concrete sampler different exceptions can be raised
	 */
	@Override
	public final void sample(final IMonitoringController monitoringController) throws Exception {
		if (!monitoringController.isMonitoringEnabled()) {
			return;
		}

		final long timestamp = monitoringController.getTimeSource().getTime();
		final String hostname = monitoringController.getHostname();

		final IMonitoringRecord[] records = this.createNewMonitoringRecords(timestamp, hostname, VM_NAME, monitoringController);

		for (final IMonitoringRecord record : records) {
			monitoringController.newMonitoringRecord(record);
		}
	}

	/**
	 * Abstract method used as interface to realize concrete samplers.
	 *
	 * @param timestamp
	 *            the current time.
	 * @param hostname
	 *            the hostname of the machine where this measurement is performed
	 * @param vmName
	 *            name of the vm
	 * @param monitoringCtr
	 *            monitoring controller used in the measurement
	 *
	 * @return returns an array of records containing the measurement
	 */
	protected abstract IMonitoringRecord[] createNewMonitoringRecords(final long timestamp, final String hostname, final String vmName,
			final IMonitoringController monitoringCtr);

}
