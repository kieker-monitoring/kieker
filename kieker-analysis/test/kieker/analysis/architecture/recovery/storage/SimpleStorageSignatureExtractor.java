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

import kieker.model.analysismodel.type.StorageType;

/**
 * Dummy signature extractor for storage signatures.
 *
 * @author Reiner Jung
 * @since 1.3.0
 */
public class SimpleStorageSignatureExtractor implements IStorageSignatureExtractor {

	/**
	 * Dummy function used in tests to support the {@link IStorageSignatureExtractor} interface.
	 */
	@Override
	public void extract(final StorageType storageType) {
		// nothing to be done here, as this is used testing {@link StorageTypeModelAssembler}
	}

}
