/***************************************************************************
 * Copyright 2020 Kieker Project (http://kieker-monitoring.net)
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

package kieker.analysisteetime.util.graph.traversal;

import java.util.ArrayList;
import java.util.List;

import kieker.analysisteetime.util.graph.IGraph;

/**
 * @author Sören Henning
 *
 * @since 1.14
 */
public abstract class AbstractGraphTraverser {

	protected List<IVertexVisitor> vertexVisitors;
	protected List<IEdgeVisitor> edgeVisitors;

	public AbstractGraphTraverser() {
		this.vertexVisitors = new ArrayList<>();
		this.edgeVisitors = new ArrayList<>();
	}

	public AbstractGraphTraverser(final IVertexVisitor vertexVisitor, final IEdgeVisitor edgeVisitor) {
		this();
		this.vertexVisitors.add(vertexVisitor);
		this.edgeVisitors.add(edgeVisitor);
	}

	public AbstractGraphTraverser(final List<IVertexVisitor> vertexVisitors, final List<IEdgeVisitor> edgeVisitors) {
		this.vertexVisitors = vertexVisitors;
		this.edgeVisitors = edgeVisitors;
	}

	public List<IVertexVisitor> getVertexVisitors() {
		return this.vertexVisitors;
	}

	public void setVertexVisitors(final List<IVertexVisitor> vertexVisitors) {
		this.vertexVisitors = vertexVisitors;
	}

	public void addVertexVisitor(final IVertexVisitor vertexVisitor) {
		this.vertexVisitors.add(vertexVisitor);
	}

	public List<IEdgeVisitor> getEdgeVisitors() {
		return this.edgeVisitors;
	}

	public void setEdgeVisitors(final List<IEdgeVisitor> edgeVisitors) {
		this.edgeVisitors = edgeVisitors;
	}

	public void addEdgeVisitor(final IEdgeVisitor edgeVisitor) {
		this.edgeVisitors.add(edgeVisitor);
	}

	public abstract void traverse(final IGraph graph);

}
