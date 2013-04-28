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

package kieker.tools.opad.filter;

import java.util.concurrent.ConcurrentHashMap;

import kieker.analysis.IProjectContext;
import kieker.analysis.plugin.annotation.InputPort;
import kieker.analysis.plugin.annotation.OutputPort;
import kieker.analysis.plugin.annotation.Plugin;
import kieker.analysis.plugin.filter.AbstractFilterPlugin;
import kieker.common.configuration.Configuration;
import kieker.tools.opad.record.ForecastMeasurementPair;
import kieker.tools.opad.record.IForecastMeasurementPair;
import kieker.tools.opad.record.NamedDoubleTimeSeriesPoint;
import kieker.tools.tslib.ITimeSeriesPoint;

/**
 * This Filter brings a Measurement Point and a corresponding Forecasting value together.
 * 
 * @author Tom Frotscher
 * 
 */
@Plugin(name = "UniteMeasurementPair Filter", outputPorts = {
	@OutputPort(eventTypes = { IForecastMeasurementPair.class }, name = UniteMeasurementPairFilter.OUTPUT_PORT_NAME_FORECASTED_AND_CURRENT) })
public class UniteMeasurementPairFilter extends AbstractFilterPlugin {

	/**
	 * Name of the input port receiving the measurements.
	 */
	public static final String INPUT_PORT_NAME_TSPOINT = "tspoint";

	/**
	 * Name of the input port receiving the forecasts.
	 */
	public static final String INPUT_PORT_NAME_FORECAST = "forecast";

	/**
	 * Name of the output port delivering the corresponding measurement-forecast-pairs.
	 */
	public static final String OUTPUT_PORT_NAME_FORECASTED_AND_CURRENT = "forecastedcurrent";

	/**
	 * Stores incoming measurements and forecasts until a corresponding forecast or measurement is found.
	 */
	private final ConcurrentHashMap<Long, NamedDoubleTimeSeriesPoint> tsPointMap;

	private volatile boolean firstTSPoint = true;

	/**
	 * Creates a new instance of this class using the given parameters.
	 * 
	 * @param configuration
	 *            The configuration for this component.
	 * @param projectContext
	 *            The projectContext for this component.
	 */
	public UniteMeasurementPairFilter(final Configuration configuration, final IProjectContext projectContext) {
		super(configuration, projectContext);

		this.tsPointMap = new ConcurrentHashMap<Long, NamedDoubleTimeSeriesPoint>();
	}

	@Override
	public Configuration getCurrentConfiguration() {
		return new Configuration();
	}

	/**
	 * Method that represents the input port for incoming measurements.
	 * 
	 * @param input
	 *            The incoming measurement.
	 */
	@InputPort(eventTypes = { NamedDoubleTimeSeriesPoint.class }, name = UniteMeasurementPairFilter.INPUT_PORT_NAME_TSPOINT)
	public void inputTSPoint(final NamedDoubleTimeSeriesPoint input) {
		System.out.println("timestampmeasurements: " + input.getTime().getTime());
		if (!this.checkCorrespondingForecast(input)) {
			// no matching point found --> add it to the waiting map
			this.addTsPoint(input);
		}
	}

	/**
	 * Method that represents the input port for incoming forecast.
	 * 
	 * @param input
	 *            The incoming forecast.
	 */
	@InputPort(eventTypes = { IForecastMeasurementPair.class }, name = UniteMeasurementPairFilter.INPUT_PORT_NAME_FORECAST)
	public void inputForecastValue(final IForecastMeasurementPair input) {
		System.out.println("timestampforecasts: " + input.getTime().getTime());
		final NamedDoubleTimeSeriesPoint pointFromForecast = new NamedDoubleTimeSeriesPoint(input.getTime(), input.getForecasted(), input.getName());
		if (!this.checkCorrespondingMeasurement(pointFromForecast)) {
			// no matching point found --> add it to the waiting map
			this.addTsPoint(pointFromForecast);
		}

	}

	/**
	 * This method adds a TimeSeriesPoint to this class's tsPointMap.
	 * 
	 * @param p
	 *            TimeSeriesPoint to add.
	 */
	private void addTsPoint(final NamedDoubleTimeSeriesPoint p) {
		this.tsPointMap.put(p.getTime().getTime(), p);
	}

	/**
	 * This method checks the tsPointMap for a corresponding forecast to its measurement. If this check is successfully
	 * the measurement-forecast-pair is delivered.
	 * 
	 * @param p
	 *            Measurement
	 * @return
	 *         True, if corresponding forecast found
	 *         False, else
	 */
	private boolean checkCorrespondingForecast(final NamedDoubleTimeSeriesPoint p) {
		// The first measurement has no corresponding forecast --> submit a dummy (the measurement itself as forecast replacement)
		if (this.firstTSPoint) {
			this.firstTSPoint = false;
			final ForecastMeasurementPair fmp = new ForecastMeasurementPair(
					p.getName(),
					p.getValue(),
					p.getValue(),
					p.getTime());
			super.deliver(OUTPUT_PORT_NAME_FORECASTED_AND_CURRENT, fmp);
			return true;
		} else {
			final long key = p.getTime().getTime();
			if (this.tsPointMap.containsKey(key)) {
				final ForecastMeasurementPair fmp = new ForecastMeasurementPair(
						p.getName(),
						this.tsPointMap.get(key).getValue(),
						p.getValue(),
						p.getTime());
				super.deliver(OUTPUT_PORT_NAME_FORECASTED_AND_CURRENT, fmp);
				System.out.println("deliveredm");
				return true;
			}
			return false;
		}
	}

	/**
	 * This method checks the tsPointMap for a corresponding measurement to its forecast. If this check is successfully
	 * the measurement-forecast-pair is delivered.
	 * 
	 * @param p
	 *            forecast
	 * @return
	 *         True, if corresponding measurement found
	 *         False, else
	 */
	private boolean checkCorrespondingMeasurement(final ITimeSeriesPoint<Double> p) {
		final long key = p.getTime().getTime();
		if (this.tsPointMap.containsKey(key)) {
			final NamedDoubleTimeSeriesPoint m = this.tsPointMap.get(key);
			final ForecastMeasurementPair fmp = new ForecastMeasurementPair(
					m.getName(),
					p.getValue(),
					m.getValue(),
					m.getTime());
			super.deliver(OUTPUT_PORT_NAME_FORECASTED_AND_CURRENT, fmp);
			System.out.println("deliveredf");
			return true;
		}
		return false;
	}

}
