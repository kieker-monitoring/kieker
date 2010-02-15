package kieker.tpan.datamodel.factories;

import kieker.tpan.datamodel.AllocationComponentInstance;
import kieker.tpan.datamodel.AssemblyComponentInstance;
import kieker.tpan.datamodel.ComponentType;
import kieker.tpan.datamodel.ExecutionContainer;
import kieker.tpan.datamodel.Operation;
import kieker.tpan.datamodel.Signature;

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
public class SystemEntityFactory {

    private final TypeRepositoryFactory typeRepositoryFactory;
    private final AssemblyFactory assemblyFactory;
    private final ExecutionEnvironmentFactory executionEnvironmentFactory;
    private final AllocationFactory allocationFactory;
    private final OperationFactory operationFactory;

    public SystemEntityFactory() {
        ComponentType rootComponentType =
                new ComponentType(AbstractSystemSubFactory.ROOT_ELEMENT_ID, "$");
        this.typeRepositoryFactory = new TypeRepositoryFactory(this, rootComponentType);
        AssemblyComponentInstance rootAssemblyComponentInstance =
                new AssemblyComponentInstance(AbstractSystemSubFactory.ROOT_ELEMENT_ID, "$", rootComponentType);
        this.assemblyFactory = new AssemblyFactory(this, rootAssemblyComponentInstance);
        ExecutionContainer rootExecutionContainer =
                new ExecutionContainer(AbstractSystemSubFactory.ROOT_ELEMENT_ID, null, "$");
        this.executionEnvironmentFactory = new ExecutionEnvironmentFactory(this, rootExecutionContainer);
        AllocationComponentInstance rootAllocation =
                new AllocationComponentInstance(AbstractSystemSubFactory.ROOT_ELEMENT_ID, rootAssemblyComponentInstance, rootExecutionContainer);
        this.allocationFactory = new AllocationFactory(this, rootAllocation);
        Signature rootSignature = new Signature("$", "<>", new String[]{});
        Operation rootOperation = new Operation(AbstractSystemSubFactory.ROOT_ELEMENT_ID, rootComponentType, rootSignature);
        this.operationFactory = new OperationFactory(this, rootOperation);
    }

    public final AllocationFactory getAllocationFactory() {
        return this.allocationFactory;
    }

    public final AssemblyFactory getAssemblyFactory() {
        return this.assemblyFactory;
    }

    public final ExecutionEnvironmentFactory getExecutionEnvironmentFactory() {
        return this.executionEnvironmentFactory;
    }

    public final OperationFactory getOperationFactory() {
        return this.operationFactory;
    }

    public final TypeRepositoryFactory getTypeRepositoryFactory() {
        return this.typeRepositoryFactory;
    }
}
