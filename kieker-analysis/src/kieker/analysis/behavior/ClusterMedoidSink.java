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
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;

import kieker.analysis.behavior.data.EntryCallEvent;
import kieker.analysis.behavior.model.BehaviorModel;
import kieker.analysis.behavior.model.EventSerializer;

import teetime.framework.AbstractConsumerStage;

/**
 * This is a sink stage, that returns a JSON object, for each cluster medoid.
 *
 * @author Lars Jürgensen
 * @since 2.0.0
 */
public class ClusterMedoidSink extends AbstractConsumerStage<BehaviorModel> {

	private final ObjectMapper objectMapper;

	private final Path path;

	private int clusterNumber = 0;

	/**
	 * Create behavior model writer.
	 *
	 * @param path
	 *            path fragment used for each medoid
	 */
	public ClusterMedoidSink(final Path path) {
		this.objectMapper = new ObjectMapper();
		this.path = path;
	}

	@Override
	protected void execute(final BehaviorModel model) throws Exception {
		// the name of the JSON objects contains the number of the cluster
		final Path numberedFilename = Paths.get(this.path.toString() + "_cluster_" + this.clusterNumber);
		this.clusterNumber++;

		this.logger.info("Write cluster medoid to " + numberedFilename);
		try (final BufferedWriter bw = Files.newBufferedWriter(numberedFilename)) {
			this.objectMapper.enable(SerializationFeature.INDENT_OUTPUT);

			// this custom serializer just prints the values array of an event
			final SimpleModule module = new SimpleModule();
			module.addSerializer(EntryCallEvent.class, new EventSerializer());
			this.objectMapper.registerModule(module);

			this.objectMapper.writeValue(bw, model);
		} catch (final IOException e) {
			this.logger.error("Cannot write file {}", numberedFilename.toString());
		}

	}

}
