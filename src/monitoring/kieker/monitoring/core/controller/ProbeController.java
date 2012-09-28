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

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.ListIterator;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import kieker.common.configuration.Configuration;
import kieker.common.logging.Log;
import kieker.common.logging.LogFactory;
import kieker.monitoring.core.configuration.ConfigurationFactory;
import kieker.monitoring.core.helper.FileWatcher;
import kieker.monitoring.core.helper.InvalidPatternException;
import kieker.monitoring.core.helper.Pair;
import kieker.monitoring.core.helper.PatternParser;

/**
 * @author Jan Waller, Bjoern Weissenfels
 */
public class ProbeController extends AbstractController implements IProbeController {
	private static final Log LOG = LogFactory.getLog(ProbeController.class);

	private final CopyOnWriteArrayList<Pair<Matcher, Boolean>> matcherList = new CopyOnWriteArrayList<Pair<Matcher, Boolean>>();
	private final CopyOnWriteArrayList<Pair<String, Boolean>> patternList = new CopyOnWriteArrayList<Pair<String, Boolean>>();
	private final ConcurrentHashMap<String, Boolean> signatureCache = new ConcurrentHashMap<String, Boolean>();
	private final PatternParser parser = new PatternParser();
	private boolean updateConfigFile;
	private final long readIntervall;
	private final String pathname;
	private FileWatcher fileWatcher;
	private File file;

	protected ProbeController(final Configuration configuration) {
		super(configuration);
		this.updateConfigFile = configuration.getBooleanProperty(ConfigurationFactory.ACTIVATE_UPDATE_CONFIG_FILE);
		this.readIntervall = configuration.getLongProperty(ConfigurationFactory.READ_INTERVALL);
		this.pathname = configuration.getStringProperty(ConfigurationFactory.CUSTOM_CONFIG_FILE_LOCATION);
	}

	@Override
	protected void init() {
		// InputStream is = null;
		// try {
		// is = new FileInputStream(this.pathname);
		// } catch (final FileNotFoundException e1) {
		// is = MonitoringController.class.getClassLoader().getResourceAsStream(this.pathname);
		// if (is == null) {
		// LOG.warn("File '" + this.pathname + "' not found");
		// }
		// }

		this.file = new File(this.pathname);
		if (this.file.exists()) {
			this.fileWatcher = new FileWatcher(this.file, this.readIntervall * 1000, this);
			this.fileWatcher.start();
		} else {
			LOG.info("Config file did not exist. " + this.pathname);
			this.updateConfigFile = false;
			try {
				final Matcher matcher = this.parser.parseToPattern("*").matcher("");
				this.matcherList.add(new Pair<Matcher, Boolean>(matcher, true));
			} catch (final InvalidPatternException e) {
				LOG.error(e.getMessage()); // In this case, nothing will be monitored.
				e.printStackTrace();
			}

		}
	}

	@Override
	protected void cleanup() {
		if (LOG.isDebugEnabled()) {
			LOG.debug("Shutting down Probe Controller");
		}
	}

	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder();
		sb.append("ProbeController: ");
		if (this.readIntervall > 0) {
			sb.append(" The pattern file will be checked every " + this.readIntervall + " seconds (readIntervall = " + this.readIntervall + ").\n");
		} else {
			sb.append(" The pattern file won't be checked. (readIntervall = " + this.readIntervall + ").\n");
		}
		if (this.updateConfigFile) {
			sb.append("\tThe pattern file will be updated after de/activateProbe method (updateConfigFile = " + this.updateConfigFile + ").\n");
		} else {
			sb.append("\tThe pattern file won't be updated after de/activateProbe method (updateConfigFile = " + this.updateConfigFile + ").\n");
		}
		return sb.toString();
	}

	public boolean activateProbe(final String pattern) {
		return this.de_activateProbe(pattern, true);
	}

	public boolean deactivateProbe(final String pattern) {
		return this.de_activateProbe(pattern, false);
	}

	private boolean de_activateProbe(final String pattern, final boolean activate) {
		final boolean clearCache = !this.signatureCache.containsKey(pattern);
		if (clearCache) {
			this.signatureCache.clear();
		} else {
			this.signatureCache.put(pattern, activate);
		}

		Pattern regex;
		try {
			regex = this.parser.parseToPattern(pattern);
		} catch (final InvalidPatternException e) {
			LOG.error(pattern + " is not a valid pattern: " + e.getMessage());
			return false;
		}
		final Matcher matcher = regex.matcher("");
		this.matcherList.add(0, new Pair<Matcher, Boolean>(matcher, activate));

		final Pair<String, Boolean> pair = new Pair<String, Boolean>(pattern, activate);
		if (this.patternList.contains(pair)) {
			final int index = this.patternList.indexOf(pair);
			this.patternList.remove(index);
		}
		this.patternList.add(pair);
		if (this.updateConfigFile) {
			this.updatePatternFile();
		}
		return true;

	}

	public boolean isProbeActive(final String signature) {
		if (this.monitoringController.isMonitoringEnabled()) {
			if (this.signatureCache.containsKey(signature)) {
				return this.signatureCache.get(signature);
			} else {
				return this.matchesIncludePattern(signature);
			}
		} else {
			return false;
		}
	}

	/*
	 * tests if signature matches a pattern
	 * and completes accordingly the SIGNATURE_CACHE HashMap
	 */
	private boolean matchesIncludePattern(final String signature) {
		final ListIterator<Pair<Matcher, Boolean>> patternList = this.matcherList.listIterator();
		Pair<Matcher, Boolean> pair;
		while (patternList.hasNext()) {
			pair = patternList.next();
			if (pair.getPattern().reset(signature).matches()) {
				final boolean value = pair.isActive();
				this.signatureCache.put(signature, value);
				return value;
			}
		}
		return false;
	}

	private void updatePatternFile() {
		try {
			final FileWriter fw = new FileWriter(this.pathname);
			final BufferedWriter bw = new BufferedWriter(fw);
			final ListIterator<Pair<String, Boolean>> patternList = this.patternList.listIterator();
			String prefix;
			while (patternList.hasNext()) {
				prefix = "+ ";
				final Pair<String, Boolean> namePattern = patternList.next();
				if (!namePattern.isActive()) {
					prefix = "- ";
				}
				bw.write(prefix + namePattern.getPattern());
				bw.newLine();
			}
			bw.close();
			fw.close();
			if (this.fileWatcher != null) {
				this.fileWatcher.setLastModified(this.file.lastModified());
			}
			LOG.info("updating config file succeed");
		} catch (final IOException e) {
			LOG.error("updating config file failed");
			e.printStackTrace();
		}
	}

	public void replaceProbePatternList(final List<Pair<String, Boolean>> patternList) {
		this.patternList.clear();
		this.matcherList.clear();
		this.signatureCache.clear();
		if (patternList == null) {
			return;
		}
		this.patternList.addAll(patternList);
		final int size = this.patternList.size();
		for (int i = 1; i <= size; i++) {
			final Pair<String, Boolean> pair = this.patternList.get(size - i);
			try {
				final Pattern pattern = this.parser.parseToPattern(pair.getPattern());
				final Matcher matcher = pattern.matcher("");
				this.matcherList.add(new Pair<Matcher, Boolean>(matcher, pair.isActive()));
			} catch (final InvalidPatternException e) {
				LOG.error(pair.getPattern() + " is not a valid pattern: " + e.getMessage());
			}
		}
		if (this.updateConfigFile) {
			this.updatePatternFile();
		}
	}
}
