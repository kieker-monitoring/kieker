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

import java.util.Map;

import org.primefaces.model.chart.CartesianChartModel;
import org.primefaces.model.chart.ChartSeries;

import kieker.analysis.IProjectContext;
import kieker.common.configuration.Configuration;
import kieker.common.record.system.MemSwapUsageRecord;
import kieker.examples.livedemo.analysis.util.LimitedHashMap;

/**
 * @author Nils Christian Ehmke
 * 
 * @since 1.10
 */
public class MemoryDisplayFilter extends AbstractNonAggregatingDisplayFilter<MemSwapUsageRecord, CartesianChartModel> {

	private Map<Object, Number> usedMemoryData;
	private Map<Object, Number> freeMemoryData;

	private ChartSeries usedMemorySeries;
	private ChartSeries freeMemorySeries;

	private final int byteToMB = 1048576;

	public MemoryDisplayFilter(final Configuration configuration, final IProjectContext projectContext) {
		super(configuration, projectContext);
	}

	@Override
	protected CartesianChartModel createChartModel(final int numberOfEntries) {
		final CartesianChartModel model = new CartesianChartModel();

		this.usedMemorySeries = new ChartSeries();
		this.freeMemorySeries = new ChartSeries();

		this.usedMemoryData = new LimitedHashMap<Object, Number>(numberOfEntries);
		this.freeMemoryData = new LimitedHashMap<Object, Number>(numberOfEntries);

		model.addSeries(this.usedMemorySeries);
		model.addSeries(this.freeMemorySeries);

		this.usedMemorySeries.setData(this.usedMemoryData);
		this.freeMemorySeries.setData(this.freeMemoryData);

		this.usedMemorySeries.setLabel("Used Memory");
		this.freeMemorySeries.setLabel("Free Memory");

		return model;
	}

	@Override
	protected void fillChartModelWithRecordData(final CartesianChartModel chartModel, final MemSwapUsageRecord record, final String minutesAndSeconds,
			final int numberOfEntries) {
		this.usedMemoryData.put(minutesAndSeconds, record.getMemUsed() / this.byteToMB);
		this.freeMemoryData.put(minutesAndSeconds, record.getMemFree() / this.byteToMB);
	}

}
