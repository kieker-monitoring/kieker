/***************************************************************************
 * Copyright 2018 Kieker Project (http://kieker-monitoring.net)
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

package kieker.analysisteetime.util.graph.mapping;

import java.util.function.Function;

import kieker.analysisteetime.util.graph.Graph;
import kieker.analysisteetime.util.graph.util.FileExtension;

/**
 * This function maps a graph to a file name with the pattern:
 * output directory + graph name + file extension
 *
 * @author Sören Henning
 *
 * @since 1.14
 */
public class SimpleFileNameMapper implements Function<Graph, String> {

	private final String outputDirectory;
	private final FileExtension fileExtension;

	public SimpleFileNameMapper(final String outputDirectory, final FileExtension fileExtension) {
		this.outputDirectory = outputDirectory;
		this.fileExtension = fileExtension;
	}

	@Override
	public String apply(final Graph graph) {
		return this.outputDirectory + '/' + graph.getName() + '.' + this.fileExtension;
	}

}
