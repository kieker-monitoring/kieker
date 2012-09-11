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
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ListIterator;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

import kieker.common.configuration.Configuration;
import kieker.common.logging.Log;
import kieker.common.logging.LogFactory;
import kieker.monitoring.core.configuration.ConfigurationFactory;
import kieker.monitoring.core.helper.FileWatcher;
import kieker.monitoring.core.helper.NamePattern;

/**
 * @author Jan Waller, Björn Weißenfels
 */
public class ProbeController extends AbstractController implements IProbeController {
	private static final Log LOG = LogFactory.getLog(ProbeController.class);

	private static final CopyOnWriteArrayList<NamePattern> PATTERNS = new CopyOnWriteArrayList<NamePattern>();
	private static final ConcurrentHashMap<String, Boolean> SIGNATURE_CACHE = new ConcurrentHashMap<String, Boolean>();

	private boolean updateConfigFile;
	private final long readIntervall;
	private String pathname;

	protected ProbeController(final Configuration configuration) {
		super(configuration);
		this.updateConfigFile = configuration.getBooleanProperty(ConfigurationFactory.ACTIVATE_UPDATE_CONFIG_FILE);
		this.readIntervall = configuration.getLongProperty(ConfigurationFactory.READ_INTERVALL);
		this.pathname = configuration.getStringProperty(ConfigurationFactory.CUSTOM_CONFIG_FILE_LOCATION);
	}

	@Override
	protected void init() {
		final URL url = ClassLoader.getSystemResource(this.pathname);
		try {
			this.pathname = URLDecoder.decode(url.getFile(), "UTF-8");
		} catch (final UnsupportedEncodingException e) {
			LOG.info("Decoding pathname failed.");
			e.printStackTrace();
		}
		final File file = new File(this.pathname);
		if (file.exists()) {
			new FileWatcher(file, this.readIntervall * 1000, PATTERNS, SIGNATURE_CACHE).start();
		} else {
			LOG.info("Config file did not exist. " + this.pathname);
			this.updateConfigFile = false;

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
		final boolean clearCache = !ProbeController.SIGNATURE_CACHE.containsKey(pattern);
		if (clearCache) {
			ProbeController.SIGNATURE_CACHE.clear();
		} else {
			ProbeController.SIGNATURE_CACHE.put(pattern, true);
		}

		final NamePattern namePattern = new NamePattern(pattern, true);
		if (ProbeController.PATTERNS.contains(namePattern)) {
			final int index = ProbeController.PATTERNS.indexOf(namePattern);
			final NamePattern existingPattern = ProbeController.PATTERNS.get(index);
			final boolean result = !existingPattern.isInclude();
			if (result) {
				existingPattern.setInclude(true);
				if (this.updateConfigFile) {
					this.updatePatternFile();
				}
			}
			return result;
		} else {
			ProbeController.PATTERNS.add(namePattern);
			if (this.updateConfigFile) {
				this.updatePatternFile();
			}
			return true;
		}
	}

	public boolean deactivateProbe(final String pattern) {
		final boolean clearCache = !ProbeController.SIGNATURE_CACHE.containsKey(pattern);
		if (clearCache) {
			ProbeController.SIGNATURE_CACHE.clear();
		} else {
			ProbeController.SIGNATURE_CACHE.put(pattern, false);
		}

		final NamePattern namePattern = new NamePattern(pattern, false);
		if (ProbeController.PATTERNS.contains(namePattern)) {
			final int index = ProbeController.PATTERNS.indexOf(namePattern);
			final NamePattern existingPattern = ProbeController.PATTERNS.get(index);
			final boolean result = existingPattern.isInclude();
			if (result) {
				existingPattern.setInclude(false);
				if (this.updateConfigFile) {
					this.updatePatternFile();
				}
			}
			return result;
		} else {
			ProbeController.PATTERNS.add(namePattern);
			if (this.updateConfigFile) {
				this.updatePatternFile();
			}
			return true;
		}
	}

	public boolean isActive(final String signature) {
		if (this.monitoringController.isMonitoringEnabled()) {
			if (ProbeController.SIGNATURE_CACHE.containsKey(signature)) {
				return ProbeController.SIGNATURE_CACHE.get(signature);
			} else {
				return this.matchesIncludePattern(signature);
			}
		} else {
			return false;
		}
	}

	/*
	 * tests if signature matches a pattern
	 * and completes accordingly the SIGNATURE HashMap
	 * 
	 * will be replaced
	 */
	private boolean matchesIncludePattern(final String signature) {

		// TODO: liste rückwärts durchlaufen
		final ListIterator<NamePattern> namePatterns = ProbeController.PATTERNS.listIterator();
		NamePattern pattern;
		while (namePatterns.hasNext()) {
			pattern = namePatterns.next();
			if (pattern.matches(signature)) {
				final boolean value = pattern.isInclude();
				ProbeController.SIGNATURE_CACHE.put(signature, value);
				return value;
			}
		}
		return false;
	}

	private void updatePatternFile() {
		try {
			final FileWriter fw = new FileWriter(this.pathname);
			final BufferedWriter bw = new BufferedWriter(fw);
			final ListIterator<NamePattern> patternList = ProbeController.PATTERNS.listIterator();
			String prefix;
			while (patternList.hasNext()) {
				prefix = "+ ";
				final NamePattern namePattern = patternList.next();
				if (!namePattern.isInclude()) {
					prefix = "- ";
				}
				bw.write(prefix + namePattern);
				bw.newLine();
			}
			bw.close();
			fw.close();
			LOG.info("updating config file succeed");
		} catch (final IOException e) {
			LOG.info("updating config file failed");
			e.printStackTrace();
		}
	}

}
