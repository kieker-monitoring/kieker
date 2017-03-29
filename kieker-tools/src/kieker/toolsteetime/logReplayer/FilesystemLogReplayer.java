package kieker.toolsteetime.logReplayer;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

import kieker.analysisteetime.plugin.reader.filesystem.fsReader.FSReader;
import kieker.common.record.IMonitoringRecord;

import teetime.framework.AbstractProducerStage;

/**
 * An implementation of the {@link AbstractLogReplayer}, using the {@link FSReader} to replay {@link kieker.common.record.IMonitoringRecord}s from a list of file
 * system monitoring logs.
 *
 * @author Andre van Hoorn
 *
 * @since 0.95a
 */
public class FilesystemLogReplayer extends AbstractLogReplayer {
	// private static final Log LOG = LogFactory.getLog(FilesystemLogReplayer.class);

	private final String[] inputDirs;
	private final boolean ignoreUnknownRecordTypes;

	/**
	 * Creates a new instance of this class using the given parameters.
	 *
	 * @param monitoringConfigurationFile
	 *            The name of the monitoring configuration file.
	 * @param realtimeMode
	 *            Whether realtime mode should be used.
	 * @param realtimeTimeunit
	 *            The time unit to be used in realtime mode.
	 * @param realtimeAccelerationFactor
	 *            Determines whether to accelerate (value > 1.0) or slow down (<1.0) the replay in realtime mode by the given factor.
	 *            Choose a value of 1.0 for "real" realtime mode (i.e., no acceleration/slow down)
	 * @param keepOriginalLoggingTimestamps
	 *            Whether to keep the original logging timestamps or not.
	 * @param ignoreRecordsBeforeTimestamp
	 *            The lower limit for the timestamps.
	 * @param ignoreRecordsAfterTimestamp
	 *            The upper limit for the timestamps.
	 * @param inputDirs
	 *            The array containing the input directories.
	 * @param ignoreUnknownRecordTypes
	 *            Determines whether the filesystem reader ignores unknown record types or not.
	 */
	public FilesystemLogReplayer(final String monitoringConfigurationFile, final boolean realtimeMode, final TimeUnit realtimeTimeunit,
			final double realtimeAccelerationFactor, final boolean keepOriginalLoggingTimestamps, final long ignoreRecordsBeforeTimestamp,
			final long ignoreRecordsAfterTimestamp, final String[] inputDirs, final boolean ignoreUnknownRecordTypes) {
		super(monitoringConfigurationFile, realtimeMode, realtimeTimeunit, realtimeAccelerationFactor, keepOriginalLoggingTimestamps, ignoreRecordsBeforeTimestamp,
				ignoreRecordsAfterTimestamp);

		this.inputDirs = Arrays.copyOf(inputDirs, inputDirs.length);
		this.ignoreUnknownRecordTypes = ignoreUnknownRecordTypes;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected AbstractProducerStage<IMonitoringRecord> createReader() {
		return new FSReader(this.inputDirs, this.ignoreUnknownRecordTypes);
	}

}
