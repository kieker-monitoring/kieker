/**
 *
 */
package kieker.analysis.behavior.data;

/**
 * @author Reiner Jung
 * @since 2.0.0
 *
 */
// TODO rename class
public class PayloadAwareEntryCallEvent extends EntryCallEvent {

	/** property declarations. */
	private final String[] parameters;
	private final String[] values;
	private final int requestType;

	public PayloadAwareEntryCallEvent(final long entryTime, final long exitTime, final String operationSignature, final String classSignature,
			final String sessionId, final String hostname,
			final String[] parameters, final String[] values, final int requestType) {
		super(entryTime, exitTime, operationSignature, classSignature, sessionId, hostname);
		this.parameters = parameters;
		this.values = values;
		this.requestType = requestType;
	}

	public String[] getParameters() {
		return this.parameters;
	}

	public String[] getValues() {
		return this.values;
	}

	public int getRequestType() {
		return this.requestType;
	}

}
