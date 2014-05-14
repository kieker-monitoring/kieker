/***************************************************************************
 * Copyright 2013 Kieker Project (http://kieker-monitoring.net)
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

import java.util.Deque;
import java.util.Map;

import kieker.analysis.IProjectContext;
import kieker.common.configuration.Configuration;
import kieker.common.record.jvm.CompilationRecord;
import livedemo.filter.display.util.LimitedHashMap;

import org.primefaces.model.chart.CartesianChartModel;
import org.primefaces.model.chart.ChartSeries;

/**
 * @author Nils Christian Ehmke
 * 
 * @since 1.10
 */
public class CompilationDisplayFilter extends AbstractDisplayFilter<CompilationRecord, CartesianChartModel> {

	private Map<Object, Number> totalCompilationTimeData;
	private ChartSeries totalCompilationTimeSeries;

	public CompilationDisplayFilter(final Configuration configuration, final IProjectContext projectContext) {
		super(configuration, projectContext);
	}

	@Override
	protected CartesianChartModel createChartModel(final int numberOfEntries) {
		final CartesianChartModel model = new CartesianChartModel();

		this.totalCompilationTimeSeries = new ChartSeries();
		this.totalCompilationTimeData = new LimitedHashMap<>(numberOfEntries);

		model.addSeries(this.totalCompilationTimeSeries);

		this.totalCompilationTimeSeries.setData(this.totalCompilationTimeData);
		this.totalCompilationTimeSeries.setLabel("Total compilation time");

		return model;
	}

	@Override
	protected void fillChartModelWithRecordData(final CartesianChartModel chartModel, final Deque<CompilationRecord> records, final String minutesAndSeconds,
			final int numberOfEntries) {
		if (!records.isEmpty()) {
			final CompilationRecord lastRecord = records.getLast();
			this.totalCompilationTimeData.put(minutesAndSeconds, lastRecord.getTotalCompilationTime());
		}
	}

}
