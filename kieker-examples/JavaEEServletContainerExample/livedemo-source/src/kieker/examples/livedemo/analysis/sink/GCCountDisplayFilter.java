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

package kieker.examples.livedemo.analysis.sink;

import java.util.HashMap;
import java.util.Map;

import org.primefaces.model.chart.CartesianChartModel;
import org.primefaces.model.chart.ChartSeries;

import kieker.analysis.IProjectContext;
import kieker.common.configuration.Configuration;
import kieker.common.record.jvm.GCRecord;
import kieker.examples.livedemo.analysis.util.LimitedHashMap;

/**
 * @author Nils Christian Ehmke
 * 
 * @since 1.10
 */
public class GCCountDisplayFilter extends AbstractNonAggregatingDisplayFilter<GCRecord, CartesianChartModel> {

	private final Map<String, Map<Object, Number>> dataMap = new HashMap<String, Map<Object, Number>>();

	public GCCountDisplayFilter(final Configuration configuration, final IProjectContext projectContext) {
		super(configuration, projectContext);
	}

	@Override
	protected CartesianChartModel createChartModel(final int numberOfEntries) {
		return new CartesianChartModel();
	}

	@Override
	protected void fillChartModelWithRecordData(final CartesianChartModel chartModel, final GCRecord record, final String minutesAndSeconds,
			final int numberOfEntries) {
		final String gcName = record.getGcName();
		if (!this.dataMap.containsKey(gcName)) {
			final ChartSeries series = new ChartSeries();
			final Map<Object, Number> data = new LimitedHashMap<Object, Number>(numberOfEntries);

			chartModel.addSeries(series);
			series.setData(data);
			series.setLabel(gcName);
			this.dataMap.put(gcName, data);
		}

		final Map<Object, Number> data = this.dataMap.get(gcName);
		data.put(minutesAndSeconds, record.getCollectionCount());
	}

}
