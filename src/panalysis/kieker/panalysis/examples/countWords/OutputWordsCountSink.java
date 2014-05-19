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

import de.chw.util.Pair;

import kieker.panalysis.framework.core.AbstractFilter;
import kieker.panalysis.framework.core.Context;
import kieker.panalysis.framework.core.IInputPort;

/**
 * @author Christian Wulf
 * 
 * @since 1.10
 */
public class OutputWordsCountSink extends AbstractFilter<OutputWordsCountSink> {

	public final IInputPort<OutputWordsCountSink, Pair<File, Integer>> FILE_WORDCOUNT_TUPLE = this.createInputPort();

	private int numFiles = 0;

	public OutputWordsCountSink() {
		this.setAccessesDeviceId(2);
	}

	@Override
	protected boolean execute(final Context<OutputWordsCountSink> context) {
		final Pair<File, Integer> pair = context.tryTake(this.FILE_WORDCOUNT_TUPLE);
		if (pair == null) {
			return false;
		}

		final File file = pair.getFirst();
		final Number wordsCount = pair.getSecond();
		// System.out.println(wordsCount + " words in file '" + file.getAbsolutePath() + "'"); // NOPMD (Just for example purposes)
		this.numFiles++;

		return true;
	}

	/**
	 * @since 1.10
	 * @return
	 */
	public int getNumFiles() {
		return this.numFiles;
	}

}
