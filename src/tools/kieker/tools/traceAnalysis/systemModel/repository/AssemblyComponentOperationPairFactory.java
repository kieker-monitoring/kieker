/***************************************************************************
 * Copyright 2011 by
 *  + Christian-Albrechts-University of Kiel
 *    + Department of Computer Science
 *      + Software Engineering Group 
 *  and others.
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

import kieker.tools.traceAnalysis.systemModel.AssemblyComponent;
import kieker.tools.traceAnalysis.systemModel.Operation;
import kieker.tools.traceAnalysis.systemModel.util.AssemblyComponentOperationPair;

/**
 * 
 * @author Andre van Hoorn
 */
public class AssemblyComponentOperationPairFactory extends AbstractSystemSubRepository {
	public final AssemblyComponentOperationPair rootPair;

	private final Map<String, AssemblyComponentOperationPair> pairsByName = new Hashtable<String, AssemblyComponentOperationPair>();
	private final Map<Integer, AssemblyComponentOperationPair> pairsById = new Hashtable<Integer, AssemblyComponentOperationPair>();

	public AssemblyComponentOperationPairFactory(final SystemModelRepository systemFactory) {
		super(systemFactory);
		final AssemblyComponent rootAssembly = systemFactory.getAssemblyFactory().rootAssemblyComponent;
		final Operation rootOperation = systemFactory.getOperationFactory().rootOperation;
		this.rootPair = this.getPairInstanceByPair(rootAssembly, rootOperation);
	}

	/** Returns a corresponding pair instance (existing or newly created) */
	public final AssemblyComponentOperationPair getPairInstanceByPair(final AssemblyComponent assemblyComponent, final Operation operation) {
		final AssemblyComponentOperationPair inst = this.getPairByNamedIdentifier(assemblyComponent.getId() + "-" + operation.getId());
		if (inst == null) {
			return this.createAndRegisterPair(operation, assemblyComponent);
		}
		return inst;
	}

	private AssemblyComponentOperationPair createAndRegisterPair(final Operation operation, final AssemblyComponent assemblyComponent) {
		return this.createAndRegisterPair(assemblyComponent.getId() + "-" + operation.getId(), operation, assemblyComponent);
	}

	/**
	 * Returns the instance for the passed factory name; null if no instance
	 * with this factory name.
	 */
	private AssemblyComponentOperationPair getPairByNamedIdentifier(final String namedIdentifier) {
		return this.pairsByName.get(namedIdentifier);
	}

	/**
	 * Returns the instance for the passed ID; null if no instance
	 * with this ID.
	 */
	public final AssemblyComponentOperationPair getPairById(final int id) {
		return this.pairsById.get(id);
	}

	private AssemblyComponentOperationPair createAndRegisterPair(final String namedIdentifier, final Operation operation, final AssemblyComponent assemblyComponent) {
		AssemblyComponentOperationPair newInst;
		if (this.pairsByName.containsKey(namedIdentifier)) {
			throw new IllegalArgumentException("Element with name " + namedIdentifier + "exists already");
		}
		final int id = this.getAndIncrementNextId();
		newInst = new AssemblyComponentOperationPair(id, operation, assemblyComponent);
		this.pairsById.put(id, newInst);
		this.pairsByName.put(namedIdentifier, newInst);
		return newInst;
	}

	public final Collection<AssemblyComponentOperationPair> getPairs() {
		return this.pairsById.values();
	}
}
