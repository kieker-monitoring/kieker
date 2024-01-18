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
package kieker.log.rewriter;

import java.io.File;
import java.util.List;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.converters.FileConverter;

/**
 * @author Reiner Jung
 * @since 1.0
 */
public class Settings { // NOPMD

	@Parameter(names = { "-i",
		"--input" }, required = true, variableArity = true, converter = FileConverter.class, description = "Input Kieker log directories")
	private List<File> inputFiles;

	@Parameter(names = { "-o",
		"--output" }, required = true, converter = FileConverter.class, description = "Output directory where to put the Kieker log directory")
	private File outputFile;

	@Parameter(names = { "-a",
		"--addrline" }, required = true, converter = FileConverter.class, description = "Location of the addrline tool")
	private File addrlineExecutable;

	@Parameter(names = { "-m",
		"--model" }, required = true, converter = FileConverter.class, description = "Location of the model executable")
	private File modelExecutable;

	public List<File> getInputFiles() {
		return this.inputFiles;
	}

	public File getOutputFile() {
		return this.outputFile;
	}

	public File getAddrlineExecutable() {
		return this.addrlineExecutable;
	}

	public File getModelExecutable() {
		return this.modelExecutable;
	}
}
