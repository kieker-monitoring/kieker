/***************************************************************************
 * Copyright 2015 Kieker Project (http://kieker-monitoring.net)
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

package kieker.monitoring.core.controller;

import java.util.List;
import java.util.concurrent.TimeUnit;

import kieker.common.record.IMonitoringRecord;
import kieker.monitoring.core.sampler.ISampler;
import kieker.monitoring.core.sampler.ScheduledSamplerJob;
import kieker.monitoring.timer.ITimeSource;

/**
 * Enables to wait for the monitoring controller to terminate.
 *
 * <br>
 * Used and thus included in tests only.
 *
 * @author Christian Wulf
 *
 * @since 1.13
 */
public class WaitableController implements IMonitoringController {

	/** the wrapped monitoring controller instance */
	private final MonitoringController controller;

	/**
	 * @see WaitableController
	 */
	public WaitableController(final MonitoringController controller) {
		super();
		this.controller = controller;
	}

	/**
	 * Waits for the termination of the monitoring controller.
	 *
	 * @param timeoutInMs
	 *            timeout in milliseconds to wait (a timeout of 0 means to wait forever)
	 *
	 * @throws InterruptedException
	 *             if the calling thread was interrupted while waiting for the termination
	 *
	 * @since 2.0
	 * @author Christian Wulf
	 */
	public void waitForTermination(final long timeoutInMs) throws InterruptedException { // raises the visibility for tests
		this.controller.waitForTermination(timeoutInMs);
	}

	@Override
	public final String toString() {
		return this.controller.toString();
	}

	@Override
	public final boolean sendMetadataAsRecord() {
		return this.controller.sendMetadataAsRecord();
	}

	@Override
	public final boolean terminateMonitoring() {
		return this.controller.terminateMonitoring();
	}

	@Override
	public final boolean isMonitoringTerminated() {
		return this.controller.isMonitoringTerminated();
	}

	@Override
	public final boolean enableMonitoring() {
		return this.controller.enableMonitoring();
	}

	@Override
	public final boolean disableMonitoring() {
		return this.controller.disableMonitoring();
	}

	@Override
	public final boolean isMonitoringEnabled() {
		return this.controller.isMonitoringEnabled();
	}

	@Override
	public final boolean isDebug() {
		return this.controller.isDebug();
	}

	@Override
	public final String getName() {
		return this.controller.getName();
	}

	@Override
	public final String getHostname() {
		return this.controller.getHostname();
	}

	@Override
	public final int incExperimentId() {
		return this.controller.incExperimentId();
	}

	@Override
	public final void setExperimentId(final int newExperimentID) {
		this.controller.setExperimentId(newExperimentID);
	}

	@Override
	public final int getExperimentId() {
		return this.controller.getExperimentId();
	}

	@Override
	public final boolean newMonitoringRecord(final IMonitoringRecord record) {
		return this.controller.newMonitoringRecord(record);
	}

	@Override
	public final ScheduledSamplerJob schedulePeriodicSampler(final ISampler sampler, final long initialDelay, final long period, final TimeUnit timeUnit) {
		return this.controller.schedulePeriodicSampler(sampler, initialDelay, period, timeUnit);
	}

	@Override
	public final boolean removeScheduledSampler(final ScheduledSamplerJob sampler) {
		return this.controller.removeScheduledSampler(sampler);
	}

	@Override
	public final ITimeSource getTimeSource() {
		return this.controller.getTimeSource();
	}

	@Override
	public final String getJMXDomain() {
		return this.controller.getJMXDomain();
	}

	@Override
	public final boolean activateProbe(final String pattern) {
		return this.controller.activateProbe(pattern);
	}

	@Override
	public final boolean deactivateProbe(final String pattern) {
		return this.controller.deactivateProbe(pattern);
	}

	@Override
	public boolean isProbeActivated(final String signature) {
		return this.controller.isProbeActivated(signature);
	}

	@Override
	public void setProbePatternList(final List<String> patternList) {
		this.controller.setProbePatternList(patternList);
	}

	@Override
	public List<String> getProbePatternList() {
		return this.controller.getProbePatternList();
	}

}
