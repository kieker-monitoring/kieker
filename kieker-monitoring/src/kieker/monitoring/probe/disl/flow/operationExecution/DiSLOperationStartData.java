package kieker.monitoring.probe.disl.flow.operationExecution;

import kieker.monitoring.probe.aspectj.operationExecution.OperationStartData;

public final class DiSLOperationStartData extends OperationStartData {

	private final String operationSignature;

	public DiSLOperationStartData(boolean entrypoint, String sessionId, long traceId, long tin, String hostname,
			int eoi, int ess, String signature) {
		super(entrypoint, sessionId, traceId, tin, hostname, eoi, ess);
		this.operationSignature = signature;
	}

	public String getOperationSignature() {
		return operationSignature;
	}

}
