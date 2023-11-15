/***************************************************************************
 * Copyright (C) 2021 Kieker Project (https://kieker-monitoring.net)
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
package kieker.tools.mop;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.converters.DoubleConverter;
import com.beust.jcommander.converters.PathConverter;

/**
 * All settings including command line parameters for the analysis.
 *
 * @author Reiner Jung
 * @since 2.0.0
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

	@Parameter(converter = OperationConverter.class, required = true, description = "Specify an operation merge, select", validateWith = OperationValidator.class)
	private EOperation operation;

	@Parameter(names = { "-s",
		"--selection-criteria" }, required = false, converter = PathConverter.class, description = "Element selection criteria file")
	private Path selectionCriteriaPath;

	@Parameter(names = { "-t",
		"--threshold" }, required = false, converter = DoubleConverter.class,
			description = "Threshold for accepted similarity in component names: 1 = identical, 0 = nothing identical, default = 0.4")
	private double threshold = 0.4d;

	private final List<Pattern> selectionCriteriaPatterns = new ArrayList<>();

	public List<Path> getInputModelPaths() {
		return this.inputModelPaths;
	}

	public Path getOutputDirectory() {
		return this.outputDirectory;
	}

	public String getExperimentName() {
		return this.experimentName;
	}

	public EOperation getOperation() {
		return this.operation;
	}

	public Path getSelectionCriteriaPath() {
		return this.selectionCriteriaPath;
	}

	public List<Pattern> getSelectionCriteriaPatterns() {
		return this.selectionCriteriaPatterns;
	}

	public double getThreshold() {
		return this.threshold;
	}

	public void setThreshold(final double threshold) {
		this.threshold = threshold;
	}

}
