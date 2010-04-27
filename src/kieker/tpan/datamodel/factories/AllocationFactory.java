package kieker.tpan.datamodel.factories;

import java.util.Collection;
import java.util.Hashtable;
import kieker.tpan.datamodel.AllocationComponent;
import kieker.tpan.datamodel.AssemblyComponent;
import kieker.tpan.datamodel.ExecutionContainer;

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
    private final Hashtable<String, AllocationComponent>
            allocationComponentInstancesByName =
            new Hashtable<String, AllocationComponent>();
    private final Hashtable<Integer, AllocationComponent>
            allocationComponentInstancesById = new Hashtable<Integer, AllocationComponent>();

    public final AllocationComponent rootAllocationComponent;

    public AllocationFactory(final SystemEntityFactory systemFactory,
            final AllocationComponent rootAllocationComponent){
        super(systemFactory);
        this.rootAllocationComponent = rootAllocationComponent;
    }

    /** Returns the instance for the passed factoryIdentifier; null if no instance
     *  with this factoryIdentifier.
     */
    public final AllocationComponent getAllocationComponentInstanceByFactoryIdentifier(final String factoryIdentifier){
        return this.allocationComponentInstancesByName.get(factoryIdentifier);
    }

    public final AllocationComponent createAndRegisterAllocationComponentInstance(
            final String factoryIdentifier,
            final AssemblyComponent assemblyComponentInstance,
            final ExecutionContainer executionContainer){
            AllocationComponent newInst;
            if (this.allocationComponentInstancesByName.containsKey(factoryIdentifier)){
                throw new IllegalArgumentException("Element with name " + factoryIdentifier + "exists already");
            }
            int id = this.getAndIncrementNextId();
            newInst = new AllocationComponent(id,
                    assemblyComponentInstance, executionContainer);
            this.allocationComponentInstancesById.put(id, newInst);
            this.allocationComponentInstancesByName.put(factoryIdentifier, newInst);
            return newInst;
    }

    public final Collection<AllocationComponent> getAllocationComponentInstances(){
        return this.allocationComponentInstancesById.values();
    }
}
