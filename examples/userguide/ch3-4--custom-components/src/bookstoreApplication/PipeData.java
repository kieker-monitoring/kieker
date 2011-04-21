package bookstoreApplication;

public class PipeData {
	private final long loggingTimestamp;
	private final Object[] recordData;

	public PipeData(final long loggingTimestamp, final Object[] recordData) {
		this.loggingTimestamp = loggingTimestamp;
		this.recordData = recordData;
	}

	public final long getLoggingTimestamp() {
		return this.loggingTimestamp;
	}

	public final Object[] getRecordData() {
		return this.recordData;
	}	
}
