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

import java.util.Collection;
import java.util.concurrent.Callable;

import kieker.analysis.AnalysisController;
import kieker.analysis.CollectorSink;
import kieker.analysis.EmptyPassOnFilter;
import kieker.analysis.IAnalysisController;
import kieker.analysis.ObjectProducer;
import kieker.analysis.StartTimestampFilter;
import kieker.analysis.StopTimestampFilter;
import kieker.analysis.exception.AnalysisConfigurationException;
import kieker.common.configuration.Configuration;
import kieker.panalysis.examples.throughput.TimestampObject;

/**
 * @author Nils Christian Ehmke
 * 
 * @since 1.10
 */
public class ThroughputTimestampAnalysis {

	private final IAnalysisController ac = new AnalysisController();
	private int numNoopFilters;
	private int numInputObjects;
	private Callable<TimestampObject> inputObjectCreator;
	private Collection<TimestampObject> timestampObjects;

	public void setNumNoopFilters(final int numNoopFilters) {
		this.numNoopFilters = numNoopFilters;
	}

	public void setInput(final int numInputObjects, final Callable<TimestampObject> inputObjectCreator) {
		this.numInputObjects = numInputObjects;
		this.inputObjectCreator = inputObjectCreator;
	}

	public void setTimestampObjects(final Collection<TimestampObject> timestampObjects) {
		this.timestampObjects = timestampObjects;
	}

	public void start() throws IllegalStateException, AnalysisConfigurationException {
		this.ac.run();
	}

	public void init() throws IllegalStateException, AnalysisConfigurationException {
		final Configuration producerConfig = new Configuration();
		producerConfig.setProperty(ObjectProducer.CONFIG_PROPERTY_NAME_OBJECTS_TO_CREATE, Long.toString(this.numInputObjects));
		final ObjectProducer<TimestampObject> producer = new ObjectProducer<TimestampObject>(producerConfig, this.ac, this.inputObjectCreator);

		final StartTimestampFilter startTimestampFilter = new StartTimestampFilter(new Configuration(), this.ac);
		EmptyPassOnFilter predecessor = new EmptyPassOnFilter(new Configuration(), this.ac);
		this.ac.connect(producer, ObjectProducer.OUTPUT_PORT_NAME, startTimestampFilter, StartTimestampFilter.INPUT_PORT_NAME);
		this.ac.connect(startTimestampFilter, StartTimestampFilter.OUTPUT_PORT_NAME, predecessor, EmptyPassOnFilter.INPUT_PORT_NAME);
		for (int idx = 0; idx < (this.numNoopFilters - 1); idx++) {
			final EmptyPassOnFilter newPredecessor = new EmptyPassOnFilter(new Configuration(), this.ac);
			this.ac.connect(predecessor, EmptyPassOnFilter.OUTPUT_PORT_NAME, newPredecessor, EmptyPassOnFilter.INPUT_PORT_NAME);
			predecessor = newPredecessor;
		}
		final StopTimestampFilter stopTimestampFilter = new StopTimestampFilter(new Configuration(), this.ac);
		final CollectorSink<TimestampObject> collectorSink = new CollectorSink<TimestampObject>(new Configuration(), this.ac, this.timestampObjects);

		this.ac.connect(predecessor, EmptyPassOnFilter.OUTPUT_PORT_NAME, stopTimestampFilter, StopTimestampFilter.INPUT_PORT_NAME);
		this.ac.connect(stopTimestampFilter, StopTimestampFilter.OUTPUT_PORT_NAME, collectorSink, CollectorSink.INPUT_PORT_NAME);
	}

}
