/***************************************************************************
 * Copyright 2022 Kieker Project (http://kieker-monitoring.net)
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

package kieker.monitoring.probe.aspectj.operationExecution;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Combines the streams of a process.
 * 
 * @author dagere
 *
 */
public final class StreamGobbler extends Thread {

	private static final Logger LOGGER = LoggerFactory.getLogger(StreamGobbler.class);

	private final InputStream is;
	private final StringBuffer output;

	private StreamGobbler(final InputStream is, final StringBuffer output) {
		super("Gobbler");
		this.is = is;
		this.output = output;
	}

	@Override
	public void run() {
		try (final InputStreamReader isr = new InputStreamReader(is);
				final BufferedReader br = new BufferedReader(isr);) {

			String line = null;
			while ((line = br.readLine()) != null) { // NOPMD (assign)
				if (output != null) {
					output.append(line);
					output.append("\n");
				}
			}
		} catch (final IOException e) {
			LOGGER.error("Error during stream gobbling", e);
		}
	}

	public static void showFullProcess(final Process process) {
		getFullProcess(process, true);
	}

	/**
	 * Combines the streams of the process and eventually shows the output. Parallel
	 * calls to this method will lead to mixed outputs.
	 * 
	 * @param process
	 *            The process that should be printed
	 * @param showOutput
	 *            Whether the output should be printed directly to System.out
	 * @return The combined output of the streams of the process
	 */
	public static String getFullProcess(final Process process, final boolean showOutput) {
		final StringBuffer output = new StringBuffer();
		final StreamGobbler errorGobbler = new StreamGobbler(process.getErrorStream(), output);
		final StreamGobbler outputGobbler = new StreamGobbler(process.getInputStream(), output);

		outputGobbler.start();
		errorGobbler.start();

		try {
			outputGobbler.join();
			errorGobbler.join();
		} catch (final InterruptedException e) {
			LOGGER.error("Error during stream gobbling", e);
		}
		return output.toString();
	}

}
