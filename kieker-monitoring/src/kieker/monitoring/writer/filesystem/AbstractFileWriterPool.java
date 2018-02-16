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
import java.nio.Buffer;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import kieker.common.logging.Log;
import kieker.common.util.filesystem.FSUtil;
import kieker.monitoring.writer.filesystem.compression.ICompressionFilter;
import kieker.monitoring.writer.filesystem.compression.NoneCompressionFilter;

/**
 * Abstract file writer pool.
 *
 * @param <T>
 *            buffer type
 *
 * @author Christian Wulf
 * @author Reiner Jung
 *
 * @since 1.14
 *
 * @deprecated 1.14 should be removed in 1.15 replaced by new FileWriter API
 */
@Deprecated
public abstract class AbstractFileWriterPool<T extends Buffer> {

	private static final String TIME_ZONE = "UTC";
	private static final Locale LOCALE = Locale.US;

	protected final Log writerLog; // NOPMD (logger passed by caller)
	protected final Path folder;

	protected final Charset charset;
	protected final int maxEntriesInFile;
	protected int numEntriesInCurrentFile;
	protected final ICompressionFilter compressionFilter;
	protected final int maxAmountOfFiles;
	protected final long maxBytesInFile;

	protected final String fileExtensionWithDot;

	protected T buffer;
	protected AbstractPooledFileChannel<T> currentChannel;

	private final List<Path> logFiles = new ArrayList<>();

	private int currentFileNumber;

	private final SimpleDateFormat dateFormatter;

	/**
	 * Create an ASCII writer pool.
	 *
	 * @param writerLog
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
	 * @param fileExtensionWithDot
	 *            file extension to be used for files if not compressed
	 */
	protected AbstractFileWriterPool(final Log writerLog, final Path folder, final String charsetName, final int maxEntriesInFile,
			final ICompressionFilter compressionFilter,
			final int maxAmountOfFiles, final int maxMegaBytesInFile, final String fileExtensionWithDot) {
		this.writerLog = writerLog;
		this.folder = folder;

		this.dateFormatter = new SimpleDateFormat("yyyyMMdd'-'HHmmssSSS", LOCALE);
		this.dateFormatter.setTimeZone(TimeZone.getTimeZone(TIME_ZONE));

		this.charset = Charset.forName(charsetName);
		this.maxEntriesInFile = maxEntriesInFile;
		this.numEntriesInCurrentFile = maxEntriesInFile; // triggers file creation
		this.compressionFilter = compressionFilter;
		this.maxAmountOfFiles = maxAmountOfFiles;
		this.maxBytesInFile = maxMegaBytesInFile * 1024L * 1024L; // conversion from MB to Bytes

		this.fileExtensionWithDot = (this.compressionFilter instanceof NoneCompressionFilter) ? fileExtensionWithDot : this.compressionFilter.getExtension(); // NOCS

		this.currentFileNumber = 0;
	}

	public AbstractPooledFileChannel<T> getFileWriter() {
		final int oldNumberOfEntires = this.numEntriesInCurrentFile;
		this.numEntriesInCurrentFile++;
		if (oldNumberOfEntires == this.maxEntriesInFile) {
			this.onThresholdExceeded();
		} else if ((this.numEntriesInCurrentFile - this.maxEntriesInFile) > 0) {
			this.onThresholdExceeded();
		} else if (this.currentChannel.getBytesWritten() > this.maxBytesInFile) {
			this.onThresholdExceeded();
		}

		if ((this.logFiles.size() > this.maxAmountOfFiles) && (this.maxAmountOfFiles > 0)) {
			this.onMaxLogFilesExceeded();
		}

		return this.currentChannel;
	}

	public void onMaxLogFilesExceeded() {
		final Path oldestFile = this.logFiles.remove(0);
		try {
			Files.delete(oldestFile);
		} catch (final IOException e) {
			this.writerLog.warn("Cannot delete oldest file.", e);
		}
	}

	protected abstract void onThresholdExceeded();

	public T getBuffer() {
		return this.buffer;
	}

	public Path getNextFileName(final int counter, final String extensionWithDot) {
		final Date now = new Date();

		// "%1$s-%2$tY%2$tm%2$td-%2$tH%2$tM%2$tS%2$tL-UTC-%3$03d-%4$s.%5$s"
		final String fileName = String.format(LOCALE, "%s-%s-%s-%03d%s",
				FSUtil.FILE_PREFIX, this.dateFormatter.format(now), TIME_ZONE, counter, extensionWithDot);

		final Path logFile = this.folder.resolve(fileName);
		this.logFiles.add(logFile);

		return logFile;
	}

	/**
	 * Request space in the buffer, if necessary flush the buffer.
	 *
	 * @param bufferSpace
	 *            requested size
	 * @param log
	 */
	public void requestBufferSpace(final int bufferSpace, final Log log) {
		if (bufferSpace > this.buffer.remaining()) {
			this.currentChannel.flush(log);
		}
	}

	public void close() {
		this.currentChannel.close(this.writerLog);
	}

	protected int getCurrentFileNumber() {
		return this.currentFileNumber;
	}

	protected void setCurrentFileNumber(final int currentFileNumber) {
		this.currentFileNumber = currentFileNumber;
	}

}
