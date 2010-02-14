package kieker.tpan.datamodel.system.factories;

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
    private final TypeRepositoryFactory typeRepositoryFactory =
            new TypeRepositoryFactory(this);
    private final AssemblyFactory assemblyFactory =
            new AssemblyFactory(this);
    private final ExecutionEnvironmentFactory executionEnvironmentFactory =
            new ExecutionEnvironmentFactory(this);
    private final AllocationFactory allocationFactory =
            new AllocationFactory(this);
    private final OperationFactory operationFactory =
            new OperationFactory(this);

    public SystemEntityFactory(){
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
