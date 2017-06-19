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
package kieker.test.common.junit.record.system;

//import java.nio.ByteBuffer;

import org.junit.Assert;
import org.junit.Test;

import kieker.common.record.system.NetworkUtilizationRecord;
//import kieker.common.util.registry.IRegistry;
//import kieker.common.util.registry.Registry;

import kieker.test.common.junit.AbstractGeneratedKiekerTest;
//import kieker.test.common.util.record.BookstoreOperationExecutionRecordFactory;
		
/**
 * Creates {@link OperationExecutionRecord}s via the available constructors and
 * checks the values passed values via getters.
 * 
 * @author Teerat Pitakrat
 * 
 * @since 1.12
 */
public class TestGeneratedNetworkUtilizationRecord extends AbstractGeneratedKiekerTest {

	public TestGeneratedNetworkUtilizationRecord() {
		// empty default constructor
	}

	/**
	 * Tests {@link NetworkUtilizationRecord#TestNetworkUtilizationRecord(long, string, string, long, double, double, double, double, double, double, double, double, double, double, double, double, double)}.
	 */
	@Test
	public void testToArray() { // NOPMD (assert missing)
		for (int i=0;i<ARRAY_LENGTH;i++) {
			// initialize
			NetworkUtilizationRecord record = new NetworkUtilizationRecord(LONG_VALUES.get(i % LONG_VALUES.size()), STRING_VALUES.get(i % STRING_VALUES.size()), STRING_VALUES.get(i % STRING_VALUES.size()), LONG_VALUES.get(i % LONG_VALUES.size()), DOUBLE_VALUES.get(i % DOUBLE_VALUES.size()), DOUBLE_VALUES.get(i % DOUBLE_VALUES.size()), DOUBLE_VALUES.get(i % DOUBLE_VALUES.size()), DOUBLE_VALUES.get(i % DOUBLE_VALUES.size()), DOUBLE_VALUES.get(i % DOUBLE_VALUES.size()), DOUBLE_VALUES.get(i % DOUBLE_VALUES.size()), DOUBLE_VALUES.get(i % DOUBLE_VALUES.size()), DOUBLE_VALUES.get(i % DOUBLE_VALUES.size()), DOUBLE_VALUES.get(i % DOUBLE_VALUES.size()), DOUBLE_VALUES.get(i % DOUBLE_VALUES.size()), DOUBLE_VALUES.get(i % DOUBLE_VALUES.size()), DOUBLE_VALUES.get(i % DOUBLE_VALUES.size()), DOUBLE_VALUES.get(i % DOUBLE_VALUES.size()));
			
			// check values
			Assert.assertEquals("NetworkUtilizationRecord.timestamp values are not equal.", (long) LONG_VALUES.get(i % LONG_VALUES.size()), record.getTimestamp());
			Assert.assertEquals("NetworkUtilizationRecord.hostname values are not equal.", STRING_VALUES.get(i % STRING_VALUES.size()) == null?"":STRING_VALUES.get(i % STRING_VALUES.size()), record.getHostname());
			Assert.assertEquals("NetworkUtilizationRecord.interfaceName values are not equal.", STRING_VALUES.get(i % STRING_VALUES.size()) == null?"":STRING_VALUES.get(i % STRING_VALUES.size()), record.getInterfaceName());
			Assert.assertEquals("NetworkUtilizationRecord.speed values are not equal.", (long) LONG_VALUES.get(i % LONG_VALUES.size()), record.getSpeed());
			Assert.assertEquals("NetworkUtilizationRecord.txBytesPerSecond values are not equal.", 
			(double) DOUBLE_VALUES.get(i % DOUBLE_VALUES.size()), record.getTxBytesPerSecond(), 0.0000001);
			Assert.assertEquals("NetworkUtilizationRecord.txCarrierPerSecond values are not equal.", 
			(double) DOUBLE_VALUES.get(i % DOUBLE_VALUES.size()), record.getTxCarrierPerSecond(), 0.0000001);
			Assert.assertEquals("NetworkUtilizationRecord.txCollisionsPerSecond values are not equal.", 
			(double) DOUBLE_VALUES.get(i % DOUBLE_VALUES.size()), record.getTxCollisionsPerSecond(), 0.0000001);
			Assert.assertEquals("NetworkUtilizationRecord.txDroppedPerSecond values are not equal.", 
			(double) DOUBLE_VALUES.get(i % DOUBLE_VALUES.size()), record.getTxDroppedPerSecond(), 0.0000001);
			Assert.assertEquals("NetworkUtilizationRecord.txErrorsPerSecond values are not equal.", 
			(double) DOUBLE_VALUES.get(i % DOUBLE_VALUES.size()), record.getTxErrorsPerSecond(), 0.0000001);
			Assert.assertEquals("NetworkUtilizationRecord.txOverrunsPerSecond values are not equal.", 
			(double) DOUBLE_VALUES.get(i % DOUBLE_VALUES.size()), record.getTxOverrunsPerSecond(), 0.0000001);
			Assert.assertEquals("NetworkUtilizationRecord.txPacketsPerSecond values are not equal.", 
			(double) DOUBLE_VALUES.get(i % DOUBLE_VALUES.size()), record.getTxPacketsPerSecond(), 0.0000001);
			Assert.assertEquals("NetworkUtilizationRecord.rxBytesPerSecond values are not equal.", 
			(double) DOUBLE_VALUES.get(i % DOUBLE_VALUES.size()), record.getRxBytesPerSecond(), 0.0000001);
			Assert.assertEquals("NetworkUtilizationRecord.rxDroppedPerSecond values are not equal.", 
			(double) DOUBLE_VALUES.get(i % DOUBLE_VALUES.size()), record.getRxDroppedPerSecond(), 0.0000001);
			Assert.assertEquals("NetworkUtilizationRecord.rxErrorsPerSecond values are not equal.", 
			(double) DOUBLE_VALUES.get(i % DOUBLE_VALUES.size()), record.getRxErrorsPerSecond(), 0.0000001);
			Assert.assertEquals("NetworkUtilizationRecord.rxFramePerSecond values are not equal.", 
			(double) DOUBLE_VALUES.get(i % DOUBLE_VALUES.size()), record.getRxFramePerSecond(), 0.0000001);
			Assert.assertEquals("NetworkUtilizationRecord.rxOverrunsPerSecond values are not equal.", 
			(double) DOUBLE_VALUES.get(i % DOUBLE_VALUES.size()), record.getRxOverrunsPerSecond(), 0.0000001);
			Assert.assertEquals("NetworkUtilizationRecord.rxPacketsPerSecond values are not equal.", 
			(double) DOUBLE_VALUES.get(i % DOUBLE_VALUES.size()), record.getRxPacketsPerSecond(), 0.0000001);
			
			Object[] values = record.toArray();
			
			Assert.assertNotNull("Record array serialization failed. No values array returned.", values);
			Assert.assertEquals("Record array size does not match expected number of properties 17.", 17, values.length);
			
			// check all object values exist
			Assert.assertNotNull("Array value [0] of type Long must be not null.", values[0]); 
			Assert.assertNotNull("Array value [1] of type String must be not null.", values[1]); 
			Assert.assertNotNull("Array value [2] of type String must be not null.", values[2]); 
			Assert.assertNotNull("Array value [3] of type Long must be not null.", values[3]); 
			Assert.assertNotNull("Array value [4] of type Double must be not null.", values[4]); 
			Assert.assertNotNull("Array value [5] of type Double must be not null.", values[5]); 
			Assert.assertNotNull("Array value [6] of type Double must be not null.", values[6]); 
			Assert.assertNotNull("Array value [7] of type Double must be not null.", values[7]); 
			Assert.assertNotNull("Array value [8] of type Double must be not null.", values[8]); 
			Assert.assertNotNull("Array value [9] of type Double must be not null.", values[9]); 
			Assert.assertNotNull("Array value [10] of type Double must be not null.", values[10]); 
			Assert.assertNotNull("Array value [11] of type Double must be not null.", values[11]); 
			Assert.assertNotNull("Array value [12] of type Double must be not null.", values[12]); 
			Assert.assertNotNull("Array value [13] of type Double must be not null.", values[13]); 
			Assert.assertNotNull("Array value [14] of type Double must be not null.", values[14]); 
			Assert.assertNotNull("Array value [15] of type Double must be not null.", values[15]); 
			Assert.assertNotNull("Array value [16] of type Double must be not null.", values[16]); 
			
			// check all types
			Assert.assertTrue("Type of array value [0] " + values[0].getClass().getCanonicalName() + " does not match the desired type Long", values[0] instanceof Long);
			Assert.assertTrue("Type of array value [1] " + values[1].getClass().getCanonicalName() + " does not match the desired type String", values[1] instanceof String);
			Assert.assertTrue("Type of array value [2] " + values[2].getClass().getCanonicalName() + " does not match the desired type String", values[2] instanceof String);
			Assert.assertTrue("Type of array value [3] " + values[3].getClass().getCanonicalName() + " does not match the desired type Long", values[3] instanceof Long);
			Assert.assertTrue("Type of array value [4] " + values[4].getClass().getCanonicalName() + " does not match the desired type Double", values[4] instanceof Double);
			Assert.assertTrue("Type of array value [5] " + values[5].getClass().getCanonicalName() + " does not match the desired type Double", values[5] instanceof Double);
			Assert.assertTrue("Type of array value [6] " + values[6].getClass().getCanonicalName() + " does not match the desired type Double", values[6] instanceof Double);
			Assert.assertTrue("Type of array value [7] " + values[7].getClass().getCanonicalName() + " does not match the desired type Double", values[7] instanceof Double);
			Assert.assertTrue("Type of array value [8] " + values[8].getClass().getCanonicalName() + " does not match the desired type Double", values[8] instanceof Double);
			Assert.assertTrue("Type of array value [9] " + values[9].getClass().getCanonicalName() + " does not match the desired type Double", values[9] instanceof Double);
			Assert.assertTrue("Type of array value [10] " + values[10].getClass().getCanonicalName() + " does not match the desired type Double", values[10] instanceof Double);
			Assert.assertTrue("Type of array value [11] " + values[11].getClass().getCanonicalName() + " does not match the desired type Double", values[11] instanceof Double);
			Assert.assertTrue("Type of array value [12] " + values[12].getClass().getCanonicalName() + " does not match the desired type Double", values[12] instanceof Double);
			Assert.assertTrue("Type of array value [13] " + values[13].getClass().getCanonicalName() + " does not match the desired type Double", values[13] instanceof Double);
			Assert.assertTrue("Type of array value [14] " + values[14].getClass().getCanonicalName() + " does not match the desired type Double", values[14] instanceof Double);
			Assert.assertTrue("Type of array value [15] " + values[15].getClass().getCanonicalName() + " does not match the desired type Double", values[15] instanceof Double);
			Assert.assertTrue("Type of array value [16] " + values[16].getClass().getCanonicalName() + " does not match the desired type Double", values[16] instanceof Double);
								
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
			Assert.assertEquals("Array value [9] " + values[9] + " does not match the desired value " + DOUBLE_VALUES.get(i % DOUBLE_VALUES.size()),
				(double) DOUBLE_VALUES.get(i % DOUBLE_VALUES.size()), (double) (Double)values[9], 0.0000001
			);
			Assert.assertEquals("Array value [10] " + values[10] + " does not match the desired value " + DOUBLE_VALUES.get(i % DOUBLE_VALUES.size()),
				(double) DOUBLE_VALUES.get(i % DOUBLE_VALUES.size()), (double) (Double)values[10], 0.0000001
			);
			Assert.assertEquals("Array value [11] " + values[11] + " does not match the desired value " + DOUBLE_VALUES.get(i % DOUBLE_VALUES.size()),
				(double) DOUBLE_VALUES.get(i % DOUBLE_VALUES.size()), (double) (Double)values[11], 0.0000001
			);
			Assert.assertEquals("Array value [12] " + values[12] + " does not match the desired value " + DOUBLE_VALUES.get(i % DOUBLE_VALUES.size()),
				(double) DOUBLE_VALUES.get(i % DOUBLE_VALUES.size()), (double) (Double)values[12], 0.0000001
			);
			Assert.assertEquals("Array value [13] " + values[13] + " does not match the desired value " + DOUBLE_VALUES.get(i % DOUBLE_VALUES.size()),
				(double) DOUBLE_VALUES.get(i % DOUBLE_VALUES.size()), (double) (Double)values[13], 0.0000001
			);
			Assert.assertEquals("Array value [14] " + values[14] + " does not match the desired value " + DOUBLE_VALUES.get(i % DOUBLE_VALUES.size()),
				(double) DOUBLE_VALUES.get(i % DOUBLE_VALUES.size()), (double) (Double)values[14], 0.0000001
			);
			Assert.assertEquals("Array value [15] " + values[15] + " does not match the desired value " + DOUBLE_VALUES.get(i % DOUBLE_VALUES.size()),
				(double) DOUBLE_VALUES.get(i % DOUBLE_VALUES.size()), (double) (Double)values[15], 0.0000001
			);
			Assert.assertEquals("Array value [16] " + values[16] + " does not match the desired value " + DOUBLE_VALUES.get(i % DOUBLE_VALUES.size()),
				(double) DOUBLE_VALUES.get(i % DOUBLE_VALUES.size()), (double) (Double)values[16], 0.0000001
			);
		}
	}
	
	/**
	 * Tests {@link NetworkUtilizationRecord#TestNetworkUtilizationRecord(long, string, string, long, double, double, double, double, double, double, double, double, double, double, double, double, double)}.
	 */
	@Test
	public void testBuffer() { // NOPMD (assert missing)
		for (int i=0;i<ARRAY_LENGTH;i++) {
			// initialize
			NetworkUtilizationRecord record = new NetworkUtilizationRecord(LONG_VALUES.get(i % LONG_VALUES.size()), STRING_VALUES.get(i % STRING_VALUES.size()), STRING_VALUES.get(i % STRING_VALUES.size()), LONG_VALUES.get(i % LONG_VALUES.size()), DOUBLE_VALUES.get(i % DOUBLE_VALUES.size()), DOUBLE_VALUES.get(i % DOUBLE_VALUES.size()), DOUBLE_VALUES.get(i % DOUBLE_VALUES.size()), DOUBLE_VALUES.get(i % DOUBLE_VALUES.size()), DOUBLE_VALUES.get(i % DOUBLE_VALUES.size()), DOUBLE_VALUES.get(i % DOUBLE_VALUES.size()), DOUBLE_VALUES.get(i % DOUBLE_VALUES.size()), DOUBLE_VALUES.get(i % DOUBLE_VALUES.size()), DOUBLE_VALUES.get(i % DOUBLE_VALUES.size()), DOUBLE_VALUES.get(i % DOUBLE_VALUES.size()), DOUBLE_VALUES.get(i % DOUBLE_VALUES.size()), DOUBLE_VALUES.get(i % DOUBLE_VALUES.size()), DOUBLE_VALUES.get(i % DOUBLE_VALUES.size()));
			
			// check values
			Assert.assertEquals("NetworkUtilizationRecord.timestamp values are not equal.", (long) LONG_VALUES.get(i % LONG_VALUES.size()), record.getTimestamp());
			Assert.assertEquals("NetworkUtilizationRecord.hostname values are not equal.", STRING_VALUES.get(i % STRING_VALUES.size()) == null?"":STRING_VALUES.get(i % STRING_VALUES.size()), record.getHostname());
			Assert.assertEquals("NetworkUtilizationRecord.interfaceName values are not equal.", STRING_VALUES.get(i % STRING_VALUES.size()) == null?"":STRING_VALUES.get(i % STRING_VALUES.size()), record.getInterfaceName());
			Assert.assertEquals("NetworkUtilizationRecord.speed values are not equal.", (long) LONG_VALUES.get(i % LONG_VALUES.size()), record.getSpeed());
			Assert.assertEquals("NetworkUtilizationRecord.txBytesPerSecond values are not equal.", 
			(double) DOUBLE_VALUES.get(i % DOUBLE_VALUES.size()), record.getTxBytesPerSecond(), 0.0000001);
			Assert.assertEquals("NetworkUtilizationRecord.txCarrierPerSecond values are not equal.", 
			(double) DOUBLE_VALUES.get(i % DOUBLE_VALUES.size()), record.getTxCarrierPerSecond(), 0.0000001);
			Assert.assertEquals("NetworkUtilizationRecord.txCollisionsPerSecond values are not equal.", 
			(double) DOUBLE_VALUES.get(i % DOUBLE_VALUES.size()), record.getTxCollisionsPerSecond(), 0.0000001);
			Assert.assertEquals("NetworkUtilizationRecord.txDroppedPerSecond values are not equal.", 
			(double) DOUBLE_VALUES.get(i % DOUBLE_VALUES.size()), record.getTxDroppedPerSecond(), 0.0000001);
			Assert.assertEquals("NetworkUtilizationRecord.txErrorsPerSecond values are not equal.", 
			(double) DOUBLE_VALUES.get(i % DOUBLE_VALUES.size()), record.getTxErrorsPerSecond(), 0.0000001);
			Assert.assertEquals("NetworkUtilizationRecord.txOverrunsPerSecond values are not equal.", 
			(double) DOUBLE_VALUES.get(i % DOUBLE_VALUES.size()), record.getTxOverrunsPerSecond(), 0.0000001);
			Assert.assertEquals("NetworkUtilizationRecord.txPacketsPerSecond values are not equal.", 
			(double) DOUBLE_VALUES.get(i % DOUBLE_VALUES.size()), record.getTxPacketsPerSecond(), 0.0000001);
			Assert.assertEquals("NetworkUtilizationRecord.rxBytesPerSecond values are not equal.", 
			(double) DOUBLE_VALUES.get(i % DOUBLE_VALUES.size()), record.getRxBytesPerSecond(), 0.0000001);
			Assert.assertEquals("NetworkUtilizationRecord.rxDroppedPerSecond values are not equal.", 
			(double) DOUBLE_VALUES.get(i % DOUBLE_VALUES.size()), record.getRxDroppedPerSecond(), 0.0000001);
			Assert.assertEquals("NetworkUtilizationRecord.rxErrorsPerSecond values are not equal.", 
			(double) DOUBLE_VALUES.get(i % DOUBLE_VALUES.size()), record.getRxErrorsPerSecond(), 0.0000001);
			Assert.assertEquals("NetworkUtilizationRecord.rxFramePerSecond values are not equal.", 
			(double) DOUBLE_VALUES.get(i % DOUBLE_VALUES.size()), record.getRxFramePerSecond(), 0.0000001);
			Assert.assertEquals("NetworkUtilizationRecord.rxOverrunsPerSecond values are not equal.", 
			(double) DOUBLE_VALUES.get(i % DOUBLE_VALUES.size()), record.getRxOverrunsPerSecond(), 0.0000001);
			Assert.assertEquals("NetworkUtilizationRecord.rxPacketsPerSecond values are not equal.", 
			(double) DOUBLE_VALUES.get(i % DOUBLE_VALUES.size()), record.getRxPacketsPerSecond(), 0.0000001);
		}
	}
	
	/**
	 * Tests {@link NetworkUtilizationRecord#TestNetworkUtilizationRecord(long, string, string, long, double, double, double, double, double, double, double, double, double, double, double, double, double)}.
	 */
	@Test
	public void testParameterConstruction() { // NOPMD (assert missing)
		for (int i=0;i<ARRAY_LENGTH;i++) {
			// initialize
			NetworkUtilizationRecord record = new NetworkUtilizationRecord(LONG_VALUES.get(i % LONG_VALUES.size()), STRING_VALUES.get(i % STRING_VALUES.size()), STRING_VALUES.get(i % STRING_VALUES.size()), LONG_VALUES.get(i % LONG_VALUES.size()), DOUBLE_VALUES.get(i % DOUBLE_VALUES.size()), DOUBLE_VALUES.get(i % DOUBLE_VALUES.size()), DOUBLE_VALUES.get(i % DOUBLE_VALUES.size()), DOUBLE_VALUES.get(i % DOUBLE_VALUES.size()), DOUBLE_VALUES.get(i % DOUBLE_VALUES.size()), DOUBLE_VALUES.get(i % DOUBLE_VALUES.size()), DOUBLE_VALUES.get(i % DOUBLE_VALUES.size()), DOUBLE_VALUES.get(i % DOUBLE_VALUES.size()), DOUBLE_VALUES.get(i % DOUBLE_VALUES.size()), DOUBLE_VALUES.get(i % DOUBLE_VALUES.size()), DOUBLE_VALUES.get(i % DOUBLE_VALUES.size()), DOUBLE_VALUES.get(i % DOUBLE_VALUES.size()), DOUBLE_VALUES.get(i % DOUBLE_VALUES.size()));
			
			// check values
			Assert.assertEquals("NetworkUtilizationRecord.timestamp values are not equal.", (long) LONG_VALUES.get(i % LONG_VALUES.size()), record.getTimestamp());
			Assert.assertEquals("NetworkUtilizationRecord.hostname values are not equal.", STRING_VALUES.get(i % STRING_VALUES.size()) == null?"":STRING_VALUES.get(i % STRING_VALUES.size()), record.getHostname());
			Assert.assertEquals("NetworkUtilizationRecord.interfaceName values are not equal.", STRING_VALUES.get(i % STRING_VALUES.size()) == null?"":STRING_VALUES.get(i % STRING_VALUES.size()), record.getInterfaceName());
			Assert.assertEquals("NetworkUtilizationRecord.speed values are not equal.", (long) LONG_VALUES.get(i % LONG_VALUES.size()), record.getSpeed());
			Assert.assertEquals("NetworkUtilizationRecord.txBytesPerSecond values are not equal.", 
			(double) DOUBLE_VALUES.get(i % DOUBLE_VALUES.size()), record.getTxBytesPerSecond(), 0.0000001);
			Assert.assertEquals("NetworkUtilizationRecord.txCarrierPerSecond values are not equal.", 
			(double) DOUBLE_VALUES.get(i % DOUBLE_VALUES.size()), record.getTxCarrierPerSecond(), 0.0000001);
			Assert.assertEquals("NetworkUtilizationRecord.txCollisionsPerSecond values are not equal.", 
			(double) DOUBLE_VALUES.get(i % DOUBLE_VALUES.size()), record.getTxCollisionsPerSecond(), 0.0000001);
			Assert.assertEquals("NetworkUtilizationRecord.txDroppedPerSecond values are not equal.", 
			(double) DOUBLE_VALUES.get(i % DOUBLE_VALUES.size()), record.getTxDroppedPerSecond(), 0.0000001);
			Assert.assertEquals("NetworkUtilizationRecord.txErrorsPerSecond values are not equal.", 
			(double) DOUBLE_VALUES.get(i % DOUBLE_VALUES.size()), record.getTxErrorsPerSecond(), 0.0000001);
			Assert.assertEquals("NetworkUtilizationRecord.txOverrunsPerSecond values are not equal.", 
			(double) DOUBLE_VALUES.get(i % DOUBLE_VALUES.size()), record.getTxOverrunsPerSecond(), 0.0000001);
			Assert.assertEquals("NetworkUtilizationRecord.txPacketsPerSecond values are not equal.", 
			(double) DOUBLE_VALUES.get(i % DOUBLE_VALUES.size()), record.getTxPacketsPerSecond(), 0.0000001);
			Assert.assertEquals("NetworkUtilizationRecord.rxBytesPerSecond values are not equal.", 
			(double) DOUBLE_VALUES.get(i % DOUBLE_VALUES.size()), record.getRxBytesPerSecond(), 0.0000001);
			Assert.assertEquals("NetworkUtilizationRecord.rxDroppedPerSecond values are not equal.", 
			(double) DOUBLE_VALUES.get(i % DOUBLE_VALUES.size()), record.getRxDroppedPerSecond(), 0.0000001);
			Assert.assertEquals("NetworkUtilizationRecord.rxErrorsPerSecond values are not equal.", 
			(double) DOUBLE_VALUES.get(i % DOUBLE_VALUES.size()), record.getRxErrorsPerSecond(), 0.0000001);
			Assert.assertEquals("NetworkUtilizationRecord.rxFramePerSecond values are not equal.", 
			(double) DOUBLE_VALUES.get(i % DOUBLE_VALUES.size()), record.getRxFramePerSecond(), 0.0000001);
			Assert.assertEquals("NetworkUtilizationRecord.rxOverrunsPerSecond values are not equal.", 
			(double) DOUBLE_VALUES.get(i % DOUBLE_VALUES.size()), record.getRxOverrunsPerSecond(), 0.0000001);
			Assert.assertEquals("NetworkUtilizationRecord.rxPacketsPerSecond values are not equal.", 
			(double) DOUBLE_VALUES.get(i % DOUBLE_VALUES.size()), record.getRxPacketsPerSecond(), 0.0000001);
		}
	}
	
	@Test
	public void testEquality() {
		int i = 0;
		NetworkUtilizationRecord oneRecord = new NetworkUtilizationRecord(LONG_VALUES.get(i % LONG_VALUES.size()), STRING_VALUES.get(i % STRING_VALUES.size()), STRING_VALUES.get(i % STRING_VALUES.size()), LONG_VALUES.get(i % LONG_VALUES.size()), DOUBLE_VALUES.get(i % DOUBLE_VALUES.size()), DOUBLE_VALUES.get(i % DOUBLE_VALUES.size()), DOUBLE_VALUES.get(i % DOUBLE_VALUES.size()), DOUBLE_VALUES.get(i % DOUBLE_VALUES.size()), DOUBLE_VALUES.get(i % DOUBLE_VALUES.size()), DOUBLE_VALUES.get(i % DOUBLE_VALUES.size()), DOUBLE_VALUES.get(i % DOUBLE_VALUES.size()), DOUBLE_VALUES.get(i % DOUBLE_VALUES.size()), DOUBLE_VALUES.get(i % DOUBLE_VALUES.size()), DOUBLE_VALUES.get(i % DOUBLE_VALUES.size()), DOUBLE_VALUES.get(i % DOUBLE_VALUES.size()), DOUBLE_VALUES.get(i % DOUBLE_VALUES.size()), DOUBLE_VALUES.get(i % DOUBLE_VALUES.size()));
		i = 0;
		NetworkUtilizationRecord copiedRecord = new NetworkUtilizationRecord(LONG_VALUES.get(i % LONG_VALUES.size()), STRING_VALUES.get(i % STRING_VALUES.size()), STRING_VALUES.get(i % STRING_VALUES.size()), LONG_VALUES.get(i % LONG_VALUES.size()), DOUBLE_VALUES.get(i % DOUBLE_VALUES.size()), DOUBLE_VALUES.get(i % DOUBLE_VALUES.size()), DOUBLE_VALUES.get(i % DOUBLE_VALUES.size()), DOUBLE_VALUES.get(i % DOUBLE_VALUES.size()), DOUBLE_VALUES.get(i % DOUBLE_VALUES.size()), DOUBLE_VALUES.get(i % DOUBLE_VALUES.size()), DOUBLE_VALUES.get(i % DOUBLE_VALUES.size()), DOUBLE_VALUES.get(i % DOUBLE_VALUES.size()), DOUBLE_VALUES.get(i % DOUBLE_VALUES.size()), DOUBLE_VALUES.get(i % DOUBLE_VALUES.size()), DOUBLE_VALUES.get(i % DOUBLE_VALUES.size()), DOUBLE_VALUES.get(i % DOUBLE_VALUES.size()), DOUBLE_VALUES.get(i % DOUBLE_VALUES.size()));
		
		Assert.assertEquals(oneRecord, copiedRecord);
	}	
	
	@Test
	public void testUnequality() {
		int i = 0;
		NetworkUtilizationRecord oneRecord = new NetworkUtilizationRecord(LONG_VALUES.get(i % LONG_VALUES.size()), STRING_VALUES.get(i % STRING_VALUES.size()), STRING_VALUES.get(i % STRING_VALUES.size()), LONG_VALUES.get(i % LONG_VALUES.size()), DOUBLE_VALUES.get(i % DOUBLE_VALUES.size()), DOUBLE_VALUES.get(i % DOUBLE_VALUES.size()), DOUBLE_VALUES.get(i % DOUBLE_VALUES.size()), DOUBLE_VALUES.get(i % DOUBLE_VALUES.size()), DOUBLE_VALUES.get(i % DOUBLE_VALUES.size()), DOUBLE_VALUES.get(i % DOUBLE_VALUES.size()), DOUBLE_VALUES.get(i % DOUBLE_VALUES.size()), DOUBLE_VALUES.get(i % DOUBLE_VALUES.size()), DOUBLE_VALUES.get(i % DOUBLE_VALUES.size()), DOUBLE_VALUES.get(i % DOUBLE_VALUES.size()), DOUBLE_VALUES.get(i % DOUBLE_VALUES.size()), DOUBLE_VALUES.get(i % DOUBLE_VALUES.size()), DOUBLE_VALUES.get(i % DOUBLE_VALUES.size()));
		i = 2;
		NetworkUtilizationRecord anotherRecord = new NetworkUtilizationRecord(LONG_VALUES.get(i % LONG_VALUES.size()), STRING_VALUES.get(i % STRING_VALUES.size()), STRING_VALUES.get(i % STRING_VALUES.size()), LONG_VALUES.get(i % LONG_VALUES.size()), DOUBLE_VALUES.get(i % DOUBLE_VALUES.size()), DOUBLE_VALUES.get(i % DOUBLE_VALUES.size()), DOUBLE_VALUES.get(i % DOUBLE_VALUES.size()), DOUBLE_VALUES.get(i % DOUBLE_VALUES.size()), DOUBLE_VALUES.get(i % DOUBLE_VALUES.size()), DOUBLE_VALUES.get(i % DOUBLE_VALUES.size()), DOUBLE_VALUES.get(i % DOUBLE_VALUES.size()), DOUBLE_VALUES.get(i % DOUBLE_VALUES.size()), DOUBLE_VALUES.get(i % DOUBLE_VALUES.size()), DOUBLE_VALUES.get(i % DOUBLE_VALUES.size()), DOUBLE_VALUES.get(i % DOUBLE_VALUES.size()), DOUBLE_VALUES.get(i % DOUBLE_VALUES.size()), DOUBLE_VALUES.get(i % DOUBLE_VALUES.size()));
		
		Assert.assertNotEquals(oneRecord, anotherRecord);
	}
}
