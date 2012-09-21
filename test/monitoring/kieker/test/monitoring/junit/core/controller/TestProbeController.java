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

package kieker.test.monitoring.junit.core.controller;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLDecoder;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import kieker.common.configuration.Configuration;
import kieker.monitoring.core.configuration.ConfigurationFactory;
import kieker.monitoring.core.controller.IMonitoringController;
import kieker.monitoring.core.controller.MonitoringController;

/**
 * @author Björn Weißenfels
 */
public class TestProbeController {

	File file = null;
	IMonitoringController MC;

	@Before
	public void init() {
		// generating config file, may not exist when starting the test
		String pathname = "META-INF/";
		final URL url = ClassLoader.getSystemResource(pathname);
		final String path = url.getFile();

		try {
			pathname = URLDecoder.decode(path, "UTF-8") + "kieker.monitoring.adaptiveMonitoring.configFile";
			new File(pathname).createNewFile();
			this.file = new File(pathname);
		} catch (final IOException e) {
			e.printStackTrace();
		}

		// load properties
		final Configuration configuration = ConfigurationFactory.createSingletonConfiguration();
		this.MC = MonitoringController.createInstance(configuration);

	}

	@Test
	public void testInitialization() {
		Assert.assertNotNull(this.file);
		Assert.assertTrue(this.file.exists());
		Assert.assertNotNull(this.MC);
	}

	@Test
	public void testIt() {

		// generate test signature
		final String signature = "public void kieker.test.monitoring.junit.core.controller.TestProbeController.testIt()";

		// test methods
		final String pattern = "..* kieker..*.*(..)";
		Assert.assertFalse(this.MC.activateProbe("ungültiges Pattern"));
		Assert.assertTrue(this.MC.activateProbe(pattern));
		Assert.assertTrue(this.MC.isActive(signature));
		Assert.assertTrue(this.MC.deactivateProbe(pattern));
		Assert.assertFalse(this.MC.isActive(signature));

	}

	@After
	public void cleanup() {
		this.file.delete();
		Assert.assertFalse(this.file.exists());
	}

}
