/***************************************************************************
 * Copyright 2014 Kieker Project (http://kieker-monitoring.net)
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

package kieker.test.common.junit.record.flow.trace.operation.constructor;

import java.nio.ByteBuffer;

import org.junit.Assert;
import org.junit.Test;

import kieker.common.record.flow.trace.operation.constructor.AfterConstructorEvent;
import kieker.common.util.registry.IRegistry;
import kieker.common.util.registry.Registry;

import kieker.test.common.junit.AbstractKiekerTest;
import kieker.test.common.junit.TestValueRangeConstants;
import kieker.test.common.util.record.BookstoreOperationExecutionRecordFactory;
		
/**
 * Creates {@link OperationExecutionRecord}s via the available constructors and
 * checks the values passed values via getters.
 * 
 * @author Generic Kieker
 * 
 * @since 1.10
 */
public class TestGeneratedAfterConstructorEvent extends AbstractKiekerTest {

	public TestGeneratedAfterConstructorEvent() {
		// empty default constructor
	}

	/**
	 * Tests {@link AfterConstructorEvent#TestAfterConstructorEvent(String, String, long, long, long, String, int, int)}.
	 */
	@Test
	public void testToArray() { // NOPMD (assert missing)
	for (int i=0;i<TestValueRangeConstants.ARRAY_LENGTH;i++) {
			// initialize
			AfterConstructorEvent record = new AfterConstructorEvent(TestValueRangeConstants.LONG_VALUES[i%TestValueRangeConstants.LONG_VALUES.length], TestValueRangeConstants.LONG_VALUES[i%TestValueRangeConstants.LONG_VALUES.length], TestValueRangeConstants.INT_VALUES[i%TestValueRangeConstants.INT_VALUES.length], TestValueRangeConstants.STRING_VALUES[i%TestValueRangeConstants.STRING_VALUES.length], TestValueRangeConstants.STRING_VALUES[i%TestValueRangeConstants.STRING_VALUES.length]);
			
			// check values
			Assert.assertEquals("AfterConstructorEvent.timestamp values are not equal.", TestValueRangeConstants.LONG_VALUES[i%TestValueRangeConstants.LONG_VALUES.length], record.getTimestamp());
			Assert.assertEquals("AfterConstructorEvent.traceId values are not equal.", TestValueRangeConstants.LONG_VALUES[i%TestValueRangeConstants.LONG_VALUES.length], record.getTraceId());
			Assert.assertEquals("AfterConstructorEvent.orderIndex values are not equal.", TestValueRangeConstants.INT_VALUES[i%TestValueRangeConstants.INT_VALUES.length], record.getOrderIndex());
			Assert.assertEquals("AfterConstructorEvent.classSignature values are not equal.", TestValueRangeConstants.STRING_VALUES[i%TestValueRangeConstants.STRING_VALUES.length] == null?"":TestValueRangeConstants.STRING_VALUES[i%TestValueRangeConstants.STRING_VALUES.length], record.getClassSignature());
			Assert.assertEquals("AfterConstructorEvent.operationSignature values are not equal.", TestValueRangeConstants.STRING_VALUES[i%TestValueRangeConstants.STRING_VALUES.length] == null?"":TestValueRangeConstants.STRING_VALUES[i%TestValueRangeConstants.STRING_VALUES.length], record.getOperationSignature());
			
			Object[] values = record.toArray();
			
			Assert.assertNotNull("Record array serialization failed. No values array returned.", values);
			Assert.assertEquals("Record array size does not match expected number of properties 5.", 5, values.length);
			
			// check all object values exist
			Assert.assertNotNull("Array value [0] of type Long must be not null.", values[0]); 
			Assert.assertNotNull("Array value [1] of type Long must be not null.", values[1]); 
			Assert.assertNotNull("Array value [2] of type Integer must be not null.", values[2]); 
			Assert.assertNotNull("Array value [3] of type String must be not null.", values[3]); 
			Assert.assertNotNull("Array value [4] of type String must be not null.", values[4]); 
			
			// check all types
			Assert.assertTrue("Type of array value [0] " + values[0].getClass().getCanonicalName() + " does not match the desired type Long", values[0] instanceof Long);
			Assert.assertTrue("Type of array value [1] " + values[1].getClass().getCanonicalName() + " does not match the desired type Long", values[1] instanceof Long);
			Assert.assertTrue("Type of array value [2] " + values[2].getClass().getCanonicalName() + " does not match the desired type Integer", values[2] instanceof Integer);
			Assert.assertTrue("Type of array value [3] " + values[3].getClass().getCanonicalName() + " does not match the desired type String", values[3] instanceof String);
			Assert.assertTrue("Type of array value [4] " + values[4].getClass().getCanonicalName() + " does not match the desired type String", values[4] instanceof String);
								
			// check all object values 
			Assert.assertEquals("Array value [0] " + values[0] + " does not match the desired value " + TestValueRangeConstants.LONG_VALUES[i%TestValueRangeConstants.LONG_VALUES.length],
				TestValueRangeConstants.LONG_VALUES[i%TestValueRangeConstants.LONG_VALUES.length], (long) (Long)values[0]
					);
			Assert.assertEquals("Array value [1] " + values[1] + " does not match the desired value " + TestValueRangeConstants.LONG_VALUES[i%TestValueRangeConstants.LONG_VALUES.length],
				TestValueRangeConstants.LONG_VALUES[i%TestValueRangeConstants.LONG_VALUES.length], (long) (Long)values[1]
					);
			Assert.assertEquals("Array value [2] " + values[2] + " does not match the desired value " + TestValueRangeConstants.INT_VALUES[i%TestValueRangeConstants.INT_VALUES.length],
				TestValueRangeConstants.INT_VALUES[i%TestValueRangeConstants.INT_VALUES.length], (int) (Integer)values[2]
					);
			Assert.assertEquals("Array value [3] " + values[3] + " does not match the desired value " + TestValueRangeConstants.STRING_VALUES[i%TestValueRangeConstants.STRING_VALUES.length],
				TestValueRangeConstants.STRING_VALUES[i%TestValueRangeConstants.STRING_VALUES.length] == null?"":TestValueRangeConstants.STRING_VALUES[i%TestValueRangeConstants.STRING_VALUES.length], values[3]
			);
			Assert.assertEquals("Array value [4] " + values[4] + " does not match the desired value " + TestValueRangeConstants.STRING_VALUES[i%TestValueRangeConstants.STRING_VALUES.length],
				TestValueRangeConstants.STRING_VALUES[i%TestValueRangeConstants.STRING_VALUES.length] == null?"":TestValueRangeConstants.STRING_VALUES[i%TestValueRangeConstants.STRING_VALUES.length], values[4]
			);
		}
	}
	
	/**
	 * Tests {@link AfterConstructorEvent#TestAfterConstructorEvent(String, String, long, long, long, String, int, int)}.
	 */
	@Test
	public void testBuffer() { // NOPMD (assert missing)
		for (int i=0;i<TestValueRangeConstants.ARRAY_LENGTH;i++) {
			// initialize
			AfterConstructorEvent record = new AfterConstructorEvent(TestValueRangeConstants.LONG_VALUES[i%TestValueRangeConstants.LONG_VALUES.length], TestValueRangeConstants.LONG_VALUES[i%TestValueRangeConstants.LONG_VALUES.length], TestValueRangeConstants.INT_VALUES[i%TestValueRangeConstants.INT_VALUES.length], TestValueRangeConstants.STRING_VALUES[i%TestValueRangeConstants.STRING_VALUES.length], TestValueRangeConstants.STRING_VALUES[i%TestValueRangeConstants.STRING_VALUES.length]);
			
			// check values
			Assert.assertEquals("AfterConstructorEvent.timestamp values are not equal.", TestValueRangeConstants.LONG_VALUES[i%TestValueRangeConstants.LONG_VALUES.length], record.getTimestamp());
			Assert.assertEquals("AfterConstructorEvent.traceId values are not equal.", TestValueRangeConstants.LONG_VALUES[i%TestValueRangeConstants.LONG_VALUES.length], record.getTraceId());
			Assert.assertEquals("AfterConstructorEvent.orderIndex values are not equal.", TestValueRangeConstants.INT_VALUES[i%TestValueRangeConstants.INT_VALUES.length], record.getOrderIndex());
			Assert.assertEquals("AfterConstructorEvent.classSignature values are not equal.", TestValueRangeConstants.STRING_VALUES[i%TestValueRangeConstants.STRING_VALUES.length] == null?"":TestValueRangeConstants.STRING_VALUES[i%TestValueRangeConstants.STRING_VALUES.length], record.getClassSignature());
			Assert.assertEquals("AfterConstructorEvent.operationSignature values are not equal.", TestValueRangeConstants.STRING_VALUES[i%TestValueRangeConstants.STRING_VALUES.length] == null?"":TestValueRangeConstants.STRING_VALUES[i%TestValueRangeConstants.STRING_VALUES.length], record.getOperationSignature());
		}
	}
	
	/**
	 * Tests {@link AfterConstructorEvent#TestAfterConstructorEvent(String, String, long, long, long, String, int, int)}.
	 */
	@Test
	public void testParameterConstruction() { // NOPMD (assert missing)
		for (int i=0;i<TestValueRangeConstants.ARRAY_LENGTH;i++) {
			// initialize
			AfterConstructorEvent record = new AfterConstructorEvent(TestValueRangeConstants.LONG_VALUES[i%TestValueRangeConstants.LONG_VALUES.length], TestValueRangeConstants.LONG_VALUES[i%TestValueRangeConstants.LONG_VALUES.length], TestValueRangeConstants.INT_VALUES[i%TestValueRangeConstants.INT_VALUES.length], TestValueRangeConstants.STRING_VALUES[i%TestValueRangeConstants.STRING_VALUES.length], TestValueRangeConstants.STRING_VALUES[i%TestValueRangeConstants.STRING_VALUES.length]);
			
			// check values
			Assert.assertEquals("AfterConstructorEvent.timestamp values are not equal.", TestValueRangeConstants.LONG_VALUES[i%TestValueRangeConstants.LONG_VALUES.length], record.getTimestamp());
			Assert.assertEquals("AfterConstructorEvent.traceId values are not equal.", TestValueRangeConstants.LONG_VALUES[i%TestValueRangeConstants.LONG_VALUES.length], record.getTraceId());
			Assert.assertEquals("AfterConstructorEvent.orderIndex values are not equal.", TestValueRangeConstants.INT_VALUES[i%TestValueRangeConstants.INT_VALUES.length], record.getOrderIndex());
			Assert.assertEquals("AfterConstructorEvent.classSignature values are not equal.", TestValueRangeConstants.STRING_VALUES[i%TestValueRangeConstants.STRING_VALUES.length] == null?"":TestValueRangeConstants.STRING_VALUES[i%TestValueRangeConstants.STRING_VALUES.length], record.getClassSignature());
			Assert.assertEquals("AfterConstructorEvent.operationSignature values are not equal.", TestValueRangeConstants.STRING_VALUES[i%TestValueRangeConstants.STRING_VALUES.length] == null?"":TestValueRangeConstants.STRING_VALUES[i%TestValueRangeConstants.STRING_VALUES.length], record.getOperationSignature());
		}
	}
}
