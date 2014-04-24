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

import java.lang.management.GarbageCollectorMXBean;
import java.lang.management.ManagementFactory;
import java.util.Collection;

import kieker.common.record.IMonitoringRecord;
import kieker.common.record.jvm.GCRecord;
import kieker.monitoring.core.controller.IMonitoringController;
import kieker.monitoring.core.sampler.ISampler;

/**
 * A sampler using the Java MXBean interface to access information about the garbage collector(s). The sampler produces a {@link GCRecord} for each garbage collector
 * each time the {@link #sample(IMonitoringController)} method is called.
 *
 * @author Nils Christian Ehmke
 *
 * @since 1.10
 */
public class GCSampler implements ISampler {

	public GCSampler() {
		// Empty default constructor
	}

	@Override
	public void sample(final IMonitoringController monitoringController) throws Exception {
		final long timestamp = monitoringController.getTimeSource().getTime();
		final String vmName = ManagementFactory.getRuntimeMXBean().getName();
		final String hostname = monitoringController.getHostname();

		final Collection<GarbageCollectorMXBean> gcBeans = ManagementFactory.getGarbageCollectorMXBeans();
		for (final GarbageCollectorMXBean gcBean : gcBeans) {
			final long collectionCount = gcBean.getCollectionCount();
			final long collectionTime = gcBean.getCollectionTime();
			final String gcName = gcBean.getName();

			final IMonitoringRecord record = new GCRecord(timestamp, hostname, vmName, gcName, collectionCount, collectionTime);
			monitoringController.newMonitoringRecord(record);
		}
	}

}
