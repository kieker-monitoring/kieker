/**
 *
 */
package kieker.analysisteetime;

import java.io.File;

import teetime.framework.Execution;

/**
 * Class that executes the {@link ExampleConfiguration}. This is just for testing the current development.
 *
 * @author Sören Henning
 */
public final class ExampleConfigExecution {

	public static void main(final String[] args) {

		// TODO Temp
		final File importDirectory = new File("kieker-examples/userguide/ch5--trace-monitoring-aspectj/testdata/kieker-20141008-101258768-UTC");

		final ExampleConfiguration configuration = new ExampleConfiguration(importDirectory);
		final Execution<ExampleConfiguration> analysis = new Execution<>(configuration);
		analysis.executeBlocking();

	}

}
