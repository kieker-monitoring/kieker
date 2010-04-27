package kieker.tpan.datamodel.factories;

import java.util.Collection;
import java.util.Hashtable;
import kieker.tpan.datamodel.AllocationComponent;
import kieker.tpan.datamodel.Operation;
import kieker.tpan.datamodel.util.AllocationComponentOperationPair;

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
public class AllocationComponentOperationPairFactory extends AbstractSystemSubFactory {
    private final Hashtable<String, AllocationComponentOperationPair> pairsByName =
            new Hashtable<String, AllocationComponentOperationPair>();
    private final Hashtable<Integer, AllocationComponentOperationPair> pairsById =
            new Hashtable<Integer, AllocationComponentOperationPair>();

    public final AllocationComponentOperationPair rootPair;

    public AllocationComponentOperationPairFactory(final SystemEntityFactory systemFactory){
        super(systemFactory);
        AllocationComponent rootAllocation =
                systemFactory.getAllocationFactory().rootAllocationComponent;
        Operation rootOperation =
                systemFactory.getOperationFactory().rootOperation;
        this.rootPair = getPairInstanceByPair(rootAllocation, rootOperation);
    }

    /** Returns a corresponding pair instance (existing or newly created) */
    public final AllocationComponentOperationPair getPairInstanceByPair(
            final AllocationComponent allocationComponent,
            final Operation operation){
        AllocationComponentOperationPair inst = 
                this.getPairByFactoryName(allocationComponent.getId()+"-"+operation.getId());
        if (inst == null){
            return this.createAndRegisterPair(operation, allocationComponent);
        }
        return inst;
    }

    private final AllocationComponentOperationPair createAndRegisterPair(
            final Operation operation,
            final AllocationComponent allocationComponent){
            return this.createAndRegisterPair(allocationComponent.getId()+"-"+operation.getId(),
                    operation, allocationComponent);
    }

    /** Returns the instance for the passed factory name; null if no instance
     *  with this factory name.
     */
    private final AllocationComponentOperationPair getPairByFactoryName(final String factoryIdentifier){
        return this.pairsByName.get(factoryIdentifier);
    }

    /** Returns the instance for the passed ID; null if no instance
     *  with this ID.
     */
    public final AllocationComponentOperationPair getPairById(final int id){
        return this.pairsById.get(id);
    }

    private final AllocationComponentOperationPair createAndRegisterPair(
            final String factoryIdentifier,
            final Operation operation,
            final AllocationComponent allocationComponent){
            AllocationComponentOperationPair newInst;
            if (this.pairsByName.containsKey(factoryIdentifier)){
                throw new IllegalArgumentException("Element with name " + factoryIdentifier + "exists already");
            }
            int id = this.getAndIncrementNextId();
            newInst = new AllocationComponentOperationPair(id,
                    operation, allocationComponent);
            this.pairsById.put(id, newInst);
            this.pairsByName.put(factoryIdentifier, newInst);
            return newInst;
    }

    public final Collection<AllocationComponentOperationPair> getPairs(){
        return this.pairsById.values();
    }
}
