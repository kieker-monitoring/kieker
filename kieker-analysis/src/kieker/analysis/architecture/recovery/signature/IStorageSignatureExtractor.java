/***************************************************************************
 * Copyright (C) 2022 OceanDSL (https://oceandsl.uni-kiel.de)
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

import kieker.model.analysismodel.type.StorageType;

/**
 * Signature extractor interface for storage references.
 *
 * @author Reiner Jung
 * @since 1.3.0
 */
public interface IStorageSignatureExtractor {

    /**
     * Extract information from the signature to set all other parameter of a storage type.
     *
     * @param storageType
     *            the storage type to be modified
     */
    void extract(StorageType storageType);

}
