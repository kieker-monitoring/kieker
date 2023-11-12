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

import org.eclipse.emf.common.util.EMap;

import kieker.model.analysismodel.assembly.AssemblyComponent;
import kieker.model.analysismodel.assembly.AssemblyModel;
import kieker.model.analysismodel.deployment.DeployedComponent;
import kieker.model.analysismodel.deployment.DeployedOperation;
import kieker.model.analysismodel.deployment.DeployedStorage;
import kieker.model.analysismodel.deployment.DeploymentContext;
import kieker.model.analysismodel.deployment.DeploymentModel;

/**
 * @author Reiner Jung
 * @since 1.1
 */
public final class DeploymentModelMerger {

    private DeploymentModelMerger() {
        // Utility class
    }

    /* default */ static void mergeDeploymentModel(final AssemblyModel assemblyModel, final DeploymentModel model, // NOPMD
            final DeploymentModel mergeModel) {
        // add additional contexts if necessary
        for (final DeploymentContext mergeDeploymentContext : mergeModel.getContexts().values()) {
            if (!model.getContexts().containsKey(mergeDeploymentContext.getName())) {
                model.getContexts().put(mergeDeploymentContext.getName(),
                        DeploymentModelCloneUtils.duplicate(assemblyModel, mergeDeploymentContext));
            }
        }
        // now merge operations
        for (final DeploymentContext deploymentContext : model.getContexts().values()) {
            final DeploymentContext mergeDeploymentContext = mergeModel.getContexts().get(deploymentContext.getName());
            if (mergeDeploymentContext != null) {
                DeploymentModelMerger.mergeDepolymentComponents(assemblyModel, deploymentContext,
                        mergeDeploymentContext.getComponents());
            }
        }

    }

    private static void mergeDepolymentComponents(final AssemblyModel assemblyModel,
            final DeploymentContext deploymentContext, final EMap<String, DeployedComponent> mergeComponents) {
        for (final DeployedComponent mergeComponent : mergeComponents.values()) {
            if (!deploymentContext.getComponents().containsKey(mergeComponent.getSignature())) {
                deploymentContext.getComponents().put(mergeComponent.getSignature(),
                        DeploymentModelCloneUtils.duplicate(assemblyModel, mergeComponent));
            } else {
                final DeployedComponent component = deploymentContext.getComponents()
                        .get(mergeComponent.getSignature());
                DeploymentModelMerger.mergeDeploymentOperations(component.getAssemblyComponent(), component,
                        mergeComponent.getOperations());
                DeploymentModelMerger.mergeDeploymentStorages(component.getAssemblyComponent(), component,
                        mergeComponent.getStorages());
            }
        }
    }

    private static void mergeDeploymentOperations(final AssemblyComponent assemblyComponent,
            final DeployedComponent component, final EMap<String, DeployedOperation> containedOperations) {
        for (final Entry<String, DeployedOperation> mergeOperation : containedOperations) {
            if (!component.getOperations().containsKey(mergeOperation.getKey())) {
                component.getOperations().put(mergeOperation.getKey(),
                        DeploymentModelCloneUtils.duplicate(assemblyComponent, mergeOperation.getValue()));
            }
        }
    }

    private static void mergeDeploymentStorages(final AssemblyComponent assemblyComponent,
            final DeployedComponent component, final EMap<String, DeployedStorage> containedStorages) {
        for (final Entry<String, DeployedStorage> mergeStorage : containedStorages) {
            if (!component.getStorages().containsKey(mergeStorage.getKey())) {
                component.getStorages().put(mergeStorage.getKey(),
                        DeploymentModelCloneUtils.duplicate(assemblyComponent, mergeStorage.getValue()));
            }
        }
    }
}
