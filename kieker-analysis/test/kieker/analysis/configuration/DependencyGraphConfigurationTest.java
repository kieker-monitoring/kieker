/***************************************************************************
 * Copyright 2020 Kieker Project (http://kieker-monitoring.net)
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

package kieker.analysis.configuration;

import java.io.File;
import java.net.URL;
import java.time.temporal.ChronoUnit;

import org.junit.Test;

import kieker.analysis.ExampleConfigurationTest;
import teetime.framework.Execution;

/**
 * @author Christian Wulf, Sören Henning
 *
 * @since 1.14
 */
public class DependencyGraphConfigurationTest {

	public DependencyGraphConfigurationTest() {
		// empty default constructor
	}

	@Test
	public void testWithLargeInputLog() throws Exception { // NOPMD (nothing to assert)
		// from within Eclipse:
		// . = <absolute path>/kieker/kieker-analysis/build-eclipse/kieker/analysisteetime/
		// / = <absolute path>/kieker/kieker-analysis/build-eclipse/
		final URL projectDir = ExampleConfigurationTest.class.getResource("/.");
		final File importDirectory = new File(projectDir.getFile(), "kieker-20170805-132418-9229368724068-UTC--KIEKER");
		final File exportDirectory = new File(projectDir.getFile());

		final DependencyGraphConfiguration configuration = new DependencyGraphConfiguration(importDirectory, ChronoUnit.NANOS, exportDirectory);
		final Execution<DependencyGraphConfiguration> execution = new Execution<>(configuration);
		execution.executeBlocking();
	}
}
