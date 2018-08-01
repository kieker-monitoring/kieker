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

package kieker.common.util.signature;

import java.util.Arrays;

/**
 * A signature for a software operation. Note that this is just an operation signature declaration which is not bound to an implementing class or interface.
 * 
 * @author Andre van Hoorn
 * 
 * @since 1.1
 */
public class Signature {

	/** This constant can be used to mark that a signature has no return type. */
	public static final String NO_RETURN_TYPE = "<NO-RETURN-TYPE>";

	public static final String CONSTRUCTOR_METHOD_NAME = "<init>";

	private final String name;

	private final String[] modifierList;
	private final String returnType;
	private final String[] paramTypeList;

	/**
	 * 
	 * Please use the constant {@link #NO_RETURN_TYPE} to indicate that the Signature contains no return type.
	 * 
	 * @param name
	 *            The name of the operation.
	 * @param modifierList
	 *            The list of modifiers of the operation.
	 * @param returnType
	 *            The return type of the operation. If this parameter is null, the return type will be set to {@link #NO_RETURN_TYPE}
	 * @param paramTypeList
	 *            The list of parameters of the operation.s
	 */
	public Signature(final String name, final String[] modifierList, final String returnType, final String[] paramTypeList) {
		this.name = name;
		this.modifierList = modifierList.clone();
		if (returnType == null) {
			this.returnType = NO_RETURN_TYPE;
		} else {
			this.returnType = returnType;
		}
		this.paramTypeList = paramTypeList.clone();
	}

	public final String getName() {
		return this.name;
	}

	public final String[] getModifier() {
		return this.modifierList.clone();
	}

	public final String[] getParamTypeList() {
		return this.paramTypeList.clone();
	}

	public final String getReturnType() {
		return this.returnType;
	}

	/**
	 * Tells whether the current signature has a return type or not.
	 * 
	 * @return true if and only if the signature has a return type.
	 */
	public final boolean hasReturnType() {
		return this.returnType != NO_RETURN_TYPE;
	}

	@Override
	public String toString() {
		final StringBuilder strBuild = new StringBuilder();
		for (final String t : this.modifierList) {
			strBuild.append(t);
			strBuild.append(' ');
		}
		strBuild.append(this.name).append('(');
		boolean first = true;
		for (final String t : this.paramTypeList) {
			if (!first) {
				strBuild.append(',');
			} else {
				first = false;
			}
			strBuild.append(t);
		}
		strBuild.append(')');
		if (this.hasReturnType()) {
			strBuild.append(':').append(this.returnType);
		}
		return strBuild.toString();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = (prime * result) + Arrays.hashCode(this.modifierList);
		result = (prime * result) + ((this.name == null) ? 0 : this.name.hashCode()); // NOCS (?:)
		result = (prime * result) + Arrays.hashCode(this.paramTypeList);
		result = (prime * result) + ((this.returnType == null) ? 0 : this.returnType.hashCode()); // NOCS (?:)
		return result;
	}

	@Override
	public boolean equals(final Object obj) {
		if (this == obj) {
			return true;
		}
		if ((obj == null) || (this.getClass() != obj.getClass())) {
			return false;
		}
		final Signature other = (Signature) obj;
		// could also be checked without order?
		if (!Arrays.equals(this.modifierList, other.modifierList)) {
			return false;
		}
		if (this.name == null) {
			if (other.name != null) {
				return false;
			}
		} else if (!this.name.equals(other.name)) {
			return false;
		}
		if (!Arrays.equals(this.paramTypeList, other.paramTypeList)) {
			return false;
		}
		if (this.returnType == null) {
			if (other.returnType != null) {
				return false;
			}
		} else if (!this.returnType.equals(other.returnType)) {
			return false;
		}
		return true;
	}
}
