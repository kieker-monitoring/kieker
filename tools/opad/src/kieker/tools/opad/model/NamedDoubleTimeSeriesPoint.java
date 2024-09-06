/***************************************************************************
 * Copyright 2023 Kieker Project (http://kieker-monitoring.net)
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

package kieker.tools.opad.model;

import kieker.tools.opad.timeseries.TimeSeriesPoint;

/**
 * @author Tillmann Carlos Bielefeld
 * @since 1.10
 *
 */
public class NamedDoubleTimeSeriesPoint extends TimeSeriesPoint<Double> implements INamedElement {

	private final String name;

	/**
	 * Constructor.
	 *
	 * @param time
	 *            timestamp
	 * @param value
	 *            measurement value
	 * @param name
	 *            name of the ts point
	 */
	public NamedDoubleTimeSeriesPoint(final long time, final Double value, final String name) {
		super(time, value);
		this.name = name;
	}

	@Override
	public String getName() {
		return this.name;
	}

	public double getDoubleValue() {
		return this.getValue();
	}

	@Override
	public String toString() {
		return this.name + " >> " + super.toString();
	}
}
