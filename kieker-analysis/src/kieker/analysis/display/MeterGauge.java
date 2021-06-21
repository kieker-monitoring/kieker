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

package kieker.analysis.display;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * This class is currently under development, mostly for test purposes, and not designed for productive deployment.
 *
 * @author Nils Christian Ehmke
 *
 * @since 1.8
 */
public class MeterGauge extends AbstractDisplay {

	private final Map<String, List<Number>> intervalMap;
	private final Map<String, Number> valueMap;
	private final Map<String, List<String>> intervalColorMap;

	/**
	 * Creates a new instance of this class.
	 */
	public MeterGauge() {
		this.intervalMap = new ConcurrentHashMap<>();
		this.intervalColorMap = new ConcurrentHashMap<>();
		this.valueMap = new ConcurrentHashMap<>();
	}

	/**
	 * Sets the intervals for the colors of the meter gauge with the given name.
	 *
	 * @param key
	 *            The name of the meter gauge "series".
	 * @param intervals
	 *            The intervals for the colors.
	 * @param colors
	 *            The colors of the intervals. It is assumed that the size of this list is the same as the one for the intervals. The colors are given as html string
	 *            (e.g., FF000).
	 */
	public void setIntervals(final String key, final List<Number> intervals, final List<String> colors) {
		this.intervalMap.put(key, intervals);
		this.intervalColorMap.put(key, colors);
	}

	/**
	 * Sets the value for the given meter gauge "series".
	 *
	 * @param key
	 *            The name of the meter gauge "series".
	 * @param value
	 *            The new value for the meter gauge.
	 */
	public void setValue(final String key, final Number value) {
		this.valueMap.put(key, value);
	}

	public Set<String> getKeys() {
		return this.intervalMap.keySet();
	}

	public List<Number> getIntervals(final String key) {
		return this.intervalMap.get(key);
	}

	public List<String> getIntervalColors(final String key) {
		return this.intervalColorMap.get(key);
	}

	public Number getValue(final String key) {
		return this.valueMap.get(key);
	}

}
