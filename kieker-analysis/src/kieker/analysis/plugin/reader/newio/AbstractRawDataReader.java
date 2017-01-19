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

package kieker.analysis.plugin.reader.newio;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.nio.ByteBuffer;
import java.util.List;

import kieker.analysis.IProjectContext;
import kieker.analysis.plugin.reader.AbstractReaderPlugin;
import kieker.analysis.plugin.reader.newio.deserializer.IMonitoringRecordDeserializer;
import kieker.common.configuration.Configuration;
import kieker.common.record.IMonitoringRecord;

/**
 * Abstract superclass for all readers which only read raw data to be processed by a
 * configurable deserializer.
 *
 * @author Holger Knoche
 *
 * @since 1.13
 */
public abstract class AbstractRawDataReader extends AbstractReaderPlugin {

	private final IMonitoringRecordDeserializer deserializer;

	/**
	 * Creates a new reader using the given data.
	 * @param configuration The configuration to use
	 * @param projectContext The project context the reader runs in
	 * @param deserializerClassName The class name of the deserializer to use
	 */
	public AbstractRawDataReader(final Configuration configuration, final IProjectContext projectContext, final String deserializerClassName) {
		super(configuration, projectContext);

		this.deserializer = this.createDeserializer(deserializerClassName, configuration, projectContext);
	}

	@SuppressWarnings("unchecked")
	private IMonitoringRecordDeserializer createDeserializer(final String deserializerClassName, final Configuration configuration,
			final IProjectContext projectContext) {

		// TODO Externalize instance creation into a factory
		Class<? extends IMonitoringRecordDeserializer> deserializerClass;
		IMonitoringRecordDeserializer localDeserializer = null;

		try {
			deserializerClass = (Class<? extends IMonitoringRecordDeserializer>) Class.forName(deserializerClassName);
			final Constructor<? extends IMonitoringRecordDeserializer> constructor = deserializerClass.getConstructor(Configuration.class, IProjectContext.class);
			localDeserializer = constructor.newInstance(configuration, projectContext);
		} catch (final ClassNotFoundException e) {
			LOG.error("The deserializer class '" + deserializerClassName + "' was not found.");
		} catch (final NoSuchMethodException e) {
			LOG.error("The deserializer class '" + deserializerClassName + "' does not provide a suitable constructor.");
		} catch (final InstantiationException e) {
			this.logInstantiationFailed(deserializerClassName, e);
		} catch (final IllegalAccessException e) {
			this.logInstantiationFailed(deserializerClassName, e);
		} catch (final IllegalArgumentException e) {
			this.logInstantiationFailed(deserializerClassName, e);
		} catch (final InvocationTargetException e) {
			this.logInstantiationFailed(deserializerClassName, e);
		}

		return localDeserializer;
	}

	private void logInstantiationFailed(final String className, final Throwable e) {
		LOG.error("The deserializer class '" + className + "' could not be instantiated.", e);
	}

	/**
	 * Decodes the given raw data using the configured deserializer and delivers them to the
	 * given output port.
	 *
	 * @param rawData
	 *            The raw data to decode
	 * @param outputPortName
	 *            The output port name to send the decoded records to
	 */
	protected void decodeAndDeliverRecords(final byte[] rawData, final String outputPortName) {
		this.decodeAndDeliverRecords(ByteBuffer.wrap(rawData), rawData.length, outputPortName);
	}

	/**
	 * Decodes the given raw data using the configured deserializer and delivers
	 * them to the given output port.
	 *
	 * @param rawData
	 *            The raw data to decode
	 * @param dataSize
	 *            The size of the data to decode
	 * @param outputPortName
	 *            The output port name to send the decoded records to
	 */
	protected void decodeAndDeliverRecords(final ByteBuffer rawData, final int dataSize, final String outputPortName) {
		final List<IMonitoringRecord> monitoringRecords = this.deserializer.deserializeRecords(rawData, dataSize);

		for (final IMonitoringRecord monitoringRecord : monitoringRecords) {
			this.deliver(outputPortName, monitoringRecord);
		}
	}

}
