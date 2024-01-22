package kieker.monitoring.probe.javassist;

import java.io.File;
import java.io.IOException;
import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.security.ProtectionDomain;
import java.util.List;

import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.NotFoundException;
import javassist.bytecode.AccessFlag;
import kieker.monitoring.util.KiekerPattern;

public class KiekerClassTransformer implements ClassFileTransformer {
	
	private final List<KiekerPattern> patternObjects;

	public KiekerClassTransformer(List<KiekerPattern> patternObjects) {
		this.patternObjects = patternObjects;
	}

	@Override
	public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined,
			ProtectionDomain protectionDomain, byte[] classfileBuffer) throws IllegalClassFormatException {
		String realClassName = className.replaceAll(File.separator, ".");
		for (KiekerPattern pattern : patternObjects) {
			if (realClassName.equals(pattern.getOnlyClass())) {
				System.out.println("Instrumenting: " + realClassName);
				ClassPool cp = ClassPool.getDefault();
				try {
					CtClass cc = cp.get(realClassName);
					if (!cc.isInterface()) {
						// TODO: Interfaces would also require instrumentation of default method; this is left out for now because of the experimental structure
						
						for (CtMethod method : cc.getDeclaredMethods()) {
							instrumentMethod(cp, method);
						}
						
						byte[] byteCode = cc.toBytecode();
		                cc.detach();
						
						return byteCode;
					}
				} catch (NotFoundException | CannotCompileException | IOException e) {
					e.printStackTrace();
				}
			}
		}
		return null;
	}

	private void instrumentMethod(ClassPool cp, CtMethod method) throws NotFoundException, CannotCompileException {
		String signature = buildSignature(method);
		System.out.println("Signature: " + signature);
		
		method.addLocalVariable("operationStartData", cp.get("kieker.monitoring.probe.disl.flow.operationExecution.DiSLOperationStartData"));
		method.insertBefore("operationStartData = kieker.monitoring.probe.javassist.KiekerJavassistAnalysis.operationStart(\"" + signature + "\");");
		
		StringBuilder endBlock = new StringBuilder();
		endBlock.append("if (operationStartData != null) {"
				+ "   kieker.monitoring.probe.javassist.KiekerJavassistAnalysis.operationEnd(operationStartData);"
				+ "} ");
		
		method.insertAfter(endBlock.toString());
	}

	private String buildSignature(CtMethod method) throws NotFoundException {
		StringBuilder builder = new StringBuilder();
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
