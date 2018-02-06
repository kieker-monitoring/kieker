package kieker.analysisteetime.util.graph.export.blueprints;

import kieker.analysisteetime.util.graph.Graph;

import teetime.stage.basic.AbstractTransformation;

/**
 * @author Sören Henning
 *
 * @since 1.13
 */
public class BlueprintsTransformerStage extends AbstractTransformation<Graph, com.tinkerpop.blueprints.Graph> {

	public BlueprintsTransformerStage() {
		super();
	}

	@Override
	protected void execute(final Graph graph) {
		final BlueprintsTransformer transformer = new BlueprintsTransformer(graph);
		this.getOutputPort().send(transformer.transform());
	}

}
