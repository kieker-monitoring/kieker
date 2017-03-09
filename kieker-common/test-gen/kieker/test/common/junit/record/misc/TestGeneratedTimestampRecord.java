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

package kieker.test.common.junit.record.misc;

import org.junit.Assert;
import org.junit.Test;

import kieker.common.record.controlflow.OperationExecutionRecord;
import kieker.common.record.misc.TimestampRecord;

import kieker.test.common.junit.AbstractGeneratedKiekerTest;
		
/**
 * Creates {@link OperationExecutionRecord}s via the available constructors and
 * checks the values passed values via getters.
 * 
 * @author Kieker Build
 * 
 * @since 1.10
 */
public class TestGeneratedTimestampRecord extends AbstractGeneratedKiekerTest {

	public TestGeneratedTimestampRecord() {
		// empty default constructor
	}

	/**
	 * Tests {@link TimestampRecord#TestTimestampRecord(String, String, long, long, long, String, int, int)}.
	 */
	@Test
	public void testToArray() { // NOPMD (assert missing)
	for (int i=0;i<ARRAY_LENGTH;i++) {
			// initialize
			TimestampRecord record = new TimestampRecord(LONG_VALUES.get(i % LONG_VALUES.size()));
			
			// check values
			Assert.assertEquals("TimestampRecord.timestamp values are not equal.", (long) LONG_VALUES.get(i % LONG_VALUES.size()), record.getTimestamp());
			
			Object[] values = record.toArray();
			
			Assert.assertNotNull("Record array serialization failed. No values array returned.", values);
			Assert.assertEquals("Record array size does not match expected number of properties 1.", 1, values.length);
			
			// check all object values exist
			Assert.assertNotNull("Array value [0] of type Long must be not null.", values[0]); 
			
			// check all types
			Assert.assertTrue("Type of array value [0] " + values[0].getClass().getCanonicalName() + " does not match the desired type Long", values[0] instanceof Long);
								
			// check all object values 
			Assert.assertEquals("Array value [0] " + values[0] + " does not match the desired value " + LONG_VALUES.get(i % LONG_VALUES.size()),
				LONG_VALUES.get(i % LONG_VALUES.size()), values[0]
					);
		}
	}
	
	/**
	 * Tests {@link TimestampRecord#TestTimestampRecord(String, String, long, long, long, String, int, int)}.
	 */
	@Test
	public void testBuffer() { // NOPMD (assert missing)
		for (int i=0;i<ARRAY_LENGTH;i++) {
			// initialize
			TimestampRecord record = new TimestampRecord(LONG_VALUES.get(i % LONG_VALUES.size()));
			
			// check values
			Assert.assertEquals("TimestampRecord.timestamp values are not equal.", (long) LONG_VALUES.get(i % LONG_VALUES.size()), record.getTimestamp());
		}
	}
	
	/**
	 * Tests {@link TimestampRecord#TestTimestampRecord(String, String, long, long, long, String, int, int)}.
	 */
	@Test
	public void testParameterConstruction() { // NOPMD (assert missing)
		for (int i=0;i<ARRAY_LENGTH;i++) {
			// initialize
			TimestampRecord record = new TimestampRecord(LONG_VALUES.get(i % LONG_VALUES.size()));
			
			// check values
			Assert.assertEquals("TimestampRecord.timestamp values are not equal.", (long) LONG_VALUES.get(i % LONG_VALUES.size()), record.getTimestamp());
		}
	}
}
