package kieker.tpan.datamodel.system.factories;

import java.util.ArrayList;
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
    private final ArrayList<ExecutionContainer> executionContainersById =
            new ArrayList<ExecutionContainer>();

    public ExecutionEnvironmentFactory(SystemEntityFactory systemFactory){
        super(systemFactory);
    }

   /** Returns the instance for the passed name; null if no instance
     *  with this name.
     */
    public final ExecutionContainer getExecutionContainerByName(final String name){
        return this.executionContainersByName.get(name);
    }

    public final ExecutionContainer createAndRegisterExecutionContainer(
            final String name){
            ExecutionContainer newInst;
            if (this.executionContainersByName.containsKey(name)){
                throw new IllegalArgumentException("Element with name " + name + "exists already");
            }
            int id = this.getAndIncrementNextId();
            newInst = new ExecutionContainer(id, null, name);
            this.executionContainersById.add(id, newInst);
            this.executionContainersByName.put(name, newInst);
            return newInst;
    }
}
