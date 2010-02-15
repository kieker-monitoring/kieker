package kieker.tpan.datamodel.system.factories;

import java.util.Collection;
import java.util.Hashtable;
import kieker.tpan.datamodel.system.ComponentType;
import kieker.tpan.datamodel.system.Operation;
import kieker.tpan.datamodel.system.Signature;

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
public class OperationFactory extends AbstractSystemSubFactory {
    private final Hashtable<String, Operation> operationsByName =
            new Hashtable<String, Operation>();
    private final Hashtable<Integer, Operation> operationsById =
            new Hashtable<Integer, Operation>();

    public final Operation rootOperation;

    public OperationFactory(final SystemEntityFactory systemFactory,
            final Operation rootOperation){
        super(systemFactory);
        this.rootOperation = rootOperation;
    }

    /** Returns the instance for the passed factoryIdentifier; null if no instance
     *  with this factoryIdentifier.
     */
    public final Operation getOperationByFactoryIdentifier(final String factoryIdentifier){
        return this.operationsByName.get(factoryIdentifier);
    }

    public final Operation createAndRegisterOperation(
            final String factoryIdentifier,
            final ComponentType componentType,
            final Signature signature){
            Operation newInst;
            if (this.operationsByName.containsKey(factoryIdentifier)){
                throw new IllegalArgumentException("Element with name " + factoryIdentifier + "exists already");
            }
            int id = this.getAndIncrementNextId();
            newInst = new Operation(id,
                    componentType, signature);
            this.operationsById.put(id, newInst);
            this.operationsByName.put(factoryIdentifier, newInst);
            return newInst;
    }

    public final Collection<Operation> getOperations(){
        return this.operationsById.values();
    }
}
