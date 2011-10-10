/***************************************************************************
 * Copyright 2011 by
 *  + Christian-Albrechts-University of Kiel
 *    + Department of Computer Science
 *      + Software Engineering Group 
 *  and others.
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

package kieker.test.monitoring.junit.util;

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
import kieker.monitoring.writer.namedRecordPipe.PipeWriter;

/**
 * 
 * @author Andre van Hoorn
 * 
 */
public final class NamedPipeFactory {
	
	private static final AtomicInteger NEXT_PIPE_ID = new AtomicInteger(0);
	private static final String PIPE_NAME_PREFIX = "pipeName_" + NamedPipeFactory.class.getName() + "_";

	private NamedPipeFactory() {}

	/**
	 * This method should be used in tests to generate unique names for {@link Configuration}s with {@link PipeWriter}s and {@link java.io.PipedReader}s
	 * in order to avoid naming conflicts.
	 * 
	 * @return a unique name
	 */
	public static String createPipeName() {
		return NamedPipeFactory.PIPE_NAME_PREFIX + NamedPipeFactory.NEXT_PIPE_ID.getAndIncrement();
	}

	/**
	 * Creates a new {@link IMonitoringController} instance with the writer
	 * being a {@link PipeWriter} with the given name.
	 * 
	 * @param pipeName
	 * @return the created IMonitoringController instance
	 */
	public static IMonitoringController createMonitoringControllerWithNamedPipe(final String pipeName) {
		return NamedPipeFactory.createMonitoringControllerWithNamedPipe(pipeName, null);
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
	public static IMonitoringController createMonitoringControllerWithNamedPipe(final String pipeName, final Properties additionalProperties) {
		final Configuration configuration = Configuration.createDefaultConfiguration();
		configuration.setProperty(Configuration.WRITER_CLASSNAME, PipeWriter.class.getName());
		configuration.setProperty(PipeWriter.CONFIG_PIPENAME, pipeName);

		if (additionalProperties != null) {
			for (final Entry<Object, Object> property : additionalProperties.entrySet()) {
				final String key = (String) property.getKey();
				final String value = (String) property.getValue();
				configuration.setProperty(key, value);
			}
		}

		final IMonitoringController monitoringController = MonitoringController.createInstance(configuration);
		return monitoringController;
	}

	/**
	 * Creates an {@link kieker.monitoring.writer.IMonitoringWriter} that collects records from a {@link Pipe} and collects these in the returned {@link List}.
	 * 
	 * @param pipeName
	 * @return a list which contains the collected records
	 */
	public static List<IMonitoringRecord> createAndRegisterNamedPipeRecordCollector(final String pipeName) {
		final List<IMonitoringRecord> receivedRecords = new ArrayList<IMonitoringRecord>();
		final Pipe namedPipe = Broker.getInstance().acquirePipe(pipeName);
		namedPipe.setPipeReader(new IPipeReader() {

			@Override
			public boolean newMonitoringRecord(final IMonitoringRecord record) {
				return receivedRecords.add(record);
			}

			@Override
			public void notifyPipeClosed() {}
		});
		return receivedRecords;
	}
}
