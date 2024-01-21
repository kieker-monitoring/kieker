package kieker.monitoring.probe.javassist;

import java.io.File;
import java.io.IOException;
import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.lang.reflect.Modifier;
import java.security.ProtectionDomain;
import java.util.List;

import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtField;
import javassist.CtMethod;
import javassist.NotFoundException;
import javassist.bytecode.AccessFlag;
import javassist.bytecode.LineNumberAttribute;
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
						addStaticFields(cp, cc);
						
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
		
		method.addLocalVariable("operationSignature", cp.get("java.lang.String"));
		method.insertBefore("operationSignature = \"" + signature + "\";");
		
		method.addLocalVariable("traceId", CtClass.longType);

		method.addLocalVariable("entrypoint", CtClass.booleanType);
		method.addLocalVariable("eoi", CtClass.intType);
		method.addLocalVariable("ess", CtClass.intType);
		
		method.insertBefore("   traceId = CFREGISTRY.recallThreadLocalTraceId();"
				+ "  if (traceId == -1) {\n"
				+ "			entrypoint = true;\n"
				+ "			traceId = CFREGISTRY.getAndStoreUniqueThreadLocalTraceId();\n"
				+ "			CFREGISTRY.storeThreadLocalEOI(0);\n"
				+ "			CFREGISTRY.storeThreadLocalESS(1); // next operation is ess + 1\n"
				+ "			eoi = 0;\n"
				+ "			ess = 0;\n"
				+ "		} else {\n"
				+ "			entrypoint = false;\n"
				+ "			eoi = CFREGISTRY.incrementAndRecallThreadLocalEOI(); // ess > 1\n"
				+ "			ess = CFREGISTRY.recallAndIncrementThreadLocalESS(); // ess >= 0\n"
				+ "			if ((eoi == -1) || (ess == -1)) {\n"
				+ "				System.out.println(\"eoi and/or ess have invalid values: eoi == \"+eoi+\" ess == \"+ess);\n"
				+ "				CTRLINST.terminateMonitoring();\n"
				+ "			}\n"
				+ "		}");
		
		method.addLocalVariable("tin", CtClass.longType);
		method.insertBefore("tin = TIME.getTime();");
		
		StringBuilder endBlock = new StringBuilder();
		
		endBlock.append("if (CTRLINST.isMonitoringEnabled() && CTRLINST.isProbeActivated(operationSignature)) {"
				+ "   long tout = TIME.getTime();"
				+ "   java.lang.String sessionId = SESSIONREGISTRY.recallThreadLocalSessionId();"
				+ "   CTRLINST.newMonitoringRecord(\n"
				+ "				new kieker.common.record.controlflow.OperationExecutionRecord(operationSignature, sessionId,\n"
				+ "						traceId, tin, tout, VMNAME, eoi, ess));"
				+ "} ");
		
		method.insertAfter(endBlock.toString());
	}

	private void addStaticFields(ClassPool cp, CtClass cc) throws CannotCompileException, NotFoundException {
		CtField controller = new CtField(cp.get("kieker.monitoring.core.controller.IMonitoringController"), "CTRLINST", cc);
		controller.setModifiers(Modifier.STATIC | Modifier.FINAL | Modifier.PRIVATE);
		cc.addField(controller, CtField.Initializer.byExpr("kieker.monitoring.core.controller.MonitoringController.getInstance()"));
		
		CtField time = new CtField(cp.get("kieker.monitoring.timer.ITimeSource"), "TIME", cc);
		time.setModifiers(Modifier.STATIC | Modifier.FINAL | Modifier.PRIVATE);
		cc.addField(time, CtField.Initializer.byExpr("CTRLINST.getTimeSource()"));
		
		CtField vmname = new CtField(cp.get("java.lang.String"), "VMNAME", cc);
		vmname.setModifiers(Modifier.STATIC | Modifier.FINAL | Modifier.PRIVATE);
		cc.addField(vmname, "CTRLINST.getHostname()");
		
		CtField cfregistry = new CtField(cp.get("kieker.monitoring.core.registry.ControlFlowRegistry"), "CFREGISTRY", cc);
		cfregistry.setModifiers(Modifier.STATIC | Modifier.FINAL | Modifier.PRIVATE);
		cc.addField(cfregistry, "kieker.monitoring.core.registry.ControlFlowRegistry.INSTANCE");
		
		CtField sessionRegistry = new CtField(cp.get("kieker.monitoring.core.registry.SessionRegistry"), "SESSIONREGISTRY", cc);
		sessionRegistry.setModifiers(Modifier.STATIC | Modifier.FINAL | Modifier.PRIVATE);
		cc.addField(sessionRegistry, "kieker.monitoring.core.registry.SessionRegistry.INSTANCE");
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
