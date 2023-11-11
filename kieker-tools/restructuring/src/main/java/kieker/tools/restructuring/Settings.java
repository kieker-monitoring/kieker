/***************************************************************************
 * Copyright (C) 2023 Kieker Project (http://kieker-monitoring.net)
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
package kieker.tools.restructuring;

import java.nio.file.Path;
import java.util.List;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.converters.PathConverter;

/**
 * All settings including command line parameters for the analysis.
 *
 * @author Serafim Simonov
 * @since 1.3.0
 */
public class Settings { // NOPMD data class

	@Parameter(names = { "-i",
		"--input" }, required = true, variableArity = true, converter = PathConverter.class, description = "Input architecture model directories")
	private List<Path> inputModelPaths;

	@Parameter(names = { "-o",
		"--output" }, required = true, converter = PathConverter.class, description = "Output architecture model directory")
	private Path outputDirectory;

	@Parameter(names = { "-e", "--experiment" }, required = true, description = "Experiment name")
	private String experimentName;

	@Parameter(names = { "-s",
		"--strategy" }, required = true, converter = MappingStrategyConverter.class, description = "Strategy identifier")
	private EMappingStrategy mappingStrat;

	@Parameter(names = "--eol", required = false, description = "End of line symbol")
	private String lineSeparator = System.lineSeparator();

	public List<Path> getInputModelPaths() {
		return this.inputModelPaths;
	}

	public Path getOutputDirectory() {
		return this.outputDirectory;
	}

	public String getExperimentName() {
		return this.experimentName;
	}

	public EMappingStrategy getMappingStrat() {
		return this.mappingStrat;
	}

	public char[] getLineSeparator() {
		return this.lineSeparator.toCharArray();
	}

	public void setLineSeparator(final String lineSeparator) {
		this.lineSeparator = lineSeparator;
	}
}
