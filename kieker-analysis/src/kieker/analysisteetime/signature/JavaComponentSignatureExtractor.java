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

import kieker.analysisteetime.model.analysismodel.type.ComponentType;

/**
 * @author Sören Henning
 *
 * @since 1.13
 */
public class JavaComponentSignatureExtractor implements ComponentSignatureExtractor {

	@Override
	public void extract(final ComponentType componentType) {
		// TODO code clean up
		final String signature = componentType.getSignature();
		
		String packageName;
		String name;
		if (signature.indexOf('.') != -1) {
			final String tmpComponentName = signature;
			int index = 0;
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
