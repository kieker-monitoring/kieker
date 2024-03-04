/***************************************************************************
 * Copyright (C) 2022 Kieker Project (https://kieker-monitoring.net)
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
 * @since 2.0.0
 */
public class FindDistinctCollectionsStage extends
		AbstractTransformation<ModelRepository, Tuple<ModelRepository, Map<ComponentType, Map<Set<ComponentType>, Set<OperationType>>>>> {

	@Override
	protected void execute(final ModelRepository repository) throws Exception {
		final Connections connections = repository.getModel(CollectionPackage.Literals.CONNECTIONS);

		final Map<ComponentType, Set<OperationType>> providedComponentToCallerMap = this
				.createProvidedComponentToCallerOperationsMap(connections);

		final Map<OperationType, Set<ComponentType>> calleeToCallerComponentSetMap = this
				.createCalleeOperationToCallerComponentSetMap(connections);

		final Map<ComponentType, Map<Set<ComponentType>, Set<OperationType>>> protointerfaceSourceGroupedOperations = this
				.groupProvidedOperationByRequiringComponentSet(providedComponentToCallerMap, calleeToCallerComponentSetMap);

		this.outputPort.send(new Tuple<>(repository, protointerfaceSourceGroupedOperations));
	}

	/**
	 * Create a map for all callers to the respective called component (provided)
	 *
	 * @param connections
	 * @return
	 */
	private Map<ComponentType, Set<OperationType>> createProvidedComponentToCallerOperationsMap(
			final Connections connections) {
		final Map<ComponentType, Set<OperationType>> providedComponentToCallerOperationsMap = new HashMap<>();
		for (final OperationCollection connection : connections.getConnections().values()) {
			Set<OperationType> callerSet = providedComponentToCallerOperationsMap.get(connection.getProvided());
			if (callerSet == null) {
				callerSet = new HashSet<>();
				providedComponentToCallerOperationsMap.put(connection.getProvided(), callerSet);
			}
			for (final OperationType caller : connection.getCallers().values()) {
				callerSet.add(caller);
			}
		}
		return providedComponentToCallerOperationsMap;
	}

	/**
	 * Generate a map for callee operations to their set of caller components.
	 *
	 * @param connections
	 *            all cross module connections
	 * @return callee to caller component set map
	 */
	private Map<OperationType, Set<ComponentType>> createCalleeOperationToCallerComponentSetMap(
			final Connections connections) {
		final Map<OperationType, Set<ComponentType>> calleeOperationToCallerComponentSetMap = new HashMap<>();
		for (final OperationCollection connection : connections.getConnections().values()) {
			for (final OperationType callee : connection.getCallees().values()) {
				Set<ComponentType> callerComponentSet = calleeOperationToCallerComponentSetMap.get(callee);
				if (callerComponentSet == null) {
					callerComponentSet = new HashSet<>();
					calleeOperationToCallerComponentSetMap.put(callee, callerComponentSet);
				}
				callerComponentSet.add(connection.getRequired());
			}
		}
		return calleeOperationToCallerComponentSetMap;
	}

	/**
	 * Creates a map of callee component to caller component set and the respective
	 * callee?-operation set.
	 *
	 * @param providedComponentToCallerMap
	 * @param providedOperationToRequiringComponentSetMap
	 * @return returns map
	 */
	private Map<ComponentType, Map<Set<ComponentType>, Set<OperationType>>> groupProvidedOperationByRequiringComponentSet(
			final Map<ComponentType, Set<OperationType>> providedComponentToCallerMap,
			final Map<OperationType, Set<ComponentType>> providedOperationToRequiringComponentSetMap) {
		final Map<ComponentType, Map<Set<ComponentType>, Set<OperationType>>> protointerfaceSourceGroupedOperations = new HashMap<>();
		providedComponentToCallerMap.entrySet().forEach(entry -> {
			protointerfaceSourceGroupedOperations.put(entry.getKey(), // callee component
					this.createSourceGroupedOperations(entry.getValue(),
							providedOperationToRequiringComponentSetMap));
		});
		return protointerfaceSourceGroupedOperations;
	}

	/**
	 * Find all provided operations that have the same source set of ComponentType.
	 *
	 * @param requiredOperations
	 *            set containing all relevant required operations
	 * @param providedOperationToRequiringComponentSetMap
	 *            map containing provided operations to ComponentType set representing callers.
	 * @return map containing ComponentType source sets with their OperationTypes
	 */
	private Map<Set<ComponentType>, Set<OperationType>> createSourceGroupedOperations(
			final Set<OperationType> requiredOperations,
			final Map<OperationType, Set<ComponentType>> providedOperationToRequiringComponentSetMap) {
		final Map<Set<ComponentType>, Set<OperationType>> callerComponentsToCalleesMap = new HashMap<>();
		requiredOperations.forEach(providedOperation -> {
			final Set<ComponentType> callerComponentSet = providedOperationToRequiringComponentSetMap.get(providedOperation);
			final Optional<Entry<Set<ComponentType>, Set<OperationType>>> element = this.findOperationSetByCallerComponentSet(callerComponentSet,
					callerComponentsToCalleesMap);

			// create and add or insert into existing operation set
			final Set<OperationType> operationTypeSet;
			if (element.isEmpty()) {
				operationTypeSet = new HashSet<>();
				callerComponentsToCalleesMap.put(callerComponentSet, operationTypeSet);
			} else {
				operationTypeSet = element.get().getValue();
			}
			operationTypeSet.add(providedOperation);
		});

		return callerComponentsToCalleesMap;
	}

	/**
	 * Search for an existing entry in the component type to callee type set and return it.
	 *
	 * @param requiringComponentSet
	 *            set of requiring components for the given callee operation
	 * @param requiringComponentsToCalleesMap
	 *            the map to be search for
	 * @return returns either a set entry on success or an empty optional element
	 */
	private Optional<Entry<Set<ComponentType>, Set<OperationType>>> findOperationSetByCallerComponentSet(final Set<ComponentType> requiringComponentSet,
			final Map<Set<ComponentType>, Set<OperationType>> requiringComponentsToCalleesMap) {
		return requiringComponentsToCalleesMap.entrySet()
				.stream().filter(entry -> this.compareSets(entry.getKey(), requiringComponentSet)).findFirst();
	}

	/**
	 * Compare two sets of an existingSet with a callerSet.
	 *
	 * @param existingSet
	 * @param callerSet
	 * @return
	 */
	private boolean compareSets(final Set<ComponentType> existingSet, final Set<ComponentType> callerSet) {
		if (existingSet.size() == callerSet.size()) {
			return existingSet.stream().allMatch(existingComponentType -> callerSet.contains(existingComponentType));
		} else {
			return false;
		}
	}

}
