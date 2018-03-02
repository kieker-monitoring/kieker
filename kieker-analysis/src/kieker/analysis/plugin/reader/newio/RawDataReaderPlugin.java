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
package kieker.analysis.plugin.reader.newio;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.util.List;

import kieker.analysis.IProjectContext;
import kieker.analysis.exception.AnalysisConfigurationException;
import kieker.analysis.plugin.annotation.OutputPort;
import kieker.analysis.plugin.annotation.Plugin;
import kieker.analysis.plugin.annotation.Property;
import kieker.analysis.plugin.reader.AbstractReaderPlugin;
import kieker.analysis.plugin.reader.newio.deserializer.IMonitoringRecordDeserializer;
import kieker.common.configuration.Configuration;
import kieker.common.logging.Log;
import kieker.common.logging.LogFactory;
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
		@OutputPort(name = RawDataReaderPlugin.OUTPUT_PORT_NAME_RECORDS, eventTypes = {
				IMonitoringRecord.class }, description = "Output port for the decoded records") }, configuration = {
						@Property(name = RawDataReaderPlugin.CONFIG_PROPERTY_DESERIALIZER, defaultValue = "", description = "Class name of the deserializer to use"),
						@Property(name = RawDataReaderPlugin.CONFIG_PROPERTY_READER, defaultValue = "", description = "Class name of the reader to use")
})
public class RawDataReaderPlugin extends AbstractReaderPlugin implements IRawDataProcessor {

	/** The name of the output port for the decoded records. */
	public static final String OUTPUT_PORT_NAME_RECORDS = "monitoringRecords";

	/** The name of the configuration property for the deserializer class name. */
	public static final String CONFIG_PROPERTY_DESERIALIZER = "deserializer";

	/** The name of the configuration property for the reader class name. */
	public static final String CONFIG_PROPERTY_READER = "reader";

	private static final Log LOG = LogFactory.getLog(RawDataReaderPlugin.class);

	private final String deserializerClassName;

	private final String readerClassName;

	private final IRawDataReader reader;

	private final IMonitoringRecordDeserializer deserializer;

	public RawDataReaderPlugin(final Configuration configuration, final IProjectContext projectContext) throws AnalysisConfigurationException {
		super(configuration, projectContext);

		final String deserializerName = configuration.getStringProperty(CONFIG_PROPERTY_DESERIALIZER);
		this.deserializerClassName = deserializerName;
		this.deserializer = this.createAndInitializeDeserializer(deserializerName, configuration, projectContext, IMonitoringRecordDeserializer.class);

		final String readerName = configuration.getStringProperty(CONFIG_PROPERTY_READER);
		this.readerClassName = readerName;
		this.reader = this.createAndInitializeReader(readerName, configuration, this.deserializer, IRawDataReader.class);
	}

	@SuppressWarnings("unchecked")
	private <C> C createAndInitializeReader(final String className, final Configuration configuration, final IMonitoringRecordDeserializer recordDeserializer, final Class<C> expectedType) throws AnalysisConfigurationException {
		C createdInstance = null;

		try {
			final Class<?> clazz = Class.forName(className);

			// Check whether the class is a subtype of the expected type
			if (!(expectedType.isAssignableFrom(clazz))) {
				final String message = "Class " + className + " must implement " + expectedType.getSimpleName() + ".";
				this.handleConfigurationError(message);
			}

			// Actually create the instance
			createdInstance = (C) this.instantiateReader(clazz, configuration, recordDeserializer);
		} catch (final ClassNotFoundException e) {
			this.handleConfigurationError("Class '" + className + "' not found.");
		} catch (final IllegalAccessException | InstantiationException | InvocationTargetException e) {
			this.handleConfigurationError("Unable to instantiate class " + className + ".", e);
		}

		return createdInstance;
	}

	private <C> C instantiateReader(final Class<C> clazz, final Configuration configuration, final IMonitoringRecordDeserializer recordDeserializer) throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, SecurityException, AnalysisConfigurationException {
		// Determine which constructors are provided by the reader
		final Constructor<C> constructorWithUnwrapper = this.getConstructor(clazz, Configuration.class, Class.class, IRawDataProcessor.class);
		final Constructor<C> constructorWithoutUnwrapper = this.getConstructor(clazz, Configuration.class, IRawDataProcessor.class);

		// Check whether the required constructor is available
		if (constructorWithUnwrapper == null && constructorWithoutUnwrapper == null) {
			// No suitable constructor at all
			this.handleConfigurationError("Class " + clazz.getName() + " does not provide a suitable constructor.");
			return null;
		}

		// Get the unwrapper type from the deserializer, if any
		final Class<? extends IRawDataUnwrapper> unwrapperType = recordDeserializer.getUnwrapperType();

		if (constructorWithoutUnwrapper == null && unwrapperType == null) {
			// Reader requires unwrapper, but none is provided
			this.handleConfigurationError("Reader " + clazz.getName() + " requires an unwrapper, but serializer " + recordDeserializer.getClass().getName() + " does not provide one.");
			return null;
		}

		final C instantiatedReader;
		final Configuration configurationToPass = configuration.flatten();

		// Execute the appropriate constructor
		if (constructorWithUnwrapper != null && unwrapperType != null) {
			// If a constructor with unwrapper as well as an unwrapper type are available, use this constructor
			instantiatedReader = constructorWithUnwrapper.newInstance(configurationToPass, unwrapperType, this);
		} else {
			// Otherwise use the no-unwrapper constructor
			instantiatedReader = constructorWithoutUnwrapper.newInstance(configurationToPass, this);
		}

		return instantiatedReader;
	}

	private <C> Constructor<C> getConstructor(final Class<C> clazz, final Class<?>... parameterTypes) {
		try {
			return clazz.getConstructor(parameterTypes);
		} catch (final NoSuchMethodException e) {
			return null;
		}
	}

	@SuppressWarnings("unchecked")
	private <C> C createAndInitializeDeserializer(final String className, final Configuration configuration, final IProjectContext context, final Class<C> expectedType) throws AnalysisConfigurationException {
		C createdInstance = null;

		try {
			final Class<?> clazz = Class.forName(className);

			// Check whether the class is a subtype of the expected type
			if (!(expectedType.isAssignableFrom(clazz))) {
				this.handleConfigurationError("Class " + className + " must implement " + expectedType.getSimpleName() + ".");
			}

			// Actually create the instance
			createdInstance = (C) this.instantiateDeserializer(clazz, configuration, context);
		} catch (final ClassNotFoundException e) {
			this.handleConfigurationError("Class '" + className + "' not found.");
		} catch (final NoSuchMethodException e) {
			this.handleConfigurationError("Class " + className + " must implement a (public) constructor that accepts a Configuration.");
		} catch (final IllegalAccessException | InstantiationException | InvocationTargetException e) {
			this.handleConfigurationError("Unable to instantiate class " + className + ".", e);
		}

		return createdInstance;
	}

	private <C> C instantiateDeserializer(final Class<C> clazz, final Configuration configuration, final IProjectContext context) throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {
		final Configuration configurationToPass = configuration.flatten();

		return clazz.getConstructor(Configuration.class, IProjectContext.class).newInstance(configurationToPass, context);
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
		if (readerInitOutcome != Outcome.SUCCESS) {
			return false;
		}

		return true;
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
		this.decodeBytesAndDeliverRecords(rawData);
	}

	@Override
	public void decodeAndDeliverRecords(final ByteBuffer rawData, final int dataSize) {
		this.decodeBytesAndDeliverRecords(rawData, dataSize);
	}

	@Override
	public void decodeBytesAndDeliverRecords(final byte[] rawData) {
		this.decodeAndDeliverRecords(ByteBuffer.wrap(rawData), rawData.length);
	}

	@Override
	public void decodeBytesAndDeliverRecords(final ByteBuffer rawData, final int dataSize) {
		final List<IMonitoringRecord> records = this.deserializer.deserializeRecordsFromByteBuffer(rawData, dataSize);

		// Deliver the records
		this.deliver(records);
	}

	@Override
	public void decodeCharactersAndDeliverRecords(final char[] rawData) {
		this.decodeCharactersAndDeliverRecords(CharBuffer.wrap(rawData), rawData.length);
	}

	@Override
	public void decodeCharactersAndDeliverRecords(final CharBuffer rawData, final int dataSize) {
		final List<IMonitoringRecord> records = this.deserializer.deserializeRecordsFromCharBuffer(rawData, dataSize);

		// Deliver the records
		this.deliver(records);
	}

	private void deliver(final List<IMonitoringRecord> records) {
		for (final IMonitoringRecord record : records) {
			this.deliver(OUTPUT_PORT_NAME_RECORDS, record);
		}
	}

	private void handleConfigurationError(final String message) throws AnalysisConfigurationException {
		this.handleConfigurationError(message, null);
	}

	private void handleConfigurationError(final String message, final Throwable cause) throws AnalysisConfigurationException {
		if (cause != null) {
			LOG.error(message, cause);
			throw new AnalysisConfigurationException(message, cause);
		} else {
			LOG.error(message);
			throw new AnalysisConfigurationException(message);
		}
	}

}
