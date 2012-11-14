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

import kieker.common.configuration.Configuration;
import kieker.common.logging.LogImplJUnit;
import kieker.monitoring.core.configuration.ConfigurationFactory;
import kieker.monitoring.core.controller.IMonitoringController;
import kieker.monitoring.core.controller.MonitoringController;
import kieker.monitoring.core.signaturePattern.InvalidPatternException;
import kieker.monitoring.writer.DummyWriter;

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

	private volatile File configFile;
	private final static String ENCODING = "UTF-8";

	public TestProbeController() {
		// empty default constructor
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
			configuration.setProperty(ConfigurationFactory.WRITER_CLASSNAME, DummyWriter.class.getName());
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
			configuration.setProperty(ConfigurationFactory.WRITER_CLASSNAME, DummyWriter.class.getName());
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
	public void testAutomatedReadingFromConfigFile() throws UnsupportedEncodingException, FileNotFoundException, InterruptedException {
		final int READ_INTERVALL = 2;
		final Configuration configuration = ConfigurationFactory.createSingletonConfiguration();
		configuration.setProperty(ConfigurationFactory.WRITER_CLASSNAME, DummyWriter.class.getName());
		configuration.setProperty(ConfigurationFactory.ADAPTIVE_MONITORING_ENABLED, "true");
		configuration.setProperty(ConfigurationFactory.ADAPTIVE_MONITORING_CONFIG_FILE, this.configFile.getAbsolutePath());
		configuration.setProperty(ConfigurationFactory.ADAPTIVE_MONITORING_CONFIG_FILE_READ_INTERVALL, Integer.toString(READ_INTERVALL));

		this.writeToConfigFile(new String[] { "+ *", "- * test.Test()", });
		final IMonitoringController ctrl = MonitoringController.createInstance(configuration);

		Assert.assertTrue(this.configFile.exists());

		final List<String> list = ctrl.getProbePatternList();
		Assert.assertFalse(list.isEmpty());
		Assert.assertArrayEquals(new String[] { "+*", "-* test.Test()", }, list.toArray());

		this.writeToConfigFile(new String[] { "- *", "+ * test.Test(..)", });
		Thread.sleep((READ_INTERVALL * 1000) + 1000);
		final List<String> list2 = ctrl.getProbePatternList();
		Assert.assertArrayEquals(new String[] { "-*", "+* test.Test(..)", }, list2.toArray());

		this.writeToConfigFile(new String[] { "- * test.Test(..)", "+ public void test.Test()", });// new content
		Thread.sleep((READ_INTERVALL * 1000) + 1000);
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
		Assert.assertArrayEquals(new String[] { "-public * test.Test.get*()", "+public void test.Test.getNothing()", "+Test test.Test.getTest()", }, list4.toArray());

		Assert.assertFalse(ctrl.isMonitoringTerminated());
		ctrl.terminateMonitoring();
	}

	/*
	 * Reads the significant content of the config file.
	 */
	private List<String> readFromConfigFile() throws IOException {
		final BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(this.configFile), TestProbeController.ENCODING));
		final List<String> strPatternList = new LinkedList<String>();
		String line;
		while ((line = reader.readLine()) != null) {
			if ((line.charAt(0) == '+') || (line.charAt(0) == '-')) {
				strPatternList.add(line);
			}
		}
		return strPatternList;
	}

	/*
	 * Replaces the old content of the config file with the given pattern and a few additional information.
	 */
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
		final String signature = "public void kieker.test.monitoring.junit.core.controller.TestProbeController.testIt()";

		// test methods
		final String pattern = "..* kieker..*.*(..)";

		LogImplJUnit.disableThrowable(InvalidPatternException.class);
		Assert.assertFalse(ctrl.activateProbe("InvalidPatternException expected"));
		LogImplJUnit.reset();

		Assert.assertTrue(ctrl.activateProbe(pattern));
		Assert.assertTrue(ctrl.isProbeActivated(signature));
		Assert.assertTrue(ctrl.deactivateProbe(pattern));
		Assert.assertFalse(ctrl.isProbeActivated(signature));
		ctrl.terminateMonitoring();
	}
}
