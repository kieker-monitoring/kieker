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
package kieker.tools.sar;

import java.nio.file.Path;
import java.util.List;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.converters.PathConverter;

import kieker.analysis.generic.EModuleMode;
import kieker.analysis.generic.EModuleModeConverter;
import kieker.tools.settings.validators.ParentPathIsWriteableValidator;
import kieker.tools.settings.validators.PathIsDirectoryValidator;
import kieker.tools.settings.validators.PathIsWriteableValidator;

/**
 * All settings including command line parameters for the analysis.
 *
 * @author Reiner Jung
 * @since 2.0.0
 */
public class Settings { // NOPMD dataclass - required to contain settings

	@Parameter(names = { "-i",
		"--input" }, required = true, converter = PathConverter.class, validateWith = PathIsDirectoryValidator.class, description = "Input directory")
	private Path inputFile;

	@Parameter(names = { "-sc",
		"--separation-character" }, required = false, converter = CharacterConverter.class, description = "Separation character for CSV files, default is comma (,)")
	private Character splitSymbol;

	@Parameter(names = { "-g",
		"--input-mode" }, required = true, converter = InputModeConverter.class, validateWith = InputModeValidator.class,
			description = "Input mode to be used for static analysis")
	private EInputMode inputMode;

	@Parameter(names = { "-a",
		"--missing-functions" }, required = false, converter = PathConverter.class, validateWith = ParentPathIsWriteableValidator.class,
			description = "Output file for the list of functions without an associated file")
	private Path missingFunctionsFile;

	@Parameter(names = { "-o",
		"--output" }, required = true, converter = PathConverter.class, validateWith = PathIsWriteableValidator.class,
			description = "Output directory to store graphics and statistics")
	private Path outputDirectory;

	@Parameter(names = { "-m",
		"--module-modes" }, required = true, variableArity = true, converter = EModuleModeConverter.class,
			description = "Module converter strategies (at lease one of): module-mode, file-mode, map-mode")
	private List<EModuleMode> moduleModes;

	@Parameter(names = { "-M",
		"--component-maps" }, required = false, variableArity = true, converter = PathConverter.class, description = "Component, file and function map files")
	private List<Path> componentMapFiles;

	@Parameter(names = { "-l", "--source-label" }, required = true, description = "Set source label for the read data")
	private String sourceLabel;

	@Parameter(names = { "-H",
		"--hostname" }, required = false, description = "Hostname to be used in CSV reconstruction")
	private String hostname;

	@Parameter(names = { "-E", "--experiment-name" }, required = true, description = "Name of the experiment")
	private String experimentName;

	@Parameter(names = { "-n",
		"--missing-mappings-file" }, required = false, converter = PathConverter.class, validateWith = ParentPathIsWriteableValidator.class,
			description = "Output file for the list of files with a missing mapping in the mapping file.")
	private Path missingMappingsFile;

	public Path getInputFile() {
		return this.inputFile;
	}

	public char getSplitSymbol() {
		if (this.splitSymbol == null) {
			return ',';
		} else {
			return this.splitSymbol;
		}
	}

	public EInputMode getInputMode() {
		return this.inputMode;
	}

	public List<EModuleMode> getModuleModes() {
		return this.moduleModes;
	}

	public Path getOutputDirectory() {
		return this.outputDirectory;
	}

	public String getSourceLabel() {
		return this.sourceLabel;
	}

	public String getHostname() {
		return this.hostname == null ? "<static>" : this.hostname;
	}

	public String getExperimentName() {
		return this.experimentName;
	}

	public List<Path> getComponentMapFiles() {
		return this.componentMapFiles;
	}

	public Path getMissingFunctionsFile() {
		return this.missingFunctionsFile;
	}

	public Path getMissingMappingsFile() {
		return this.missingMappingsFile;
	}
}
