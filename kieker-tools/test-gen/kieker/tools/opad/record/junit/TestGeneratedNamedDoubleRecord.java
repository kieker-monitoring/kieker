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

package kieker.tools.opad.record.junit;

import java.nio.ByteBuffer;

import org.junit.Assert;
import org.junit.Test;

import kieker.tools.opad.record.NamedDoubleRecord;
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
public class TestGeneratedNamedDoubleRecord extends AbstractGeneratedKiekerTest {

	public TestGeneratedNamedDoubleRecord() {
		// empty default constructor
	}

	/**
	 * Tests {@link NamedDoubleRecord#TestNamedDoubleRecord(String, String, long, long, long, String, int, int)}.
	 */
	@Test
	public void testToArray() { // NOPMD (assert missing)
	for (int i=0;i<ARRAY_LENGTH;i++) {
			// initialize
			NamedDoubleRecord record = new NamedDoubleRecord(STRING_VALUES.get(i % STRING_VALUES.size()), LONG_VALUES.get(i % LONG_VALUES.size()), DOUBLE_VALUES.get(i % DOUBLE_VALUES.size()));
			
			// check values
			Assert.assertEquals("NamedDoubleRecord.applicationName values are not equal.", STRING_VALUES.get(i % STRING_VALUES.size()) == null?"":STRING_VALUES.get(i % STRING_VALUES.size()), record.getApplicationName());
			Assert.assertEquals("NamedDoubleRecord.timestamp values are not equal.", (long) LONG_VALUES.get(i % LONG_VALUES.size()), record.getTimestamp());
			Assert.assertEquals("NamedDoubleRecord.responseTime values are not equal.", 
			(double) DOUBLE_VALUES.get(i % DOUBLE_VALUES.size()), record.getResponseTime(), 0.0000001);
			
			Object[] values = record.toArray();
			
			Assert.assertNotNull("Record array serialization failed. No values array returned.", values);
			Assert.assertEquals("Record array size does not match expected number of properties 3.", 3, values.length);
			
			// check all object values exist
			Assert.assertNotNull("Array value [0] of type String must be not null.", values[0]); 
			Assert.assertNotNull("Array value [1] of type Long must be not null.", values[1]); 
			Assert.assertNotNull("Array value [2] of type Double must be not null.", values[2]); 
			
			// check all types
			Assert.assertTrue("Type of array value [0] " + values[0].getClass().getCanonicalName() + " does not match the desired type String", values[0] instanceof String);
			Assert.assertTrue("Type of array value [1] " + values[1].getClass().getCanonicalName() + " does not match the desired type Long", values[1] instanceof Long);
			Assert.assertTrue("Type of array value [2] " + values[2].getClass().getCanonicalName() + " does not match the desired type Double", values[2] instanceof Double);
								
			// check all object values 
			Assert.assertEquals("Array value [0] " + values[0] + " does not match the desired value " + STRING_VALUES.get(i % STRING_VALUES.size()),
				STRING_VALUES.get(i % STRING_VALUES.size()) == null?"":STRING_VALUES.get(i % STRING_VALUES.size()), values[0]
			);
			Assert.assertEquals("Array value [1] " + values[1] + " does not match the desired value " + LONG_VALUES.get(i % LONG_VALUES.size()),
				LONG_VALUES.get(i % LONG_VALUES.size()), values[1]
					);
			Assert.assertEquals("Array value [2] " + values[2] + " does not match the desired value " + DOUBLE_VALUES.get(i % DOUBLE_VALUES.size()),
				(double) DOUBLE_VALUES.get(i % DOUBLE_VALUES.size()), (double) (Double)values[2], 0.0000001
			);
		}
	}
	
	/**
	 * Tests {@link NamedDoubleRecord#TestNamedDoubleRecord(String, String, long, long, long, String, int, int)}.
	 */
	@Test
	public void testBuffer() { // NOPMD (assert missing)
		for (int i=0;i<ARRAY_LENGTH;i++) {
			// initialize
			NamedDoubleRecord record = new NamedDoubleRecord(STRING_VALUES.get(i % STRING_VALUES.size()), LONG_VALUES.get(i % LONG_VALUES.size()), DOUBLE_VALUES.get(i % DOUBLE_VALUES.size()));
			
			// check values
			Assert.assertEquals("NamedDoubleRecord.applicationName values are not equal.", STRING_VALUES.get(i % STRING_VALUES.size()) == null?"":STRING_VALUES.get(i % STRING_VALUES.size()), record.getApplicationName());
			Assert.assertEquals("NamedDoubleRecord.timestamp values are not equal.", (long) LONG_VALUES.get(i % LONG_VALUES.size()), record.getTimestamp());
			Assert.assertEquals("NamedDoubleRecord.responseTime values are not equal.", 
			(double) DOUBLE_VALUES.get(i % DOUBLE_VALUES.size()), record.getResponseTime(), 0.0000001);
		}
	}
	
	/**
	 * Tests {@link NamedDoubleRecord#TestNamedDoubleRecord(String, String, long, long, long, String, int, int)}.
	 */
	@Test
	public void testParameterConstruction() { // NOPMD (assert missing)
		for (int i=0;i<ARRAY_LENGTH;i++) {
			// initialize
			NamedDoubleRecord record = new NamedDoubleRecord(STRING_VALUES.get(i % STRING_VALUES.size()), LONG_VALUES.get(i % LONG_VALUES.size()), DOUBLE_VALUES.get(i % DOUBLE_VALUES.size()));
			
			// check values
			Assert.assertEquals("NamedDoubleRecord.applicationName values are not equal.", STRING_VALUES.get(i % STRING_VALUES.size()) == null?"":STRING_VALUES.get(i % STRING_VALUES.size()), record.getApplicationName());
			Assert.assertEquals("NamedDoubleRecord.timestamp values are not equal.", (long) LONG_VALUES.get(i % LONG_VALUES.size()), record.getTimestamp());
			Assert.assertEquals("NamedDoubleRecord.responseTime values are not equal.", 
			(double) DOUBLE_VALUES.get(i % DOUBLE_VALUES.size()), record.getResponseTime(), 0.0000001);
		}
	}
}
