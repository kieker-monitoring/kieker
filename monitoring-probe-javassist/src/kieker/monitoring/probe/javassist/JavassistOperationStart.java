package kieker.monitoring.probe.javassist;

import kieker.monitoring.probe.aspectj.operationExecution.OperationStartData;

public class JavassistOperationStart extends OperationStartData {

	private final String operationSignature;

	public JavassistOperationStart(boolean entrypoint, String sessionId, long traceId, long tin, String hostname,
			int eoi, int ess, String signature) {
		super(entrypoint, sessionId, traceId, tin, hostname, eoi, ess);
		this.operationSignature = signature;
	}

	public String getOperationSignature() {
		return operationSignature;
	}

}
