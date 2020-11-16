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
package kieker.analysis.plugin.reader.newio;

import java.lang.reflect.InvocationTargetException;
import java.nio.ByteBuffer;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import kieker.analysis.IProjectContext;
import kieker.analysis.plugin.annotation.OutputPort;
import kieker.analysis.plugin.annotation.Plugin;
import kieker.analysis.plugin.annotation.Property;
import kieker.analysis.plugin.reader.AbstractReaderPlugin;
import kieker.analysis.plugin.reader.newio.deserializer.IMonitoringRecordDeserializer;
import kieker.common.configuration.Configuration;
import kieker.common.record.IMonitoringRecord;

/**
 * Generic reader plugin for the new raw data I/O infrastructure.
 *
 * @author Holger Knoche
 *
 * @since 1.13
 *
 */
@Plugin(description = "Generic reader plugin for raw data.", outputPorts = {
	@OutputPort(name = RawDataReaderPlugin.OUTPUT_PORT_NAME_RECORDS, eventTypes = IMonitoringRecord.class,
			description = "Output port for the decoded records") },
		configuration = {
			@Property(name = RawDataReaderPlugin.CONFIG_PROPERTY_DESERIALIZER, defaultValue = "", description = "Class name of the deserializer to use"),
			@Property(name = RawDataReaderPlugin.CONFIG_PROPERTY_READER, defaultValue = "", description = "Class name of the reader to use") })
public class RawDataReaderPlugin extends AbstractReaderPlugin implements IRawDataProcessor {

	/** The name of the output port for the decoded records. */
	public static final String OUTPUT_PORT_NAME_RECORDS = "monitoringRecords";

	/** The name of the configuration property for the deserializer class name. */
	public static final String CONFIG_PROPERTY_DESERIALIZER = "deserializer";

	/** The name of the configuration property for the reader class name. */
	public static final String CONFIG_PROPERTY_READER = "reader";

	private static final Logger LOGGER = LoggerFactory.getLogger(RawDataReaderPlugin.class);

	private final String deserializerClassName;

	private final String readerClassName;

	private final IRawDataReader reader;

	private final IMonitoringRecordDeserializer deserializer;

	public RawDataReaderPlugin(final Configuration configuration, final IProjectContext projectContext) {
		super(configuration, projectContext);

		final String readerName = configuration.getStringProperty(CONFIG_PROPERTY_READER);
		this.readerClassName = readerName;
		this.reader = this.createAndInitializeReader(readerName, configuration, IRawDataReader.class);

		final String deserializerName = configuration.getStringProperty(CONFIG_PROPERTY_DESERIALIZER);
		this.deserializerClassName = deserializerName;
		this.deserializer = this.createAndInitializeDeserializer(deserializerName, configuration, projectContext,
				IMonitoringRecordDeserializer.class);
	}

	@SuppressWarnings("unchecked")
	private <C> C createAndInitializeReader(final String className, final Configuration configuration,
			final Class<C> expectedType) {
		C createdInstance = null;

		try {
			final Class<?> clazz = Class.forName(className);

			// Check whether the class is a subtype of the expected type
			if (!(expectedType.isAssignableFrom(clazz))) {
				LOGGER.error("Class {} must implement {}.", className, expectedType.getSimpleName());
			}

			// Actually create the instance
			createdInstance = (C) this.instantiateReader(clazz, configuration);
		} catch (final ClassNotFoundException e) {
			LOGGER.error("Class '{}' not found.", className);
		} catch (final NoSuchMethodException e) {
			LOGGER.error("Class {} must implement a (public) constructor that accepts a Configuration.", className);
		} catch (final IllegalAccessException | InstantiationException | InvocationTargetException e) {
			LOGGER.error("Unable to instantiate class {}.", className, e);
		}

		return createdInstance;
	}

	private <C> C instantiateReader(final Class<C> clazz, final Configuration configuration)
			throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException,
			NoSuchMethodException, SecurityException {
		final Configuration configurationToPass = configuration.flatten();

		return clazz.getConstructor(Configuration.class, IRawDataProcessor.class).newInstance(configurationToPass,
				this);
	}

	@SuppressWarnings("unchecked")
	private <C> C createAndInitializeDeserializer(final String className, final Configuration configuration,
			final IProjectContext context, final Class<C> expectedType) {
		C createdInstance = null;

		try {
			final Class<?> clazz = Class.forName(className);

			// Check whether the class is a subtype of the expected type
			if (!(expectedType.isAssignableFrom(clazz))) {
				LOGGER.error("Class {} must implement {}.", className, expectedType.getSimpleName());
			}

			// Actually create the instance
			createdInstance = (C) this.instantiateDeserializer(clazz, configuration, context);
		} catch (final ClassNotFoundException e) {
			LOGGER.error("Class '{}' not found.", className);
		} catch (final NoSuchMethodException e) {
			LOGGER.error("Class {} must implement a (public) constructor that accepts a Configuration.", className);
		} catch (final IllegalAccessException | InstantiationException | InvocationTargetException e) {
			LOGGER.error("Unable to instantiate class {}.", className, e);
		}

		return createdInstance;
	}

	private <C> C instantiateDeserializer(final Class<C> clazz, final Configuration configuration,
			final IProjectContext context) throws InstantiationException, IllegalAccessException,
			IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {
		final Configuration configurationToPass = configuration.flatten();

		return clazz.getConstructor(Configuration.class, IProjectContext.class).newInstance(configurationToPass,
				context);
	}

	@Override
	public Configuration getCurrentConfiguration() {
		final Configuration configuration = new Configuration();

		configuration.setProperty(CONFIG_PROPERTY_DESERIALIZER, this.deserializerClassName);
		configuration.setProperty(CONFIG_PROPERTY_READER, this.readerClassName);

		return configuration;
	}

	@Override
	public boolean init() {
		// Invoke super init
		final boolean superInitSuccessful = super.init();
		if (!superInitSuccessful) {
			return false;
		}

		// Initialize the reader
		final Outcome readerInitOutcome = this.initReader();

		return readerInitOutcome == Outcome.SUCCESS;
	}

	private Outcome initReader() {
		return this.reader.onInitialization();
	}

	@Override
	public boolean read() {
		final Outcome readOutcome = this.reader.read();

		return (readOutcome == Outcome.SUCCESS);
	}

	@Override
	public void terminate(final boolean error) {
		this.terminateReader();
	}

	private Outcome terminateReader() {
		return this.reader.onTermination();
	}

	@Override
	public void decodeAndDeliverRecords(final byte[] rawData) {
		this.decodeAndDeliverRecords(ByteBuffer.wrap(rawData), rawData.length);
	}

	@Override
	public void decodeAndDeliverRecords(final ByteBuffer rawData, final int dataSize) {
		final List<IMonitoringRecord> records = this.deserializer.deserializeRecords(rawData, dataSize);

		// Deliver the records
		for (final IMonitoringRecord record : records) {
			this.deliver(OUTPUT_PORT_NAME_RECORDS, record);
		}
	}

}
