/***************************************************************************
 * Copyright 2020 Kieker Project (http://kieker-monitoring.net)
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

import java.lang.management.ClassLoadingMXBean;
import java.lang.management.ManagementFactory;

import kieker.common.record.IMonitoringRecord;
import kieker.common.record.jvm.ClassLoadingRecord;
import kieker.monitoring.core.controller.IMonitoringController;
import kieker.monitoring.core.signaturePattern.SignatureFactory;

/**
 * A sampler using the MXBean interface to access information about the class loading. The sampler produces a {@link ClassLoadingRecord} each time the {@code sample}
 * method is called.
 *
 * @author Nils Christian Ehmke
 *
 * @since 1.10
 */
public class ClassLoadingSampler extends AbstractMXBeanSampler {

	/**
	 * Create a new ClassLoadingSampler.
	 */
	public ClassLoadingSampler() {
		// Empty default constructor
	}

	@Override
	protected IMonitoringRecord[] createNewMonitoringRecords(final long timestamp, final String hostname, final String vmName,
			final IMonitoringController monitoringCtr) {
		if (!monitoringCtr.isProbeActivated(SignatureFactory.createJVMClassLoadSignature())) {
			return new IMonitoringRecord[] {};
		}

		final ClassLoadingMXBean classLoadingBean = ManagementFactory.getClassLoadingMXBean();
		return new IMonitoringRecord[] { new ClassLoadingRecord(timestamp, hostname, vmName, classLoadingBean.getTotalLoadedClassCount(),
				classLoadingBean.getLoadedClassCount(), classLoadingBean.getUnloadedClassCount()), };
	}

}
