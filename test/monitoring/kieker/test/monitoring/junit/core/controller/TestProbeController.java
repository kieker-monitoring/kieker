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

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

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
 * TODO: missing test:
 * * periodic reading of config file
 * * writing of config file
 * 
 * @author Bjoern Weissenfels, Jan Waller
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
	public void testInitializationDefaultConfigLocation() {
		final Configuration configuration = ConfigurationFactory.createSingletonConfiguration();
		configuration.setProperty(ConfigurationFactory.ADAPTIVE_MONITORING_ENABLED, "true");
		final IMonitoringController ctrl = MonitoringController.createInstance(configuration);
		Assert.assertFalse(ctrl.isMonitoringTerminated());
		final List<String> list = ctrl.getProbePatternList();
		Assert.assertTrue(list.isEmpty());
		ctrl.terminateMonitoring();
	}

	@Test
	public void testInitializationWithCustomConfiguration() throws UnsupportedEncodingException, FileNotFoundException {
		final PrintWriter pw = new PrintWriter(new BufferedWriter(new OutputStreamWriter(new FileOutputStream(this.configFile, false), "UTF-8")));
		pw.print("## Adaptive Monitoring Config File: ");
		pw.println(this.configFile.getAbsolutePath());
		pw.print("## written on: ");
		final DateFormat date = new SimpleDateFormat("yyyyMMdd'-'HHmmssSSS", Locale.US);
		date.setTimeZone(TimeZone.getTimeZone("UTC"));
		pw.println(date.format(new java.util.Date()));
		pw.println('#');
		// write different pattern
		pw.println("+ *");
		pw.println("- * test.Test()");
		pw.println("test invalid line in config file");
		pw.println("- InvalidPatternException expected");
		pw.close();
		final Configuration configuration = ConfigurationFactory.createSingletonConfiguration();
		configuration.setProperty(ConfigurationFactory.ADAPTIVE_MONITORING_ENABLED, "true");
		configuration.setProperty(ConfigurationFactory.ADAPTIVE_MONITORING_CONFIG_FILE, this.configFile.getAbsolutePath());
		final IMonitoringController ctrl = MonitoringController.createInstance(configuration);
		Assert.assertTrue(this.configFile.exists());
		final List<String> list = ctrl.getProbePatternList();
		Assert.assertFalse(list.isEmpty());
		Assert.assertArrayEquals(new String[] { "+*", "-* test.Test()", }, list.toArray());
		// add manual entries to list
		ctrl.activateProbe("void test.Test()");
		final List<String> list2 = ctrl.getProbePatternList();
		Assert.assertArrayEquals(new String[] { "+*", "-* test.Test()", "+void test.Test()", }, list2.toArray());
		Assert.assertFalse(ctrl.isMonitoringTerminated());
		ctrl.terminateMonitoring();
	}

	@Test
	public void testEnabledDisabledMatching() {
		{ // NOCS // adaptive enabled
			final Configuration configuration = ConfigurationFactory.createSingletonConfiguration();
			configuration.setProperty(ConfigurationFactory.ADAPTIVE_MONITORING_ENABLED, "true");
			final IMonitoringController ctrl = MonitoringController.createInstance(configuration);
			Assert.assertTrue(ctrl.isMonitoringEnabled());
			Assert.assertTrue(ctrl.isProbeActivated("void test.Test()"));
			ctrl.disableMonitoring();
			Assert.assertFalse(ctrl.isMonitoringEnabled());
			Assert.assertFalse(ctrl.isProbeActivated("void test.Test()"));
			ctrl.enableMonitoring();
			Assert.assertTrue(ctrl.isMonitoringEnabled());
			Assert.assertTrue(ctrl.isProbeActivated("void test.Test()"));
			ctrl.deactivateProbe("*");
			Assert.assertFalse(ctrl.isProbeActivated("void test.Test()"));
			ctrl.terminateMonitoring();
		}
		{ // NOCS // adaptive disabled
			final Configuration configuration = ConfigurationFactory.createSingletonConfiguration();
			configuration.setProperty(ConfigurationFactory.ADAPTIVE_MONITORING_ENABLED, "false");
			final IMonitoringController ctrl = MonitoringController.createInstance(configuration);
			ctrl.deactivateProbe("*");
			Assert.assertTrue(ctrl.isMonitoringEnabled());
			Assert.assertTrue(ctrl.isProbeActivated("void test.Test()"));
			ctrl.disableMonitoring();
			Assert.assertFalse(ctrl.isMonitoringEnabled());
			Assert.assertFalse(ctrl.isProbeActivated("void test.Test()"));
			ctrl.enableMonitoring();
			Assert.assertTrue(ctrl.isMonitoringEnabled());
			Assert.assertTrue(ctrl.isProbeActivated("void test.Test()"));
			ctrl.terminateMonitoring();
		}
	}

	@Test
	public void testMatching() {
		final Configuration configuration = ConfigurationFactory.createSingletonConfiguration();
		configuration.setProperty(ConfigurationFactory.ADAPTIVE_MONITORING_ENABLED, "true");
		configuration.setProperty(ConfigurationFactory.ADAPTIVE_MONITORING_CONFIG_FILE, this.configFile.getAbsolutePath());
		final IMonitoringController ctrl = MonitoringController.createInstance(configuration);

		// generate test signature
		final String signature = "public void kieker.test.monitoring.junit.core.controller.TestProbeController.testIt()";

		// test methods
		final String pattern = "..* kieker..*.*(..)";
		Assert.assertFalse(ctrl.activateProbe("InvalidPatternException expected"));
		Assert.assertTrue(ctrl.activateProbe(pattern));
		Assert.assertTrue(ctrl.isProbeActivated(signature));
		Assert.assertTrue(ctrl.deactivateProbe(pattern));
		Assert.assertFalse(ctrl.isProbeActivated(signature));
	}
}
