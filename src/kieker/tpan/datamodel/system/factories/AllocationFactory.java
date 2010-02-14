package kieker.tpan.datamodel.system.factories;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Hashtable;
import kieker.tpan.datamodel.system.AllocationComponentInstance;
import kieker.tpan.datamodel.system.AssemblyComponentInstance;
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
public class AllocationFactory extends AbstractSystemSubFactory {
    private final Hashtable<String, AllocationComponentInstance>
            allocationComponentInstancesByName =
            new Hashtable<String, AllocationComponentInstance>();
    private final ArrayList<AllocationComponentInstance>
            allocationComponentInstancesById = new ArrayList<AllocationComponentInstance>();

    public AllocationFactory(SystemEntityFactory systemFactory){
        super(systemFactory);
    }

    /** Returns the instance for the passed name; null if no instance
     *  with this name.
     */
    public final AllocationComponentInstance getAllocationComponentInstanceByName(final String name){
        return this.allocationComponentInstancesByName.get(name);
    }

    public final AllocationComponentInstance createAndRegisterAllocationComponentInstance(
            final String name,
            final AssemblyComponentInstance assemblyComponentInstance,
            final ExecutionContainer executionContainer){
            AllocationComponentInstance newInst;
            if (this.allocationComponentInstancesByName.containsKey(name)){
                throw new IllegalArgumentException("Element with name " + name + "exists already");
            }
            int id = this.getAndIncrementNextId();
            newInst = new AllocationComponentInstance(id,
                    assemblyComponentInstance, executionContainer);
            this.allocationComponentInstancesById.add(id, newInst);
            this.allocationComponentInstancesByName.put(name, newInst);
            return newInst;
    }

    public final Collection<AllocationComponentInstance> getAllocationComponentInstances(){
        return this.allocationComponentInstancesById;
    }
}
