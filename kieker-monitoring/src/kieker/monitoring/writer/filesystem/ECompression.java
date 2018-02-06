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

import kieker.common.util.filesystem.FSUtil;

/**
 * Enumeration collecting all compression functions the file writer supports.
 *
 * @author Reiner Jung
 *
 * @since 1.14
 */
public enum ECompression {
	NONE("none", FSUtil.NORMAL_FILE_EXTENSION, new NoneCompressionFilter()), // No compression
	ZIP("zip", FSUtil.ZIP_FILE_EXTENSION, new ZipCompressionFilter()), // use ZIP compression
	GZIP("gzip", FSUtil.GZIP_FILE_EXTENSION, new GZipCompressionFilter()), // use GZIP compression
	XZ("xz", FSUtil.XZ_FILE_EXTENSION, new XZCompressionFilter()); // use XZ compression

	private String methodName;

	private String extension;

	private ICompressionFilter compressionFilter;

	private ECompression(final String methodName, final String extension, final ICompressionFilter compressionFilter) {
		this.methodName = methodName;
		this.extension = extension;
		this.compressionFilter = compressionFilter;
	}

	public String getMethodName() {
		return this.methodName;
	}

	public ICompressionFilter getClassType() {
		return this.compressionFilter;
	}

	public static ECompression findCompressionMethod(final String methodName) {
		for (final ECompression selected : ECompression.values()) {
			if (selected.getMethodName().equals(methodName)) {
				return selected;
			}
		}
		return NONE;
	}

	public String getExtension() {
		return this.extension;
	}

	/**
	 * Create a compression output stream chained to uncompressed output stream.
	 *
	 * @param outputStream
	 *            uncompressed output stream
	 * @param fileName
	 *            file name used in compression approaches which use a internal dictionary
	 *
	 * @return compressed output stream
	 *
	 * @throws IOException
	 *             on file or stream errors
	 */
	public OutputStream chainOutputStream(final OutputStream outputStream, final Path fileName) throws IOException {
		return this.getClassType().chain(outputStream, fileName);
	}

}
