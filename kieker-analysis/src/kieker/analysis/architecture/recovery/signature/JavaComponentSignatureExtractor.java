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

import kieker.model.analysismodel.type.ComponentType;

/**
 * A {@link IComponentSignatureExtractor} that uses the Java signature style.
 *
 * @author Sören Henning
 *
 * @since 1.14
 */
public class JavaComponentSignatureExtractor implements IComponentSignatureExtractor {

	public JavaComponentSignatureExtractor() {
		// create extractor
	}

	@Override
	public void extract(final ComponentType componentType) {
		// BETTER code clean up, copied from old kieker analysis
		final String signature = componentType.getSignature();

		final String packageName;
		final String name;
		if (signature.indexOf('.') != -1) {
			final String tmpComponentName = signature;
			int index;
			for (index = tmpComponentName.length() - 1; index > 0; index--) {
				if (tmpComponentName.charAt(index) == '.') {
					break;
				}
			}
			packageName = tmpComponentName.substring(0, index);
			name = tmpComponentName.substring(index + 1);
		} else {
			packageName = "";
			name = signature;
		}

		componentType.setName(name);
		componentType.setPackage(packageName);
	}

}
