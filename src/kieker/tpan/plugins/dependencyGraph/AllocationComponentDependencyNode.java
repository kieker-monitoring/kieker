package kieker.tpan.plugins.dependencyGraph;

import java.util.Collection;
import java.util.Comparator;
import java.util.TreeMap;
import java.util.TreeSet;
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
public class AllocationComponentDependencyNode {
    private final AllocationComponentInstance allocationComponent;
    private final int id;

    private final TreeMap<Integer, AllocationComponentDependencyEdge> incomingDependencies
            = new TreeMap<Integer, AllocationComponentDependencyEdge>();

     private final TreeMap<Integer, AllocationComponentDependencyEdge> outgoingDependencies
            = new TreeMap<Integer, AllocationComponentDependencyEdge>();

    public AllocationComponentDependencyNode (final int id, final AllocationComponentInstance allocationComponent){
        this.id = id;
        this.allocationComponent = allocationComponent;
    }

   public final AllocationComponentInstance getAllocationComponent() {
        return this.allocationComponent;
    }

    public final Collection<AllocationComponentDependencyEdge> getIncomingDependencies() {
        return this.incomingDependencies.values();
    }

    public final Collection<AllocationComponentDependencyEdge> getOutgoingDependencies() {
        return this.outgoingDependencies.values();
    }

    public final void addOutgoingDependency(AllocationComponentDependencyNode destination){
        AllocationComponentDependencyEdge e = this.outgoingDependencies.get(destination.getId());
        if (e == null){
            e = new AllocationComponentDependencyEdge(this, destination);
            this.outgoingDependencies.put(destination.getId(), e);
        }
        e.incOutgoingWeight();
    }

    public final void addIncomingDependency(AllocationComponentDependencyNode source){
        AllocationComponentDependencyEdge e = this.incomingDependencies.get(source.getId());
        if (e == null){
            e = new AllocationComponentDependencyEdge(this, source);
            this.incomingDependencies.put(source.getId(), e);
        }
        e.incIncomingWeight();
    }

    public final int getId() {
        return this.id;
    }
}
