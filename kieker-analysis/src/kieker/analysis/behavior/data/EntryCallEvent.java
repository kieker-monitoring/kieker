/**
 *
 */
package kieker.analysis.behavior.data;

/**
 * @author Reiner Jung
 * @since 2.0.0
 */
public class EntryCallEvent {

	/** property declarations. */
	private final long entryTime;
	private final long exitTime;
	private String operationSignature;
	private String classSignature;
	private final String sessionId;
	private final String hostname;

	public EntryCallEvent(final long entryTime, final long exitTime, final String operationSignature, final String classSignature, final String sessionId,
			final String hostname) {
		super();
		this.entryTime = entryTime;
		this.exitTime = exitTime;
		this.operationSignature = operationSignature;
		this.classSignature = classSignature;
		this.sessionId = sessionId;
		this.hostname = hostname;
	}

	public String getOperationSignature() {
		return this.operationSignature;
	}

	public void setOperationSignature(final String operationSignature) {
		this.operationSignature = operationSignature;
	}

	public String getClassSignature() {
		return this.classSignature;
	}

	public void setClassSignature(final String classSignature) {
		this.classSignature = classSignature;
	}

	public long getEntryTime() {
		return this.entryTime;
	}

	public long getExitTime() {
		return this.exitTime;
	}

	public String getSessionId() {
		return this.sessionId;
	}

	public String getHostname() {
		return this.hostname;
	}

}
