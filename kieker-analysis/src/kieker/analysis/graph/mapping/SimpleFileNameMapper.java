/***************************************************************************
 * Copyright 2021 Kieker Project (http://kieker-monitoring.net)
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

package kieker.analysis.graph.mapping;

import java.nio.file.Path;
import java.util.function.Function;

import kieker.analysis.graph.IGraph;
import kieker.analysis.graph.util.FileExtension;

/**
 * This function maps a graph to a file name with a specific pattern.
 * Pattern is defined as: output directory + graph name + file extension
 *
 * @author Sören Henning
 *
 * @since 1.14
 */
public class SimpleFileNameMapper implements Function<IGraph, Path> {

	private final Path outputDirectory;
	private final FileExtension fileExtension;

	/**
	 * Create a simple file mapper.
	 *
	 * @param outputDirectory
	 *            output directory path
	 * @param fileExtension
	 *            file extension for the graph
	 */
	public SimpleFileNameMapper(final Path outputDirectory, final FileExtension fileExtension) {
		this.outputDirectory = outputDirectory;
		this.fileExtension = fileExtension;
	}

	@Override
	public Path apply(final IGraph graph) {
		return this.outputDirectory.resolve(String.format("%s.%s", graph.getName(), this.fileExtension));
	}

}
