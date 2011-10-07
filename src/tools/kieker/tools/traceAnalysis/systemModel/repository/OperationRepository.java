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

import kieker.tools.traceAnalysis.systemModel.ComponentType;
import kieker.tools.traceAnalysis.systemModel.Operation;
import kieker.tools.traceAnalysis.systemModel.Signature;

/**
 * 
 * @author Andre van Hoorn
 */
public class OperationRepository extends AbstractSystemSubRepository {
	private final Map<String, Operation> operationsByName = new Hashtable<String, Operation>();
	private final Map<Integer, Operation> operationsById = new Hashtable<Integer, Operation>();

	public final Operation rootOperation;

	public OperationRepository(final SystemModelRepository systemFactory, final Operation rootOperation) {
		super(systemFactory);
		this.rootOperation = rootOperation;
	}

	/**
	 * Returns the instance for the passed namedIdentifier; null if no instance
	 * with this namedIdentifier.
	 */
	public final Operation lookupOperationByNamedIdentifier(final String namedIdentifier) {
		return this.operationsByName.get(namedIdentifier);
	}

	public final Operation createAndRegisterOperation(final String namedIdentifier, final ComponentType componentType, final Signature signature) {
		Operation newInst;
		if (this.operationsByName.containsKey(namedIdentifier)) {
			throw new IllegalArgumentException("Element with name " + namedIdentifier + "exists already");
		}
		final int id = this.getAndIncrementNextId();
		newInst = new Operation(id, componentType, signature);
		this.operationsById.put(id, newInst);
		this.operationsByName.put(namedIdentifier, newInst);
		return newInst;
	}

	public final Collection<Operation> getOperations() {
		return this.operationsById.values();
	}
}
