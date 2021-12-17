/***************************************************************************
 * Copyright 2021 Kieker Project (http://kieker-monitoring.net)
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
package kieker.analysis.source.file;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.List;

import teetime.framework.AbstractProducerStage;

/**
 * Scan a set of directories recursively for Kieker logs.
 * This producer stage outputs File objects representing valid Kieker log directories.
 *
 * @author Reiner Jung
 *
 * @since 1.15
 */
public class DirectoryScannerStage extends AbstractProducerStage<File> {

	private final List<File> directories;
	private final FilenameFilter filter = new MapFileFilter();
	private int numOfDirectories;

	/**
	 * Create a directory scanner.
	 *
	 * @param directories
	 *            array of directories to scan
	 */
	public DirectoryScannerStage(final List<File> directories) { // NOPMD passing array is not an issue here, read only
		this.directories = directories;
	}

	/**
	 * Create a directory scanner for a single directory.
	 *
	 * @param directory
	 *            the single directory
	 */
	public DirectoryScannerStage(final File directory) {
		this.directories = new ArrayList<>();
		this.directories.add(directory);
	}

	/**
	 * Execute the directory scanner.
	 */
	@Override
	protected void execute() {
		if (this.directories != null) {
			for (final File directory : this.directories) {
				this.logger.debug("Scanning directories in {} for Kieker logs", directory);
				if (directory.isDirectory()) {
					this.scanDirectory(directory);
				}
			}
		} else {
			this.logger.error("Cannot process an empty array of directories.");
		}
		this.logger.debug("Processed {} directories.", this.numOfDirectories);
		this.workCompleted();
	}

	private void scanDirectory(final File directory) {
		if (this.isKiekerDirectory(directory)) {
			this.logger.debug("Reading log data from {}", directory.getAbsolutePath());
			this.numOfDirectories++;
			this.getOutputPort().send(directory);
		} else {
			for (final File subDirectory : directory.listFiles()) { // NOFB is guaranteed to be a directory
				if (subDirectory.isDirectory()) {
					this.scanDirectory(subDirectory);
				}
			}
		}
	}

	private boolean isKiekerDirectory(final File directory) {
		if (directory.isDirectory()) {
			if (directory.exists()) { // NOPMD NOFB collapsing is not useful
				if (directory.listFiles(this.filter).length > 0) { // NOPMD NOFB collapsing is not useful
					return true;
				}
			}
		}
		return false;
	}

}
