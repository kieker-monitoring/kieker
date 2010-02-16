package kieker.tpan.plugins.dependencyGraph;

import kieker.tpan.datamodel.factories.*;
import java.util.Collection;
import java.util.Hashtable;
import kieker.tpan.datamodel.AllocationComponentInstance;
import kieker.tpan.datamodel.Operation;

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

    public AllocationComponentOperationPairFactory(final SystemEntityFactory systemFactory,
            final AllocationComponentOperationPair rootPair){
        super(systemFactory);
        this.rootPair = rootPair;
    }

    /** Returns the instance for the passed factory name; null if no instance
     *  with this factory name.
     */
    public final AllocationComponentOperationPair getPairByFactoryName(final String factoryIdentifier){
        return this.pairsByName.get(factoryIdentifier);
    }

    /** Returns the instance for the passed ID; null if no instance
     *  with this ID.
     */
    public final AllocationComponentOperationPair getPairById(final int id){
        return this.pairsById.get(id);
    }

    public final AllocationComponentOperationPair createAndRegisterPair(
            final String factoryIdentifier,
            final Operation operation,
            final AllocationComponentInstance allocationComponent){
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
