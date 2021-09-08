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

package kieker.analysis.stage.model;

import java.io.PrintStream;

import kieker.model.analysismodel.deployment.DeployedComponent;
import kieker.model.analysismodel.deployment.DeployedOperation;
import kieker.model.analysismodel.deployment.DeploymentContext;
import kieker.model.analysismodel.deployment.DeploymentModel;

/**
 * @author Sören Henning
 *
 * @since 1.14
 */
public class DeploymentModelPrinter {

	private static final String INSERTION = "    ";

	private final PrintStream printStream;

	public DeploymentModelPrinter(final PrintStream printStream) {
		this.printStream = printStream;
	}

	public void print(final DeploymentModel model) {
		for (final DeploymentContext deploymentContext : model.getDeploymentContexts().values()) {
			this.printDeploymentContext(deploymentContext);
			for (final DeployedComponent component : deploymentContext.getComponents().values()) {
				this.printComponent(component);
				for (final DeployedOperation operation : component.getContainedOperations().values()) {
					this.printOperation(operation);
				}
			}
		}
	}

	private void printDeploymentContext(final DeploymentContext deploymentContext) {
		final String name = deploymentContext.getName();
		this.printStream.println(name);
	}

	private void printComponent(final DeployedComponent component) {
		final String name = component.getAssemblyComponent().getComponentType().getSignature();
		this.printStream.println(INSERTION + name);
	}

	private void printOperation(final DeployedOperation operation) {
		final String name = operation.getAssemblyOperation().getOperationType().getSignature();
		this.printStream.println(INSERTION + INSERTION + name);
	}

}
