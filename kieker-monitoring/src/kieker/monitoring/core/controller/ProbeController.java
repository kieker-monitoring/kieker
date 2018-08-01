/***************************************************************************
 * Copyright 2018 Kieker Project (http://kieker-monitoring.net)
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

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import kieker.common.configuration.Configuration;
import kieker.common.util.map.BoundedConcurrentHashMap;
import kieker.common.util.map.BoundedConcurrentHashMap.BoundedCacheBehaviour;
import kieker.monitoring.core.configuration.ConfigurationKeys;
import kieker.monitoring.core.signaturePattern.InvalidPatternException;
import kieker.monitoring.core.signaturePattern.PatternEntry;
import kieker.monitoring.core.signaturePattern.PatternParser;

/**
 * @author Jan Waller, Bjoern Weissenfels, Nils Christian Ehmke
 *
 * @since 1.6
 */
public class ProbeController extends AbstractController implements IProbeController {

	static final Logger LOGGER = LoggerFactory.getLogger(ProbeController.class); // NOPMD package for inner class
	private static final String ENCODING = "UTF-8";

	private final boolean enabled;
	private final String configFilePathname;
	private final boolean configFileUpdate;
	private final int configFileReadIntervall;
	private final int maxCacheSize;
	private final int boundedCacheBehaviour;
	private final ConfigFileReader configFileReader;

	private final ConcurrentMap<String, Boolean> signatureCache;
	private final List<PatternEntry> patternList = new ArrayList<>(); // only accessed synchronized
	private final Map<String, Map<String, List<String>>> patternListParameters = new HashMap<>();

	/**
	 * Creates a new instance of this class using the given configuration to
	 * initialize the class.
	 *
	 * @param configuration
	 *            The configuration used to initialize this controller.
	 */
	protected ProbeController(final Configuration configuration) {
		super(configuration);
		this.enabled = configuration.getBooleanProperty(ConfigurationKeys.ADAPTIVE_MONITORING_ENABLED);
		if (this.enabled) {
			this.configFilePathname = configuration.getPathProperty(ConfigurationKeys.ADAPTIVE_MONITORING_CONFIG_FILE);
			this.configFileUpdate = configuration
					.getBooleanProperty(ConfigurationKeys.ADAPTIVE_MONITORING_CONFIG_FILE_UPDATE);
			this.configFileReadIntervall = configuration
					.getIntProperty(ConfigurationKeys.ADAPTIVE_MONITORING_CONFIG_FILE_READ_INTERVALL);
			this.maxCacheSize = configuration.getIntProperty(ConfigurationKeys.ADAPTIVE_MONITORING_MAX_CACHE_SIZE);
			this.boundedCacheBehaviour = configuration
					.getIntProperty(ConfigurationKeys.ADAPTIVE_MONITORING_BOUNDED_CACHE_BEHAVIOUR);
			if (this.maxCacheSize >= 0) {
				// Bounded cache
				final BoundedConcurrentHashMap.BoundedCacheBehaviour behaviour;
				switch (this.boundedCacheBehaviour) {
				case 0:
					behaviour = BoundedCacheBehaviour.IGNORE_NEW_ENTRIES;
					break;
				case 1:
					behaviour = BoundedCacheBehaviour.REMOVE_RANDOM_ENTRY;
					break;
				case 2:
					behaviour = BoundedCacheBehaviour.CLEAR_CACHE;
					break;
				default:
					LOGGER.warn("Unexpected value for property '{}'. Using default value 0.",
							ConfigurationKeys.ADAPTIVE_MONITORING_BOUNDED_CACHE_BEHAVIOUR);

					behaviour = BoundedCacheBehaviour.IGNORE_NEW_ENTRIES;
					break;
				}
				final int cacheSize;
				if (this.maxCacheSize >= 1) {
					cacheSize = this.maxCacheSize;
				} else {
					LOGGER.warn("Invalid value for property '{}'. Using default value 100.",
							ConfigurationKeys.ADAPTIVE_MONITORING_MAX_CACHE_SIZE);

					cacheSize = 100;
				}
				this.signatureCache = new BoundedConcurrentHashMap<>(behaviour, cacheSize);
			} else {
				// Unbounded cache
				this.signatureCache = new ConcurrentHashMap<>();
			}
			this.configFileReader = new ConfigFileReader(this.configFilePathname);
			// run once to get the initial file contents
			this.configFileReader.readFile(true);
		} else {
			this.configFilePathname = null; // NOPMD (null)
			this.configFileUpdate = false;
			this.configFileReadIntervall = 0;
			this.maxCacheSize = 0;
			this.boundedCacheBehaviour = 0;
			this.configFileReader = null; // NOPMD (null)
			this.signatureCache = new ConcurrentHashMap<>();
		}
	}

	@Override
	protected void init() {
		if (this.enabled && (this.monitoringController != null)) {
			final SamplingController samplingController = this.monitoringController.getSamplingController();
			final ScheduledThreadPoolExecutor scheduler = samplingController.periodicSensorsPoolExecutor;
			if ((this.configFileReadIntervall > 0) && (null != scheduler)) {
				scheduler.scheduleWithFixedDelay(this.configFileReader, this.configFileReadIntervall,
						this.configFileReadIntervall, TimeUnit.SECONDS);
			} else {
				if ((this.configFileReadIntervall > 0) && (null == scheduler)) {
					LOGGER.warn(
							"Failed to enable regular reading of adaptive monitoring config file. '{}' must be > 0!",
							ConfigurationKeys.PERIODIC_SENSORS_EXECUTOR_POOL_SIZE);
				}
			}
		}
	}

	@Override
	protected void cleanup() {
		LOGGER.debug("Shutting down Probe Controller");
	}

	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder(255);
		sb.append("ProbeController: ");
		if (this.enabled) {
			sb.append('\'');
			sb.append(this.configFilePathname);
			sb.append("'\n\tTime intervall for update checks of pattern file (in seconds): ");
			if (this.configFileReadIntervall > 0) {
				sb.append(this.configFileReadIntervall);
			} else {
				sb.append("deactivated");
			}
			sb.append("\n\tUpdate pattern file with additional patterns: ");
			sb.append(this.configFileUpdate);
			sb.append("\n\tSignature cache: ");
			if (this.maxCacheSize >= 0) {
				sb.append("bounded");
			} else {
				sb.append("unbounded");
			}
		} else {
			sb.append("disabled");
		}
		sb.append('\n');
		return sb.toString();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean activateProbe(final String pattern) {
		return this.addPattern(pattern, true);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean deactivateProbe(final String pattern) {
		return this.addPattern(pattern, false);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isProbeActivated(final String signature) {
		if (this.enabled) {
			final Boolean active = this.signatureCache.get(signature);
			if (null == active) {
				return this.matchesPattern(signature);
			} else {
				return active;
			}
		} else {
			return true;
		}
	}

	/**
	 * Sets the list of probe patterns.
	 *
	 * @param strPatternList
	 *            The new list with pattern strings.
	 *
	 * @param updateConfig
	 *            Whether the pattern file should be updated or not.
	 */
	protected void setProbePatternList(final List<String> strPatternList, final boolean updateConfig) {
		if (!this.enabled) {
			LOGGER.warn("Adapative Monitoring is disabled!");
			return;
		}

		synchronized (this) {
			this.patternList.clear();
			this.signatureCache.clear();
			for (final String pattern : strPatternList) {
				this.addToPatternEntryList(pattern);
			}
			if (updateConfig && this.configFileUpdate) {
				this.updatePatternFile();
			}
		}

	}

	private void addToPatternEntryList(final String pattern) {
		if (pattern.length() == 0) {
			// ignore empty lines
			return;
		}
		try {
			switch (pattern.charAt(0)) {
			case '+':
				this.patternList.add(new PatternEntry(pattern.substring(1).trim(), true));
				break;
			case '-':
				this.patternList.add(new PatternEntry(pattern.substring(1).trim(), false));
				break;
			case '#':
				// ignore comment
				break;
			default:
				LOGGER.warn("Each line should either start with '+', '-', or '#'. Ignoring: {}", pattern);
				break;
			}
		} catch (final InvalidPatternException ex) {
			LOGGER.error("'{}' is not a valid pattern.", pattern.substring(1), ex);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void setProbePatternList(final List<String> strPatternList) {
		this.setProbePatternList(strPatternList, true);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<String> getProbePatternList() {
		if (!this.enabled) {
			LOGGER.warn("Adapative Monitoring is disabled!");
			return new ArrayList<>(0);
		}
		synchronized (this) {
			final List<String> list = new ArrayList<>(this.patternList.size());
			for (final PatternEntry entry : this.patternList) {
				final String strPattern;
				if (entry.isActivated()) {
					strPattern = '+' + entry.getStrPattern();
				} else {
					strPattern = '-' + entry.getStrPattern();
				}
				list.add(strPattern);
			}
			return list;
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Map<String, List<String>> getParameters(final String pattern) {
		synchronized (this) {
			return this.patternListParameters.get(pattern);
		}

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void deleteParameterEntry(final String pattern) {
		synchronized (this) {
			this.patternListParameters.remove(pattern);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void addParameterEntry(final String pattern, final String parameterName, final List<String> parameters) {
		synchronized (this) {
			this.patternListParameters.get(pattern).put(parameterName, parameters);
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void addCompletePatternParameters(final String pattern, final Map<String, List<String>> parameterMap) {
		synchronized (this) {
			this.patternListParameters.put(pattern, parameterMap);
		}
	}

	/**
	 * This method tests if the given signature matches a pattern and completes
	 * accordingly the signatureCache map.
	 *
	 * @param signature
	 *            The signature to match.
	 */
	private boolean matchesPattern(final String signature) {
		synchronized (this) {
			final ListIterator<PatternEntry> patternListIterator = this.patternList
					.listIterator(this.patternList.size());
			while (patternListIterator.hasPrevious()) {
				final PatternEntry patternEntry = patternListIterator.previous();
				if (patternEntry.getPattern().matcher(signature).matches()) {
					final boolean value = patternEntry.isActivated();
					this.signatureCache.put(signature, value);
					return value;
				}
			}
		}
		// Do not forget to remember this default value
		this.signatureCache.put(signature, true);

		return true; // if nothing matches, the default is true!
	}

	private boolean addPattern(final String strPattern, final boolean activated) {
		if (!this.enabled) {
			LOGGER.warn("Adapative Monitoring is disabled!");
			return false;
		}
		synchronized (this) {
			// we must always clear the cache!
			this.signatureCache.clear();
			final Pattern pattern;
			try {
				pattern = PatternParser.parseToPattern(strPattern);
			} catch (final InvalidPatternException ex) {
				LOGGER.error("'{}' is not a valid pattern.", strPattern, ex);
				return false;
			}
			this.patternList.add(new PatternEntry(strPattern, pattern, activated));
			if (this.configFileUpdate) {
				this.updatePatternFile();
			}
		}
		return true;
	}

	private void updatePatternFile() { // only called within synchronized
		PrintWriter pw = null;
		try {
			pw = new PrintWriter(new BufferedWriter(
					new OutputStreamWriter(new FileOutputStream(this.configFilePathname, false), ENCODING)));
			pw.print("## Adaptive Monitoring Config File: ");
			pw.println(this.configFilePathname);
			pw.print("## written on: ");
			final DateFormat date = new SimpleDateFormat("yyyyMMdd'-'HHmmssSSS", Locale.US);
			date.setTimeZone(TimeZone.getTimeZone("UTC"));
			pw.println(date.format(new java.util.Date()));
			pw.println('#');
			final List<String> strPatternList = this.getProbePatternList();
			for (final String string : strPatternList) {
				pw.println(string);
			}
		} catch (final IOException ex) {
			LOGGER.error("Updating Adaptive Monitoring config file failed.", ex);
		} finally {
			if (pw != null) {
				pw.close();
			}
		}
		this.configFileReader.lastModifiedTimestamp = System.currentTimeMillis();
		LOGGER.info("Updating Adaptive Monitoring config file succeeded.");
	}

	/**
	 * @author Jan Waller
	 */
	private final class ConfigFileReader implements Runnable {
		private final String configFilePathname;
		volatile long lastModifiedTimestamp; // NOPMD NOCS (package)

		public ConfigFileReader(final String configFilePathname) {
			this.configFilePathname = configFilePathname;
		}

		private List<String> readConfigFile(final BufferedReader reader) throws IOException {
			final List<String> strPatternList = new LinkedList<>();
			String line;
			while ((line = reader.readLine()) != null) { // NOPMD (assign)
				strPatternList.add(line);
			}
			return strPatternList;
		}

		public void readFile(final boolean fallbackToResource) {
			BufferedReader reader = null;
			final long lastModified;
			final File file = new File(this.configFilePathname);
			try {
				if (file.canRead() && ((lastModified = file.lastModified()) > 0L)) { // NOPMD NOCS
					if (lastModified > this.lastModifiedTimestamp) {
						this.lastModifiedTimestamp = lastModified;
						reader = new BufferedReader(new InputStreamReader(new FileInputStream(file), ENCODING));
						try {
							ProbeController.this.setProbePatternList(this.readConfigFile(reader), false);
							return;
						} catch (final IOException ex) {
							LOGGER.warn("Error reading adaptive monitoring config file: {}", this.configFilePathname, ex);
						}
					} else {
						return; // nothing do this time
					}
				}
			} catch (final SecurityException ex) { // NOPMD NOCS
				// file not found or not readable
			} catch (final IOException ex) { // NOPMD NOCS
				// file not found or not readable
			} finally {
				if (reader != null) {
					try {
						reader.close();
					} catch (final IOException ex) {
						LOGGER.error("Failed to close file: {}", this.configFilePathname, ex);
					}
				}
			}
			if (fallbackToResource) {
				try {
					final URL configFileAsResource = MonitoringController.class.getClassLoader()
							.getResource(this.configFilePathname);
					if (null != configFileAsResource) {
						reader = new BufferedReader(new InputStreamReader(configFileAsResource.openStream(), ENCODING));
						try {
							ProbeController.this.setProbePatternList(this.readConfigFile(reader), true);
							return;
						} catch (final IOException ex) {
							LOGGER.warn("Error reading adaptive monitoring config file: {}", this.configFilePathname, ex);
						}
					}
				} catch (final SecurityException ex) { // NOPMD NOCS
					// file not found or not readable
				} catch (final IOException ex) { // NOPMD NOCS
					// file not found or not readable
				} finally {
					if (reader != null) {
						try {
							reader.close();
						} catch (final IOException ex) {
							LOGGER.error("Failed to close file: {}", this.configFilePathname, ex);
						}
					}
				}
				LOGGER.warn("Adaptive monitoring config file not found: {}", this.configFilePathname);
			}
		}

		@Override
		public void run() {
			this.readFile(false);
		}
	}
}
