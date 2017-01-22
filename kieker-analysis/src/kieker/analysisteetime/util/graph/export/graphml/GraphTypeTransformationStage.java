package kieker.analysisteetime.util.graph.export.graphml;

import org.graphdrawing.graphml.GraphType;

import kieker.analysisteetime.util.graph.Graph;

import teetime.stage.basic.AbstractTransformation;

@Deprecated
public class GraphTypeTransformationStage extends AbstractTransformation<Graph, GraphType> {

	@Override
	protected void execute(final Graph graph) {

		final GraphTypeTransformer graphTypeTransformer = new GraphTypeTransformer(graph);
		final GraphType graphType = graphTypeTransformer.transform();

		this.getOutputPort().send(graphType);

	}

}
