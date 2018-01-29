package kieker.monitoring.writer.tcp;

public class ConnectionTimeoutException extends RuntimeException {

	private static final long serialVersionUID = -8790734039487468700L;

	public ConnectionTimeoutException(final String message) {
		super(message);
	}
}
