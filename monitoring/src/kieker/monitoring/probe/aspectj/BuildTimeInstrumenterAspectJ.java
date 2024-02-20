package kieker.monitoring.probe.aspectj;

import java.io.File;
import java.io.IOException;
import java.lang.instrument.IllegalClassFormatException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.nio.file.Path;

import org.aspectj.weaver.loadtime.WeavingURLClassLoader;

import kieker.monitoring.probe.aspectj.operationExecution.OperationExecutionAspectFull;
import kieker.monitoring.probe.javassist.BuildTimeAdaptionUtil;

public class BuildTimeInstrumenterAspectJ {

	public static void main(String[] args) throws IOException, ClassNotFoundException, IllegalClassFormatException {
		File instrumentJar = new File(args[0]);
		URL url = instrumentJar.toURI().toURL();
		URL[] urls = new URL[] { url };

		Path tempDirectory = Files.createTempDirectory("kieker-instrumentation-");
		File tempDir = tempDirectory.toFile();

		System.out.println("Writing to: " + tempDir.getAbsolutePath());

		try (URLClassLoader urlClassLoader = new URLClassLoader(urls)) {

			BuildTimeAdaptionUtil.extractJar(instrumentJar, tempDir);

			String classFileName = "moobench/application/MonitoredClassSimple.class";
			Path classFile = tempDirectory.resolve(classFileName);

			System.out.println("File existing: " + classFile.toFile().getAbsolutePath() + " " + classFile.toFile().exists());

			// byte[] originalClass = IOUtils.toByteArray(urlClassLoader.getResourceAsStream(classFileName));

			Class<?> loadClass = urlClassLoader.loadClass("moobench.application.MonitoredClassSimple");

			URL[] aspectURLs = new URL[] { OperationExecutionAspectFull.class.getProtectionDomain().getCodeSource().getLocation() };
			WeavingURLClassLoader loader = new WeavingURLClassLoader(urls, aspectURLs,
					urlClassLoader);

			loader.loadClass("moobench.application.MonitoredClassSimple");

			// ClassPreProcessorAgentAdapter processor = new ClassPreProcessorAgentAdapter();

			// byte[] processedClass = aspectJ.preProcess("moobench.application.MonitoredClassSimple", originalClass, urlClassLoader, null);
			// byte[] processedClass = processor.transform(urlClassLoader, classFileName, null, loadClass.getProtectionDomain(), new byte[8196]);

			System.out.println("Replacing: " + classFile.toString());

			// Files.write(classFile, processedClass);

			BuildTimeAdaptionUtil.createJar(instrumentJar, tempDir);
		} catch (MalformedURLException e) {
			throw e;
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
