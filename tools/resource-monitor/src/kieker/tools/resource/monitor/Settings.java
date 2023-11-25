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
package kieker.tools.resource.monitor;

import java.nio.file.Path;
import java.util.concurrent.TimeUnit;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.converters.PathConverter;

/**
 * @author Reiner Jung
 * @since 2.0.0
 *
 */
public class Settings {

	@Parameter(names = "--interval", required = false, description = "Sampling interval")
	private volatile long interval = 15;

	@Parameter(names = { "-u", "--interval-unit" }, required = false, description = "Sampling interval time unit (default: SECONDS)")
	private volatile TimeUnit intervalUnit = TimeUnit.SECONDS;

	@Parameter(names = "--initial-delay", required = false, description = "Initial delay")
	private volatile long initialDelay;

	@Parameter(names = "--initial-delay-unit", required = false, description = "Initial delay time unit (default: SECONDS)")
	private volatile TimeUnit initialDelayUnit = TimeUnit.SECONDS;

	@Parameter(names = { "-D", "--duration" }, required = false, description = "Monitoring duration")
	private volatile long duration = -1;

	@Parameter(names = "--duration-unit", required = false, description = "Monitoring duration time unit (default: MINUTES)")
	private volatile TimeUnit durationUnit = TimeUnit.MINUTES;

	@Parameter(names = { "-c", "--monitoring-configuration" }, required = false, converter = PathConverter.class,
			description = "Configuration to use for the Kieker monitoring instance")
	private volatile Path monitoringConfiguration;

	public long getInterval() {
		return this.interval;
	}

	public TimeUnit getIntervalUnit() {
		return this.intervalUnit;
	}

	public long getInitialDelay() {
		return this.initialDelay;
	}

	public TimeUnit getInitialDelayUnit() {
		return this.initialDelayUnit;
	}

	public long getDuration() {
		return this.duration;
	}

	public TimeUnit getDurationUnit() {
		return this.durationUnit;
	}

	public Path getMonitoringConfiguration() {
		return this.monitoringConfiguration;
	}
}
