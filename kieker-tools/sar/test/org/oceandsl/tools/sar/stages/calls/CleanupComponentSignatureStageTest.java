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
package org.oceandsl.tools.sar.stages.calls;

import java.util.ArrayList;
import java.util.List;

import org.hamcrest.MatcherAssert;
import org.junit.jupiter.api.Test;

import teetime.framework.test.StageTester;

import org.oceandsl.analysis.code.stages.data.CallerCalleeEntry;

import kieker.tools.sar.signature.processor.AbstractSignatureProcessor;
import kieker.tools.sar.signature.processor.FileBasedSignatureProcessor;
import kieker.tools.sar.stages.calls.CleanupComponentSignatureStage;

/**
 *
 * @author Reiner Jung
 * @since 1.3.0
 */
class CleanupComponentSignatureStageTest {

    private static final String SOURCE_PATH = "a/b/c";
    private static final String SOURCE_MODULE = "fish";
    private static final String CALLER = "caller()";
    private static final String TARGET_PATH = "a/b/d";
    private static final String TARGET_MODULE = "bicycle";
    private static final String CALLEE = "callee()";

    @Test
    void test() {
        final List<AbstractSignatureProcessor> processors = new ArrayList<>();

        processors.add(new FileBasedSignatureProcessor(true));

        final CleanupComponentSignatureStage stage = new CleanupComponentSignatureStage(processors);

        final CallerCalleeEntry cc = new CallerCalleeEntry(CleanupComponentSignatureStageTest.SOURCE_PATH,
                CleanupComponentSignatureStageTest.SOURCE_MODULE, CleanupComponentSignatureStageTest.CALLER,
                CleanupComponentSignatureStageTest.TARGET_PATH, CleanupComponentSignatureStageTest.TARGET_MODULE,
                CleanupComponentSignatureStageTest.CALLEE);

        final CallerCalleeEntry resultCC = new CallerCalleeEntry(CleanupComponentSignatureStageTest.SOURCE_PATH, "c",
                CleanupComponentSignatureStageTest.CALLER, CleanupComponentSignatureStageTest.TARGET_PATH, "d",
                CleanupComponentSignatureStageTest.CALLEE);

        StageTester.test(stage).send(cc).to(stage.getInputPort()).start();
        MatcherAssert.assertThat(stage.getOutputPort(), StageTester.produces(resultCC));
    }

}
