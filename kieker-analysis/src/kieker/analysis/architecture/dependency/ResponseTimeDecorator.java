/***************************************************************************
 * Copyright 2022 Kieker Project (http://kieker-monitoring.net)
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

package kieker.analysis.architecture.dependency;

import java.time.temporal.ChronoUnit;

import kieker.analysis.graph.IVertex;
import kieker.analysis.util.time.ChronoUnitToSymbolMapper;
import kieker.model.analysismodel.statistics.EPredefinedUnits;
import kieker.model.analysismodel.statistics.EPropertyType;
import kieker.model.analysismodel.statistics.StatisticsModel;

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
		vertex.setPropertyIfAbsent(PropertyConstants.MIN_REPSONSE_TIME, this.getStatisticValue(object, EPropertyType.MIN));
		vertex.setPropertyIfAbsent(PropertyConstants.MAX_REPSONSE_TIME, this.getStatisticValue(object, EPropertyType.MAX));
		vertex.setPropertyIfAbsent(PropertyConstants.TOTAL_RESPONSE_TIME, this.getStatisticValue(object, EPropertyType.TOTAL));
		vertex.setPropertyIfAbsent(PropertyConstants.MEAN_REPSONSE_TIME, this.getStatisticValue(object, EPropertyType.MEAN));
		vertex.setPropertyIfAbsent(PropertyConstants.MEDIAN_REPSONSE_TIME, this.getStatisticValue(object, EPropertyType.MEDIAN));
		vertex.setPropertyIfAbsent(PropertyConstants.TIME_UNIT, this.timeUnit);
	}

	private long getStatisticValue(final Object object, final EPropertyType property) {
		if (this.statisticsModel.getStatistics().containsKey(object)) {
			final Long value = (Long) this.statisticsModel.getStatistics().get(object).getStatistics().get(EPredefinedUnits.RESPONSE_TIME).getProperties()
					.get(property);
			if (value != null) {
				return value;
			}
		}
		return 0;
	}
}
