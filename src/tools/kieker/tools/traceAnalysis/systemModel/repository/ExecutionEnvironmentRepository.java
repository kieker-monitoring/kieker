package kieker.tools.traceAnalysis.systemModel.repository;

import java.util.Collection;
import java.util.Hashtable;
import kieker.tools.traceAnalysis.systemModel.ExecutionContainer;

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
public class ExecutionEnvironmentRepository extends AbstractSystemSubRepository {
    private final Hashtable<String, ExecutionContainer> executionContainersByName =
            new Hashtable<String, ExecutionContainer>();
    private final Hashtable<Integer, ExecutionContainer> executionContainersById =
            new Hashtable<Integer, ExecutionContainer>();

    public final ExecutionContainer rootExecutionContainer;

    public ExecutionEnvironmentRepository(final SystemModelRepository systemFactory,
            final ExecutionContainer rootExecutionContainer){
        super(systemFactory);
        this.rootExecutionContainer = rootExecutionContainer;
        //this.executionContainersById.put(rootExecutionContainer.getId(),
        //        rootExecutionContainer);
    }

   /** Returns the instance for the passed namedIdentifier; null if no instance
     *  with this namedIdentifier.
     */
    public final ExecutionContainer lookupExecutionContainerByNamedIdentifier(
            final String namedIdentifier){
        return this.executionContainersByName.get(namedIdentifier);
    }

   /** Returns the instance for the passed container ID; null if no instance
     *  with this ID.
     */
    public final ExecutionContainer lookupExecutionContainerByContainerId(
            final int containerId){
        return this.executionContainersById.get(containerId);
    }

    public final ExecutionContainer createAndRegisterExecutionContainer(
            final String namedIdentifier,
            final String name){
            ExecutionContainer newInst;
            if (this.executionContainersByName.containsKey(namedIdentifier)){
                throw new IllegalArgumentException("Element with name " + namedIdentifier + "exists already");
            }
            int id = this.getAndIncrementNextId();
            newInst = new ExecutionContainer(id, null, name);
            this.executionContainersById.put(id, newInst);
            this.executionContainersByName.put(namedIdentifier, newInst);
            return newInst;
    }

    public final Collection<ExecutionContainer> getExecutionContainers(){
        return this.executionContainersById.values();
    }
}
