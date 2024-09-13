/***************************************************************************
 * Copyright (C) 2024 Kieker Project (https://kieker-monitoring.net)
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

package kieker.tools.oteltransformer;

import java.nio.file.Path;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.converters.PathConverter;

public class Settings {
	@Parameter(names = { "-lp",
		"--listenPort" }, required = true, description = "Port where the otel-transformer listens for traces")
	private int listenPort;

	@Parameter(names = { "-c",
		"--configuration" }, required = false, description = "Configuration file.", converter = PathConverter.class)
	private Path configurationPath;

	public int getListenPort() {
		return listenPort;
	}

	public Path getKiekerMonitoringProperties() {
		return this.configurationPath;
	}
}
