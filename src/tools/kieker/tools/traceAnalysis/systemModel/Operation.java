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

package kieker.tools.traceAnalysis.systemModel;

import kieker.common.util.Signature;
import kieker.tools.traceAnalysis.systemModel.repository.AbstractSystemSubRepository;

/**
 * 
 * @author Andre van Hoorn
 */
public class Operation {
	public static final int ROOT_OPERATION_ID = AbstractSystemSubRepository.ROOT_ELEMENT_ID;
	private final int id;
	private final ComponentType componentType;
	private final Signature signature;

	public Operation(final int id, final ComponentType componentType, final Signature signature) {
		this.id = id;
		this.componentType = componentType;
		this.signature = signature;
	}

	public final int getId() {
		return this.id;
	}

	public final ComponentType getComponentType() {
		return this.componentType;
	}

	public final Signature getSignature() {
		return this.signature;
	}

	/**
	 * Two Operation objects are equal if their ids are equal.
	 * 
	 * @param obj
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
		int hash = 5; // NOCS (MagicNumberCheck)
		hash = (17 * hash) + this.id; // NOCS (MagicNumberCheck)
		return hash;
	}

	@Override
	public String toString() {
		final StringBuilder strBuild = new StringBuilder();
		strBuild.append(this.componentType.getFullQualifiedName()).append(".").append(this.signature.toString());
		return strBuild.toString();
	}
}
