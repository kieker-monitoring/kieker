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

package kieker.analysisteetime.util.graph.export.dot;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.function.Function;

import kieker.analysisteetime.util.graph.Graph;
import kieker.analysisteetime.util.graph.mapping.SimpleFileNameMapper;
import kieker.analysisteetime.util.graph.util.FileExtension;

/**
 * @author Sören Henning
 *
 * @since 1.13
 */
public class DotFileWriterStage extends DotWriterStage {

	public DotFileWriterStage(final Function<Graph, String> fileNameMapper) {
		this(fileNameMapper, new SimpleDotExportConfiguration());
	}

	public DotFileWriterStage(final Function<Graph, String> fileNameMapper, final DotExportConfiguration exportConfiguration) {
		super(fileNameMapper.andThen(fileName -> {
			try {
				// return new FileWriter(fileName); // Criticized by Findbugs
				return new OutputStreamWriter(new FileOutputStream(fileName), StandardCharsets.UTF_8);
			} catch (final IOException e) {
				throw new IllegalArgumentException(e);
			}
		}), exportConfiguration);
	}

	public DotFileWriterStage(final String outputDirectory) {
		this(outputDirectory, new SimpleDotExportConfiguration());
	}

	public DotFileWriterStage(final String outputDirectory, final DotExportConfiguration exportConfiguration) {
		this(new SimpleFileNameMapper(outputDirectory, FileExtension.DOT), exportConfiguration);
	}

}
