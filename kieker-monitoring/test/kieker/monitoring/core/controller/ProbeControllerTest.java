/***************************************************************************
 * Copyright 2019 Kieker Project (http://kieker-monitoring.net)
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

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import kieker.common.configuration.Configuration;
import kieker.monitoring.core.configuration.ConfigurationFactory;
import kieker.monitoring.core.configuration.ConfigurationKeys;

/**
 * @author Reiner Jung
 *
 * @since 1.15
 */
public class ProbeControllerTest {

	private static final String WHITELIST = "whitelist";
	private static final String BLACKLIST = "blacklist";
	private static final String CLASSNAME = "example.Class";
	private static final String OTHER_CLASSNAME = "example.Ohter";

	private ProbeController controller;

	
	public ProbeControllerTest() {
		// nothing to be done on construction of the test
	}
	
	@Before
	public void setUp() {
		final Configuration configuration = ConfigurationFactory.createDefaultConfiguration();

		configuration.setProperty(ConfigurationKeys.ADAPTIVE_MONITORING_ENABLED, true);
		configuration.setProperty(ConfigurationKeys.ADAPTIVE_MONITORING_CONFIG_FILE, "/tmp");
		configuration.setProperty(ConfigurationKeys.ADAPTIVE_MONITORING_CONFIG_FILE_UPDATE, true);
		configuration.setProperty(ConfigurationKeys.ADAPTIVE_MONITORING_CONFIG_FILE_READ_INTERVALL, 100);
		configuration.setProperty(ConfigurationKeys.ADAPTIVE_MONITORING_MAX_CACHE_SIZE, 10000);
		configuration.setProperty(ConfigurationKeys.ADAPTIVE_MONITORING_BOUNDED_CACHE_BEHAVIOUR, 3);

		final MonitoringController monitoringController = MonitoringController.createInstance(configuration);

		this.controller = new ProbeController(configuration);
		this.controller.setMonitoringController(monitoringController);
	}

	/**
	 * Test method for {@link kieker.monitoring.core.controller.ProbeController#activateProbe(java.lang.String)}.
	 */
	@Ignore // NOCS for now
	@Test
	public void testActivateProbe() {

		this.controller.activateProbe("+" + CLASSNAME);

		Assert.fail("Not yet implemented");
	}

	/**
	 * Test method for {@link kieker.monitoring.core.controller.ProbeController#deactivateProbe(java.lang.String)}.
	 */
	@Ignore  // NOCS for now
	@Test
	public void testDeactivateProbe() {
		Assert.fail("Not yet implemented");
	}

	/**
	 * Test method for {@link kieker.monitoring.core.controller.ProbeController#setProbePatternList(java.util.List, boolean)}.
	 */
	@Ignore  // NOCS for now
	@Test
	public void testSetProbePatternListListOfStringBoolean() {
		Assert.fail("Not yet implemented");
	}

	/**
	 * Test method for {@link kieker.monitoring.core.controller.ProbeController#setProbePatternList(java.util.List)}.
	 */
	@Ignore  // NOCS for now
	@Test
	public void testSetProbePatternListListOfString() {
		Assert.fail("Not yet implemented");
	}

	/**
	 * Test method for {@link kieker.monitoring.core.controller.ProbeController#getProbePatternList()}.
	 */
	@Ignore // NOCS for now we need to ignore it
	@Test
	public void testGetProbePatternList() {
		final List<String> patterns = this.controller.getProbePatternList();
		Assert.assertEquals("Before storing patterns, list should be empty", 0, patterns.size());

		final List<String> patternList = Arrays.asList(new String[] { "+" + CLASSNAME, "-" + OTHER_CLASSNAME });
		this.controller.setProbePatternList(patternList, false);

		final List<String> filledPatterns = this.controller.getProbePatternList();
		Assert.assertEquals("Before storing patterns, list should be empty", 2, filledPatterns.size());

		Assert.fail("Not yet implemented");
	}

	/**
	 * Test method for {@link kieker.monitoring.core.controller.ProbeController#getAllPatternParameters(java.lang.String)}.
	 */
	@Test
	public void testGetAllPatternParameters() {
		final Map<String, List<String>> result = this.controller.getAllPatternParameters(CLASSNAME);
		Assert.assertNull("There should be no patterns", result);

		final String[] values = new String[] { "a", "b", "c" };
		this.controller.addPatternParameter(CLASSNAME, WHITELIST, Arrays.asList(values));
		final Map<String, List<String>> result2 = this.controller.getAllPatternParameters(CLASSNAME);
		Assert.assertNotNull("Should contain patterns", result2);
		Assert.assertEquals("Should contain a string", WHITELIST, result2.keySet().toArray()[0]);

		final List<String> actual = result2.get(WHITELIST);

		Assert.assertArrayEquals("Should contain three elements", values, actual.toArray(new String[actual.size()]));
	}

	/**
	 * Test method for {@link kieker.monitoring.core.controller.ProbeController#deletePatternParameter(java.lang.String, java.lang.String)}.
	 */
	@Test
	public void testDeletePatternParameter() {
		final String[] values = new String[] { "a", "b", "c" };
		this.controller.addPatternParameter(CLASSNAME, WHITELIST, Arrays.asList(values));
		this.controller.deletePatternParameter(CLASSNAME, WHITELIST);

		final Map<String, List<String>> result = this.controller.getAllPatternParameters(CLASSNAME);
		Assert.assertEquals("Map must be empty", 0, result.size());
	}

	/**
	 * Test method for {@link kieker.monitoring.core.controller.ProbeController#clearPatternParameters(java.lang.String)}.
	 */
	@Test
	public void testClearPatternParameters() {
		final String[] values = new String[] { "a", "b", "c" };
		this.controller.addPatternParameter(CLASSNAME, WHITELIST, Arrays.asList(values));
		this.controller.clearPatternParameters(CLASSNAME);

		final Map<String, List<String>> result = this.controller.getAllPatternParameters(CLASSNAME);
		Assert.assertNull("Map must be null", result);
	}

	/**
	 * Test method for {@link kieker.monitoring.core.controller.ProbeController#addPatternParameter(java.lang.String, java.lang.String, java.util.List)}.
	 */
	@Test
	public void testAddPatternParameter() {
		final String[] whitelistValues = new String[] { "a", "b", "c" };
		this.controller.addPatternParameter(CLASSNAME, WHITELIST, Arrays.asList(whitelistValues));
		final Map<String, List<String>> resultW = this.controller.getAllPatternParameters(CLASSNAME);
		Assert.assertNotNull("Should contain patterns", resultW);
		Assert.assertEquals("Should contain a string", WHITELIST, resultW.keySet().toArray()[0]);

		final String[] blacklistValues = new String[] { "d", "e", "f" };
		this.controller.addPatternParameter(CLASSNAME, BLACKLIST, Arrays.asList(blacklistValues));
		final Map<String, List<String>> resultB = this.controller.getAllPatternParameters(CLASSNAME);
		Assert.assertNotNull("Should contain patterns", resultB);
		Assert.assertEquals("Number of entries do not match", 2, resultB.size());
		for (final String result : resultB.keySet()) {
			if (BLACKLIST.equals(result)) {
				final List<String> data = resultB.get(result);
				Assert.assertArrayEquals("Should contain a string", blacklistValues, data.toArray(new String[data.size()]));
			} else if (WHITELIST.equals(result)) {
				final List<String> data = resultB.get(result);
				Assert.assertArrayEquals("Should contain a string", whitelistValues, data.toArray(new String[data.size()]));
			} else {
				Assert.fail("Found parameters which have not be inserted before.");
			}
		}
	}

}
