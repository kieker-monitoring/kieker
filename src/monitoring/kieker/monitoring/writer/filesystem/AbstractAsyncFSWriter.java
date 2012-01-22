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

import java.io.File;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Locale;
import java.util.TimeZone;
import java.util.concurrent.BlockingQueue;

import kieker.common.configuration.Configuration;
import kieker.common.record.IMonitoringRecord;
import kieker.monitoring.core.controller.IMonitoringController;
import kieker.monitoring.writer.AbstractAsyncWriter;
import kieker.monitoring.writer.filesystem.async.AbstractFsWriterThread;

/**
 * @author Matthias Rohr, Andre van Hoorn, Jan Waller, Robert von Massow
 */
public abstract class AbstractAsyncFSWriter extends AbstractAsyncWriter {
	public static final String CONFIG_PATH = "customStoragePath";
	public static final String CONFIG_TEMP = "storeInJavaIoTmpdir";
	public static final String CONFIG_MAXENTRIESINFILE = "maxEntriesInFile";

	protected AbstractAsyncFSWriter(final Configuration configuration) {
		super(configuration);
	}

	/**
	 * Make sure that the required properties always have default values!
	 */
	@Override
	protected Configuration getDefaultConfiguration() {
		final Configuration configuration = new Configuration(super.getDefaultConfiguration());
		final String prefix = this.getClass().getName() + "."; // can't use this.prefix, maybe uninitialized
		configuration.setProperty(prefix + AbstractAsyncFSWriter.CONFIG_PATH, ".");
		configuration.setProperty(prefix + AbstractAsyncFSWriter.CONFIG_TEMP, "true");
		configuration.setProperty(prefix + AbstractAsyncFSWriter.CONFIG_MAXENTRIESINFILE, "25000");
		return configuration;
	}

	@Override
	protected final void init() throws Exception {
		final String prefix = this.getClass().getName() + '.';
		// Determine path
		String path;
		if (this.configuration.getBooleanProperty(prefix + AbstractAsyncFSWriter.CONFIG_TEMP)) {
			path = System.getProperty("java.io.tmpdir");
		} else {
			path = this.configuration.getStringProperty(prefix + AbstractAsyncFSWriter.CONFIG_PATH);
		}
		File f = new File(path);
		if (!f.isDirectory()) {
			throw new IllegalArgumentException("'" + path + "' is not a directory.");
		}
		// Determine directory for files
		final String ctrlName = super.monitoringController.getHostName() + "-" + super.monitoringController.getName();
		final DateFormat date = new SimpleDateFormat("yyyyMMdd'-'HHmmssSSS", Locale.US);
		date.setTimeZone(TimeZone.getTimeZone("UTC"));
		final String dateStr = date.format(new java.util.Date()); // NOPMD (Date)
		final StringBuffer sb = new StringBuffer(path.length() + ctrlName.length() + 32); // NOCS (MagicNumber)
		sb.append(path).append(File.separatorChar).append("kieker-").append(dateStr).append("-UTC-").append(ctrlName).append(File.separatorChar);
		path = sb.toString();
		f = new File(path);
		if (!f.mkdir()) {
			throw new IllegalArgumentException("Failed to create directory '" + path + "'");
		}
		// get number of entries per file
		final int maxEntriesInFile = this.configuration.getIntProperty(prefix + AbstractAsyncFSWriter.CONFIG_MAXENTRIESINFILE);
		if (maxEntriesInFile < 1) {
			throw new IllegalArgumentException(prefix + AbstractAsyncFSWriter.CONFIG_MAXENTRIESINFILE + " must be greater than 0 but is '" + maxEntriesInFile + "'");
		}
		// Mapping file
		final MappingFileWriter mappingFileWriter = new MappingFileWriter(path);
		// Create writer thread
		this.addWorker(this.initWorker(super.monitoringController, this.blockingQueue, mappingFileWriter, path, maxEntriesInFile));
	}

	protected abstract AbstractFsWriterThread initWorker(final IMonitoringController monitoringController, final BlockingQueue<IMonitoringRecord> writeQueue,
			final MappingFileWriter mappingFileWriter, final String path, final int maxEntiresInFile);
}
