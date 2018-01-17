package kieker.analysisteetime.util.graph.export.blueprints;

import kieker.analysisteetime.util.graph.Graph;

import teetime.stage.basic.AbstractTransformation;

public class BlueprintsTransformerStage extends AbstractTransformation<Graph, com.tinkerpop.blueprints.Graph> {

	@Override
	protected void execute(final Graph graph) {
		BlueprintsTransformer transformer = new BlueprintsTransformer(graph);
		this.getOutputPort().send(transformer.transform());
	}

}
