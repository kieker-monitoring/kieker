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

import org.eclipse.emf.common.util.EMap;

import kieker.model.analysismodel.type.ComponentType;
import kieker.model.analysismodel.type.OperationType;
import kieker.model.analysismodel.type.StorageType;
import kieker.model.analysismodel.type.TypeModel;

/**
 * @author Reiner Jung
 * @since 1.1
 */
public final class TypeModelMerger {

    private TypeModelMerger() {
        // Utility class
    }

    /* default */ static void mergeTypeModel(final TypeModel model, final TypeModel mergeModel) { // NOPMD
        // add additional component types if necessary
        for (final ComponentType mergeType : mergeModel.getComponentTypes().values()) {
            if (!model.getComponentTypes().containsKey(mergeType.getSignature())) {
                model.getComponentTypes().put(mergeType.getSignature(), TypeModelCloneUtils.duplicate(mergeType));
            }
        }
        // now merge operations
        for (final ComponentType type : model.getComponentTypes().values()) {
            final ComponentType mergeType = mergeModel.getComponentTypes().get(type.getSignature());
            if (mergeType != null) {
                TypeModelMerger.mergeTypeOperations(type, mergeType.getProvidedOperations());
                TypeModelMerger.mergeTypeStorages(type, mergeType.getProvidedStorages());
            }
        }
    }

    private static void mergeTypeOperations(final ComponentType type,
            final EMap<String, OperationType> mergeProvidedOperations) {
        for (final OperationType mergeOperation : mergeProvidedOperations.values()) {
            if (!type.getProvidedOperations().containsKey(mergeOperation.getSignature())) {
                type.getProvidedOperations().put(mergeOperation.getSignature(),
                        TypeModelCloneUtils.duplicate(mergeOperation));
            }
        }
    }

    private static void mergeTypeStorages(final ComponentType type, final EMap<String, StorageType> providedStorages) {
        for (final StorageType mergeStorage : providedStorages.values()) {
            if (!type.getProvidedStorages().containsKey(mergeStorage.getName())) {
                type.getProvidedStorages().put(mergeStorage.getName(), TypeModelCloneUtils.duplicate(mergeStorage));
            }
        }
    }

}
