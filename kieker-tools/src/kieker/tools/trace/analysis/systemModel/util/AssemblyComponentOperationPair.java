/***************************************************************************
 * Copyright 2022 Kieker Project (http://kieker-monitoring.net)
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

package kieker.tools.trace.analysis.systemModel.util;

import kieker.tools.trace.analysis.systemModel.AssemblyComponent;
import kieker.tools.trace.analysis.systemModel.ISystemModelElement;
import kieker.tools.trace.analysis.systemModel.Operation;

/**
 * This class represents a pair consisting of an {@link Operation} and an {@link AssemblyComponent}.
 * 
 * @author Andre van Hoorn
 * 
 * @since 1.1
 */
public class AssemblyComponentOperationPair implements ISystemModelElement {
	private final int id;
	private final Operation operation;

	private final AssemblyComponent assemblyComponent;

	/**
	 * Creates a new instance of this class using the given parameters.
	 * 
	 * @param id
	 *            The ID of this pair.
	 * @param operation
	 *            The operation.
	 * @param assemblyComponent
	 *            The assembly component.
	 */
	public AssemblyComponentOperationPair(final int id, final Operation operation, final AssemblyComponent assemblyComponent) {
		this.id = id;
		this.operation = operation;
		this.assemblyComponent = assemblyComponent;
	}

	public final int getId() {
		return this.id;
	}

	public final AssemblyComponent getAssemblyComponent() {
		return this.assemblyComponent;
	}

	public final Operation getOperation() {
		return this.operation;
	}

	@Override
	public String toString() {
		return +this.assemblyComponent.getId() + ":" + this.operation.getId() + "@" + this.id;
	}

	@Override
	public String getIdentifier() {
		return this.getAssemblyComponent().getIdentifier();
	}
}
