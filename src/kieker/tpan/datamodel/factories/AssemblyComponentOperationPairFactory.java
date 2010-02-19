package kieker.tpan.datamodel.factories;

import kieker.tpan.datamodel.AssemblyComponentOperationPair;
import kieker.tpan.datamodel.factories.*;
import java.util.Collection;
import java.util.Hashtable;
import kieker.tpan.datamodel.AssemblyComponentInstance;
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
public class AssemblyComponentOperationPairFactory extends AbstractSystemSubFactory {
    private final Hashtable<String, AssemblyComponentOperationPair> pairsByName =
            new Hashtable<String, AssemblyComponentOperationPair>();
    private final Hashtable<Integer, AssemblyComponentOperationPair> pairsById =
            new Hashtable<Integer, AssemblyComponentOperationPair>();

    public final AssemblyComponentOperationPair rootPair;

    public AssemblyComponentOperationPairFactory(final SystemEntityFactory systemFactory,
            final AssemblyComponentOperationPair rootPair){
        super(systemFactory);
        this.rootPair = rootPair;
    }

    /** Returns a corresponding pair instance (existing or newly created) */
    public final AssemblyComponentOperationPair getPairInstanceByPair(
            final AssemblyComponentInstance AssemblyComponent,
            final Operation operation){
        AssemblyComponentOperationPair inst =
                this.getPairByFactoryName(AssemblyComponent.getId()+"-"+operation.getId());
        if (inst == null){
            return this.createAndRegisterPair(operation, AssemblyComponent);
        }
        return inst;
    }

    private final AssemblyComponentOperationPair createAndRegisterPair(
            final Operation operation,
            final AssemblyComponentInstance AssemblyComponent){
            return this.createAndRegisterPair(AssemblyComponent.getId()+"-"+operation.getId(),
                    operation, AssemblyComponent);
    }

    /** Returns the instance for the passed factory name; null if no instance
     *  with this factory name.
     */
    public final AssemblyComponentOperationPair getPairByFactoryName(final String factoryIdentifier){
        return this.pairsByName.get(factoryIdentifier);
    }

    /** Returns the instance for the passed ID; null if no instance
     *  with this ID.
     */
    public final AssemblyComponentOperationPair getPairById(final int id){
        return this.pairsById.get(id);
    }

    public final AssemblyComponentOperationPair createAndRegisterPair(
            final String factoryIdentifier,
            final Operation operation,
            final AssemblyComponentInstance AssemblyComponent){
            AssemblyComponentOperationPair newInst;
            if (this.pairsByName.containsKey(factoryIdentifier)){
                throw new IllegalArgumentException("Element with name " + factoryIdentifier + "exists already");
            }
            int id = this.getAndIncrementNextId();
            newInst = new AssemblyComponentOperationPair(id,
                    operation, AssemblyComponent);
            this.pairsById.put(id, newInst);
            this.pairsByName.put(factoryIdentifier, newInst);
            return newInst;
    }

    public final Collection<AssemblyComponentOperationPair> getPairs(){
        return this.pairsById.values();
    }
}
