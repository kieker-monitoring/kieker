package kieker.monitoring.probe.aspectj.flow.operationExecution;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

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

// integration test
public class AbstractAspectTest {

	@Test
	public void testMonitoring() throws Exception {
		final String javaCommand = "java"; // C:/Program Files/Java/jre7/bin/
		final String kiekerAspectjFileName = "kieker-1.13-SNAPSHOT-aspectj.jar";
		// BookstoreApplication.jar from /kieker-examples/monitoring/probe-aspectj/build/libs/
		// gradle uses the env. var. JAVA_HOME to build the bookstore example
		final String appJarFilePath = "BookstoreApplication.jar";
		final String mainClassName = "kieker.examples.monitoring.aspectj.BookstoreStarter";
		final String[] commandWithArgs = { javaCommand, "-javaagent:" + kiekerAspectjFileName, "-cp", ".;" + appJarFilePath, mainClassName };
		final String workingDirectory = "test-resources/kieker.monitoring.probe.aspectj.flow.operationExecution";

		final ProcessBuilder builder = new ProcessBuilder(commandWithArgs)
				.directory(new File(workingDirectory))
				.redirectOutput(new File("output.txt"))
				.redirectError(new File("error.txt"));
		final Process process = builder.start();
		// do something in between
		final int exitValue = process.waitFor();

		// check whether monitoring was successful
		assertThat(exitValue, is(0));

		this.assertEvents(workingDirectory);
	}

	private void assertEvents(String workingDirectory) throws IllegalStateException, AnalysisConfigurationException, IOException {
		final AnalysisController analysisController = new AnalysisController();

		final Configuration conf = new Configuration();
		final File[] directories = new File(workingDirectory).listFiles(new FileFilter() {
			@Override
			public boolean accept(File pathname) {
				return pathname.isDirectory() && !pathname.getName().equals("META-INF");
			}
		});

		try {
			conf.setProperty(AsciiLogReader.CONFIG_PROPERTY_NAME_INPUTDIRS, Configuration.toProperty(directories));
			final AsciiLogReader reader = new AsciiLogReader(conf, analysisController);

			final ListCollectionFilter<IMonitoringRecord> collectionSink = new ListCollectionFilter<>(new Configuration(), analysisController);

			analysisController.connect(reader, AsciiLogReader.OUTPUT_PORT_NAME_RECORDS, collectionSink, ListCollectionFilter.INPUT_PORT_NAME);

			analysisController.run();

			assertThat(collectionSink.getList().size(), is(19 - 1)); // -1 because AnalysisController absorbs the KiekerMetadataRecord
		} finally {
			this.deleteTempoararyFiles(directories);
		}
	}

	private void deleteTempoararyFiles(final File[] directories) {
		for (final File logDir : directories) {
			for (final File file : logDir.listFiles()) {
				final boolean deleted = file.delete();
				if (!deleted) {
					System.err.println("Could not delete temporary test file: " + file);
				}
			}
			final boolean deleted = logDir.delete();
			if (!deleted) {
				System.err.println("Could not delete temporary kieker test log directory: " + logDir);
			}
		}
	}
}
