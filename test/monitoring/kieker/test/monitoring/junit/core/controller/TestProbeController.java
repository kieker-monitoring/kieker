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

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import kieker.common.configuration.Configuration;
import kieker.monitoring.core.configuration.ConfigurationFactory;
import kieker.monitoring.core.controller.IMonitoringController;
import kieker.monitoring.core.controller.MonitoringController;

import kieker.test.common.junit.AbstractKiekerTest;

/**
 * @author Bjoern Weissenfels
 */
public class TestProbeController extends AbstractKiekerTest {

	@Rule
	public final TemporaryFolder tmpFolder = new TemporaryFolder(); // NOCS (@Rule must be public)

	private File configFile = null;

	public TestProbeController() {
		// empty default consstructor
	}

	@Before
	public void init() throws IOException {
		this.configFile = this.tmpFolder.newFile("adaptiveMonitoring.configFile");
	}

	@After
	public void cleanup() {
		this.tmpFolder.delete();
	}

	@Test
	public void testInitialization() {
		final Configuration configuration = ConfigurationFactory.createSingletonConfiguration();
		configuration.setProperty(ConfigurationFactory.ADAPTIVE_MONITORING_CONFIG_FILE, this.configFile.getAbsolutePath());
		configuration.setProperty(ConfigurationFactory.ADAPTIVE_MONITORING_CONFIG_FILE_UPDATE, "true");
		configuration.setProperty(ConfigurationFactory.ADAPTIVE_MONITORING_CONFIG_FILE_READ_INTERVALL, "10");
		final IMonitoringController ctrl = MonitoringController.createInstance(configuration);
		Assert.assertNotNull(this.configFile);
		Assert.assertTrue(this.configFile.exists());
		Assert.assertNotNull(ctrl);
		ctrl.terminateMonitoring();
	}

	@Test
	public void testIt() {
		final Configuration configuration = ConfigurationFactory.createSingletonConfiguration();
		configuration.setProperty(ConfigurationFactory.ADAPTIVE_MONITORING_CONFIG_FILE, this.configFile.getAbsolutePath());
		configuration.setProperty(ConfigurationFactory.ADAPTIVE_MONITORING_CONFIG_FILE_UPDATE, "true");
		configuration.setProperty(ConfigurationFactory.ADAPTIVE_MONITORING_CONFIG_FILE_READ_INTERVALL, "10");
		final IMonitoringController ctrl = MonitoringController.createInstance(configuration);

		// generate test signature
		final String signature = "public void kieker.test.monitoring.junit.core.controller.TestProbeController.testIt()";

		// test methods
		final String pattern = "..* kieker..*.*(..)";
		Assert.assertFalse(ctrl.activateProbe("invalid pattern"));
		Assert.assertTrue(ctrl.activateProbe(pattern));
		Assert.assertTrue(ctrl.isProbeActivated(signature));
		Assert.assertTrue(ctrl.deactivateProbe(pattern));
		Assert.assertFalse(ctrl.isProbeActivated(signature));
	}
}
