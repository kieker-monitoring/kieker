/***************************************************************************
 * Copyright 2022 Kieker Project (http://kieker-monitoring.net)
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

package kieker.analysis.junit.plugin.filter.record.delayfilter;

/**
 *
 * @author Andre van Hoorn
 *
 * @since 1.7
 */
public class TestRealtimeRecordDelayFilterNoAcceleration extends AbstractTestRealtimeRecordDelayFilter {

	private static final long[] EVENT_TIME_OFFSETS_SECONDS = { 0L, 1L, 2L, 7L, 17L, 19L }; // relative to the start time
	// intervals of length INTERVAL_SIZE_NANOS relative to start time
	private static final long[] EXPECTED_THROUGHPUT_LIST_OFFSET_SECS_INTERVAL_5SECS = {
		3L, // i.e., in interval (0,5(
		1L, // i.e., in interval (5,10(
		0L, // i.e., in interval (10,15(
		2L, // i.e., in interval (15,20(
	};

	public TestRealtimeRecordDelayFilterNoAcceleration() {
		super(EVENT_TIME_OFFSETS_SECONDS, EXPECTED_THROUGHPUT_LIST_OFFSET_SECS_INTERVAL_5SECS, 1); // 1 = no acceleration/slow down
	}
}
