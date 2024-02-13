package kieker.monitoring.probe.disl.flow.operationExecution;

import java.util.Arrays;
import java.util.stream.Collectors;

import org.objectweb.asm.Type;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodNode;

import ch.usi.dag.disl.staticcontext.AbstractStaticContext;
import ch.usi.dag.disl.util.JavaNames;

/**
 * Provides static context information for the method being
 * instrumented tailored to Kieker records.
 */
public class KiekerStaticContext extends AbstractStaticContext {

	/**
	 * Returns the fully qualified name of the method and its signature,
	 * including the name of the containing class in the canonical (not
	 * internal) format, i.e., with packages delimited by the '.' character.
	 */
	public String operationSignature() {
		final ClassNode classNode = staticContextData.getClassNode();
		final MethodNode methodNode = staticContextData.getMethodNode();
		final Type methodType = Type.getMethodType(methodNode.desc);

		final String result = String.format("%s %s%s(%s)",
				formatMethodReturnType(methodType),
				__formatClassName(classNode.name),
				__formatMethodName(methodNode.name),
				formatMethodParameters(methodType));

		return result;
	}

	private String __formatClassName(final String className) {
		return JavaNames.internalToType(className);
	}

	private String __formatMethodName(final String methodName) {
		if (JavaNames.isConstructorName(methodName)) {
			return "";
		} else {
			return String.format(".%s", methodName);
		}
	}

	private String formatMethodReturnType(final Type methodType) {
		return __formatType(methodType.getReturnType());
	}

	private String formatMethodParameters(final Type methodType) {
		return Arrays.stream(methodType.getArgumentTypes())
				.map(t -> __formatType(t)).collect(Collectors.joining(","));
	}

	private String __formatType(final Type type) {
		if (type.getSort() != Type.METHOD) {
			return type.getClassName().replace("java.lang.", "");
		} else {
			throw new AssertionError("unexpected sort of type: " + type.getSort());
		}
	}

}
