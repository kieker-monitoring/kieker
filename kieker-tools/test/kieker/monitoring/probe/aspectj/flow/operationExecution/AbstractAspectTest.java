/***************************************************************************
 * Copyright 2017 Kieker Project (http://kieker-monitoring.net)
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

import static org.hamcrest.CoreMatchers.is; // NOCS (static import)
import static org.hamcrest.Matchers.hasSize; // NOCS (static import)
import static org.junit.Assert.assertThat; // NOCS (static import)

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import kieker.analysis.AnalysisController;
import kieker.analysis.exception.AnalysisConfigurationException;
import kieker.analysis.plugin.filter.forward.ListCollectionFilter;
import kieker.analysis.plugin.reader.filesystem.AsciiLogReader;
import kieker.common.configuration.Configuration;
import kieker.common.record.IMonitoringRecord;
import kieker.monitoring.writer.filesystem.AsciiFileWriter;

/**
 * An integration test for AspectJ-based probes.
 * 
 * @author Christian Wulf
 *
 * @since 1.13
 */
public class AbstractAspectTest { // NOCS (abstract class)

	public AbstractAspectTest() {} // NOCS NOPMD (empty ctor)

	@Test
	public void testMonitoring() throws Exception {
		final URL resource = this.getClass().getResource("/kieker.monitoring.probe.aspectj.flow.operationExecution");
		final File workingDirectory = new File(resource.toURI());

		final AspectjMonitoringToAsciiFileLog aspectjMonitoring = new AspectjMonitoringToAsciiFileLog("BookstoreApplication.jar",
				"kieker.examples.monitoring.aspectj.BookstoreStarter");
		final int exitValue = aspectjMonitoring.runMonitoring(workingDirectory);

		// check whether monitoring was successful
		assertThat(exitValue, is(0));

		final AspectjAnalysisFromAsciiFileLog aspectjAnalysis = new AspectjAnalysisFromAsciiFileLog();
		final List<IMonitoringRecord> records = aspectjAnalysis.runAnalysis(workingDirectory);

		// -1 because AnalysisController absorbs the KiekerMetadataRecord
		assertThat(records, hasSize(19 - 1));
	}

	private static class AspectjMonitoringToAsciiFileLog {

		private final String javaCommand = "java"; // C:/Program Files/Java/jre7/bin/
		private final String kiekerAspectjFileName = "kieker-1.13-SNAPSHOT-aspectj.jar";
		// BookstoreApplication.jar from /kieker-examples/monitoring/probe-aspectj/build/libs/
		// gradle uses the env. var. JAVA_HOME to build the bookstore example
		private final String appJarFilePath;
		private final String appMainClassName;
		private final List<String> arguments = new ArrayList<>();
		private final List<String> jvmArguments = new ArrayList<>();

		public AspectjMonitoringToAsciiFileLog(final String appJarFilePath, final String appMainClassName) {
			this.appJarFilePath = appJarFilePath;
			this.appMainClassName = appMainClassName;

			this.arguments.add("-javaagent:" + this.kiekerAspectjFileName);
			this.arguments.add("-cp");
			this.arguments.add(".;" + this.appJarFilePath);
			this.arguments.add(this.appMainClassName);
		}

		public void addJmvArgument(String additionalJvmArg) {
			this.jvmArguments.add("-D" + additionalJvmArg);
		}

		public int runMonitoring(final File workingDirectory) throws IOException, InterruptedException {
			this.addJmvArgument(AsciiFileWriter.CONFIG_PATH + "=" + workingDirectory);

			final List<String> commandWithArgs = new ArrayList<>();
			commandWithArgs.add(this.javaCommand);
			commandWithArgs.addAll(this.jvmArguments);
			commandWithArgs.addAll(this.arguments);

			final ProcessBuilder builder = new ProcessBuilder(commandWithArgs)
					.directory(workingDirectory)
					.redirectOutput(new File(workingDirectory, "output.txt"))
					.redirectError(new File(workingDirectory, "error.txt"));
			final Process process = builder.start();

			final int exitValue = process.waitFor();
			return exitValue;
		}

	}

	private static class AspectjAnalysisFromAsciiFileLog {

		public List<IMonitoringRecord> runAnalysis(final File workingDirectory) throws IllegalStateException, AnalysisConfigurationException {
			final AnalysisController analysisController = new AnalysisController();

			final Configuration conf = new Configuration();
			final File[] directories = workingDirectory.listFiles(new FileFilter() {
				@Override
				public boolean accept(final File pathname) {
					return pathname.isDirectory() && !"META-INF".equals(pathname.getName());
				}
			});

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
				for (final File file : logDir.listFiles()) {
					final boolean deleted = file.delete();
					if (!deleted) {
						System.err.println("Could not delete temporary test file: " + file); // NOPMD (sysout)
					}
				}
				final boolean deleted = logDir.delete();
				if (!deleted) {
					System.err.println("Could not delete temporary kieker test log directory: " + logDir); // NOPMD (sysout)
				}
			}
		}
	}
}
