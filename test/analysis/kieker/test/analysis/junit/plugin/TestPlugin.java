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

package kieker.test.analysis.junit.plugin; // NOCS (outer types)

import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import kieker.analysis.AnalysisController;
import kieker.analysis.exception.AnalysisConfigurationException;
import kieker.analysis.plugin.AbstractPlugin;
import kieker.analysis.repository.AbstractRepository;
import kieker.common.configuration.Configuration;

import kieker.test.analysis.util.plugin.filter.SimpleForwardFilterWithRepository;
import kieker.test.analysis.util.plugin.filter.SimpleSinkFilter;
import kieker.test.analysis.util.plugin.reader.SimpleListReader;
import kieker.test.analysis.util.repository.SimpleRepository;

/**
 * A simple test for the plugins in general. It tests for example if the chaining of different plugins does work.
 * 
 * @author Nils Christian Ehmke, Jan Waller
 */
public class TestPlugin {

	public TestPlugin() {
		// empty default constructor
	}

	@Test
	public void testPluginAttributes() {
		final Configuration myPluginConfig = new Configuration();
		// Set a name in order to test the getName() function below
		final String myPluginName = "name-ieuIyxLG";
		myPluginConfig.setProperty(AbstractPlugin.CONFIG_NAME, myPluginName);
		final SimpleForwardFilterWithRepository sourcePlugin = new SimpleForwardFilterWithRepository(myPluginConfig);
		Assert.assertEquals("Unexpected plugin name", myPluginName, sourcePlugin.getName());
		// Test if name and description from the annotation are returned correctly
		Assert.assertEquals("Unexpected plugin type name", SimpleForwardFilterWithRepository.FILTER_NAME, sourcePlugin.getPluginName());
		Assert.assertEquals("Unexpected plugin description", SimpleForwardFilterWithRepository.FILTER_DESCRIPTION, sourcePlugin.getPluginDescription());
	}

	@Test
	public void testRepository() {
		final Configuration myRepoConfig = new Configuration();
		// Set a name in order to test the getName() function below
		final String myRepoName = "name-22db22rLQ";
		myRepoConfig.setProperty(AbstractRepository.CONFIG_NAME, myRepoName);
		final SimpleRepository myRepo = new SimpleRepository(myRepoConfig);
		Assert.assertEquals("Unexpected repository name", myRepoName, myRepo.getName());
		// Test if name and description from the annotation are returned correctly
		Assert.assertEquals("Unexpected repository type name", SimpleRepository.REPOSITORY_NAME, myRepo.getRepositoryName());
		Assert.assertEquals("Unexpected repository description", SimpleRepository.REPOSITORY_DESCRIPTION, myRepo.getRepositoryDescription());
	}

	@Test
	public void testChaining() throws IllegalStateException, AnalysisConfigurationException {
		final Object testObject1 = new Object();
		final Object testObject2 = new Object();

		final SimpleRepository simpleRepository = new SimpleRepository(new Configuration());
		final SimpleListReader<Object> simpleListReader = new SimpleListReader<Object>(new Configuration());
		simpleListReader.addObject(testObject1);
		simpleListReader.addObject(testObject2);
		final SimpleForwardFilterWithRepository simpleFilter = new SimpleForwardFilterWithRepository(new Configuration());
		final SimpleSinkFilter<Object> simpleSinkPlugin = new SimpleSinkFilter<Object>(new Configuration());

		final AnalysisController analysisController = new AnalysisController();
		analysisController.registerRepository(simpleRepository);
		analysisController.registerReader(simpleListReader);
		analysisController.registerFilter(simpleFilter);
		analysisController.registerFilter(simpleSinkPlugin);

		/* Connect the plugins. */
		analysisController.connect(
				simpleListReader, SimpleListReader.OUTPUT_PORT_NAME,
				simpleFilter, SimpleForwardFilterWithRepository.INPUT_PORT_NAME);
		analysisController.connect(
				simpleFilter, SimpleForwardFilterWithRepository.OUTPUT_PORT_NAME,
				simpleSinkPlugin, SimpleSinkFilter.INPUT_PORT_NAME);

		analysisController.connect(simpleFilter, SimpleForwardFilterWithRepository.REPOSITORY_PORT_NAME, simpleRepository);

		analysisController.run();
		Assert.assertEquals(AnalysisController.STATE.TERMINATED, analysisController.getState());

		final List<Object> list = simpleSinkPlugin.getList();
		Assert.assertEquals(2, list.size());
		Assert.assertEquals(testObject1, list.get(0));
		Assert.assertEquals(testObject2, list.get(1));
	}
}
