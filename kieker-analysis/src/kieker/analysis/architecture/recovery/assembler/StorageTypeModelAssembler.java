/***************************************************************************
 * Copyright (C) 2022 OceanDSL (https://oceandsl.uni-kiel.de)
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
package kieker.analysis.architecture.recovery.assembler;

import kieker.analysis.architecture.recovery.events.StorageEvent;
import kieker.analysis.architecture.recovery.signature.IComponentSignatureExtractor;
import kieker.analysis.architecture.recovery.signature.IStorageSignatureExtractor;
import kieker.model.analysismodel.source.SourceModel;
import kieker.model.analysismodel.type.ComponentType;
import kieker.model.analysismodel.type.StorageType;
import kieker.model.analysismodel.type.TypeFactory;
import kieker.model.analysismodel.type.TypeModel;

/**
 * Create storage elements in the type model.
 *
 * @author Reiner Jung
 * @since 2.0.0
 */
public class StorageTypeModelAssembler extends AbstractModelAssembler<StorageEvent> {

	private final TypeFactory factory = TypeFactory.eINSTANCE;
	private final IComponentSignatureExtractor componentSignatureExtractor;
	private final IStorageSignatureExtractor storageSignatureExtractor;

	private final TypeModel typeModel;

	public StorageTypeModelAssembler(final TypeModel typeModel, final SourceModel sourceModel, final String sourceLabel,
			final IComponentSignatureExtractor componentSignatureExtractor,
			final IStorageSignatureExtractor storageSignatureExtractor) {
		super(sourceModel, sourceLabel);
		this.typeModel = typeModel;
		this.componentSignatureExtractor = componentSignatureExtractor;
		this.storageSignatureExtractor = storageSignatureExtractor;
	}

	@Override
	public void assemble(final StorageEvent event) {
		final String componentSignature = event.getComponentSignature();
		final String operationSignature = event.getStorageSignature();

		final ComponentType componentType = this.findOrCreateComponentType(componentSignature);
		this.addStorageType(componentType, operationSignature);
	}

	private ComponentType findOrCreateComponentType(final String componentSignature) {
		ComponentType componentType = this.typeModel.getComponentTypes().get(componentSignature);
		if (componentType == null) {
			componentType = this.factory.createComponentType();
			componentType.setSignature(componentSignature);
			this.componentSignatureExtractor.extract(componentType);
			this.typeModel.getComponentTypes().put(componentSignature, componentType);
		}
		this.updateSourceModel(componentType);
		return componentType;
	}

	private StorageType addStorageType(final ComponentType componentType, final String storageSignature) {
		StorageType storageType = componentType.getProvidedStorages().get(storageSignature);
		if (storageType == null) {
			storageType = this.factory.createStorageType();
			storageType.setName(storageSignature);
			storageType.setType("<unknown>");
			this.storageSignatureExtractor.extract(storageType);
			componentType.getProvidedStorages().put(storageSignature, storageType);
		}
		this.updateSourceModel(storageType);
		return storageType;
	}

}
