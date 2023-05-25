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

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import kieker.analysis.architecture.recovery.events.StorageEvent;
import kieker.model.analysismodel.source.SourceFactory;
import kieker.model.analysismodel.source.SourceModel;
import kieker.model.analysismodel.type.ComponentType;
import kieker.model.analysismodel.type.StorageType;
import kieker.model.analysismodel.type.TypeFactory;
import kieker.model.analysismodel.type.TypeModel;

/**
 *
 * @author Reiner Jung
 * @since 1.3.0
 */
class StorageTypeModelAssemblerTest {

	private static final String SOURCE_LABEL = "test";
	private static final String HOSTNAME = "host";
	private static final String COMPONENT_SIGNATUE = "test.component";
	private static final String STORAGE_SIGNATURE = "data";
	private static final String STORAGE_TYPE = "integer";

	@Test
	void test() {
		final TypeModel typeModel = TypeFactory.eINSTANCE.createTypeModel();
		final SourceModel sourceModel = SourceFactory.eINSTANCE.createSourceModel();

		final StorageTypeModelAssembler assembler = new StorageTypeModelAssembler(typeModel, sourceModel, StorageTypeModelAssemblerTest.SOURCE_LABEL,
				new SimpleComponentSignatureExtractor(), new SimpleStorageSignatureExtractor());

		final StorageEvent storageEvent = new StorageEvent(StorageTypeModelAssemblerTest.HOSTNAME, StorageTypeModelAssemblerTest.COMPONENT_SIGNATUE,
				StorageTypeModelAssemblerTest.STORAGE_SIGNATURE,
				StorageTypeModelAssemblerTest.STORAGE_TYPE);

		assembler.assemble(storageEvent);

		Assertions.assertEquals(1, typeModel.getComponentTypes().size());
		final ComponentType type = typeModel.getComponentTypes().get(StorageTypeModelAssemblerTest.COMPONENT_SIGNATUE);
		Assertions.assertNotNull(type);
		Assertions.assertEquals(StorageTypeModelAssemblerTest.COMPONENT_SIGNATUE, type.getSignature());
		Assertions.assertEquals(1, type.getProvidedStorages().size());
		final StorageType storageType = type.getProvidedStorages().get(StorageTypeModelAssemblerTest.STORAGE_SIGNATURE);
		Assertions.assertNotNull(storageType);
		Assertions.assertEquals(StorageTypeModelAssemblerTest.STORAGE_SIGNATURE, storageType.getName());
	}

}
