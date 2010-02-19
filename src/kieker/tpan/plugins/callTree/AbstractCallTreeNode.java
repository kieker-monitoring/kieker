package kieker.tpan.plugins.callTree;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import kieker.tpan.datamodel.SynchronousCallMessage;

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
public abstract class AbstractCallTreeNode<T> {

    private final T entity;
    private final int id;
    private WeightedDirectedCallTreeEdge<T> parentEdge;

    private final List<WeightedDirectedCallTreeEdge<T>> childEdges =
            new ArrayList<WeightedDirectedCallTreeEdge<T>>();

    public AbstractCallTreeNode(final int id, final T entity) {
        this.id = id;
        this.entity = entity;
    }

    public final T getEntity() {
        return this.entity;
    }

    public final WeightedDirectedCallTreeEdge<T> getParentEdge() {
        return this.parentEdge;
    }

    public final Collection<WeightedDirectedCallTreeEdge<T>> getChildEdges() {
        return this.childEdges;
    }

    /** Append edge to *sorted* list of children */
    protected final void appendChildEdge(WeightedDirectedCallTreeEdge<T> destination){
        this.childEdges.add(destination);
    }

    public abstract AbstractCallTreeNode<T> newCall(SynchronousCallMessage callMsg);

    public final int getId() {
        return this.id;
    }

    public final boolean isRootNode(){
        return this.parentEdge == null;
    }
}
