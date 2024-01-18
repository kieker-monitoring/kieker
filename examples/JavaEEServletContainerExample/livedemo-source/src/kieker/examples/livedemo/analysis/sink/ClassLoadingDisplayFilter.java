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

package kieker.examples.livedemo.analysis.sink;

import java.util.Map;

import org.primefaces.model.chart.CartesianChartModel;
import org.primefaces.model.chart.ChartSeries;

import kieker.analysis.IProjectContext;
import kieker.common.configuration.Configuration;
import kieker.common.record.jvm.ClassLoadingRecord;
import kieker.examples.livedemo.analysis.util.LimitedHashMap;

/**
 * @author Nils Christian Ehmke
 * 
 * @since 1.10
 */
public class ClassLoadingDisplayFilter extends AbstractNonAggregatingDisplayFilter<ClassLoadingRecord, CartesianChartModel> {

	private Map<Object, Number> totalLoadedClassesData;
	private Map<Object, Number> loadedClassesData;
	private Map<Object, Number> unloadedClassesData;

	private ChartSeries totalLoadedClassesSeries;
	private ChartSeries loadedClassesSeries;
	private ChartSeries unloadedClassesSeries;

	public ClassLoadingDisplayFilter(final Configuration configuration, final IProjectContext projectContext) {
		super(configuration, projectContext);
	}

	@Override
	protected CartesianChartModel createChartModel(final int numberOfEntries) {
		final CartesianChartModel model = new CartesianChartModel();

		this.totalLoadedClassesSeries = new ChartSeries();
		this.loadedClassesSeries = new ChartSeries();
		this.unloadedClassesSeries = new ChartSeries();

		this.totalLoadedClassesData = new LimitedHashMap<Object, Number>(numberOfEntries);
		this.loadedClassesData = new LimitedHashMap<Object, Number>(numberOfEntries);
		this.unloadedClassesData = new LimitedHashMap<Object, Number>(numberOfEntries);

		model.addSeries(this.totalLoadedClassesSeries);
		model.addSeries(this.loadedClassesSeries);
		model.addSeries(this.unloadedClassesSeries);

		this.totalLoadedClassesSeries.setData(this.totalLoadedClassesData);
		this.loadedClassesSeries.setData(this.loadedClassesData);
		this.unloadedClassesSeries.setData(this.unloadedClassesData);

		this.totalLoadedClassesSeries.setLabel("Total loaded classes");
		this.loadedClassesSeries.setLabel("Loaded classes");
		this.unloadedClassesSeries.setLabel("Unloaded classes");

		return model;
	}

	@Override
	protected void fillChartModelWithRecordData(final CartesianChartModel chartModel, final ClassLoadingRecord record, final String minutesAndSeconds,
			final int numberOfEntries) {
		this.totalLoadedClassesData.put(minutesAndSeconds, record.getTotalLoadedClassCount());
		this.loadedClassesData.put(minutesAndSeconds, record.getLoadedClassCount());
		this.unloadedClassesData.put(minutesAndSeconds, record.getUnloadedClassCount());
	}

}
