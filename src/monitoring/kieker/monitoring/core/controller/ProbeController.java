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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import kieker.common.configuration.Configuration;
import kieker.common.logging.Log;
import kieker.common.logging.LogFactory;
import kieker.monitoring.core.configuration.ConfigurationFactory;
import kieker.monitoring.core.helper.FileWatcher;
import kieker.monitoring.core.helper.InvalidPatternException;
import kieker.monitoring.core.helper.Pair;
import kieker.monitoring.core.helper.Parser;

/**
 * @author Jan Waller, Björn Weißenfels
 */
public class ProbeController extends AbstractController implements IProbeController {
	private static final Log LOG = LogFactory.getLog(ProbeController.class);

	private static final CopyOnWriteArrayList<Pair<Matcher, Boolean>> MATCHER_LIST = new CopyOnWriteArrayList<Pair<Matcher, Boolean>>();
	private static final CopyOnWriteArrayList<Pair<String, Boolean>> UPDATE_LIST = new CopyOnWriteArrayList<Pair<String, Boolean>>();
	private static final ConcurrentHashMap<String, Boolean> SIGNATURE_CACHE = new ConcurrentHashMap<String, Boolean>();

	private boolean updateConfigFile;
	private final long readIntervall;
	private String pathname;
	private FileWatcher fileWatcher;
	private File file;
	private final Parser parser = new Parser();

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
		this.file = new File(this.pathname);
		if (this.file.exists()) {
			this.fileWatcher = new FileWatcher(this.file, this.readIntervall * 1000, MATCHER_LIST, UPDATE_LIST, SIGNATURE_CACHE);
			this.fileWatcher.start();
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

		Pattern regex;
		try {
			regex = this.parser.parseToPattern(pattern);
		} catch (final InvalidPatternException e) {
			LOG.error(pattern + " is not a valid pattern.");
			e.printStackTrace();
			return false;
		}
		final Matcher matcher = regex.matcher("");
		ProbeController.MATCHER_LIST.add(0, new Pair<Matcher, Boolean>(matcher, true));
		if (this.updateConfigFile) {
			this.updatePatternFile();
		}

		final Pair<String, Boolean> pair = new Pair<String, Boolean>(pattern, true);
		if (ProbeController.UPDATE_LIST.contains(pair)) {
			final int index = ProbeController.UPDATE_LIST.indexOf(pair);
			final Pair<String, Boolean> existingPattern = ProbeController.UPDATE_LIST.get(index);
			final boolean result = !existingPattern.isActive();
			if (result) {
				existingPattern.setActive(true);
			}
			return result;
		} else {
			ProbeController.UPDATE_LIST.add(pair);
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

		Pattern regex;
		try {
			regex = this.parser.parseToPattern(pattern);
		} catch (final InvalidPatternException e) {
			LOG.error(pattern + " is not a valid pattern.");
			e.printStackTrace();
			return false;
		}
		final Matcher matcher = regex.matcher("");
		ProbeController.MATCHER_LIST.add(0, new Pair<Matcher, Boolean>(matcher, false));
		if (this.updateConfigFile) {
			this.updatePatternFile();
		}

		final Pair<String, Boolean> pair = new Pair<String, Boolean>(pattern, false);
		if (ProbeController.UPDATE_LIST.contains(pair)) {
			final int index = ProbeController.UPDATE_LIST.indexOf(pair);
			final Pair<String, Boolean> existingPattern = ProbeController.UPDATE_LIST.get(index);
			final boolean result = existingPattern.isActive();
			if (result) {
				existingPattern.setActive(false);
			}
			return result;
		} else {
			ProbeController.UPDATE_LIST.add(pair);
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
		final ListIterator<Pair<Matcher, Boolean>> patternList = ProbeController.MATCHER_LIST.listIterator();
		Pair<Matcher, Boolean> pair;
		while (patternList.hasNext()) {
			pair = patternList.next();
			if (pair.getPattern().reset(signature).matches()) {
				final boolean value = pair.isActive();
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
			final ListIterator<Pair<String, Boolean>> patternList = ProbeController.UPDATE_LIST.listIterator();
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
			LOG.info("updating config file failed");
			e.printStackTrace();
		}
	}

}
