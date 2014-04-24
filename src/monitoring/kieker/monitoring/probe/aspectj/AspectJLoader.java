/***************************************************************************
 * Copyright 2014 Kieker Project (http://kieker-monitoring.net)
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

import kieker.common.logging.Log;
import kieker.common.logging.LogFactory;

/**
 * @author Nils Christian Ehmke, Jan Waller
 * 
 * @since 1.9
 */
public final class AspectJLoader {
	private static final Log LOG = LogFactory.getLog(AspectJLoader.class);

	public static final String KIEKER_MONITORING_SKIP_DEFAULT_AOP_CONFIGURATION = "kieker.monitoring.skipDefaultAOPConfiguration";

	private AspectJLoader() {
		// Avoid instantiation
	}

	/**
	 * JSR-163 preMain entry method
	 * 
	 * @param options
	 * @param instrumentation
	 */
	public static void premain(final String options, final Instrumentation instrumentation) {
		if (AspectJLoader.noConfigurationFileAvailable()) {
			LOG.info("No AspectJ configuration file found. Using Kieker's default AspectJ configuration file.");
			AspectJLoader.addKiekerDefaultConfigFile();
		}

		Agent.premain(options, instrumentation);
	}

	private static boolean noConfigurationFileAvailable() {
		if (Boolean.getBoolean(KIEKER_MONITORING_SKIP_DEFAULT_AOP_CONFIGURATION)) {
			return false;
		}

		LOG.info("Using Kieker's AspectJLoader. This is not recommended for multi-classloader environments such as JavaEE and OSGI.");
		LOG.info("To disable Kieker's AspectJLoader use the additional VM parameter '-D" + KIEKER_MONITORING_SKIP_DEFAULT_AOP_CONFIGURATION + "=true'.");

		if (null != System.getProperty("aj5.def")) {
			return false;
		}

		if (null != System.getProperty("org.aspectj.weaver.loadtime.configuration")) {
			return false;
		}

		final ClassLoader cl = ClassLoader.getSystemClassLoader();
		try {
			final Enumeration<URL> aopUserXMLs = cl.getResources(Constants.AOP_USER_XML);
			final Enumeration<URL> aopAJCXMLs = cl.getResources(Constants.AOP_AJC_XML);
			final Enumeration<URL> aopOSGIXMLs = cl.getResources(Constants.AOP_OSGI_XML);

			final boolean anyConfigFileAvailable = aopUserXMLs.hasMoreElements() || aopAJCXMLs.hasMoreElements() || aopOSGIXMLs.hasMoreElements();
			return !anyConfigFileAvailable; // NOPMD
		} catch (final IOException ex) {
			return true;
		}
	}

	private static void addKiekerDefaultConfigFile() {
		System.setProperty("org.aspectj.weaver.loadtime.configuration", "META-INF/aop.example.xml");
	}

}
