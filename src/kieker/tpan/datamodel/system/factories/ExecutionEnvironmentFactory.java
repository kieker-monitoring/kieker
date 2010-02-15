package kieker.tpan.datamodel.system.factories;

import java.util.Collection;
import java.util.Hashtable;
import kieker.tpan.datamodel.system.ExecutionContainer;

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
public class ExecutionEnvironmentFactory extends AbstractSystemSubFactory {
    private final Hashtable<String, ExecutionContainer> executionContainersByName =
            new Hashtable<String, ExecutionContainer>();
    private final Hashtable<Integer, ExecutionContainer> executionContainersById =
            new Hashtable<Integer, ExecutionContainer>();

    public final ExecutionContainer rootExecutionContainer;

    public ExecutionEnvironmentFactory(final SystemEntityFactory systemFactory,
            final ExecutionContainer rootExecutionContainer){
        super(systemFactory);
        this.rootExecutionContainer = rootExecutionContainer;
    }

   /** Returns the instance for the passed factoryIdentifier; null if no instance
     *  with this factoryIdentifier.
     */
    public final ExecutionContainer getExecutionContainerByFactoryIdentifier(final String factoryIdentifier){
        return this.executionContainersByName.get(factoryIdentifier);
    }

    public final ExecutionContainer createAndRegisterExecutionContainer(
            final String factoryIdentifier,
            final String name){
            ExecutionContainer newInst;
            if (this.executionContainersByName.containsKey(factoryIdentifier)){
                throw new IllegalArgumentException("Element with name " + factoryIdentifier + "exists already");
            }
            int id = this.getAndIncrementNextId();
            newInst = new ExecutionContainer(id, null, name);
            this.executionContainersById.put(id, newInst);
            this.executionContainersByName.put(factoryIdentifier, newInst);
            return newInst;
    }

    public final Collection<ExecutionContainer> getExecutionContainers(){
        return this.executionContainersById.values();
    }
}
