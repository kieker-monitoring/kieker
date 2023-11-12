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
package org.oceandsl.tools.maa.stages;

import lombok.Getter;

/**
 * Call entry.
 *
 * @author Reiner Jung
 * @since 2.0.0
 */
public class CallEntry {

    @Getter
    private final String callerComponent;
    @Getter
    private final String caller;
    @Getter
    private final String calleeComponent;
    @Getter
    private final String callee;
    @Getter
    private final int numOfCalls;

    public CallEntry(final String callerComponent, final String caller, final String calleeComponent,
            final String callee, final int numOfCalls) {
        this.callerComponent = callerComponent;
        this.caller = caller;
        this.calleeComponent = calleeComponent;
        this.callee = callee;
        this.numOfCalls = numOfCalls;
    }

}
