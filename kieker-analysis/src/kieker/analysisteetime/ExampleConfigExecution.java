/**
 *
 */
package kieker.analysisteetime;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;

import kieker.analysisteetime.model.analysismodel.deployment.DeploymentModel;

import teetime.framework.Execution;

/**
 * Class that executes the {@link ExampleConfiguration}. This is just for testing the current development.
 *
 * @author Sören Henning
 */
public final class ExampleConfigExecution {

	public static void main(final String[] args) {

		// TODO Temp
		// final File importDirectory = new File("C:/Users/Soeren/Desktop/SoftArchKram/jedit-records/kieker-20170115-163405515-UTC-Leonard-KIEKER");
		final File importDirectory = new File("kieker-examples/userguide/ch5--trace-monitoring-aspectj/testdata/kieker-20141008-101258768-UTC");
		final File exportDirectory = new File("C:/Users/Soeren/Desktop/kieker-output");

		final ExampleConfiguration configuration = new ExampleConfiguration(importDirectory, exportDirectory);
		final Execution<ExampleConfiguration> analysis = new Execution<>(configuration);
		analysis.executeBlocking();

		try {
			final DeploymentModel deploymentModel = configuration.getDeploymentModel();
			final DeploymentModelPrinter deploymentModelPrinter = new DeploymentModelPrinter(new PrintStream(new File("output.txt")));
			// final DeploymentModelPrinter deploymentModelPrinter = new DeploymentModelPrinter(System.out);
			deploymentModelPrinter.print(deploymentModel);
		} catch (final FileNotFoundException e) {
			// Just for testing
		}

	}

}
