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
package org.oceandsl.tools.dar.stages;

/**
 * @author Reiner Jung
 * @since 1.3.0
 */
public class SignatureProcessor {

    private String classSignature;
    private String operationSignature;

    public void parse(final String signature) {
        final String[] parts = signature.split("\\(");
        final String[] leftSideParts = parts[0].split(" ");
        final String fqnOperationName = leftSideParts[leftSideParts.length - 1];
        final int separatorPosition = fqnOperationName.lastIndexOf('.');
        this.classSignature = fqnOperationName.substring(0, separatorPosition);
        this.operationSignature = "";
        for (int i = 0; i < (leftSideParts.length - 1); i++) {
            this.operationSignature += leftSideParts[i] + " ";
        }
        this.operationSignature += fqnOperationName.substring(separatorPosition + 1) + "(" + parts[1];
    }

    public String getClassSignature() {
        return this.classSignature;
    }

    public String getOperationSignature() {
        return this.operationSignature;
    }

}
