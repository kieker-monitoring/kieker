package kieker.tools.traceAnalysis.plugins.visualization.dependencyGraph;

import kieker.tools.traceAnalysis.systemModel.Execution;

public class ResponseTimeDecoration extends NodeDecoration {

	private static final String OUTPUT_TEMPLATE = "min: %dms, avg: %.2fms, max: %dms";

	private long responseTimeSum = 0;
	private int executionCount = 0;
	private int minimalResponseTime = Integer.MAX_VALUE;
	private int maximalResponseTime = 0;

	public void registerExecution(final Execution execution) {
		final int responseTime = (int) ((execution.getTout() / 1000000) - (execution.getTin() / 1000000));

		this.responseTimeSum += responseTime;
		this.executionCount++;

		if (responseTime < this.minimalResponseTime) {
			this.minimalResponseTime = responseTime;
		}
		if (responseTime > this.maximalResponseTime) {
			this.maximalResponseTime = responseTime;
		}
	}

	public int getMinimalResponseTime() {
		return this.minimalResponseTime;
	}

	public int getMaximalResponseTime() {
		return this.maximalResponseTime;
	}

	public float getAverageResponseTime() {
		return (this.executionCount == 0) ? 0 : ((float) this.responseTimeSum / (float) this.executionCount);
	}

	@Override
	public String createFormattedOutput() {
		return String.format(ResponseTimeDecoration.OUTPUT_TEMPLATE, this.getMinimalResponseTime(), this.getAverageResponseTime(), this.getMaximalResponseTime());
	}

}
