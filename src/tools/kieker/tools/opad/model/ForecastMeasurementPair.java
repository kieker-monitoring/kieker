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

package kieker.tools.opad.model;

/**
 * 
 * @author Tillmann Carlos Bielefeld
 * @since 1.10
 * 
 * @since 1.9
 */
public class ForecastMeasurementPair implements IForecastMeasurementPair {

	private final String name;
	private final Double forecast;
	private final Double measurement;
	private final long time;

	public ForecastMeasurementPair(final String name, final Double forecast, final Double measurement, final long time) {
		this.name = name;
		this.forecast = forecast;
		this.measurement = measurement;
		this.time = time;
	}

	@Override
	public String getName() {
		return this.name;
	}

	@Override
	public long getTime() {
		return this.time;
	}

	@Override
	public Double getValue() {
		return this.measurement;
	}

	@Override
	public Double getForecasted() {
		return this.forecast;
	}

}
