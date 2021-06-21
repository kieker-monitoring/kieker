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
package kieker.monitoring.writer.compression;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Path;

/**
 * Common interface for compression filter used in the binary writer.
 *
 * @author Reiner Jung
 *
 * @since 1.14
 */
public interface ICompressionFilter {

	/**
	 * Create an output stream with compression support and use the normal output stream as source.
	 *
	 * @param outputStream
	 *            uncompressed output stream
	 * @param fileName
	 *            file name used in compression system, which also use an internal directory structure.
	 *
	 * @return the compression output stream
	 *
	 * @throws IOException
	 *             on file or stream errors
	 *
	 * @since 1.14
	 */
	OutputStream chainOutputStream(OutputStream outputStream, Path fileName) throws IOException;

	/**
	 * Return the extension with leading dot.
	 *
	 * @return return the extension
	 *
	 * @since 1.14
	 */
	String getExtension();

}
