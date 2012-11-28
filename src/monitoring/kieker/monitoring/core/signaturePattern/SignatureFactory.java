package kieker.monitoring.core.signaturePattern;

public class SignatureFactory {

	public static final String CPU = "%CPU";

	public static String createCPUSignature(final int cpuid) {
		return CPU + cpuid;
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
		final StringBuilder signature = new StringBuilder();
		if (modList != null) {
			for (final String element : modList) {
				signature.append(element.trim());
				signature.append(" ");
			}
		}
		if (retType != null) {
			signature.append(retType.trim());
			signature.append(" ");
		} else {
			throw new InvalidPatternException("return type is requiered");
		}
		if (fqName != null) {
			signature.append(fqName.trim());
			signature.append(".");
		} else {
			throw new InvalidPatternException("return type is requiered");
		}
		if (method != null) {
			signature.append(method.trim());
			signature.append("(");
		} else {
			throw new InvalidPatternException("return type is requiered");
		}
		if (params != null) {
			for (final String element : params) {
				signature.append(element.trim());
				signature.append(",");
			}
			signature.deleteCharAt(signature.length() - 1);
		}
		signature.append(")");
		if (exceptions != null) {
			signature.append(" throws ");
			for (final String element : exceptions) {
				signature.append(element.trim());
				signature.append(",");
			}
			signature.deleteCharAt(signature.length() - 1);
		}
		return signature.toString();
	}
}
