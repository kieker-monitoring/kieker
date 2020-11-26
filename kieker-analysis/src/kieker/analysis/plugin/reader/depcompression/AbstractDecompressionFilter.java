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
package kieker.analysis.plugin.reader.depcompression;

import java.io.IOException;
import java.io.InputStream;

import kieker.common.configuration.Configuration;

/**
 * @author Reiner Jung
 *
 * @since 1.15
 */
public abstract class AbstractDecompressionFilter {

	/**
	 * Initialize pass through "decompression" with parameter to adhere Kieker configuration system.
	 *
	 * @param configuration
	 *            Kieker configuration object
	 */
	public AbstractDecompressionFilter(final Configuration configuration) { // NOPMD block warning of unused configuration parameter

	}

	/**
	 * Create an input stream with decompression support and use the normal input stream as source.
	 *
	 * @param inputStream
	 *            uncompressed input stream
	 *
	 * @return the decompression input stream
	 *
	 * @throws IOException
	 *             on file or stream errors
	 *
	 */
	public abstract InputStream chainInputStream(InputStream inputStream) throws IOException;

	/**
	 * Return the extension with leading dot.
	 *
	 * @return return the extension
	 */
	public abstract String getExtension();
}
