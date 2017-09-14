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

package kieker.analysis.display;

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
public class PieChart extends AbstractDisplay {

	private final Map<String, Number> valueMap;

	/**
	 * Creates a new instance of this class.
	 */
	public PieChart() {
		this.valueMap = new ConcurrentHashMap<String, Number>();
	}

	public void setValue(final String key, final Number value) {
		this.valueMap.put(key, value);
	}

	public Set<String> getKeys() {
		return this.valueMap.keySet();
	}

	public Number getValue(final String key) {
		return this.valueMap.get(key);
	}
}
