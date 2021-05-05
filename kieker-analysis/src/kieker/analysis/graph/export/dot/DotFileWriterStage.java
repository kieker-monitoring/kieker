/***************************************************************************
 * Copyright 2020 Kieker Project (http://kieker-monitoring.net)
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

package kieker.analysis.graph.export.dot;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.function.Function;

import kieker.analysis.graph.IGraph;
import kieker.analysis.graph.mapping.SimpleFileNameMapper;
import kieker.analysis.graph.util.FileExtension;

/**
 * @author Sören Henning
 *
 * @since 1.14
 */
public class DotFileWriterStage extends DotWriterStage {

	public DotFileWriterStage(final Function<IGraph, Path> fileNameMapper) {
		this(fileNameMapper, new SimpleDotExportConfiguration());
	}

	public DotFileWriterStage(final Function<IGraph, Path> fileNameMapper, final DotExportConfiguration exportConfiguration) {
		super(fileNameMapper.andThen(fileName -> {
			try {
				return Files.newBufferedWriter(fileName, StandardCharsets.UTF_8);
			} catch (final IOException e) {
				throw new IllegalArgumentException(e);
			}
		}), exportConfiguration);
	}

	public DotFileWriterStage(final Path outputDirectory) {
		this(outputDirectory, new SimpleDotExportConfiguration());
	}

	public DotFileWriterStage(final Path outputDirectory, final DotExportConfiguration exportConfiguration) {
		this(new SimpleFileNameMapper(outputDirectory, FileExtension.DOT), exportConfiguration);
	}

}
