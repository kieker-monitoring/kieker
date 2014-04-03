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
import java.util.List;

import kieker.panalysis.base.AbstractSink;

/**
 * @author Christian Wulf
 * 
 * @since 1.10
 */
public class OutputWordsCountSink extends AbstractSink<OutputWordsCountSink.INPUT_PORT> {

	static public enum INPUT_PORT {
		FILE_WORDCOUNT_TUPLE
	}

	private long overallDuration;
	private int numFiles = 0;

	public OutputWordsCountSink() {
		super(INPUT_PORT.class);
	}

	public void execute() {
		final long start = System.currentTimeMillis();

		final List<?> tuple = (List<?>) this.take(INPUT_PORT.FILE_WORDCOUNT_TUPLE);
		final File file = (File) tuple.get(0);
		final Number wordsCount = (Number) tuple.get(1);
		System.out.println(wordsCount + " words in file '" + file.getAbsolutePath() + "'");
		this.numFiles++;

		final long end = System.currentTimeMillis();
		final long duration = end - start;
		this.overallDuration += duration;
	}

	public long getOverallDuration() {
		return this.overallDuration;
	}

	public int getNumFiles() {
		return this.numFiles;
	}

}
