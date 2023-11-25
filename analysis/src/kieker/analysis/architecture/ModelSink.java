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

import kieker.analysis.architecture.repository.ArchitectureModelRepositoryFactory;
import kieker.analysis.architecture.repository.ModelRepository;

import teetime.framework.AbstractConsumerStage;

/**
 * Write an in memory model into an output directory.
 *
 * @author Reiner Jung
 * @since 1.1
 */
public class ModelSink extends AbstractConsumerStage<ModelRepository> {

	private final Path outputPath;
	private final boolean useRepositoryName;

	/**
	 * Create a model sink. In the output directory does not exist, it tries to create it.
	 *
	 * @param outputPath
	 *            path to the directory where the model is stored in
	 */
	public ModelSink(final Path outputPath) {
		this(outputPath, false);
	}

	/**
	 * Create a model sink. In the output directory does not exist, it tries to create it.
	 *
	 * @param outputPath
	 *            path to the directory where the model is stored in
	 * @param useRepositoryName
	 *            inf true the outputPath is extended by the repository name
	 */
	public ModelSink(final Path outputPath, final boolean useRepositoryName) {
		this.outputPath = outputPath;
		this.useRepositoryName = useRepositoryName;
	}

	@Override
	protected void execute(final ModelRepository element) throws Exception {
		final Path path;
		if (this.useRepositoryName) {
			path = this.outputPath.resolve(element.getName());
		} else {
			path = this.outputPath;
		}
		ArchitectureModelRepositoryFactory.writeModelRepository(path, element);
	}

}
