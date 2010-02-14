package kieker.tpan.datamodel.system.factories;

import java.util.ArrayList;
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
    private final ArrayList<Operation> operationsById =
            new ArrayList<Operation>();

    public OperationFactory(SystemEntityFactory systemFactory){
        super(systemFactory);
    }

    /** Returns the instance for the passed name; null if no instance
     *  with this name.
     */
    public final Operation getOperationByName(final String name){
        return this.operationsByName.get(name);
    }

    public final Operation createAndRegisterOperation(
            final String name,
            final ComponentType componentType,
            final Signature signature){
            Operation newInst;
            if (this.operationsByName.containsKey(name)){
                throw new IllegalArgumentException("Element with name " + name + "exists already");
            }
            int id = this.getAndIncrementNextId();
            newInst = new Operation(id,
                    componentType, signature);
            this.operationsById.add(id, newInst);
            this.operationsByName.put(name, newInst);
            return newInst;
    }

    public final Collection<Operation> getOperations(){
        return this.operationsById;
    }
}
