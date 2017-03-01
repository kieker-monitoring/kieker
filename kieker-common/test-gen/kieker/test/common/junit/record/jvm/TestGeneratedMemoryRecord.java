/***************************************************************************
 * Copyright 2017 Kieker Project (http://kieker-monitoring.net)
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
package kieker.test.common.junit.record.jvm;

import java.nio.ByteBuffer;

import org.junit.Assert;
import org.junit.Test;

import kieker.common.record.jvm.MemoryRecord;
import kieker.common.util.registry.IRegistry;
import kieker.common.util.registry.Registry;

import kieker.test.common.junit.AbstractGeneratedKiekerTest;
import kieker.test.common.util.record.BookstoreOperationExecutionRecordFactory;
		
/**
 * Creates {@link OperationExecutionRecord}s via the available constructors and
 * checks the values passed values via getters.
 * 
 * @author Nils Christian Ehmke
 * 
 * @since 1.10
 */
public class TestGeneratedMemoryRecord extends AbstractGeneratedKiekerTest {

	public TestGeneratedMemoryRecord() {
		// empty default constructor
	}

	/**
	 * Tests {@link MemoryRecord#TestMemoryRecord(long, string, string, long, long, long, long, long, long, long, long, int)}.
	 */
	@Test
	public void testToArray() { // NOPMD (assert missing)
		for (int i=0;i<ARRAY_LENGTH;i++) {
			// initialize
			MemoryRecord record = new MemoryRecord(LONG_VALUES.get(i % LONG_VALUES.size()), STRING_VALUES.get(i % STRING_VALUES.size()), STRING_VALUES.get(i % STRING_VALUES.size()), LONG_VALUES.get(i % LONG_VALUES.size()), LONG_VALUES.get(i % LONG_VALUES.size()), LONG_VALUES.get(i % LONG_VALUES.size()), LONG_VALUES.get(i % LONG_VALUES.size()), LONG_VALUES.get(i % LONG_VALUES.size()), LONG_VALUES.get(i % LONG_VALUES.size()), LONG_VALUES.get(i % LONG_VALUES.size()), LONG_VALUES.get(i % LONG_VALUES.size()), INT_VALUES.get(i % INT_VALUES.size()));
			
			// check values
			Assert.assertEquals("MemoryRecord.timestamp values are not equal.", (long) LONG_VALUES.get(i % LONG_VALUES.size()), record.getTimestamp());
			Assert.assertEquals("MemoryRecord.hostname values are not equal.", STRING_VALUES.get(i % STRING_VALUES.size()) == null?"":STRING_VALUES.get(i % STRING_VALUES.size()), record.getHostname());
			Assert.assertEquals("MemoryRecord.vmName values are not equal.", STRING_VALUES.get(i % STRING_VALUES.size()) == null?"":STRING_VALUES.get(i % STRING_VALUES.size()), record.getVmName());
			Assert.assertEquals("MemoryRecord.heapMaxBytes values are not equal.", (long) LONG_VALUES.get(i % LONG_VALUES.size()), record.getHeapMaxBytes());
			Assert.assertEquals("MemoryRecord.heapUsedBytes values are not equal.", (long) LONG_VALUES.get(i % LONG_VALUES.size()), record.getHeapUsedBytes());
			Assert.assertEquals("MemoryRecord.heapCommittedBytes values are not equal.", (long) LONG_VALUES.get(i % LONG_VALUES.size()), record.getHeapCommittedBytes());
			Assert.assertEquals("MemoryRecord.heapInitBytes values are not equal.", (long) LONG_VALUES.get(i % LONG_VALUES.size()), record.getHeapInitBytes());
			Assert.assertEquals("MemoryRecord.nonHeapMaxBytes values are not equal.", (long) LONG_VALUES.get(i % LONG_VALUES.size()), record.getNonHeapMaxBytes());
			Assert.assertEquals("MemoryRecord.nonHeapUsedBytes values are not equal.", (long) LONG_VALUES.get(i % LONG_VALUES.size()), record.getNonHeapUsedBytes());
			Assert.assertEquals("MemoryRecord.nonHeapCommittedBytes values are not equal.", (long) LONG_VALUES.get(i % LONG_VALUES.size()), record.getNonHeapCommittedBytes());
			Assert.assertEquals("MemoryRecord.nonHeapInitBytes values are not equal.", (long) LONG_VALUES.get(i % LONG_VALUES.size()), record.getNonHeapInitBytes());
			Assert.assertEquals("MemoryRecord.objectPendingFinalizationCount values are not equal.", (int) INT_VALUES.get(i % INT_VALUES.size()), record.getObjectPendingFinalizationCount());
			
			Object[] values = record.toArray();
			
			Assert.assertNotNull("Record array serialization failed. No values array returned.", values);
			Assert.assertEquals("Record array size does not match expected number of properties 12.", 12, values.length);
			
			// check all object values exist
			Assert.assertNotNull("Array value [0] of type Long must be not null.", values[0]); 
			Assert.assertNotNull("Array value [1] of type String must be not null.", values[1]); 
			Assert.assertNotNull("Array value [2] of type String must be not null.", values[2]); 
			Assert.assertNotNull("Array value [3] of type Long must be not null.", values[3]); 
			Assert.assertNotNull("Array value [4] of type Long must be not null.", values[4]); 
			Assert.assertNotNull("Array value [5] of type Long must be not null.", values[5]); 
			Assert.assertNotNull("Array value [6] of type Long must be not null.", values[6]); 
			Assert.assertNotNull("Array value [7] of type Long must be not null.", values[7]); 
			Assert.assertNotNull("Array value [8] of type Long must be not null.", values[8]); 
			Assert.assertNotNull("Array value [9] of type Long must be not null.", values[9]); 
			Assert.assertNotNull("Array value [10] of type Long must be not null.", values[10]); 
			Assert.assertNotNull("Array value [11] of type Integer must be not null.", values[11]); 
			
			// check all types
			Assert.assertTrue("Type of array value [0] " + values[0].getClass().getCanonicalName() + " does not match the desired type Long", values[0] instanceof Long);
			Assert.assertTrue("Type of array value [1] " + values[1].getClass().getCanonicalName() + " does not match the desired type String", values[1] instanceof String);
			Assert.assertTrue("Type of array value [2] " + values[2].getClass().getCanonicalName() + " does not match the desired type String", values[2] instanceof String);
			Assert.assertTrue("Type of array value [3] " + values[3].getClass().getCanonicalName() + " does not match the desired type Long", values[3] instanceof Long);
			Assert.assertTrue("Type of array value [4] " + values[4].getClass().getCanonicalName() + " does not match the desired type Long", values[4] instanceof Long);
			Assert.assertTrue("Type of array value [5] " + values[5].getClass().getCanonicalName() + " does not match the desired type Long", values[5] instanceof Long);
			Assert.assertTrue("Type of array value [6] " + values[6].getClass().getCanonicalName() + " does not match the desired type Long", values[6] instanceof Long);
			Assert.assertTrue("Type of array value [7] " + values[7].getClass().getCanonicalName() + " does not match the desired type Long", values[7] instanceof Long);
			Assert.assertTrue("Type of array value [8] " + values[8].getClass().getCanonicalName() + " does not match the desired type Long", values[8] instanceof Long);
			Assert.assertTrue("Type of array value [9] " + values[9].getClass().getCanonicalName() + " does not match the desired type Long", values[9] instanceof Long);
			Assert.assertTrue("Type of array value [10] " + values[10].getClass().getCanonicalName() + " does not match the desired type Long", values[10] instanceof Long);
			Assert.assertTrue("Type of array value [11] " + values[11].getClass().getCanonicalName() + " does not match the desired type Integer", values[11] instanceof Integer);
								
			// check all object values 
			Assert.assertEquals("Array value [0] " + values[0] + " does not match the desired value " + LONG_VALUES.get(i % LONG_VALUES.size()),
				LONG_VALUES.get(i % LONG_VALUES.size()), values[0]
					);
			Assert.assertEquals("Array value [1] " + values[1] + " does not match the desired value " + STRING_VALUES.get(i % STRING_VALUES.size()),
				STRING_VALUES.get(i % STRING_VALUES.size()) == null?"":STRING_VALUES.get(i % STRING_VALUES.size()), values[1]
			);
			Assert.assertEquals("Array value [2] " + values[2] + " does not match the desired value " + STRING_VALUES.get(i % STRING_VALUES.size()),
				STRING_VALUES.get(i % STRING_VALUES.size()) == null?"":STRING_VALUES.get(i % STRING_VALUES.size()), values[2]
			);
			Assert.assertEquals("Array value [3] " + values[3] + " does not match the desired value " + LONG_VALUES.get(i % LONG_VALUES.size()),
				LONG_VALUES.get(i % LONG_VALUES.size()), values[3]
					);
			Assert.assertEquals("Array value [4] " + values[4] + " does not match the desired value " + LONG_VALUES.get(i % LONG_VALUES.size()),
				LONG_VALUES.get(i % LONG_VALUES.size()), values[4]
					);
			Assert.assertEquals("Array value [5] " + values[5] + " does not match the desired value " + LONG_VALUES.get(i % LONG_VALUES.size()),
				LONG_VALUES.get(i % LONG_VALUES.size()), values[5]
					);
			Assert.assertEquals("Array value [6] " + values[6] + " does not match the desired value " + LONG_VALUES.get(i % LONG_VALUES.size()),
				LONG_VALUES.get(i % LONG_VALUES.size()), values[6]
					);
			Assert.assertEquals("Array value [7] " + values[7] + " does not match the desired value " + LONG_VALUES.get(i % LONG_VALUES.size()),
				LONG_VALUES.get(i % LONG_VALUES.size()), values[7]
					);
			Assert.assertEquals("Array value [8] " + values[8] + " does not match the desired value " + LONG_VALUES.get(i % LONG_VALUES.size()),
				LONG_VALUES.get(i % LONG_VALUES.size()), values[8]
					);
			Assert.assertEquals("Array value [9] " + values[9] + " does not match the desired value " + LONG_VALUES.get(i % LONG_VALUES.size()),
				LONG_VALUES.get(i % LONG_VALUES.size()), values[9]
					);
			Assert.assertEquals("Array value [10] " + values[10] + " does not match the desired value " + LONG_VALUES.get(i % LONG_VALUES.size()),
				LONG_VALUES.get(i % LONG_VALUES.size()), values[10]
					);
			Assert.assertEquals("Array value [11] " + values[11] + " does not match the desired value " + INT_VALUES.get(i % INT_VALUES.size()),
				INT_VALUES.get(i % INT_VALUES.size()), values[11]
					);
		}
	}
	
	/**
	 * Tests {@link MemoryRecord#TestMemoryRecord(long, string, string, long, long, long, long, long, long, long, long, int)}.
	 */
	@Test
	public void testBuffer() { // NOPMD (assert missing)
		for (int i=0;i<ARRAY_LENGTH;i++) {
			// initialize
			MemoryRecord record = new MemoryRecord(LONG_VALUES.get(i % LONG_VALUES.size()), STRING_VALUES.get(i % STRING_VALUES.size()), STRING_VALUES.get(i % STRING_VALUES.size()), LONG_VALUES.get(i % LONG_VALUES.size()), LONG_VALUES.get(i % LONG_VALUES.size()), LONG_VALUES.get(i % LONG_VALUES.size()), LONG_VALUES.get(i % LONG_VALUES.size()), LONG_VALUES.get(i % LONG_VALUES.size()), LONG_VALUES.get(i % LONG_VALUES.size()), LONG_VALUES.get(i % LONG_VALUES.size()), LONG_VALUES.get(i % LONG_VALUES.size()), INT_VALUES.get(i % INT_VALUES.size()));
			
			// check values
			Assert.assertEquals("MemoryRecord.timestamp values are not equal.", (long) LONG_VALUES.get(i % LONG_VALUES.size()), record.getTimestamp());
			Assert.assertEquals("MemoryRecord.hostname values are not equal.", STRING_VALUES.get(i % STRING_VALUES.size()) == null?"":STRING_VALUES.get(i % STRING_VALUES.size()), record.getHostname());
			Assert.assertEquals("MemoryRecord.vmName values are not equal.", STRING_VALUES.get(i % STRING_VALUES.size()) == null?"":STRING_VALUES.get(i % STRING_VALUES.size()), record.getVmName());
			Assert.assertEquals("MemoryRecord.heapMaxBytes values are not equal.", (long) LONG_VALUES.get(i % LONG_VALUES.size()), record.getHeapMaxBytes());
			Assert.assertEquals("MemoryRecord.heapUsedBytes values are not equal.", (long) LONG_VALUES.get(i % LONG_VALUES.size()), record.getHeapUsedBytes());
			Assert.assertEquals("MemoryRecord.heapCommittedBytes values are not equal.", (long) LONG_VALUES.get(i % LONG_VALUES.size()), record.getHeapCommittedBytes());
			Assert.assertEquals("MemoryRecord.heapInitBytes values are not equal.", (long) LONG_VALUES.get(i % LONG_VALUES.size()), record.getHeapInitBytes());
			Assert.assertEquals("MemoryRecord.nonHeapMaxBytes values are not equal.", (long) LONG_VALUES.get(i % LONG_VALUES.size()), record.getNonHeapMaxBytes());
			Assert.assertEquals("MemoryRecord.nonHeapUsedBytes values are not equal.", (long) LONG_VALUES.get(i % LONG_VALUES.size()), record.getNonHeapUsedBytes());
			Assert.assertEquals("MemoryRecord.nonHeapCommittedBytes values are not equal.", (long) LONG_VALUES.get(i % LONG_VALUES.size()), record.getNonHeapCommittedBytes());
			Assert.assertEquals("MemoryRecord.nonHeapInitBytes values are not equal.", (long) LONG_VALUES.get(i % LONG_VALUES.size()), record.getNonHeapInitBytes());
			Assert.assertEquals("MemoryRecord.objectPendingFinalizationCount values are not equal.", (int) INT_VALUES.get(i % INT_VALUES.size()), record.getObjectPendingFinalizationCount());
		}
	}
	
	/**
	 * Tests {@link MemoryRecord#TestMemoryRecord(long, string, string, long, long, long, long, long, long, long, long, int)}.
	 */
	@Test
	public void testParameterConstruction() { // NOPMD (assert missing)
		for (int i=0;i<ARRAY_LENGTH;i++) {
			// initialize
			MemoryRecord record = new MemoryRecord(LONG_VALUES.get(i % LONG_VALUES.size()), STRING_VALUES.get(i % STRING_VALUES.size()), STRING_VALUES.get(i % STRING_VALUES.size()), LONG_VALUES.get(i % LONG_VALUES.size()), LONG_VALUES.get(i % LONG_VALUES.size()), LONG_VALUES.get(i % LONG_VALUES.size()), LONG_VALUES.get(i % LONG_VALUES.size()), LONG_VALUES.get(i % LONG_VALUES.size()), LONG_VALUES.get(i % LONG_VALUES.size()), LONG_VALUES.get(i % LONG_VALUES.size()), LONG_VALUES.get(i % LONG_VALUES.size()), INT_VALUES.get(i % INT_VALUES.size()));
			
			// check values
			Assert.assertEquals("MemoryRecord.timestamp values are not equal.", (long) LONG_VALUES.get(i % LONG_VALUES.size()), record.getTimestamp());
			Assert.assertEquals("MemoryRecord.hostname values are not equal.", STRING_VALUES.get(i % STRING_VALUES.size()) == null?"":STRING_VALUES.get(i % STRING_VALUES.size()), record.getHostname());
			Assert.assertEquals("MemoryRecord.vmName values are not equal.", STRING_VALUES.get(i % STRING_VALUES.size()) == null?"":STRING_VALUES.get(i % STRING_VALUES.size()), record.getVmName());
			Assert.assertEquals("MemoryRecord.heapMaxBytes values are not equal.", (long) LONG_VALUES.get(i % LONG_VALUES.size()), record.getHeapMaxBytes());
			Assert.assertEquals("MemoryRecord.heapUsedBytes values are not equal.", (long) LONG_VALUES.get(i % LONG_VALUES.size()), record.getHeapUsedBytes());
			Assert.assertEquals("MemoryRecord.heapCommittedBytes values are not equal.", (long) LONG_VALUES.get(i % LONG_VALUES.size()), record.getHeapCommittedBytes());
			Assert.assertEquals("MemoryRecord.heapInitBytes values are not equal.", (long) LONG_VALUES.get(i % LONG_VALUES.size()), record.getHeapInitBytes());
			Assert.assertEquals("MemoryRecord.nonHeapMaxBytes values are not equal.", (long) LONG_VALUES.get(i % LONG_VALUES.size()), record.getNonHeapMaxBytes());
			Assert.assertEquals("MemoryRecord.nonHeapUsedBytes values are not equal.", (long) LONG_VALUES.get(i % LONG_VALUES.size()), record.getNonHeapUsedBytes());
			Assert.assertEquals("MemoryRecord.nonHeapCommittedBytes values are not equal.", (long) LONG_VALUES.get(i % LONG_VALUES.size()), record.getNonHeapCommittedBytes());
			Assert.assertEquals("MemoryRecord.nonHeapInitBytes values are not equal.", (long) LONG_VALUES.get(i % LONG_VALUES.size()), record.getNonHeapInitBytes());
			Assert.assertEquals("MemoryRecord.objectPendingFinalizationCount values are not equal.", (int) INT_VALUES.get(i % INT_VALUES.size()), record.getObjectPendingFinalizationCount());
		}
	}
	
	@Test
	public void testEquality() {
		int i = 0;
		MemoryRecord oneRecord = new MemoryRecord(LONG_VALUES.get(i % LONG_VALUES.size()), STRING_VALUES.get(i % STRING_VALUES.size()), STRING_VALUES.get(i % STRING_VALUES.size()), LONG_VALUES.get(i % LONG_VALUES.size()), LONG_VALUES.get(i % LONG_VALUES.size()), LONG_VALUES.get(i % LONG_VALUES.size()), LONG_VALUES.get(i % LONG_VALUES.size()), LONG_VALUES.get(i % LONG_VALUES.size()), LONG_VALUES.get(i % LONG_VALUES.size()), LONG_VALUES.get(i % LONG_VALUES.size()), LONG_VALUES.get(i % LONG_VALUES.size()), INT_VALUES.get(i % INT_VALUES.size()));
		i = 0;
		MemoryRecord copiedRecord = new MemoryRecord(LONG_VALUES.get(i % LONG_VALUES.size()), STRING_VALUES.get(i % STRING_VALUES.size()), STRING_VALUES.get(i % STRING_VALUES.size()), LONG_VALUES.get(i % LONG_VALUES.size()), LONG_VALUES.get(i % LONG_VALUES.size()), LONG_VALUES.get(i % LONG_VALUES.size()), LONG_VALUES.get(i % LONG_VALUES.size()), LONG_VALUES.get(i % LONG_VALUES.size()), LONG_VALUES.get(i % LONG_VALUES.size()), LONG_VALUES.get(i % LONG_VALUES.size()), LONG_VALUES.get(i % LONG_VALUES.size()), INT_VALUES.get(i % INT_VALUES.size()));
		
		Assert.assertEquals(oneRecord, copiedRecord);
	}	
	
	@Test
	public void testUnequality() {
		int i = 0;
		MemoryRecord oneRecord = new MemoryRecord(LONG_VALUES.get(i % LONG_VALUES.size()), STRING_VALUES.get(i % STRING_VALUES.size()), STRING_VALUES.get(i % STRING_VALUES.size()), LONG_VALUES.get(i % LONG_VALUES.size()), LONG_VALUES.get(i % LONG_VALUES.size()), LONG_VALUES.get(i % LONG_VALUES.size()), LONG_VALUES.get(i % LONG_VALUES.size()), LONG_VALUES.get(i % LONG_VALUES.size()), LONG_VALUES.get(i % LONG_VALUES.size()), LONG_VALUES.get(i % LONG_VALUES.size()), LONG_VALUES.get(i % LONG_VALUES.size()), INT_VALUES.get(i % INT_VALUES.size()));
		i = 2;
		MemoryRecord anotherRecord = new MemoryRecord(LONG_VALUES.get(i % LONG_VALUES.size()), STRING_VALUES.get(i % STRING_VALUES.size()), STRING_VALUES.get(i % STRING_VALUES.size()), LONG_VALUES.get(i % LONG_VALUES.size()), LONG_VALUES.get(i % LONG_VALUES.size()), LONG_VALUES.get(i % LONG_VALUES.size()), LONG_VALUES.get(i % LONG_VALUES.size()), LONG_VALUES.get(i % LONG_VALUES.size()), LONG_VALUES.get(i % LONG_VALUES.size()), LONG_VALUES.get(i % LONG_VALUES.size()), LONG_VALUES.get(i % LONG_VALUES.size()), INT_VALUES.get(i % INT_VALUES.size()));
		
		Assert.assertNotEquals(oneRecord, anotherRecord);
	}
}
