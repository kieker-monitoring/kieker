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

/**
 * 
 * @author Andre van Hoorn
 */
public class Signature {
	private final String name;

	public static final String NO_RETURN_TYPE = "<NO-RETURN-TYPE>";

	private final String[] modifierList;
	private final String returnType;
	private final String[] paramTypeList;

	/**
	 * 
	 * Please use the constant {@link #NO_RETURN_TYPE} to indicate that the Signature contains no return type.
	 * 
	 * @param name
	 * @param modifierList
	 * @param returnType
	 *            if null, the return type will be set to {@link #NO_RETURN_TYPE}
	 * @param paramTypeList
	 */
	public Signature(final String name, final String[] modifierList, final String returnType, final String[] paramTypeList) {
		this.name = name;
		this.modifierList = modifierList;
		if (returnType == null) {
			this.returnType = Signature.NO_RETURN_TYPE;
		} else {
			this.returnType = returnType;
		}
		this.paramTypeList = paramTypeList.clone();
	}

	public final String getName() {
		return this.name;
	}

	public final String[] getModifier() {
		return this.modifierList;
	}

	public final String[] getParamTypeList() {
		return this.paramTypeList.clone();
	}

	public final String getReturnType() {
		return this.returnType;
	}

	public final boolean hasReturnType() {
		return this.returnType != Signature.NO_RETURN_TYPE;
	}

	@Override
	public String toString() {
		final StringBuilder strBuild = new StringBuilder();
		for (final String t : this.modifierList) {
			strBuild.append(t);
			strBuild.append(" ");
		}
		strBuild.append(this.name).append("(");
		boolean first = true;
		for (final String t : this.paramTypeList) {
			if (!first) {
				strBuild.append(",");
			} else {
				first = false;
			}
			strBuild.append(t);
		}
		strBuild.append(")");
		if (this.hasReturnType()) {
			strBuild.append(":").append(this.returnType);
		}
		return strBuild.toString();
	}

	// TODO: implement equals
}
