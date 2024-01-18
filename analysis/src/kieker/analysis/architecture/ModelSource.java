/***************************************************************************
 * Copyright (C) 2021 OceanDSL (https://oceandsl.uni-kiel.de)
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
package kieker.analysis.architecture;

import java.nio.file.Path;
import java.util.List;

import teetime.framework.AbstractProducerStage;

/**
 * Send out single model repository paths.
 *
 * @author Reiner Jung
 * @since 1.1
 */
public class ModelSource extends AbstractProducerStage<Path> {

	private final List<Path> modelPaths;

	public ModelSource(final List<Path> modelPaths) {
		this.modelPaths = modelPaths;
	}

	@Override
	protected void execute() throws Exception {
		for (final Path modelPath : this.modelPaths) {
			this.outputPort.send(modelPath);
		}
		this.workCompleted();
	}

}
