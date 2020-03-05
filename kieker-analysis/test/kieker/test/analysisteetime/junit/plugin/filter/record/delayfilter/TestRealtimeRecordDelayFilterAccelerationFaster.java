/***************************************************************************
 * Copyright 2020 Kieker Project (http://kieker-monitoring.net)
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

package kieker.test.analysisteetime.junit.plugin.filter.record.delayfilter;

/**
 *
 * @author Andre van Hoorn
 *
 * @since 1.7
 */
public class TestRealtimeRecordDelayFilterAccelerationFaster extends AbstractTestRealtimeRecordDelayFilter {

	// Note for the following array that by considering the double speed, the actual array is
	// { 0, 2, 6, 7, 9, 11 }
	private static final long[] EVENT_TIME_OFFSETS_SECONDS = { 0L, 4L, 12L, 14L, 18L, 22L };

	// intervals of length INTERVAL_SIZE_NANOS relative to start time
	private static final long[] EXPECTED_THROUGHPUT_LIST_OFFSET_SECS_INTERVAL_5SECS = {
		2L, // i.e., in interval (0,5(
		3L, // i.e., in interval (5,10(
		1L, // i.e., in interval (10,15(
	};

	public TestRealtimeRecordDelayFilterAccelerationFaster() {
		super(EVENT_TIME_OFFSETS_SECONDS, EXPECTED_THROUGHPUT_LIST_OFFSET_SECS_INTERVAL_5SECS, 2); // 2 = double the speed
	}
}
