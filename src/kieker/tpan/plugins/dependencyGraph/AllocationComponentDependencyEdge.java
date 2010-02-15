package kieker.tpan.plugins.dependencyGraph;

import kieker.tpan.datamodel.system.AllocationComponentInstance;

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
 * @author voorn
 */
public class AllocationComponentDependencyEdge {
    private final AllocationComponentDependencyNode source;
    private final AllocationComponentDependencyNode destination;
    private int outgoingWeight = 0;
    private int incomingWeight = 0;

    public AllocationComponentDependencyEdge (
            final AllocationComponentDependencyNode source,
            final AllocationComponentDependencyNode destination){
        this.source = source;
        this.destination = destination;
    }

    public final AllocationComponentDependencyNode getDestination() {
        return this.destination;
    }

    public final int getOutgoingWeight() {
        return this.outgoingWeight;
    }

    public final void incOutgoingWeight() {
        this.outgoingWeight++;
    }

    public final void incIncomingWeight() {
        this.incomingWeight++;
    }

    public final AllocationComponentDependencyNode getSource() {
        return this.source;
    }


}
