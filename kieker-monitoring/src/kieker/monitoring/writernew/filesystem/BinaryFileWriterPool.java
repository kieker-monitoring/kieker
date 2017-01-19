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
import java.nio.channels.WritableByteChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.zip.GZIPOutputStream;

import kieker.common.logging.Log;
import kieker.common.util.filesystem.FSUtil;
import kieker.monitoring.writernew.WriterUtil;

/**
 * @author Christian Wulf (chw)
 *
 * @since 1.13
 */
public class BinaryFileWriterPool extends AbstractWriterPool {

	private final int maxEntriesInFile;
	private int numEntriesInCurrentFile;
	// private int currentAmountOfFiles;
	private final boolean shouldCompress;
	private final String fileExtensionWithDot;
	private final int maxAmountOfFiles;

	private WritableByteChannel currentWritableChannel;

	public BinaryFileWriterPool(final Log writerLog, final Path folder, final int maxEntriesInFile, final boolean shouldCompress, final int maxAmountOfFiles) {
		super(writerLog, folder);
		this.maxEntriesInFile = maxEntriesInFile;
		this.numEntriesInCurrentFile = maxEntriesInFile; // triggers file creation
		this.shouldCompress = shouldCompress;
		this.maxAmountOfFiles = maxAmountOfFiles;

		this.currentWritableChannel = Channels.newChannel(new ByteArrayOutputStream()); // NullObject design pattern
		this.fileExtensionWithDot = (shouldCompress) ? FSUtil.GZIP_FILE_EXTENSION : FSUtil.BINARY_FILE_EXTENSION;
	}

	public WritableByteChannel getFileWriter(final ByteBuffer buffer) {
		this.numEntriesInCurrentFile++;

		if (this.numEntriesInCurrentFile > this.maxEntriesInFile) {
			this.onMaxEntriesInFileExceeded(buffer);
		}

		if (this.logFiles.size() > this.maxAmountOfFiles) {
			this.onMaxLogFilesExceeded();
		}

		return this.currentWritableChannel;
	}

	private void onMaxEntriesInFileExceeded(final ByteBuffer buffer) {
		WriterUtil.flushBuffer(buffer, this.currentWritableChannel, this.writerLog);
		WriterUtil.close(this.currentWritableChannel, this.writerLog);

		final Path newFile = this.getNextFileName(this.fileExtensionWithDot);
		try {
			Files.createDirectories(this.folder);

			// use CREATE_NEW to fail if the file already exists
			OutputStream outputStream = Files.newOutputStream(newFile, StandardOpenOption.CREATE_NEW, StandardOpenOption.WRITE);

			if (this.shouldCompress) {
				final GZIPOutputStream compressedOutputStream = new GZIPOutputStream(outputStream);
				// final ZipEntry newZipEntry = new ZipEntry(newFileName + FSUtil.NORMAL_FILE_EXTENSION);
				// compressedOutputStream.putNextEntry(newZipEntry);
				outputStream = compressedOutputStream;
			}

			this.currentWritableChannel = Channels.newChannel(outputStream);
		} catch (final IOException e) {
			throw new IllegalStateException("This exception should not have been thrown.", e);
		}

		this.numEntriesInCurrentFile = 1;
	}

	public void close(final ByteBuffer buffer) {
		WriterUtil.flushBuffer(buffer, this.currentWritableChannel, this.writerLog);
		WriterUtil.close(this.currentWritableChannel, this.writerLog);
	}

}
