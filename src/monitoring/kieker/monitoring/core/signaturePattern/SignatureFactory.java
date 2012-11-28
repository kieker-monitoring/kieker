/***************************************************************************
 * Copyright 2012 Kieker Project (http://kieker-monitoring.net)
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

package kieker.monitoring.core.signaturePattern;

/**
 * @author Bjoern Weissenfels, Jan Waller
 */
public final class SignatureFactory {

	public static final String CPU = "%CPU";

	private SignatureFactory() {
		// private default constructor
	}

	public static String createCPUSignature(final int cpuid) {
		return new StringBuilder(6).append(CPU).append(cpuid).toString();
	}

	/**
	 * Creates a method signature.
	 * 
	 * @param modList
	 *            List of modifiers in the following order:
	 *            1. public, protected, private, package
	 *            2. abstract, non_abstract
	 *            3. static, non_static
	 *            4. final, non_final
	 *            5. synchronized, non_synchronized
	 *            6. native, non_native
	 *            null for any modifier.
	 * @param retType
	 *            primitive type ,fully qualified class name or pattern
	 * @param fqName
	 *            fully qualified class name or pattern
	 * @param method
	 *            method name or pattern
	 * @param params
	 *            list of primitive types, fully qualified class names or pattern
	 * @param exceptions
	 *            list of exceptions or pattern
	 * @return
	 *         A signature which has been generated from the inputs.
	 * @throws InvalidPatternException
	 */
	public static String createMethodSignature(final String[] modList, final String retType,
			final String fqName, final String method, final String[] params, final String[] exceptions) throws InvalidPatternException {
		// TODO: trim() really needed? Maybe this method gets called a lot by manual probes? removed for now!
		final StringBuilder signature = new StringBuilder(512);
		if (modList != null) {
			for (final String element : modList) {
				signature.append(element);
				signature.append(' ');
			}
		}
		if (retType != null) {
			signature.append(retType);
			signature.append(' ');
		} else {
			throw new InvalidPatternException("return type is requiered");
		}
		if (fqName != null) {
			signature.append(fqName);
			signature.append('.');
		} else {
			throw new InvalidPatternException("return type is requiered");
		}
		if (method != null) {
			signature.append(method);
			signature.append('(');
		} else {
			throw new InvalidPatternException("return type is requiered");
		}
		if (params != null) {
			for (final String element : params) {
				signature.append(element);
				signature.append(',');
			}
			signature.deleteCharAt(signature.length() - 1); // TODO: better otherwise
		}
		signature.append(')');
		if (exceptions != null) {
			signature.append(" throws ");
			for (final String element : exceptions) {
				signature.append(element);
				signature.append(',');
			}
			signature.deleteCharAt(signature.length() - 1); // TODO: better otherwise
		}
		return signature.toString();
	}
}
