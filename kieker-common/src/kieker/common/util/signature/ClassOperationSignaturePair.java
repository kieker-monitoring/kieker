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

package kieker.common.util.signature;

import java.util.StringTokenizer;

/**
 * This class represents a pair containing the classname and the signature of an operation.
 * 
 * @author Andre van Hoorn, Jan Waller
 * 
 * @since 1.5
 */
public class ClassOperationSignaturePair {

	private final int lastDot;
	private final String fqClassname;
	private final Signature signature;

	/**
	 * Creates a new instance of this class using the given parameters.
	 * 
	 * @param fqClassname
	 *            The fully qualified name of the class.
	 * @param signature
	 *            The signature.
	 */
	public ClassOperationSignaturePair(final String fqClassname, final Signature signature) {
		this.fqClassname = fqClassname;
		this.signature = signature;
		this.lastDot = fqClassname.lastIndexOf('.');
	}

	/**
	 * Delivers the fully qualified class name.
	 * 
	 * @return the fqClassname
	 */
	public String getFqClassname() {
		return this.fqClassname;
	}

	/**
	 * Delivers the signature.
	 * 
	 * @return the signature
	 */
	public Signature getSignature() {
		return this.signature;
	}

	/**
	 * This method delivers a simplified version of the fully qualified class name. In other words: The string behind the last '.' of the fully qualified name (this
	 * is usually just the name of the class and nothing else).
	 * 
	 * @return The simple class name.
	 */
	public String getSimpleClassname() {
		if (this.lastDot == -1) {
			return this.fqClassname;
		} else {
			return this.fqClassname.substring(this.lastDot + 1);
		}
	}

	/**
	 * This method delivers the package name of the class. In other words: This method returns everything before the last '.' of the fully qualified name.
	 * 
	 * @return The package name.
	 */
	public String getPackageName() {
		if (this.lastDot == -1) {
			return "";
		} else {
			return this.fqClassname.substring(0, this.lastDot);
		}
	}

	/**
	 * This method assembles an operation signature string from the current fields.
	 * 
	 * @return An operation signature string.
	 */
	public String toOperationSignatureString() {
		return ClassOperationSignaturePair.createOperationSignatureString(this.fqClassname, this.signature);
	}

	/**
	 * Extracts an {@link ClassOperationSignaturePair} from an operation signature string (e.g., <code>public static Boolean a.b.c.D.op1(Integer, Long)</code>).
	 * Modifier list, return type, and parameter list wrapped by parentheses are optional. But note that a return type must be given if one or more modifiers are
	 * present.
	 * 
	 * @param operationSignatureStr
	 *            The signature string.
	 * 
	 * @return The {@link ClassOperationSignaturePair} extracted from the given string.
	 */
	public static ClassOperationSignaturePair splitOperationSignatureStr(final String operationSignatureStr) {
		return ClassOperationSignaturePair.splitOperationSignatureStr(operationSignatureStr, false);
	}

	/**
	 * Split up an operation signature string and populate a class operation signature pair.
	 * 
	 * @param operationSignatureStr
	 *            the signature string
	 * @param javaConstructor
	 *            if true the string holds a constructor signature
	 * 
	 * @return a ClassOperationSignaturePair
	 */
	public static ClassOperationSignaturePair splitOperationSignatureStr(final String operationSignatureStr, final boolean javaConstructor) {
		final String fqClassname;
		final String returnType;
		final String name;
		final String opName;
		final String[] paramTypeList;
		final String[] modifierList;
		final int openParenIdx = operationSignatureStr.indexOf('(');
		final String modRetName;

		if (openParenIdx == -1) { // no parameter list
			paramTypeList = new String[] {};
			modRetName = operationSignatureStr;
		} else {
			modRetName = operationSignatureStr.substring(0, openParenIdx);
			final StringTokenizer strTokenizer = new StringTokenizer(operationSignatureStr.substring(openParenIdx + 1, operationSignatureStr.length() - 1), ",");
			paramTypeList = new String[strTokenizer.countTokens()];
			for (int i = 0; strTokenizer.hasMoreTokens(); i++) {
				paramTypeList[i] = strTokenizer.nextToken().trim();
			}
		}
		final int nameBeginIdx = modRetName.lastIndexOf(' ');
		if (nameBeginIdx == -1) {
			name = modRetName;
			returnType = null;
			modifierList = new String[] {};
		} else {
			final String[] modRetNameArr = modRetName.split("\\s");
			name = modRetNameArr[modRetNameArr.length - 1];
			if (javaConstructor) {
				returnType = null;
				modifierList = new String[modRetNameArr.length - 1];
			} else {
				returnType = modRetNameArr[modRetNameArr.length - 2];
				modifierList = new String[modRetNameArr.length - 2];
			}
			System.arraycopy(modRetNameArr, 0, modifierList, 0, modifierList.length);
		}
		final int opNameIdx = name.lastIndexOf('.');
		if (opNameIdx != -1) {
			fqClassname = name.substring(0, opNameIdx);
		} else {
			fqClassname = "";
		}
		opName = name.substring(opNameIdx + 1);
		return new ClassOperationSignaturePair(fqClassname, new Signature(opName, modifierList, returnType, paramTypeList));
	}

	/**
	 * Given a fully-qualified class name <i>fqClassName</i> (e.g., <code>a.b.c.D</code>) and a {@link Signature} (e.g., for operation <code>op1</code> with
	 * modifiers <code>public</code> and <code>static</code>, the return type <code>Boolean</code> , and the parameter types <code>Integer</code> and
	 * <code>Long</code>), the method returns an operation signature string (e.g., <code>public static Boolean a.b.c.D.op1(Integer, Long)</code>).
	 * 
	 * @param fqClassName
	 *            The fully qualified class name.
	 * @param signature
	 *            The signature string.
	 * 
	 * @return An operation signature string as defined by the given parameters.
	 */
	public static String createOperationSignatureString(final String fqClassName, final Signature signature) {
		final StringBuilder strBuilder = new StringBuilder();
		// Append modifiers and return type
		if ((signature.getReturnType() != null) && (signature.getReturnType().length() != 0)) {
			for (final String type : signature.getModifier()) {
				strBuilder.append(type).append(' ');
			}
			if (signature.hasReturnType()) {
				strBuilder.append(signature.getReturnType()).append(' ');
			}
		}

		// Append operation name and parameter type list
		strBuilder.append(fqClassName)
				.append('.').append(signature.getName())
				.append('(');
		boolean first = true;
		for (final String type : signature.getParamTypeList()) {
			if (!first) {
				strBuilder.append(", ");
			}
			first = false;
			strBuilder.append(type);
		}
		strBuilder.append(')');

		return strBuilder.toString();
	}

	/**
	 * Returns a String representation of this {@link ClassOperationSignaturePair} using the {@link #createOperationSignatureString(String, Signature)} method.
	 * 
	 * @return A string representation of this object.
	 */
	@Override
	public String toString() {
		return ClassOperationSignaturePair.createOperationSignatureString(this.fqClassname, this.signature);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = (prime * result) + ((this.fqClassname == null) ? 0 : this.fqClassname.hashCode()); // NOCS
		result = (prime * result) + this.lastDot;
		result = (prime * result) + ((this.signature == null) ? 0 : this.signature.hashCode()); // NOCS
		return result;
	}

	@Override
	public boolean equals(final Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (this.getClass() != obj.getClass()) {
			return false;
		}
		final ClassOperationSignaturePair other = (ClassOperationSignaturePair) obj;
		if (this.fqClassname == null) {
			if (other.fqClassname != null) {
				return false;
			}
		} else if (!this.fqClassname.equals(other.fqClassname)) {
			return false;
		}
		if (this.lastDot != other.lastDot) {
			return false;
		}
		if (this.signature == null) {
			if (other.signature != null) {
				return false;
			}
		} else if (!this.signature.equals(other.signature)) {
			return false;
		}
		return true;
	}
}
