package kieker.analysisteetime.util.graph.export.graphml;

import org.graphdrawing.graphml.GraphType;
import org.graphdrawing.graphml.GraphmlType;

import teetime.stage.basic.AbstractTransformation;

@Deprecated
public class GraphMLTypeTransformationStage extends AbstractTransformation<GraphType, GraphmlType> {

	@Override
	protected void execute(final GraphType graphType) {

		final GraphmlType graphmlType = new GraphmlType();
		graphmlType.getGraphOrData().add(graphType);

		this.getOutputPort().send(graphmlType);

	}

}
