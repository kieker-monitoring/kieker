/***************************************************************************
 * Copyright 2017 Kieker Project (http://kieker-monitoring.net)
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

import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Writer;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

import kieker.common.logging.Log;
import kieker.common.util.filesystem.FSUtil;
import kieker.monitoring.writer.WriterUtil;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

/**
 * @author Christian Wulf (chw)
 *
 * @since 1.13
 */
class AsciiFileWriterPool extends WriterPool {

	private final Charset charset;

	private final int maxEntriesInFile;
	private int numEntriesInCurrentFile;
	// private final int maxAmountOfFiles;
	// private int currentAmountOfFiles;

	private final ICompressionFilter compressionFilter;
	private final String fileExtensionWithDot;
	private final int maxAmountOfFiles;
	private final long maxBytesPerFile;

	private PrintWriter currentFileWriter;
	private MeasuringWriter currentMeasuringWriter;

	private int currentFileNumber;

	/**
	 * Create an ASCII writer pool.
	 *
	 * @param writerLog
	 *            logger for the pool
	 * @param folder
	 *            path where all files go
	 * @param maxEntriesPerFile
	 *            max entries per file
	 * @param compressionMethod
	 *            compression method
	 * @param maxAmountOfFiles
	 *            upper limit for the number of files
	 * @param maxMegaBytesPerFile
	 *            upper limit for the file size
	 */
	@SuppressFBWarnings("DM_DEFAULT_ENCODING")
	public AsciiFileWriterPool(final Log writerLog, final Path folder, final String charsetName, final int maxEntriesInFile,
			final ICompressionFilter compressionFilter,
			final int maxAmountOfFiles, final int maxMegaBytesPerFile) {
		super(writerLog, folder);
		this.maxAmountOfFiles = maxAmountOfFiles;
		this.maxBytesPerFile = maxMegaBytesPerFile * 1024L * 1024L; // conversion from MB to Bytes
		this.charset = Charset.forName(charsetName);
		this.maxEntriesInFile = maxEntriesInFile;
		this.numEntriesInCurrentFile = maxEntriesInFile; // triggers file creation
		this.compressionFilter = compressionFilter;

		// this.currentFileWriter = Channels.newChannel(new ByteArrayOutputStream()); // NullObject design pattern
		// final CharBuffer charBuffer = this.buffer.asCharBuffer();
		this.currentFileWriter = new PrintWriter(new ByteArrayOutputStream()); // NullObject design pattern
		this.fileExtensionWithDot = (this.compressionFilter instanceof NoneCompressionFilter) ? FSUtil.NORMAL_FILE_EXTENSION : this.compressionFilter.getExtension(); // NOCS
	}

	public PrintWriter getFileWriter() {
		this.numEntriesInCurrentFile++;

		// (buffer overflow aware comparison) means: numEntriesInCurrentFile > maxEntriesInFile
		if ((this.numEntriesInCurrentFile - this.maxEntriesInFile) > 0) {
			this.onThresholdExceeded();
		}

		// IMPROVE correctness: replace the property maxBytesPerFile by maxCharactersPerFile
		if (this.currentMeasuringWriter.getCharactersWritten() > this.maxBytesPerFile) {
			this.onThresholdExceeded();
		}

		if (this.logFiles.size() > this.maxAmountOfFiles) {
			this.onMaxLogFilesExceeded();
		}

		return this.currentFileWriter;
	}

	private void onThresholdExceeded() {
		WriterUtil.close(this.currentFileWriter, this.writerLog);

		this.currentFileNumber++;

		final Path newFile = this.getNextFileName(this.currentFileNumber, this.fileExtensionWithDot);
		try {
			Files.createDirectories(this.folder);

			// use CREATE_NEW to fail if the file already exists
			OutputStream outputStream = Files.newOutputStream(newFile, StandardOpenOption.CREATE_NEW, StandardOpenOption.WRITE);

			outputStream = this.compressionFilter.chainOutputStream(outputStream, newFile);

			// this.currentFileWriter = Channels.newChannel(outputStream);

			Writer writer = new OutputStreamWriter(outputStream, this.charset);
			writer = new BufferedWriter(writer);
			this.currentMeasuringWriter = new MeasuringWriter(writer);
			this.currentFileWriter = new PrintWriter(this.currentMeasuringWriter);
		} catch (final IOException e) {
			throw new IllegalStateException("This exception should not have been thrown.", e);
		}

		this.numEntriesInCurrentFile = 1;
	}

	public void close() {
		WriterUtil.close(this.currentFileWriter, this.writerLog);
	}

}
