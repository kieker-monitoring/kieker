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

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.regex.Pattern;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import kieker.analysis.architecture.repository.ModelDescriptor;
import kieker.analysis.architecture.repository.ModelRepository;
import kieker.model.analysismodel.assembly.AssemblyComponent;
import kieker.model.analysismodel.assembly.AssemblyFactory;
import kieker.model.analysismodel.assembly.AssemblyModel;
import kieker.model.analysismodel.assembly.AssemblyPackage;
import kieker.model.analysismodel.deployment.DeployedComponent;
import kieker.model.analysismodel.deployment.DeploymentFactory;
import kieker.model.analysismodel.deployment.DeploymentModel;
import kieker.model.analysismodel.deployment.DeploymentPackage;
import kieker.model.analysismodel.execution.ExecutionFactory;
import kieker.model.analysismodel.execution.ExecutionModel;
import kieker.model.analysismodel.execution.ExecutionPackage;
import kieker.model.analysismodel.source.SourceFactory;
import kieker.model.analysismodel.source.SourceModel;
import kieker.model.analysismodel.source.SourcePackage;
import kieker.model.analysismodel.statistics.StatisticsFactory;
import kieker.model.analysismodel.statistics.StatisticsModel;
import kieker.model.analysismodel.statistics.StatisticsPackage;
import kieker.model.analysismodel.type.ComponentType;
import kieker.model.analysismodel.type.TypeFactory;
import kieker.model.analysismodel.type.TypeModel;
import kieker.model.analysismodel.type.TypePackage;

import teetime.framework.test.StageTester;

import org.oceandsl.tools.mop.AbstractModelTestFactory;
import org.oceandsl.tools.mop.SarModelFactory;

class ModelSelectStageTest {

    private ModelRepository repository;

    @BeforeEach
    void setUp() {
        this.repository = new ModelRepository("test");
        final TypeModel typeModel = SarModelFactory.createTypeModel();
        this.repository.register(
                new ModelDescriptor("type-model.xmi", TypePackage.eINSTANCE.getTypeModel(), TypeFactory.eINSTANCE),
                typeModel);
        final AssemblyModel assemblyModel = SarModelFactory.createAssemblyModel(typeModel);
        this.repository.register(new ModelDescriptor("assembly-model.xmi", AssemblyPackage.eINSTANCE.getAssemblyModel(),
                AssemblyFactory.eINSTANCE), assemblyModel);
        final DeploymentModel deploymentModel = SarModelFactory.createDeploymentModel(assemblyModel);
        this.repository.register(new ModelDescriptor("deloyment-model.xmi",
                DeploymentPackage.eINSTANCE.getDeploymentModel(), DeploymentFactory.eINSTANCE), deploymentModel);
        final ExecutionModel executionModel = SarModelFactory.createExecutionModel(deploymentModel);
        this.repository.register(new ModelDescriptor("execution-model.xmi",
                ExecutionPackage.eINSTANCE.getExecutionModel(), ExecutionFactory.eINSTANCE), executionModel);
        final StatisticsModel statisticsModel = SarModelFactory.createStatisticsModel(executionModel);
        this.repository.register(new ModelDescriptor("statistics-model.xmi",
                StatisticsPackage.eINSTANCE.getStatisticsModel(), StatisticsFactory.eINSTANCE), statisticsModel);
        final SourceModel sourceModel = AbstractModelTestFactory.createSourceModel(typeModel, assemblyModel,
                deploymentModel, executionModel, "test");
        this.repository.register(new ModelDescriptor("source-model.xmi", SourcePackage.eINSTANCE.getSourceModel(),
                SourceFactory.eINSTANCE), sourceModel);
    }

    @Test
    void test() {
        final List<Pattern> patterns = new ArrayList<>();
        patterns.add(Pattern.compile(SarModelFactory.SAR_ASSEMBLY_SIGNATURE));

        final ModelSelectStage stage = new ModelSelectStage(patterns);

        final List<ModelRepository> results = new ArrayList<>();
        StageTester.test(stage).send(this.repository).to(stage.getInputPort()).and().receive(results)
                .from(stage.getOutputPort()).start();

        Assertions.assertEquals(1, results.size(), "Number of model repositories");

        final DeploymentModel deployment = this.repository.getModel(DeploymentPackage.eINSTANCE.getDeploymentModel());
        final Collection<DeployedComponent> deployedComponents = deployment.getContexts()
                .get(AbstractModelTestFactory.HOSTNAME).getComponents().values();
        Assertions.assertEquals(1, deployedComponents.size(), "Number of deployed components");
        Assertions.assertEquals(SarModelFactory.SAR_ASSEMBLY_SIGNATURE,
                deployedComponents.iterator().next().getSignature(), "Deployed component name");

        final AssemblyModel assembly = this.repository.getModel(AssemblyPackage.eINSTANCE.getAssemblyModel());
        final Collection<AssemblyComponent> assemblyComponents = assembly.getComponents().values();
        Assertions.assertEquals(1, assemblyComponents.size(), "Number of assembly components");
        Assertions.assertEquals(SarModelFactory.SAR_ASSEMBLY_SIGNATURE,
                assemblyComponents.iterator().next().getSignature(), "Assembly component name");

        final TypeModel type = this.repository.getModel(TypePackage.eINSTANCE.getTypeModel());
        final Collection<ComponentType> componentTypes = type.getComponentTypes().values();
        Assertions.assertEquals(1, componentTypes.size(), "Number of component types");
        Assertions.assertEquals(SarModelFactory.SAR_COMPONENT_SIGNATURE,
                componentTypes.iterator().next().getSignature(), "Component type name");
    }

}
