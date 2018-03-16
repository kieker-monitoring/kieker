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

package kieker.analysisteetime.util.graph.export.graphml;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.function.Function;

import kieker.analysisteetime.util.graph.IGraph;
import kieker.analysisteetime.util.graph.mapping.SimpleFileNameMapper;
import kieker.analysisteetime.util.graph.util.FileExtension;

/**
 * @author Sören Henning
 *
 * @since 1.14
 */
public class GraphMLFileWriterStage extends GraphMLWriterStage {

	public GraphMLFileWriterStage(final Function<IGraph, String> fileNameMapper) {
		super(fileNameMapper.andThen(fileName -> {
			try {
				return new FileOutputStream(fileName);
			} catch (final FileNotFoundException e) {
				throw new IllegalArgumentException(e);
			}
		}));
	}

	public GraphMLFileWriterStage(final String outputDirectory) {
		this(new SimpleFileNameMapper(outputDirectory, FileExtension.GRAPHML));
	}

}
