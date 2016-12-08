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

import java.io.PrintWriter;

import kieker.common.configuration.Configuration;
import kieker.common.record.IMonitoringRecord;
import kieker.monitoring.writernew.AbstractMonitoringWriter;

/**
 * @author Christian Wulf (chw)
 *
 * @since 1.13
 */
public class AsciiFileWriter extends AbstractMonitoringWriter {

	private static final String PREFIX = AsciiFileWriter.class.getName() + ".";
	/** The name of the configuration for the custom storage path if the writer is advised not to store in the temporary directory. */
	public static final String CONFIG_PATH = PREFIX + "customStoragePath";
	/** The name of the configuration for the charset name (e.g. "UTF-8") */
	public static final String CONFIG_CHARSET_NAME = PREFIX + "charsetName";
	/** The name of the configuration determining the maximal number of entries in a file. */
	public static final String CONFIG_MAXENTRIESINFILE = PREFIX + "maxEntriesInFile";
	/** The name of the configuration determining the maximal size of the files in MiB. */
	public static final String CONFIG_MAXLOGSIZE = PREFIX + "maxLogSize"; // in MiB
	/** The name of the configuration determining the maximal number of log files. */
	public static final String CONFIG_MAXLOGFILES = PREFIX + "maxLogFiles";
	/** The name of the configuration determining whether to store the data in the temporary directory or not. */
	private static final String CONFIG_TEMP = PREFIX + "storeInJavaIoTmpdir";

	private final FileWriterPool fileWriterPool;

	public AsciiFileWriter(final Configuration configuration) {
		super(configuration);
		final String folder = configuration.getStringProperty(CONFIG_PATH);
		final String charsetName = configuration.getStringProperty(CONFIG_CHARSET_NAME);
		final int maxEntriesInFile = configuration.getIntProperty(CONFIG_MAXENTRIESINFILE);
		// this.configMaxlogSize = configuration.getIntProperty(CONFIG_MAXLOGSIZE);
		// this.configMaxLogFiles = configuration.getIntProperty(CONFIG_MAXLOGFILES);
		this.fileWriterPool = new FileWriterPool(folder, charsetName, maxEntriesInFile);
	}

	@Override
	public void onStarting() {}

	@Override
	public void writeMonitoringRecord(final IMonitoringRecord record) {
		final PrintWriter fileWriter = this.fileWriterPool.getFileWriter();

		fileWriter.print('$');
		// ...
		fileWriter.println();
	}

	@Override
	public void onTerminating() {}

}
