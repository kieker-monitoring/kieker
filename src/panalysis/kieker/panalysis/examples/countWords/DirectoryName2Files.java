/***************************************************************************
 * Copyright 2014 Kieker Project (http://kieker-monitoring.net)
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

package kieker.panalysis.examples.countWords;

import java.io.File;

import kieker.panalysis.base.AbstractDefaultFilter;
import kieker.panalysis.base.IInputPort;
import kieker.panalysis.base.IOutputPort;

/**
 * @author Christian Wulf
 * 
 * @since 1.10
 */
public class DirectoryName2Files extends AbstractDefaultFilter<DirectoryName2Files> {

	public final IInputPort<DirectoryName2Files, String> DIRECTORY_NAME = this.createInputPort();

	public final IOutputPort<DirectoryName2Files, File> FILE = this.createOutputPort();

	private long overallDuration;
	private int numFiles = 0;

	public boolean execute() {
		final long start = System.currentTimeMillis();

		final String inputDir = this.tryTake(this.DIRECTORY_NAME);
		if (inputDir == null) {
			return false;
		}

		final File[] availableFiles = new File(inputDir).listFiles();
		for (final File file : availableFiles) {
			if (file.isFile()) {
				// this.logger.info("Sending " + file);
				this.put(this.FILE, file);
				this.numFiles++;
			}
		}

		final long end = System.currentTimeMillis();
		final long duration = end - start;
		this.overallDuration += duration;

		return true;
	}

	public long getOverallDuration() {
		return this.overallDuration;
	}

	public int getNumFiles() {
		return this.numFiles;
	}

}
