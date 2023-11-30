/***************************************************************************
 * Copyright (C) 2023 Kieker Project (https://kieker-monitoring.net)
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
package kieker.tools.aul;

import java.nio.file.Path;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.converters.PathConverter;

import kieker.tools.aul.stages.INetworkCreator;

/**
 * All settings including command line parameters for the analysis.
 *
 * @author Reiner Jung
 * @since 2.0.0
 */
public class Settings { // NOPMD

	@Parameter(names = { "-i",
		"--input" }, required = false, converter = PathConverter.class, description = "Input model directory")
	private Path inputDirectory;

	@Parameter(names = { "-n", "--nodes" }, required = false, description = "Number of nodes in graph")
	private Integer nodes;

	@Parameter(names = { "-o",
		"--output" }, required = true, converter = PathConverter.class, description = "Output directory to store graphics and statistics")
	private Path outputDirectory;

	@Parameter(names = { "-M",
		"--component-map" }, required = false, converter = PathConverter.class, description = "Component, file and function map file")
	private Path componentMapFile;

	@Parameter(names = { "-m",
		"--creator-mode" }, required = false, converter = NetworkCreatorConverter.class, description = "Select topology generator")
	private INetworkCreator networkCreator;

	public Path getInputDirectory() {
		return this.inputDirectory;
	}

	public Integer getNodes() {
		return this.nodes;
	}

	public Path getOutputDirectory() {
		return this.outputDirectory;
	}

	public Path getComponentMapFile() {
		return this.componentMapFile;
	}

	public INetworkCreator getCreatorMode() {
		return this.networkCreator;
	}

}
