/***************************************************************************
 * Copyright 2022 Kieker Project (http://kieker-monitoring.net)
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
package kieker.analysis.architecture.recovery.signature;

/**
 * Do nothing processor. However, supports conversion to lower case.
 *
 * @author Reiner Jung
 * @since 1.15
 */
public class NullSignatureProcessor extends AbstractSignatureProcessor {

	private String componentSignature;
	private String operationSignature;

	public NullSignatureProcessor(final boolean caseInsensitive) {
		super(caseInsensitive);
	}

	@Override
	public void processSignatures(final String componentSignature, final String operationSignature) { // NOCS field name hiding
		this.componentSignature = this.convertToLowerCase(componentSignature);
		this.operationSignature = this.convertToLowerCase(operationSignature);
	}

	@Override
	public String getComponentSignature() {
		return this.componentSignature;
	}

	@Override
	public String getOperationSignature() {
		return this.operationSignature;
	}

}
