/***************************************************************************
 * Copyright (C) 2023 OceanDSL (https://oceandsl.uni-kiel.de)
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

import kieker.analysis.architecture.repository.ModelRepository;

import teetime.stage.basic.AbstractFilter;

/**
 * Allow to change the repository's name.
 *
 * @author Reiner Jung
 * @since 1.3.0
 *
 */
public class ModelChangeNameStage extends AbstractFilter<ModelRepository> {

	private final String name;

	public ModelChangeNameStage(final String name) {
		this.name = name;
	}

	@Override
	protected void execute(final ModelRepository input) throws Exception {
		final ModelRepository output = new ModelRepository(this.name);
		input.getModels().entrySet()
				.forEach(entry -> output.register(input.getModelDescriptor(entry.getKey()), entry.getValue()));
		this.outputPort.send(output);
	}

}
