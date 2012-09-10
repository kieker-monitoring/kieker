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
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLDecoder;
import java.util.Enumeration;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import org.aspectj.util.LangUtil;
import org.aspectj.weaver.tools.PointcutExpression;
import org.aspectj.weaver.tools.PointcutParser;
import org.aspectj.weaver.tools.ShadowMatch;

import kieker.common.configuration.Configuration;
import kieker.common.logging.Log;
import kieker.common.logging.LogFactory;
import kieker.monitoring.core.configuration.ConfigurationFactory;
import kieker.monitoring.core.helper.FileWatcher;

/**
 * @author Jan Waller, Björn Weißenfels
 */
public class ProbeController extends AbstractController implements IProbeController {
	private static final Log LOG = LogFactory.getLog(ProbeController.class);

	private static final ConcurrentHashMap<String, Boolean> PATTERNS = new ConcurrentHashMap<String, Boolean>();
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

		boolean activate = true;
		if (ProbeController.PATTERNS.containsKey(pattern)) {
			activate = !ProbeController.PATTERNS.get(pattern);
		}
		if (activate) {
			ProbeController.PATTERNS.put(pattern, true);
			if (this.updateConfigFile) {
				this.updatePatternFile();
			}
			LOG.info("The pattern " + pattern + " is activated.");
			return true;
		} else {
			LOG.info(pattern + " was already active.");
			return false;
		}
	}

	public boolean deactivateProbe(final String pattern) {
		final boolean clearCache = !ProbeController.SIGNATURE_CACHE.containsKey(pattern);
		if (clearCache) {
			ProbeController.SIGNATURE_CACHE.clear();
		} else {
			ProbeController.SIGNATURE_CACHE.put(pattern, false);
		}

		boolean deactivate = true;
		if (ProbeController.PATTERNS.containsKey(pattern)) {
			deactivate = ProbeController.PATTERNS.get(pattern);
		}
		if (deactivate) {
			ProbeController.PATTERNS.put(pattern, false);
			if (this.updateConfigFile) {
				this.updatePatternFile();
			}
			LOG.info("The pattern " + pattern + " is deactivated.");
			return true;
		} else {
			LOG.info(pattern + " was already deactivated.");
			return false;
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
		Method m;
		try {
			m = this.signatureToMethod(signature);
		} catch (final NoSuchMethodException e) {
			e.printStackTrace();
			return false;
		} catch (final SecurityException e) {
			e.printStackTrace();
			return false;
		} catch (final ClassNotFoundException e) {
			e.printStackTrace();
			return false;
		}

		final PointcutParser pp = PointcutParser.getPointcutParserSupportingAllPrimitivesAndUsingContextClassloaderForResolution();

		final Enumeration<String> keys = ProbeController.PATTERNS.keys();
		String pattern;
		while (keys.hasMoreElements()) {
			pattern = keys.nextElement();
			final String execPattern = "execution(" + pattern + ")";
			final PointcutExpression pe = pp.parsePointcutExpression(execPattern);
			final ShadowMatch sm = pe.matchesMethodExecution(m);
			if (sm.alwaysMatches()) {
				final boolean value = ProbeController.PATTERNS.get(pattern);
				ProbeController.SIGNATURE_CACHE.put(signature, value);
				return value;
			}
		}
		return false;
	}

	private Method signatureToMethod(final String signature) throws NoSuchMethodException, SecurityException, ClassNotFoundException {
		final List<String> sigAsList = LangUtil.anySplit(signature, "(");
		final String prefix = sigAsList.get(0);
		final String suffix = sigAsList.get(1);

		final List<String> suffixList = LangUtil.anySplit(suffix, ")");
		Class<?>[] paramClasses = null;
		if (!suffixList.isEmpty()) {
			final String params = suffixList.get(0);
			final List<String> paramList = LangUtil.anySplit(params, ",");
			final int numberOfParams = paramList.size();
			paramClasses = new Class<?>[numberOfParams];
			for (int i = 0; i < numberOfParams; i++) {
				Class<?> c = this.getPrimitiveType(paramList.get(i));
				if (c == null) {
					c = Class.forName(paramList.get(i));
				}
				paramClasses[i] = c;
			}
		}

		final List<String> prefixList = LangUtil.anySplit(prefix, " ");
		final String name = prefixList.get(prefixList.size() - 1);
		final int index = name.lastIndexOf(".");
		final String methodName = name.substring(index + 1);
		final String className = name.substring(0, index);
		Method result;
		if (paramClasses != null) {
			result = Class.forName(className).getMethod(methodName, paramClasses);
		} else {
			result = Class.forName(className).getMethod(methodName);
		}
		return result;
	}

	private Class<?> getPrimitiveType(final String name)
	{
		if (name.equals("byte")) {
			return byte.class;
		}
		if (name.equals("short")) {
			return short.class;
		}
		if (name.equals("int")) {
			return int.class;
		}
		if (name.equals("long")) {
			return long.class;
		}
		if (name.equals("char")) {
			return char.class;
		}
		if (name.equals("float")) {
			return float.class;
		}
		if (name.equals("double")) {
			return double.class;
		}
		if (name.equals("boolean")) {
			return boolean.class;
		}
		if (name.equals("void")) {
			return void.class;
		}

		return null;
	}

	private void updatePatternFile() {
		try {
			final FileWriter fw = new FileWriter(this.pathname);
			final BufferedWriter bw = new BufferedWriter(fw);
			final Enumeration<String> keys = ProbeController.PATTERNS.keys();
			String prefix;
			while (keys.hasMoreElements()) {
				prefix = "+ ";
				final String key = keys.nextElement();
				if (!ProbeController.PATTERNS.get(key)) {
					prefix = "- ";
				}
				bw.write(prefix + key);
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
