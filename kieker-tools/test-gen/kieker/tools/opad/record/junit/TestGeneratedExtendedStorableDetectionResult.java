/***************************************************************************
 * Copyright 2018 iObserve Project (https://www.iobserve-devops.net)
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
		
import org.junit.Assert;
import org.junit.Test;

import kieker.tools.opad.record.ExtendedStorableDetectionResult;

import kieker.test.common.junit.AbstractGeneratedKiekerTest;
		
/**
 * Creates {@link OperationExecutionRecord}s via the available constructors and
 * checks the values passed values via getters.
 * 
 * @author Thomas Duellmann
 * 
 * @since 1.10
 */
public class TestGeneratedExtendedStorableDetectionResult extends AbstractGeneratedKiekerTest {

	public TestGeneratedExtendedStorableDetectionResult() {
		// empty default constructor
	}

	/**
	 * Tests {@link ExtendedStorableDetectionResult#TestExtendedStorableDetectionResult(string, double, long, double, double, double)}.
	 */
	@Test
	public void testToArray() { // NOPMD (assert missing)
		for (int i=0;i<ARRAY_LENGTH;i++) {
			// initialize
			ExtendedStorableDetectionResult record = new ExtendedStorableDetectionResult(STRING_VALUES.get(i % STRING_VALUES.size()), DOUBLE_VALUES.get(i % DOUBLE_VALUES.size()), LONG_VALUES.get(i % LONG_VALUES.size()), DOUBLE_VALUES.get(i % DOUBLE_VALUES.size()), DOUBLE_VALUES.get(i % DOUBLE_VALUES.size()), DOUBLE_VALUES.get(i % DOUBLE_VALUES.size()));
			
			// check values
			Assert.assertEquals("ExtendedStorableDetectionResult.applicationName values are not equal.", STRING_VALUES.get(i % STRING_VALUES.size()) == null?"":STRING_VALUES.get(i % STRING_VALUES.size()), record.getApplicationName());
			Assert.assertEquals("ExtendedStorableDetectionResult.value values are not equal.", 
			(double) DOUBLE_VALUES.get(i % DOUBLE_VALUES.size()), record.getValue(), 0.0000001);
			Assert.assertEquals("ExtendedStorableDetectionResult.timestamp values are not equal.", (long) LONG_VALUES.get(i % LONG_VALUES.size()), record.getTimestamp());
			Assert.assertEquals("ExtendedStorableDetectionResult.forecast values are not equal.", 
			(double) DOUBLE_VALUES.get(i % DOUBLE_VALUES.size()), record.getForecast(), 0.0000001);
			Assert.assertEquals("ExtendedStorableDetectionResult.score values are not equal.", 
			(double) DOUBLE_VALUES.get(i % DOUBLE_VALUES.size()), record.getScore(), 0.0000001);
			Assert.assertEquals("ExtendedStorableDetectionResult.anomalyThreshold values are not equal.", 
			(double) DOUBLE_VALUES.get(i % DOUBLE_VALUES.size()), record.getAnomalyThreshold(), 0.0000001);
			
			Object[] values = record.toArray();
			
			Assert.assertNotNull("Record array serialization failed. No values array returned.", values);
			Assert.assertEquals("Record array size does not match expected number of properties 6.", 6, values.length);
			
			// check all object values exist
			Assert.assertNotNull("Array value [0] of type String must be not null.", values[0]); 
			Assert.assertNotNull("Array value [1] of type Double must be not null.", values[1]); 
			Assert.assertNotNull("Array value [2] of type Long must be not null.", values[2]); 
			Assert.assertNotNull("Array value [3] of type Double must be not null.", values[3]); 
			Assert.assertNotNull("Array value [4] of type Double must be not null.", values[4]); 
			Assert.assertNotNull("Array value [5] of type Double must be not null.", values[5]); 
			
			// check all types
			Assert.assertTrue("Type of array value [0] " + values[0].getClass().getCanonicalName() + " does not match the desired type String", values[0] instanceof String);
			Assert.assertTrue("Type of array value [1] " + values[1].getClass().getCanonicalName() + " does not match the desired type Double", values[1] instanceof Double);
			Assert.assertTrue("Type of array value [2] " + values[2].getClass().getCanonicalName() + " does not match the desired type Long", values[2] instanceof Long);
			Assert.assertTrue("Type of array value [3] " + values[3].getClass().getCanonicalName() + " does not match the desired type Double", values[3] instanceof Double);
			Assert.assertTrue("Type of array value [4] " + values[4].getClass().getCanonicalName() + " does not match the desired type Double", values[4] instanceof Double);
			Assert.assertTrue("Type of array value [5] " + values[5].getClass().getCanonicalName() + " does not match the desired type Double", values[5] instanceof Double);
								
			// check all object values 
			Assert.assertEquals("Array value [0] " + values[0] + " does not match the desired value " + STRING_VALUES.get(i % STRING_VALUES.size()),
				STRING_VALUES.get(i % STRING_VALUES.size()) == null?"":STRING_VALUES.get(i % STRING_VALUES.size()), values[0]
			);
			Assert.assertEquals("Array value [1] " + values[1] + " does not match the desired value " + DOUBLE_VALUES.get(i % DOUBLE_VALUES.size()),
				(double) DOUBLE_VALUES.get(i % DOUBLE_VALUES.size()), (double) (Double)values[1], 0.0000001
			);
			Assert.assertEquals("Array value [2] " + values[2] + " does not match the desired value " + LONG_VALUES.get(i % LONG_VALUES.size()),
				LONG_VALUES.get(i % LONG_VALUES.size()), values[2]
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
		}
	}
	
	/**
	 * Tests {@link ExtendedStorableDetectionResult#TestExtendedStorableDetectionResult(string, double, long, double, double, double)}.
	 */
	@Test
	public void testBuffer() { // NOPMD (assert missing)
		for (int i=0;i<ARRAY_LENGTH;i++) {
			// initialize
			ExtendedStorableDetectionResult record = new ExtendedStorableDetectionResult(STRING_VALUES.get(i % STRING_VALUES.size()), DOUBLE_VALUES.get(i % DOUBLE_VALUES.size()), LONG_VALUES.get(i % LONG_VALUES.size()), DOUBLE_VALUES.get(i % DOUBLE_VALUES.size()), DOUBLE_VALUES.get(i % DOUBLE_VALUES.size()), DOUBLE_VALUES.get(i % DOUBLE_VALUES.size()));
			
			// check values
			Assert.assertEquals("ExtendedStorableDetectionResult.applicationName values are not equal.", STRING_VALUES.get(i % STRING_VALUES.size()) == null?"":STRING_VALUES.get(i % STRING_VALUES.size()), record.getApplicationName());
			Assert.assertEquals("ExtendedStorableDetectionResult.value values are not equal.", 
			(double) DOUBLE_VALUES.get(i % DOUBLE_VALUES.size()), record.getValue(), 0.0000001);
			Assert.assertEquals("ExtendedStorableDetectionResult.timestamp values are not equal.", (long) LONG_VALUES.get(i % LONG_VALUES.size()), record.getTimestamp());
			Assert.assertEquals("ExtendedStorableDetectionResult.forecast values are not equal.", 
			(double) DOUBLE_VALUES.get(i % DOUBLE_VALUES.size()), record.getForecast(), 0.0000001);
			Assert.assertEquals("ExtendedStorableDetectionResult.score values are not equal.", 
			(double) DOUBLE_VALUES.get(i % DOUBLE_VALUES.size()), record.getScore(), 0.0000001);
			Assert.assertEquals("ExtendedStorableDetectionResult.anomalyThreshold values are not equal.", 
			(double) DOUBLE_VALUES.get(i % DOUBLE_VALUES.size()), record.getAnomalyThreshold(), 0.0000001);
		}
	}
	
	/**
	 * Tests {@link ExtendedStorableDetectionResult#TestExtendedStorableDetectionResult(string, double, long, double, double, double)}.
	 */
	@Test
	public void testParameterConstruction() { // NOPMD (assert missing)
		for (int i=0;i<ARRAY_LENGTH;i++) {
			// initialize
			ExtendedStorableDetectionResult record = new ExtendedStorableDetectionResult(STRING_VALUES.get(i % STRING_VALUES.size()), DOUBLE_VALUES.get(i % DOUBLE_VALUES.size()), LONG_VALUES.get(i % LONG_VALUES.size()), DOUBLE_VALUES.get(i % DOUBLE_VALUES.size()), DOUBLE_VALUES.get(i % DOUBLE_VALUES.size()), DOUBLE_VALUES.get(i % DOUBLE_VALUES.size()));
			
			// check values
			Assert.assertEquals("ExtendedStorableDetectionResult.applicationName values are not equal.", STRING_VALUES.get(i % STRING_VALUES.size()) == null?"":STRING_VALUES.get(i % STRING_VALUES.size()), record.getApplicationName());
			Assert.assertEquals("ExtendedStorableDetectionResult.value values are not equal.", 
			(double) DOUBLE_VALUES.get(i % DOUBLE_VALUES.size()), record.getValue(), 0.0000001);
			Assert.assertEquals("ExtendedStorableDetectionResult.timestamp values are not equal.", (long) LONG_VALUES.get(i % LONG_VALUES.size()), record.getTimestamp());
			Assert.assertEquals("ExtendedStorableDetectionResult.forecast values are not equal.", 
			(double) DOUBLE_VALUES.get(i % DOUBLE_VALUES.size()), record.getForecast(), 0.0000001);
			Assert.assertEquals("ExtendedStorableDetectionResult.score values are not equal.", 
			(double) DOUBLE_VALUES.get(i % DOUBLE_VALUES.size()), record.getScore(), 0.0000001);
			Assert.assertEquals("ExtendedStorableDetectionResult.anomalyThreshold values are not equal.", 
			(double) DOUBLE_VALUES.get(i % DOUBLE_VALUES.size()), record.getAnomalyThreshold(), 0.0000001);
		}
	}
	
	@Test
	public void testEquality() {
		int i = 0;
		ExtendedStorableDetectionResult oneRecord = new ExtendedStorableDetectionResult(STRING_VALUES.get(i % STRING_VALUES.size()), DOUBLE_VALUES.get(i % DOUBLE_VALUES.size()), LONG_VALUES.get(i % LONG_VALUES.size()), DOUBLE_VALUES.get(i % DOUBLE_VALUES.size()), DOUBLE_VALUES.get(i % DOUBLE_VALUES.size()), DOUBLE_VALUES.get(i % DOUBLE_VALUES.size()));
		i = 0;
		ExtendedStorableDetectionResult copiedRecord = new ExtendedStorableDetectionResult(STRING_VALUES.get(i % STRING_VALUES.size()), DOUBLE_VALUES.get(i % DOUBLE_VALUES.size()), LONG_VALUES.get(i % LONG_VALUES.size()), DOUBLE_VALUES.get(i % DOUBLE_VALUES.size()), DOUBLE_VALUES.get(i % DOUBLE_VALUES.size()), DOUBLE_VALUES.get(i % DOUBLE_VALUES.size()));
		
		Assert.assertEquals(oneRecord, copiedRecord);
	}	
	
	@Test
	public void testUnequality() {
		int i = 0;
		ExtendedStorableDetectionResult oneRecord = new ExtendedStorableDetectionResult(STRING_VALUES.get(i % STRING_VALUES.size()), DOUBLE_VALUES.get(i % DOUBLE_VALUES.size()), LONG_VALUES.get(i % LONG_VALUES.size()), DOUBLE_VALUES.get(i % DOUBLE_VALUES.size()), DOUBLE_VALUES.get(i % DOUBLE_VALUES.size()), DOUBLE_VALUES.get(i % DOUBLE_VALUES.size()));
		i = 2;
		ExtendedStorableDetectionResult anotherRecord = new ExtendedStorableDetectionResult(STRING_VALUES.get(i % STRING_VALUES.size()), DOUBLE_VALUES.get(i % DOUBLE_VALUES.size()), LONG_VALUES.get(i % LONG_VALUES.size()), DOUBLE_VALUES.get(i % DOUBLE_VALUES.size()), DOUBLE_VALUES.get(i % DOUBLE_VALUES.size()), DOUBLE_VALUES.get(i % DOUBLE_VALUES.size()));
		
		Assert.assertNotEquals(oneRecord, anotherRecord);
	}
}
