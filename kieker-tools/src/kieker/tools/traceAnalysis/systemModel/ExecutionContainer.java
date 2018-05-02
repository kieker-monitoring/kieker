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

package kieker.tools.traceAnalysis.systemModel;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

/**
 * @author Andre van Hoorn
 * 
 * @since 1.1
 */
public class ExecutionContainer implements ISystemModelElement {
	private final int id;
	private final String name;
	private final ExecutionContainer parent;
	private final Collection<ExecutionContainer> childContainers = Collections.synchronizedList(new ArrayList<ExecutionContainer>());

	/**
	 * Creates a new instance of this class using the given parameters.
	 * 
	 * @param id
	 *            The ID of this container.
	 * @param parent
	 *            The parent of this container.
	 * @param name
	 *            The name of this container.
	 */
	public ExecutionContainer(final int id, final ExecutionContainer parent, final String name) {
		this.id = id;
		this.name = name;
		this.parent = parent;
	}

	/**
	 * Delivers the ID of the container.
	 * 
	 * @return The ID.
	 */
	public final int getId() {
		return this.id;
	}

	/**
	 * Delivers the name of the container.
	 * 
	 * @return The name.
	 */
	public final String getName() {
		return this.name;
	}

	/**
	 * Delivers the parent of the container.
	 * 
	 * @return The parent.
	 */
	public final ExecutionContainer getParent() {
		return this.parent;
	}

	/**
	 * Delivers a collection containing the added child containers.
	 * 
	 * @return The child containers.
	 */
	public final Collection<ExecutionContainer> getChildContainers() {
		return this.childContainers;
	}

	/**
	 * This method adds a given container to the list of child containers.
	 * 
	 * @param container
	 *            The new child container.
	 */
	public final void addChildContainer(final ExecutionContainer container) {
		this.childContainers.add(this);
	}

	@Override
	public int hashCode() {
		return this.id;
	}

	@Override
	public boolean equals(final Object obj) {
		if (!(obj instanceof ExecutionContainer)) {
			return false;
		}
		final ExecutionContainer other = (ExecutionContainer) obj;
		return other.id == this.id;
	}

	/**
	 * Returns whether this container is a root container.
	 * 
	 * @return See above
	 */
	public boolean isRootContainer() {
		return false;
	}

	/**
	 * Delivers the identifier (name) of this object.
	 * 
	 * @return The identifier.
	 */
	@Override
	public String getIdentifier() {
		return this.getName();
	}
}
