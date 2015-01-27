/***************************************************************************
 * Copyright 2015 Kieker Project (http://kieker-monitoring.net)
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

package kieker.test.common.junit.record.flow.trace.operation.object;

import java.nio.ByteBuffer;

import org.junit.Assert;
import org.junit.Test;

import kieker.common.record.flow.trace.operation.object.BeforeOperationObjectEvent;
import kieker.common.util.registry.IRegistry;
import kieker.common.util.registry.Registry;

import kieker.test.common.junit.AbstractGeneratedKiekerTest;
import kieker.test.common.util.record.BookstoreOperationExecutionRecordFactory;
		
/**
 * Creates {@link OperationExecutionRecord}s via the available constructors and
 * checks the values passed values via getters.
 * 
 * @author Kieker Build
 * 
 * @since 1.10
 */
public class TestGeneratedBeforeOperationObjectEvent extends AbstractGeneratedKiekerTest {

	public TestGeneratedBeforeOperationObjectEvent() {
		// empty default constructor
	}

	/**
	 * Tests {@link BeforeOperationObjectEvent#TestBeforeOperationObjectEvent(String, String, long, long, long, String, int, int)}.
	 */
	@Test
	public void testToArray() { // NOPMD (assert missing)
	for (int i=0;i<ARRAY_LENGTH;i++) {
			// initialize
			BeforeOperationObjectEvent record = new BeforeOperationObjectEvent(LONG_VALUES.get(i % LONG_VALUES.size()), LONG_VALUES.get(i % LONG_VALUES.size()), INT_VALUES.get(i % INT_VALUES.size()), STRING_VALUES.get(i % STRING_VALUES.size()), STRING_VALUES.get(i % STRING_VALUES.size()), INT_VALUES.get(i % INT_VALUES.size()));
			
			// check values
			Assert.assertEquals("BeforeOperationObjectEvent.timestamp values are not equal.", (long) LONG_VALUES.get(i % LONG_VALUES.size()), record.getTimestamp());
			Assert.assertEquals("BeforeOperationObjectEvent.traceId values are not equal.", (long) LONG_VALUES.get(i % LONG_VALUES.size()), record.getTraceId());
			Assert.assertEquals("BeforeOperationObjectEvent.orderIndex values are not equal.", (int) INT_VALUES.get(i % INT_VALUES.size()), record.getOrderIndex());
			Assert.assertEquals("BeforeOperationObjectEvent.operationSignature values are not equal.", STRING_VALUES.get(i % STRING_VALUES.size()) == null?"":STRING_VALUES.get(i % STRING_VALUES.size()), record.getOperationSignature());
			Assert.assertEquals("BeforeOperationObjectEvent.classSignature values are not equal.", STRING_VALUES.get(i % STRING_VALUES.size()) == null?"":STRING_VALUES.get(i % STRING_VALUES.size()), record.getClassSignature());
			Assert.assertEquals("BeforeOperationObjectEvent.objectId values are not equal.", (int) INT_VALUES.get(i % INT_VALUES.size()), record.getObjectId());
			
			Object[] values = record.toArray();
			
			Assert.assertNotNull("Record array serialization failed. No values array returned.", values);
			Assert.assertEquals("Record array size does not match expected number of properties 6.", 6, values.length);
			
			// check all object values exist
			Assert.assertNotNull("Array value [0] of type Long must be not null.", values[0]); 
			Assert.assertNotNull("Array value [1] of type Long must be not null.", values[1]); 
			Assert.assertNotNull("Array value [2] of type Integer must be not null.", values[2]); 
			Assert.assertNotNull("Array value [3] of type String must be not null.", values[3]); 
			Assert.assertNotNull("Array value [4] of type String must be not null.", values[4]); 
			Assert.assertNotNull("Array value [5] of type Integer must be not null.", values[5]); 
			
			// check all types
			Assert.assertTrue("Type of array value [0] " + values[0].getClass().getCanonicalName() + " does not match the desired type Long", values[0] instanceof Long);
			Assert.assertTrue("Type of array value [1] " + values[1].getClass().getCanonicalName() + " does not match the desired type Long", values[1] instanceof Long);
			Assert.assertTrue("Type of array value [2] " + values[2].getClass().getCanonicalName() + " does not match the desired type Integer", values[2] instanceof Integer);
			Assert.assertTrue("Type of array value [3] " + values[3].getClass().getCanonicalName() + " does not match the desired type String", values[3] instanceof String);
			Assert.assertTrue("Type of array value [4] " + values[4].getClass().getCanonicalName() + " does not match the desired type String", values[4] instanceof String);
			Assert.assertTrue("Type of array value [5] " + values[5].getClass().getCanonicalName() + " does not match the desired type Integer", values[5] instanceof Integer);
								
			// check all object values 
			Assert.assertEquals("Array value [0] " + values[0] + " does not match the desired value " + LONG_VALUES.get(i % LONG_VALUES.size()),
				LONG_VALUES.get(i % LONG_VALUES.size()), values[0]
					);
			Assert.assertEquals("Array value [1] " + values[1] + " does not match the desired value " + LONG_VALUES.get(i % LONG_VALUES.size()),
				LONG_VALUES.get(i % LONG_VALUES.size()), values[1]
					);
			Assert.assertEquals("Array value [2] " + values[2] + " does not match the desired value " + INT_VALUES.get(i % INT_VALUES.size()),
				INT_VALUES.get(i % INT_VALUES.size()), values[2]
					);
			Assert.assertEquals("Array value [3] " + values[3] + " does not match the desired value " + STRING_VALUES.get(i % STRING_VALUES.size()),
				STRING_VALUES.get(i % STRING_VALUES.size()) == null?"":STRING_VALUES.get(i % STRING_VALUES.size()), values[3]
			);
			Assert.assertEquals("Array value [4] " + values[4] + " does not match the desired value " + STRING_VALUES.get(i % STRING_VALUES.size()),
				STRING_VALUES.get(i % STRING_VALUES.size()) == null?"":STRING_VALUES.get(i % STRING_VALUES.size()), values[4]
			);
			Assert.assertEquals("Array value [5] " + values[5] + " does not match the desired value " + INT_VALUES.get(i % INT_VALUES.size()),
				INT_VALUES.get(i % INT_VALUES.size()), values[5]
					);
		}
	}
	
	/**
	 * Tests {@link BeforeOperationObjectEvent#TestBeforeOperationObjectEvent(String, String, long, long, long, String, int, int)}.
	 */
	@Test
	public void testBuffer() { // NOPMD (assert missing)
		for (int i=0;i<ARRAY_LENGTH;i++) {
			// initialize
			BeforeOperationObjectEvent record = new BeforeOperationObjectEvent(LONG_VALUES.get(i % LONG_VALUES.size()), LONG_VALUES.get(i % LONG_VALUES.size()), INT_VALUES.get(i % INT_VALUES.size()), STRING_VALUES.get(i % STRING_VALUES.size()), STRING_VALUES.get(i % STRING_VALUES.size()), INT_VALUES.get(i % INT_VALUES.size()));
			
			// check values
			Assert.assertEquals("BeforeOperationObjectEvent.timestamp values are not equal.", (long) LONG_VALUES.get(i % LONG_VALUES.size()), record.getTimestamp());
			Assert.assertEquals("BeforeOperationObjectEvent.traceId values are not equal.", (long) LONG_VALUES.get(i % LONG_VALUES.size()), record.getTraceId());
			Assert.assertEquals("BeforeOperationObjectEvent.orderIndex values are not equal.", (int) INT_VALUES.get(i % INT_VALUES.size()), record.getOrderIndex());
			Assert.assertEquals("BeforeOperationObjectEvent.operationSignature values are not equal.", STRING_VALUES.get(i % STRING_VALUES.size()) == null?"":STRING_VALUES.get(i % STRING_VALUES.size()), record.getOperationSignature());
			Assert.assertEquals("BeforeOperationObjectEvent.classSignature values are not equal.", STRING_VALUES.get(i % STRING_VALUES.size()) == null?"":STRING_VALUES.get(i % STRING_VALUES.size()), record.getClassSignature());
			Assert.assertEquals("BeforeOperationObjectEvent.objectId values are not equal.", (int) INT_VALUES.get(i % INT_VALUES.size()), record.getObjectId());
		}
	}
	
	/**
	 * Tests {@link BeforeOperationObjectEvent#TestBeforeOperationObjectEvent(String, String, long, long, long, String, int, int)}.
	 */
	@Test
	public void testParameterConstruction() { // NOPMD (assert missing)
		for (int i=0;i<ARRAY_LENGTH;i++) {
			// initialize
			BeforeOperationObjectEvent record = new BeforeOperationObjectEvent(LONG_VALUES.get(i % LONG_VALUES.size()), LONG_VALUES.get(i % LONG_VALUES.size()), INT_VALUES.get(i % INT_VALUES.size()), STRING_VALUES.get(i % STRING_VALUES.size()), STRING_VALUES.get(i % STRING_VALUES.size()), INT_VALUES.get(i % INT_VALUES.size()));
			
			// check values
			Assert.assertEquals("BeforeOperationObjectEvent.timestamp values are not equal.", (long) LONG_VALUES.get(i % LONG_VALUES.size()), record.getTimestamp());
			Assert.assertEquals("BeforeOperationObjectEvent.traceId values are not equal.", (long) LONG_VALUES.get(i % LONG_VALUES.size()), record.getTraceId());
			Assert.assertEquals("BeforeOperationObjectEvent.orderIndex values are not equal.", (int) INT_VALUES.get(i % INT_VALUES.size()), record.getOrderIndex());
			Assert.assertEquals("BeforeOperationObjectEvent.operationSignature values are not equal.", STRING_VALUES.get(i % STRING_VALUES.size()) == null?"":STRING_VALUES.get(i % STRING_VALUES.size()), record.getOperationSignature());
			Assert.assertEquals("BeforeOperationObjectEvent.classSignature values are not equal.", STRING_VALUES.get(i % STRING_VALUES.size()) == null?"":STRING_VALUES.get(i % STRING_VALUES.size()), record.getClassSignature());
			Assert.assertEquals("BeforeOperationObjectEvent.objectId values are not equal.", (int) INT_VALUES.get(i % INT_VALUES.size()), record.getObjectId());
		}
	}
}
