/***************************************************************************
 * Copyright 2011 by
 *  + Christian-Albrechts-University of Kiel
 *    + Department of Computer Science
 *      + Software Engineering Group 
 *  and others.
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

package kieker.test.tools.junit.traceAnalysis.util;

import kieker.tools.traceAnalysis.systemModel.AllocationComponent;
import kieker.tools.traceAnalysis.systemModel.AssemblyComponent;
import kieker.tools.traceAnalysis.systemModel.ComponentType;
import kieker.tools.traceAnalysis.systemModel.Execution;
import kieker.tools.traceAnalysis.systemModel.ExecutionContainer;
import kieker.tools.traceAnalysis.systemModel.Operation;
import kieker.tools.traceAnalysis.systemModel.Signature;
import kieker.tools.traceAnalysis.systemModel.repository.SystemModelRepository;

/**
 * 
 * @author Andre van Hoorn
 */
public class ExecutionFactory {

	private static final String DEFAULT_STRING = "N/A";
	private final SystemModelRepository systemEntityFactory;

	@SuppressWarnings("unused")
	private ExecutionFactory() {
		this.systemEntityFactory = null;
	}

	public ExecutionFactory(final SystemModelRepository systemEntityFactory) {
		this.systemEntityFactory = systemEntityFactory;
	}

	/**
	 * Creates an Execution object initialized with the passed values.
	 * The remaining values of the Execution object are assigned default
	 * values.
	 * 
	 * @param componentTypeName
	 * @param componentInstanceName
	 * @param operationName
	 * @param traceId
	 * @param tin
	 * @param tout
	 * @param eoi
	 * @param ess
	 * @throws NullPointerException
	 *             if one of the String args has the value null.
	 * @return the created Execution object
	 */
	public Execution genExecution(final String componentTypeName, final String componentInstanceName, final String operationName, final long traceId,
			final long tin, final long tout, final int eoi, final int ess) {
		if ((componentTypeName == null) || (componentInstanceName == null) || (operationName == null)) {
			throw new NullPointerException("None of the String args must be null.");
		}

		/* Register component type (if it hasn't been registered before) */
		ComponentType componentTypeA = this.systemEntityFactory.getTypeRepositoryFactory().lookupComponentTypeByNamedIdentifier(componentTypeName);
		if (componentTypeA == null) {
			componentTypeA = this.systemEntityFactory.getTypeRepositoryFactory().createAndRegisterComponentType(componentTypeName, componentTypeName);
		}
		/* Register operation (if it hasn't been registered before) */
		Operation operationAa = this.systemEntityFactory.getOperationFactory().lookupOperationByNamedIdentifier(operationName);
		if (operationAa == null) {
			operationAa = this.systemEntityFactory.getOperationFactory().createAndRegisterOperation(operationName, componentTypeA,
					new Signature(operationName, ExecutionFactory.DEFAULT_STRING, new String[] { ExecutionFactory.DEFAULT_STRING }));
		}
		if (componentTypeA.getOperations().contains(operationAa)) {
			componentTypeA.addOperation(operationAa);
		}

		/* Register assembly component (if it hasn't been registered before) */
		AssemblyComponent assemblyComponentA = this.systemEntityFactory.getAssemblyFactory().lookupAssemblyComponentInstanceByNamedIdentifier(componentInstanceName);
		if (assemblyComponentA == null) {
			assemblyComponentA = this.systemEntityFactory.getAssemblyFactory().createAndRegisterAssemblyComponentInstance(componentInstanceName, componentTypeA);
		}

		/* Register execution container (if it hasn't been registered before) */
		ExecutionContainer containerC = this.systemEntityFactory.getExecutionEnvironmentFactory().lookupExecutionContainerByNamedIdentifier(
				ExecutionFactory.DEFAULT_STRING);
		if (containerC == null) {
			containerC = this.systemEntityFactory.getExecutionEnvironmentFactory().createAndRegisterExecutionContainer(ExecutionFactory.DEFAULT_STRING,
					ExecutionFactory.DEFAULT_STRING);
		}

		/* Register allocation container (if it hasn't been registered before) */
		AllocationComponent allocationComponentA = this.systemEntityFactory.getAllocationFactory().lookupAllocationComponentInstanceByNamedIdentifier(
				ExecutionFactory.DEFAULT_STRING);
		if (allocationComponentA == null) {
			allocationComponentA = this.systemEntityFactory.getAllocationFactory().createAndRegisterAllocationComponentInstance(ExecutionFactory.DEFAULT_STRING,
					assemblyComponentA, containerC);
		}

		return new Execution(operationAa, allocationComponentA, traceId, ExecutionFactory.DEFAULT_STRING, eoi, ess, tin, tout);
	}

	/**
	 * Creates an Execution object initialized with the passed values.
	 * The remaining values of the Execution object are assigned default
	 * values.
	 * 
	 * @param traceId
	 * @param tin
	 * @param tout
	 * @param eoi
	 * @param ess
	 * @return the created Execution object
	 */
	public Execution genExecution(final long traceId, final long tin, final long tout, final int eoi, final int ess) {
		return this.genExecution(ExecutionFactory.DEFAULT_STRING, // component type
				ExecutionFactory.DEFAULT_STRING, // component instance
				ExecutionFactory.DEFAULT_STRING, // operation name
				traceId, tin, tout, eoi, ess);
	}
}
