/***************************************************************************
 * Copyright 2023 Kieker Project (http://kieker-monitoring.net)
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
package kieker.analysis.architecture.trace.flow;

import org.junit.Assert;
import org.junit.Test;

import kieker.common.record.IMonitoringRecord;

import kieker.test.analysis.util.stage.BookstoreEventRecordFactory;

import teetime.framework.test.StageTester;

public class ThreadEvent2TraceEventStageTest { // NOCS test do not need constructors

	@Test
	public void testTrace() {
		final ThreadEvent2TraceEventStage stage = new ThreadEvent2TraceEventStage();

		// TODO this might be useless, as it does not contain any *ThreadEvents
		final TraceEventRecords trace = BookstoreEventRecordFactory.validSyncTraceBeforeAfterEvents(0, 1, "test-session", "test-host");
		final IMonitoringRecord[] elements = new IMonitoringRecord[1 + trace.getTraceEvents().length];
		elements[0] = trace.getTraceMetadata();
		for (int i = 0; i < trace.getTraceEvents().length; i++) {
			elements[i + 1] = trace.getTraceEvents()[i];
		}

		StageTester.test(stage).send(elements).to(stage.getInputPort()).start();

		Assert.assertThat(stage.getOutputPort(), StageTester.produces(elements));
	}

}
