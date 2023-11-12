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
 * @author Reiner Jung
 * @since 1.3.0
 */
public abstract class AbstractSignatureProcessor {

    protected boolean caseInsensitive;
    protected String componentSignature;
    protected String elementSignature;

    public AbstractSignatureProcessor(final boolean caseInsensitive) {
        this.caseInsensitive = caseInsensitive;
    }

    protected String removeTrailingUnderscore(final String string) {
        if (string.endsWith("_")) {
            return string.substring(0, string.length() - 1);
        } else {
            return string;
        }
    }

    protected String convertToLowerCase(final String string) {
        return this.caseInsensitive ? string.toLowerCase() : string; // NOCS NOPMD
    }

    public abstract String getErrorMessage();

    /**
     * Process component and element signature of an operation or storage.
     *
     * @param path
     *            file path of the element
     * @param componentSignature
     *            component signature of an element
     * @param elementSignature
     *            the element signature, i.e., operation or storage signature
     * @return true when the processing worked
     */
    public abstract boolean processSignatures(String path, String componentSignature, String elementSignature); // NOCS
                                                                                                                // hiding
                                                                                                                // fields

    public String getComponentSignature() {
        return this.componentSignature;
    }

    public String getElementSignature() {
        return this.elementSignature;
    }
}
