package kieker.common.exception;

public class UnknownRecordTypeException extends Exception {

	private static final long serialVersionUID = 3967732396720668295L;
	private final String classname;

	public UnknownRecordTypeException(final String message, final String classname, final Throwable cause) {
		super(message, cause);
		this.classname = classname;
	}

	public String getClassName() {
		return this.classname;
	}
}
