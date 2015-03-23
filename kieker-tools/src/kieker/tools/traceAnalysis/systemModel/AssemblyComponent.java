/***************************************************************************
 * Copyright 2015 Kieker Project (http://kieker-monitoring.net)
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

/**
 * 
 * @author Andre van Hoorn
 * 
 * @since 1.1
 */
public class AssemblyComponent implements ISystemModelElement {
	private final int id;
	private final String name;
	private final ComponentType type;

	/**
	 * Creates a new instance of this class using the given parameters.
	 * 
	 * @param id
	 *            The ID of this assembly component.
	 * @param name
	 *            The name of this component.
	 * @param type
	 *            The type of this component.
	 */
	public AssemblyComponent(final int id, final String name, final ComponentType type) {
		this.id = id;
		this.name = name;
		this.type = type;
	}

	public final int getId() {
		return this.id;
	}

	public final String getName() {
		return this.name;
	}

	public final ComponentType getType() {
		return this.type;
	}

	@Override
	public final String toString() {
		final StringBuilder strBuild = new StringBuilder();
		strBuild.append(this.name).append(':').append(this.type.getFullQualifiedName());
		return strBuild.toString();
	}

	@Override
	public int hashCode() {
		return this.id;
	}

	@Override
	public boolean equals(final Object obj) {
		if (!(obj instanceof AssemblyComponent)) {
			return false;
		}
		final AssemblyComponent other = (AssemblyComponent) obj;
		return other.id == this.id;
	}

	/**
	 * Denotes whether this assembly component is a root component.
	 * 
	 * @return See above
	 */
	public boolean isRootComponent() {
		return false;
	}

	@Override
	public String getIdentifier() {
		if (this.getType() == null) {
			return this.getName();
		} else {
			return this.getType().getFullQualifiedName();
		}
	}
}
