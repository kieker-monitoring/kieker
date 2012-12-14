/***************************************************************************
 * Copyright 2012 Kieker Project (http://kieker-monitoring.net)
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

package kieker.tools.traceAnalysis.systemModel.repository;

import java.util.Collection;
import java.util.Hashtable;
import java.util.Map;

import kieker.tools.traceAnalysis.systemModel.AllocationComponent;
import kieker.tools.traceAnalysis.systemModel.Operation;
import kieker.tools.traceAnalysis.systemModel.util.AllocationComponentOperationPair;

/**
 * 
 * @author Andre van Hoorn
 */
public class AllocationComponentOperationPairFactory extends AbstractSystemSubRepository {
	public static final AllocationComponentOperationPair ROOT_PAIR =
			new AllocationComponentOperationPair(AbstractSystemSubRepository.ROOT_ELEMENT_ID, OperationRepository.ROOT_OPERATION,
					AllocationRepository.ROOT_ALLOCATION_COMPONENT);

	private final Map<String, AllocationComponentOperationPair> pairsByName = new Hashtable<String, AllocationComponentOperationPair>(); // NOPMD
	private final Map<Integer, AllocationComponentOperationPair> pairsById = new Hashtable<Integer, AllocationComponentOperationPair>(); // NOPMD

	public AllocationComponentOperationPairFactory(final SystemModelRepository systemFactory) {
		super(systemFactory);
	}

	/** Returns a corresponding pair instance (existing or newly created) */
	public final AllocationComponentOperationPair getPairInstanceByPair(final AllocationComponent allocationComponent, final Operation operation) {
		final AllocationComponentOperationPair inst = this.getPairByNamedIdentifier(allocationComponent.getId() + "-" + operation.getId());
		if (inst == null) {
			return this.createAndRegisterPair(operation, allocationComponent);
		}
		return inst;
	}

	private final AllocationComponentOperationPair createAndRegisterPair(final Operation operation, final AllocationComponent allocationComponent) {
		return this.createAndRegisterPair(allocationComponent.getId() + "-" + operation.getId(), operation, allocationComponent);
	}

	/**
	 * Returns the instance for the passed factory name; null if no instance
	 * with this factory name.
	 */
	private final AllocationComponentOperationPair getPairByNamedIdentifier(final String namedIdentifier) {
		return this.pairsByName.get(namedIdentifier);
	}

	/**
	 * Returns the instance for the passed ID; null if no instance
	 * with this ID.
	 */
	public final AllocationComponentOperationPair getPairById(final int id) {
		return this.pairsById.get(id);
	}

	private final AllocationComponentOperationPair createAndRegisterPair(final String namedIdentifier, final Operation operation,
			final AllocationComponent allocationComponent) {
		if (this.pairsByName.containsKey(namedIdentifier)) {
			throw new IllegalArgumentException("Element with name " + namedIdentifier + "exists already");
		}
		final int id = this.getAndIncrementNextId();
		final AllocationComponentOperationPair newInst = new AllocationComponentOperationPair(id, operation, allocationComponent);
		this.pairsById.put(id, newInst);
		this.pairsByName.put(namedIdentifier, newInst);
		return newInst;
	}

	public final Collection<AllocationComponentOperationPair> getPairs() {
		return this.pairsById.values();
	}
}
