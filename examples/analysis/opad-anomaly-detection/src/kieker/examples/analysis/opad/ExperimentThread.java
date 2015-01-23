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

package kieker.examples.analysis.opad;

import java.util.List;

import kieker.analysis.exception.AnalysisConfigurationException;
import kieker.common.logging.Log;
import kieker.common.logging.LogFactory;
import kieker.tools.opad.model.ForecastMeasurementPair;
import kieker.tools.opad.model.NamedDoubleTimeSeriesPoint;
import kieker.tools.tslib.ForecastMethod;

/**
 * @author Thomas Duellmann
 *
 * @since 1.11
 */
public class ExperimentThread extends Thread {

	private static final Log LOG = LogFactory.getLog(ExperimentThread.class);
	private final List<NamedDoubleTimeSeriesPoint> readData;
	private final String fileName;
	private final ForecastMethod fcMethod;

	public ExperimentThread(final String fileName, final ForecastMethod fcMethod, final List<NamedDoubleTimeSeriesPoint> readData) {
		this.readData = readData;
		this.fileName = fileName;
		this.fcMethod = fcMethod;
	}

	@Override
	public void run() {
		ExperimentThread.LOG.info("Starting experiment for " + this.fcMethod.name());
		List<ForecastMeasurementPair> measurements;
		try {
			measurements = ExperimentStarter.forecastWithR(this.readData, this.fcMethod);
			ExperimentStarter.writeToCsv(measurements, this.fileName, this.fcMethod);
		} catch (final IllegalStateException e) {
			LOG.warn("An exception occurred", e);
		} catch (final AnalysisConfigurationException e) {
			LOG.warn("An exception occurred", e);
		}
		ExperimentThread.LOG.info("Done with experiment for " + this.fcMethod.name());
	}

}
