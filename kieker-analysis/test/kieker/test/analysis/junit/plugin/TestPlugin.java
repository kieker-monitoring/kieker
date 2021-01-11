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

package kieker.test.analysis.junit.plugin; // NOCS (outer types)

import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import kieker.analysis.AnalysisController;
import kieker.analysis.IAnalysisController;
import kieker.analysis.analysisComponent.AbstractAnalysisComponent;
import kieker.analysis.exception.AnalysisConfigurationException;
import kieker.analysis.plugin.filter.forward.ListCollectionFilter;
import kieker.analysis.plugin.reader.filesystem.FSReader;
import kieker.analysis.plugin.reader.list.ListReader;
import kieker.common.configuration.Configuration;

import kieker.test.analysis.util.plugin.filter.SimpleForwardFilterWithRepository;
import kieker.test.analysis.util.repository.SimpleRepository;
import kieker.test.common.junit.AbstractKiekerTest;

/**
 * A simple test for the plugins in general. It tests for example if the chaining of different plugins does work.
 *
 * @author Nils Christian Ehmke, Jan Waller
 *
 * @since 1.6
 */
public class TestPlugin extends AbstractKiekerTest {

	/**
	 * Default constructor.
	 */
	public TestPlugin() {
		// empty default constructor
	}

	/**
	 * This method tests some attributes of plugins.
	 */
	@Test
	public void testPluginAttributes() {
		final IAnalysisController ac = new AnalysisController();
		final Configuration myPluginConfig = new Configuration();
		// Set a name in order to test the getName() function below
		final String myPluginName = "name-ieuIyxLG";
		myPluginConfig.setProperty(AbstractAnalysisComponent.CONFIG_NAME, myPluginName);
		final SimpleForwardFilterWithRepository sourcePlugin = new SimpleForwardFilterWithRepository(myPluginConfig, ac);
		Assert.assertEquals("Unexpected plugin name", myPluginName, sourcePlugin.getName());
		// Test if name and description from the annotation are returned correctly
		Assert.assertEquals("Unexpected plugin type name", SimpleForwardFilterWithRepository.FILTER_NAME, sourcePlugin.getPluginName());
		Assert.assertEquals("Unexpected plugin description", SimpleForwardFilterWithRepository.FILTER_DESCRIPTION, sourcePlugin.getPluginDescription());
	}

	/**
	 * This method tests whether the repository class works.
	 */
	@Test
	public void testRepository() {
		final IAnalysisController ac = new AnalysisController();
		final Configuration myRepoConfig = new Configuration();
		// Set a name in order to test the getName() function below
		final String myRepoName = "name-22db22rLQ";
		myRepoConfig.setProperty(AbstractAnalysisComponent.CONFIG_NAME, myRepoName);
		final SimpleRepository myRepo = new SimpleRepository(myRepoConfig, ac);
		Assert.assertEquals("Unexpected repository name", myRepoName, myRepo.getName());
		// Test if name and description from the annotation are returned correctly
		Assert.assertEquals("Unexpected repository type name", SimpleRepository.REPOSITORY_NAME, myRepo.getRepositoryName());
		Assert.assertEquals("Unexpected repository description", SimpleRepository.REPOSITORY_DESCRIPTION, myRepo.getRepositoryDescription());
	}

	/**
	 * This method tests whether the chaining of filters, readers, and repositories is working.
	 *
	 * @throws IllegalStateException
	 *             If something went wrong during the test.
	 * @throws AnalysisConfigurationException
	 *             If something went wrong during the test.
	 */
	@Test
	public void testChaining() throws IllegalStateException, AnalysisConfigurationException {
		final Object testObject1 = new Object();
		final Object testObject2 = new Object();

		final IAnalysisController analysisController = new AnalysisController();

		final SimpleRepository simpleRepository = new SimpleRepository(new Configuration(), analysisController);
		final ListReader<Object> simpleListReader = new ListReader<>(new Configuration(), analysisController);
		simpleListReader.addObject(testObject1);
		simpleListReader.addObject(testObject2);
		final SimpleForwardFilterWithRepository simpleFilter = new SimpleForwardFilterWithRepository(new Configuration(), analysisController);
		final ListCollectionFilter<Object> simpleSinkPlugin = new ListCollectionFilter<>(new Configuration(), analysisController);

		// Connect the plugins.
		analysisController.connect(
				simpleListReader, ListReader.OUTPUT_PORT_NAME,
				simpleFilter, SimpleForwardFilterWithRepository.INPUT_PORT_NAME);
		analysisController.connect(
				simpleFilter, SimpleForwardFilterWithRepository.OUTPUT_PORT_NAME,
				simpleSinkPlugin, ListCollectionFilter.INPUT_PORT_NAME);

		analysisController.connect(simpleFilter, SimpleForwardFilterWithRepository.REPOSITORY_PORT_NAME, simpleRepository);

		analysisController.run();
		Assert.assertEquals(AnalysisController.STATE.TERMINATED, analysisController.getState());

		final List<Object> list = simpleSinkPlugin.getList();
		Assert.assertEquals(2, list.size());
		Assert.assertEquals(testObject1, list.get(0));
		Assert.assertEquals(testObject2, list.get(1));
	}

	/**
	 * This method tests that the analysis controller makes sure that component names are unique.
	 */
	@Test
	public void testComponentNameRegistering() {
		final IAnalysisController ac = new AnalysisController();

		final Configuration readerConfig1 = new Configuration();
		final Configuration readerConfig2 = new Configuration();

		readerConfig1.setProperty(AbstractAnalysisComponent.CONFIG_NAME, "reader");
		readerConfig2.setProperty(AbstractAnalysisComponent.CONFIG_NAME, "reader");

		final FSReader reader1 = new FSReader(readerConfig1, ac);
		final FSReader reader2 = new FSReader(readerConfig2, ac);

		// make sure the first reader is named as intended
		Assert.assertEquals("reader", reader1.getName());

		// make sure the the second reader is named beginning with the simple class name
		Assert.assertTrue(reader2.getName().startsWith(FSReader.class.getSimpleName()));

		// make sure that both reader names differ
		Assert.assertNotEquals(reader1.getName(), reader2.getName());
	}
}
