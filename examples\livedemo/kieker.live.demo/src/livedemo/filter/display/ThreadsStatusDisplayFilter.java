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

import java.util.Queue;
import java.util.SortedMap;
import java.util.TreeMap;

import kieker.analysis.IProjectContext;
import kieker.common.configuration.Configuration;
import kieker.common.record.jvm.ThreadsStatusRecord;

import org.primefaces.model.chart.CartesianChartModel;
import org.primefaces.model.chart.ChartSeries;

/**
 * @author Nils Christian Ehmke
 * 
 * @since 1.10
 */
public class ThreadsStatusDisplayFilter extends AbstractDisplayFilter<ThreadsStatusRecord, CartesianChartModel> {

	private SortedMap<Object, Number> threadsData;
	private SortedMap<Object, Number> peakThreadsData;
	private SortedMap<Object, Number> daemonThreadsData;

	private ChartSeries threadsSeries;
	private ChartSeries peakThreadsSeries;
	private ChartSeries daemonThreadsSeries;

	public ThreadsStatusDisplayFilter(final Configuration configuration, final IProjectContext projectContext) {
		super(configuration, projectContext);
	}

	@Override
	protected CartesianChartModel createChartModel() {
		final CartesianChartModel model = new CartesianChartModel();

		this.threadsSeries = new ChartSeries();
		this.peakThreadsSeries = new ChartSeries();
		this.daemonThreadsSeries = new ChartSeries();

		this.threadsData = new TreeMap<>();
		this.peakThreadsData = new TreeMap<>();
		this.daemonThreadsData = new TreeMap<>();

		model.addSeries(this.threadsSeries);
		model.addSeries(this.peakThreadsSeries);
		model.addSeries(this.daemonThreadsSeries);

		this.threadsSeries.setData(this.threadsData);
		this.peakThreadsSeries.setData(this.peakThreadsData);
		this.daemonThreadsSeries.setData(this.daemonThreadsData);

		this.threadsSeries.setLabel("Threads");
		this.peakThreadsSeries.setLabel("Peak threads");
		this.daemonThreadsSeries.setLabel("Daemon threads");

		return model;
	}

	@Override
	protected void fillChartModelWithRecordData(final CartesianChartModel chartModel, final Queue<ThreadsStatusRecord> records, final String minutesAndSeconds,
			final int numberOfEntries) {
		if (this.threadsData.size() >= numberOfEntries) {
			this.threadsData.remove(this.threadsData.firstKey());
			this.peakThreadsData.remove(this.peakThreadsData.firstKey());
			this.daemonThreadsData.remove(this.daemonThreadsData.firstKey());
		}

		for (final ThreadsStatusRecord record : records) {
			this.threadsData.put(minutesAndSeconds, record.getThreadCount());
			this.peakThreadsData.put(minutesAndSeconds, record.getPeakThreadCount());
			this.daemonThreadsData.put(minutesAndSeconds, record.getDaemonThreadCount());
		}
	}

}
