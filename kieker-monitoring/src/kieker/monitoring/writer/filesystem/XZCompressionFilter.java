/***************************************************************************
 * Copyright 2018 Kieker Project (http://kieker-monitoring.net)
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
package kieker.monitoring.writer.filesystem;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Path;

import org.tukaani.xz.FilterOptions;
import org.tukaani.xz.LZMA2Options;
import org.tukaani.xz.XZOutputStream;

import kieker.common.configuration.Configuration;
import kieker.common.util.filesystem.FSUtil;

/**
 * XZ compression filter for the writer pool.
 *
 * @author Reiner Jung
 * @author Henning Schnoor
 *
 * @since 1.14
 */
public class XZCompressionFilter implements ICompressionFilter {

	/**
	 * Initialize XZCompression with parameter to adhere Kieker configuration system.
	 *
	 * @param configuration
	 *            Kieker configuration object
	 */
	public XZCompressionFilter(final Configuration configuration) { // NOPMD block warning of unused configuration parameter
		// Empty constructor. No initialization necessary.
	}

	@Override
	public OutputStream chainOutputStream(final OutputStream outputStream, final Path fileName) throws IOException {
		final FilterOptions filterOptions = new LZMA2Options(LZMA2Options.PRESET_MAX);
		return new XZOutputStream(outputStream, filterOptions);
	}

	@Override
	public String getExtension() {
		return FSUtil.XZ_FILE_EXTENSION;
	}

}
