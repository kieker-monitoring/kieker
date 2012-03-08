package kieker.tools.traceAnalysis.plugins.visualization.dependencyGraph;

import kieker.tools.traceAnalysis.Constants;
import kieker.tools.traceAnalysis.systemModel.AbstractMessage;

public abstract class NodeDecorator {

	public static NodeDecorator createFromName(final String optionName) {
		if (Constants.RESPONSE_TIME_DECORATOR_FLAG.equals(optionName)) {
			return new ResponseTimeNodeDecorator();
		}

		return null;
	}

	public abstract void processMessage(AbstractMessage message, DependencyGraphNode<?> sourceNode, DependencyGraphNode<?> targetNode);

}
