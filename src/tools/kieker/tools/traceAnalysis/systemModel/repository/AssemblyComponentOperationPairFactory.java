/***************************************************************************
 * Copyright 2011 by
 *  + Christian-Albrechts-University of Kiel
 *    + Department of Computer Science
 *      + Software Engineering Group 
 *  and others.
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
 ***************************************************************************/

package kieker.tools.traceAnalysis.systemModel.repository;

import kieker.tools.traceAnalysis.systemModel.util.AssemblyComponentOperationPair;
import java.util.Collection;
import java.util.Hashtable;
import kieker.tools.traceAnalysis.systemModel.AssemblyComponent;
import kieker.tools.traceAnalysis.systemModel.Operation;

/**
 *
 * @author Andre van Hoorn
 */
public class AssemblyComponentOperationPairFactory extends AbstractSystemSubRepository {
    private final Hashtable<String, AssemblyComponentOperationPair> pairsByName =
            new Hashtable<String, AssemblyComponentOperationPair>();
    private final Hashtable<Integer, AssemblyComponentOperationPair> pairsById =
            new Hashtable<Integer, AssemblyComponentOperationPair>();

    public final AssemblyComponentOperationPair rootPair;

    public AssemblyComponentOperationPairFactory(
            final SystemModelRepository systemFactory){
        super(systemFactory);
        AssemblyComponent rootAssembly =
                systemFactory.getAssemblyFactory().rootAssemblyComponent;
        Operation rootOperation =
                systemFactory.getOperationFactory().rootOperation;
        this.rootPair = this.getPairInstanceByPair(rootAssembly, rootOperation);
    }

    /** Returns a corresponding pair instance (existing or newly created) */
    public final AssemblyComponentOperationPair getPairInstanceByPair(
            final AssemblyComponent AssemblyComponent,
            final Operation operation){
        AssemblyComponentOperationPair inst =
                this.getPairByNamedIdentifier(AssemblyComponent.getId()+"-"+operation.getId());
        if (inst == null){
            return this.createAndRegisterPair(operation, AssemblyComponent);
        }
        return inst;
    }

    private AssemblyComponentOperationPair createAndRegisterPair(
            final Operation operation,
            final AssemblyComponent AssemblyComponent){
            return this.createAndRegisterPair(AssemblyComponent.getId()+"-"+operation.getId(),
                    operation, AssemblyComponent);
    }

    /** Returns the instance for the passed factory name; null if no instance
     *  with this factory name.
     */
    private AssemblyComponentOperationPair getPairByNamedIdentifier(final String namedIdentifier){
        return this.pairsByName.get(namedIdentifier);
    }

    /** Returns the instance for the passed ID; null if no instance
     *  with this ID.
     */
    public final AssemblyComponentOperationPair getPairById(final int id){
        return this.pairsById.get(id);
    }

    private AssemblyComponentOperationPair createAndRegisterPair(
            final String namedIdentifier,
            final Operation operation,
            final AssemblyComponent AssemblyComponent){
            AssemblyComponentOperationPair newInst;
            if (this.pairsByName.containsKey(namedIdentifier)){
                throw new IllegalArgumentException("Element with name " + namedIdentifier + "exists already");
            }
            int id = this.getAndIncrementNextId();
            newInst = new AssemblyComponentOperationPair(id,
                    operation, AssemblyComponent);
            this.pairsById.put(id, newInst);
            this.pairsByName.put(namedIdentifier, newInst);
            return newInst;
    }

    public final Collection<AssemblyComponentOperationPair> getPairs(){
        return this.pairsById.values();
    }
}
