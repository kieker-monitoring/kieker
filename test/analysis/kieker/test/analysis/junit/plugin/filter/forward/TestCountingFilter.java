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

package kieker.test.analysis.junit.plugin.filter.forward;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import kieker.analysis.AnalysisController;
import kieker.analysis.exception.AnalysisConfigurationException;
import kieker.analysis.plugin.filter.forward.CountingFilter;
import kieker.common.configuration.Configuration;

import kieker.test.analysis.util.plugin.reader.SimpleListReader;

/**
 * This test is for the class {@link CountingFilter}.
 * 
 * @author Nils Christian Ehmke, Jan Waller
 */
public class TestCountingFilter {

	private AnalysisController analysisController;
	private SimpleListReader<Object> simpleListReader;
	private CountingFilter countingFilter;

	public TestCountingFilter() {
		// empty default constructor
	}

	@Before
	public void before() throws IllegalStateException, AnalysisConfigurationException {
		this.analysisController = new AnalysisController();
		this.countingFilter = new CountingFilter(new Configuration());
		this.simpleListReader = new SimpleListReader<Object>(new Configuration());
		this.analysisController.registerReader(this.simpleListReader);
		this.analysisController.registerFilter(this.countingFilter);
		this.analysisController.connect(this.simpleListReader, SimpleListReader.OUTPUT_PORT_NAME, this.countingFilter, CountingFilter.INPUT_PORT_NAME_EVENTS);
	}

	@Test
	public void testNormal() throws IllegalStateException, AnalysisConfigurationException {
		this.simpleListReader.addObject(new Object());
		this.simpleListReader.addObject(new Object());
		this.simpleListReader.addObject(new Object());
		Assert.assertEquals(0, this.countingFilter.getMessageCount());
		this.analysisController.run();
		Assert.assertEquals(3, this.countingFilter.getMessageCount());
	}

	@Test
	public void testConcurrently() throws IllegalStateException, AnalysisConfigurationException {
		this.simpleListReader.addObject(new Object());
		// register multiple readers (they fire concurrently)
		final SimpleListReader<Object> reader2 = new SimpleListReader<Object>(new Configuration());
		this.analysisController.registerReader(reader2);
		this.analysisController.connect(reader2, SimpleListReader.OUTPUT_PORT_NAME, this.countingFilter, CountingFilter.INPUT_PORT_NAME_EVENTS);
		reader2.addObject(new Object());
		final SimpleListReader<Object> reader3 = new SimpleListReader<Object>(new Configuration());
		this.analysisController.registerReader(reader3);
		this.analysisController.connect(reader3, SimpleListReader.OUTPUT_PORT_NAME, this.countingFilter, CountingFilter.INPUT_PORT_NAME_EVENTS);
		reader3.addObject(new Object());
		Assert.assertEquals(0, this.countingFilter.getMessageCount());
		this.analysisController.run();
		Assert.assertEquals(3, this.countingFilter.getMessageCount());
	}

	@Test
	public void testDifferentClasses() throws IllegalStateException, AnalysisConfigurationException {
		this.simpleListReader.addObject(Long.valueOf(10));
		this.simpleListReader.addObject(null);
		this.simpleListReader.addObject("");
		Assert.assertEquals(0, this.countingFilter.getMessageCount());
		this.analysisController.run();
		Assert.assertEquals(2, this.countingFilter.getMessageCount());
	}
}
