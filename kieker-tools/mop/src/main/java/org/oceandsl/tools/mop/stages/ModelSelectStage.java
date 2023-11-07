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
package org.oceandsl.tools.mop.stages;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

import org.eclipse.emf.ecore.EObject;

import kieker.analysis.architecture.repository.ModelRepository;
import kieker.model.analysismodel.assembly.AssemblyComponent;
import kieker.model.analysismodel.assembly.AssemblyModel;
import kieker.model.analysismodel.assembly.AssemblyPackage;
import kieker.model.analysismodel.deployment.DeployedComponent;
import kieker.model.analysismodel.deployment.DeploymentModel;
import kieker.model.analysismodel.deployment.DeploymentPackage;
import kieker.model.analysismodel.execution.ExecutionModel;
import kieker.model.analysismodel.execution.ExecutionPackage;
import kieker.model.analysismodel.execution.Invocation;
import kieker.model.analysismodel.source.SourceModel;
import kieker.model.analysismodel.source.SourcePackage;
import kieker.model.analysismodel.statistics.StatisticRecord;
import kieker.model.analysismodel.statistics.StatisticsModel;
import kieker.model.analysismodel.statistics.StatisticsPackage;
import kieker.model.analysismodel.type.ComponentType;
import kieker.model.analysismodel.type.TypeModel;
import kieker.model.analysismodel.type.TypePackage;

import teetime.stage.basic.AbstractTransformation;

/**
 * Keep only deployed components that match the patterns and remove all other deployed components
 * including the all assembly components, component types, statistics and source annotations that
 * are no longer used.
 *
 * @author Reiner Jung
 * @since 1.3
 *
 */
public class ModelSelectStage extends AbstractTransformation<ModelRepository, ModelRepository>
        implements IModelOperationStage {

    private final List<Pattern> patterns;

    public ModelSelectStage(final List<Pattern> patterns) {
        this.patterns = patterns;
    }

    @Override
    protected void execute(final ModelRepository repository) throws Exception {
        final DeploymentModel model = repository.getModel(DeploymentPackage.eINSTANCE.getDeploymentModel());

        final Set<DeployedComponent> selectedDeployedComponents = this.selectPrimaryComponents(model);
        selectedDeployedComponents.addAll(this.selectContainedComponents(model, selectedDeployedComponents));

        final Set<AssemblyComponent> selectedAssemblyComponents = this
                .selectAssemblyComponents(selectedDeployedComponents);
        final Set<ComponentType> selectedComponentTypes = this.selectComponentTypes(selectedAssemblyComponents);

        final Set<ComponentType> removedComponentTypes = this.removeComponentTypes(
                repository.getModel(TypePackage.eINSTANCE.getTypeModel()), selectedComponentTypes);
        final Set<AssemblyComponent> removedAssemblyComponents = this.removeAssemblyComponents(
                repository.getModel(AssemblyPackage.eINSTANCE.getAssemblyModel()), selectedAssemblyComponents);
        final Set<DeployedComponent> removedDeployedComponents = this.removeDeployedComponents(
                repository.getModel(DeploymentPackage.eINSTANCE.getDeploymentModel()), selectedDeployedComponents);
        final Set<Invocation> removedExecutions = this.removeExecution(
                repository.getModel(ExecutionPackage.eINSTANCE.getExecutionModel()), removedDeployedComponents);

        final StatisticsModel statisticsModel = repository.getModel(StatisticsPackage.eINSTANCE.getStatisticsModel());
        final SourceModel sourceModel = repository.getModel(SourcePackage.eINSTANCE.getSourceModel());

        removedComponentTypes.forEach(type -> this.removeStatisticsAndSources(statisticsModel, sourceModel, type));
        removedAssemblyComponents
                .forEach(assembly -> this.removeStatisticsAndSources(statisticsModel, sourceModel, assembly));
        removedDeployedComponents
                .forEach(deployed -> this.removeStatisticsAndSources(statisticsModel, sourceModel, deployed));
        removedExecutions
                .forEach(invocation -> this.removeStatisticsAndSources(statisticsModel, sourceModel, invocation));

        this.outputPort.send(repository);
    }

    private void removeStatisticsAndSources(final StatisticsModel statisticsModel, final SourceModel sourceModel,
            final EObject element) {
        final StatisticRecord statistics = statisticsModel.getStatistics().removeKey(element);
        sourceModel.getSources().removeKey(element);
        if (statistics != null) {
            sourceModel.getSources().removeKey(statistics);
        }
    }

    private Set<DeployedComponent> selectPrimaryComponents(final DeploymentModel model) {
        final Set<DeployedComponent> selectedComponents = new HashSet<>();
        /** select primary components. */
        model.getContexts().values().forEach(context -> {
            context.getComponents().values().forEach(component -> {
                if (this.selectComponent(component.getSignature())) {
                    selectedComponents.add(component);
                }
            });
        });

        return selectedComponents;
    }

    private Set<DeployedComponent> selectContainedComponents(final DeploymentModel model,
            final Set<DeployedComponent> selectedComponents) {
        final Set<DeployedComponent> indirectSelectedComponents = new HashSet<>();
        model.getContexts().values().forEach(context -> {
            context.getComponents().values().forEach(component -> {
                if (this.containedInSelectedComponent(component, selectedComponents)) {
                    indirectSelectedComponents.add(component);
                }
            });
        });

        return indirectSelectedComponents;
    }

    private boolean containedInSelectedComponent(final DeployedComponent component,
            final Set<DeployedComponent> selectedComponents) {

        for (final DeployedComponent container : selectedComponents) {
            if (this.nestedContainedComponent(container, component)) {
                return true;
            }
        }

        return false;
    }

    private boolean nestedContainedComponent(final DeployedComponent container, final DeployedComponent component) {
        for (final DeployedComponent contained : container.getContainedComponents()) {
            if (contained.equals(component)) {
                return true;
            } else {
                return this.nestedContainedComponent(contained, component);
            }
        }
        return false;
    }

    private boolean selectComponent(final String signature) {
        for (final Pattern pattern : this.patterns) {
            if (pattern.matcher(signature).matches()) {
                return true;
            }
        }
        return false;
    }

    private Set<AssemblyComponent> selectAssemblyComponents(final Set<DeployedComponent> selectedDeployedComponents) {
        final Set<AssemblyComponent> components = new HashSet<>();
        selectedDeployedComponents.forEach(deployed -> components.add(deployed.getAssemblyComponent()));
        return components;
    }

    private Set<ComponentType> selectComponentTypes(final Set<AssemblyComponent> selectedAssemblyComponents) {
        final Set<ComponentType> components = new HashSet<>();
        selectedAssemblyComponents.forEach(assembly -> components.add(assembly.getComponentType()));
        return components;
    }

    private Set<ComponentType> removeComponentTypes(final TypeModel model,
            final Set<ComponentType> selectedComponentTypes) {
        final Set<ComponentType> removeComponentTypes = new HashSet<>();
        model.getComponentTypes().values().forEach(type -> {
            if (!selectedComponentTypes.contains(type)) {
                removeComponentTypes.add(type);
            }
        });
        removeComponentTypes.forEach(type -> model.getComponentTypes().values().remove(type));
        return removeComponentTypes;
    }

    private Set<AssemblyComponent> removeAssemblyComponents(final AssemblyModel model,
            final Set<AssemblyComponent> selectedAssemblyComponents) {
        final Set<AssemblyComponent> removeAssemblyComponents = new HashSet<>();
        model.getComponents().values().forEach(assembly -> {
            if (!selectedAssemblyComponents.contains(assembly)) {
                removeAssemblyComponents.add(assembly);
            }
        });
        removeAssemblyComponents.forEach(assembly -> model.getComponents().values().remove(assembly));
        return removeAssemblyComponents;
    }

    private Set<DeployedComponent> removeDeployedComponents(final DeploymentModel model,
            final Set<DeployedComponent> selectedDeployedComponents) {
        final Set<DeployedComponent> removeDeployedComponents = new HashSet<>();
        model.getContexts().values().forEach(context -> {
            context.getComponents().values().forEach(deployed -> {
                if (!selectedDeployedComponents.contains(deployed)) {
                    removeDeployedComponents.add(deployed);
                }
            });
        });
        model.getContexts().values().forEach(context -> {
            removeDeployedComponents.forEach(deployed -> context.getComponents().values().remove(deployed));
        });
        return removeDeployedComponents;
    }

    private Set<Invocation> removeExecution(final ExecutionModel model,
            final Set<DeployedComponent> removedDeployedComponents) {
        final Set<Invocation> removeExecutions = new HashSet<>();
        model.getInvocations().values().forEach(invocation -> {
            if (removedDeployedComponents.contains(invocation.getCaller().getComponent())
                    || removedDeployedComponents.contains(invocation.getCallee().getComponent())) {
                removeExecutions.add(invocation);
            }
        });

        removeExecutions.forEach(invocation -> model.getInvocations().values().remove(invocation));

        return removeExecutions;
    }

}
