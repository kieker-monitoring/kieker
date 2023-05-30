/***************************************************************************
 * Copyright 2023 Kieker Project (http://kieker-monitoring.net)
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
package kieker.tools.common;

import com.beust.jcommander.Parameter;

/**
 * Some default settings which may be used by legacy applications and maybe new applications.
 *
 * @author Reiner Jung
 * @since 2.0.0
 */
public abstract class AbstractSettings {
	@Parameter(names = { "-v", "--verbose" }, required = false, description = "verbosely prints additional information")
	private boolean verbose;

	@Parameter(names = { "-d", "--debug" }, required = false, description = "prints additional debug information")
	private boolean debug;

	@Parameter(names = { "-h", "--help" }, required = false, description = "prints the usage information for the tool, including available options")
	private boolean help;

	public boolean isVerbose() {
		return this.verbose;
	}

	public boolean isDebug() {
		return this.debug;
	}

	public boolean isHelp() {
		return this.help;
	}

}
