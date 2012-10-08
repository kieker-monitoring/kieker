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

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
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
	private static final String ENCODING = "UTF-8";
	private static final PatternEntry DEFAULTPATTERN = new PatternEntry("*", PatternParser.parseToPattern("*"), true);

	private final boolean enabled;
	private final String configFilePathname;
	private final boolean configFileUpdate;
	private final int configFileReadIntervall;
	private final ConfigFileReader configFileReader;

	private final ScheduledFuture<?> configFileReaderSchedule;

	private final ConcurrentMap<String, Boolean> signatureCache = new ConcurrentHashMap<String, Boolean>();
	private final List<PatternEntry> patternList = new ArrayList<PatternEntry>(); // only accessed synchronized

	protected ProbeController(final Configuration configuration) {
		super(configuration);
		this.enabled = configuration.getBooleanProperty(ConfigurationFactory.ADAPTIVE_MONITORING_ENABLED);
		if (this.enabled) {
			this.configFilePathname = configuration.getPathProperty(ConfigurationFactory.ADAPTIVE_MONITORING_CONFIG_FILE);
			this.configFileUpdate = configuration.getBooleanProperty(ConfigurationFactory.ADAPTIVE_MONITORING_CONFIG_FILE_UPDATE);
			this.configFileReader = new ConfigFileReader(this.configFilePathname);
			final int tmpConfigFileReadIntervall = configuration.getIntProperty(ConfigurationFactory.ADAPTIVE_MONITORING_CONFIG_FILE_READ_INTERVALL);
			final ScheduledThreadPoolExecutor scheduler = this.monitoringController.getSamplingController().periodicSensorsPoolExecutor;
			if ((tmpConfigFileReadIntervall > 0) && (null != scheduler)) {
				this.configFileReadIntervall = tmpConfigFileReadIntervall;
				this.configFileReaderSchedule = scheduler.scheduleWithFixedDelay(this.configFileReader,
						tmpConfigFileReadIntervall, tmpConfigFileReadIntervall, TimeUnit.SECONDS);
			} else {
				this.configFileReadIntervall = 0;
				this.configFileReaderSchedule = null; // NOPMD (null)
				if ((tmpConfigFileReadIntervall > 0) && (null == scheduler)) {
					LOG.warn("Failed to enable regular reading of adaptive monitoring config file. '" + ConfigurationFactory.PERIODIC_SENSORS_EXECUTOR_POOL_SIZE
							+ "' must be > 0!");
				}
			}
			this.configFileReader.run(); // run once to get the initial file contents
		} else {
			this.configFilePathname = null; // NOPMD (null)
			this.configFileUpdate = false;
			this.configFileReader = null; // NOPMD (null)
			this.configFileReadIntervall = 0;
			this.configFileReaderSchedule = null; // NOPMD (null)
		}
	}

	@Override
	protected void init() {
		// currently nothing to do?
	}

	@Override
	protected void cleanup() {
		if (LOG.isDebugEnabled()) {
			LOG.debug("Shutting down Probe Controller");
		}
		if (null != this.configFileReaderSchedule) {
			this.configFileReaderSchedule.cancel(true);
		}
	}

	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder();
		sb.append("ProbeController: ");
		if (this.enabled) {
			sb.append('\'');
			sb.append(this.configFilePathname).append("\'\n");
			sb.append("\tTime intervall for update checks of pattern file (in seconds): ");
			if (this.configFileReadIntervall > 0) {
				sb.append(this.configFileReadIntervall);
			} else {
				sb.append("deactivated");
			}
			sb.append("\n\tUpdate pattern file with additional patterns: ");
			sb.append(this.configFileUpdate);
		} else {
			sb.append("disabled");
		}
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

	public void replaceProbePatternList(final List<PatternEntry> newPatternList) {
		if (!this.enabled) {
			LOG.warn("Adapative Monitoring is disabled!");
			return;
		}
		synchronized (this) {
			this.patternList.clear();
			this.signatureCache.clear(); // TODO: maybe rebuild
			this.patternList.add(ProbeController.DEFAULTPATTERN);
			if (null != newPatternList) {
				this.patternList.addAll(0, newPatternList);
			}
			if (this.configFileUpdate) {
				this.updatePatternFile();
			}
		}
	}

	/**
	 * tests if signature matches a pattern and completes accordingly the signatureCache map
	 */
	private boolean matchesPattern(final String signature) {
		synchronized (this) {
			final ListIterator<PatternEntry> patternListIterator = this.patternList.listIterator();
			while (patternListIterator.hasNext()) {
				final PatternEntry patternEntry = patternListIterator.next();
				if (patternEntry.getPattern().matcher(signature).matches()) {
					final boolean value = patternEntry.isActivated();
					this.signatureCache.put(signature, value);
					return value;
				}
			}
		}
		return false;
	}

	private boolean addPattern(final String strPattern, final boolean activated) {
		if (!this.enabled) {
			LOG.warn("Adapative Monitoring is disabled!");
			return false;
		}
		synchronized (this) {
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
		}
		return true;
	}

	private void updatePatternFile() { // only called within synchronized
		PrintWriter pw = null;
		try {
			pw = new PrintWriter(new BufferedWriter(new OutputStreamWriter(new FileOutputStream(this.configFilePathname, false), ENCODING)));
			final ListIterator<PatternEntry> patternListIterator = this.patternList.listIterator();
			while (patternListIterator.hasNext()) {
				final PatternEntry patternEntry = patternListIterator.next();
				if (patternEntry.isActivated()) {
					pw.print('+');
				} else {
					pw.print('-');
				}
				pw.print(' ');
				pw.println(patternEntry.getStrPattern());
			}
		} catch (final IOException ex) {
			LOG.error("Updating Adaptive Monitoring config file failed.", ex);
		} finally {
			if (pw != null) {
				pw.close();
			}
		}
		this.configFileReader.lastModifiedTimestamp = System.currentTimeMillis();
		LOG.info("Updating Adaptive Monitoring config file succeeded.");
	}

	private final class ConfigFileReader implements Runnable {
		private final String configFilePathname;
		volatile long lastModifiedTimestamp; // NOPMD NOCS (package)

		public ConfigFileReader(final String configFilePathname) {
			this.configFilePathname = configFilePathname;
		}

		public void run() {
			InputStream is = null;
			try {
				try {
					final long lastModified;
					final File file = new File(this.configFilePathname);
					if (file.canRead() && ((lastModified = file.lastModified()) > 0L)) { // NOPMD NOCS
						if (lastModified > this.lastModifiedTimestamp) {
							this.lastModifiedTimestamp = lastModified;
							is = new FileInputStream(file);
						} else {
							return; // nothing do this time
						}
					} // else { // file not found or not accessible
						// is = null;
					// }
				} catch (final SecurityException ex) { // NOPMD
					// is = null;
				} catch (final FileNotFoundException ex) { // NOPMD
					// is = null;
				}
				if (null == is) {
					final long lastModified;
					final File file = new File(MonitoringController.class.getClassLoader().getResource(this.configFilePathname).toURI());
					if (file.canRead() && ((lastModified = file.lastModified()) > 0L)) { // NOPMD NOCS
						if (lastModified > this.lastModifiedTimestamp) {
							this.lastModifiedTimestamp = lastModified;
							is = new FileInputStream(file);
						} else {
							return; // nothing do this time
						}
					} else { // no file found ...
						if (LOG.isDebugEnabled()) {
							LOG.debug("Adaptive monitoring config file not found: " + this.configFilePathname);
						}
						return;
					}
				}
				// if we are here (is != null)
				BufferedReader in = null;
				final List<PatternEntry> newPatternList = new LinkedList<PatternEntry>();
				try {
					in = new BufferedReader(new InputStreamReader(is, ENCODING));
					String line;
					while ((line = in.readLine()) != null) { // NOPMD (assign)
						if (line.length() == 0) {
							continue; // ignore empty lines
						} else if (line.charAt(0) == '+') {
							final String strPattern = line.substring(1);
							newPatternList.add(new PatternEntry(strPattern, true));
							continue;
						} else if (line.charAt(0) == '-') {
							final String strPattern = line.substring(1);
							newPatternList.add(new PatternEntry(strPattern, false));
							continue;
						} else if (!(line.charAt(0) == '#')) {
							LOG.info("Adaptive monitoring config file: Please start every line with a '+' for an 'include', a '-' for an 'exclude' and a '#' for a comment.");
							continue;
						}
					}
				} finally {
					if (in != null) {
						in.close();
					}
				}
				ProbeController.this.replaceProbePatternList(newPatternList);
			} catch (final URISyntaxException ex) {
				LOG.warn("Adaptive monitoring config file not found: " + this.configFilePathname, ex);
			} catch (final SecurityException ex) {
				LOG.warn("Adaptive monitoring config file not found: " + this.configFilePathname, ex);
			} catch (final FileNotFoundException ex) {
				LOG.warn("Adaptive monitoring config file not found: " + this.configFilePathname, ex);
			} catch (final IOException ex) {
				LOG.warn("Error reading adaptive monitoring config file: " + this.configFilePathname, ex);
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
	}
}
