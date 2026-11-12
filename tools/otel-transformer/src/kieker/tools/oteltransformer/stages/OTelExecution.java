package kieker.tools.oteltransformer.stages;

public class OTelExecution {
	
	private final String hostname;
	private final String classSignature;
	private final String operationSignature;
	private final String sessionId;
	private final long tin;
	private final long tout;
	private final int ess;
	private final int eoi;
	
	public OTelExecution(String hostname, String classSignature, String operationSignature, String sessionId, long tin, long tout, int ess, int eoi) {
		this.hostname = hostname;
		this.classSignature = classSignature;
		this.operationSignature = operationSignature;
		this.sessionId = sessionId;
		this.tin = tin;
		this.tout = tout;
		this.ess = ess;
		this.eoi = eoi;
	}
	
	public String getClassSignature() {
		return this.classSignature;
	}
	
	public String getOperationSignature() {
		return this.operationSignature;
	}
	
	public String getSessionId() {
		return this.sessionId;
	}

	public long getTin() {
		return this.tin;
	}
	
	public long getTout() {
		return tout;
	}

	public int getEss() {
		return this.ess;
	}
	
	public int getEoi() {
		return eoi;
	}

	public String getHostname() {
		return this.hostname;
	}
}
