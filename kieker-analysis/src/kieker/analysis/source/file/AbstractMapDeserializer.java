/***************************************************************************
 * Copyright 2022 Kieker Project (http://kieker-monitoring.net)
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

import java.io.InputStream;

import kieker.common.registry.reader.ReaderRegistry;

/**
 * Read a mapping file.
 *
 * @author Reiner Jung
 *
 * @since 1.15
 */
public abstract class AbstractMapDeserializer {

	/**
	 * Create an abstract map deserializer.
	 */
	public AbstractMapDeserializer() {
		// Intentionally empty
	}

	/**
	 * Read a string map from an input stream and initialize the specified registry with the values.
	 *
	 * @param inputStream
	 *            the input stream
	 * @param registry
	 *            the string registry
	 * @param mapFileName
	 *            the associated file name used for error and debug output
	 */
	public abstract void processDataStream(final InputStream inputStream, final ReaderRegistry<String> registry, String mapFileName);
}
