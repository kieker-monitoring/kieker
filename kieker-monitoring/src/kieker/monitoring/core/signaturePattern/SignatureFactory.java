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

package kieker.monitoring.core.signaturePattern;

/**
 * This class can be used to create signatures (for example a string containing the whole signature of a method).
 *
 * @author Bjoern Weissenfels, Jan Waller
 *
 * @since 1.7
 */
public final class SignatureFactory {

	/** This constant contains the necessary prefix for patterns. */
	public static final char PATTERN_PREFIX = '%';
	/** This constant contains the colons, which are used to separate the elements in the signature. */
	public static final String COLONS = "::";

	// Note: for static final String it is better to use "+" to help the compiler to resolve them at compile time!

	/**
	 * Prefix of a cpu signature.
	 */
	public static final String PATTERN_PREFIX_CPU = PATTERN_PREFIX + "CPU";

	/**
	 * Prefix of a memory signature.
	 */
	public static final String PATTERN_PREFIX_MEM_SWAP = PATTERN_PREFIX + "MEM_SWAP";

	/**
	 * Prefix of a load average signature.
	 */
	public static final String PATTERN_PREFIX_LOAD_AVERAGE = PATTERN_PREFIX + "LOAD_AVERAGE";

	/**
	 * Prefix of a network utilization signature.
	 */
	public static final String PATTERN_PREFIX_NETWORK_UTILIZATION = PATTERN_PREFIX + "NETWORK_UTILIZATION";

	/**
	 * Prefix of a JVM memory signature.
	 */
	public static final String PATTERN_PREFIX_JVM_MEM = PATTERN_PREFIX + "JVM_MEM";

	/**
	 * Prefix of a JVM class loading signature.
	 */
	public static final String PATTERN_PREFIX_JVM_CL = PATTERN_PREFIX + "JVM_CL";

	/**
	 * Prefix of a JVM uptime signature.
	 */
	public static final String PATTERN_PREFIX_JVM_UP_TIME = PATTERN_PREFIX + "JVM_UPTIME";

	/**
	 * Prefix of a JVM threads signature.
	 */
	public static final String PATTERN_PREFIX_JVM_THREADS = PATTERN_PREFIX + "JVM_THREADS";

	/**
	 * Prefix of a JVM compilation signature.
	 */
	public static final String PATTERN_PREFIX_JVM_COMPILATION = PATTERN_PREFIX + "JVM_COMPILATION";

	/**
	 * Prefix of a JVM garbage collector signature.
	 */
	public static final String PATTERN_PREFIX_JVM_GC = PATTERN_PREFIX + "JVM_GC";

	/**
	 * Private constructor to avoid instantiation.
	 */
	private SignatureFactory() {
		// private default constructor
	}

	/**
	 * Creates a cpu signature with a given cpu id.
	 *
	 * @param cpuid
	 *            The id of the cpu.
	 * @return
	 *         A signature for the cpu.
	 */
	public static String createCPUSignature(final int cpuid) {
		return new StringBuilder(8)
				.append(PATTERN_PREFIX_CPU) // 4
				.append(COLONS) // 2
				.append(cpuid) // 2(?)
				.toString();
	}

	/**
	 * Creates a CPU signature.
	 *
	 * @return The signature.
	 */
	public static String createCPUSignature() {
		return PATTERN_PREFIX_CPU;
	}

	/**
	 * Creates a mem swap signature.
	 *
	 * @return The signature.
	 */
	public static String createMemSwapSignature() {
		return PATTERN_PREFIX_MEM_SWAP;
	}

	/**
	 * Creates a load average signature.
	 *
	 * @return The signature.
	 */
	public static String createLoadAverageSignature() {
		return PATTERN_PREFIX_LOAD_AVERAGE;
	}

	/**
	 * Creates a network utilization signature.
	 *
	 * @return The signature.
	 */
	public static String createNetworkUtilizationSignature() {
		return PATTERN_PREFIX_NETWORK_UTILIZATION;
	}

	/**
	 * Creates a JVM Mem signature.
	 *
	 * @return The signature.
	 */
	public static String createJVMMemSignature() {
		return PATTERN_PREFIX_JVM_MEM;
	}

	/**
	 * Creates a JVM class loading signature.
	 *
	 * @return The signature.
	 */
	public static String createJVMClassLoadSignature() {
		return PATTERN_PREFIX_JVM_CL;
	}

	/**
	 * Creates a JVM uptime signature.
	 *
	 * @return The signature.
	 */
	public static String createJVMUpTimeSignature() {
		return PATTERN_PREFIX_JVM_UP_TIME;
	}

	/**
	 * Creates a JVM threads signature.
	 *
	 * @return The signature.
	 */
	public static String createJVMThreadsSignature() {
		return PATTERN_PREFIX_JVM_THREADS;
	}

	/**
	 * Creates a JVM compilation signature.
	 *
	 * @return The signature.
	 */
	public static String createJVMCompilationSignature() {
		return PATTERN_PREFIX_JVM_COMPILATION;
	}

	/**
	 * Creates a JVM garbage collector signature.
	 *
	 * @return The signature.
	 */
	public static String createJVMGarbageCollectorSignature() {
		return PATTERN_PREFIX_JVM_GC;
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
	 *            One or none of each sub-point is allowed.
	 *            Null or empty list stands for any modifiers.
	 * @param retType
	 *            Primitive type ,fully qualified class name or pattern.
	 * @param fqName
	 *            Fully qualified class name or pattern.
	 * @param method
	 *            Method name or pattern.
	 * @param params
	 *            List of primitive types, fully qualified class names or pattern.
	 *            Null or empty list, if no parameters are required.
	 * @param exceptions
	 *            List of exceptions or pattern.
	 *            Null or empty list, if no exceptions are required.
	 * @return
	 *         A signature which has been generated from the inputs.
	 *
	 * @throws InvalidPatternException
	 *             If any of the mandatory parameters is null.
	 */
	public static String createMethodSignature(final String[] modList, final String retType,
			final String fqName, final String method, final String[] params, final String[] exceptions) throws InvalidPatternException {
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
			throw new InvalidPatternException("fully qualified name is requiered");
		}
		if (method != null) {
			signature.append(method);
			signature.append('(');
		} else {
			throw new InvalidPatternException("method name is requiered");
		}
		if (params != null) {
			signature.append(params[0]);
			for (int i = 1; i < params.length; i++) {
				signature.append(',');
				signature.append(params[i]);
			}
		}
		signature.append(')');
		if (exceptions != null) {
			signature.append(" throws ");
			signature.append(exceptions[0]);
			for (int i = 1; i < exceptions.length; i++) {
				signature.append(',');
				signature.append(exceptions[i]);
			}
		}
		return signature.toString();
	}
}
