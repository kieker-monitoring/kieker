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

package kieker.tools.trace.analysis.systemModel;

import kieker.common.util.signature.Signature;
import kieker.tools.trace.analysis.systemModel.repository.AbstractSystemSubRepository;

/**
 * This class represents an operation within the trace analysis tool. It
 * consists of the component type and a signature.
 *
 * @author Andre van Hoorn
 *
 * @since 1.1
 * @deprecated 1.15 moved to kieker-model
 */
@Deprecated
public class Operation {

	/** The ID for the root operation. */
	public static final int ROOT_OPERATION_ID = AbstractSystemSubRepository.ROOT_ELEMENT_ID;

	private final int id;
	private final ComponentType componentType;
	private final Signature signature;

	/**
	 * Creates a new instance of this class using the given parameters.
	 *
	 * @param id
	 *            The ID of this operation.
	 * @param componentType
	 *            The type of the component of this operation.
	 * @param signature
	 *            The signature of this operation.
	 */
	public Operation(final int id, final ComponentType componentType, final Signature signature) {
		this.id = id;
		this.componentType = componentType;
		this.signature = signature;
	}

	/**
	 * Delivers the ID of the operation.
	 *
	 * @return The ID.
	 */
	public final int getId() {
		return this.id;
	}

	/**
	 * Delivers the component type of the operation.
	 *
	 * @return The component type.
	 */
	public final ComponentType getComponentType() {
		return this.componentType;
	}

	/**
	 * Delivers the signature of the operation.
	 *
	 * @return The signature.
	 */
	public final Signature getSignature() {
		return this.signature;
	}

	/**
	 * Two Operation objects are equal if their ids are equal.
	 *
	 * @param obj
	 *            The object to be compared for equality with this
	 * @return true if the two objects are equal.
	 */
	@Override
	public boolean equals(final Object obj) {
		if (!(obj instanceof Operation)) {
			return false;
		}
		final Operation other = (Operation) obj;
		return other.id == this.id;
	}

	@Override
	public int hashCode() {
		int hash = 5;
		hash = (17 * hash) + this.id;
		return hash;
	}

	@Override
	public String toString() {
		final StringBuilder strBuild = new StringBuilder();
		strBuild.append(this.componentType.getFullQualifiedName()).append('.').append(this.signature.toString());
		return strBuild.toString();
	}
}
