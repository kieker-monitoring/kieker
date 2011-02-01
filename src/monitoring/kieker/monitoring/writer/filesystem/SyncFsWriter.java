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
import java.util.Random;
import java.util.TimeZone;

import kieker.common.record.IMonitoringRecord;
import kieker.monitoring.core.configuration.ConfigurationFileConstants;
import kieker.monitoring.writer.IMonitoringLogWriter;

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
 * @author Matthias Rohr, Andre van Hoorn
 * 
 *         History:
 *         2008/01/04: Refactoring for the first release of Kieker and
 *                     publication under an open source license
 *         2007/03/13: Refactoring
 *         2006/12/20: Initial Prototype
 */
public final class SyncFsWriter implements IMonitoringLogWriter {

	private static final Log log = LogFactory.getLog(SyncFsWriter.class);
	// configuration parameters
	private static final int maxEntriesInFile = 22000;
	// internal variables
	private final String storagePathBase;
	private final String storagePathPostfix;
	private boolean filenameInitialized = false;
	private int entriesInCurrentFileCounter = 0;
	private PrintWriter pos = null;

	private final MappingFileWriter mappingFileWriter;

	private final static String defaultConstructionErrorMsg = 
		"Do not select this writer using the fully qualified classname. "
			+ "Use the the constant "
			+ ConfigurationFileConstants.WRITER_SYNCFS
			+ " and the file system specific configuration properties.";

	public SyncFsWriter() {
		throw new UnsupportedOperationException(SyncFsWriter.defaultConstructionErrorMsg);
	}

	@Override
	public final boolean init(final String initString) {
		throw new UnsupportedOperationException(SyncFsWriter.defaultConstructionErrorMsg);
	}
	
	@Override
	public final void terminate() {
		SyncFsWriter.log.info("Writer: SyncFsWriter shutdown complete");
		this.pos.close();
	}

	public SyncFsWriter(final String storagePathBase, final String storagePathPostfix) {
		SyncFsWriter.log.debug("storagePathBase :" + storagePathBase);
		File f = new File(storagePathBase);
		if (!f.isDirectory()) {
			SyncFsWriter.log.error(storagePathBase + " is not a directory");
			SyncFsWriter.log.error("Will abort constructor.");
			throw new IllegalArgumentException(storagePathBase + " is not a directory");
		}

		this.storagePathPostfix = storagePathPostfix;

		final DateFormat m_ISO8601UTC = new SimpleDateFormat("yyyyMMdd'-'HHmmssSS");
		m_ISO8601UTC.setTimeZone(TimeZone.getTimeZone("UTC"));
		final String dateStr = m_ISO8601UTC.format(new java.util.Date());
		this.storagePathBase = storagePathBase + "/tpmon-" + dateStr + "-UTC-" + this.storagePathPostfix + "/";
		SyncFsWriter.log.debug("this.storagePathBase :" + this.storagePathBase);

		f = new File(this.storagePathBase);
		if (!f.mkdir()) {
			SyncFsWriter.log.error("Failed to create directory '" + this.storagePathBase + "'");
			SyncFsWriter.log.error("Will abort constructor.");
			throw new IllegalArgumentException("Failed to create directory '" + this.storagePathBase + "'");
		}
		SyncFsWriter.log.debug("Directory for monitoring data: " + this.storagePathBase);

		final String mappingFileFn = this.storagePathBase + File.separatorChar + "tpmon.map";
		try {
			this.mappingFileWriter = new MappingFileWriter(mappingFileFn);
		} catch (final Exception exc) {
			SyncFsWriter.log.error("Failed to create mapping file '" + mappingFileFn + "'", exc);
			SyncFsWriter.log.error("Will abort init().");
			throw new IllegalArgumentException("Failed to create mapping file '" + mappingFileFn + "'", exc);
		}
	}

	/**
	 * Determines and sets a new Filename
	 */
	private final void prepareFile() throws FileNotFoundException {
		if ((this.entriesInCurrentFileCounter++ > SyncFsWriter.maxEntriesInFile) || !this.filenameInitialized) {
			if (this.pos != null) {
				this.pos.close();
			}
			this.filenameInitialized = true;
			this.entriesInCurrentFileCounter = 0;

			final DateFormat m_ISO8601Local = new SimpleDateFormat("yyyyMMdd'-'HHmmssSS");
			m_ISO8601Local.setTimeZone(TimeZone.getTimeZone("UTC"));
			final String dateStr = m_ISO8601Local.format(new java.util.Date());
			// TODO: where does this number come from ??
			final int random = (new Random()).nextInt(100);
			final String filename = this.storagePathBase + "/tpmon-" + dateStr + "-UTC-" + random + ".dat";
			log.debug("** " + java.util.Calendar.getInstance().getTime().toString() + " new filename: " + filename);
			try {
				final FileOutputStream fos = new FileOutputStream(filename);
				final BufferedOutputStream bos = new BufferedOutputStream(fos);
				final DataOutputStream dos = new DataOutputStream(bos);
				this.pos = new PrintWriter(dos);
				this.pos.flush();
			} catch (final FileNotFoundException ex) {
				SyncFsWriter.log.error("Tpmon: Error creating the file: " + filename + " \n ", ex);
				throw ex;
			}
		}
	}

	// TODO: keep track of record type ID mapping!
	@Override
	public final synchronized boolean newMonitoringRecord(final IMonitoringRecord monitoringRecord) {
		try {
			final Object[] recordFields = monitoringRecord.toArray();
			final int LAST_FIELD_INDEX = recordFields.length - 1;
			this.prepareFile(); // may throw FileNotFoundException

			this.pos.write("$" + this.mappingFileWriter.idForRecordTypeClass(monitoringRecord.getClass()));
			this.pos.write(';');
			this.pos.write(Long.toString(monitoringRecord.getLoggingTimestamp()));
			if (LAST_FIELD_INDEX > 0) {
				this.pos.write(';');
			}
			for (int i = 0; i <= LAST_FIELD_INDEX; i++) {
				this.pos.write(recordFields[i].toString());
				if (i < LAST_FIELD_INDEX) {
					this.pos.write(';');
				}
			}
			this.pos.println();
			this.pos.flush();
		} catch (final IOException ex) {
			SyncFsWriter.log.error("Failed to write data", ex);
			return false;
		}
		return true;
	}

	@Override
	public final String getInfoString() {
		return "filenamePrefix :" + this.storagePathBase;
	}
}
