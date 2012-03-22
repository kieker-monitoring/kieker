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

package kieker.common.util;

import java.util.StringTokenizer;

/**
 * TODO: Add methods getPackageName and getSimpleClassname which
 * is a split of the FQ class name.
 * 
 * @author Andre van Hoorn
 * 
 */
public class ClassOperationSignaturePair {
	private final String fqClassname;
	private final Signature signature;

	/**
	 * 
	 * @param fqClassname
	 * @param signature
	 */
	public ClassOperationSignaturePair(final String fqClassname, final Signature signature) {
		this.fqClassname = fqClassname;
		this.signature = signature;
	}

	/**
	 * @return the fqClassname
	 */
	public String getFqClassname() {
		return this.fqClassname;
	}

	/**
	 * @return the signature
	 */
	public Signature getSignature() {
		return this.signature;
	}

	public String toOperationSignatureString() {
		return ClassOperationSignaturePair.createOperationSignatureString(this.fqClassname, this.signature);
	}

	/**
	 * Extracts an {@link ClassOperationSignaturePair} from an an operation signature string (e.g., <code>public static Boolean a.b.c.D.op1(Integer, Long)</code>).
	 * Modifier list, return type, and parameter list wrapped by parentheses are optional. But note that
	 * a return type must be given if one or more modifiers are present.
	 * 
	 * TODO: Move this method to the then-extracted class FQComponentNameSignaturePair
	 * 
	 * @param operationSignatureStr
	 * @return
	 */
	public static ClassOperationSignaturePair splitOperationSignatureStr(final String operationSignatureStr) {
		final String fqClassname;
		final String returnType;
		String name;
		String opName;
		String[] paramTypeList;
		String[] modifierList;
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
			// name = modRetName.substring(nameBeginIdx + 1);
			final String[] modRetNameArr = modRetName.split("\\s");
			name = modRetNameArr[modRetNameArr.length - 1];
			returnType = modRetNameArr[modRetNameArr.length - 2];
			modifierList = new String[modRetNameArr.length - 2];
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
	 * Given a fully-qualified class name <i>fqClassName</i> (e.g., <code>a.b.c.D</code>) and
	 * and a {@link Signature} (e.g., for operation <code>op1</code> with modifiers <code>public</code> and <code>static</code>, the return type <code>Boolean</code>
	 * ,
	 * and the parameter types <code>Integer</code> and <code>Long</code>), the method
	 * returns an operation signature string (e.g., <code>public static Boolean a.b.c.D.op1(Integer, Long)</code>).
	 * 
	 * @param fqClassName
	 * @param signature
	 * @return
	 */
	public static String createOperationSignatureString(final String fqClassName, final Signature signature) {
		final StringBuilder strBuilder = new StringBuilder();

		if ((signature.getModifier().length != 0) && (!signature.hasReturnType())) {
			throw new IllegalArgumentException("Modifier not list empty but return type null/empty");
		}

		/* Append modifiers and return type */
		if ((signature.getReturnType() != null) && (signature.getReturnType().length() != 0)) {
			for (final String type : signature.getModifier()) {
				strBuilder.append(type).append(" ");
			}
			if (signature.hasReturnType()) {
				strBuilder.append(signature.getReturnType()).append(" ");
			}
		}

		/* Append operation name and parameter type list */
		strBuilder.append(fqClassName);
		strBuilder.append(".").append(signature.getName());
		strBuilder.append("(");
		boolean first = true;
		for (final String type : signature.getParamTypeList()) {
			if (!first) {
				strBuilder.append(", ");
			}
			first = false;
			strBuilder.append(type);
		}
		strBuilder.append(")");

		return strBuilder.toString();
	}
}
