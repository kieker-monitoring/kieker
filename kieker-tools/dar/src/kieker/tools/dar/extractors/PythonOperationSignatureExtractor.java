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

import kieker.analysis.architecture.recovery.signature.IOperationSignatureExtractor;
import kieker.model.analysismodel.type.OperationType;

/**
 * Extract and parse python operation names.
 *
 * @author Reiner Jung
 * @since 2.0.0
 */
public class PythonOperationSignatureExtractor implements IOperationSignatureExtractor {

    @Override
    public void extract(final OperationType operationType) {
        // TODO this will be wrong when other parts are fixed.
        final String name = operationType.getSignature();
        operationType.setName(name);
        operationType.setReturnType("unknown");
    }

}