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
package kieker.tools.cmi;

import java.nio.file.Path;
import java.util.List;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.converters.PathConverter;

/**
 * All settings including command line parameters for the analysis.
 *
 * @author Reiner Jung
 * @since 2.0.0
 */
public class Settings {

	@Parameter(names = { "-i",
		"--input" }, required = true, converter = PathConverter.class, description = "Directory for the input model")
	private Path inputDirectory;

	@Parameter(names = { "-c",
		"--checks" }, variableArity = true, required = false, converter = CheckConverter.class, validateWith = CheckValidator.class,
			description = "Select the model checks to be applied")
	private List<ECheck> checks;

	public Path getInputDirectory() {
		return this.inputDirectory;
	}

	public List<ECheck> getChecks() {
		return this.checks;
	}

	public void setChecks(final List<ECheck> checks) {
		this.checks = checks;
	}

}
