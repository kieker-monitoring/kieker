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

package kieker.monitoring.writernew.filesystem;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import kieker.common.logging.Log;
import kieker.common.util.filesystem.FSUtil;

/**
 * @author Christian Wulf (chw)
 *
 * @since 1.13
 */
class BinaryFileWriterPool extends WriterPool {

	private final int maxEntriesPerFile;
	private int numEntriesInCurrentFile;
	// private int currentAmountOfFiles;
	private final long maxBytesPerFile;

	private final boolean shouldCompress;
	private final String fileExtensionWithDot;
	private final int maxAmountOfFiles;

	private PooledFileChannel currentChannel;
	private int currentFileNumber;

	public BinaryFileWriterPool(final Log writerLog, final Path folder, final int maxEntriesPerFile, final boolean shouldCompress, final int maxAmountOfFiles,
			final int maxMegaBytesPerFile) {
		super(writerLog, folder);
		this.maxEntriesPerFile = maxEntriesPerFile;
		this.numEntriesInCurrentFile = maxEntriesPerFile; // triggers file creation
		this.shouldCompress = shouldCompress;
		this.maxAmountOfFiles = maxAmountOfFiles;
		this.maxBytesPerFile = maxMegaBytesPerFile * 1024L * 1024L; // conversion from MB to Bytes

		this.currentChannel = new PooledFileChannel(Channels.newChannel(new ByteArrayOutputStream())); // NullObject design pattern
		this.fileExtensionWithDot = (shouldCompress) ? FSUtil.ZIP_FILE_EXTENSION : FSUtil.BINARY_FILE_EXTENSION; // NOCS
	}

	public PooledFileChannel getFileWriter(final ByteBuffer buffer) {
		this.numEntriesInCurrentFile++;

		// (buffer overflow aware comparison) means: numEntriesInCurrentFile > maxEntriesPerFile
		if ((this.numEntriesInCurrentFile - this.maxEntriesPerFile) > 0) {
			this.onThresholdExceeded(buffer);
		}

		if (this.currentChannel.getBytesWritten() > this.maxBytesPerFile) {
			this.onThresholdExceeded(buffer);
		}

		if (this.logFiles.size() > this.maxAmountOfFiles) {
			this.onMaxLogFilesExceeded();
		}

		return this.currentChannel;
	}

	private void onThresholdExceeded(final ByteBuffer buffer) {
		this.currentChannel.close(buffer, this.writerLog);
		// we expect this.folder to exist

		this.currentFileNumber++;

		final Path newFile = this.getNextFileName(this.currentFileNumber, this.fileExtensionWithDot);
		try {
			// use CREATE_NEW to fail if the file already exists
			OutputStream outputStream = Files.newOutputStream(newFile, StandardOpenOption.CREATE_NEW, StandardOpenOption.WRITE);
			// stream is not buffered, since the byte buffer itself is the buffer

			if (this.shouldCompress) {
				// final GZIPOutputStream compressedOutputStream = new GZIPOutputStream(outputStream);
				final ZipOutputStream compressedOutputStream = new ZipOutputStream(outputStream);
				final ZipEntry newZipEntry = new ZipEntry(newFile.toString() + FSUtil.NORMAL_FILE_EXTENSION);
				compressedOutputStream.putNextEntry(newZipEntry);
				outputStream = compressedOutputStream;
			}

			this.currentChannel = new PooledFileChannel(Channels.newChannel(outputStream));
		} catch (final IOException e) {
			throw new IllegalStateException("This exception should not have been thrown.", e);
		}

		this.numEntriesInCurrentFile = 1;
	}

	public void close(final ByteBuffer buffer) {
		this.currentChannel.close(buffer, this.writerLog);
	}

}
