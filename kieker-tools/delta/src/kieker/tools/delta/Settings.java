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
package kieker.tools.delta;

import java.nio.file.Path;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.converters.PathConverter;

/**
 * All settings including command line parameters for the analysis.
 *
 * @author Serafim Simonov
 * @since 2.0.0
 */
public class Settings { // NOPMD data class

	@Parameter(names = { "-i",
		"--input" }, required = true, converter = PathConverter.class, description = "Input restructure information")
	private Path inputPath;

	@Parameter(names = { "-o",
		"--output" }, required = true, converter = PathConverter.class, description = "Output restructure information path and filename without extension")
	private Path outputPath;

	@Parameter(names = "--eol", required = false, description = "End of line symbol")
	private String lineSeparator = System.lineSeparator();

	public Path getInputPath() {
		return this.inputPath;
	}

	public Path getOutputPath() {
		return this.outputPath;
	}

	public char[] getLineSeparator() {
		return this.lineSeparator.toCharArray();
	}

	public void setLineSeparator(final String lineSeparator) {
		this.lineSeparator = lineSeparator;
	}
}
