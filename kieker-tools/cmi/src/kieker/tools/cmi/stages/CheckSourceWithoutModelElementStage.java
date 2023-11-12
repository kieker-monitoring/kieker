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
package kieker.tools.cmi.stages;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.emf.common.util.TreeIterator;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;

import kieker.analysis.architecture.repository.ModelRepository;
import kieker.model.analysismodel.assembly.AssemblyPackage;
import kieker.model.analysismodel.deployment.DeploymentPackage;
import kieker.model.analysismodel.execution.ExecutionPackage;
import kieker.model.analysismodel.execution.Tuple;
import kieker.model.analysismodel.source.SourceModel;
import kieker.model.analysismodel.source.SourcePackage;
import kieker.model.analysismodel.type.TypePackage;
import kieker.tools.cmi.RepositoryUtils;

/**
 * Check source elements.
 *
 * @author Reiner Jung
 * @since 2.0.0
 */
public class CheckSourceWithoutModelElementStage extends AbstractCollector<ModelRepository> {

    @Override
    protected void execute(final ModelRepository repository) throws Exception {
        final Report report = new Report("source model");

        final SourceModel sourceModel = repository.getModel(SourcePackage.Literals.SOURCE_MODEL);

        this.checkForSourcesWithoutModelElements(sourceModel, repository, report);

        this.outputPort.send(repository);
        this.reportOutputPort.send(report);
    }

    private void checkForSourcesWithoutModelElements(final SourceModel sourceModel, final ModelRepository repository,
            final Report report) {
        long errors = 0;
        for (final EObject element : sourceModel.getSources().keySet()) {
            element.eCrossReferences();
            if (!this.existsObjectForSource(element, repository, report)) {
                errors++;
            }
        }

        report.addMessage("Number of missing references to sources %s", errors);
    }

    private boolean existsObjectForSource(final EObject element, final ModelRepository repository,
            final Report report) {
        for (final EClass modelRootClass : this.configureModels().keySet()) {
            final EObject model = repository.getModel(modelRootClass);
            if (this.modelContains(model, element)) {
                return true;
            }
        }
        report.addMessage("Object %s not found.", RepositoryUtils.getName(element));
        return false;
    }

    private boolean modelContains(final EObject model, final EObject element) {
        final TreeIterator<EObject> iterator = model.eAllContents();
        while (iterator.hasNext()) {
            final EObject modelElement = iterator.next();
            if (modelElement.equals(element)) {
                return true;
            }
        }
        return false;
    }

    // used in multiple stages
    private Map<EClass, List<Class<? extends EObject>>> configureModels() {
        final Map<EClass, List<Class<? extends EObject>>> modelConfigs = new HashMap<>();
        modelConfigs.put(TypePackage.Literals.TYPE_MODEL, new ArrayList<Class<? extends EObject>>());
        modelConfigs.put(AssemblyPackage.Literals.ASSEMBLY_MODEL, new ArrayList<Class<? extends EObject>>());
        modelConfigs.put(DeploymentPackage.Literals.DEPLOYMENT_MODEL, new ArrayList<Class<? extends EObject>>());
        final List<Class<? extends EObject>> executionIgnoreList = new ArrayList<>();
        executionIgnoreList.add(Tuple.class);
        modelConfigs.put(ExecutionPackage.Literals.EXECUTION_MODEL, executionIgnoreList);

        return modelConfigs;
    }

}
