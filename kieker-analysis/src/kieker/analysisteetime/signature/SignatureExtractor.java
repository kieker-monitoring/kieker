package kieker.analysisteetime.signature;

public final class SignatureExtractor {

	private final OperationSignatureExtractor operationSignatureExtractor;
	private final ComponentSignatureExtractor componentSignatureExtractor;

	private SignatureExtractor(final OperationSignatureExtractor operationSignatureExtractor, final ComponentSignatureExtractor componentSignatureExtractor) {
		this.operationSignatureExtractor = operationSignatureExtractor;
		this.componentSignatureExtractor = componentSignatureExtractor;
	}

	public OperationSignatureExtractor getOperationSignatureExtractor() {
		return this.operationSignatureExtractor;
	}

	public ComponentSignatureExtractor getComponentSignatureExtractor() {
		return this.componentSignatureExtractor;
	}

	public static SignatureExtractor of(final OperationSignatureExtractor operationSignatureExtractor,
			final ComponentSignatureExtractor componentSignatureExtractor) {
		return new SignatureExtractor(operationSignatureExtractor, componentSignatureExtractor);
	}

	public static SignatureExtractor forJava() {
		return new SignatureExtractor(new JavaOperationSignatureExtractor(), new JavaComponentSignatureExtractor());
	}

}
