package kieker.test.monitoring.junit.util;

import java.io.PipedReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.concurrent.atomic.AtomicInteger;

import kieker.common.namedRecordPipe.Broker;
import kieker.common.namedRecordPipe.IPipeReader;
import kieker.common.namedRecordPipe.Pipe;
import kieker.common.record.IMonitoringRecord;
import kieker.monitoring.core.configuration.Configuration;
import kieker.monitoring.core.controller.IMonitoringController;
import kieker.monitoring.core.controller.MonitoringController;
import kieker.monitoring.writer.IMonitoringWriter;
import kieker.monitoring.writer.namedRecordPipe.PipeWriter;

/**
 * 
 * @author Andre van Hoorn
 * 
 */
public class NamedPipeFactory {
	private final static AtomicInteger nextPipeId = new AtomicInteger(0);
	private final static String PIPE_NAME_PREFIX = "pipeName_";

	/**
	 * This method should be used in tests to generate unique names for
	 * {@link Configuration}s with {@link PipeWriter}s and {@link PipedReader}s
	 * in order to avoid naming conflicts.
	 * 
	 * @return a unique name
	 */
	public static String createPipeName() {
		return NamedPipeFactory.PIPE_NAME_PREFIX
				+ NamedPipeFactory.nextPipeId.getAndIncrement();
	}

	/**
	 * Creates a new {@link IMonitoringController} instance with the writer
	 * being a {@link PipeWriter} with the given name.
	 * 
	 * @param pipeName
	 * @return the created IMonitoringController instance
	 */
	public static IMonitoringController createMonitoringControllerWithNamedPipe(
			final String pipeName) {
		return NamedPipeFactory.createMonitoringControllerWithNamedPipe(
				pipeName, null);
	}

	/**
	 * Creates a new {@link IMonitoringController} instance with the writer
	 * being a {@link PipeWriter} with the given name. Additional configuration
	 * properties can be passed.
	 * 
	 * @param pipeName
	 * @param additionalProperties
	 *            additional configuration properties; null is allowed
	 * @return the created IMonitoringController instance
	 */
	public static IMonitoringController createMonitoringControllerWithNamedPipe(
			final String pipeName, final Properties additionalProperties) {
		final Configuration configuration =
				Configuration.createDefaultConfiguration();
		configuration.setProperty(Configuration.WRITER_CLASSNAME,
				PipeWriter.class.getName());
		configuration.setProperty(PipeWriter.CONFIG__PIPENAME, pipeName);

		if (additionalProperties != null) {
			for (final Entry<Object, Object> property : additionalProperties
					.entrySet()) {
				final String key = (String) property.getKey();
				final String value = (String) property.getValue();
				configuration.setProperty(key, value);
			}
		}

		final IMonitoringController monitoringController =
				MonitoringController.createInstance(configuration);
		return monitoringController;
	}

	/**
	 * Creates an {@link IMonitoringWriter} that collects records from a
	 * {@link Pipe} and collects these in the returned {@link List}.
	 * 
	 * @param pipeName
	 * @return a list which contains the collected records
	 */
	public static List<IMonitoringRecord> createAndRegisterNamedPipeRecordCollector(
			final String pipeName) {
		final List<IMonitoringRecord> receivedRecords =
				new ArrayList<IMonitoringRecord>();
		final Pipe namedPipe = Broker.getInstance().acquirePipe(pipeName);
		namedPipe.setPipeReader(new IPipeReader() {

			@Override
			public boolean newMonitoringRecord(final IMonitoringRecord record) {
				return receivedRecords.add(record);
			}

			@Override
			public void notifyPipeClosed() {
			}
		});
		return receivedRecords;
	}
}