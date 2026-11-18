/***************************************************************************
 * Copyright (C) 2026 Kieker Project (https://kieker-monitoring.net)
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
package kieker.tools.otelfransformer.stages;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import kieker.common.record.flow.trace.TraceMetadata;
import kieker.common.record.flow.trace.operation.AfterOperationEvent;
import kieker.common.record.flow.trace.operation.BeforeOperationEvent;
import kieker.tools.oteltransformer.stages.FlowTraceCollector;
import kieker.tools.oteltransformer.stages.OTelExecution;
import teetime.framework.test.StageTester;

/**
 * 
 */
public class FlowTraceCollectorTest {

	private static final long TRACE_ID = 1;
	private static final long THREAD_ID = 5;
	private static final String SESSION_ID = "test-session";
	private static final String HOSTNAME = "test-hostname";
	private static final long PARENT_TRACE_ID = -1;
	private static final int PARENT_ORDER_ID = -2;
	private static final String OP_1 = "int package.MyClass.op1(int i)";
	private static final String OP_2 = "int package.MyClass.op2(int i)";
	private static final String OP_3 = "int package.MyClass.op3(int i)";
	private static final String OP_4 = "int package.MyClass.op4(int i)";
	private static final String CLASS_1 = "package.MyFirstClass";
	private static final String CLASS_2 = "package.MySecondClass";

	@Test
	public void test() {
		FlowTraceCollector stage = new FlowTraceCollector();
		
		TraceMetadata metadata = new TraceMetadata(TRACE_ID, THREAD_ID, SESSION_ID, HOSTNAME, PARENT_TRACE_ID, PARENT_ORDER_ID);
		long timestamp = 10;
		int orderIndex = 0;
		BeforeOperationEvent b1 = new BeforeOperationEvent(timestamp++, TRACE_ID, orderIndex++, OP_1, CLASS_1);

		BeforeOperationEvent b2 = new BeforeOperationEvent(timestamp++, TRACE_ID, orderIndex++, OP_2, CLASS_1);
		AfterOperationEvent a2 = new AfterOperationEvent(timestamp++, TRACE_ID, orderIndex++, OP_2, CLASS_1);

		BeforeOperationEvent b3 = new BeforeOperationEvent(timestamp++, TRACE_ID, orderIndex++, OP_3, CLASS_2);
		BeforeOperationEvent b4 = new BeforeOperationEvent(timestamp++, TRACE_ID, orderIndex++, OP_4, CLASS_2);
		AfterOperationEvent a4 = new AfterOperationEvent(timestamp++, TRACE_ID, orderIndex++, OP_4, CLASS_2);
		AfterOperationEvent a3 = new AfterOperationEvent(timestamp++, TRACE_ID, orderIndex++, OP_3, CLASS_2);
		AfterOperationEvent a1 = new AfterOperationEvent(timestamp++, TRACE_ID, orderIndex++, OP_1, CLASS_1);
		
		List<OTelExecution> results = new ArrayList<>();
		results.add(new OTelExecution(HOSTNAME, CLASS_1, OP_2, SESSION_ID, 11, 12, 1, 1));
		results.add(new OTelExecution(HOSTNAME, CLASS_2, OP_4, SESSION_ID, 13, 16, 2, 4));
		results.add(new OTelExecution(HOSTNAME, CLASS_2, OP_3, SESSION_ID, 14, 15, 1, 3));
		results.add(new OTelExecution(HOSTNAME, CLASS_1, OP_1, SESSION_ID, 10, 17, 0, 0));
				
		List<List<OTelExecution>> r = new ArrayList<List<OTelExecution>>();
		// r.add(results);
		
		StageTester.test(stage).send(metadata, b1, b2, a2, b3, b4, a4, a3, a1).to(stage.getInputPort()).and().
			receive(r).from(stage.getOutputPort()).start();
		
		int i = 0;
		for (OTelExecution execution : r.get(0)) {
			OTelExecution expected = results.get(i++);
			this.compare(execution, expected);
		}
	}
	
	private void compare(OTelExecution received, OTelExecution expected) {
		Assertions.assertEquals(expected.getHostname(), received.getHostname());
		Assertions.assertEquals(expected.getSessionId(), received.getSessionId());
		Assertions.assertEquals(expected.getClassSignature(), received.getClassSignature());
		Assertions.assertEquals(expected.getOperationSignature(), received.getOperationSignature());
		Assertions.assertEquals(expected.getEoi(), received.getEoi());
		Assertions.assertEquals(expected.getEss(), received.getEss());
		Assertions.assertEquals(expected.getTin(), received.getTin());
		Assertions.assertEquals(expected.getTout(), received.getTout());
		
	}

}
