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
	 * 
	 * @return Fully qualified name of the method and its signature
	 */
	public String operationSignature() {
		final ClassNode classNode = staticContextData.getClassNode();
		final MethodNode methodNode = staticContextData.getMethodNode();
		final Type methodType = Type.getMethodType(methodNode.desc);

		final String result = String.format("%s %s%s(%s)",
				formatMethodReturnType(methodType),
				formatClassName(classNode.name),
				formatMethodName(methodNode.name),
				formatMethodParameters(methodType));

		return result;
	}

	private String formatClassName(final String className) {
		return JavaNames.internalToType(className);
	}

	private String formatMethodName(final String methodName) {
		if (JavaNames.isConstructorName(methodName)) {
			return "";
		} else {
			return String.format(".%s", methodName);
		}
	}

	private String formatMethodReturnType(final Type methodType) {
		return formatASMType(methodType.getReturnType());
	}

	private String formatMethodParameters(final Type methodType) {
		return Arrays.stream(methodType.getArgumentTypes())
				.map(t -> formatASMType(t)).collect(Collectors.joining(","));
	}

	private String formatASMType(final Type type) {
		if (type.getSort() != Type.METHOD) {
			return type.getClassName().replace("java.lang.", "");
		} else {
			throw new AssertionError("unexpected sort of type: " + type.getSort());
		}
	}

}
