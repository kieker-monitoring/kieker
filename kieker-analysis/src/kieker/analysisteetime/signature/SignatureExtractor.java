/***************************************************************************
 * Copyright 2015 Kieker Project (http://kieker-monitoring.net)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ***************************************************************************/
package kieker.analysisteetime.signature;

/**
 * This is a wrapper class around a {@link OperationSignatureExtractor} and a {@link ComponentSignatureExtractor}.
 *
 * @author Sören Henning
 *
 * @since 1.13
 *
 */
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

	public static SignatureExtractor of(final OperationSignatureExtractor operationSignatureExtractor, // NOPMD
			final ComponentSignatureExtractor componentSignatureExtractor) {
		return new SignatureExtractor(operationSignatureExtractor, componentSignatureExtractor);
	}

	public static SignatureExtractor forJava() {
		return new SignatureExtractor(new JavaOperationSignatureExtractor(), new JavaComponentSignatureExtractor());
	}

}
