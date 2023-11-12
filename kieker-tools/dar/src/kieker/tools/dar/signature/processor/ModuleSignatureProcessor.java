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
package kieker.tools.dar.signature.processor;

import kieker.analysis.architecture.recovery.signature.AbstractSignatureProcessor;

/**
 * Process module information in signatures based on Fortran 95 code.
 *
 * @author Reiner Jung
 * @since 1.3.0
 *
 */
public class ModuleSignatureProcessor extends AbstractSignatureProcessor {

    public ModuleSignatureProcessor(final boolean caseInsensitive) {
        super(caseInsensitive);
    }

    @Override
    public void processSignatures(final String componentSignature, final String operationSignature) {
        final String[] values = operationSignature.split(this.caseInsensitive ? "_mod_" : "_MOD_");
        if (values.length == 2) {
            this.componentSignature = this
                    .convertToLowerCase(this.removeLeadingUnderscore(this.removeTrailingUnderscore(values[0])));
            this.operationSignature = this.convertToLowerCase(this.removeTrailingUnderscore(values[1]));
        } else {
            this.componentSignature = componentSignature;
            this.operationSignature = this.convertToLowerCase(this.removeTrailingUnderscore(operationSignature));
        }
    }

    private String removeLeadingUnderscore(final String signature) {
        if (signature.startsWith("_")) {
            return this.removeLeadingUnderscore(signature.substring(1));
        } else {
            return signature;
        }
    }

}
