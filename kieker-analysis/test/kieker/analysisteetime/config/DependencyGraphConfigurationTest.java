package kieker.analysisteetime.config;

import java.io.File;
import java.net.URL;
import java.time.temporal.ChronoUnit;

import org.junit.Test;

import kieker.analysisteetime.ExampleConfigurationTest;

import teetime.framework.Execution;

public class DependencyGraphConfigurationTest {

	@Test
	public void testWithLargeInputLog() throws Exception {
		// from within Eclipse:
		// . = <absolute path>/kieker/kieker-analysis/build-eclipse/kieker/analysisteetime/
		// / = <absolute path>/kieker/kieker-analysis/build-eclipse/
		final URL projectDir = ExampleConfigurationTest.class.getResource("/.");
		final File importDirectory = new File(projectDir.getFile(), "kieker-20170805-132418-9229368724068-UTC--KIEKER");
		final File exportDirectory = new File(projectDir.getFile());

		final DependencyGraphConfiguration configuration = new DependencyGraphConfiguration(importDirectory, ChronoUnit.NANOS, exportDirectory);
		final Execution<DependencyGraphConfiguration> execution = new Execution<>(configuration);
		execution.executeBlocking();
	}
}
