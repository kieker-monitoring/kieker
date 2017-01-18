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
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import java.util.zip.GZIPOutputStream;

import kieker.common.logging.Log;
import kieker.common.util.filesystem.FSUtil;
import kieker.monitoring.writernew.WriterUtil;

/**
 * @author Christian Wulf (chw)
 *
 * @since 1.13
 */
public class BinaryFileWriterPool {

	private static final String TIME_ZONE = "UTC";
	private static final Locale LOCALE = Locale.US;

	private final Log writerLog;
	private final Path folder;

	private final int maxEntriesInFile;
	private int numEntriesInCurrentFile;
	// private final int maxAmountOfFiles;
	// private int currentAmountOfFiles;

	private final SimpleDateFormat dateFormatter;
	private WritableByteChannel currentWritableChannel;
	private int sameFilenameCounter;
	private final boolean shouldCompress;
	private final String fileExtensionWithDot;

	public BinaryFileWriterPool(final Log writerLog, final Path folder, final int maxEntriesInFile, final boolean shouldCompress) {
		this.writerLog = writerLog;
		this.folder = folder;
		this.maxEntriesInFile = maxEntriesInFile;
		this.numEntriesInCurrentFile = maxEntriesInFile; // triggers file creation
		this.shouldCompress = shouldCompress;
		// this.maxAmountOfFiles = maxAmountOfFiles;

		this.dateFormatter = new SimpleDateFormat("yyyyMMdd'-'HHmmssSSS", LOCALE);
		this.dateFormatter.setTimeZone(TimeZone.getTimeZone(TIME_ZONE));

		// this.currentWritableChannel = new DataOutputStream(new ByteArrayOutputStream()); // NullObject design pattern
		this.currentWritableChannel = Channels.newChannel(new ByteArrayOutputStream()); // NullObject design pattern
		this.fileExtensionWithDot = (shouldCompress) ? FSUtil.GZIP_FILE_EXTENSION : FSUtil.BINARY_FILE_EXTENSION;
	}

	public WritableByteChannel getFileWriter(final ByteBuffer buffer) {
		this.numEntriesInCurrentFile++;

		if (this.numEntriesInCurrentFile > this.maxEntriesInFile) {
			WriterUtil.flushBuffer(buffer, this.currentWritableChannel, this.writerLog);
			WriterUtil.close(this.currentWritableChannel, this.writerLog);

			final String newFileName = this.getNextFileName(this.sameFilenameCounter++);
			final Path newFile = this.folder.resolve(newFileName + this.fileExtensionWithDot);
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

			// currentAmountOfFiles++;
			// Files.delete(oldestfile);
		}

		return this.currentWritableChannel;
	}

	private String getNextFileName(final int counter) {
		final Date now = new Date();

		// "%1$s-%2$tY%2$tm%2$td-%2$tH%2$tM%2$tS%2$tL-UTC-%3$03d-%4$s.%5$s"
		final String filename = String.format(LOCALE, "%s-%s-%s-%03d",
				FSUtil.FILE_PREFIX, this.dateFormatter.format(now), TIME_ZONE, counter);
		return filename;
	}

	public void close(final ByteBuffer buffer) {
		WriterUtil.flushBuffer(buffer, this.currentWritableChannel, this.writerLog);
		WriterUtil.close(this.currentWritableChannel, this.writerLog);
	}

}
