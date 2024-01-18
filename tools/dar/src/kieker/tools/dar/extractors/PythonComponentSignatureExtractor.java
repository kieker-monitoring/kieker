/***************************************************************************
 * Copyright (C) 2021 Kieker Project (https://kieker-monitoring.net)
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
package kieker.tools.dar.extractors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import kieker.analysis.architecture.recovery.signature.IComponentSignatureExtractor;
import kieker.analysis.code.CodeUtils;
import kieker.model.analysismodel.type.ComponentType;

/**
 * Extract component signatures from Python classnames.
 *
 * @author Reiner Jung
 * @since 2.0.0
 */
public class PythonComponentSignatureExtractor implements IComponentSignatureExtractor {

	private static final Logger LOGGER = LoggerFactory.getLogger(PythonComponentSignatureExtractor.class);

	@Override
	public void extract(final ComponentType componentType) {
		String signature = componentType.getSignature();
		if (signature == null) {
			signature = "-- none --";
		}

		if (CodeUtils.UNKNOWN_COMPONENT.equals(signature)) {
			componentType.setName(signature);
			componentType.setPackage(CodeUtils.NO_PACKAGE);
		} else {
			final int lastIndex = signature.lastIndexOf('.');
			if (lastIndex < 0) {
				componentType.setName(signature);
				componentType.setPackage("");
				PythonComponentSignatureExtractor.LOGGER.warn("Component without package name {}", signature);
			} else {
				componentType.setName(signature.substring(lastIndex + 1));
				componentType.setPackage(signature.substring(0, lastIndex - 1));
			}
		}
	}

}
