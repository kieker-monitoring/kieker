/***************************************************************************
 * Copyright (C) 2021 Kieker Project (https://kieker-monitoring.net)
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
package kieker.tools.mvis.graph;

import java.time.temporal.ChronoUnit;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EObject;

import kieker.analysis.architecture.dependency.AbstractDependencyGraphBuilder;
import kieker.analysis.architecture.dependency.PropertyConstants;
import kieker.analysis.architecture.dependency.ResponseTimeDecorator;
import kieker.analysis.architecture.repository.ModelRepository;
import kieker.analysis.generic.graph.GraphFactory;
import kieker.analysis.generic.graph.IEdge;
import kieker.analysis.generic.graph.IGraph;
import kieker.analysis.generic.graph.IGraphElementSelector;
import kieker.analysis.generic.graph.INode;
import kieker.model.analysismodel.assembly.AssemblyComponent;
import kieker.model.analysismodel.deployment.DeployedStorage;
import kieker.model.analysismodel.execution.EDirection;
import kieker.model.analysismodel.execution.ExecutionPackage;
import kieker.model.analysismodel.execution.Invocation;
import kieker.model.analysismodel.execution.OperationDataflow;
import kieker.model.analysismodel.execution.StorageDataflow;
import kieker.model.analysismodel.source.SourceModel;
import kieker.model.analysismodel.source.SourcePackage;
import kieker.model.analysismodel.statistics.StatisticRecord;
import kieker.model.analysismodel.statistics.StatisticsPackage;

/**
 * @author Reiner Jung
 * @since 2.0.0
 *
 */
public abstract class AbstractColorDependencyGraphBuilder extends AbstractDependencyGraphBuilder {

	private SourceModel sourcesModel;
	private final IGraphElementSelector selector;

	private final ColorSet componentFGSet = new ColorSet("#000000", "#0000af", "#00af00", "#4a4a4a", "#ff00ff");
	private final ColorSet componentBGSet = new ColorSet("#ffffff", "#c0c0ff", "#c0ffc0", "#fafafa", "#ffc0ff");

	private final ColorSet operationFGSet = new ColorSet("#000000", "#0000af", "#00af00", "#fafafa", "#ff00ff");
	private final ColorSet operationBGSet = new ColorSet("#ffffff", "#a0a0ff", "#90ff90", "#eaeaea", "#d000d0");

	public AbstractColorDependencyGraphBuilder(final IGraphElementSelector selector) {
		super();
		this.selector = selector;
	}

	protected String selectForegroundColor(final EObject object) {
		final ColorSet colorSet;
		if (object instanceof AssemblyComponent) {
			colorSet = this.componentFGSet;
		} else {
			colorSet = this.operationFGSet;
		}
		return this.selectColor(object, colorSet);
	}

	protected String selectBackgroundColor(final EObject object) {
		final ColorSet colorSet;

		if (object instanceof AssemblyComponent) {
			colorSet = this.componentBGSet;
		} else {
			colorSet = this.operationBGSet;
		}
		return this.selectColor(object, colorSet);
	}

	private String selectColor(final EObject object, final ColorSet colorSet) {
		final EList<String> sources = this.sourcesModel.getSources().get(object);
		if (sources != null) {
			if (this.selector.isColorGroup(sources, 0)) {
				return colorSet.both;
			} else if (this.selector.isColorGroup(sources, 1)) {
				return colorSet.first;
			} else if (this.selector.isColorGroup(sources, 2)) {
				return colorSet.second;
			} else {
				return colorSet.other; // other objects
			}
		} else {
			return colorSet.error; // pink on error
		}
	}

	@Override
	public IGraph<INode, IEdge> build(final ModelRepository repository) {
		this.graph = GraphFactory.createGraph(repository.getName());

		this.sourcesModel = repository.getModel(SourcePackage.Literals.SOURCE_MODEL);
		this.executionModel = repository.getModel(ExecutionPackage.Literals.EXECUTION_MODEL);
		this.statisticsModel = repository.getModel(StatisticsPackage.Literals.STATISTICS_MODEL);
		this.responseTimeDecorator = new ResponseTimeDecorator(this.statisticsModel, ChronoUnit.NANOS);

		for (final Invocation invocation : this.executionModel.getInvocations().values()) {
			this.handleInvocation(invocation);
		}
		for (final StorageDataflow storageDataflow : this.executionModel.getStorageDataflows().values()) {
			this.handleStorageDataflow(storageDataflow);
		}
		for (final OperationDataflow operationDataflow : this.executionModel.getOperationDataflows().values()) {
			this.handleOperationDataflow(operationDataflow);
		}
		return this.graph;
	}

	private void handleInvocation(final Invocation invocation) {
		final INode sourceVertex = invocation.getCaller() != null ? this.addVertex(invocation.getCaller()) // NOCS
				: this.addVertexForEntry(); // NOCS (declarative)
		final INode targetVertex = this.addVertex(invocation.getCallee());

		final StatisticRecord statisticRecord = this.statisticsModel.getStatistics().get(invocation);

		if (statisticRecord != null) {
			final long calls = (Long) statisticRecord.getProperties().get(PropertyConstants.CALLS);
			this.addEdge(sourceVertex, targetVertex, calls);
		} else {
			this.addEdge(sourceVertex, targetVertex, 0);
		}
	}

	private void handleStorageDataflow(final StorageDataflow storageDataflow) {
		final INode sourceVertex = storageDataflow.getCode() != null ? this.addVertex(storageDataflow.getCode()) // NOCS
				: this.addVertexForEntry(); // NOCS (declarative)
		final INode targetVertex = this.addStorageVertex(storageDataflow.getStorage());

		final StatisticRecord statisticRecord = this.statisticsModel.getStatistics().get(storageDataflow);
		if (statisticRecord != null) {
			final Long calls = (Long) statisticRecord.getProperties().get(PropertyConstants.CALLS);
			if (calls != null) {
				this.addEdge(sourceVertex, targetVertex, calls);
			}
		}

		// TODO add this if useful: how to add values to the edge for dataflow
		// EDirection direction = storageAccess.getDirection();
		// switch(direction){
		// case WRITE:
		// this.addEdge(sourceVertex, targetVertex, 1);
		// break;
		// case READ:
		// this.addEdge(targetVertex, sourceVertex, 0);
		// break;
		// case BOTH:
		// this.addEdge(sourceVertex, targetVertex, 2);
		// this.addEdge(targetVertex, sourceVertex, 2);
		// break;
		// }
	}

	private void handleOperationDataflow(final OperationDataflow operationDataflow) {
		final INode sourceVertex = operationDataflow.getCaller() != null ? this.addVertex(operationDataflow.getCaller())
				: this.addVertexForEntry(); // NOCS (declarative)
		final INode targetVertex = this.addVertex(operationDataflow.getCallee());

		// final EMap<EObject, StatisticRecord> statisticsMap =
		// this.statisticsModel.getStatistics();
		/*
		 * final Statistics statistics = statisticsMap.get(operationDataflow); final
		 * EMap<EPredefinedUnits, StatisticRecord> recordMap = statistics.getStatistics(); final
		 * StatisticRecord record = recordMap.get(EPredefinedUnits.INVOCATION); final
		 * EMap<EPropertyType, Object> properties = record.getProperties();
		 *
		 * final long calls = (Long) properties.get(EPropertyType.COUNT);
		 */
		final EDirection direction = operationDataflow.getDirection();
		switch (direction) {
		case WRITE:
			this.addEdge(sourceVertex, targetVertex, 1);
			break;
		case READ:
			this.addEdge(targetVertex, sourceVertex, 0);
			break;
		case BOTH:
			this.addEdge(sourceVertex, targetVertex, 2);
			this.addEdge(targetVertex, sourceVertex, 2);
			break;
		default:
			break;
		}
	}

	protected abstract INode addStorageVertex(final DeployedStorage deployedStorage); // NOPMD

	/**
	 * Color set for coloring for and background of elements.
	 *
	 * @author Reiner Jung
	 * @since 2.0.0
	 */
	private final class ColorSet {
		private final String both;
		private final String first;
		private final String second;
		private final String other;
		private final String error;

		private ColorSet(final String both, final String first, final String second, final String other,
				final String error) {
			this.both = both;
			this.first = first;
			this.second = second;
			this.other = other;
			this.error = error;
		}
	}
}
