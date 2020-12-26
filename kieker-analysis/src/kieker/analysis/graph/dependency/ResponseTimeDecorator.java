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

package kieker.analysis.graph.dependency;

import java.time.temporal.ChronoUnit;

import kieker.analysis.graph.IVertex;
import kieker.analysis.statistics.IProperty;
import kieker.analysis.statistics.Properties;
import kieker.analysis.statistics.StatisticsModel;
import kieker.analysis.statistics.Units;
import kieker.analysis.util.time.ChronoUnitToSymbolMapper;

/**
 * This class adds corresponding statistics to vertices.
 *
 * @author Sören Henning
 *
 * @since 1.14
 */
public class ResponseTimeDecorator {

	private final StatisticsModel statisticsModel;
	private final String timeUnit;

	public ResponseTimeDecorator(final StatisticsModel statisticsModel, final ChronoUnit chronoUnit) {
		this.statisticsModel = statisticsModel;
		this.timeUnit = ChronoUnitToSymbolMapper.create().apply(chronoUnit);
	}

	public void decorate(final IVertex vertex, final Object object) {
		vertex.setPropertyIfAbsent(PropertyConstants.MIN_REPSONSE_TIME, this.getStatisticValue(object, Properties.MIN));
		vertex.setPropertyIfAbsent(PropertyConstants.MAX_REPSONSE_TIME, this.getStatisticValue(object, Properties.MAX));
		vertex.setPropertyIfAbsent(PropertyConstants.TOTAL_RESPONSE_TIME, this.getStatisticValue(object, Properties.TOTAL));
		vertex.setPropertyIfAbsent(PropertyConstants.MEAN_REPSONSE_TIME, this.getStatisticValue(object, Properties.AVERAGE));
		vertex.setPropertyIfAbsent(PropertyConstants.MEDIAN_REPSONSE_TIME, this.getStatisticValue(object, Properties.MEDIAN));
		vertex.setPropertyIfAbsent(PropertyConstants.TIME_UNIT, this.timeUnit);
	}

	private long getStatisticValue(final Object object, final IProperty property) {
		if (this.statisticsModel.has(object)) {
			final Long value = this.statisticsModel.get(object).getStatistic(Units.RESPONSE_TIME).getProperty(property);
			if (value != null) {
				return value;
			}
		}
		return 0;
	}
}
