/***************************************************************************
 * Copyright 2022 Kieker Project (http://kieker-monitoring.net)
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
package kieker.analysis.stage.model;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import kieker.analysis.architecture.recovery.OperationAndCallGeneratorStage;
import kieker.analysis.architecture.recovery.data.CallEvent;
import kieker.analysis.architecture.recovery.data.OperationEvent;
import kieker.analysis.architecture.recovery.signature.AbstractSignatureProcessor;
import kieker.common.record.flow.IFlowRecord;
import kieker.common.record.flow.trace.TraceMetadata;
import kieker.common.record.flow.trace.operation.AfterOperationEvent;
import kieker.common.record.flow.trace.operation.BeforeOperationEvent;

import teetime.framework.test.StageTester;

/**
 * @author Reiner Jung
 * @since 1.15
 */
public class OperationAndCallGeneratorStageTest { // NOCS NOPMD

	private static final long TRACE_ID = 1;
	private static final long THREAD_ID = 1;
	private static final String SESSION_ID = "no-session";
	private static final String HOSTNAME = "hostname";
	private static final long PARENT_TRACE_ID = -1;
	private static final int PARENT_THREAD_ID = -1;
	private static final String OP_1 = "operation";
	private static final String UNDERSCORE_OP_1 = "operation_";
	private static final String CLASS_1 = "SomeClass";

	private static final String OP_2 = "other";
	private static final String UNDERSCORE_OP_2 = "other_";
	private static final String CLASS_2 = "OtherClass";

	private static final String UNKOWN = "<unknown>";
	private static final String EXTERNAL = "external";

	@Test
	public void nullNoExternalCallCleanupExecute() { // NOPMD
		final OperationAndCallGeneratorStage stage = new OperationAndCallGeneratorStage(false);
		final List<IFlowRecord> events = this.createPlainTrace();
		StageTester.test(stage).and().send(events).to(stage.getInputPort()).start();

		final OperationEvent o1 = new OperationEvent(HOSTNAME, CLASS_1, OP_1);
		final OperationEvent o2 = new OperationEvent(HOSTNAME, CLASS_2, OP_2);
		final CallEvent call = new CallEvent(o1, o2, Duration.of(1, ChronoUnit.NANOS));

		Assert.assertThat(stage.getOperationOutputPort(), StageTester.produces(o1, o2));
		Assert.assertThat(stage.getCallOutputPort(), StageTester.produces(call));
	}

	@Test
	public void nullWithExternalCallCleanupExecute() { // NOPMD
		final OperationAndCallGeneratorStage stage = new OperationAndCallGeneratorStage(true);
		final List<IFlowRecord> events = this.createPlainTrace();
		StageTester.test(stage).and().send(events).to(stage.getInputPort()).start();

		final OperationEvent o0 = new OperationEvent(EXTERNAL, UNKOWN, UNKOWN);
		final OperationEvent o1 = new OperationEvent(HOSTNAME, CLASS_1, OP_1);
		final OperationEvent o2 = new OperationEvent(HOSTNAME, CLASS_2, OP_2);

		final CallEvent call1 = new CallEvent(o0, o1, Duration.of(3, ChronoUnit.NANOS));
		final CallEvent call2 = new CallEvent(o1, o2, Duration.of(1, ChronoUnit.NANOS));

		Assert.assertThat(stage.getOperationOutputPort(), StageTester.produces(o0, o1, o2));
		// calls are returned in reverse, as only completed calls are returned
		Assert.assertThat(stage.getCallOutputPort(), StageTester.produces(call2, call1));
	}

	private List<IFlowRecord> createPlainTrace() {
		int time = 0;
		int orderIndex = 0;
		final List<IFlowRecord> events = new ArrayList<>();
		events.add(new TraceMetadata(TRACE_ID, THREAD_ID, SESSION_ID, HOSTNAME, PARENT_TRACE_ID, PARENT_THREAD_ID));
		events.add(new BeforeOperationEvent(time++, TRACE_ID, orderIndex++, OP_1, CLASS_1));
		events.add(new BeforeOperationEvent(time++, TRACE_ID, orderIndex++, OP_2, CLASS_2));
		events.add(new AfterOperationEvent(time++, TRACE_ID, orderIndex++, OP_2, CLASS_2));
		events.add(new AfterOperationEvent(time++, TRACE_ID, orderIndex++, OP_1, CLASS_1));
		return events;
	}

	@Test
	public void specialCaseSensitiveCleanupExecute() { // NOPMD
		this.specialCleanupExecute(false);
	}

	@Test
	public void specialCaseInsensitiveCleanupExecute() { // NOPMD
		this.specialCleanupExecute(true);
	}

	private void specialCleanupExecute(final boolean caseInsenstive) {
		final OperationAndCallGeneratorStage stage = new OperationAndCallGeneratorStage(false,
				this.createProcessor(caseInsenstive));
		final List<IFlowRecord> events = this.createPathClassTrace();
		StageTester.test(stage).and().send(events).to(stage.getInputPort()).start();

		final OperationEvent o1 = new OperationEvent(HOSTNAME, caseInsenstive ? CLASS_1.toLowerCase() : CLASS_1, OP_1); // NOCS NOPMD
		final OperationEvent o2 = new OperationEvent(HOSTNAME, caseInsenstive ? CLASS_2.toLowerCase() : CLASS_2, OP_2); // NOCS NOPMD
		final CallEvent call = new CallEvent(o1, o2, Duration.of(1, ChronoUnit.NANOS));

		Assert.assertThat(stage.getOperationOutputPort(), StageTester.produces(o1, o2));
		Assert.assertThat(stage.getCallOutputPort(), StageTester.produces(call));
	}

	private List<IFlowRecord> createPathClassTrace() {
		int time = 0;
		int orderIndex = 0;
		final List<IFlowRecord> events = new ArrayList<>();
		events.add(new TraceMetadata(TRACE_ID, THREAD_ID, SESSION_ID, HOSTNAME, PARENT_TRACE_ID, PARENT_THREAD_ID));
		events.add(new BeforeOperationEvent(time++, TRACE_ID, orderIndex++, UNDERSCORE_OP_1, CLASS_1));
		events.add(new BeforeOperationEvent(time++, TRACE_ID, orderIndex++, UNDERSCORE_OP_2, CLASS_2));
		events.add(new AfterOperationEvent(time++, TRACE_ID, orderIndex++, UNDERSCORE_OP_2, CLASS_2));
		events.add(new AfterOperationEvent(time++, TRACE_ID, orderIndex++, UNDERSCORE_OP_1, CLASS_1));

		return events;
	}

	private AbstractSignatureProcessor createProcessor(final boolean caseInsensitive) {
		return new AbstractSignatureProcessor(caseInsensitive) {

			private String componentSignature;
			private String operationSignature;

			@Override
			public void processSignatures(final String componentSignature, final String operationSignature) {
				this.componentSignature = this.convertToLowerCase(this.removeTrailingUnderscore(componentSignature));
				this.operationSignature = this.convertToLowerCase(this.removeTrailingUnderscore(operationSignature));
			}

			@Override
			public String getComponentSignature() {
				return this.componentSignature;
			}

			@Override
			public String getOperationSignature() {
				return this.operationSignature;
			}

		};
	}

}
