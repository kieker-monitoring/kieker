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

package kieker.analysisteetime.dependencygraphs;

import java.time.temporal.ChronoUnit;

import kieker.analysisteetime.statistics.IProperty;
import kieker.analysisteetime.statistics.Properties;
import kieker.analysisteetime.statistics.StatisticsModel;
import kieker.analysisteetime.statistics.Units;
import kieker.analysisteetime.util.graph.IVertex;
import kieker.analysisteetime.util.time.ChronoUnitToSymbolMapper;

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
		vertex.setPropertyIfAbsent(PropertyKeys.MIN_REPSONSE_TIME, this.getStatisticValue(object, Properties.MIN));
		vertex.setPropertyIfAbsent(PropertyKeys.MAX_REPSONSE_TIME, this.getStatisticValue(object, Properties.MAX));
		vertex.setPropertyIfAbsent(PropertyKeys.TOTAL_RESPONSE_TIME, this.getStatisticValue(object, Properties.TOTAL));
		vertex.setPropertyIfAbsent(PropertyKeys.MEAN_REPSONSE_TIME, this.getStatisticValue(object, Properties.AVERAGE));
		vertex.setPropertyIfAbsent(PropertyKeys.MEDIAN_REPSONSE_TIME, this.getStatisticValue(object, Properties.MEDIAN));
		vertex.setPropertyIfAbsent(PropertyKeys.TIME_UNIT, this.timeUnit);
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
