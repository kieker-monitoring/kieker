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
package kieker.analysis.examples.throughput;

import java.util.concurrent.Callable;

import kieker.analysis.AnalysisController;
import kieker.analysis.EmptyPassOnFilter;
import kieker.analysis.IAnalysisController;
import kieker.analysis.ObjectProducer;
import kieker.analysis.exception.AnalysisConfigurationException;
import kieker.common.configuration.Configuration;

/**
 * @author Nils Christian Ehmke
 * 
 * @since 1.10
 */
public class ThroughputAnalysis<T> {

	private final IAnalysisController ac = new AnalysisController();
	private int numNoopFilters;
	private int numInputObjects;
	private Callable<T> inputObjectCreator;

	public void setNumNoopFilters(final int numNoopFilters) {
		this.numNoopFilters = numNoopFilters;
	}

	public void setInput(final int numInputObjects, final Callable<T> inputObjectCreator) {
		this.numInputObjects = numInputObjects;
		this.inputObjectCreator = inputObjectCreator;
	}

	public void start() throws IllegalStateException, AnalysisConfigurationException {
		this.ac.run();
	}

	public void init() throws IllegalStateException, AnalysisConfigurationException {
		final Configuration producerConfig = new Configuration();
		producerConfig.setProperty(ObjectProducer.CONFIG_PROPERTY_NAME_OBJECTS_TO_CREATE, Long.toString(this.numInputObjects));
		final ObjectProducer<T> producer = new ObjectProducer<T>(producerConfig, this.ac, this.inputObjectCreator);

		EmptyPassOnFilter predecessor = new EmptyPassOnFilter(new Configuration(), this.ac);
		this.ac.connect(producer, ObjectProducer.OUTPUT_PORT_NAME, predecessor, EmptyPassOnFilter.INPUT_PORT_NAME);
		for (int idx = 0; idx < (this.numNoopFilters - 1); idx++) {
			final EmptyPassOnFilter newPredecessor = new EmptyPassOnFilter(new Configuration(), this.ac);
			this.ac.connect(predecessor, EmptyPassOnFilter.OUTPUT_PORT_NAME, newPredecessor, EmptyPassOnFilter.INPUT_PORT_NAME);
			predecessor = newPredecessor;
		}
	}

}
