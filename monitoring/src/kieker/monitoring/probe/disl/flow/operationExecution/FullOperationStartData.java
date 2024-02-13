package kieker.monitoring.probe.disl.flow.operationExecution;

import kieker.monitoring.probe.aspectj.operationExecution.OperationStartData;

public final class FullOperationStartData extends OperationStartData {

	private final String operationSignature;

	public FullOperationStartData(final boolean entrypoint, final long traceId, final long tin, final int eoi, final int ess, final String signature) {
		super(entrypoint, traceId, tin, eoi, ess);
		this.operationSignature = signature;
	}

	public String getOperationSignature() {
		return operationSignature;
	}

}
