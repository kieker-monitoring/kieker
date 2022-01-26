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

package kieker.analysis.signature;

import kieker.common.util.signature.ClassOperationSignaturePair;
import kieker.common.util.signature.Signature;
import kieker.model.analysismodel.type.OperationType;

/**
 * A {@link IOperationSignatureExtractor} that uses the Java signature style.
 *
 * @author Sören Henning
 *
 * @since 1.14
 */
public class JavaOperationSignatureExtractor implements IOperationSignatureExtractor {

	public JavaOperationSignatureExtractor() {
		// create extractor
	}

	@Override
	public void extract(final OperationType operationType) {
		// BETTER Implement new and do not rely on the class in kieker.common
		final ClassOperationSignaturePair classSignaturePair = ClassOperationSignaturePair.splitOperationSignatureStr(operationType.getSignature());
		final Signature signature = classSignaturePair.getSignature();

		operationType.setName(signature.getName());
		operationType.setReturnType(signature.getReturnType());
		for (final String modifier : signature.getModifier()) {
			if (!modifier.isEmpty()) {
				operationType.getModifiers().add(modifier);
			}
		}
		for (final String parameterType : signature.getParamTypeList()) {
			if (!parameterType.isEmpty()) {
				operationType.getParameterTypes().add(parameterType);
			}
		}
	}

}
