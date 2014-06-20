/***************************************************************************
 * Copyright 2014 Kieker Project (http://kieker-monitoring.net)
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
	private volatile IMonitoringController monitoringCtr;

	public AbstractMXBeanSampler() {
		// Empty default constructor
	}

	// necessary for ProbeActivation-Check in child classes
	public IMonitoringController getMonitoringCtr() {
		return this.monitoringCtr;
	}

	@Override
	public final void sample(final IMonitoringController monitoringController) throws Exception {

		this.monitoringCtr = monitoringController;

		if (!monitoringController.isMonitoringEnabled()) {
			return;
		}

		final long timestamp = monitoringController.getTimeSource().getTime();
		final String hostname = monitoringController.getHostname();

		final IMonitoringRecord[] records = this.createNewMonitoringRecords(timestamp, hostname, VM_NAME);

		for (final IMonitoringRecord record : records) {
			monitoringController.newMonitoringRecord(record);
		}
	}

	protected abstract IMonitoringRecord[] createNewMonitoringRecords(final long timestamp, final String hostname, final String vmName);

}
