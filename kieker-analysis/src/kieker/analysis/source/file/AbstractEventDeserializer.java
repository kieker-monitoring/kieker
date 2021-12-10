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
package kieker.analysis.source.file;

import java.io.IOException;
import java.io.InputStream;

import kieker.common.record.IMonitoringRecord;
import kieker.common.registry.reader.ReaderRegistry;

import teetime.framework.OutputPort;

/**
 * Abstract interface for the event deserialization.
 *
 * @author Reiner Jung
 *
 * @since 1.15
 *
 */
public abstract class AbstractEventDeserializer {

	protected final ReaderRegistry<String> registry;

	/**
	 * Create an abstract event deserializer.
	 *
	 * @param registry
	 *            string registry to be used.
	 */
	public AbstractEventDeserializer(final ReaderRegistry<String> registry) {
		this.registry = registry;
	}

	/**
	 * Read an input stream of data, deserialize it and output proper monitoring records.
	 *
	 * @param chainInputStream
	 *            the input stream
	 * @param outputPort
	 *            the output port
	 * @throws IOException
	 *             on input stream errors
	 *
	 */
	public abstract void processDataStream(InputStream chainInputStream, OutputPort<IMonitoringRecord> outputPort) throws IOException;

}
