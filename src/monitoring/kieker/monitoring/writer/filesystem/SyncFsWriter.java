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
import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.TimeZone;

import kieker.common.configuration.Configuration;
import kieker.common.logging.Log;
import kieker.common.logging.LogFactory;
import kieker.common.record.IMonitoringRecord;
import kieker.monitoring.core.registry.RegistryRecord;
import kieker.monitoring.writer.AbstractMonitoringWriter;

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
	private static final String PREFIX = SyncFsWriter.class.getName() + ".";
	public static final String CONFIG_PATH = PREFIX + "customStoragePath"; // NOCS (afterPREFIX)
	public static final String CONFIG_TEMP = PREFIX + "storeInJavaIoTmpdir"; // NOCS (afterPREFIX)
	public static final String CONFIG_MAXENTRIESINFILE = PREFIX + "maxEntriesInFile"; // NOCS (afterPREFIX)
	public static final String CONFIG_FLUSH = PREFIX + "flush"; // NOCS (afterPREFIX)

	private static final Log LOG = LogFactory.getLog(SyncFsWriter.class);

	private static final String ENCODING = "UTF-8";

	// internal variables
	private final boolean autoflush;
	private final int maxEntriesInFile;
	// only access within synchronized
	private int entriesInCurrentFileCounter;
	private MappingFileWriter mappingFileWriter;
	private String filenamePrefix;
	private String path;
	private PrintWriter pos = null;

	public SyncFsWriter(final Configuration configuration) throws IllegalArgumentException {
		super(configuration);
		this.autoflush = this.configuration.getBooleanProperty(CONFIG_FLUSH);
		// get number of entries per file
		this.maxEntriesInFile = this.configuration.getIntProperty(CONFIG_MAXENTRIESINFILE);
		if (this.maxEntriesInFile < 1) {
			throw new IllegalArgumentException(CONFIG_MAXENTRIESINFILE + " must be greater than 0 but is '" + this.maxEntriesInFile + "'");
		}
		this.entriesInCurrentFileCounter = this.maxEntriesInFile;
	}

	@Override
	protected void init() throws IllegalArgumentException, IOException {
		// Determine path
		String pathTmp;
		if (this.configuration.getBooleanProperty(CONFIG_TEMP)) {
			pathTmp = System.getProperty("java.io.tmpdir");
		} else {
			pathTmp = this.configuration.getStringProperty(CONFIG_PATH);
		}
		File f = new File(pathTmp);
		if (!f.isDirectory()) {
			throw new IllegalArgumentException("'" + pathTmp + "' is not a directory.");
		}
		// Determine directory for files
		final String ctrlName = super.monitoringController.getHostname() + "-" + super.monitoringController.getName();
		final DateFormat date = new SimpleDateFormat("yyyyMMdd'-'HHmmssSSS", Locale.US);
		date.setTimeZone(TimeZone.getTimeZone("UTC"));
		final String dateStr = date.format(new java.util.Date()); // NOPMD (Date)
		final StringBuffer sb = new StringBuffer(pathTmp.length() + ctrlName.length() + 32);
		sb.append(pathTmp).append(File.separatorChar).append("kieker-").append(dateStr).append("-UTC-").append(ctrlName).append(File.separatorChar);
		pathTmp = sb.toString();
		f = new File(pathTmp);
		if (!f.mkdir()) {
			throw new IllegalArgumentException("Failed to create directory '" + pathTmp + "'");
		}
		synchronized (this) { // visibility
			this.path = f.getAbsolutePath();
			this.filenamePrefix = this.path + File.separatorChar + "kieker";
			this.mappingFileWriter = new MappingFileWriter(this.path);
		}
	}

	public final boolean newMonitoringRecord(final IMonitoringRecord monitoringRecord) {
		if (monitoringRecord instanceof RegistryRecord) {
			try {
				this.mappingFileWriter.write((RegistryRecord) monitoringRecord);
			} catch (final IOException ex) {
				LOG.error("Failed to write monitoring record", ex);
				return false;
			}
		} else {
			final Object[] recordFields = monitoringRecord.toArray();
			final StringBuilder sb = new StringBuilder(256);
			sb.append('$');
			sb.append(this.monitoringController.getIdForString(monitoringRecord.getClass().getName()));
			sb.append(';');
			sb.append(monitoringRecord.getLoggingTimestamp());
			for (final Object recordField : recordFields) {
				sb.append(';');
				sb.append(recordField);
			}
			try {
				synchronized (this) { // we must not synch on pos, it changes within!
					this.prepareFile(); // may throw FileNotFoundException
					this.pos.println(sb.toString());
				}
			} catch (final IOException ex) {
				LOG.error("Failed to write monitoring record", ex);
				return false;
			}
		}
		return true;
	}

	/**
	 * Determines and sets a new filename
	 * 
	 * @throws UnsupportedEncodingException
	 */
	private final void prepareFile() throws FileNotFoundException, UnsupportedEncodingException {
		if (++this.entriesInCurrentFileCounter > this.maxEntriesInFile) {
			this.entriesInCurrentFileCounter = 1;
			if (this.pos != null) {
				this.pos.close();
			}
			if (this.autoflush) {
				this.pos = new PrintWriter(new OutputStreamWriter(new FileOutputStream(this.getFilename()), ENCODING), true);
			} else {
				this.pos = new PrintWriter(new BufferedWriter(new OutputStreamWriter(new FileOutputStream(this.getFilename()), ENCODING)), false);
			}
			this.pos.flush();
		}
	}

	private final String getFilename() {
		final DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd'-'HHmmssSSS", Locale.US);
		dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
		final StringBuilder sb = new StringBuilder(this.filenamePrefix.length() + 27);
		sb.append(this.filenamePrefix).append('-').append(dateFormat.format(new java.util.Date())).append("-UTC.dat"); // NOPMD (Date)
		return sb.toString();
	}

	public final void terminate() {
		synchronized (this) {
			if (this.pos != null) {
				this.pos.close();
			}
		}
		LOG.info("Writer: SyncFsWriter shutdown complete");
	}

	@Override
	public final String toString() {
		final StringBuilder sb = new StringBuilder();
		sb.append(super.toString());
		sb.append("\n\tWriting to Directory: '");
		sb.append(this.path);
		sb.append('\'');
		return sb.toString();
	}
}
