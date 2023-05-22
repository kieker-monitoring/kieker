/***************************************************************************
 * Copyright (C) 2023 OceanDSL (https://oceandsl.uni-kiel.de)
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
package kieker.analysis.architecture.recovery.storage;

import kieker.analysis.architecture.recovery.signature.IComponentSignatureExtractor;
import kieker.model.analysismodel.type.ComponentType;

/**
 * Simple signature extractor used in test cases.
 *
 * @author Reiner Jung
 * @since 1.3.0
 */
public class SimpleComponentSignatureExtractor implements IComponentSignatureExtractor {

	/**
	 * Extract component name and fully qualified package name from the component signature.
	 *
	 * @param componentType
	 *            component type with a signature.
	 */
	@Override
	public void extract(final ComponentType componentType) {
		final String signature = componentType.getSignature();
		componentType.setName(signature.substring(signature.lastIndexOf('.')));
		componentType.setPackage(signature.substring(0, signature.lastIndexOf('.')));
	}
}
