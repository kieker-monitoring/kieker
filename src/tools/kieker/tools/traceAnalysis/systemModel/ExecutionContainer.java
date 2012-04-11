/***************************************************************************
 * Copyright 2012 by
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

package kieker.tools.traceAnalysis.systemModel;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

/**
 * 
 * @author Andre van Hoorn
 */
public class ExecutionContainer {
	private final int id;
	private final String name;
	private final ExecutionContainer parent;
	private final Collection<ExecutionContainer> childContainers = Collections.synchronizedList(new ArrayList<ExecutionContainer>());

	public ExecutionContainer(final int id, final ExecutionContainer parent, final String name) {
		this.id = id;
		this.name = name;
		this.parent = parent;
	}

	public final int getId() {
		return this.id;
	}

	public final String getName() {
		return this.name;
	}

	public final ExecutionContainer getParent() {
		return this.parent;
	}

	public final Collection<ExecutionContainer> getChildContainers() {
		return this.childContainers;
	}

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
}
