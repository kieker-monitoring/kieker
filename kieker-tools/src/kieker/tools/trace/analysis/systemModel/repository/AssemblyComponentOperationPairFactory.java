/***************************************************************************
 * Copyright 2021 Kieker Project (http://kieker-monitoring.net)
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

package kieker.tools.trace.analysis.systemModel.repository;

import java.util.Collection;
import java.util.Hashtable;
import java.util.Map;

import kieker.tools.trace.analysis.systemModel.AssemblyComponent;
import kieker.tools.trace.analysis.systemModel.Operation;
import kieker.tools.trace.analysis.systemModel.util.AssemblyComponentOperationPair;

/**
 *
 * @author Andre van Hoorn
 *
 * @since 1.1
 */
public class AssemblyComponentOperationPairFactory extends AbstractSystemSubRepository {
	public static final AssemblyComponentOperationPair ROOT_PAIR = new AssemblyComponentOperationPair(AbstractSystemSubRepository.ROOT_ELEMENT_ID,
			OperationRepository.ROOT_OPERATION,
			AssemblyRepository.ROOT_ASSEMBLY_COMPONENT);

	private final Map<String, AssemblyComponentOperationPair> pairsByName = new Hashtable<>(); // NOPMD (UseConcurrentHashMap)
	private final Map<Integer, AssemblyComponentOperationPair> pairsById = new Hashtable<>(); // NOPMD (UseConcurrentHashMap)

	/**
	 * Creates a new instance of this class using the given parameters.
	 *
	 * @param systemFactory
	 *            The system factory.
	 */
	public AssemblyComponentOperationPairFactory(final SystemModelRepository systemFactory) {
		super(systemFactory);
	}

	/**
	 * Returns a corresponding pair instance (existing or newly created).
	 *
	 * @param assemblyComponent
	 *            The assemble component for the pair.
	 * @param operation
	 *            The operation for the pair.
	 *
	 * @return The corresponding pair instance if it exists, otherwise a new one.
	 */
	public final AssemblyComponentOperationPair getPairInstanceByPair(final AssemblyComponent assemblyComponent, final Operation operation) {
		final String namedIdentifier = assemblyComponent.getId() + "-" + operation.getId();
		final AssemblyComponentOperationPair inst = this.getPairByNamedIdentifier(namedIdentifier);
		if (inst == null) {
			return this.createAndRegisterPair(namedIdentifier, operation, assemblyComponent);
		}
		return inst;
	}

	/**
	 * Returns the instance for the passed factory name; null if no instance
	 * with this factory name.
	 *
	 * @param namedIdentifier
	 *            The identifier to search for.
	 */
	private AssemblyComponentOperationPair getPairByNamedIdentifier(final String namedIdentifier) {
		return this.pairsByName.get(namedIdentifier);
	}

	/**
	 * @param id
	 *            The ID of the instance in question.
	 *
	 * @return The instance for the passed ID; null if no instance with this ID is available.
	 */
	public final AssemblyComponentOperationPair getPairById(final int id) {
		return this.pairsById.get(id);
	}

	private AssemblyComponentOperationPair createAndRegisterPair(final String namedIdentifier, final Operation operation,
			final AssemblyComponent assemblyComponent) {
		if (this.pairsByName.containsKey(namedIdentifier)) {
			throw new IllegalArgumentException("Element with name " + namedIdentifier + "exists already");
		}
		final int id = this.getAndIncrementNextId();
		final AssemblyComponentOperationPair newInst = new AssemblyComponentOperationPair(id, operation, assemblyComponent);
		this.pairsById.put(id, newInst);
		this.pairsByName.put(namedIdentifier, newInst);
		return newInst;
	}

	public final Collection<AssemblyComponentOperationPair> getPairs() {
		return this.pairsById.values();
	}

	public AssemblyComponentOperationPair getRootPair() {
		return ROOT_PAIR;
	}
}
