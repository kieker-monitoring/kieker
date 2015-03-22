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
import java.util.LinkedList;
import java.util.Locale;
import java.util.TimeZone;

import kieker.common.configuration.Configuration;
import kieker.common.logging.Log;
import kieker.common.logging.LogFactory;
import kieker.common.record.IMonitoringRecord;
import kieker.common.record.misc.RegistryRecord;
import kieker.common.util.filesystem.FSUtil;
import kieker.monitoring.writer.AbstractMonitoringWriter;
import kieker.monitoring.writer.filesystem.map.MappingFileWriter;

/**
 * Simple class to store monitoring data in the file system. Although a buffered
 * writer is used, outliers (delays of 1000 ms) occur from time to time if many
 * monitoring events have to be written. We believe that outliers result from a
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
 * @author Matthias Rohr, Andre van Hoorn, Jan Waller
 * 
 * @since < 0.9
 */
public final class SyncFsWriter extends AbstractMonitoringWriter {
	private static final String PREFIX = SyncFsWriter.class.getName() + ".";
	public static final String CONFIG_PATH = PREFIX + "customStoragePath"; // NOCS (afterPREFIX)
	public static final String CONFIG_MAXENTRIESINFILE = PREFIX + "maxEntriesInFile"; // NOCS (afterPREFIX)
	public static final String CONFIG_MAXLOGSIZE = PREFIX + "maxLogSize"; // NOCS (afterPREFIX)
	public static final String CONFIG_MAXLOGFILES = PREFIX + "maxLogFiles"; // NOCS (afterPREFIX)
	public static final String CONFIG_FLUSH = PREFIX + "flush"; // NOCS (afterPREFIX)
	public static final String CONFIG_BUFFER = PREFIX + "bufferSize"; // NOCS (afterPREFIX)
	private static final String CONFIG_TEMP = PREFIX + "storeInJavaIoTmpdir"; // NOCS (afterPREFIX)

	private static final Log LOG = LogFactory.getLog(SyncFsWriter.class);

	// internal variables
	private final boolean autoflush;
	private final int bufferSize;
	private final int maxEntriesInFile;
	private final long maxLogSize;
	private final int maxLogFiles;
	private final String configPath;

	// only access within synchronized
	private int entriesInCurrentFileCounter;

	private final LinkedList<FileNameSize> listOfLogFiles; // NOCS NOPMD (we explicitly need LinekdList here)
	private long totalLogSize;

	private String path;
	private MappingFileWriter mappingFileWriter;
	private PrintWriter pos;

	private final DateFormat dateFormat;

	private long previousFileDate; // only used in synchronized
	private long sameFilenameCounter; // only used in synchronized

	/**
	 * Creates a new instance of this class using the given configuration to initialize the class.
	 * 
	 * @param configuration
	 *            The configuration used to initialize this writer.
	 * 
	 * @throws IllegalArgumentException
	 *             If the given configuration is invalid.
	 */
	public SyncFsWriter(final Configuration configuration) throws IllegalArgumentException {
		super(configuration);
		this.autoflush = configuration.getBooleanProperty(CONFIG_FLUSH);
		this.bufferSize = configuration.getIntProperty(CONFIG_BUFFER);
		// get number of entries per file
		this.maxEntriesInFile = configuration.getIntProperty(CONFIG_MAXENTRIESINFILE);
		if (this.maxEntriesInFile < 1) {
			throw new IllegalArgumentException(CONFIG_MAXENTRIESINFILE + " must be greater than 0 but is '" + this.maxEntriesInFile + "'");
		}
		final int configMaxLogSize = configuration.getIntProperty(CONFIG_MAXLOGSIZE);
		final int configMaxLogFiles = configuration.getIntProperty(CONFIG_MAXLOGFILES);
		if ((configMaxLogSize > 0) || (configMaxLogFiles > 0)) {
			this.maxLogSize = configMaxLogSize * 1024L * 1024L; // convert from MiBytes to Bytes
			this.maxLogFiles = configMaxLogFiles;
			this.listOfLogFiles = new LinkedList<FileNameSize>();
		} else {
			this.maxLogSize = -1;
			this.maxLogFiles = -1;
			this.listOfLogFiles = null; // NOPMD (set explicitly to null)
		}
		this.entriesInCurrentFileCounter = this.maxEntriesInFile;
		// initialize Date
		this.dateFormat = new SimpleDateFormat("yyyyMMdd'-'HHmmssSSS", Locale.US);
		this.dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
		// Determine path
		if (configuration.getBooleanProperty(CONFIG_TEMP)) {
			LOG.warn("Using deprecated configuration property " + CONFIG_TEMP + ". Instead use empty value for " + CONFIG_PATH);
		}
		String pathTmp = configuration.getStringProperty(CONFIG_PATH);
		if (pathTmp.length() == 0) {
			pathTmp = System.getProperty("java.io.tmpdir");
		}
		if (!(new File(pathTmp)).isDirectory()) {
			throw new IllegalArgumentException("'" + pathTmp + "' is not a directory.");
		}
		this.configPath = pathTmp;
	}

	@Override
	protected void init() throws IllegalArgumentException, IOException {
		// Determine directory for files
		final String ctrlName = super.monitoringController.getHostname() + "-" + super.monitoringController.getName();
		final String dateStr = this.dateFormat.format(new java.util.Date()); // NOPMD (Date)
		final StringBuffer sb = new StringBuffer(this.configPath.length() + FSUtil.FILE_PREFIX.length() + ctrlName.length() + 26);
		sb.append(this.configPath).append(File.separatorChar).append(FSUtil.FILE_PREFIX).append('-').append(dateStr).append("-UTC-").append(ctrlName)
				.append(File.separatorChar);
		final String pathTmp = sb.toString();
		final File f = new File(pathTmp);
		if (!f.mkdir()) {
			throw new IllegalArgumentException("Failed to create directory '" + pathTmp + "'");
		}
		synchronized (this) { // visibility
			this.path = f.getAbsolutePath();
			this.mappingFileWriter = new MappingFileWriter(this.path);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	@SuppressWarnings("unchecked")
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
			sb.append(this.monitoringController.getUniqueIdForString(monitoringRecord.getClass().getName()));
			sb.append(';');
			sb.append(monitoringRecord.getLoggingTimestamp());
			for (final Object recordField : recordFields) {
				sb.append(';');
				sb.append(recordField);
			}
			try {
				synchronized (this) { // we must not synch on pos, it changes within!
					if (++this.entriesInCurrentFileCounter > this.maxEntriesInFile) { // NOPMD
						final String filename = this.getFilename();
						this.prepareFile(filename); // may throw FileNotFoundException
						if (this.listOfLogFiles != null) {
							if (!this.listOfLogFiles.isEmpty()) {
								final FileNameSize fns = this.listOfLogFiles.getLast();
								final long filesize = new File(fns.name).length();
								fns.size = filesize;
								this.totalLogSize += filesize;
							}
							this.listOfLogFiles.add(new FileNameSize(filename));
							if ((this.maxLogFiles > 0) && (this.listOfLogFiles.size() > this.maxLogFiles)) { // too many files (at most one!)
								final FileNameSize removeFile = this.listOfLogFiles.removeFirst();
								if (!new File(removeFile.name).delete()) { // NOCS NOPMD (nested if)
									throw new IOException("Failed to delete file " + removeFile.name);
								}
								this.totalLogSize -= removeFile.size;
							}
							if (this.maxLogSize > 0) {
								while ((this.listOfLogFiles.size() > 1) && (this.totalLogSize > this.maxLogSize)) {
									final FileNameSize removeFile = this.listOfLogFiles.removeFirst();
									if (!new File(removeFile.name).delete()) { // NOCS NOPMD (nested if)
										throw new IOException("Failed to delete file " + removeFile.name);
									}
									this.totalLogSize -= removeFile.size;
								}
							}
						}
					}
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
	 * Determines and sets a new filename.
	 * 
	 * @param filename
	 *            The name of the file to prepare.
	 * 
	 * @throws FileNotFoundException
	 *             If the given file is somehow invalid.
	 * @throws UnsupportedEncodingException
	 *             If the encoding is not supported.
	 */
	private final void prepareFile(final String filename) throws FileNotFoundException, UnsupportedEncodingException {
		this.entriesInCurrentFileCounter = 1;
		if (this.pos != null) {
			this.pos.close();
		}
		if (this.autoflush) {
			this.pos = new PrintWriter(new OutputStreamWriter(new FileOutputStream(filename), FSUtil.ENCODING), true);
		} else {
			this.pos = new PrintWriter(new BufferedWriter(new OutputStreamWriter(new FileOutputStream(filename), FSUtil.ENCODING), this.bufferSize), false);
		}
		this.pos.flush();
	}

	/**
	 * May only be used in synchronized blocks!
	 */
	private final String getFilename() {
		final long date = System.currentTimeMillis();
		if (this.previousFileDate == date) {
			this.sameFilenameCounter++;
		} else {
			this.sameFilenameCounter = 0;
			this.previousFileDate = date;
		}
		final StringBuilder sb = new StringBuilder(this.path.length() + FSUtil.FILE_PREFIX.length() + 31);
		sb.append(this.path).append(File.separatorChar).append(FSUtil.FILE_PREFIX).append(this.dateFormat.format(new java.util.Date(date))) // NOPMD (date)
				.append("-UTC-").append(String.format("%03d", this.sameFilenameCounter)).append(FSUtil.NORMAL_FILE_EXTENSION);
		return sb.toString();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
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
		final StringBuilder sb = new StringBuilder(128);
		sb.append(super.toString());
		sb.append("\n\tWriting to Directory: '");
		sb.append(this.path);
		sb.append('\'');
		return sb.toString();
	}

	/**
	 * @author Jan Waller
	 */
	private static final class FileNameSize {
		public final String name; // NOCS
		public long size; // NOCS

		public FileNameSize(final String name) {
			this.name = name;
		}
	}
}
