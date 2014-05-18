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

package livedemo.filter.display;

import java.util.Map;

import org.primefaces.model.chart.CartesianChartModel;
import org.primefaces.model.chart.ChartSeries;

import kieker.analysis.IProjectContext;
import kieker.common.configuration.Configuration;
import kieker.common.record.jvm.MemoryRecord;

import livedemo.filter.display.util.LimitedHashMap;

/**
 * @author Nils Christian Ehmke
 * 
 * @since 1.10
 */
public class JVMMemoryDisplayFilter extends AbstractNonAggregatingDisplayFilter<MemoryRecord, CartesianChartModel> {

	private Map<Object, Number> usedNonHeapData;
	private Map<Object, Number> usedHeapData;

	private ChartSeries usedNonHeapSeries;
	private ChartSeries usedHeapSeries;

	public JVMMemoryDisplayFilter(final Configuration configuration, final IProjectContext projectContext) {
		super(configuration, projectContext);
	}

	@Override
	protected CartesianChartModel createChartModel(final int numberOfEntries) {
		final CartesianChartModel model = new CartesianChartModel();

		this.usedNonHeapSeries = new ChartSeries();
		this.usedHeapSeries = new ChartSeries();

		this.usedNonHeapData = new LimitedHashMap<>(numberOfEntries);
		this.usedHeapData = new LimitedHashMap<>(numberOfEntries);

		model.addSeries(this.usedNonHeapSeries);
		model.addSeries(this.usedHeapSeries);

		this.usedNonHeapSeries.setData(this.usedNonHeapData);
		this.usedHeapSeries.setData(this.usedHeapData);

		this.usedNonHeapSeries.setLabel("Non Heap");
		this.usedHeapSeries.setLabel("Heap");

		return model;
	}

	@Override
	protected void fillChartModelWithRecordData(final CartesianChartModel chartModel, final MemoryRecord record, final String minutesAndSeconds,
			final int numberOfEntries) {
		this.usedNonHeapData.put(minutesAndSeconds, record.getNonHeapUsed());
		this.usedHeapData.put(minutesAndSeconds, record.getHeapUsed());
	}

}
