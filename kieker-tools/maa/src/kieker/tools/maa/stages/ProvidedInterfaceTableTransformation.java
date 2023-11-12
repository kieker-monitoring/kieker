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
package kieker.tools.maa.stages;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import kieker.analysis.architecture.repository.ModelRepository;
import kieker.model.analysismodel.type.ComponentType;
import kieker.model.analysismodel.type.OperationType;
import kieker.model.analysismodel.type.ProvidedInterfaceType;
import kieker.model.analysismodel.type.RequiredInterfaceType;
import kieker.model.analysismodel.type.TypeModel;
import kieker.model.analysismodel.type.TypePackage;

import teetime.stage.basic.AbstractTransformation;

import org.oceandsl.analysis.generic.Table;

/**
 * Generate table for interfaces in component type, interface name, operations.
 *
 * @author Reiner Jung
 * @since 1.2
 */
public class ProvidedInterfaceTableTransformation
        extends AbstractTransformation<ModelRepository, Table<String, ProvidedInterfaceEntry>> {

    @Override
    protected void execute(final ModelRepository element) throws Exception {
        final Table<String, ProvidedInterfaceEntry> table = new Table<>("interfaces");
        final TypeModel typeModel = element.getModel(TypePackage.Literals.TYPE_MODEL);
        final Map<ProvidedInterfaceType, Set<RequiredInterfaceType>> providedToRequiredMap = this
                .createLookupProvidedInterfaceType(typeModel.getComponentTypes().values());

        for (final ComponentType componentType : typeModel.getComponentTypes().values()) {
            for (final ProvidedInterfaceType providedInterfaceType : componentType.getProvidedInterfaceTypes()) {
                final String requiredString = providedToRequiredMap.get(providedInterfaceType).stream().map(
                        requiredInterfaceType -> ((ComponentType) requiredInterfaceType.eContainer()).getSignature())
                        .collect(Collectors.joining(","));

                for (final OperationType operation : providedInterfaceType.getProvidedOperationTypes().values()) {
                    table.getRows().add(new ProvidedInterfaceEntry(componentType.getSignature(),
                            providedInterfaceType.getSignature(), operation.getSignature(), requiredString));
                }
            }
        }
        this.outputPort.send(table);
    }

    private Map<ProvidedInterfaceType, Set<RequiredInterfaceType>> createLookupProvidedInterfaceType(
            final Collection<ComponentType> componentTypes) {
        final Map<ProvidedInterfaceType, Set<RequiredInterfaceType>> providedToRequiredMap = new HashMap<>();

        componentTypes.forEach(componentType -> {
            componentType.getRequiredInterfaceTypes().forEach(requiredInterfaceType -> {
                Set<RequiredInterfaceType> requiredInterfaceTypes = providedToRequiredMap
                        .get(requiredInterfaceType.getRequires());
                if (requiredInterfaceTypes == null) {
                    requiredInterfaceTypes = new HashSet<>();
                    providedToRequiredMap.put(requiredInterfaceType.getRequires(), requiredInterfaceTypes);
                }
                requiredInterfaceTypes.add(requiredInterfaceType);
            });
        });

        return providedToRequiredMap;
    }

}
