/***************************************************************************
 * Copyright 2012 Kieker Project (http://kieker-monitoring.net)
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

package kieker.tools.opad.record;

import java.util.Date;

/**
 * 
 * @author Tillmann Carlos Bielefeld
 * 
 */
public class ForecastMeasurementPair implements IForecastMeasurementPair {

	private final String name;
	private final Double forecast;
	private final Double measurement;
	private final Date time;

	public ForecastMeasurementPair(final String name, final Double forecast,
			final Double measurement, final Date time) {
		super();
		this.name = name;
		this.forecast = forecast;
		this.measurement = measurement;
		this.time = time;
	}

	public String getName() {
		return this.name;
	}

	public Date getTime() {
		return this.time;
	}

	public Double getValue() {
		return this.measurement;
	}

	public Double getForecasted() {
		return this.forecast;
	}

}
