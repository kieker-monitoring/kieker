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
import java.nio.file.Paths;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

import teetime.framework.AbstractConsumerStage;

/**
 * This is a sink stage, that returns a JSON object, for each cluster medoid.
 *
 * @param <T>
 *            model type
 *
 * @author Lars Jürgensen
 * @since 2.0.0
 */
public class ClusterMedoidSink<T> extends AbstractConsumerStage<T> {

	private final ObjectMapper objectMapper;

	private final Path path;

	private int clusterNumber = 0; // NOPMD set for documentation purpose

	/**
	 * Create behavior model writer.
	 *
	 * @param path
	 *            path fragment used for each medoid
	 */
	public ClusterMedoidSink(final Path path, final StdSerializer<?>... serializers) {
		this.objectMapper = new ObjectMapper();
		if (serializers != null) {
			final SimpleModule module = new SimpleModule();
			for (final StdSerializer<?> serializer : serializers) {
				module.addSerializer(serializer);
			}
			this.objectMapper.registerModule(module);
		}
		this.path = path;
	}

	@Override
	protected void execute(final T model) throws Exception {
		// the name of the JSON objects contains the number of the cluster
		final Path numberedFilename = Paths.get(this.path.toString() + "_cluster_" + this.clusterNumber);
		this.clusterNumber++;

		this.logger.info("Write cluster medoid to " + numberedFilename);
		try (final BufferedWriter bw = Files.newBufferedWriter(numberedFilename)) {
			this.objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
			this.objectMapper.writeValue(bw, model);
		} catch (final IOException e) {
			this.logger.error("Cannot write file {}", numberedFilename.toString());
		}

	}

}
