/***************************************************************************
 * Copyright 2023 Kieker Project (http://kieker-monitoring.net)
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

package kieker.model.system.model;

/**
 * 
 * @author Andre van Hoorn
 * 
 * @since 1.1
 */
public class AllocationComponent implements ISystemModelElement {

	private final int id;
	private final AssemblyComponent assemblyComponent;
	private final ExecutionContainer executionContainer;

	public AllocationComponent(final int id, final AssemblyComponent assemblyComponent, final ExecutionContainer executionContainer) {
		this.id = id;
		this.assemblyComponent = assemblyComponent;
		this.executionContainer = executionContainer;
	}

	public final int getId() {
		return this.id;
	}

	public final AssemblyComponent getAssemblyComponent() {
		return this.assemblyComponent;
	}

	public final ExecutionContainer getExecutionContainer() {
		return this.executionContainer;
	}

	@Override
	public final String toString() {
		final StringBuilder strBuild = new StringBuilder();
		strBuild.append(this.executionContainer.getName()).append("::").append(this.assemblyComponent.toString());
		return strBuild.toString();
	}

	@Override
	public int hashCode() {
		return this.id;
	}

	@Override
	public boolean equals(final Object obj) {
		if (!(obj instanceof AllocationComponent)) {
			return false;
		}
		final AllocationComponent other = (AllocationComponent) obj;
		return other.id == this.id;
	}

	@Override
	public String getIdentifier() {
		return this.getAssemblyComponent().getName();
	}
}
