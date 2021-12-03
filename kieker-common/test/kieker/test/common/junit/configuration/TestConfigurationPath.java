/***************************************************************************
 * Copyright 2021 Kieker Project (http://kieker-monitoring.net)
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

package kieker.test.common.junit.configuration;

import java.io.File;

import org.junit.Assert;
import org.junit.Test;

import kieker.common.configuration.Configuration;

import kieker.test.common.junit.AbstractKiekerTest;

/**
 * @author Jan Waller
 *
 * @since 1.6
 */
public class TestConfigurationPath extends AbstractKiekerTest {

	private static String[] paths = {
		".", "",
		"a", "a",
		"./x", "x",
		"../x\\y\\.././x", "../x/x",
		"C:\\Temp\\x.txt", "C:/Temp/x.txt",
		"out/containerDependencyGraph.dot", "out/containerDependencyGraph.dot",
		"out" + File.separator + File.separator + "containerDependencyGraph.dot", "out/containerDependencyGraph.dot",
		"out/", "out/",
		"/", "/",
		"././", "",
	};

	/**
	 * Default constructor.
	 */
	public TestConfigurationPath() {
		// empty default constructor
	}

	/**
	 * Test for correct path conversion.
	 */
	@Test
	public void testPath() {
		Assert.assertTrue((paths.length % 2) == 0);
		for (int i = 0; i < paths.length; i += 2) {
			Assert.assertEquals(paths[i + 1], Configuration.convertToPath(paths[i]));
		}
	}

}
