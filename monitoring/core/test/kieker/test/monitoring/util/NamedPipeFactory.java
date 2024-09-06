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

package kieker.test.monitoring.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.concurrent.atomic.AtomicInteger;

import kieker.common.configuration.Configuration;
import kieker.common.namedRecordPipe.Broker;
import kieker.common.namedRecordPipe.IPipeReader;
import kieker.common.namedRecordPipe.Pipe;
import kieker.common.record.IMonitoringRecord;
import kieker.monitoring.core.configuration.ConfigurationConstants;
import kieker.monitoring.core.configuration.ConfigurationFactory;
import kieker.monitoring.core.controller.MonitoringController;
import kieker.monitoring.writer.namedRecordPipe.PipeWriter;

/**
 * Provides factory methods for {@link MonitoringController}s configured to write to a {@link Pipe} and a convenient
 * collector facility to access the records received in a {@link List}. Note that, in contrast to the similar class
 * {@link NamedPipeFactory}, doesn't use the {@link PipeWriter} directly, but uses an * {@link MonitoringController}.
 * Also, the {@link kieker.analysis.plugin.reader.namedRecordPipe.PipeReader} is used.
 *
 * @author Andre van Hoorn
 *
 * @since 1.4
 */
public final class NamedPipeFactory {

	private static final AtomicInteger NEXT_PIPE_ID = new AtomicInteger(0);
	private static final String PIPE_NAME_PREFIX = "pipeName_" + NamedPipeFactory.class.getName() + "_";

	private NamedPipeFactory() {
		// utility class
	}

	/**
	 * This method should be used in tests to generate unique names for {@link Configuration}s with {@link PipeWriter}s
	 * and {@link java.io.PipedReader}s in order to avoid naming conflicts.
	 *
	 * @return a unique name
	 */
	public static String createPipeName() {
		return NamedPipeFactory.PIPE_NAME_PREFIX + NamedPipeFactory.NEXT_PIPE_ID.getAndIncrement();
	}

	/**
	 * Creates a new {@link MonitoringController} instance with the writer being a {@link PipeWriter} with the given
	 * name.
	 *
	 * @param pipeName
	 *            The name of the pipe to use.
	 * @return the created IMonitoringController instance
	 */
	public static MonitoringController createMonitoringControllerWithNamedPipe(final String pipeName) {
		return NamedPipeFactory.createMonitoringControllerWithNamedPipe(pipeName, null);
	}

	/**
	 * Creates a new {@link MonitoringController} instance with the writer being a {@link PipeWriter} with the given
	 * name. Additional configuration properties can be passed.
	 *
	 * @param pipeName
	 *            The name of the pipe to use.
	 * @param additionalProperties
	 *            additional configuration properties; null is allowed
	 * @return the created IMonitoringController instance
	 */
	public static MonitoringController createMonitoringControllerWithNamedPipe(final String pipeName,
			final Properties additionalProperties) {
		final Configuration configuration = ConfigurationFactory.createDefaultConfiguration();
		configuration.setProperty(ConfigurationConstants.META_DATA, "false");
		configuration.setProperty(ConfigurationConstants.WRITER_CLASSNAME, PipeWriter.class.getName());
		configuration.setProperty(PipeWriter.CONFIG_PIPENAME, pipeName);

		if (additionalProperties != null) {
			for (final Entry<Object, Object> property : additionalProperties.entrySet()) {
				final String key = (String) property.getKey();
				final String value = (String) property.getValue();
				configuration.setProperty(key, value);
			}
		}
		return MonitoringController.createInstance(configuration);
	}

	/**
	 * Creates an {@link kieker.common.namedRecordPipe.IPipeReader IPipeReader} that collects records from a
	 * {@link Pipe} and collects these in the returned {@link List}.
	 *
	 * @param pipeName
	 *            The name of the pipe to use.
	 * @return a synchronized list which contains the collected records
	 */
	public static List<IMonitoringRecord> createAndRegisterNamedPipeRecordCollector(final String pipeName) {
		final List<IMonitoringRecord> receivedRecords = Collections
				.synchronizedList(new ArrayList<>());
		final Pipe namedPipe = Broker.INSTANCE.acquirePipe(pipeName);
		namedPipe.setPipeReader(new IPipeReader() {

			@Override
			public void notifyPipeClosed() {
				// do nothing
			}

			@Override
			public boolean newMonitoringRecord(final IMonitoringRecord record) {
				return receivedRecords.add(record);
			}

		});
		return receivedRecords;
	}
}
