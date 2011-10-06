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
import java.util.TimeZone;

import kieker.common.record.IMonitoringRecord;
import kieker.monitoring.core.configuration.Configuration;
import kieker.monitoring.writer.AbstractMonitoringWriter;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Simple class to store monitoring data in the file system. Although a buffered
 * writer is used, outliers (delays of 1000 ms) occur from time to time if many
 * monitoring events have to be writen. We believe that outliers result from a
 * flush on the buffer of the writer.
 * 
 * A more sophisticated writer to store data in the file system is the
 * AsyncFsWriter. This does not introduce the outliers that result from flushing
 * the writing buffer, since provides an asynchronous insertMonitoringData
 * method. However, the AsyncFsWriter introduces a little more overhead because
 * a writing queue is required and it isn't tested as much as the
 * FileSystenWriter. Additionally, the resource demands (CPU, bus etc.) for
 * writing monitoring data are not anymore occurring during the time of the
 * execution that is monitored, but at some other (unknown) time.
 * 
 * The AsyncFsWriter should usually be used instead of this class to avoid the
 * outliers described above.
 * 
 * The asyncFsWriter is not(!) faster (but also it shouldn't be much slower)
 * because only one thread is used for writing into a single file. To tune it,
 * it might be an option to write to multiple files, while writing with more
 * than one thread into a single file is not considered a save option.
 * 
 * @author Matthias Rohr, Andre van Hoorn, Jan Waller
 */
public final class SyncFsWriter extends AbstractMonitoringWriter {
	private static final Log LOG = LogFactory.getLog(SyncFsWriter.class);

	private static final String PREFIX = SyncFsWriter.class.getName() + ".";
	public static final String CONFIG__PATH = SyncFsWriter.PREFIX + "customStoragePath";
	public static final String CONFIG__TEMP = SyncFsWriter.PREFIX + "storeInJavaIoTmpdir";
	public static final String CONFIG__FLUSH = SyncFsWriter.PREFIX + "flush";

	// configuration parameters
	private static final int MAXENTRIESINFILE = 25000;

	// internal variables
	private String filenamePrefix;
	private boolean autoflush;
	private MappingFileWriter mappingFileWriter;
	private PrintWriter pos = null;
	// Force to initialize first file!
	private int entriesInCurrentFileCounter = SyncFsWriter.MAXENTRIESINFILE;
	// only to get that information later
	private String path;

	public SyncFsWriter(final Configuration configuration) {
		super(configuration);
	}

	@Override
	protected void init() {
		this.autoflush = this.configuration.getBooleanProperty(SyncFsWriter.CONFIG__FLUSH);
		String path;
		if (this.configuration.getBooleanProperty(SyncFsWriter.CONFIG__TEMP)) {
			path = System.getProperty("java.io.tmpdir");
		} else {
			path = this.configuration.getStringProperty(SyncFsWriter.CONFIG__PATH);
		}
		File f = new File(path);
		if (!f.isDirectory()) {
			SyncFsWriter.LOG.error("'" + path + "' is not a directory.");
			throw new IllegalArgumentException("'" + path + "' is not a directory.");
		}
		final String ctrlName = super.monitoringController.getHostName() + "-" + super.monitoringController.getName();

		final DateFormat dateFormat_ISO8601UTC = new SimpleDateFormat("yyyyMMdd'-'HHmmssSS"); // NOCS
		dateFormat_ISO8601UTC.setTimeZone(TimeZone.getTimeZone("UTC"));
		final String dateStr = dateFormat_ISO8601UTC.format(new java.util.Date());
		path = path + File.separatorChar + "kieker-" + dateStr + "-UTC-" + ctrlName + File.separatorChar;
		f = new File(path);
		if (!f.mkdir()) {
			final String errorMsg = "Failed to create directory '" + path + "'";
			SyncFsWriter.LOG.error(errorMsg);
			throw new IllegalArgumentException(errorMsg);
		}
		this.filenamePrefix = path + File.separatorChar + "kieker";
		this.path = f.getAbsolutePath();
		final String mappingFileFn = path + File.separatorChar + "kieker.map";
		try {
			this.mappingFileWriter = new MappingFileWriter(mappingFileFn);
		} catch (final Exception ex) {
			SyncFsWriter.LOG.error("Failed to create mapping file '" + mappingFileFn + "'");
			throw new IllegalArgumentException("Failed to create mapping file '" + mappingFileFn + "'", ex);
		}
	}

	@Override
	public final boolean newMonitoringRecord(final IMonitoringRecord monitoringRecord) {
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
		try {
			synchronized (this) { // we must not synch on pos, it changes within!
				this.prepareFile(); // may throw FileNotFoundException
				this.pos.println(sb);
			}
		} catch (final IOException ex) {
			SyncFsWriter.LOG.error("Failed to write monitoring record", ex);
			return false;
		}
		return true;
	}

	/**
	 * Determines and sets a new filename
	 */
	private final void prepareFile() throws FileNotFoundException {
		if (++this.entriesInCurrentFileCounter > SyncFsWriter.MAXENTRIESINFILE) {
			if (this.pos != null) {
				this.pos.close();
			}
			this.entriesInCurrentFileCounter = 1;

			final DateFormat dateFormat_ISO8601UTC = new SimpleDateFormat("yyyyMMdd'-'HHmmssSS"); // NOCS
			dateFormat_ISO8601UTC.setTimeZone(TimeZone.getTimeZone("UTC"));
			final String dateStr = dateFormat_ISO8601UTC.format(new java.util.Date());
			final String filename = this.filenamePrefix + "-" + dateStr + "-UTC.dat";
			if (this.autoflush) {
				this.pos = new PrintWriter(new OutputStreamWriter(new FileOutputStream(filename)), true);
			} else {
				this.pos = new PrintWriter(new BufferedWriter(new OutputStreamWriter(new FileOutputStream(filename))), false);
			}
			this.pos.flush();
		}
	}

	@Override
	public final void terminate() {
		synchronized (this) {
			if (this.pos != null) {
				this.pos.close();
			}
		}
		SyncFsWriter.LOG.info("Writer: SyncFsWriter shutdown complete");
	}

	@Override
	public final String toString() {
		final StringBuilder sb = new StringBuilder();
		sb.append(super.toString());
		sb.append("\n\tWriting to Directory: '");
		sb.append(this.path);
		sb.append("'");
		return sb.toString();
	}
}
