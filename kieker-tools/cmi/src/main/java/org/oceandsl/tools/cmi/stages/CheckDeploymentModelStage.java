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
package org.oceandsl.tools.cmi.stages;

import kieker.analysis.architecture.repository.ModelRepository;
import kieker.model.analysismodel.deployment.DeployedComponent;
import kieker.model.analysismodel.deployment.DeploymentContext;
import kieker.model.analysismodel.deployment.DeploymentModel;
import kieker.model.analysismodel.deployment.DeploymentPackage;

/**
 * check deployment model.
 *
 * @author Reiner Jung
 * @since 2.0.0
 */
public class CheckDeploymentModelStage extends AbstractCollector<ModelRepository> {

    @Override
    protected void execute(final ModelRepository repository) throws Exception {
        final Report report = new Report("deployment model");

        final DeploymentModel deploymentModel = repository.getModel(DeploymentPackage.Literals.DEPLOYMENT_MODEL);
        this.checkForDuplicateDeployedOperations(deploymentModel);

        GenericCheckUtils.missingSignature(deploymentModel.eAllContents(), report);
        GenericCheckUtils.checkReferences(DeploymentPackage.Literals.DEPLOYMENT_MODEL, deploymentModel.eAllContents(),
                report);

        this.outputPort.send(repository);
        this.reportOutputPort.send(report);
    }

    private void checkForDuplicateDeployedOperations(final DeploymentModel model) {
        for (final DeploymentContext context : model.getContexts().values()) {
            for (final DeployedComponent component : context.getComponents().values()) {
                component.getOperations().keySet();
            }
        }
    }

}
