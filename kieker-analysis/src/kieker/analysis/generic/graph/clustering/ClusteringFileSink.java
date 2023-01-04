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
package kieker.analysis.generic.graph.clustering;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.google.common.graph.MutableNetwork;

import kieker.analysis.behavior.events.EntryCallEvent;
import kieker.analysis.behavior.model.EventSerializer;
import kieker.analysis.generic.graph.IEdge;
import kieker.analysis.generic.graph.INode;

import teetime.framework.AbstractConsumerStage;

/**
 * A sink stage, which returns all clusters and all noise objects. All elements of the clusters are
 * part of the JSON result.
 *
 * @param <N>
 *            node type
 * @param <E>
 *            edge type
 *
 * @author Lars Jürgensen
 * @since 2.0.0
 */
public class ClusteringFileSink<N extends INode, E extends IEdge> extends AbstractConsumerStage<Clustering<MutableNetwork<N, E>>> {

	private final ObjectMapper objectMapper;

	private final Path path;

	/**
	 * Create behavior model writer.
	 *
	 * @param path
	 *            path
	 */
	public ClusteringFileSink(final Path path) {
		this.objectMapper = new ObjectMapper();
		this.path = path;
	}

	@Override
	protected void execute(final Clustering<MutableNetwork<N, E>> clustering) throws IOException {
		this.logger.info("Write models to {}", this.path.toString());
		try (final BufferedWriter bw = Files.newBufferedWriter(this.path)) {
			this.objectMapper.enable(SerializationFeature.INDENT_OUTPUT);

			// TODO add the ability to change serializer
			// this custom serializer just prints the values array of an event
			final SimpleModule module = new SimpleModule();
			module.addSerializer(EntryCallEvent.class, new EventSerializer());
			this.objectMapper.registerModule(module);

			this.objectMapper.writeValue(bw, clustering);
		} catch (final IOException e) {
			this.logger.error("Writing to {} failed", this.path.toString());
		}
	}

}
