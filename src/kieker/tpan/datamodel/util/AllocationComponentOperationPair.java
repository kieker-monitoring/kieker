package kieker.tpan.datamodel.util;

import kieker.tpan.datamodel.AllocationComponent;
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
public class AllocationComponentOperationPair {
    private final int id;
    private final Operation operation;

    private final AllocationComponent allocationComponent;

    private AllocationComponentOperationPair (){
        this.id = -1;
        this.operation = null;
        this.allocationComponent = null;
    }

    public AllocationComponentOperationPair (
            final int id, final Operation operation, final AllocationComponent allocationComponent){
        this.id = id;
        this.operation = operation;
        this.allocationComponent = allocationComponent;
    }

    public final int getId() {
        return this.id;
    }

    public final AllocationComponent getAllocationComponent() {
        return this.allocationComponent;
    }

    public final Operation getOperation() {
        return this.operation;
    }

    @Override
    public String toString() {
        return  +  this.allocationComponent.getId()+":"
                + this.operation.getId()
                + "@"+this.id + "";
    }
}
