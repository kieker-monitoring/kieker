/***************************************************************************
 * Copyright 2023 Kieker Project (http://kieker-monitoring.net)
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
public class JVMNonHeapDisplayFilter extends AbstractNonAggregatingDisplayFilter<MemoryRecord, CartesianChartModel> {

	private static final float BYTE_TO_MEGABYTE_CONVERSION_VALUE = 1.0f / (1024.0f * 1024.0f);

	private Map<Object, Number> committedNonHeapData;
	private Map<Object, Number> usedNonHeapData;

	private ChartSeries committedNonHeapSeries;
	private ChartSeries usedNonHeapSeries;

	public JVMNonHeapDisplayFilter(final Configuration configuration, final IProjectContext projectContext) {
		super(configuration, projectContext);
	}

	@Override
	protected CartesianChartModel createChartModel(final int numberOfEntries) {
		final CartesianChartModel model = new CartesianChartModel();

		this.committedNonHeapSeries = new ChartSeries();
		this.usedNonHeapSeries = new ChartSeries();

		this.committedNonHeapData = new LimitedHashMap<Object, Number>(numberOfEntries);
		this.usedNonHeapData = new LimitedHashMap<Object, Number>(numberOfEntries);

		model.addSeries(this.committedNonHeapSeries);
		model.addSeries(this.usedNonHeapSeries);

		this.committedNonHeapSeries.setData(this.committedNonHeapData);
		this.usedNonHeapSeries.setData(this.usedNonHeapData);

		this.committedNonHeapSeries.setLabel("Committed");
		this.usedNonHeapSeries.setLabel("Used");

		return model;
	}

	@Override
	protected void fillChartModelWithRecordData(final CartesianChartModel chartModel, final MemoryRecord record, final String minutesAndSeconds,
			final int numberOfEntries) {
		this.committedNonHeapData.put(minutesAndSeconds, JVMNonHeapDisplayFilter.BYTE_TO_MEGABYTE_CONVERSION_VALUE * record.getNonHeapCommittedBytes());
		this.usedNonHeapData.put(minutesAndSeconds, JVMNonHeapDisplayFilter.BYTE_TO_MEGABYTE_CONVERSION_VALUE * record.getNonHeapUsedBytes());
	}

}
