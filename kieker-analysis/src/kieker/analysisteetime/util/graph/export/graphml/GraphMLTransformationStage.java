package kieker.analysisteetime.util.graph.export.graphml;

import org.graphdrawing.graphml.GraphmlType;

import kieker.analysisteetime.util.graph.Graph;

import teetime.stage.basic.AbstractTransformation;

/**
 * @author Sören Henning
 *
 * @since 1.13
 */
public class GraphMLTransformationStage extends AbstractTransformation<Graph, GraphmlType> {

	public GraphMLTransformationStage() {
		super();
	}

	@Override
	protected void execute(final Graph graph) {

		final GraphMLTransformer graphMLTransformer = new GraphMLTransformer(graph);
		final GraphmlType graphmlType = graphMLTransformer.transform();

		this.getOutputPort().send(graphmlType);

	}

}
