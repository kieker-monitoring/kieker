/***************************************************************************
 * Copyright (C) 2023 Kieker Project (https://kieker-monitoring.net)
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
package kiekerl.tools.sar.stages;

import java.time.Duration;

import org.hamcrest.MatcherAssert;
import org.junit.jupiter.api.Test;

import kieker.analysis.architecture.recovery.events.DataflowEvent;
import kieker.analysis.architecture.recovery.events.OperationEvent;
import kieker.model.analysismodel.execution.EDirection;
import kieker.tools.sar.stages.DataflowConstraintStage;

import teetime.framework.test.StageTester;

class DataflowConstraintStageTest {

    private static final String HOSTNAME = "test-host";
    private static final String COMPONENT = "TestComponent";
    private static final String OPERATION_1 = "op1()";
    private static final String OPERATION_2 = "op2()";
    private static final String OPERATION_3 = "op3()";

    /**
     * Test whether all {@link DataflowEvent}s pass when all @{link {@link OperationEvent}s have
     * been received.
     */
    @Test
    void testIfAllDataflowPass() {
        final DataflowConstraintStage stage = new DataflowConstraintStage();

        final OperationEvent e1 = new OperationEvent(DataflowConstraintStageTest.HOSTNAME,
                DataflowConstraintStageTest.COMPONENT, DataflowConstraintStageTest.OPERATION_1);
        final OperationEvent e2 = new OperationEvent(DataflowConstraintStageTest.HOSTNAME,
                DataflowConstraintStageTest.COMPONENT, DataflowConstraintStageTest.OPERATION_2);
        final OperationEvent e3 = new OperationEvent(DataflowConstraintStageTest.HOSTNAME,
                DataflowConstraintStageTest.COMPONENT, DataflowConstraintStageTest.OPERATION_3);

        final DataflowEvent d1 = new DataflowEvent(e1, e2, EDirection.WRITE, Duration.ZERO);
        final DataflowEvent d2 = new DataflowEvent(e2, e3, EDirection.WRITE, Duration.ZERO);

        StageTester.test(stage).and().send(d1, d2).to(stage.getInputPort()).send(e1, e2, e3)
                .to(stage.getControlInputPort()).start();
        MatcherAssert.assertThat(stage.getOutputPort(), StageTester.produces(d1, d2));
    }

    /**
     * Test whether one {@link DataflowEvent} passes and one is held back, as not enough
     * {@link OperationEvent}s have been received.
     */
    @Test
    void testIfOnlyOneDataflowPasses() {
        final DataflowConstraintStage stage = new DataflowConstraintStage();

        final OperationEvent e1 = new OperationEvent(DataflowConstraintStageTest.HOSTNAME,
                DataflowConstraintStageTest.COMPONENT, DataflowConstraintStageTest.OPERATION_1);
        final OperationEvent e2 = new OperationEvent(DataflowConstraintStageTest.HOSTNAME,
                DataflowConstraintStageTest.COMPONENT, DataflowConstraintStageTest.OPERATION_2);
        final OperationEvent e3 = new OperationEvent(DataflowConstraintStageTest.HOSTNAME,
                DataflowConstraintStageTest.COMPONENT, DataflowConstraintStageTest.OPERATION_3);

        final DataflowEvent d1 = new DataflowEvent(e1, e2, EDirection.WRITE, Duration.ZERO);
        final DataflowEvent d2 = new DataflowEvent(e2, e3, EDirection.WRITE, Duration.ZERO);

        StageTester.test(stage).and().send(d1, d2).to(stage.getInputPort()).send(e1, e2).to(stage.getControlInputPort())
                .start();
        MatcherAssert.assertThat(stage.getOutputPort(), StageTester.produces(d1));
    }

}
