/***************************************************************************
 * Copyright 2013 Kieker Project (http://kieker-monitoring.net)
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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * This class is currently under development, mostly for test purposes, and not designed for productive deployment.
 * 
 * @author Nils Christian Ehmke
 * 
 * @since 1.8
 */
public class MeterGauge extends AbstractDisplay {

	private volatile List<Number> intervals;
	private volatile Number value;

	/**
	 * Creates a new instance of this class.
	 */
	public MeterGauge() {
		this.intervals = Collections.emptyList();
	}

	public void setIntervals(final List<Number> intervals) {
		this.intervals = new ArrayList<Number>(intervals);
	}

	public void setValue(final Number value) {
		this.value = value;
	}

	public List<Number> getIntervals() {
		return this.intervals;
	}

	public Number getValue() {
		return this.value;
	}

}
