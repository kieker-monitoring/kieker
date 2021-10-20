/***************************************************************************
 * Copyright 2021 Kieker Project (http://kieker-monitoring.net)
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
package kieker.monitoring.probe.aspectj.flow.operationExecution;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.hamcrest.CoreMatchers;
import org.hamcrest.Matchers;
import org.junit.Assert;

import kieker.analysis.AnalysisController;
import kieker.analysis.exception.AnalysisConfigurationException;
import kieker.analysis.plugin.filter.forward.ListCollectionFilter;
import kieker.analysis.plugin.reader.filesystem.AsciiLogReader;
import kieker.common.configuration.Configuration;
import kieker.common.record.IMonitoringRecord;
import kieker.common.util.Version;
import kieker.monitoring.writer.filesystem.FileWriter;

/**
 * An integration test for AspectJ-based probes.
 *
 * @author Christian Wulf
 *
 * @since 1.13
 */
public class AbstractAspectTest { // NOCS NOPMD (abstract class)

	/**
	 * Empty constructor.
	 */
	public AbstractAspectTest() { // NOCS NOPMD (empty ctor)
		// empty constructor.
	}

	// why is this test deactivated?
	// @Test
	// @SuppressFBWarnings(value = "UI_INHERITANCE_UNSAFE_GETRESOURCE", justification = "no problem since we use getResource without package name prefix")
	public void testMonitoring() throws Exception { // NOPMD (rules/java/junit.html#JUnitTestContainsTooManyAsserts)
		final URL resource = this.getClass().getResource("/kieker.monitoring.probe.aspectj.flow.operationExecution");
		final File workingDirectory = new File(resource.toURI());

		final AspectjMonitoringToAsciiFileLog aspectjMonitoring = new AspectjMonitoringToAsciiFileLog(
				"BookstoreApplication.jar", "kieker.examples.monitoring.aspectj.BookstoreStarter");
		final int exitValue = aspectjMonitoring.runMonitoring(workingDirectory);

		// check whether monitoring was successful
		Assert.assertThat(exitValue, CoreMatchers.is(0));

		final AspectjAnalysisFromAsciiFileLog aspectjAnalysis = new AspectjAnalysisFromAsciiFileLog();
		final List<IMonitoringRecord> records = aspectjAnalysis.runAnalysis(workingDirectory);

		// -1 because AnalysisController absorbs the KiekerMetadataRecord
		Assert.assertThat(records, Matchers.hasSize(16 - 1));
	}

	/**
	 * Some logging class.
	 *
	 * @author Christian Wulf
	 *
	 */
	private static class AspectjMonitoringToAsciiFileLog {

		private static final String JAVA_COMMAND = "java"; // C:/Program Files/Java/jre7/bin/
		private static final String KIEKER_ASPECTJ_FILE_NAME = "kieker-" + Version.getVERSION() + "-aspectj.jar";
		// BookstoreApplication.jar from /kieker-examples/monitoring/probe-aspectj/build/libs/
		// gradle uses the env. var. JAVA_HOME to build the bookstore example
		private final String appJarFilePath;
		private final String appMainClassName;
		private final List<String> jvmArguments = new ArrayList<>();
		private final List<String> arguments = new ArrayList<>();

		public AspectjMonitoringToAsciiFileLog(final String appJarFilePath, final String appMainClassName) {
			this.appJarFilePath = appJarFilePath;
			this.appMainClassName = appMainClassName;

			this.arguments.add("-javaagent:" + KIEKER_ASPECTJ_FILE_NAME);
			this.arguments.add("-cp");
			this.arguments.add("." + File.pathSeparator + this.appJarFilePath); // NOPMD
			this.arguments.add(this.appMainClassName);
		}

		public void addJmvArgument(final String additionalJvmArg) {
			this.jvmArguments.add("-D" + additionalJvmArg);
		}

		/**
		 * @param workingDirectory
		 * @return the exit value of the process. By convention, the value 0 indicates normal termination.
		 * @throws IOException
		 * @throws InterruptedException
		 */
		public int runMonitoring(final File workingDirectory) throws IOException, InterruptedException {
			this.addJmvArgument(FileWriter.CONFIG_PATH + "=" + workingDirectory);

			final List<String> commandWithArgs = new ArrayList<>();
			commandWithArgs.add(JAVA_COMMAND);
			commandWithArgs.addAll(this.jvmArguments);
			commandWithArgs.addAll(this.arguments);

			final ProcessBuilder builder = new ProcessBuilder(commandWithArgs).directory(workingDirectory)
					// .redirectOutput(new File(workingDirectory, "output.txt"))
					// .redirectError(new File(workingDirectory, "error.txt"))
					.inheritIO();
			final Process process = builder.start();

			return process.waitFor();
		}

	}

	/**
	 * Filter to filter out directories containing META-INF.
	 *
	 * @author Christian Wulf
	 *
	 */
	private static class NonMetaInfDirectoryFilter implements FileFilter { // NOCS (no ctor)
		@Override
		public boolean accept(final File pathname) {
			return pathname.isDirectory() && !"META-INF".equals(pathname.getName());
		}
	}

	/**
	 * ASCII log checker.
	 *
	 * @author Christian Wulf
	 *
	 */
	private static class AspectjAnalysisFromAsciiFileLog { // NOCS (no ctor)

		private static final FileFilter FILE_FILTER = new NonMetaInfDirectoryFilter();

		public List<IMonitoringRecord> runAnalysis(final File workingDirectory)
				throws IllegalStateException, AnalysisConfigurationException {
			final File[] directories = workingDirectory.listFiles(FILE_FILTER);
			if (null == directories) {
				return Collections.emptyList();
			}

			final Configuration conf = new Configuration();
			final AnalysisController analysisController = new AnalysisController();
			try {
				conf.setProperty(AsciiLogReader.CONFIG_PROPERTY_NAME_INPUTDIRS, Configuration.toProperty(directories));
				final AsciiLogReader reader = new AsciiLogReader(conf, analysisController);

				final ListCollectionFilter<IMonitoringRecord> collectionSink = new ListCollectionFilter<>(
						new Configuration(), analysisController);

				analysisController.connect(reader, AsciiLogReader.OUTPUT_PORT_NAME_RECORDS, collectionSink,
						ListCollectionFilter.INPUT_PORT_NAME);

				analysisController.run();

				return collectionSink.getList();
			} finally {
				this.deleteTempoararyFiles(directories);
			}
		}

		private void deleteTempoararyFiles(final File[] directories) {
			for (final File logDir : directories) {
				final File[] files = logDir.listFiles();
				if (null == files) {
					continue;
				}
				for (final File file : files) {
					final boolean deleted = file.delete();
					if (!deleted) {
						System.err.println("Could not delete temporary test file: " + file); // NOPMD (sysout)
					}
				}
				final boolean deleted = logDir.delete();
				if (!deleted) {
					System.err.println("Could not delete temporary kieker test log directory: " + logDir); // NOPMD
																											// (sysout)
				}
			}
		}
	}
}
