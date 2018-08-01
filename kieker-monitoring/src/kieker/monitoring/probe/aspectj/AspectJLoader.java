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

package kieker.monitoring.probe.aspectj;

import java.io.IOException;
import java.lang.instrument.Instrumentation;
import java.net.URL;
import java.util.Enumeration;

import org.aspectj.bridge.Constants;
import org.aspectj.weaver.loadtime.Agent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author Nils Christian Ehmke, Jan Waller
 *
 * @since 1.9
 */
public final class AspectJLoader {

	public static final String KIEKER_MONITORING_SKIP_DEFAULT_AOP_CONFIGURATION = "kieker.monitoring.skipDefaultAOPConfiguration";

	private static final String DEFAULT_AOP_CONFIG = "META-INF/aop.example.xml";
	private static final Logger LOGGER = LoggerFactory.getLogger(AspectJLoader.class);

	private AspectJLoader() {
		// Avoid instantiation
	}

	/**
	 * JSR-163 preMain entry method.
	 *
	 * @param options
	 *            for the weaver agent
	 * @param instrumentation
	 *            java API instrumentation object
	 */
	public static void premain(final String options, final Instrumentation instrumentation) {
		if (!AspectJLoader.checkConfigurationFileAvailable()) {
			final URL aspectConfigURL = AspectJLoader.class.getClassLoader().getResource(DEFAULT_AOP_CONFIG);
			LOGGER.info("No AspectJ configuration file found. Using Kieker's default AspectJ configuration file ({}).", aspectConfigURL);
			AspectJLoader.addKiekerDefaultConfigFile();
		}

		Agent.premain(options, instrumentation);
	}

	private static boolean checkConfigurationFileAvailable() {
		if (Boolean.getBoolean(KIEKER_MONITORING_SKIP_DEFAULT_AOP_CONFIGURATION)) {
			return true;
		}

		LOGGER.info(
				"Using Kieker's AspectJLoader. This is not recommended for multi-classloader environments such as JavaEE and OSGI. Use the additional VM parameter '-D {}=true'. to disable Kieker's AspectJLoader",
				KIEKER_MONITORING_SKIP_DEFAULT_AOP_CONFIGURATION);

		if (null != System.getProperty("aj5.def")) {
			return true;
		}

		if (null != System.getProperty("org.aspectj.weaver.loadtime.configuration")) {
			return true;
		}

		final ClassLoader cl = ClassLoader.getSystemClassLoader();
		try {
			final Enumeration<URL> aopUserXMLs = cl.getResources(Constants.AOP_USER_XML);
			final Enumeration<URL> aopAJCXMLs = cl.getResources(Constants.AOP_AJC_XML);
			final Enumeration<URL> aopOSGIXMLs = cl.getResources(Constants.AOP_OSGI_XML);

			final boolean anyConfigFileAvailable = aopUserXMLs.hasMoreElements() || aopAJCXMLs.hasMoreElements() || aopOSGIXMLs.hasMoreElements();
			return anyConfigFileAvailable; // NOPMD
		} catch (final IOException ex) {
			return false;
		}
	}

	private static void addKiekerDefaultConfigFile() {
		System.setProperty("org.aspectj.weaver.loadtime.configuration", DEFAULT_AOP_CONFIG);
	}

}
