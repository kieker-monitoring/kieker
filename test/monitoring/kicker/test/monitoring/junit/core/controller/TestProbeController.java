/***************************************************************************
 * Copyright 2014 Kicker Project (http://kicker-monitoring.net)
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

package kicker.test.monitoring.junit.core.controller;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import kicker.common.configuration.Configuration;
import kicker.common.logging.LogImplJUnit;
import kicker.monitoring.core.configuration.ConfigurationFactory;
import kicker.monitoring.core.controller.IMonitoringController;
import kicker.monitoring.core.controller.MonitoringController;
import kicker.monitoring.core.signaturePattern.InvalidPatternException;
import kicker.monitoring.core.signaturePattern.SignatureFactory;
import kicker.monitoring.writer.DummyWriter;
import kicker.test.common.junit.AbstractKickerTest;

/**
 * @author Bjoern Weissenfels, Jan Waller
 * 
 * @since 1.6
 */
public class TestProbeController extends AbstractKickerTest {

	private static final String ENCODING = "UTF-8";

	/** A rule making sure that a temporary folder exists for every test method (which is removed after the test). */
	@Rule
	public final TemporaryFolder tmpFolder = new TemporaryFolder(); // NOCS (@Rule must be public)

	private volatile File configFile;

	/**
	 * Default constructor.
	 */
	public TestProbeController() {
		// empty default constructor
	}

	@Before
	public void init() throws IOException {
		this.configFile = this.tmpFolder.newFile("adaptiveMonitoring.configFile");
	}

	/**
	 * This method does some cleanup after the test.
	 */
	@After
	public void cleanup() {
		this.tmpFolder.delete();
	}

	@Test
	public void testInitializationDefaultConfigLocation() {
		final Configuration configuration = ConfigurationFactory.createSingletonConfiguration();
		configuration.setProperty(ConfigurationFactory.WRITER_CLASSNAME, DummyWriter.class.getName());
		configuration.setProperty(ConfigurationFactory.ADAPTIVE_MONITORING_ENABLED, "true");
		final IMonitoringController ctrl = MonitoringController.createInstance(configuration);
		Assert.assertFalse(ctrl.isMonitoringTerminated());
		final List<String> list = ctrl.getProbePatternList();
		Assert.assertTrue(list.isEmpty());
		ctrl.terminateMonitoring();
	}

	@Test
	public void testInitializationWithCustomConfiguration() throws UnsupportedEncodingException, FileNotFoundException, InterruptedException {
		this.writeToConfigFile(new String[] { "+ *", "- * test.Test()", "test invalid line in config file", "- InvalidPatternException expected", });
		final Configuration configuration = ConfigurationFactory.createSingletonConfiguration();
		configuration.setProperty(ConfigurationFactory.WRITER_CLASSNAME, DummyWriter.class.getName());
		configuration.setProperty(ConfigurationFactory.ADAPTIVE_MONITORING_ENABLED, "true");
		configuration.setProperty(ConfigurationFactory.ADAPTIVE_MONITORING_CONFIG_FILE, this.configFile.getAbsolutePath());

		LogImplJUnit.disableThrowable(InvalidPatternException.class);
		final IMonitoringController ctrl = MonitoringController.createInstance(configuration);
		LogImplJUnit.reset();

		Assert.assertTrue(this.configFile.exists());
		final List<String> list = ctrl.getProbePatternList();
		Assert.assertFalse(list.isEmpty());
		Assert.assertArrayEquals(new String[] { "+*", "-* test.Test()", }, list.toArray());
		// add manual entries to list
		ctrl.activateProbe("void test.$1.Test()");
		final List<String> list2 = ctrl.getProbePatternList();
		Assert.assertArrayEquals(new String[] { "+*", "-* test.Test()", "+void test.$1.Test()", }, list2.toArray());
		Assert.assertFalse(ctrl.isMonitoringTerminated());
		ctrl.terminateMonitoring();
	}

	@Test
	public void testEnabledDisabledMatching() {
		{ // NOCS // adaptive enabled
			final Configuration configuration = ConfigurationFactory.createSingletonConfiguration();
			configuration.setProperty(ConfigurationFactory.WRITER_CLASSNAME, DummyWriter.class.getName());
			configuration.setProperty(ConfigurationFactory.ADAPTIVE_MONITORING_ENABLED, "true");
			final IMonitoringController ctrl = MonitoringController.createInstance(configuration);
			Assert.assertTrue(ctrl.isMonitoringEnabled());
			Assert.assertTrue(ctrl.isProbeActivated("void test.Test()"));
			ctrl.disableMonitoring();
			Assert.assertFalse(ctrl.isMonitoringEnabled());
			Assert.assertTrue(ctrl.isProbeActivated("void test.Test()"));
			ctrl.enableMonitoring();
			Assert.assertTrue(ctrl.isMonitoringEnabled());
			Assert.assertTrue(ctrl.isProbeActivated("void test.Test()"));
			ctrl.deactivateProbe("*");
			Assert.assertTrue(ctrl.isMonitoringEnabled());
			Assert.assertFalse(ctrl.isProbeActivated("void test.Test()"));
			ctrl.activateProbe("*");
			Assert.assertTrue(ctrl.isMonitoringEnabled());
			Assert.assertTrue(ctrl.isProbeActivated("void test.Test()"));
			ctrl.terminateMonitoring();
		}
		{ // NOCS // adaptive disabled
			final Configuration configuration = ConfigurationFactory.createSingletonConfiguration();
			configuration.setProperty(ConfigurationFactory.WRITER_CLASSNAME, DummyWriter.class.getName());
			configuration.setProperty(ConfigurationFactory.ADAPTIVE_MONITORING_ENABLED, "false");
			final IMonitoringController ctrl = MonitoringController.createInstance(configuration);
			ctrl.deactivateProbe("*");
			Assert.assertTrue(ctrl.isMonitoringEnabled());
			Assert.assertTrue(ctrl.isProbeActivated("void test.Test()"));
			ctrl.disableMonitoring();
			Assert.assertFalse(ctrl.isMonitoringEnabled());
			Assert.assertTrue(ctrl.isProbeActivated("void test.Test()"));
			ctrl.enableMonitoring();
			Assert.assertTrue(ctrl.isMonitoringEnabled());
			Assert.assertTrue(ctrl.isProbeActivated("void test.Test()"));
			ctrl.terminateMonitoring();
		}
	}

	@Test
	public void testAutomatedReadingFromConfigFile() throws UnsupportedEncodingException, FileNotFoundException, InterruptedException {
		final int readIntervall = 2;
		final Configuration configuration = ConfigurationFactory.createSingletonConfiguration();
		configuration.setProperty(ConfigurationFactory.WRITER_CLASSNAME, DummyWriter.class.getName());
		configuration.setProperty(ConfigurationFactory.ADAPTIVE_MONITORING_ENABLED, "true");
		configuration.setProperty(ConfigurationFactory.ADAPTIVE_MONITORING_CONFIG_FILE, this.configFile.getAbsolutePath());
		configuration.setProperty(ConfigurationFactory.ADAPTIVE_MONITORING_CONFIG_FILE_READ_INTERVALL, Integer.toString(readIntervall));

		this.writeToConfigFile(new String[] { "+ *", "- * test.Test()", });
		final IMonitoringController ctrl = MonitoringController.createInstance(configuration);

		Assert.assertTrue(this.configFile.exists());

		final List<String> list = ctrl.getProbePatternList();
		Assert.assertFalse(list.isEmpty());
		Assert.assertArrayEquals(new String[] { "+*", "-* test.Test()", }, list.toArray());

		this.writeToConfigFile(new String[] { "- *", "+ * test.Test(..)", });
		Thread.sleep((readIntervall * 1000) + 1000);
		final List<String> list2 = ctrl.getProbePatternList();
		Assert.assertArrayEquals(new String[] { "-*", "+* test.Test(..)", }, list2.toArray());

		this.writeToConfigFile(new String[] { "- * test.Test(..)", "+ public void test.Test()", }); // new content
		Thread.sleep((readIntervall * 1000) + 1000);
		final List<String> list3 = ctrl.getProbePatternList();
		Assert.assertArrayEquals(new String[] { "-* test.Test(..)", "+public void test.Test()", }, list3.toArray());

		Assert.assertFalse(ctrl.isMonitoringTerminated());
		ctrl.terminateMonitoring();
	}

	@Test
	public void testAutomatedWriteBackToConfigFile() throws IOException {
		final Configuration configuration = ConfigurationFactory.createSingletonConfiguration();
		configuration.setProperty(ConfigurationFactory.WRITER_CLASSNAME, DummyWriter.class.getName());
		configuration.setProperty(ConfigurationFactory.ADAPTIVE_MONITORING_ENABLED, "true");
		configuration.setProperty(ConfigurationFactory.ADAPTIVE_MONITORING_CONFIG_FILE, this.configFile.getAbsolutePath());
		configuration.setProperty(ConfigurationFactory.ADAPTIVE_MONITORING_CONFIG_FILE_UPDATE, "true");

		LogImplJUnit.disableThrowable(InvalidPatternException.class);
		final IMonitoringController ctrl = MonitoringController.createInstance(configuration);
		LogImplJUnit.reset();

		Assert.assertTrue(this.configFile.exists());
		final List<String> list = ctrl.getProbePatternList();
		Assert.assertTrue(list.isEmpty());

		// add manual entries to list
		ctrl.activateProbe("void test.Test()");
		ctrl.deactivateProbe("Test test.Test.getTest()");
		final List<String> list2 = this.readFromConfigFile();
		Assert.assertArrayEquals(new String[] { "+void test.Test()", "-Test test.Test.getTest()", }, list2.toArray());

		// replace entries in list
		final List<String> list3 = new ArrayList<String>();
		list3.add("- public * test.Test.get*()");
		list3.add("+ public void test.Test.getNothing()");
		list3.add("+ Test test.Test.getTest()");
		ctrl.setProbePatternList(list3);
		final List<String> list4 = this.readFromConfigFile();
		Assert.assertArrayEquals(new String[] { "-public * test.Test.get*()", "+public void test.Test.getNothing()", "+Test test.Test.getTest()", },
				list4.toArray());

		Assert.assertFalse(ctrl.isMonitoringTerminated());
		ctrl.terminateMonitoring();
	}

	// Reads the significant content of the config file.
	private List<String> readFromConfigFile() throws IOException {
		BufferedReader reader = null;
		try {
			reader = new BufferedReader(new InputStreamReader(new FileInputStream(this.configFile), TestProbeController.ENCODING));
			final List<String> strPatternList = new LinkedList<String>();
			String line;
			while ((line = reader.readLine()) != null) { // NOPMD
				if ((line.charAt(0) == '+') || (line.charAt(0) == '-')) {
					strPatternList.add(line);
				}
			}
			return strPatternList;
		} finally {
			if (null != reader) {
				reader.close();
			}
		}
	}

	// Replaces the old content of the config file with the given pattern and a few additional information.
	private void writeToConfigFile(final String[] pattern) throws UnsupportedEncodingException, FileNotFoundException, InterruptedException {
		Thread.sleep(1000); // enforce last modified timestamp to be different than before
		final PrintWriter pw = new PrintWriter(
				new BufferedWriter(new OutputStreamWriter(new FileOutputStream(this.configFile, false), TestProbeController.ENCODING)));
		pw.print("## Adaptive Monitoring Config File: ");
		pw.println(this.configFile.getAbsolutePath());
		pw.print("## written on: ");
		final DateFormat date = new SimpleDateFormat("yyyyMMdd'-'HHmmssSSS", Locale.US);
		date.setTimeZone(TimeZone.getTimeZone("UTC"));
		pw.println(date.format(new java.util.Date()));
		pw.println('#');
		for (final String s : pattern) {
			pw.println(s);
		}
		pw.close();
	}

	@Test
	public void testMatching() {
		final Configuration configuration = ConfigurationFactory.createSingletonConfiguration();
		configuration.setProperty(ConfigurationFactory.WRITER_CLASSNAME, DummyWriter.class.getName());
		configuration.setProperty(ConfigurationFactory.ADAPTIVE_MONITORING_ENABLED, "true");
		configuration.setProperty(ConfigurationFactory.ADAPTIVE_MONITORING_CONFIG_FILE, this.configFile.getAbsolutePath());
		final IMonitoringController ctrl = MonitoringController.createInstance(configuration);

		// generate test signature
		final String signature = "public void kicker.test.monitoring.junit.core.controller.TestProbeController.testIt()";

		// test methods
		final String pattern = "..* kicker..*.*(..)";

		LogImplJUnit.disableThrowable(InvalidPatternException.class);
		Assert.assertFalse(ctrl.activateProbe("InvalidPatternException expected"));
		LogImplJUnit.reset();

		Assert.assertTrue(ctrl.activateProbe(pattern));
		Assert.assertTrue(ctrl.isProbeActivated(signature));
		Assert.assertTrue(ctrl.deactivateProbe(pattern));
		Assert.assertFalse(ctrl.isProbeActivated(signature));
		ctrl.terminateMonitoring();
	}

	@Test
	public void testSpecialProbes() {
		final Configuration configuration = ConfigurationFactory.createSingletonConfiguration();
		configuration.setProperty(ConfigurationFactory.WRITER_CLASSNAME, DummyWriter.class.getName());
		configuration.setProperty(ConfigurationFactory.ADAPTIVE_MONITORING_ENABLED, "true");

		final IMonitoringController ctrl = MonitoringController.createInstance(configuration);

		final String memSwapSignature = SignatureFactory.createMemSwapSignature(); // %MEM_SWAP
		final String cpuSignature = SignatureFactory.createCPUSignature(); // %CPU

		Assert.assertTrue(ctrl.isProbeActivated(memSwapSignature)); // default is true
		ctrl.deactivateProbe(memSwapSignature); // this entry deactivates the MemSwapProbe
		Assert.assertFalse(ctrl.isProbeActivated(memSwapSignature));

		Assert.assertTrue(ctrl.isProbeActivated(cpuSignature)); // default is true
		ctrl.deactivateProbe(cpuSignature); // this entry deactivates the CpuProbe
		Assert.assertFalse(ctrl.isProbeActivated(cpuSignature));

		// Independent of 'cpuSignature' all specific signatures are active by default.
		Assert.assertTrue(ctrl.isProbeActivated(SignatureFactory.createCPUSignature(0))); // %CPU::0
		ctrl.deactivateProbe(SignatureFactory.createCPUSignature(0));
		Assert.assertFalse(ctrl.isProbeActivated(SignatureFactory.createCPUSignature(0)));
		Assert.assertTrue(ctrl.isProbeActivated(SignatureFactory.createCPUSignature(1))); // %CPU::1
		ctrl.deactivateProbe("%CPU::.*"); // regular expressions also allowed, this one deactivates all probes
		Assert.assertFalse(ctrl.isProbeActivated(SignatureFactory.createCPUSignature(0)));
		Assert.assertFalse(ctrl.isProbeActivated(SignatureFactory.createCPUSignature(1)));

		Assert.assertFalse(ctrl.isMonitoringTerminated());
		ctrl.terminateMonitoring();
	}
}
