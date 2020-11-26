/***************************************************************************
 * Copyright 2020 Kieker Project (http://kieker-monitoring.net)
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

package kieker.tools.trace.analysis.systemModel;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

/**
 * This class represents the type of a component within the trace analysis tool.
 *
 * @author Andre van Hoorn
 *
 * @since 1.1
 * @deprecated 1.15 moved to kieker-model
 */
@Deprecated
public class ComponentType {
	private final int id;
	private final String packageName;
	private final String typeName;
	private final Collection<Operation> operations = Collections.synchronizedList(new ArrayList<Operation>());

	/**
	 * // NOCS requests implementation of equals and hashCode in pairs// NOCS
	 * requests implementation of equals and hashCode in pairs* Creates a new
	 * instance of this class using the given parameters.
	 *
	 * @param id
	 *            The ID of the component type.
	 * @param packageName
	 *            The package name.
	 * @param typeName
	 *            The type name.
	 */
	public ComponentType(final int id, final String packageName, final String typeName) {
		this.id = id;
		this.packageName = packageName;
		this.typeName = typeName;
	}

	/**
	 * Creates a new instance of this class using the given parameters.
	 *
	 * @param id
	 *            The ID of the component type.
	 * @param fullqualifiedTypeName
	 *            The fully qualified name of the type, separated
	 *            with '.'.
	 */
	public ComponentType(final int id, final String fullqualifiedTypeName) {
		this.id = id;
		final String tmpPackagName;
		final String tmpTypeName;
		if (fullqualifiedTypeName.indexOf('.') != -1) {
			final String tmpComponentName = fullqualifiedTypeName;
			int index = 0;
			for (index = tmpComponentName.length() - 1; index > 0; index--) {
				if (tmpComponentName.charAt(index) == '.') {
					break;
				}
			}
			tmpPackagName = tmpComponentName.substring(0, index);
			tmpTypeName = tmpComponentName.substring(index + 1);
		} else {
			tmpPackagName = "";
			tmpTypeName = fullqualifiedTypeName;
		}
		this.packageName = tmpPackagName;
		this.typeName = tmpTypeName;
	}

	/**
	 * Delivers the ID of the component type.
	 *
	 * @return The ID.
	 */
	public final int getId() {
		return this.id;
	}

	/**
	 * Delivers the name of the type.
	 *
	 * @return The type name.
	 */
	public final String getTypeName() {
		return this.typeName;
	}

	/**
	 * Delivers the package name of this type.
	 *
	 * @return The package name.
	 */
	public final String getPackageName() {
		return this.packageName;
	}

	/**
	 * Delivers the full qualified name of this type (the packages are separated
	 * with '.').
	 *
	 * @return The full qualified name.
	 */
	public final String getFullQualifiedName() {
		if ((this.packageName == null) || (this.packageName.length() == 0)) {
			return this.typeName;
		}
		return this.packageName + "." + this.typeName;
	}

	/**
	 * Delivers a collection containing the available operations within this
	 * component type.
	 *
	 * @return The operations.
	 */
	public final Collection<Operation> getOperations() {
		return this.operations;
	}

	/**
	 * This method adds a given operation to the list of available operations.
	 *
	 * @param op
	 *            The operation to be added.
	 *
	 * @return The added operation.
	 */
	public final Operation addOperation(final Operation op) {
		this.operations.add(op);
		return op;
	}

	@Override
	public int hashCode() {
		return this.id;
	}

	@Override
	public boolean equals(final Object obj) {
		if (!(obj instanceof ComponentType)) {
			return false;
		}
		final ComponentType other = (ComponentType) obj;
		return other.id == this.id;
	}
}
