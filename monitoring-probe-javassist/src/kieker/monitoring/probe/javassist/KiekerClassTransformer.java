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
						
						MethodInstrumenter instrumenter = new MethodInstrumenter(cp);
						instrumenter.instrumentAllMethods(cc);
						
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


}
