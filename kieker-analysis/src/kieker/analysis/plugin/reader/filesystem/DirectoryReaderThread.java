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

package kieker.analysis.plugin.reader.filesystem;

import java.io.File;
import java.util.Arrays;
import java.util.Comparator;

import kieker.common.exception.RecordInstantiationException;
import kieker.common.logging.Log;
import kieker.common.util.filesystem.FileExtensionFilter;

/**
 * @author Christian Wulf
 *
 * @since 1.13
 */
abstract class DirectoryReaderThread extends Thread {

	private final Log LOG; // NOPMD (private log instance passed by ctor)
	private final File inputDir;

	public DirectoryReaderThread(final Log logger, final File inputDir) {
		super();
		this.LOG = logger;
		this.inputDir = inputDir;
	}

	private volatile boolean shouldTerminate;

	@Override
	public final void run() {
		this.readMappingFile(); // must be the first line to set filePrefix!

		final FileExtensionFilter fileExtensionFilter = this.getFileExtensionFilter();

		final File[] inputFiles = this.inputDir.listFiles(fileExtensionFilter);
		if (inputFiles == null) {
			this.LOG.error("Directory '" + this.inputDir + "' does not exist or an I/O error occured.");
		} else if (inputFiles.length == 0) {
			// level 'warn' for this case, because this is not unusual for large monitoring logs including a number of directories
			this.LOG.warn("Directory '" + this.inputDir + "' contains no Kieker log files.");
		} else { // everything ok, we process the files
			Arrays.sort(inputFiles, new Comparator<File>() {

				@Override
				public final int compare(final File f1, final File f2) {
					return f1.compareTo(f2); // simplified (we expect no dirs!)
				}
			});
			for (final File inputFile : inputFiles) {
				if (this.shouldTerminate) {
					this.LOG.info("Shutting down DirectoryReader.");
					break;
				}

				this.LOG.info("< Loading " + inputFile.getAbsolutePath());

				try {
					this.processNormalInputFile(inputFile);
				} catch (final RecordInstantiationException e) {
					break;
				}
			}
		}

		this.onEndOfRun();
	}

	protected abstract FileExtensionFilter getFileExtensionFilter();

	protected abstract void readMappingFile();

	protected abstract void processNormalInputFile(File inputFile);

	protected abstract void onEndOfRun();

	public void terminate() {
		this.shouldTerminate = true;
		this.interrupt();
	}
}
