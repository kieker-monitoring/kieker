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

import org.eclipse.emf.common.util.EMap;

import kieker.analysis.architecture.repository.ModelRepository;
import kieker.model.analysismodel.type.ComponentType;
import kieker.model.analysismodel.type.OperationType;
import kieker.model.analysismodel.type.StorageType;
import kieker.model.analysismodel.type.TypeModel;
import kieker.model.analysismodel.type.TypePackage;

/**
 * Checks for the type model.
 *
 * @author Reiner Jung
 * @since 2.0.0
 */
public class CheckTypeModelStage extends AbstractCollector<ModelRepository> {

    @Override
    protected void execute(final ModelRepository repository) throws Exception {
        final Report report = new Report("type model");
        final TypeModel model = repository.getModel(TypePackage.Literals.TYPE_MODEL);

        model.getComponentTypes().entrySet().forEach(entry -> {
            final String name = entry.getKey();
            final ComponentType value = entry.getValue();
            if (value.getSignature() == null) {
                report.addMessage("Component type signature is null for %s", name);
            } else if (value.getSignature().isEmpty()) {
                report.addMessage("Component type signature is empty for %s", name);
            } else if (!value.getSignature().equals(name)) {
                report.addMessage("Component type key %s and signature %s differ", name, value.getSignature());
            }

            this.checkOperations(value.getProvidedOperations(), value.getSignature(), report);
            this.checkStorages(value.getProvidedStorages(), value.getSignature(), report);
        });

        GenericCheckUtils.missingSignature(model.eAllContents(), report);
        GenericCheckUtils.missingName(model.eAllContents(), report);
        GenericCheckUtils.missingPackage(model.eAllContents(), report);
        GenericCheckUtils.checkReferences(TypePackage.Literals.TYPE_MODEL, model.eAllContents(), report);

        this.outputPort.send(repository);
        this.reportOutputPort.send(report);
    }

    private void checkOperations(final EMap<String, OperationType> operations, final String componentName,
            final Report report) {
        operations.forEach(entry -> {
            if (entry.getKey() == null) {
                report.addMessage("Component type %s: Has null key for operation with signature %s", componentName,
                        entry.getValue().getSignature());
            } else {
                if (!entry.getKey().equals(entry.getValue().getSignature())) {
                    report.addMessage("Component type %s: Operation key %s and signature %s do not match",
                            componentName, entry.getKey(), entry.getValue().getSignature());
                }
            }
        });
    }

    private void checkStorages(final EMap<String, StorageType> storages, final String componentName,
            final Report report) {
        storages.forEach(entry -> {
            if (entry.getKey() == null) {
                report.addMessage("Component type %s: Has null key for storage with name %s", componentName,
                        entry.getValue().getName());
            } else {
                if (!entry.getKey().equals(entry.getValue().getName())) {
                    report.addMessage("Component type %s: Storage key %s and name %s do not match", componentName,
                            entry.getKey(), entry.getValue().getName());
                }
            }
        });
    }

}
