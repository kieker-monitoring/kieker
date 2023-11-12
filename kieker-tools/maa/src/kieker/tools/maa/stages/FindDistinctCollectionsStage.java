/***************************************************************************
 * Copyright (C) 2022 OceanDSL (https://oceandsl.uni-kiel.de)
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

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Set;

import kieker.analysis.architecture.repository.ModelRepository;
import kieker.analysis.util.Tuple;
import kieker.model.analysismodel.type.ComponentType;
import kieker.model.analysismodel.type.OperationType;
import kieker.model.collection.CollectionPackage;
import kieker.model.collection.Connections;
import kieker.model.collection.OperationCollection;

import teetime.stage.basic.AbstractTransformation;

/**
 * The stage receives a repository with a set of large collections of provided operations which may
 * have overlapping operations. The task of this stage is to identify a set of unique collections
 * where all operations of each collection are accessed by the same source.
 *
 * <ol>
 * <li>Make a list of all provided operations per target</li>
 * <li>for each operation identify all callers</li>
 * <li>Group all operations per target with the same callers (these are the interfaces)</li>
 * </ol>
 *
 * @author Reiner Jung
 * @since 1.3
 */
public class FindDistinctCollectionsStage extends
        AbstractTransformation<ModelRepository, Tuple<ModelRepository, Map<ComponentType, Map<Set<ComponentType>, Set<OperationType>>>>> {

    @Override
    protected void execute(final ModelRepository repository) throws Exception {
        final Connections connections = repository.getModel(CollectionPackage.Literals.CONNECTIONS);

        final Map<ComponentType, Set<OperationType>> providedOperationsMap = this
                .createProvidedComponentToCallerOperationsMap(connections);

        final Map<OperationType, Set<ComponentType>> calleeToCallerComponentSetMap = this
                .createCalleeOperationToCallerComponentSetMap(connections);

        final Map<ComponentType, Map<Set<ComponentType>, Set<OperationType>>> protointerfaceSourceGroupedOperations = this
                .groupCalleesByCallerComponentSet(providedOperationsMap, calleeToCallerComponentSetMap);

        this.outputPort.send(new Tuple<>(repository, protointerfaceSourceGroupedOperations));
    }

    /**
     * Creates a map of callee component to caller component set and the respective
     * callee?-operation set.
     *
     * @param providedOperationsMap
     * @param calleeToCallerComponentSetMap
     * @return returns map
     */
    private Map<ComponentType, Map<Set<ComponentType>, Set<OperationType>>> groupCalleesByCallerComponentSet(
            final Map<ComponentType, Set<OperationType>> providedOperationsMap,
            final Map<OperationType, Set<ComponentType>> calleeToCallerComponentSetMap) {
        final Map<ComponentType, Map<Set<ComponentType>, Set<OperationType>>> protointerfaceSourceGroupedOperations = new HashMap<>();
        providedOperationsMap.entrySet().forEach(entry -> {
            protointerfaceSourceGroupedOperations.put(entry.getKey(), // callee component
                    this.createSourceGroupedOperations(entry.getValue(), // callee operation
                            calleeToCallerComponentSetMap));
        });
        return protointerfaceSourceGroupedOperations;
    }

    /**
     * Find all operations that have the same source set of ComponentType.
     *
     * @param providedOperations
     *            set containing all relevant providing operations
     * @param calleeToCallerComponentSetMap
     *            map containing provided operations to ComponentType set representing callers.
     * @return map containing ComponentType source sets with their OperationTypes
     */
    private Map<Set<ComponentType>, Set<OperationType>> createSourceGroupedOperations(
            final Set<OperationType> providedOperations,
            final Map<OperationType, Set<ComponentType>> calleeToCallerComponentSetMap) {
        final Map<Set<ComponentType>, Set<OperationType>> callerComponentsToCalleesMap = new HashMap<>();
        providedOperations.forEach(providedOperation -> {
            final Set<ComponentType> callerComponentSet = calleeToCallerComponentSetMap.get(providedOperation);
            final Set<OperationType> operationTypeSet = this.findOperationSetByCallerComponentSet(callerComponentSet,
                    callerComponentsToCalleesMap);
            operationTypeSet.add(providedOperation);
        });

        return callerComponentsToCalleesMap;
    }

    private Set<OperationType> findOperationSetByCallerComponentSet(final Set<ComponentType> callerComponentSet,
            final Map<Set<ComponentType>, Set<OperationType>> callerComponentsToCalleesMap) {
        final Optional<Entry<Set<ComponentType>, Set<OperationType>>> element = callerComponentsToCalleesMap.entrySet()
                .stream().filter(entry -> this.compareSets(entry.getKey(), callerComponentSet)).findFirst();
        if (element.isEmpty()) {
            final Set<OperationType> operationTypeSet = new HashSet<>();
            callerComponentsToCalleesMap.put(callerComponentSet, operationTypeSet);
            return operationTypeSet;
        } else {
            return element.get().getValue();
        }
    }

    /**
     * Generate a map for callee operationa to their set of caller components.
     *
     * @param connections
     *            all cross module connections
     * @return callee to caller component set map
     */
    private Map<OperationType, Set<ComponentType>> createCalleeOperationToCallerComponentSetMap(
            final Connections connections) {
        final Map<OperationType, Set<ComponentType>> calleeOperationToCallerComponentSetMap = new HashMap<>();
        for (final OperationCollection connection : connections.getConnections().values()) {
            for (final OperationType callee : connection.getOperations().values()) {
                Set<ComponentType> callerSet = calleeOperationToCallerComponentSetMap.get(callee);
                if (callerSet == null) {
                    callerSet = new HashSet<>();
                    calleeOperationToCallerComponentSetMap.put(callee, callerSet);
                }
                callerSet.add(connection.getCaller());
            }
        }
        return calleeOperationToCallerComponentSetMap;
    }

    private Map<ComponentType, Set<OperationType>> createProvidedComponentToCallerOperationsMap(
            final Connections connections) {
        final Map<ComponentType, Set<OperationType>> providedComponentToCallerOperationsMap = new HashMap<>();
        for (final OperationCollection connection : connections.getConnections().values()) {
            for (final OperationType caller : connection.getOperations().values()) {
                Set<OperationType> callerSet = providedComponentToCallerOperationsMap.get(connection.getCallee());
                if (callerSet == null) {
                    callerSet = new HashSet<>();
                    providedComponentToCallerOperationsMap.put(connection.getCallee(), callerSet);
                }
                callerSet.add(caller);
            }
        }
        return providedComponentToCallerOperationsMap;
    }

    private boolean compareSets(final Set<ComponentType> existingSet, final Set<ComponentType> callerSet) {
        if (existingSet.size() == callerSet.size()) {
            return existingSet.stream().anyMatch(existingComponentType -> callerSet.contains(existingComponentType));
        } else {
            return false;
        }
    }

}
