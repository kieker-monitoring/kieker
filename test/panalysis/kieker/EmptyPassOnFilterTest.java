/***************************************************************************
 * Copyright 2014 Kieker Project (http://kieker-monitoring.net)
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
package kieker;

import org.junit.Test;

import kieker.analysis.AnalysisController;
import kieker.analysis.CacheFilter;
import kieker.analysis.EmptyPassOnFilter;
import kieker.analysis.IAnalysisController;
import kieker.analysis.exception.AnalysisConfigurationException;
import kieker.analysis.plugin.reader.filesystem.FSReader;
import kieker.common.configuration.Configuration;

/**
 * @author Nils Christian Ehmke
 * 
 * @since 1.10
 */
public class EmptyPassOnFilterTest {

	private static final int NUMBER_OF_EMPTY_PASS_ON_FILTERS = 1000;

	@Test
	public void test() throws IllegalStateException, AnalysisConfigurationException {
		final IAnalysisController ac = new AnalysisController();

		final Configuration fsReaderConfiguration = new Configuration();
		fsReaderConfiguration.setProperty(FSReader.CONFIG_PROPERTY_NAME_INPUTDIRS,
				"examples/userguide/ch5--trace-monitoring-aspectj/testdata/kieker-20100830-082225522-UTC");
		final FSReader reader = new FSReader(fsReaderConfiguration, ac);

		final CacheFilter cacheFilter = new CacheFilter(new Configuration(), ac);

		EmptyPassOnFilter predecessor = new EmptyPassOnFilter(new Configuration(), ac);
		ac.connect(reader, FSReader.OUTPUT_PORT_NAME_RECORDS, cacheFilter, CacheFilter.INPUT_PORT_NAME);
		ac.connect(cacheFilter, CacheFilter.OUTPUT_PORT_NAME, predecessor, EmptyPassOnFilter.INPUT_PORT_NAME);
		for (int idx = 0; idx < (NUMBER_OF_EMPTY_PASS_ON_FILTERS - 1); idx++) {
			final EmptyPassOnFilter newPredecessor = new EmptyPassOnFilter(new Configuration(), ac);
			ac.connect(predecessor, EmptyPassOnFilter.OUTPUT_PORT_NAME, newPredecessor, EmptyPassOnFilter.INPUT_PORT_NAME);
			predecessor = newPredecessor;
		}

		ac.run();
	}

}
