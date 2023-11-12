/***************************************************************************
 * Copyright (C) 2021 OceanDSL (https://oceandsl.uni-kiel.de)
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
package kieker.tools.dar.signature.processor;

import java.nio.file.Path;
import java.nio.file.Paths;

import kieker.analysis.architecture.recovery.signature.AbstractSignatureProcessor;
import kieker.analysis.code.CodeUtils;

/**
 * @author Reiner Jung
 * @since 1.1
 */
public class FileBasedSignatureProcessor extends AbstractSignatureProcessor {

	public FileBasedSignatureProcessor(final boolean caseInsensitive) {
		super(caseInsensitive);
	}

	@Override
	public void processSignatures(final String componentSignature, final String operationSignature) {
		if (CodeUtils.NO_FILE.equals(componentSignature)) {
			this.componentSignature = componentSignature;
		} else {
			final Path path = Paths.get(componentSignature);
			this.componentSignature = this.convertToLowerCase(
					this.removeTrailingUnderscore(path.getName(path.getNameCount() - 1).toString()));
		}

		this.operationSignature = this.convertToLowerCase(this.removeTrailingUnderscore(operationSignature));
	}

}
