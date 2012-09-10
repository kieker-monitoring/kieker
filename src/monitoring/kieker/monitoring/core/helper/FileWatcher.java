/***************************************************************************
 * Copyright 2012 Kieker Project (http://kieker-monitoring.net)
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

package kieker.monitoring.core.helper;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.concurrent.ConcurrentHashMap;

import kieker.common.logging.Log;
import kieker.common.logging.LogFactory;

/**
 * Based upon the Apache log4j helpers class FileWatchdog written by
 * Ceki Gülcü and Mathias Bogaert.
 * 
 * @author Björn Weißenfels
 */

public class FileWatcher extends Thread {
	private static final Log LOG = LogFactory.getLog(FileWatcher.class);
	/**
	 * The name of the file whose changes will be observed.
	 */
	protected String pathname;

	/**
	 * The delay between every check.
	 */
	protected long delay;
	private final ConcurrentHashMap<String, Boolean> patterns;
	private final ConcurrentHashMap<String, Boolean> signatureCache;

	File file;
	long lastModif = 0;
	boolean warnedAlready = false;
	boolean interrupted = false;

	public FileWatcher(final File file, final long delay, final ConcurrentHashMap<String, Boolean> patterns,
			final ConcurrentHashMap<String, Boolean> signatureCache) {
		this.file = file;
		this.pathname = file.getPath();
		this.patterns = patterns;
		this.signatureCache = signatureCache;

		this.setDaemon(true);
		this.checkAndConfigure();
		this.setDelay(delay);
	}

	/**
	 * Set the delay to observe between each check of the file changes.
	 */
	public void setDelay(final long delay) {
		if (delay <= 0) {
			this.interrupted = true;
		} else {
			this.delay = delay;
		}
	}

	protected void doOnChange() {

		// read proprietary pattern file
		try {
			this.readFile();
		} catch (final Exception e) {
			LOG.debug("Reading pattern file failed.", e);
		}

	}

	private void readFile() throws Exception {
		this.patterns.clear();
		this.signatureCache.clear();
		final FileReader fr = new FileReader(this.pathname);
		final BufferedReader br = new BufferedReader(fr);
		String currentLine;
		while (br.ready()) {
			currentLine = br.readLine();
			System.out.println(currentLine);
			if (currentLine.startsWith("+")) {
				currentLine = currentLine.replaceFirst("\\+", "").trim();
				this.patterns.put(currentLine, true);
			} else if (currentLine.startsWith("-")) {
				currentLine = currentLine.replaceFirst("\\-", "").trim();
				this.patterns.put(currentLine, false);
			}
		}
		br.close();
	}

	protected void checkAndConfigure() {
		boolean fileExists;
		try {
			fileExists = this.file.exists();
		} catch (final SecurityException e) {
			LOG.warn("Was not allowed to read check file existance, file:[" +
					this.pathname + "].");
			this.interrupted = true; // there is no point in continuing
			return;
		}

		if (fileExists) {
			final long l = this.file.lastModified(); // this can also throw a SecurityException
			if (l > this.lastModif) { // however, if we reached this point this
				this.lastModif = l; // is very unlikely.
				this.doOnChange();
				this.warnedAlready = false;
			}
		} else {
			if (!this.warnedAlready) {
				LOG.debug("[" + this.pathname + "] does not exist.");
				this.warnedAlready = true;
			}
		}
	}

	@Override
	public void run() {
		while (!this.interrupted) {
			try {
				Thread.currentThread();
				Thread.sleep(this.delay);
			} catch (final InterruptedException e) {
				// no interruption expected
			}
			this.checkAndConfigure();
		}
	}

}
