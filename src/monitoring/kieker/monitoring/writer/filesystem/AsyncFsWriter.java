/***************************************************************************
 * Copyright 2011 by
 *  + Christian-Albrechts-University of Kiel
 *    + Department of Computer Science
 *      + Software Engineering Group 
 *  and others.
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
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.TimeZone;
import java.util.concurrent.BlockingQueue;

import kieker.common.record.IMonitoringRecord;
import kieker.monitoring.core.configuration.Configuration;
import kieker.monitoring.core.controller.IMonitoringController;
import kieker.monitoring.writer.AbstractAsyncThread;
import kieker.monitoring.writer.AbstractAsyncWriter;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author Matthias Rohr, Andre van Hoorn, Jan Waller, Robert von Massow
 */
public final class AsyncFsWriter extends AbstractAsyncWriter {
	private static final String PREFIX = AsyncFsWriter.class.getName() + ".";
	public static final String CONFIG_PATH = AsyncFsWriter.PREFIX + "customStoragePath"; // NOCS (afterPREFIX)
	public static final String CONFIG_TEMP = AsyncFsWriter.PREFIX + "storeInJavaIoTmpdir"; // NOCS (afterPREFIX)
	public static final String CONFIG_FLUSH = AsyncFsWriter.PREFIX + "flush"; // NOCS (afterPREFIX)

	private static final Log LOG = LogFactory.getLog(AsyncFsWriter.class);

	public AsyncFsWriter(final Configuration configuration) {
		super(configuration);
	}

	@Override
	protected void init() {
		String path;
		if (this.configuration.getBooleanProperty(AsyncFsWriter.CONFIG_TEMP)) {
			path = System.getProperty("java.io.tmpdir");
		} else {
			path = this.configuration.getStringProperty(AsyncFsWriter.CONFIG_PATH);
		}
		File f = new File(path);
		if (!f.isDirectory()) {
			final String errorMsg = "'" + path + "' is not a directory.";
			AsyncFsWriter.LOG.error(errorMsg);
			throw new IllegalArgumentException(errorMsg);
		}
		final String ctrlName = super.monitoringController.getHostName() + "-" + super.monitoringController.getName();

		final DateFormat date = new SimpleDateFormat("yyyyMMdd'-'HHmmssSS", Locale.US);
		date.setTimeZone(TimeZone.getTimeZone("UTC"));
		final String dateStr = date.format(new java.util.Date()); // NOPMD (Date)
		final StringBuffer sb = new StringBuffer(path);
		sb.append(File.separatorChar).append("kieker-").append(dateStr).append("-UTC-").append(ctrlName).append(File.separatorChar);
		path = sb.toString();
		f = new File(path);
		if (!f.mkdir()) {
			final String errorMsg = "Failed to create directory '" + path + "'";
			AsyncFsWriter.LOG.error(errorMsg);
			throw new IllegalArgumentException(errorMsg);
		}

		final String mappingFileFn = path + File.separatorChar + "kieker.map";
		final MappingFileWriter mappingFileWriter;
		try {
			mappingFileWriter = new MappingFileWriter(mappingFileFn); // NOPMD
		} catch (final IOException ex) {
			final String errorMsg = "Failed to create mapping file '" + mappingFileFn + "'";
			AsyncFsWriter.LOG.error(errorMsg);
			throw new IllegalArgumentException(errorMsg, ex);
		}

		this.addWorker(new FsWriterThread(super.monitoringController, this.blockingQueue, mappingFileWriter, path, this.configuration
				.getBooleanProperty(AsyncFsWriter.CONFIG_FLUSH)));
	}
}

/**
 * @author Matthias Rohr, Andre van Hoorn, Jan Waller
 */
final class FsWriterThread extends AbstractAsyncThread {

	// configuration parameters
	private static final int MAXENTRIESINFILE = 25000;

	// internal variables
	private final String filenamePrefix;
	private final MappingFileWriter mappingFileWriter;
	private final boolean autoflush;
	private PrintWriter pos = null;
	// Force to initialize first file!
	private int entriesInCurrentFileCounter = FsWriterThread.MAXENTRIESINFILE;

	// to get that info later
	private final String path;

	public FsWriterThread(final IMonitoringController monitoringController, final BlockingQueue<IMonitoringRecord> writeQueue,
			final MappingFileWriter mappingFileWriter, final String path, final boolean autoflush) {
		super(monitoringController, writeQueue);
		this.path = new File(path).getAbsolutePath();
		this.filenamePrefix = path + File.separatorChar + "kieker";
		this.mappingFileWriter = mappingFileWriter;
		this.autoflush = autoflush;
	}

	/**
	 * Note that it's not necessary to synchronize this method since a file is
	 * written at most by one thread.
	 * 
	 * @throws java.io.IOException
	 */
	@Override
	protected final void consume(final IMonitoringRecord monitoringRecord) throws IOException {
		final Object[] recordFields = monitoringRecord.toArray();
		final int lastFieldIndex = recordFields.length - 1;
		final StringBuilder sb = new StringBuilder(256);
		sb.append('$');
		sb.append(this.mappingFileWriter.idForRecordTypeClass(monitoringRecord.getClass()));
		sb.append(';');
		sb.append(monitoringRecord.getLoggingTimestamp());
		if (lastFieldIndex > 0) {
			sb.append(';');
		}
		for (int i = 0; i <= lastFieldIndex; i++) {
			sb.append(recordFields[i]);
			if (i < lastFieldIndex) {
				sb.append(';');
			}
		}
		// check if file exists and is not full
		this.prepareFile(); // may throw FileNotFoundException
		this.pos.println(sb.toString());
	}

	/**
	 * Determines and sets a new filename
	 */
	private final void prepareFile() throws FileNotFoundException {
		if (++this.entriesInCurrentFileCounter > FsWriterThread.MAXENTRIESINFILE) {
			if (this.pos != null) {
				this.pos.close();
			}
			this.entriesInCurrentFileCounter = 1;
			final DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd'-'HHmmssSS", Locale.US);
			dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
			final String dateStr = dateFormat.format(new java.util.Date()); // NOPMD (Date)
			final String filename = this.filenamePrefix + "-" + dateStr + "-UTC-" + this.getName() + ".dat";
			if (this.autoflush) {
				this.pos = new PrintWriter(new OutputStreamWriter(new FileOutputStream(filename)), true);
			} else {
				this.pos = new PrintWriter(new BufferedWriter(new OutputStreamWriter(new FileOutputStream(filename))), false);
			}
			this.pos.flush();
		}
	}

	@Override
	protected void cleanup() {
		if (this.pos != null) {
			this.pos.close();
		}
	}

	@Override
	public final String toString() {
		final StringBuilder sb = new StringBuilder();
		sb.append(super.toString());
		sb.append("; Writing to Directory: '");
		sb.append(this.path);
		sb.append("'");
		return sb.toString();
	}
}
