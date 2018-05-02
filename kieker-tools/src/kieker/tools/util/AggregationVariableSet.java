/***************************************************************************
 * Copyright 2018 Kieker Project (http://kieker-monitoring.net)
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

package kieker.tools.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import kieker.tools.opad.model.NamedDoubleTimeSeriesPoint;

/**
 * 
 * @author Tom Frotscher
 * @since 1.10
 * 
 */
public class AggregationVariableSet {

	private long firstTimestampInCurrentInterval;
	private long lastTimestampInCurrentInterval;
	private long firstIntervalStart;
	private List<NamedDoubleTimeSeriesPoint> aggregationList;

	/**
	 * Creates an instance of this class.
	 * Initializes the variables needed for the aggregation.
	 */
	public AggregationVariableSet() {
		this.firstTimestampInCurrentInterval = -1;
		this.lastTimestampInCurrentInterval = -1;
		this.firstIntervalStart = -1;
		this.aggregationList = Collections.synchronizedList(new ArrayList<NamedDoubleTimeSeriesPoint>());
	}

	public long getFirstTimestampInCurrentInterval() {
		return this.firstTimestampInCurrentInterval;
	}

	public long getLastTimestampInCurrentInterval() {
		return this.lastTimestampInCurrentInterval;
	}

	public long getFirstIntervalStart() {
		return this.firstIntervalStart;
	}

	public void setFirstTimestampInCurrentInterval(final long firstTimestampInCurrentInterval) {
		this.firstTimestampInCurrentInterval = firstTimestampInCurrentInterval;
	}

	public void setLastTimestampInCurrentInterval(final long lastTimestampInCurrentInterval) {
		this.lastTimestampInCurrentInterval = lastTimestampInCurrentInterval;
	}

	public void setFirstIntervalStart(final long firstIntervalStart) {
		this.firstIntervalStart = firstIntervalStart;
	}

	public List<NamedDoubleTimeSeriesPoint> getAggregationList() {
		return this.aggregationList;
	}

	public void setAggregationList(final List<NamedDoubleTimeSeriesPoint> aggregationList) {
		this.aggregationList = aggregationList;
	}

}
