package kieker.tpan.plugins.callTree;

import java.util.TreeMap;
import kieker.tpan.datamodel.SynchronousCallMessage;
import kieker.tpan.datamodel.factories.SystemEntityFactory;

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
public abstract class AbstractAggregatedCallTreeNode<T> extends AbstractCallTreeNode<T> {

    /** For faster lookup of existing children */
    protected final TreeMap<Integer, WeightedDirectedCallTreeEdge<T>> childMap =
            new TreeMap<Integer, WeightedDirectedCallTreeEdge<T>>();

    public AbstractAggregatedCallTreeNode(
            final int id,
            final SystemEntityFactory systemEntityFactory,
            final T entity,
            final boolean rootNode) {
        super(id, systemEntityFactory, entity, rootNode);
    }

    public abstract AbstractCallTreeNode<T> newCall(SynchronousCallMessage callMsg);
}
