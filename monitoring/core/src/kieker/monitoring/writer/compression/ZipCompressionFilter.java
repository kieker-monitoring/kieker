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
package kieker.monitoring.writer.compression;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Path;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import kieker.common.configuration.Configuration;
import kieker.common.util.filesystem.FSUtil;

/**
 * Zip compression filter for the writer pool.
 *
 * @author Reiner Jung
 *
 * @since 1.14
 */
public class ZipCompressionFilter implements ICompressionFilter {

	/**
	 * Initialize ZipCompression with parameter to adhere Kieker configuration system.
	 *
	 * @param configuration
	 *            Kieker configuration object
	 */
	public ZipCompressionFilter(final Configuration configuration) { // NOPMD block warning of unused configuration parameter
		// Empty constructor. No initialization necessary.
	}

	@Override
	public OutputStream chainOutputStream(final OutputStream outputStream, final Path fileName) throws IOException {
		final ZipOutputStream compressedOutputStream = new ZipOutputStream(outputStream);
		final ZipEntry newZipEntry = new ZipEntry(fileName.toString() + FSUtil.DAT_FILE_EXTENSION);
		compressedOutputStream.putNextEntry(newZipEntry);

		return compressedOutputStream;
	}

	@Override
	public String getExtension() {
		return FSUtil.ZIP_FILE_EXTENSION;
	}

}
