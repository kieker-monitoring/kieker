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

package kieker.monitoring.core.controller;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.ListIterator;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.regex.Pattern;

import kieker.common.configuration.Configuration;
import kieker.common.logging.Log;
import kieker.common.logging.LogFactory;
import kieker.monitoring.core.configuration.ConfigurationFactory;
import kieker.monitoring.core.signaturePattern.InvalidPatternException;
import kieker.monitoring.core.signaturePattern.PatternEntry;
import kieker.monitoring.core.signaturePattern.PatternParser;

/**
 * @author Jan Waller, Bjoern Weissenfels
 */
public class ProbeController extends AbstractController implements IProbeController {
	private static final Log LOG = LogFactory.getLog(ProbeController.class);
	private static final PatternEntry defaultPattern = new PatternEntry("*", PatternParser.parseToPattern("*"), true);

	private final String configFilePathname;
	private final long configFileReadIntervall;
	private final boolean configFileUpdate;

	private final ConcurrentMap<String, Boolean> signatureCache = new ConcurrentHashMap<String, Boolean>();

	private final CopyOnWriteArrayList<PatternEntry> patternList = new CopyOnWriteArrayList<PatternEntry>();

	// private FileWatcher fileWatcher;
	// private File file;

	protected ProbeController(final Configuration configuration) {
		super(configuration);
		this.configFilePathname = configuration.getPathProperty(ConfigurationFactory.ADAPTIVE_MONITORING_CONFIG_FILE);
		this.configFileUpdate = configuration.getBooleanProperty(ConfigurationFactory.ADAPTIVE_MONITORING_CONFIG_FILE_UPDATE);
		this.configFileReadIntervall = configuration.getLongProperty(ConfigurationFactory.ADAPTIVE_MONITORING_CONFIG_FILE_READ_INTERVALL);
	}

	@Override
	protected void init() {
		InputStream is = null;
		try {
			try {
				is = new FileInputStream(this.configFilePathname);
			} catch (final FileNotFoundException ex) {
				is = MonitoringController.class.getClassLoader().getResourceAsStream(this.configFilePathname);
				if (is == null) {
					LOG.warn("Failed to read file: " + this.configFilePathname);
				}
			}
			// TODO actually read file into List
			this.replaceProbePatternList(null);
		} finally {
			if (null != is) {
				try {
					is.close();
				} catch (final IOException ex) {
					LOG.error("Failed to close file: " + this.configFilePathname, ex);
				}
			}
		}
	}

	@Override
	protected void cleanup() {
		if (LOG.isDebugEnabled()) {
			LOG.debug("Shutting down Probe Controller");
		}
		// TODO cleanup the fileWatcher
	}

	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder();
		sb.append("ProbeController: \'");
		sb.append(this.configFilePathname).append("\'\n");
		sb.append("\tTime intervall for update checks of pattern file (in seconds): ");
		if (this.configFileReadIntervall > 0) {
			sb.append(this.configFileReadIntervall);
		} else {
			sb.append("deactivated");
		}
		sb.append("\n\tUpdate pattern file with additional patterns: ");
		sb.append(this.configFileUpdate);
		sb.append('\n');
		return sb.toString();
	}

	public boolean activateProbe(final String pattern) {
		return this.addPattern(pattern, true);
	}

	public boolean deactivateProbe(final String pattern) {
		return this.addPattern(pattern, false);
	}

	public boolean isProbeActivated(final String signature) {
		if (!this.monitoringController.isMonitoringEnabled()) {
			return false;
		}
		final Boolean active = this.signatureCache.get(signature);
		if (null == active) {
			return this.matchesPattern(signature);
		} else {
			return active;
		}
	}

	public void replaceProbePatternList(final List<PatternEntry> patternList) {
		synchronized (this) {
			this.patternList.clear();
			this.signatureCache.clear(); // TODO: maybe rebuild
			this.patternList.add(ProbeController.defaultPattern);
			if (patternList == null) {
				return;
			}
			this.patternList.addAll(0, patternList);
			if (this.configFileUpdate) {
				this.updatePatternFile();
			}
		}
	}

	/**
	 * tests if signature matches a pattern and completes accordingly the signatureCache map
	 */
	private boolean matchesPattern(final String signature) {
		final ListIterator<PatternEntry> patternList = this.patternList.listIterator();
		while (patternList.hasNext()) {
			final PatternEntry pair = patternList.next();
			if (pair.getPattern().matcher(signature).matches()) {
				final boolean value = pair.isActivated();
				this.signatureCache.put(signature, value);
				return value;
			}
		}
		return false;
	}

	private boolean addPattern(final String strPattern, final boolean activated) {
		// we must always clear the cache!
		this.signatureCache.clear(); // TODO maybe not clear but rebuild?

		final Pattern pattern;
		try {
			pattern = PatternParser.parseToPattern(strPattern);
		} catch (final InvalidPatternException ex) {
			LOG.error("'" + strPattern + "' is not a valid pattern.", ex);
			return false;
		}
		this.patternList.add(0, new PatternEntry(strPattern, pattern, activated));

		if (this.configFileUpdate) {
			// TODO remove double entries in list? Use modified(?) CopyOnWriteArraySet
			this.updatePatternFile();
		}
		return true;
	}

	private void updatePatternFile() {
		// try {
		// final FileWriter fw = new FileWriter(this.configFilePathname);
		// final BufferedWriter bw = new BufferedWriter(fw);
		// final ListIterator<Pair<String, Boolean>> patternList = this.patternList.listIterator();
		// String prefix;
		// while (patternList.hasNext()) {
		// prefix = "+ ";
		// final Pair<String, Boolean> namePattern = patternList.next();
		// if (!namePattern.isActive()) {
		// prefix = "- ";
		// }
		// bw.write(prefix + namePattern.getPattern());
		// bw.newLine();
		// }
		// bw.close();
		// fw.close();
		// if (this.fileWatcher != null) {
		// this.fileWatcher.setLastModified(this.file.lastModified());
		// }
		// LOG.info("updating config file succeed");
		// } catch (final IOException e) {
		// LOG.error("updating config file failed");
		// e.printStackTrace();
		// }
	}
}
