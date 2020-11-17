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

package kieker.analysis;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.net.URL;

import org.eclipse.emf.common.util.EMap;
import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import org.junit.Test;

import kieker.analysis.model.DeploymentModelPrinter;
import kieker.analysis.util.ComposedKey;
import kieker.analysisteetime.model.analysismodel.deployment.DeployedOperation;
import kieker.analysisteetime.model.analysismodel.deployment.DeploymentModel;
import kieker.analysisteetime.model.analysismodel.execution.AggregatedInvocation;

import teetime.framework.Execution;

/**
 * Class that executes the {@link ExampleConfiguration}. This is, so far, for
 * testing the current development only.
 *
 * @author Sören Henning
 *
 * @since 1.14
 *
 */
public class ExampleConfigurationTest {

	// @Rule
	// public TemporaryFolder tempFolder = new TemporaryFolder();

	public ExampleConfigurationTest() {
		// default empty constructor
	}

	@Test
	public void testCurrentStateOfDevelopment() throws FileNotFoundException, URISyntaxException, UnsupportedEncodingException {
		// use the classloader to identify resources in a location-independent way
		// from: https://stackoverflow.com/questions/24499692/access-resources-in-unit-tests
		// => works with Eclipse and Gradle
		final ClassLoader classLoader = ExampleConfigurationTest.class.getClassLoader();

		// from within Eclipse:
		// . = <absolute
		// path>/kieker/kieker-analysis/build-eclipse/kieker/analysisteetime/
		// / = <absolute path>/kieker/kieker-analysis/build-eclipse/
		final URL projectDir = ExampleConfigurationTest.class.getResource("/");
		// final File importDirectory = new
		// File("C:/Users/Soeren/Desktop/jedit-records/kieker-20170115-163405515-UTC-Leonard-KIEKER");
		// final File importDirectory = new
		// File("C:/Users/Soeren/Desktop/jedit-records/kieker-20170505-083525426-UTC-DESKTOP-UTJ4LDE-KIEKER");
		// final File importDirectory = new File(projectDir.getFile(),
		// "kieker-20170805-132412153-UTC-Nogge-PC-KIEKER-SINGLETON");
		final URL importDirectoryURL = classLoader.getResource("kieker-20170805-132412153-UTC-Nogge-PC-KIEKER-SINGLETON");
		final File importDirectory = new File(importDirectoryURL.toURI());
		// final File importDirectory = new
		// File("../kieker-examples/userguide/ch5--trace-monitoring-aspectj/testdata/kieker-20141008-101258768-UTC");
		// final File importDirectory = new File("C:/Users/Soeren/Desktop/Neuer
		// Ordner/kieker-20141103-190203561-UTC-j2eeservice-KIEKER-TEST");

		// final File importDirectory = new
		// File("../kieker-examples/userguide/ch5--trace-monitoring-aspectj/testdata/kieker-20100830-082225522-UTC");
		// final File exportDirectory = new
		// File("C:/Users/Soeren/Desktop/kieker-output");
		// final File exportDirectory = tempFolder.getRoot();
		final File exportDirectory = new File(projectDir.getFile());

		final ExampleConfiguration configuration = new ExampleConfiguration(importDirectory, exportDirectory);
		final Execution<ExampleConfiguration> analysis = new Execution<>(configuration);
		analysis.executeBlocking();

		final DeploymentModel deploymentModel = configuration.getDeploymentModel();
		final File file = new File(projectDir.getFile(), "model.txt");
		final PrintStream printStream = new PrintStream(file, "UTF-8");
		final DeploymentModelPrinter deploymentModelPrinter = new DeploymentModelPrinter(printStream);
		// final DeploymentModelPrinter deploymentModelPrinter = new
		// DeploymentModelPrinter(System.out);
		deploymentModelPrinter.print(deploymentModel);

		// StatisticsModel statisticsModel = configuration.getStatisticsModel();

		final EMap<ComposedKey<DeployedOperation, DeployedOperation>, AggregatedInvocation> aggregatedInvocations = configuration
				.getExecutionModel().getAggregatedInvocations();
		// contains [1, 1, 50] in any order
		Assert.assertThat(aggregatedInvocations.values().size(), CoreMatchers.is(3));
	}
}
