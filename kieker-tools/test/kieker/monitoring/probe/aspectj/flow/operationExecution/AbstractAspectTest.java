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

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;

import org.junit.Test;

import kieker.analysis.AnalysisController;
import kieker.analysis.exception.AnalysisConfigurationException;
import kieker.analysis.plugin.filter.forward.ListCollectionFilter;
import kieker.analysis.plugin.reader.filesystem.AsciiLogReader;
import kieker.common.configuration.Configuration;
import kieker.common.record.IMonitoringRecord;

import static org.hamcrest.CoreMatchers.is; // NOCS (static import)
import static org.junit.Assert.assertThat; // NOCS (static import)

/**
 * An integration test for AspectJ-based probes.
 * 
 * @author Christian Wulf
 *
 * @since 1.13
 */
public class AbstractAspectTest {	// NOCS (abstract class)

	public AbstractAspectTest() {}	// NOCS NOPMD (empty ctor)

	@Test
	public void testMonitoring() throws Exception {
		final String javaCommand = "java"; // C:/Program Files/Java/jre7/bin/
		final String kiekerAspectjFileName = "kieker-1.13-SNAPSHOT-aspectj.jar";
		// BookstoreApplication.jar from /kieker-examples/monitoring/probe-aspectj/build/libs/
		// gradle uses the env. var. JAVA_HOME to build the bookstore example
		final String appJarFilePath = "BookstoreApplication.jar";
		final String mainClassName = "kieker.examples.monitoring.aspectj.BookstoreStarter";
		final String[] commandWithArgs = { javaCommand, "-javaagent:" + kiekerAspectjFileName, "-cp",
				".;" + appJarFilePath, mainClassName, };
		final String workingDirectory = "test-resources/kieker.monitoring.probe.aspectj.flow.operationExecution";

		final ProcessBuilder builder = new ProcessBuilder(commandWithArgs).directory(new File(workingDirectory))
				.redirectOutput(new File("output.txt")).redirectError(new File("error.txt"));
		final Process process = builder.start();
		// do something in between
		final int exitValue = process.waitFor();

		// check whether monitoring was successful
		assertThat(exitValue, is(0));

		this.assertEvents(workingDirectory);
	}

	private void assertEvents(final String workingDirectory)
			throws IllegalStateException, AnalysisConfigurationException, IOException {
		final AnalysisController analysisController = new AnalysisController();

		final Configuration conf = new Configuration();
		final File[] directories = new File(workingDirectory).listFiles(new FileFilter() {
			@Override
			public boolean accept(final File pathname) {
				return pathname.isDirectory() && !"META-INF".equals(pathname.getName());
			}
		});

		// try {
		conf.setProperty(AsciiLogReader.CONFIG_PROPERTY_NAME_INPUTDIRS, Configuration.toProperty(directories));
		final AsciiLogReader reader = new AsciiLogReader(conf, analysisController);

		final ListCollectionFilter<IMonitoringRecord> collectionSink = new ListCollectionFilter<>(new Configuration(),
				analysisController);

		analysisController.connect(reader, AsciiLogReader.OUTPUT_PORT_NAME_RECORDS, collectionSink,
				ListCollectionFilter.INPUT_PORT_NAME);

		analysisController.run();

		assertThat(collectionSink.getList().size(), is(19 - 1)); // -1 because AnalysisController absorbs the
																	// KiekerMetadataRecord
		// } finally {
		// this.deleteTempoararyFiles(directories);
		// }
	}

	// private void deleteTempoararyFiles(final File[] directories) {
	// for (final File logDir : directories) {
	// for (final File file : logDir.listFiles()) {
	// final boolean deleted = file.delete();
	// if (!deleted) {
	// System.err.println("Could not delete temporary test file: " + file); // NOPMD (sysout)
	// }
	// }
	// final boolean deleted = logDir.delete();
	// if (!deleted) {
	// System.err.println("Could not delete temporary kieker test log directory: " + logDir); // NOPMD (sysout)
	// }
	// }
	// }
}
