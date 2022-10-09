/***************************************************************************
 * Copyright 2022 Kieker Project (http://kieker-monitoring.net)
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
package kieker.tools.runtime.analysis;

import java.io.File;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.converters.FileConverter;

/**
 * @author Reiner Jung
 * @since 2.0.0
 */
public class RuntimeAnalysisSettings {
	@Parameter(names = "--help", help = true)
	private boolean help; // NOPMD access through reflection

	@Parameter(names = { "-c",
		"--configuration" }, required = true, description = "Configuration file.", converter = FileConverter.class)
	private File configurationFile;

	private File modelInitDirectory;

	private File modelDatabaseDirectory;

	private boolean pcmFeature;

	public final boolean isHelp() {
		return this.help;
	}

	public final void setHelp(final boolean help) {
		this.help = help;
	}

	public final File getConfigurationFile() {
		return this.configurationFile;
	}

	public final void setConfigurationFile(final File configurationFile) {
		this.configurationFile = configurationFile;
	}

	public final File getModelInitDirectory() {
		return this.modelInitDirectory;
	}

	public final void setModelInitDirectory(final File modelInitDirectory) {
		this.modelInitDirectory = modelInitDirectory;
	}

	public final File getModelDatabaseDirectory() {
		return this.modelDatabaseDirectory;
	}

	public final void setModelDatabaseDirectory(final File modelDatabaseDirectory) {
		this.modelDatabaseDirectory = modelDatabaseDirectory;
	}

	public final boolean isPcmFeature() {
		return this.pcmFeature;
	}

	public final void setPcmFeature(final boolean pcmFeature) {
		this.pcmFeature = pcmFeature;
	}

}
