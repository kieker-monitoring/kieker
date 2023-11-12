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

import kieker.model.analysismodel.assembly.AssemblyComponent;
import kieker.model.analysismodel.assembly.AssemblyModel;
import kieker.model.analysismodel.assembly.AssemblyOperation;
import kieker.model.analysismodel.assembly.AssemblyStorage;
import kieker.model.analysismodel.type.TypeModel;

/**
 * @author Reiner Jung
 * @since 1.1
 */
public final class AssemblyModelMerger {

    private AssemblyModelMerger() {
    }

    /* default access */
    static void mergeAssemblyModel(final TypeModel typeModel, final AssemblyModel model, // NOPMD
            final AssemblyModel mergeModel) {
        // add additional component types if necessary
        for (final AssemblyComponent mergeComponent : mergeModel.getComponents().values()) {
            if (!model.getComponents().containsKey(mergeComponent.getSignature())) {
                model.getComponents().put(mergeComponent.getSignature(),
                        AssemblyModelCloneUtils.duplicate(typeModel, mergeComponent));
            }
        }
        // now merge operations
        for (final AssemblyComponent component : model.getComponents().values()) {
            final AssemblyComponent mergeComponent = mergeModel.getComponents().get(component.getSignature());
            if (mergeComponent != null) {
                AssemblyModelMerger.mergeAssemblyOperations(component, mergeComponent.getOperations());
                AssemblyModelMerger.mergeAssemblyStorages(component, mergeComponent.getStorages());
            }
        }
    }

    private static void mergeAssemblyOperations(final AssemblyComponent component,
            final EMap<String, AssemblyOperation> assemblyOperations) {
        for (final AssemblyOperation mergeOperation : assemblyOperations.values()) {
            if (!component.getOperations().containsKey(mergeOperation.getOperationType().getSignature())) {
                component.getOperations().put(mergeOperation.getOperationType().getSignature(),
                        AssemblyModelCloneUtils.duplicate(component.getComponentType(), mergeOperation));
            }
        }
    }

    private static void mergeAssemblyStorages(final AssemblyComponent component,
            final EMap<String, AssemblyStorage> mergeAssemblyStorages) {
        for (final AssemblyStorage mergeStorage : mergeAssemblyStorages.values()) {
            if (!component.getStorages().containsKey(mergeStorage.getStorageType().getName())) {
                component.getStorages().put(mergeStorage.getStorageType().getName(),
                        AssemblyModelCloneUtils.duplicate(component.getComponentType(), mergeStorage));
            }
        }
    }
}
