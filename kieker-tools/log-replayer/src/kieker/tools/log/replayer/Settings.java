/***************************************************************************
 * Copyright 2022 Kieker Project (http://kieker-monitoring.net)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ***************************************************************************/
package kieker.tools.log.replayer;

import java.io.File;
import java.nio.file.Path;
import java.util.List;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.converters.FileConverter;
import com.beust.jcommander.converters.PathConverter;

import kieker.tools.settings.converters.DateConverter;

/**
 * @author Reiner Jung
 * @since 1.16
 *
 */
public class Settings { // NOCS, NOPMD does not need a constructor

	@Parameter(names = { "-i",
		"--input" }, required = true, variableArity = true, description = "Input data directory.", converter = FileConverter.class)
	private List<File> dataLocation;

	@Parameter(names = { "-c",
		"--configuration" }, required = false, description = "Configuration file.", converter = PathConverter.class)
	private Path configurationPath;

	@Parameter(names = { "-n",
		"--no-delay" }, required = false, description = "Read and send events as fast as possible.")
	private boolean noDelay;

	@Parameter(names = { "-d",
		"--delay" }, required = false, description = "Delay factor. Default is 1 = realtime, 2 = twice the speed/half of the delay.")
	private Long delayFactor;

	@Parameter(names = { "-s",
		"--show-event-count" }, required = false, description = "Show count of events sent. Display count every n-th event.")
	private Long showRecordCount;

	@Parameter(names = { "-V",
		"--verbose" }, required = false, description = "Be verbose")
	private boolean verbose;

	@Parameter(names = { "-r",
		"--time-rewrite" }, required = false, description = "Set event timestamps relative to present time.")
	private boolean timeRelative; // NOPMD pmd thinks this is not used, but this is not the case.

	@Parameter(names = { "-bd", "--ignore-records-before-date" }, required = false,
			description = "Records logged before this date (UTC timezone) are ignored (disabled by default) yyyyMMdd-HHmmss",
			converter = DateConverter.class)
	private Long ignoreBeforeDate;

	@Parameter(names = { "-ad", "--ignore-records-after-date" }, required = false,
			description = "Records logged after this date (UTC timezone) are ignored (disabled by default) yyyyMMdd-HHmmss",
			converter = DateConverter.class)
	private Long ignoreAfterDate;

	public final List<File> getDataLocation() {
		return this.dataLocation;
	}

	public Path getKiekerMonitoringProperties() {
		return this.configurationPath;
	}

	public final boolean isNoDelay() {
		return this.noDelay;
	}

	public final Long getDelayFactor() {
		return this.delayFactor;
	}

	public final Long getShowRecordCount() {
		return this.showRecordCount;
	}

	public boolean isTimeRelative() {
		return this.timeRelative;
	}

	public Long getIgnoreBeforeDate() {
		return this.ignoreBeforeDate;
	}

	public Long getIgnoreAfterDate() {
		return this.ignoreAfterDate;
	}

	public boolean isVerbose() {
		return this.verbose;
	}

}
