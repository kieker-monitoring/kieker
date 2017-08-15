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
package kieker.monitoring.sampler.mxbean;

import java.lang.management.CompilationMXBean;
import java.lang.management.ManagementFactory;

import kieker.common.record.IMonitoringRecord;
import kieker.common.record.jvm.CompilationRecord;
import kieker.monitoring.core.controller.IMonitoringController;
import kieker.monitoring.core.signaturePattern.SignatureFactory;

/**
 * A sampler using the MXBean interface to access information about the compilation time. The sampler produces a {@link CompilationRecord} each time the
 * {@code sample} method is called.
 * 
 * @author Nils Christian Ehmke
 * 
 * @since 1.10
 */
public class CompilationSampler extends AbstractMXBeanSampler {

	/**
	 * Create a new CompilationSampler.
	 */
	public CompilationSampler() {
		// Empty default constructor
	}

	@Override
	protected IMonitoringRecord[] createNewMonitoringRecords(final long timestamp, final String hostname, final String vmName,
			final IMonitoringController monitoringCtr) {

		if (!monitoringCtr.isProbeActivated(SignatureFactory.createJVMCompilationSignature())) {
			return new IMonitoringRecord[] {};
		}

		final CompilationMXBean compilationBean = ManagementFactory.getCompilationMXBean();

		return new IMonitoringRecord[] { new CompilationRecord(timestamp, hostname, vmName, compilationBean.getName(), compilationBean.getTotalCompilationTime()), };
	}
}
