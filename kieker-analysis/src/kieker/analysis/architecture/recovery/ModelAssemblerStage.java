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
package kieker.analysis.architecture.recovery;

import kieker.analysis.architecture.recovery.events.OperationEvent;

import teetime.stage.basic.AbstractFilter;

/**
 * Generic model assembler stage
 *
 * @author Reiner Jung
 * @since 2.0.0
 *
 * @param <T>
 */
public class ModelAssemblerStage<T extends IOperationEventAssembler> extends AbstractFilter<OperationEvent> {

	private final T assembler;

	public ModelAssemblerStage(final T assembler) {
		this.assembler = assembler;
	}

	@Override
	protected void execute(final OperationEvent event) {
		this.assembler.addOperation(event);
		this.outputPort.send(event);
	}
}
