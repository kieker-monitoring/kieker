package kieker.monitoring.probe.javassist;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.NotFoundException;

/**
 * Prototypical instrumentation of a class - for now, only works for MonitoredClassSimple, but 
 * could be extended for others if this yields significant performance gain. 
 */
public class BuildTimeAdaption {
 
	public static void main(String[] args) throws NotFoundException, CannotCompileException, IOException {
		File instrumentJar = new File(args[0]);
		
		Path tempDirectory = Files.createTempDirectory("kieker-instrumentation-");
		File tempDir = tempDirectory.toFile();
		
		System.out.println("Writing to: " + tempDir.getAbsolutePath());
		
		ProcessBuilder builder = new ProcessBuilder(new String[] {"jar", "xf", instrumentJar.getAbsolutePath() });
		builder.directory(tempDir);
		
		builder.start();
		
		ClassPool pool = ClassPool.getDefault();
		pool.insertClassPath(instrumentJar.getAbsolutePath());
		
		String className = "moobench.application.MonitoredClassSimple";
		instrumentClass(tempDir, pool, className);
	}

	private static void instrumentClass(File tempDir, ClassPool pool, String className)
			throws NotFoundException, CannotCompileException, IOException {
		CtClass ctClass = pool.get(className);
		
		MethodInstrumenter instrumenter = new MethodInstrumenter(pool);
		instrumenter.instrumentAllMethods(ctClass);
		
		ctClass.writeFile(tempDir.getAbsolutePath());
		ctClass.detach();
	}
}