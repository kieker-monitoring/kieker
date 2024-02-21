package kieker.monitoring.probe.aspectj;

import java.io.File;
import java.io.IOException;
import java.lang.instrument.IllegalClassFormatException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.aspectj.bridge.IMessage;
import org.aspectj.bridge.MessageHandler;
import org.aspectj.tools.ajc.Main;

import kieker.monitoring.probe.javassist.BuildTimeAdaptionUtil;

public class BuildTimeInstrumenterAspectJ {

	private static final List<String> CLASSES_TO_INSTRUMENT = Arrays.asList(new String[] { "moobench.application.MonitoredClassSimple" });

	public static void main(String[] args) throws IOException, ClassNotFoundException, IllegalClassFormatException, InterruptedException {
		File instrumentJar = new File(args[0]);
		String aopXmlFile = args[1];
		if (!instrumentJar.exists()) {
			throw new RuntimeException("Jar to instrument should exist: " + args[0]);
		}

		Path currentTempDirectory = Files.createTempDirectory("kieker-instrumentation-");
		File instrumentableDir = buildInstrumentableDirectory(instrumentJar, currentTempDirectory);

		Main compiler = new Main();

		String instrumentedTempJarName = instrumentJar.getParent() + File.separator + "instrumented.jar";
		String aspectPath = System.getProperty("user.home") + "/.m2/repository/net/kieker-monitoring/kieker/2.0.0-SNAPSHOT/kieker-2.0.0-SNAPSHOT-aspectj.jar";
		String[] ajcArguments = new String[] { "-aspectpath",
			aspectPath,
			"-xmlConfigured",
			aopXmlFile,
			"-inpath",
			instrumentableDir.getAbsolutePath(),
			"-outjar",
			instrumentedTempJarName
		};

		MessageHandler m = new MessageHandler();
		compiler.run(ajcArguments, m);
		IMessage[] ms = m.getMessages(null, true);
		System.out.println("messages: " + Arrays.asList(ms));

		File instrumentedJarDirectory = new File(currentTempDirectory.toFile(), "instrumented");
		instrumentedJarDirectory.mkdir();
		BuildTimeAdaptionUtil.extractJar(new File(instrumentedTempJarName), instrumentedJarDirectory);

		File instrumentedAndMergedJarDirectory = new File(currentTempDirectory.toFile(), "instrumentedAndMerged");
		instrumentedAndMergedJarDirectory.mkdir();
		BuildTimeAdaptionUtil.extractJar(instrumentJar, instrumentedAndMergedJarDirectory);

		for (String classname : CLASSES_TO_INSTRUMENT) {
			String classFileName = classname.replace('.', File.separatorChar) + ".class";
			File instrumentedClassFile = new File(instrumentedJarDirectory, classFileName);
			File instrumentedJarClassFile = new File(instrumentedAndMergedJarDirectory, classFileName);
			instrumentedClassFile.renameTo(instrumentedJarClassFile);
			System.out.println("Moving " + instrumentedClassFile + " to " + instrumentedJarClassFile);
		}

		BuildTimeAdaptionUtil.createJar(instrumentJar, instrumentedAndMergedJarDirectory);

	}

	private static File buildInstrumentableDirectory(File instrumentJar, Path currentTempDirectory) throws IOException {
		File instrumentDir = new File(currentTempDirectory.toFile(), "instrumentable");
		instrumentDir.mkdir();

		BuildTimeAdaptionUtil.extractJar(instrumentJar, instrumentDir);

		for (File classFile : FileUtils.listFiles(instrumentDir, null, true)) {
			String classFileName = classFile.toString();
			if (classFileName.endsWith(".class")) {
				String className = classFileName.substring(instrumentDir.toString().length() + 1, classFileName.length() - ".class".length())
						.replace(File.separatorChar, '.');
				if (!CLASSES_TO_INSTRUMENT.contains(className)) {
					classFile.delete();
				}
			}
		}
		return instrumentDir;
	}
}
