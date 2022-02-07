/***************************************************************************
 * Copyright 2021 Kieker Project (http://kieker-monitoring.net)
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

package kieker.tools.log.replayer;

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import kieker.tools.AbstractCommandLineTool;

/**
 * Command-line tool for replaying a filesystem monitoring log using the {@link FilesystemLogReplayer}.
 *
 * @author Andre van Hoorn, Nils Christian Ehmke
 *
 * @since 0.95a
 * @deprecated since 1.16
 */
@Deprecated
@SuppressWarnings("static-access")
public final class FilesystemLogReplayerStarter extends AbstractCommandLineTool {

	private static final Logger LOGGER = LoggerFactory.getLogger(FilesystemLogReplayerStarter.class);

	private static final String CMD_OPT_NAME_MONITORING_CONFIGURATION = "monitoring.configuration";
	private static final String CMD_OPT_NAME_INPUTDIRS = "inputdirs";
	private static final String CMD_OPT_NAME_KEEPORIGINALLOGGINGTIMESTAMPS = "keep-logging-timestamps";
	private static final String CMD_OPT_NAME_REALTIME = "realtime";
	private static final String CMD_OPT_NAME_NUM_REALTIME_WORKERS = "realtime-worker-threads";
	private static final String CMD_OPT_NAME_REALTIME_ACCELERATION_FACTOR = "realtime-acceleration-factor";
	private static final String CMD_OPT_NAME_IGNORERECORDSBEFOREDATE = "ignore-records-before-date";
	private static final String CMD_OPT_NAME_IGNORERECORDSAFTERDATE = "ignore-records-after-date";
	private static final String DATE_FORMAT_PATTERN = "yyyyMMdd'-'HHmmss";
	private static final String DATE_FORMAT_PATTERN_CMD_USAGE_HELP = DATE_FORMAT_PATTERN.replaceAll("'", ""); // only for usage info
	private static final String OPTION_EXAMPLE_FILE_MONITORING_PROPERTIES = File.separator + "path" + File.separator + "to" + File.separator
			+ "monitoring.properties";

	private String monitoringConfigurationFile;
	private String[] inputDirs;
	private boolean keepOriginalLoggingTimestamps;
	private boolean realtimeMode;
	private double realtimeAccelerationFactor;
	private int numRealtimeWorkerThreads = -1;
	private long ignoreRecordsBeforeTimestamp = FilesystemLogReplayer.MIN_TIMESTAMP;
	private long ignoreRecordsAfterTimestamp = FilesystemLogReplayer.MAX_TIMESTAMP;

	private FilesystemLogReplayerStarter() {
		super(true);
	}

	public static void main(final String[] args) {
		new FilesystemLogReplayerStarter().start(args);
	}

	@Override
	protected void addAdditionalOptions(final Options options) {
		Option option;

		option = new Option("c", CMD_OPT_NAME_MONITORING_CONFIGURATION, true,
				"Configuration to use for the Kieker monitoring instance");
		option.setArgName(OPTION_EXAMPLE_FILE_MONITORING_PROPERTIES);
		option.setRequired(false);
		option.setValueSeparator('=');
		options.addOption(option);

		option = new Option("i", CMD_OPT_NAME_INPUTDIRS, true, "Log directories to read data from");
		option.setArgName("dir1 ... dirN");
		option.setRequired(false);
		option.setArgs(Option.UNLIMITED_VALUES);
		options.addOption(option);

		option = new Option("k", CMD_OPT_NAME_KEEPORIGINALLOGGINGTIMESTAMPS, true,
				"Replay the original logging timestamps (defaults to true)?");
		option.setArgName("true|false");
		option.setRequired(false);
		option.setValueSeparator('=');
		options.addOption(option);

		option = new Option("r", CMD_OPT_NAME_REALTIME, true, "Replay log data in realtime?");
		option.setArgName("true|false");
		option.setRequired(false);
		option.setValueSeparator('=');
		options.addOption(option);

		option = new Option("n", CMD_OPT_NAME_NUM_REALTIME_WORKERS, true,
				"Number of worker threads used in realtime mode (defaults to 1).");
		option.setArgName("num");
		option.setRequired(false);
		options.addOption(option);

		option = new Option("a", CMD_OPT_NAME_REALTIME_ACCELERATION_FACTOR, true,
				"Factor by which to accelerate (>1.0) or slow down (<1.0) the replay in realtime mode (defaults to 1.0, i.e., no acceleration/slow down).");
		option.setArgName("factor");
		option.setRequired(false);
		option.setValueSeparator('=');
		options.addOption(option);

		option = new Option(null, CMD_OPT_NAME_IGNORERECORDSBEFOREDATE, true,
				"Records logged before this date (UTC timezone) are ignored (disabled by default).");
		option.setArgName(DATE_FORMAT_PATTERN_CMD_USAGE_HELP);
		option.setRequired(false);
		options.addOption(option);

		option = new Option(null, CMD_OPT_NAME_IGNORERECORDSAFTERDATE, true,
				"Records logged after this date (UTC timezone) are ignored (disabled by default).");
		option.setArgName(DATE_FORMAT_PATTERN_CMD_USAGE_HELP);
		option.setRequired(false);
		options.addOption(option);
	}

	@Override
	protected boolean readPropertiesFromCommandLine(final CommandLine commandLine) {
		boolean retVal = true;

		// 0.) monitoring properties
		this.monitoringConfigurationFile = commandLine.getOptionValue(CMD_OPT_NAME_MONITORING_CONFIGURATION);

		// 1.) init inputDirs
		this.inputDirs = commandLine.getOptionValues(CMD_OPT_NAME_INPUTDIRS);
		if (this.inputDirs == null) {
			LOGGER.error("No input directory configured");
			retVal = false;
		}

		// 2.) init keepOriginalLoggingTimestamps
		final String keepOriginalLoggingTimestampsOptValStr = commandLine.getOptionValue(
				CMD_OPT_NAME_KEEPORIGINALLOGGINGTIMESTAMPS, "true");
		if (!("true".equals(keepOriginalLoggingTimestampsOptValStr) || "false".equals(keepOriginalLoggingTimestampsOptValStr))) {
			LOGGER.error("Invalid value for option {}: '{}'", CMD_OPT_NAME_KEEPORIGINALLOGGINGTIMESTAMPS, keepOriginalLoggingTimestampsOptValStr);
			retVal = false;
		}
		this.keepOriginalLoggingTimestamps = "true".equals(keepOriginalLoggingTimestampsOptValStr);
		LOGGER.debug("Keeping original logging timestamps: {}", this.keepOriginalLoggingTimestamps ? "true" : "false"); // NOCS
		// 3.) init realtimeMode
		final String realtimeOptValStr = commandLine.getOptionValue(CMD_OPT_NAME_REALTIME, "false");
		if (!("true".equals(realtimeOptValStr) || "false".equals(realtimeOptValStr))) {
			LOGGER.error("Invalid value for option {}: '{}'", CMD_OPT_NAME_REALTIME, realtimeOptValStr);
			retVal = false;
		}
		this.realtimeMode = "true".equals(realtimeOptValStr);

		// 4.) init numRealtimeWorkerThreads
		final String numRealtimeWorkerThreadsStr = commandLine.getOptionValue(CMD_OPT_NAME_NUM_REALTIME_WORKERS,
				"1");
		try {
			this.numRealtimeWorkerThreads = Integer.parseInt(numRealtimeWorkerThreadsStr);
		} catch (final NumberFormatException ex) {
			LOGGER.error("Invalid value for option {}: '{}'", CMD_OPT_NAME_NUM_REALTIME_WORKERS, numRealtimeWorkerThreadsStr);
			LOGGER.error("NumberFormatException: ", ex);
			retVal = false;
		}
		if (this.numRealtimeWorkerThreads < 1) {
			LOGGER.error("Option value for {} must be >= 1; found {}", CMD_OPT_NAME_NUM_REALTIME_WORKERS, this.numRealtimeWorkerThreads);
			LOGGER.error("Invalid specification of {}: {}", CMD_OPT_NAME_NUM_REALTIME_WORKERS, this.numRealtimeWorkerThreads);
			retVal = false;
		}

		// 5.) init realtimeAccelerationFactor
		final String realtimeAccelerationFactorStr = commandLine.getOptionValue(CMD_OPT_NAME_REALTIME_ACCELERATION_FACTOR,
				"1");
		try {
			this.realtimeAccelerationFactor = Double.parseDouble(realtimeAccelerationFactorStr);
		} catch (final NumberFormatException ex) {
			LOGGER.error("Invalid value for option {}: '{}'", CMD_OPT_NAME_REALTIME_ACCELERATION_FACTOR, numRealtimeWorkerThreadsStr);
			LOGGER.error("NumberFormatException: ", ex);
			retVal = false;
		}
		if (this.numRealtimeWorkerThreads <= 0) {
			LOGGER.error("Option value for {} must be > 0; found {}", CMD_OPT_NAME_REALTIME_ACCELERATION_FACTOR, this.realtimeAccelerationFactor);
			LOGGER.error("Invalid specification of {}: {}", CMD_OPT_NAME_REALTIME_ACCELERATION_FACTOR, this.realtimeAccelerationFactor);
			retVal = false;
		}

		// 6.) init ignoreRecordsBefore/After
		final DateFormat dateFormat = new SimpleDateFormat(DATE_FORMAT_PATTERN, Locale.US);
		dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));

		try {
			final String ignoreRecordsBeforeTimestampString = commandLine.getOptionValue(
					CMD_OPT_NAME_IGNORERECORDSBEFOREDATE, null);
			final String ignoreRecordsAfterTimestampString = commandLine.getOptionValue(
					CMD_OPT_NAME_IGNORERECORDSAFTERDATE, null);
			if (ignoreRecordsBeforeTimestampString != null) {
				final Date ignoreBeforeDate = dateFormat.parse(ignoreRecordsBeforeTimestampString);
				this.ignoreRecordsBeforeTimestamp = ignoreBeforeDate.getTime() * (1000 * 1000);
				LOGGER.debug("Ignoring records before {} ({})", dateFormat.format(ignoreBeforeDate), this.ignoreRecordsBeforeTimestamp);
			}
			if (ignoreRecordsAfterTimestampString != null) {
				final Date ignoreAfterDate = dateFormat.parse(ignoreRecordsAfterTimestampString);
				this.ignoreRecordsAfterTimestamp = ignoreAfterDate.getTime() * (1000 * 1000);
				LOGGER.debug("Ignoring records after {} ({})", dateFormat.format(ignoreAfterDate), this.ignoreRecordsAfterTimestamp);
			}
		} catch (final java.text.ParseException ex) {
			LOGGER.error("Error parsing date/time string. Please use the following pattern: {}", DATE_FORMAT_PATTERN_CMD_USAGE_HELP, ex);
			return false;
		}

		// log configuration
		if (retVal && LOGGER.isDebugEnabled()) {
			LOGGER.debug("inputDirs: {}", FilesystemLogReplayerStarter.fromStringArrayToDeliminedString(this.inputDirs, ';'));
			LOGGER.debug("Replaying in {} realtime mode", this.realtimeMode ? "" : "non-"); // NOCS
			if (this.realtimeMode) {
				LOGGER.debug("Using {} realtime worker thread {}", this.numRealtimeWorkerThreads, this.numRealtimeWorkerThreads > 1 ? "s" : ""); // NOCS
			}
		}

		return retVal;
	}

	@Override
	protected boolean performTask() {
		if (LOGGER.isDebugEnabled()) {
			if (this.realtimeMode) {
				LOGGER.debug("Replaying log data in real time");
			} else {
				LOGGER.debug("Replaying log data in non-real time");
			}
		}

		final FilesystemLogReplayer player = new FilesystemLogReplayer(this.monitoringConfigurationFile, this.realtimeMode, this.realtimeAccelerationFactor,
				this.keepOriginalLoggingTimestamps, this.numRealtimeWorkerThreads, this.ignoreRecordsBeforeTimestamp, this.ignoreRecordsAfterTimestamp,
				this.inputDirs);

		if (player.replay()) {
			return true;
		} else {
			LOGGER.error("An error occured");
			return false;
		}
	}

	private static String fromStringArrayToDeliminedString(final String[] array, final char delimiter) {
		Arrays.toString(array);
		final StringBuilder arTostr = new StringBuilder();
		if (array.length > 0) {
			arTostr.append(array[0]);
			for (int i = 1; i < array.length; i++) {
				arTostr.append(delimiter);
				arTostr.append(array[i]);
			}
		}
		return arTostr.toString();
	}

}
