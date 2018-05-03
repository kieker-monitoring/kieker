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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.CharBuffer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

import org.slf4j.Logger;

import kieker.common.util.filesystem.FSUtil;
import kieker.monitoring.writer.filesystem.compression.ICompressionFilter;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

/**
 * @author Christian Wulf (chw)
 *
 * @since 1.13
 *
 * @deprecated 1.14 should be removed in 1.15 replaced by new FileWriter API
 */
@Deprecated
class AsciiFileWriterPool extends AbstractFileWriterPool<CharBuffer> {

	/**
	 * Create an ASCII writer pool.
	 *
	 * @param writerLogger
	 *            logger for the pool
	 * @param folder
	 *            path where all files go
	 * @param charsetName
	 *            identifier for the char set used for string serialization
	 * @param maxEntriesInFile
	 *            max entries per file
	 * @param compressionFilter
	 *            compression filter
	 * @param maxAmountOfFiles
	 *            upper limit for the number of files
	 * @param maxMegaBytesInFile
	 *            upper limit for the file size
	 * @param bufferSize
	 *            size of the writing buffer
	 */
	@SuppressFBWarnings("DM_DEFAULT_ENCODING")
	public AsciiFileWriterPool(final Logger writerLogger, final Path folder, final String charsetName, final int maxEntriesInFile,
			final ICompressionFilter compressionFilter,
			final int maxAmountOfFiles, final int maxMegaBytesInFile, final CharBuffer buffer) {

		super(writerLogger, folder, charsetName, maxEntriesInFile, compressionFilter, maxAmountOfFiles, maxMegaBytesInFile, FSUtil.DAT_FILE_EXTENSION);

		this.buffer = buffer;

		this.currentChannel = new AsciiPooledFileChannel(new ByteArrayOutputStream(), this.charset, this.buffer);
	}

	@Override
	protected void onThresholdExceeded() {
		this.currentChannel.close(this.writerLogger);
		// we expect this.folder to exist

		this.setCurrentFileNumber(this.getCurrentFileNumber() + 1);

		final Path newFile = this.getNextFileName(this.getCurrentFileNumber(), this.fileExtensionWithDot);
		try {
			Files.createDirectories(this.folder);

			// use CREATE_NEW to fail if the file already exists
			OutputStream outputStream = Files.newOutputStream(newFile, StandardOpenOption.CREATE_NEW, StandardOpenOption.WRITE);
			// stream is not buffered, since the byte buffer itself is the buffer

			outputStream = this.compressionFilter.chainOutputStream(outputStream, newFile);

			this.currentChannel = new AsciiPooledFileChannel(outputStream, this.charset, this.buffer);

		} catch (final IOException e) {
			throw new IllegalStateException("This exception should not have been thrown.", e);
		}

		this.numEntriesInCurrentFile = 1;
	}

}
