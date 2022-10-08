/***************************************************************************
 * Copyright (C) 2017 iObserve Project (https://www.iobserve-devops.net)
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
package kieker.analysis.behavior;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;

import kieker.analysis.behavior.clustering.Clustering;
import kieker.analysis.behavior.data.PayloadAwareEntryCallEvent;
import kieker.analysis.behavior.model.BehaviorModel;
import kieker.analysis.behavior.model.EventSerializer;

import teetime.framework.AbstractConsumerStage;

/**
 * A sink stage, which returns all clusters and all noise objects. All elements of the clusters are
 * part of the JSON result
 *
 * @author Lars Jürgensen
 *
 */
public class ClusteringSink extends AbstractConsumerStage<Clustering<BehaviorModel>> {

	private static final Logger LOGGER = LoggerFactory.getLogger(ClusteringSink.class);

	private final ObjectMapper objectMapper;

	private final String filename;

	/**
	 * Create behavior model writer.
	 *
	 * @param baseUrl
	 *            base url
	 */
	public ClusteringSink(final String filename) {
		this.objectMapper = new ObjectMapper();
		this.filename = filename;
	}

	@Override
	protected void execute(final Clustering<BehaviorModel> clustering) throws IOException {
		ClusteringSink.LOGGER.info("Write models to " + this.filename);
		final FileWriter fw = new FileWriter(this.filename);
		final BufferedWriter bw = new BufferedWriter(fw);
		this.objectMapper.enable(SerializationFeature.INDENT_OUTPUT);

		// this custom serializer just prints the values array of an event
		final SimpleModule module = new SimpleModule();
		module.addSerializer(PayloadAwareEntryCallEvent.class, new EventSerializer());
		this.objectMapper.registerModule(module);

		this.objectMapper.writeValue(bw, clustering);
		fw.close();
	}

}
