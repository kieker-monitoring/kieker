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
package kieker.tools.mop.stages;

import org.eclipse.emf.ecore.util.EcoreUtil;

import kieker.analysis.architecture.repository.ModelRepository;
import kieker.tools.mop.merge.ModelRepositoryMergerUtils;

import teetime.stage.basic.AbstractTransformation;

import org.oceandsl.analysis.architecture.ArchitectureModelManagementUtils;

/**
 * @author Reiner Jung
 * @since 1.2
 */
public class ModelMergeStage extends AbstractTransformation<ModelRepository, ModelRepository>
        implements IModelOperationStage {

    private final ModelRepository lastModel;

    public ModelMergeStage(final String repositoryName) {
        this.lastModel = ArchitectureModelManagementUtils.createModelRepository(repositoryName);
    }

    @Override
    protected void execute(final ModelRepository element) throws Exception {
        element.getModels().values().forEach(model -> EcoreUtil.resolveAll(model.eResource()));
        this.logger.info("Merging models {}", element.getName());
        ModelRepositoryMergerUtils.perform(this.lastModel, element);
    }

    @Override
    protected void onTerminating() {
        this.outputPort.send(this.lastModel);
        super.onTerminating();
    }

}
