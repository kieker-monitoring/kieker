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
package org.oceandsl.tools.delta.stages;

import java.util.HashMap;
import java.util.Map;

import teetime.stage.basic.AbstractTransformation;

import org.oceandsl.tools.delta.stages.data.YamlComponent;
import org.oceandsl.tools.delta.stages.data.YamlOperation;
import org.oceandsl.tools.delta.stages.data.YamlRestructureModel;
import org.oceandsl.tools.restructuring.restructuremodel.CutOperation;
import org.oceandsl.tools.restructuring.restructuremodel.MoveOperation;
import org.oceandsl.tools.restructuring.restructuremodel.PasteOperation;
import org.oceandsl.tools.restructuring.restructuremodel.TransformationModel;

/**
 * Compute YAML based output model.
 *
 * @author Reiner Jung
 * @since 2.0.0
 */
public class CompileRestructureYamlStage extends AbstractTransformation<TransformationModel, YamlRestructureModel> {

    private final Map<String, CutOperation> rememberCutOperation = new HashMap<>();

    @Override
    protected void execute(final TransformationModel model) throws Exception {
        final YamlRestructureModel yamlModel = new YamlRestructureModel(model.getName());
        model.getTransformations().forEach(action -> {
            if (action instanceof CutOperation) {
                final CutOperation cut = (CutOperation) action;
                this.rememberCutOperation.put(cut.getOperationName(), cut);
            } else if (action instanceof MoveOperation) {
                final MoveOperation move = (MoveOperation) action;
                final CutOperation cut = move.getCutOperation();
                final PasteOperation paste = move.getPasteOperation();

                final YamlComponent component = this.findOrCreateComponent(yamlModel, paste.getComponentName());
                component.getOperations().put(cut.getOperationName(),
                        new YamlOperation(cut.getOperationName(), cut.getComponentName()));
            } else if (action instanceof PasteOperation) {
                final PasteOperation paste = (PasteOperation) action;
                if (this.rememberCutOperation.containsKey(paste.getOperationName())) {
                    final CutOperation cut = this.rememberCutOperation.get(paste.getOperationName());

                    final YamlComponent component = this.findOrCreateComponent(yamlModel, paste.getComponentName());
                    component.getOperations().put(cut.getOperationName(),
                            new YamlOperation(cut.getOperationName(), cut.getComponentName()));
                } else {
                    this.logger.error("Have paste without cut. {} {}", paste.getComponentName(),
                            paste.getOperationName());
                }
            } else {
                this.logger.debug("Got a {}", action.getClass());
            }
        });
        this.outputPort.send(yamlModel);
    }

    private YamlComponent findOrCreateComponent(final YamlRestructureModel yamlModel, final String componentName) {
        if (yamlModel.getComponents().containsKey(componentName)) {
            return yamlModel.getComponents().get(componentName);
        } else {
            final YamlComponent component = new YamlComponent(componentName);
            yamlModel.getComponents().put(componentName, component);
            return component;
        }
    }

}
