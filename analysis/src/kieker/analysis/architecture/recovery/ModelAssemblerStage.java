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

package kieker.analysis.architecture.recovery;

import kieker.analysis.architecture.recovery.assembler.AbstractModelAssembler;

import teetime.stage.basic.AbstractFilter;

/**
 * Generic model assembler stage to run model assemblers.
 *
 * @param <T>
 *            event type as input and output of these assembler stages
 *
 * @author Reiner Jung
 * @since 2.0.0
 */
public class ModelAssemblerStage<T> extends AbstractFilter<T> {

	private final AbstractModelAssembler<T> assembler;

	public ModelAssemblerStage(final AbstractModelAssembler<T> assembler) {
		this.assembler = assembler;
	}

	@Override
	protected void execute(final T operationCall) {
		this.assembler.assemble(operationCall);
		this.outputPort.send(operationCall);
	}

}
