/***************************************************************************
 * Copyright 2020 Kieker Project (http://kieker-monitoring.net)
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

package kieker.analysis.stage.model;

import kieker.analysis.signature.IComponentSignatureExtractor;
import kieker.analysis.signature.IOperationSignatureExtractor;
import kieker.analysis.stage.model.data.OperationEvent;
import kieker.model.analysismodel.sources.SourceModel;
import kieker.model.analysismodel.type.TypeModel;

import teetime.stage.basic.AbstractFilter;

/**
 * @author Sören Henning
 *
 * @since 1.14
 */
public class TypeModelAssemblerStage extends AbstractFilter<OperationEvent> {

	private final TypeModelAssembler assembler;

	public TypeModelAssemblerStage(final TypeModel typeModel, final SourceModel sourceModel, final String sourceLabel,
			final IComponentSignatureExtractor componentSignatureExtractor,
			final IOperationSignatureExtractor operationSignatureExtractor) {
		this.assembler = new TypeModelAssembler(typeModel, sourceModel, sourceLabel, componentSignatureExtractor, operationSignatureExtractor);
	}

	@Override
	protected void execute(final OperationEvent event) {
		this.assembler.addOperation(event);
		this.outputPort.send(event);
	}

}
