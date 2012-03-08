package kieker.tools.traceAnalysis.plugins.visualization.dependencyGraph;

import kieker.tools.traceAnalysis.systemModel.AbstractMessage;

public class ResponseTimeNodeDecorator extends NodeDecorator {

	@Override
	public void processMessage(final AbstractMessage message, final DependencyGraphNode<?> sourceNode, final DependencyGraphNode<?> targetNode) {
		// Ignore internal executions
		if (sourceNode.equals(targetNode)) {
			return;
		}

		ResponseTimeDecoration timeDecoration = targetNode.getDecoration(ResponseTimeDecoration.class);

		if (timeDecoration == null) {
			timeDecoration = new ResponseTimeDecoration();
			targetNode.addDecoration(timeDecoration);
		}

		timeDecoration.registerExecution(message.getReceivingExecution());
	}

}
