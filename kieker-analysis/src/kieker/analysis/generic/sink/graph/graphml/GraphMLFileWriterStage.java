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

package kieker.analysis.generic.sink.graph.graphml;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.function.Function;

import kieker.analysis.generic.graph.IEdge;
import kieker.analysis.generic.graph.IGraph;
import kieker.analysis.generic.graph.INode;
import kieker.analysis.generic.graph.mapping.SimpleFileNameMapper;

/**
 * @author Sören Henning
 *
 * @since 1.14
 */
public class GraphMLFileWriterStage<N extends INode, E extends IEdge> extends GraphMLWriterStage<N,E> {

	private static final String GRAPHML = "graphml";

	public GraphMLFileWriterStage(final Function<IGraph<N,E>, Path> fileNameMapper) {
		super(fileNameMapper.andThen(fileName -> {
			try {
				return Files.newOutputStream(fileName, StandardOpenOption.CREATE);
			} catch (final IOException e) {
				throw new IllegalArgumentException(e);
			}
		}));
	}

	public GraphMLFileWriterStage(final Path outputDirectory) {
		this(new SimpleFileNameMapper<>(outputDirectory, GRAPHML));
	}

}
