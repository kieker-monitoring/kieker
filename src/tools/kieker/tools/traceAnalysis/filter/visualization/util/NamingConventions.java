package kieker.tools.traceAnalysis.filter.visualization.util;

import kieker.common.util.signature.Signature;
import kieker.tools.traceAnalysis.systemModel.Operation;

public class NamingConventions {

	private NamingConventions() {
		// utility class
	}

	public static String createOperationSignature(final Operation operation) {
		final StringBuilder builder = new StringBuilder();
		final Signature signature = operation.getSignature();

		builder.append(signature.getName());
		builder.append('(');

		final String[] parameterTypes = signature.getParamTypeList();
		if (parameterTypes.length > 0) { // // parameterTypes cannot be null (getParamTypeList never returns null)
			builder.append("..");
		}
		builder.append(')');

		return builder.toString();
	}
}
