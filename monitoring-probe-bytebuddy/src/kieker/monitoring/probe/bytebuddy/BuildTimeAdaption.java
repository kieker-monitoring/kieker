package kieker.monitoring.probe.bytebuddy;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Modifier;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.nio.file.Path;

import kieker.monitoring.core.controller.IMonitoringController;
import kieker.monitoring.core.registry.ControlFlowRegistry;
import kieker.monitoring.core.registry.SessionRegistry;
import kieker.monitoring.probe.javassist.BuildTimeAdaptionUtil;
import kieker.monitoring.timer.ITimeSource;

import net.bytebuddy.ByteBuddy;
import net.bytebuddy.asm.Advice;
import net.bytebuddy.dynamic.DynamicType.Unloaded;
import net.bytebuddy.matcher.ElementMatchers;

public class BuildTimeAdaption {
	public static void main(String[] args) throws ClassNotFoundException, IOException {

		File instrumentJar = new File(args[0]);
		URL url = instrumentJar.toURI().toURL();
		URL[] urls = new URL[] { url };

		Path tempDirectory = Files.createTempDirectory("kieker-instrumentation-");
		File tempDir = tempDirectory.toFile();

		try (URLClassLoader urlClassLoader = new URLClassLoader(urls)) {

			BuildTimeAdaptionUtil.extractJar(instrumentJar, tempDir);

			Class<?> classUnderMonitoring = urlClassLoader.loadClass("moobench.application.MonitoredClassSimple");

			Unloaded<?> unloaded = new ByteBuddy().redefine(classUnderMonitoring)
					.defineField("CTRLINST", IMonitoringController.class, Modifier.STATIC | Modifier.FINAL | Modifier.PRIVATE)
					.defineField("TIME", ITimeSource.class, Modifier.STATIC | Modifier.FINAL | Modifier.PRIVATE)
					.defineField("VMNAME", String.class, Modifier.STATIC | Modifier.FINAL | Modifier.PRIVATE)
					.defineField("CFREGISTRY", ControlFlowRegistry.class, Modifier.STATIC | Modifier.FINAL | Modifier.PRIVATE)
					.defineField("SESSIONREGISTRY", SessionRegistry.class, Modifier.STATIC | Modifier.FINAL | Modifier.PRIVATE)
					.visit(Advice.to(OperationExecutionAdvice.class).on(ElementMatchers.isMethod()))
					.make();

			System.out.println(unloaded.getBytes());

			Path classFile = tempDirectory.resolve("moobench/application/MonitoredClassSimple.class");
			Files.write(classFile, unloaded.getBytes());

			BuildTimeAdaptionUtil.createJar(instrumentJar, tempDir);
		} catch (MalformedURLException e) {
			throw e;
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
