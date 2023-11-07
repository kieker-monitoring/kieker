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
package org.oceandsl.tools.sar;

import lombok.Getter;
import lombok.Setter;

import kieker.model.analysismodel.execution.EDirection;

/**
 * @author Reiner Jung
 * @since 1.3.0
 *
 */
public class CallerCalleeDataflow {

    @Getter
    private final String sourceFileName;

    @Getter
    private final String sourceModuleName;

    @Getter
    private final String sourceOperationName;

    @Getter
    private final String targetFileName;

    @Getter
    private final String targetModuleName;

    @Getter
    private final String targetOperatioName;

    @Getter
    @Setter
    private EDirection direction; // NOPMD pmd does not understand lombok

    public CallerCalleeDataflow(final String sourceFileName, final String sourceModuleName,
            final String sourceOperationName, final String targetFileName, final String targetModuleName,
            final String targetOperatioName, final EDirection direction) {
        this.sourceFileName = sourceFileName;
        this.sourceModuleName = sourceModuleName;
        this.sourceOperationName = sourceOperationName;
        this.targetFileName = targetFileName;
        this.targetModuleName = targetModuleName;
        this.targetOperatioName = targetOperatioName;
        this.direction = direction;
    }

}
