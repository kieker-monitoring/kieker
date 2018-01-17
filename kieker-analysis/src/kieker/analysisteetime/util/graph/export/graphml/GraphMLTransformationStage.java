package kieker.analysisteetime.util.graph.export.graphml;

import org.graphdrawing.graphml.GraphmlType;

import kieker.analysisteetime.util.graph.Graph;

import teetime.stage.basic.AbstractTransformation;

public class GraphMLTransformationStage extends AbstractTransformation<Graph, GraphmlType> {

	@Override
	protected void execute(final Graph graph) {

		final GraphMLTransformer graphMLTransformer = new GraphMLTransformer(graph);
		final GraphmlType graphmlType = graphMLTransformer.transform();

		this.getOutputPort().send(graphmlType);

	}

}
