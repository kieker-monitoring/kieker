package kieker.monitoring.probe.aspectj.operationExecution;

final class OperationStartData {
	private final boolean entrypoint;
	private final String sessionId;
	private final long traceId;
	private final long tin;
	private final String hostname;
	private final int eoi, ess;

	public OperationStartData(final boolean entrypoint, final String sessionId, final long traceId, final long tin, final String hostname,
			final int eoi, final int ess) {
		this.entrypoint = entrypoint;
		this.sessionId = sessionId;
		this.traceId = traceId;
		this.tin = tin;
		this.hostname = hostname;
		this.eoi = eoi;
		this.ess = ess;
	}

	public boolean isEntrypoint() {
		return entrypoint;
	}

	public String getSessionId() {
		return sessionId;
	}

	public long getTraceId() {
		return traceId;
	}

	public long getTin() {
		return tin;
	}

	public String getHostname() {
		return hostname;
	}

	public int getEoi() {
		return eoi;
	}

	public int getEss() {
		return ess;
	}
}