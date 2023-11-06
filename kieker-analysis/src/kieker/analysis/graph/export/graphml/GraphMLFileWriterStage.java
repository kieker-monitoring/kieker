/***************************************************************************
 * Copyright 2023 Kieker Project (http://kieker-monitoring.net)
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

package kieker.analysis.graph.export.graphml;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.function.Function;

import kieker.analysis.graph.IGraph;
import kieker.analysis.graph.mapping.SimpleFileNameMapper;
import kieker.analysis.graph.util.FileExtension;

/**
 * @author Sören Henning
 *
 * @since 1.14
 */
public class GraphMLFileWriterStage extends GraphMLWriterStage {

	public GraphMLFileWriterStage(final Function<IGraph, Path> fileNameMapper) {
		super(fileNameMapper.andThen(fileName -> {
			try {
				return Files.newOutputStream(fileName, StandardOpenOption.CREATE);
			} catch (final IOException e) {
				throw new IllegalArgumentException(e);
			}
		}));
	}

	public GraphMLFileWriterStage(final Path outputDirectory) {
		this(new SimpleFileNameMapper(outputDirectory, FileExtension.GRAPHML));
	}

}
