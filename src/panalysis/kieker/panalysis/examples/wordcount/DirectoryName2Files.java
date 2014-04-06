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

package kieker.panalysis.examples.wordcount;

import java.io.File;

import kieker.panalysis.base.AbstractFilter;

/**
 * @author Christian Wulf
 * 
 * @since 1.10
 */
public class DirectoryName2Files extends AbstractFilter<DirectoryName2Files.INPUT_PORT, DirectoryName2Files.OUTPUT_PORT> {

	public static enum INPUT_PORT { // NOCS
		DIRECTORY_NAME
	}

	public static enum OUTPUT_PORT { // NOCS
		FILE
	}

	private long overallDuration;
	private int numFiles = 0;

	public DirectoryName2Files() {
		super(INPUT_PORT.class, OUTPUT_PORT.class);
	}

	public boolean execute() {
		final long start = System.currentTimeMillis();

		final Object token = this.tryTake(INPUT_PORT.DIRECTORY_NAME);
		if (token == null) {
			return false;
		}
		final String inputDir = (String) token;

		final File[] availableFiles = new File(inputDir).listFiles();
		for (final File file : availableFiles) {
			if (file.isFile()) {
				// this.logger.info("Sending " + file);
				this.put(OUTPUT_PORT.FILE, file);
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
