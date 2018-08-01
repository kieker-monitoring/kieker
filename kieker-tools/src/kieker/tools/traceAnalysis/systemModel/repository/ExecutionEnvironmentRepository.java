/***************************************************************************
 * Copyright 2018 Kieker Project (http://kieker-monitoring.net)
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

import kieker.tools.traceAnalysis.systemModel.ExecutionContainer;
import kieker.tools.traceAnalysis.systemModel.RootExecutionContainer;

/**
 * 
 * @author Andre van Hoorn
 * 
 * @since 1.1
 */
public class ExecutionEnvironmentRepository extends AbstractSystemSubRepository {

	/** The root execution container. */
	public static final ExecutionContainer ROOT_EXECUTION_CONTAINER = new RootExecutionContainer();

	private final Map<String, ExecutionContainer> executionContainersByName = new Hashtable<String, ExecutionContainer>(); // NOPMD (UseConcurrentHashMap)
	private final Map<Integer, ExecutionContainer> executionContainersById = new Hashtable<Integer, ExecutionContainer>(); // NOPMD (UseConcurrentHashMap)

	/**
	 * Creates a new instance of this class using the given parameters.
	 * 
	 * @param systemFactory
	 *            The system factory.
	 */
	public ExecutionEnvironmentRepository(final SystemModelRepository systemFactory) {
		super(systemFactory);
	}

	/**
	 * @param namedIdentifier
	 *            The identifier to search for.
	 * 
	 * @return The instance for the passed namedIdentifier; null if no instance with this namedIdentifier.
	 */
	public final ExecutionContainer lookupExecutionContainerByNamedIdentifier(final String namedIdentifier) {
		return this.executionContainersByName.get(namedIdentifier);
	}

	/**
	 * Returns the instance for the passed container ID; null if no instance
	 * with this ID.
	 * 
	 * @param containerId
	 *            The ID to search for.
	 * 
	 * @return The container for the given ID if it exists; null otherwise.
	 */
	public final ExecutionContainer lookupExecutionContainerByContainerId(final int containerId) {
		return this.executionContainersById.get(containerId);
	}

	/**
	 * This method creates a new execution container and registers it as well.
	 * 
	 * @param namedIdentifier
	 *            The identifier of the new container.
	 * @param name
	 *            The name of the new container.
	 * 
	 * @return The newly created execution container.
	 */
	public final ExecutionContainer createAndRegisterExecutionContainer(final String namedIdentifier, final String name) {
		if (this.executionContainersByName.containsKey(namedIdentifier)) {
			throw new IllegalArgumentException("Element with name " + namedIdentifier + "exists already");
		}
		final int id = this.getAndIncrementNextId();
		final ExecutionContainer newInst = new ExecutionContainer(id, null, name);
		this.executionContainersById.put(id, newInst);
		this.executionContainersByName.put(namedIdentifier, newInst);
		return newInst;
	}

	/**
	 * Delivers all available execution containers.
	 * 
	 * @return A collection containing the available containers.
	 */
	public final Collection<ExecutionContainer> getExecutionContainers() {
		return this.executionContainersById.values();
	}
}
