/***************************************************************************
 * Copyright 2022 Kieker Project (http://kieker-monitoring.net)
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
package kieker.tools.collector;

import kieker.analysis.generic.sink.DataSink;
import kieker.analysis.generic.source.ISourceCompositeStage;
import kieker.common.exception.ConfigurationException;
import kieker.tools.source.SourceStageFactory;

import teetime.framework.Configuration;

/**
 * Analysis configuration for the data collector.
 *
 * @author Reiner Jung
 *
 * @since 1.15
 *
 */
public class CollectorConfiguration extends Configuration {

	private final DataSink consumer;
	private final ISourceCompositeStage sourceStage;

	/**
	 * Configure analysis.
	 *
	 * @param configuration
	 *            configuration for the collector
	 * @throws ConfigurationException
	 *             on configuration error
	 */
	public CollectorConfiguration(final kieker.common.configuration.Configuration configuration)
			throws ConfigurationException {
		this.sourceStage = SourceStageFactory.createSourceCompositeStage(configuration);

		this.consumer = new DataSink(configuration);

		this.connectPorts(this.sourceStage.getOutputPort(), this.consumer.getInputPort());
	}

	public DataSink getCounter() {
		return this.consumer;
	}

	public ISourceCompositeStage getSourceStage() {
		return this.sourceStage;
	}
}
