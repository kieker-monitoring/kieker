package kieker.monitoring.probe;

public final class FullOperationStartData extends OperationStartData {

	private final String operationSignature;

	public FullOperationStartData(boolean entrypoint, long traceId, long tin, int eoi, int ess, String signature) {
		super(entrypoint, traceId, tin, eoi, ess);
		this.operationSignature = signature;
	}

	public String getOperationSignature() {
		return operationSignature;
	}

}
