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

package kieker.test.common.junit.record.system;

import org.junit.Assert;
import org.junit.Test;

import kieker.common.record.controlflow.OperationExecutionRecord;
import kieker.common.record.system.DiskUsageRecord;

import kieker.test.common.junit.AbstractGeneratedKiekerTest;
		
/**
 * Creates {@link OperationExecutionRecord}s via the available constructors and
 * checks the values passed values via getters.
 * 
 * @author Kieker Build
 * 
 * @since 1.10
 */
public class TestGeneratedDiskUsageRecord extends AbstractGeneratedKiekerTest {

	public TestGeneratedDiskUsageRecord() {
		// empty default constructor
	}

	/**
	 * Tests {@link DiskUsageRecord#TestDiskUsageRecord(String, String, long, long, long, String, int, int)}.
	 */
	@Test
	public void testToArray() { // NOPMD (assert missing)
	for (int i=0;i<ARRAY_LENGTH;i++) {
			// initialize
			DiskUsageRecord record = new DiskUsageRecord(LONG_VALUES.get(i % LONG_VALUES.size()), STRING_VALUES.get(i % STRING_VALUES.size()), STRING_VALUES.get(i % STRING_VALUES.size()), DOUBLE_VALUES.get(i % DOUBLE_VALUES.size()), DOUBLE_VALUES.get(i % DOUBLE_VALUES.size()), DOUBLE_VALUES.get(i % DOUBLE_VALUES.size()), DOUBLE_VALUES.get(i % DOUBLE_VALUES.size()), DOUBLE_VALUES.get(i % DOUBLE_VALUES.size()), DOUBLE_VALUES.get(i % DOUBLE_VALUES.size()));
			
			// check values
			Assert.assertEquals("DiskUsageRecord.timestamp values are not equal.", (long) LONG_VALUES.get(i % LONG_VALUES.size()), record.getTimestamp());
			Assert.assertEquals("DiskUsageRecord.hostname values are not equal.", STRING_VALUES.get(i % STRING_VALUES.size()) == null?"":STRING_VALUES.get(i % STRING_VALUES.size()), record.getHostname());
			Assert.assertEquals("DiskUsageRecord.deviceName values are not equal.", STRING_VALUES.get(i % STRING_VALUES.size()) == null?"":STRING_VALUES.get(i % STRING_VALUES.size()), record.getDeviceName());
			Assert.assertEquals("DiskUsageRecord.queue values are not equal.", 
			(double) DOUBLE_VALUES.get(i % DOUBLE_VALUES.size()), record.getQueue(), 0.0000001);
			Assert.assertEquals("DiskUsageRecord.readBytesPerSecond values are not equal.", 
			(double) DOUBLE_VALUES.get(i % DOUBLE_VALUES.size()), record.getReadBytesPerSecond(), 0.0000001);
			Assert.assertEquals("DiskUsageRecord.readsPerSecond values are not equal.", 
			(double) DOUBLE_VALUES.get(i % DOUBLE_VALUES.size()), record.getReadsPerSecond(), 0.0000001);
			Assert.assertEquals("DiskUsageRecord.serviceTime values are not equal.", 
			(double) DOUBLE_VALUES.get(i % DOUBLE_VALUES.size()), record.getServiceTime(), 0.0000001);
			Assert.assertEquals("DiskUsageRecord.writeBytesPerSecond values are not equal.", 
			(double) DOUBLE_VALUES.get(i % DOUBLE_VALUES.size()), record.getWriteBytesPerSecond(), 0.0000001);
			Assert.assertEquals("DiskUsageRecord.writesPerSecond values are not equal.", 
			(double) DOUBLE_VALUES.get(i % DOUBLE_VALUES.size()), record.getWritesPerSecond(), 0.0000001);
			
			Object[] values = record.toArray();
			
			Assert.assertNotNull("Record array serialization failed. No values array returned.", values);
			Assert.assertEquals("Record array size does not match expected number of properties 9.", 9, values.length);
			
			// check all object values exist
			Assert.assertNotNull("Array value [0] of type Long must be not null.", values[0]); 
			Assert.assertNotNull("Array value [1] of type String must be not null.", values[1]); 
			Assert.assertNotNull("Array value [2] of type String must be not null.", values[2]); 
			Assert.assertNotNull("Array value [3] of type Double must be not null.", values[3]); 
			Assert.assertNotNull("Array value [4] of type Double must be not null.", values[4]); 
			Assert.assertNotNull("Array value [5] of type Double must be not null.", values[5]); 
			Assert.assertNotNull("Array value [6] of type Double must be not null.", values[6]); 
			Assert.assertNotNull("Array value [7] of type Double must be not null.", values[7]); 
			Assert.assertNotNull("Array value [8] of type Double must be not null.", values[8]); 
			
			// check all types
			Assert.assertTrue("Type of array value [0] " + values[0].getClass().getCanonicalName() + " does not match the desired type Long", values[0] instanceof Long);
			Assert.assertTrue("Type of array value [1] " + values[1].getClass().getCanonicalName() + " does not match the desired type String", values[1] instanceof String);
			Assert.assertTrue("Type of array value [2] " + values[2].getClass().getCanonicalName() + " does not match the desired type String", values[2] instanceof String);
			Assert.assertTrue("Type of array value [3] " + values[3].getClass().getCanonicalName() + " does not match the desired type Double", values[3] instanceof Double);
			Assert.assertTrue("Type of array value [4] " + values[4].getClass().getCanonicalName() + " does not match the desired type Double", values[4] instanceof Double);
			Assert.assertTrue("Type of array value [5] " + values[5].getClass().getCanonicalName() + " does not match the desired type Double", values[5] instanceof Double);
			Assert.assertTrue("Type of array value [6] " + values[6].getClass().getCanonicalName() + " does not match the desired type Double", values[6] instanceof Double);
			Assert.assertTrue("Type of array value [7] " + values[7].getClass().getCanonicalName() + " does not match the desired type Double", values[7] instanceof Double);
			Assert.assertTrue("Type of array value [8] " + values[8].getClass().getCanonicalName() + " does not match the desired type Double", values[8] instanceof Double);
								
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
			Assert.assertEquals("Array value [3] " + values[3] + " does not match the desired value " + DOUBLE_VALUES.get(i % DOUBLE_VALUES.size()),
				(double) DOUBLE_VALUES.get(i % DOUBLE_VALUES.size()), (double) (Double)values[3], 0.0000001
			);
			Assert.assertEquals("Array value [4] " + values[4] + " does not match the desired value " + DOUBLE_VALUES.get(i % DOUBLE_VALUES.size()),
				(double) DOUBLE_VALUES.get(i % DOUBLE_VALUES.size()), (double) (Double)values[4], 0.0000001
			);
			Assert.assertEquals("Array value [5] " + values[5] + " does not match the desired value " + DOUBLE_VALUES.get(i % DOUBLE_VALUES.size()),
				(double) DOUBLE_VALUES.get(i % DOUBLE_VALUES.size()), (double) (Double)values[5], 0.0000001
			);
			Assert.assertEquals("Array value [6] " + values[6] + " does not match the desired value " + DOUBLE_VALUES.get(i % DOUBLE_VALUES.size()),
				(double) DOUBLE_VALUES.get(i % DOUBLE_VALUES.size()), (double) (Double)values[6], 0.0000001
			);
			Assert.assertEquals("Array value [7] " + values[7] + " does not match the desired value " + DOUBLE_VALUES.get(i % DOUBLE_VALUES.size()),
				(double) DOUBLE_VALUES.get(i % DOUBLE_VALUES.size()), (double) (Double)values[7], 0.0000001
			);
			Assert.assertEquals("Array value [8] " + values[8] + " does not match the desired value " + DOUBLE_VALUES.get(i % DOUBLE_VALUES.size()),
				(double) DOUBLE_VALUES.get(i % DOUBLE_VALUES.size()), (double) (Double)values[8], 0.0000001
			);
		}
	}
	
	/**
	 * Tests {@link DiskUsageRecord#TestDiskUsageRecord(String, String, long, long, long, String, int, int)}.
	 */
	@Test
	public void testBuffer() { // NOPMD (assert missing)
		for (int i=0;i<ARRAY_LENGTH;i++) {
			// initialize
			DiskUsageRecord record = new DiskUsageRecord(LONG_VALUES.get(i % LONG_VALUES.size()), STRING_VALUES.get(i % STRING_VALUES.size()), STRING_VALUES.get(i % STRING_VALUES.size()), DOUBLE_VALUES.get(i % DOUBLE_VALUES.size()), DOUBLE_VALUES.get(i % DOUBLE_VALUES.size()), DOUBLE_VALUES.get(i % DOUBLE_VALUES.size()), DOUBLE_VALUES.get(i % DOUBLE_VALUES.size()), DOUBLE_VALUES.get(i % DOUBLE_VALUES.size()), DOUBLE_VALUES.get(i % DOUBLE_VALUES.size()));
			
			// check values
			Assert.assertEquals("DiskUsageRecord.timestamp values are not equal.", (long) LONG_VALUES.get(i % LONG_VALUES.size()), record.getTimestamp());
			Assert.assertEquals("DiskUsageRecord.hostname values are not equal.", STRING_VALUES.get(i % STRING_VALUES.size()) == null?"":STRING_VALUES.get(i % STRING_VALUES.size()), record.getHostname());
			Assert.assertEquals("DiskUsageRecord.deviceName values are not equal.", STRING_VALUES.get(i % STRING_VALUES.size()) == null?"":STRING_VALUES.get(i % STRING_VALUES.size()), record.getDeviceName());
			Assert.assertEquals("DiskUsageRecord.queue values are not equal.", 
			(double) DOUBLE_VALUES.get(i % DOUBLE_VALUES.size()), record.getQueue(), 0.0000001);
			Assert.assertEquals("DiskUsageRecord.readBytesPerSecond values are not equal.", 
			(double) DOUBLE_VALUES.get(i % DOUBLE_VALUES.size()), record.getReadBytesPerSecond(), 0.0000001);
			Assert.assertEquals("DiskUsageRecord.readsPerSecond values are not equal.", 
			(double) DOUBLE_VALUES.get(i % DOUBLE_VALUES.size()), record.getReadsPerSecond(), 0.0000001);
			Assert.assertEquals("DiskUsageRecord.serviceTime values are not equal.", 
			(double) DOUBLE_VALUES.get(i % DOUBLE_VALUES.size()), record.getServiceTime(), 0.0000001);
			Assert.assertEquals("DiskUsageRecord.writeBytesPerSecond values are not equal.", 
			(double) DOUBLE_VALUES.get(i % DOUBLE_VALUES.size()), record.getWriteBytesPerSecond(), 0.0000001);
			Assert.assertEquals("DiskUsageRecord.writesPerSecond values are not equal.", 
			(double) DOUBLE_VALUES.get(i % DOUBLE_VALUES.size()), record.getWritesPerSecond(), 0.0000001);
		}
	}
	
	/**
	 * Tests {@link DiskUsageRecord#TestDiskUsageRecord(String, String, long, long, long, String, int, int)}.
	 */
	@Test
	public void testParameterConstruction() { // NOPMD (assert missing)
		for (int i=0;i<ARRAY_LENGTH;i++) {
			// initialize
			DiskUsageRecord record = new DiskUsageRecord(LONG_VALUES.get(i % LONG_VALUES.size()), STRING_VALUES.get(i % STRING_VALUES.size()), STRING_VALUES.get(i % STRING_VALUES.size()), DOUBLE_VALUES.get(i % DOUBLE_VALUES.size()), DOUBLE_VALUES.get(i % DOUBLE_VALUES.size()), DOUBLE_VALUES.get(i % DOUBLE_VALUES.size()), DOUBLE_VALUES.get(i % DOUBLE_VALUES.size()), DOUBLE_VALUES.get(i % DOUBLE_VALUES.size()), DOUBLE_VALUES.get(i % DOUBLE_VALUES.size()));
			
			// check values
			Assert.assertEquals("DiskUsageRecord.timestamp values are not equal.", (long) LONG_VALUES.get(i % LONG_VALUES.size()), record.getTimestamp());
			Assert.assertEquals("DiskUsageRecord.hostname values are not equal.", STRING_VALUES.get(i % STRING_VALUES.size()) == null?"":STRING_VALUES.get(i % STRING_VALUES.size()), record.getHostname());
			Assert.assertEquals("DiskUsageRecord.deviceName values are not equal.", STRING_VALUES.get(i % STRING_VALUES.size()) == null?"":STRING_VALUES.get(i % STRING_VALUES.size()), record.getDeviceName());
			Assert.assertEquals("DiskUsageRecord.queue values are not equal.", 
			(double) DOUBLE_VALUES.get(i % DOUBLE_VALUES.size()), record.getQueue(), 0.0000001);
			Assert.assertEquals("DiskUsageRecord.readBytesPerSecond values are not equal.", 
			(double) DOUBLE_VALUES.get(i % DOUBLE_VALUES.size()), record.getReadBytesPerSecond(), 0.0000001);
			Assert.assertEquals("DiskUsageRecord.readsPerSecond values are not equal.", 
			(double) DOUBLE_VALUES.get(i % DOUBLE_VALUES.size()), record.getReadsPerSecond(), 0.0000001);
			Assert.assertEquals("DiskUsageRecord.serviceTime values are not equal.", 
			(double) DOUBLE_VALUES.get(i % DOUBLE_VALUES.size()), record.getServiceTime(), 0.0000001);
			Assert.assertEquals("DiskUsageRecord.writeBytesPerSecond values are not equal.", 
			(double) DOUBLE_VALUES.get(i % DOUBLE_VALUES.size()), record.getWriteBytesPerSecond(), 0.0000001);
			Assert.assertEquals("DiskUsageRecord.writesPerSecond values are not equal.", 
			(double) DOUBLE_VALUES.get(i % DOUBLE_VALUES.size()), record.getWritesPerSecond(), 0.0000001);
		}
	}
}
