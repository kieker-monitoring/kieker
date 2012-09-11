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
import java.util.Calendar;
import java.util.List;

import junit.framework.Assert;

import org.junit.Test;

import kieker.common.configuration.Configuration;
import kieker.monitoring.core.configuration.ConfigurationFactory;
import kieker.monitoring.core.controller.IMonitoringController;
import kieker.monitoring.core.controller.MonitoringController;

/**
 * @author Björn Weißenfels
 */
public class TestProbeController {

	@Test
	public void testIt() {
		// generating config file, may not exist when starting the test
		String pathname = "META-INF/";
		final URL url = ClassLoader.getSystemResource(pathname);
		final String path = url.getFile();
		File file = null;
		try {
			pathname = URLDecoder.decode(path, "UTF-8") + "kieker.monitoring.adaptiveMonitoring.configFile";
			new File(pathname).createNewFile();
			file = new File(pathname);
		} catch (final IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		Assert.assertNotNull(file);
		Assert.assertTrue(file.exists());

		// load properties
		final Configuration configuration = ConfigurationFactory.createSingletonConfiguration();
		final IMonitoringController MC = MonitoringController.createInstance(configuration);

		// generate test signature
		// signature must be valid and in this project
		final String signature =
				"private java.util.List<java.util.Calendar> kieker.test.monitoring.junit.core.controller.TestProbeController.getToken(java.lang.String, java.util.Calendar)";

		// test methods
		final String pattern = "* kieker.*(*)";
		Assert.assertTrue(MC.activateProbe(pattern));
		Assert.assertTrue(MC.isActive(signature));
		Assert.assertFalse(MC.activateProbe(pattern)); // because it is already active
		Assert.assertTrue(MC.deactivateProbe(pattern));
		Assert.assertFalse(MC.isActive(signature));
		Assert.assertFalse(MC.deactivateProbe(pattern));

		file.delete();
		Assert.assertFalse(file.exists());
	}

	public List<Calendar> getToken(String name, Calendar calendar) {
		name = "";
		calendar = null;
		return null;
	}

}
