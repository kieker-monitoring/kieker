package kieker.analysisteetime.signature;

public final class NameBuilder {

	private final OperationNameBuilder operationNameBuilder;
	private final ComponentNameBuilder componentNameBuilder;

	private NameBuilder(final OperationNameBuilder operationNameBuilder, final ComponentNameBuilder componentNameBuilder) {
		this.operationNameBuilder = operationNameBuilder;
		this.componentNameBuilder = componentNameBuilder;
	}

	public OperationNameBuilder getOperationSignatureExtractor() {
		return this.operationNameBuilder;
	}

	public ComponentNameBuilder getComponentSignatureExtractor() {
		return this.componentNameBuilder;
	}

	public static NameBuilder of(final OperationNameBuilder operationNameBuilder, final ComponentNameBuilder componentNameBuilder) {
		return new NameBuilder(operationNameBuilder, componentNameBuilder);
	}

	public static NameBuilder forJavaShort() {
		return new NameBuilder(new JavaShortOperationNameBuilder(), new JavaShortComponentNameBuilder());
	}

	public static NameBuilder forJavaShortOperations() {
		return new NameBuilder(new JavaShortOperationNameBuilder(), new JavaFullComponentNameBuilder());
	}

	public static NameBuilder forJavaFull() {
		return new NameBuilder(new JavaFullOperationNameBuilder(), new JavaFullComponentNameBuilder());
	}

}
