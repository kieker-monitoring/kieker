/***************************************************************************
 * Copyright (C) 2021 Kieker Project (https://kieker-monitoring.net)
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
package kieker.tools.mop.merge;

import java.util.Map.Entry;

import kieker.model.analysismodel.type.ComponentType;
import kieker.model.analysismodel.type.OperationType;
import kieker.model.analysismodel.type.StorageType;
import kieker.model.analysismodel.type.TypeFactory;

/**
 * @author Reiner Jung
 * @since 2.0.0
 *
 */
public final class TypeModelCloneUtils {

	private TypeModelCloneUtils() {
		// Utility class
	}

	public static ComponentType duplicate(final ComponentType type) {
		final ComponentType newType = TypeFactory.eINSTANCE.createComponentType();

		newType.setName(type.getName());
		newType.setPackage(type.getPackage());
		newType.setSignature(type.getSignature());

		for (final Entry<String, OperationType> operation : type.getProvidedOperations()) {
			newType.getProvidedOperations().put(operation.getKey(),
					TypeModelCloneUtils.duplicate(operation.getValue()));
		}

		for (final Entry<String, StorageType> storage : type.getProvidedStorages()) {
			newType.getProvidedStorages().put(storage.getKey(), TypeModelCloneUtils.duplicate(storage.getValue()));
		}

		return newType;
	}

	public static OperationType duplicate(final OperationType operation) {
		final OperationType newOperation = TypeFactory.eINSTANCE.createOperationType();
		newOperation.setName(operation.getName());
		newOperation.setReturnType(operation.getReturnType());
		newOperation.setSignature(operation.getSignature());

		return newOperation;
	}

	public static StorageType duplicate(final StorageType storage) {
		final StorageType newStorage = TypeFactory.eINSTANCE.createStorageType();
		newStorage.setName(storage.getName());
		newStorage.setType(storage.getType());

		return newStorage;
	}

}
