package kieker.monitoring.writer.filesystem;

import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.TimeZone;

import kieker.common.record.IMonitoringRecord;
import kieker.monitoring.core.configuration.Configuration;
import kieker.monitoring.writer.AbstractMonitoringWriter;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/*
 * ==================LICENCE=========================
 * Copyright 2006-2011 Kieker Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * ==================================================
 */
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
 * 
 *         History: 2008/01/04: Refactoring for the first release of Kieker and
 *         publication under an open source license 2007/03/13: Refactoring
 *         2006/12/20: Initial Prototype
 */
public final class SyncFsWriter extends AbstractMonitoringWriter {
	private static final Log log = LogFactory.getLog(SyncFsWriter.class);

	private static final String PREFIX = SyncFsWriter.class.getName() + ".";
	private static final String PATH = SyncFsWriter.PREFIX + "customStoragePath";
	private static final String TEMP = SyncFsWriter.PREFIX + "storeInJavaIoTmpdir";

	// configuration parameters
	private static final int maxEntriesInFile = 25000;

	// internal variables
	private String filenamePrefix;
	private MappingFileWriter mappingFileWriter;
	private PrintWriter pos = null;
	private int entriesInCurrentFileCounter = SyncFsWriter.maxEntriesInFile + 1; // Force to
																	// initialize
																	// first
																	// file!
	// only to get that information later
	private String path;

	public SyncFsWriter(final Configuration configuration) {
		super(configuration);
	}

	@Override
	protected void init() {
		String path;
		if (this.configuration.getBooleanProperty(SyncFsWriter.TEMP)) {
			path = System.getProperty("java.io.tmpdir");
		} else {
			path = this.configuration.getStringProperty(SyncFsWriter.PATH);
		}
		File f = new File(path);
		if (!f.isDirectory()) {
			SyncFsWriter.log.error("'" + path + "' is not a directory.");
			throw new IllegalArgumentException("'" + path
					+ "' is not a directory.");
		}
		final String ctrlName =
				this.getController().getControllerConfig().getHostName() + "-"
						+ this.getController().getControllerConfig().getName();

		final DateFormat m_ISO8601UTC =
				new SimpleDateFormat("yyyyMMdd'-'HHmmssSS");
		m_ISO8601UTC.setTimeZone(TimeZone.getTimeZone("UTC"));
		final String dateStr = m_ISO8601UTC.format(new java.util.Date());
		path =
				path + File.separatorChar + "kieker-" + dateStr + "-UTC-"
						+ ctrlName + File.separatorChar;
		f = new File(path);
		if (!f.mkdir()) {
			SyncFsWriter.log.error("Failed to create directory '" + path + "'");
			throw new IllegalArgumentException("Failed to create directory '"
					+ path + "'");
		}
		this.filenamePrefix = path + File.separatorChar + "kieker";
		this.path = f.getAbsolutePath();

		final String mappingFileFn = path + File.separatorChar + "kieker.map";
		try {
			this.mappingFileWriter = new MappingFileWriter(mappingFileFn);
		} catch (final Exception ex) {
			SyncFsWriter.log.error("Failed to create mapping file '"
					+ mappingFileFn + "'", ex);
			throw new IllegalArgumentException(
					"Failed to create mapping file '" + mappingFileFn + "'", ex);
		}
	}

	// TODO: keep track of record type ID mapping!
	@Override
	public final boolean newMonitoringRecord(
			final IMonitoringRecord monitoringRecord) {

		final StringBuilder sb = new StringBuilder();
		final Object[] recordFields = monitoringRecord.toArray();
		final int LAST_FIELD_INDEX = recordFields.length - 1;
		// check if file exists and is not full

		sb.append("$");
		final int idForRecordType =
				this.mappingFileWriter.idForRecordTypeClass(monitoringRecord
						.getClass());
		sb.append(idForRecordType);
		sb.append(';');
		sb.append(monitoringRecord.getLoggingTimestamp());
		if (LAST_FIELD_INDEX > 0) {
			sb.append(';');
		}
		for (int i = 0; i <= LAST_FIELD_INDEX; i++) {
			final Object val = recordFields[i];
			// TODO: assert that val!=null and provide suitable log msg if null
			sb.append(val);
			if (i < LAST_FIELD_INDEX) {
				sb.append(';');
			}
		}
		try {
			synchronized (this.pos) {
				this.prepareFile(); // may throw FileNotFoundException
				this.pos.println(sb);
			}
		} catch (final IOException ex) {
			SyncFsWriter.log.error("Failed to write data", ex);
			return false;
		}
		return true;
	}

	@Override
	public final void terminate() {
		if (this.pos != null) {
			this.pos.flush();
			this.pos.close();
		}
		SyncFsWriter.log.info("Writer: SyncFsWriter shutdown complete");
	}

	@Override
	public final String getInfoString() {
		final StringBuilder sb = new StringBuilder();
		sb.append(super.getInfoString());
		sb.append("\n\tWriting to Directory: '");
		sb.append(this.path);
		sb.append("'");
		return sb.toString();
	}

	/**
	 * Determines and sets a new Filename
	 */
	private final void prepareFile() throws FileNotFoundException {
		if (this.entriesInCurrentFileCounter++ > SyncFsWriter.maxEntriesInFile) {
			if (this.pos != null) {
				this.pos.flush();
				this.pos.close();
			}
			this.entriesInCurrentFileCounter = 0;

			final DateFormat m_ISO8601UTC =
					new SimpleDateFormat("yyyyMMdd'-'HHmmssSS");
			m_ISO8601UTC.setTimeZone(TimeZone.getTimeZone("UTC"));
			final String dateStr = m_ISO8601UTC.format(new java.util.Date());
			// TODO: where does this number come from?
			// final int random = (new Random()).nextInt(100);
			final String filename =
					this.filenamePrefix + "-" + dateStr + "-UTC.dat";
			this.pos =
					new PrintWriter(new DataOutputStream(
							new BufferedOutputStream(new FileOutputStream(
									filename))));
		}
	}
}
