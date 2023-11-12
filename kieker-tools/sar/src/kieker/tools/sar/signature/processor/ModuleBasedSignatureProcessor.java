/***************************************************************************
 * Copyright (C) 2022 Kieker Project (https://kieker-monitoring.net)
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
package kieker.tools.sar.signature.processor;

/**
 * Process module information in signatures based on Fortran 95 code.
 *
 * @author Reiner Jung
 * @since 1.3.0
 *
 */
public class ModuleBasedSignatureProcessor extends AbstractSignatureProcessor {

    public ModuleBasedSignatureProcessor(final boolean caseInsensitive) {
        super(caseInsensitive);
    }

    @Override
    public boolean processSignatures(final String path, final String componentSignature,
            final String elementSignature) {
        this.elementSignature = this.convertToLowerCase(elementSignature);
        this.componentSignature = this.convertToLowerCase(componentSignature);
        // path is ignored
        return true;
    }

    @Override
    public String getErrorMessage() {
        return null;
    }
}
