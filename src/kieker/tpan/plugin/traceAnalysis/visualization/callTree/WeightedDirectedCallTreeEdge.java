package kieker.tpan.plugin.traceAnalysis.visualization.callTree;


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
public class WeightedDirectedCallTreeEdge<T> {

    private AbstractCallTreeNode<T> source;
    private AbstractCallTreeNode<T> destination;

    private WeightedDirectedCallTreeEdge() {
    }

    ;

    public WeightedDirectedCallTreeEdge(
            final AbstractCallTreeNode<T> source,
            final AbstractCallTreeNode<T> destination) {
        this.source = source;
        this.destination = destination;
    }

    public final AbstractCallTreeNode<T> getDestination() {
        return this.destination;
    }

    public final AbstractCallTreeNode<T> getSource() {
        return this.source;
    }
    private int outgoingWeight = 0;
    private int incomingWeight = 0;

    public final int getIncomingWeight() {
        return this.incomingWeight;
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
}
