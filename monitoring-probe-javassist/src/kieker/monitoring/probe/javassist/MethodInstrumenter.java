package kieker.monitoring.probe.javassist;

import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtConstructor;
import javassist.CtMethod;
import javassist.NotFoundException;
import javassist.bytecode.AccessFlag;

public class MethodInstrumenter {
	
	private final ClassPool cp;
	
	
	
	public MethodInstrumenter(ClassPool pool) {
		super();
		this.cp = pool;
	}
	
	public void instrumentAllMethods(CtClass cc) throws NotFoundException, CannotCompileException {
		for (CtMethod method : cc.getDeclaredMethods()) {
			instrumentMethod(method);
		}
	}

	private void instrumentMethod(CtMethod method) throws NotFoundException, CannotCompileException {
		String signature = buildSignature(method);
		System.out.println("Signature: " + signature);
		
		method.addLocalVariable("operationStartData", cp.get("kieker.monitoring.probe.disl.flow.operationExecution.FullOperationStartData"));
		method.insertBefore("operationStartData = kieker.monitoring.probe.disl.flow.operationExecution.KiekerMonitoringAnalysis.operationStart(\"" + signature + "\");");
		
		StringBuilder endBlock = new StringBuilder();
		endBlock.append("if (operationStartData != null) {"
				+ "   kieker.monitoring.probe.disl.flow.operationExecution.KiekerMonitoringAnalysis.operationEnd(operationStartData);"
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
