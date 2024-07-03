/***************************************************************************
 * Copyright 2024 Kieker Project (http://kieker-monitoring.net)
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
package kieker.monitoring.probe.javassist;

import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtBehavior;
import javassist.CtClass;
import javassist.CtConstructor;
import javassist.CtMethod;
import javassist.NotFoundException;
import javassist.bytecode.AccessFlag;

/**
 * Method instrumenter.
 *
 * @author David G. Reichelt
 */
public class MethodInstrumenter {

	private final ClassPool cp;

	public MethodInstrumenter(final ClassPool pool) {
		super();
		this.cp = pool;
	}

	public void instrumentAllMethods(final CtClass cc) throws NotFoundException, CannotCompileException {
		for (final CtConstructor constructor : cc.getConstructors()) {
			final String signature = this.buildSignature(constructor);
			this.instrumentMethod(constructor, signature);
		}

		for (final CtMethod method : cc.getDeclaredMethods()) {
			final String signature = this.buildSignature(method);
			this.instrumentMethod(method, signature);
		}
	}

	private void instrumentMethod(final CtBehavior method, final String signature) throws NotFoundException, CannotCompileException {
		System.out.println("Signature: " + signature);

		method.addLocalVariable("operationStartData", this.cp.get("kieker.monitoring.probe.disl.flow.operationExecution.FullOperationStartData"));
		method.insertBefore(
				"operationStartData = kieker.monitoring.probe.disl.flow.operationExecution.OperationExecutionDataGatherer.operationStart(\"" + signature + "\");");

		final StringBuilder endBlock = new StringBuilder();
		endBlock.append("if (operationStartData != null) {"
				+ "   kieker.monitoring.probe.disl.flow.operationExecution.OperationExecutionDataGatherer.operationEnd(operationStartData);"
				+ "} ");

		method.insertAfter(endBlock.toString());
	}

	private String buildSignature(final CtConstructor method) {
		final StringBuilder builder = new StringBuilder();
		if (AccessFlag.isPublic(method.getModifiers())) {
			builder.append("public ");
		}
		if (AccessFlag.isProtected(method.getModifiers())) {
			builder.append("protected ");
		}
		if (AccessFlag.isPrivate(method.getModifiers())) {
			builder.append("private ");
		}
		if ((method.getModifiers() & AccessFlag.STATIC) != 0) {
			builder.append("static ");
		}
		if ((method.getModifiers() & AccessFlag.FINAL) != 0) {
			builder.append("final ");
		}
		if ((method.getModifiers() & AccessFlag.SYNCHRONIZED) != 0) {
			builder.append("synchronized ");
		}
		if ((method.getModifiers() & AccessFlag.NATIVE) != 0) {
			builder.append("native ");
		}
		if ((method.getModifiers() & AccessFlag.STRICT) != 0) {
			builder.append("strictfp ");
		}
		builder.append(method.getLongName());
		return builder.toString();
	}

	private String buildSignature(final CtMethod method) throws NotFoundException {
		final StringBuilder builder = new StringBuilder();
		if (AccessFlag.isPublic(method.getModifiers())) {
			builder.append("public ");
		}
		if (AccessFlag.isProtected(method.getModifiers())) {
			builder.append("protected ");
		}
		if (AccessFlag.isPrivate(method.getModifiers())) {
			builder.append("private ");
		}
		if ((method.getModifiers() & AccessFlag.STATIC) != 0) {
			builder.append("static ");
		}
		if ((method.getModifiers() & AccessFlag.FINAL) != 0) {
			builder.append("final ");
		}
		if ((method.getModifiers() & AccessFlag.SYNCHRONIZED) != 0) {
			builder.append("synchronized ");
		}
		if ((method.getModifiers() & AccessFlag.NATIVE) != 0) {
			builder.append("native ");
		}
		if ((method.getModifiers() & AccessFlag.STRICT) != 0) {
			builder.append("strictfp ");
		}
		builder.append(method.getReturnType().getName());
		builder.append(" ");
		builder.append(method.getLongName());
		return builder.toString();
	}
}
