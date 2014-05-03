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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.regex.Pattern;

import de.chw.util.Pair;

import kieker.panalysis.framework.concurrent.IoStage;
import kieker.panalysis.framework.core.AbstractFilter;
import kieker.panalysis.framework.core.Context;
import kieker.panalysis.framework.core.IInputPort;
import kieker.panalysis.framework.core.IOutputPort;

/**
 * @author Christian Wulf
 * 
 * @since 1.10
 */
public class CountWordsStage extends AbstractFilter<CountWordsStage> implements IoStage {

	public final IInputPort<CountWordsStage, File> FILE = this.createInputPort();

	public final IOutputPort<CountWordsStage, Exception> EXCEPTION = this.createOutputPort();
	public final IOutputPort<CountWordsStage, Pair<File, Integer>> WORDSCOUNT = this.createOutputPort();

	private final Pattern pattern = Pattern.compile("[^\\p{Graph}]");

	@Override
	protected boolean execute(final Context<CountWordsStage> context) {
		final File file = context.tryTake(this.FILE);
		if (file == null) {
			return false;
		}

		int wordsCount = 0;
		try {
			final BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF-8"));
			try {
				String line;
				while ((line = reader.readLine()) != null) {
					line = line.trim();
					if (line.length() > 0) {
						final String[] words = this.pattern.split(line);
						// System.out.println("" + Arrays.toString(words));
						wordsCount += words.length;
					}
				}
			} finally {
				reader.close();
			}
			context.put(this.WORDSCOUNT, Pair.of(file, wordsCount));
		} catch (final FileNotFoundException e) {
			context.put(this.EXCEPTION, e);
		} catch (final IOException e) {
			context.put(this.EXCEPTION, e);
		}

		return true;
	}

}
