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
package kieker.analysis.plugin.reader.depcompression;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.InflaterInputStream;

import kieker.common.util.filesystem.FSUtil;

/**
 * Decompression filter for data stream providing Deflate decompression.
 *
 * @author Reiner Jung
 *
 * @since 1.15
 *
 */
public class DeflateDecompressionFilter extends AbstractDecompressionFilter {

	private static final int BUFFER_SIZE = 1024 * 1024;

	/**
	 * Create a deflate decompression filter.
	 */
	public DeflateDecompressionFilter() {
		super();
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see kieker.analysis.plugin.reader.depcompression.IDecompressionFilter#chainInputStream(java.io.OutputStream)
	 */
	@Override
	public InputStream chainInputStream(final InputStream inputStream) throws IOException {
		return new BufferedInputStream(new InflaterInputStream(inputStream), BUFFER_SIZE);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see kieker.analysis.plugin.reader.depcompression.IDecompressionFilter#getExtension()
	 */
	@Override
	public String getExtension() {
		return FSUtil.DEFLATE_FILE_EXTENSION;
	}

}
