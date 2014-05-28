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
package kieker.panalysis.examples.throughput;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import org.junit.Before;
import org.junit.Test;

import de.chw.util.StopWatch;

import kieker.common.logging.LogFactory;
import kieker.panalysis.util.StatisticsUtil;

/**
 * @author Christian Wulf
 * 
 * @since 1.10
 */
public class ThroughputTimestampAnalysisTest {

	private static final int NUM_OBJECTS_TO_CREATE = 100000;

	@Before
	public void before() {
		System.setProperty(LogFactory.CUSTOM_LOGGER_JVM, "NONE");
	}

	// Using QueuePipes ist 1/3 faster than using MethodCallPipes
	// TODO check why

	@Test
	public void testWithManyObjects() {
		final StopWatch stopWatch = new StopWatch();
		final List<TimestampObject> timestampObjects = new ArrayList<TimestampObject>(NUM_OBJECTS_TO_CREATE);

		final ThroughputTimestampAnalysis analysis = new ThroughputTimestampAnalysis();
		analysis.setNumNoopFilters(800);
		analysis.setTimestampObjects(timestampObjects);
		analysis.setInput(NUM_OBJECTS_TO_CREATE, new Callable<TimestampObject>() {
			public TimestampObject call() throws Exception {
				return new TimestampObject();
			}
		});
		analysis.init();

		stopWatch.start();
		try {
			analysis.start();
		} finally {
			stopWatch.end();
		}

		StatisticsUtil.printStatistics(stopWatch.getDuration(), timestampObjects);
	}

}
