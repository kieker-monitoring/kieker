package kieker.tests.junit.analysis.plugins.traceAnalysis.util;

import kieker.analysis.datamodel.AllocationComponent;
import kieker.analysis.datamodel.AssemblyComponent;
import kieker.analysis.datamodel.ComponentType;
import kieker.analysis.datamodel.Execution;
import kieker.analysis.datamodel.ExecutionContainer;
import kieker.analysis.datamodel.Operation;
import kieker.analysis.datamodel.Signature;
import kieker.analysis.datamodel.repository.SystemModelRepository;

/*
 * ==================LICENCE=========================
 * Copyright 2006-2010 Kieker Project
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
 * ==================================================
 */

/**
 *
 * @author Andre van Hoorn
 */
public class ExecutionFactory {
    private final static String DEFAULT_STRING = "N/A";

    private final SystemModelRepository systemEntityFactory;
    
    private ExecutionFactory (){
        this.systemEntityFactory = null;
    }

    public ExecutionFactory (final SystemModelRepository systemEntityFactory){
        this.systemEntityFactory = systemEntityFactory;
    }

    public Execution genExecution(
            final long traceId,
            final long tin,
            final long tout,
            final int eoi,
            final int ess){
        ComponentType componentTypeA =
                this.systemEntityFactory.getTypeRepositoryFactory().createAndRegisterComponentType(
                DEFAULT_STRING, DEFAULT_STRING);
        Operation operationAa =
                this.systemEntityFactory.getOperationFactory().createAndRegisterOperation(
                DEFAULT_STRING,
                componentTypeA,
                new Signature(
                DEFAULT_STRING,
                DEFAULT_STRING,
                new String[] {DEFAULT_STRING}));
        componentTypeA.addOperation(operationAa);
        AssemblyComponent assemblyComponentA =
                this.systemEntityFactory.getAssemblyFactory().createAndRegisterAssemblyComponentInstance(
                DEFAULT_STRING,
                componentTypeA);
        ExecutionContainer containerC =
                this.systemEntityFactory.getExecutionEnvironmentFactory().createAndRegisterExecutionContainer(
                DEFAULT_STRING, DEFAULT_STRING);
        AllocationComponent allocationComponentA =
                this.systemEntityFactory.getAllocationFactory().createAndRegisterAllocationComponentInstance(
                DEFAULT_STRING,
                assemblyComponentA,
                containerC);
        return new Execution(
                operationAa, allocationComponentA, traceId,
                DEFAULT_STRING, eoi, ess, tin, tout);
    }
}
