/***************************************************************************
 * Copyright 2023 iObserve Project (https://www.iobserve-devops.net)
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
package kieker.analysis.behavior;

import java.util.ArrayList;
import java.util.List;

import org.hamcrest.MatcherAssert;
import org.junit.Assert;
import org.junit.Test;

import kieker.analysis.behavior.events.EntryCallEvent;
import kieker.common.record.flow.IFlowRecord;
import kieker.common.record.flow.trace.TraceMetadata;
import kieker.common.record.flow.trace.operation.AfterOperationEvent;
import kieker.common.record.flow.trace.operation.BeforeOperationEvent;
import kieker.common.record.flow.trace.operation.EntryLevelBeforeOperationEvent;

import teetime.framework.test.StageTester;

/**
 *
 * @author Reiner Jung
 * @since 2.0.0
 *
 */
public class CreateEntryLevelEventStageTest { // NOCS tests do not need constructors

	private static final long TRACE_ID = 1;
	private static final long THREAD_ID = 10;
	private static final String SESSION_ID = "<session>";
	private static final String HOSTNAME = "test-host";
	private static final long PARENT_TRACE_ID = -1;
	private static final int PARENT_ORDER_ID = -1;
	private static final String COMPONENT_1 = "component-1";
	private static final String COMPONENT_2 = "component-2";
	private static final String OP_1 = "op1()";
	private static final String OP_2 = "op2()";
	private static final String OP_3 = "op3()";

	private static final String[] POAYLOAD_PARAMETERS = new String[] { "p1", "p2" };
	private static final String[] PAYLOAD_VALUES = new String[] { "v1", "v2" };

	/** same start and end time, as we do not wait for the complete trace. */
	private final EntryCallEvent noWaitEntryCall = new EntryCallEvent(0, 0, CreateEntryLevelEventStageTest.OP_1, CreateEntryLevelEventStageTest.COMPONENT_1,
			CreateEntryLevelEventStageTest.SESSION_ID, CreateEntryLevelEventStageTest.HOSTNAME, new String[0], new String[0], 0);
	/** different start and end time, as we do not wait for the complete trace. */
	private final EntryCallEvent waitEntryCall = new EntryCallEvent(0, 10, CreateEntryLevelEventStageTest.OP_1, CreateEntryLevelEventStageTest.COMPONENT_1,
			CreateEntryLevelEventStageTest.SESSION_ID, CreateEntryLevelEventStageTest.HOSTNAME, new String[0], new String[0], 0);

	/** same start and end time, as we do not wait for the complete trace. */
	private final EntryCallEvent noWaitPayloadEntryCall = new EntryCallEvent(0, 0, CreateEntryLevelEventStageTest.OP_1, CreateEntryLevelEventStageTest.COMPONENT_1,
			CreateEntryLevelEventStageTest.SESSION_ID, CreateEntryLevelEventStageTest.HOSTNAME, CreateEntryLevelEventStageTest.POAYLOAD_PARAMETERS,
			CreateEntryLevelEventStageTest.PAYLOAD_VALUES, 0);
	/** different start and end time, as we do not wait for the complete trace. */
	private final EntryCallEvent waitPayloadEntryCall = new EntryCallEvent(0, 10, CreateEntryLevelEventStageTest.OP_1, CreateEntryLevelEventStageTest.COMPONENT_1,
			CreateEntryLevelEventStageTest.SESSION_ID, CreateEntryLevelEventStageTest.HOSTNAME, CreateEntryLevelEventStageTest.POAYLOAD_PARAMETERS,
			CreateEntryLevelEventStageTest.PAYLOAD_VALUES, 0);

	@Test
	public void testDoNotWaitForCompleteTraces() {
		final CreateEntryLevelEventStage stage = new CreateEntryLevelEventStage(false);

		StageTester.test(stage).and().send(this.createCorrectTrace()).to(stage.getInputPort()).start();

		MatcherAssert.assertThat(stage.getOutputPort(), StageTester.produces(this.noWaitEntryCall));
	}

	@Test
	public void testDoNotWaitForCompletePayloadTraces() {
		final CreateEntryLevelEventStage stage = new CreateEntryLevelEventStage(false);

		StageTester.test(stage).and().send(this.createCorrectPayloadTrace()).to(stage.getInputPort()).start();

		MatcherAssert.assertThat(stage.getOutputPort(), StageTester.produces(this.noWaitPayloadEntryCall));
	}

	@Test
	public void testDoNotWaitForIncompleteTraces1() {
		final CreateEntryLevelEventStage stage = new CreateEntryLevelEventStage(false);

		StageTester.test(stage).and().send(this.createIncompleteTrace1()).to(stage.getInputPort()).start();

		MatcherAssert.assertThat(stage.getOutputPort(), StageTester.produces(this.noWaitEntryCall));
	}

	@Test
	public void testDoNotWaitForWrongOrderTraces() {
		final CreateEntryLevelEventStage stage = new CreateEntryLevelEventStage(false);

		StageTester.test(stage).and().send(this.createWrongOrderTrace()).to(stage.getInputPort()).start();

		Assert.assertEquals(true, stage.getOutputPort().getPipe().isEmpty());
	}

	@Test
	public void testDoNotWaitForWrongOrderTraces2() {
		final CreateEntryLevelEventStage stage = new CreateEntryLevelEventStage(false);

		StageTester.test(stage).and().send(this.createWrongOrderTrace2()).to(stage.getInputPort()).start();

		Assert.assertEquals(true, stage.getOutputPort().getPipe().isEmpty());
	}

	@Test
	public void testDoWaitForCompleteTraces() {
		final CreateEntryLevelEventStage stage = new CreateEntryLevelEventStage(true);

		StageTester.test(stage).and().send(this.createCorrectTrace()).to(stage.getInputPort()).start();

		MatcherAssert.assertThat(stage.getOutputPort(), StageTester.produces(this.waitEntryCall));
	}

	@Test
	public void testDoWaitForCompletePayloadTraces() {
		final CreateEntryLevelEventStage stage = new CreateEntryLevelEventStage(true);

		StageTester.test(stage).and().send(this.createCorrectPayloadTrace()).to(stage.getInputPort()).start();

		MatcherAssert.assertThat(stage.getOutputPort(), StageTester.produces(this.waitPayloadEntryCall));
	}

	@Test
	public void testDoWaitForIncompleteTraces1() {
		final CreateEntryLevelEventStage stage = new CreateEntryLevelEventStage(true);

		StageTester.test(stage).and().send(this.createIncompleteTrace1()).to(stage.getInputPort()).start();

		Assert.assertEquals(true, stage.getOutputPort().getPipe().isEmpty());
	}

	@Test
	public void testDoWaitForIncompleteTraces2() {
		final CreateEntryLevelEventStage stage = new CreateEntryLevelEventStage(true);

		StageTester.test(stage).and().send(this.createIncompleteTrace2()).to(stage.getInputPort()).start();

		MatcherAssert.assertThat(stage.getOutputPort(), StageTester.produces(this.waitEntryCall));
	}

	@Test
	public void testDoWaitForWrongOrderTraces() {
		final CreateEntryLevelEventStage stage = new CreateEntryLevelEventStage(true);

		StageTester.test(stage).and().send(this.createWrongOrderTrace()).to(stage.getInputPort()).start();

		Assert.assertEquals(true, stage.getOutputPort().getPipe().isEmpty());
	}

	@Test
	public void testDoWaitForWrongOrderTraces2() {
		final CreateEntryLevelEventStage stage = new CreateEntryLevelEventStage(true);

		StageTester.test(stage).and().send(this.createWrongOrderTrace2()).to(stage.getInputPort()).start();

		Assert.assertEquals(true, stage.getOutputPort().getPipe().isEmpty());
	}

	private List<IFlowRecord> createCorrectTrace() {
		final List<IFlowRecord> trace = new ArrayList<>();
		trace.add(new TraceMetadata(CreateEntryLevelEventStageTest.TRACE_ID,
				CreateEntryLevelEventStageTest.THREAD_ID,
				CreateEntryLevelEventStageTest.SESSION_ID, CreateEntryLevelEventStageTest.HOSTNAME, CreateEntryLevelEventStageTest.PARENT_TRACE_ID,
				CreateEntryLevelEventStageTest.PARENT_ORDER_ID));
		trace.add(new BeforeOperationEvent(0, CreateEntryLevelEventStageTest.TRACE_ID, 0, CreateEntryLevelEventStageTest.OP_1,
				CreateEntryLevelEventStageTest.COMPONENT_1));
		trace.add(new BeforeOperationEvent(2, CreateEntryLevelEventStageTest.TRACE_ID, 1, CreateEntryLevelEventStageTest.OP_2,
				CreateEntryLevelEventStageTest.COMPONENT_1));
		trace.add(new BeforeOperationEvent(4, CreateEntryLevelEventStageTest.TRACE_ID, 2, CreateEntryLevelEventStageTest.OP_3,
				CreateEntryLevelEventStageTest.COMPONENT_2));
		trace.add(new AfterOperationEvent(6, CreateEntryLevelEventStageTest.TRACE_ID, 3, CreateEntryLevelEventStageTest.OP_3,
				CreateEntryLevelEventStageTest.COMPONENT_2));
		trace.add(new AfterOperationEvent(8, CreateEntryLevelEventStageTest.TRACE_ID, 4, CreateEntryLevelEventStageTest.OP_2,
				CreateEntryLevelEventStageTest.COMPONENT_1));
		trace.add(new AfterOperationEvent(10, CreateEntryLevelEventStageTest.TRACE_ID, 5, CreateEntryLevelEventStageTest.OP_1,
				CreateEntryLevelEventStageTest.COMPONENT_1));

		return trace;
	}

	private List<IFlowRecord> createCorrectPayloadTrace() {
		final List<IFlowRecord> trace = new ArrayList<>();
		trace.add(new TraceMetadata(CreateEntryLevelEventStageTest.TRACE_ID,
				CreateEntryLevelEventStageTest.THREAD_ID,
				CreateEntryLevelEventStageTest.SESSION_ID, CreateEntryLevelEventStageTest.HOSTNAME, CreateEntryLevelEventStageTest.PARENT_TRACE_ID,
				CreateEntryLevelEventStageTest.PARENT_ORDER_ID));
		trace.add(new EntryLevelBeforeOperationEvent(0, CreateEntryLevelEventStageTest.TRACE_ID, 0, CreateEntryLevelEventStageTest.OP_1,
				CreateEntryLevelEventStageTest.COMPONENT_1, CreateEntryLevelEventStageTest.POAYLOAD_PARAMETERS, CreateEntryLevelEventStageTest.PAYLOAD_VALUES, 1));
		trace.add(new BeforeOperationEvent(2, CreateEntryLevelEventStageTest.TRACE_ID, 1, CreateEntryLevelEventStageTest.OP_2,
				CreateEntryLevelEventStageTest.COMPONENT_1));
		trace.add(new BeforeOperationEvent(4, CreateEntryLevelEventStageTest.TRACE_ID, 2, CreateEntryLevelEventStageTest.OP_3,
				CreateEntryLevelEventStageTest.COMPONENT_2));
		trace.add(new AfterOperationEvent(6, CreateEntryLevelEventStageTest.TRACE_ID, 3, CreateEntryLevelEventStageTest.OP_3,
				CreateEntryLevelEventStageTest.COMPONENT_2));
		trace.add(new AfterOperationEvent(8, CreateEntryLevelEventStageTest.TRACE_ID, 4, CreateEntryLevelEventStageTest.OP_2,
				CreateEntryLevelEventStageTest.COMPONENT_1));
		trace.add(new AfterOperationEvent(10, CreateEntryLevelEventStageTest.TRACE_ID, 5, CreateEntryLevelEventStageTest.OP_1,
				CreateEntryLevelEventStageTest.COMPONENT_1));

		return trace;
	}

	private List<IFlowRecord> createIncompleteTrace1() {
		final List<IFlowRecord> trace = new ArrayList<>();
		trace.add(new TraceMetadata(CreateEntryLevelEventStageTest.TRACE_ID,
				CreateEntryLevelEventStageTest.THREAD_ID,
				CreateEntryLevelEventStageTest.SESSION_ID, CreateEntryLevelEventStageTest.HOSTNAME, CreateEntryLevelEventStageTest.PARENT_TRACE_ID,
				CreateEntryLevelEventStageTest.PARENT_ORDER_ID));
		trace.add(new BeforeOperationEvent(0, CreateEntryLevelEventStageTest.TRACE_ID, 0, CreateEntryLevelEventStageTest.OP_1,
				CreateEntryLevelEventStageTest.COMPONENT_1));
		trace.add(new BeforeOperationEvent(2, CreateEntryLevelEventStageTest.TRACE_ID, 1, CreateEntryLevelEventStageTest.OP_2,
				CreateEntryLevelEventStageTest.COMPONENT_1));
		trace.add(new BeforeOperationEvent(4, CreateEntryLevelEventStageTest.TRACE_ID, 2, CreateEntryLevelEventStageTest.OP_3,
				CreateEntryLevelEventStageTest.COMPONENT_2));
		trace.add(new AfterOperationEvent(6, CreateEntryLevelEventStageTest.TRACE_ID, 3, CreateEntryLevelEventStageTest.OP_3,
				CreateEntryLevelEventStageTest.COMPONENT_2));
		trace.add(new AfterOperationEvent(8, CreateEntryLevelEventStageTest.TRACE_ID, 4, CreateEntryLevelEventStageTest.OP_2,
				CreateEntryLevelEventStageTest.COMPONENT_1));

		return trace;

	}

	private List<IFlowRecord> createIncompleteTrace2() {
		final List<IFlowRecord> trace = new ArrayList<>();
		trace.add(new TraceMetadata(CreateEntryLevelEventStageTest.TRACE_ID,
				CreateEntryLevelEventStageTest.THREAD_ID,
				CreateEntryLevelEventStageTest.SESSION_ID, CreateEntryLevelEventStageTest.HOSTNAME, CreateEntryLevelEventStageTest.PARENT_TRACE_ID,
				CreateEntryLevelEventStageTest.PARENT_ORDER_ID));
		trace.add(new BeforeOperationEvent(0, CreateEntryLevelEventStageTest.TRACE_ID, 0, CreateEntryLevelEventStageTest.OP_1,
				CreateEntryLevelEventStageTest.COMPONENT_1));
		trace.add(new BeforeOperationEvent(2, CreateEntryLevelEventStageTest.TRACE_ID, 1, CreateEntryLevelEventStageTest.OP_2,
				CreateEntryLevelEventStageTest.COMPONENT_1));
		trace.add(new BeforeOperationEvent(4, CreateEntryLevelEventStageTest.TRACE_ID, 2, CreateEntryLevelEventStageTest.OP_3,
				CreateEntryLevelEventStageTest.COMPONENT_2));
		trace.add(new AfterOperationEvent(6, CreateEntryLevelEventStageTest.TRACE_ID, 3, CreateEntryLevelEventStageTest.OP_3,
				CreateEntryLevelEventStageTest.COMPONENT_2));
		trace.add(new AfterOperationEvent(10, CreateEntryLevelEventStageTest.TRACE_ID, 5, CreateEntryLevelEventStageTest.OP_1,
				CreateEntryLevelEventStageTest.COMPONENT_1));

		return trace;
	}

	private List<IFlowRecord> createWrongOrderTrace() {
		final List<IFlowRecord> trace = new ArrayList<>();
		trace.add(new BeforeOperationEvent(0, CreateEntryLevelEventStageTest.TRACE_ID, 0, CreateEntryLevelEventStageTest.OP_1,
				CreateEntryLevelEventStageTest.COMPONENT_1));
		trace.add(new TraceMetadata(CreateEntryLevelEventStageTest.TRACE_ID,
				CreateEntryLevelEventStageTest.THREAD_ID,
				CreateEntryLevelEventStageTest.SESSION_ID, CreateEntryLevelEventStageTest.HOSTNAME, CreateEntryLevelEventStageTest.PARENT_TRACE_ID,
				CreateEntryLevelEventStageTest.PARENT_ORDER_ID));
		trace.add(new BeforeOperationEvent(2, CreateEntryLevelEventStageTest.TRACE_ID, 1, CreateEntryLevelEventStageTest.OP_2,
				CreateEntryLevelEventStageTest.COMPONENT_1));
		trace.add(new BeforeOperationEvent(4, CreateEntryLevelEventStageTest.TRACE_ID, 2, CreateEntryLevelEventStageTest.OP_3,
				CreateEntryLevelEventStageTest.COMPONENT_2));
		trace.add(new AfterOperationEvent(6, CreateEntryLevelEventStageTest.TRACE_ID, 3, CreateEntryLevelEventStageTest.OP_3,
				CreateEntryLevelEventStageTest.COMPONENT_2));
		trace.add(new AfterOperationEvent(8, CreateEntryLevelEventStageTest.TRACE_ID, 4, CreateEntryLevelEventStageTest.OP_2,
				CreateEntryLevelEventStageTest.COMPONENT_1));
		trace.add(new AfterOperationEvent(10, CreateEntryLevelEventStageTest.TRACE_ID, 5, CreateEntryLevelEventStageTest.OP_1,
				CreateEntryLevelEventStageTest.COMPONENT_1));

		return trace;
	}

	private List<IFlowRecord> createWrongOrderTrace2() {
		final List<IFlowRecord> trace = new ArrayList<>();
		trace.add(new BeforeOperationEvent(0, CreateEntryLevelEventStageTest.TRACE_ID, 0, CreateEntryLevelEventStageTest.OP_1,
				CreateEntryLevelEventStageTest.COMPONENT_1));
		trace.add(new BeforeOperationEvent(2, CreateEntryLevelEventStageTest.TRACE_ID, 1, CreateEntryLevelEventStageTest.OP_2,
				CreateEntryLevelEventStageTest.COMPONENT_1));
		trace.add(new TraceMetadata(CreateEntryLevelEventStageTest.TRACE_ID,
				CreateEntryLevelEventStageTest.THREAD_ID,
				CreateEntryLevelEventStageTest.SESSION_ID, CreateEntryLevelEventStageTest.HOSTNAME, CreateEntryLevelEventStageTest.PARENT_TRACE_ID,
				CreateEntryLevelEventStageTest.PARENT_ORDER_ID));
		trace.add(new BeforeOperationEvent(4, CreateEntryLevelEventStageTest.TRACE_ID, 2, CreateEntryLevelEventStageTest.OP_3,
				CreateEntryLevelEventStageTest.COMPONENT_2));
		trace.add(new AfterOperationEvent(6, CreateEntryLevelEventStageTest.TRACE_ID, 3, CreateEntryLevelEventStageTest.OP_3,
				CreateEntryLevelEventStageTest.COMPONENT_2));
		trace.add(new AfterOperationEvent(8, CreateEntryLevelEventStageTest.TRACE_ID, 4, CreateEntryLevelEventStageTest.OP_2,
				CreateEntryLevelEventStageTest.COMPONENT_1));
		trace.add(new AfterOperationEvent(10, CreateEntryLevelEventStageTest.TRACE_ID, 5, CreateEntryLevelEventStageTest.OP_1,
				CreateEntryLevelEventStageTest.COMPONENT_1));

		return trace;
	}

}
