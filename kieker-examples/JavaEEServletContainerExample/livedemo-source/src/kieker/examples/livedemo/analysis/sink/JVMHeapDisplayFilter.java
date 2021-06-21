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

package kieker.examples.livedemo.analysis.sink;

import java.util.Map;

import org.primefaces.model.chart.CartesianChartModel;
import org.primefaces.model.chart.ChartSeries;

import kieker.analysis.IProjectContext;
import kieker.common.configuration.Configuration;
import kieker.common.record.jvm.MemoryRecord;
import kieker.examples.livedemo.analysis.util.LimitedHashMap;

/**
 * @author Nils Christian Ehmke
 * 
 * @since 1.10
 */
public class JVMHeapDisplayFilter extends AbstractNonAggregatingDisplayFilter<MemoryRecord, CartesianChartModel> {

	private static final float BYTE_TO_MEGABYTE_CONVERSION_VALUE = 1.0f / (1024.0f * 1024.0f);

	private Map<Object, Number> committedHeapData;
	private Map<Object, Number> usedHeapData;

	private ChartSeries committedHeapSeries;
	private ChartSeries usedHeapSeries;

	public JVMHeapDisplayFilter(final Configuration configuration, final IProjectContext projectContext) {
		super(configuration, projectContext);
	}

	@Override
	protected CartesianChartModel createChartModel(final int numberOfEntries) {
		final CartesianChartModel model = new CartesianChartModel();

		this.committedHeapSeries = new ChartSeries();
		this.usedHeapSeries = new ChartSeries();

		this.committedHeapData = new LimitedHashMap<Object, Number>(numberOfEntries);
		this.usedHeapData = new LimitedHashMap<Object, Number>(numberOfEntries);

		model.addSeries(this.committedHeapSeries);
		model.addSeries(this.usedHeapSeries);

		this.committedHeapSeries.setData(this.committedHeapData);
		this.usedHeapSeries.setData(this.usedHeapData);

		this.committedHeapSeries.setLabel("Committed");
		this.usedHeapSeries.setLabel("Used");

		return model;
	}

	@Override
	protected void fillChartModelWithRecordData(final CartesianChartModel chartModel, final MemoryRecord record, final String minutesAndSeconds,
			final int numberOfEntries) {
		this.committedHeapData.put(minutesAndSeconds, JVMHeapDisplayFilter.BYTE_TO_MEGABYTE_CONVERSION_VALUE * record.getHeapCommittedBytes());
		this.usedHeapData.put(minutesAndSeconds, JVMHeapDisplayFilter.BYTE_TO_MEGABYTE_CONVERSION_VALUE * record.getHeapUsedBytes());
	}

}
