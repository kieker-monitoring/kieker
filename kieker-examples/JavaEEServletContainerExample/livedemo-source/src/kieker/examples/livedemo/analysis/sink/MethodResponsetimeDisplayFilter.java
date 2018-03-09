/***************************************************************************
 * Copyright 2017 Kieker Project (http://kieker-monitoring.net)
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
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

import org.primefaces.model.chart.CartesianChartModel;
import org.primefaces.model.chart.ChartSeries;

import kieker.analysis.IProjectContext;
import kieker.common.configuration.Configuration;
import kieker.common.record.controlflow.OperationExecutionRecord;
import kieker.examples.livedemo.analysis.util.LimitedHashMap;
import kieker.examples.livedemo.analysis.util.Pair;

/**
 * @author Bjoern Weissenfels
 * 
 * @since 1.9
 */
public class MethodResponsetimeDisplayFilter extends AbstractAggregatingDisplayFilter<OperationExecutionRecord, CartesianChartModel> {

	private Map<String, Map<Object, Number>> chartMaps;
	private final Map<String, Map<Object, Number>> countMaps;
	private final Map<String, Pair<Long, Integer>> signatureResponsetimeMap;
	private final CartesianChartModel countModel;

	public MethodResponsetimeDisplayFilter(final Configuration configuration, final IProjectContext projectContext) {
		super(configuration, projectContext);
		this.signatureResponsetimeMap = new ConcurrentHashMap<String, Pair<Long, Integer>>();
		this.countModel = new CartesianChartModel();
		this.countMaps = new ConcurrentHashMap<String, Map<Object, Number>>();
	}

	public synchronized CartesianChartModel getCountModel() {
		return this.countModel;
	}

	@Override
	protected CartesianChartModel createChartModel(final int numberOfEntries) {
		final CartesianChartModel model = new CartesianChartModel();
		this.chartMaps = new ConcurrentHashMap<String, Map<Object, Number>>();
		return model;
	}

	@Override
	protected void fillChartModelWithRecordData(final CartesianChartModel chartModel, final Queue<OperationExecutionRecord> records, final String minutesAndSeconds,
			final int numberOfEntries) {
		this.signatureResponsetimeMap.clear();
		for (final OperationExecutionRecord record : records) {
			final String shortSignature = this.createShortSignature(record.getOperationSignature());
			final long responseTime = record.getTout() - record.getTin();
			if (this.signatureResponsetimeMap.containsKey(shortSignature)) {
				final Pair<Long, Integer> pair = this.signatureResponsetimeMap.get(shortSignature);
				pair.setFirst(pair.getFirst() + responseTime);
				pair.setLast(pair.getLast() + 1);
			} else {
				final Pair<Long, Integer> pair = new Pair<Long, Integer>(responseTime, 1);
				this.signatureResponsetimeMap.put(shortSignature, pair);
			}
		}
		for (final String key : this.signatureResponsetimeMap.keySet()) {
			final Pair<Long, Integer> pair = this.signatureResponsetimeMap.get(key);
			final int methodCount = pair.getLast();
			final long responsetime = pair.getFirst() / methodCount;
			if (this.chartMaps.containsKey(key)) {
				this.chartMaps.get(key).put(minutesAndSeconds, this.convertFromNanosToMillis(responsetime));
				this.countMaps.get(key).put(minutesAndSeconds, methodCount);
			} else {
				final Map<Object, Number> newMap = new LimitedHashMap<Object, Number>(numberOfEntries);
				newMap.put(minutesAndSeconds, this.convertFromNanosToMillis(responsetime));
				this.chartMaps.put(key, newMap);
				final ChartSeries series = new ChartSeries();
				series.setData(newMap);
				series.setLabel(key);
				chartModel.addSeries(series);
				final Map<Object, Number> newCountMap = new LimitedHashMap<Object, Number>(numberOfEntries);
				newCountMap.put(minutesAndSeconds, methodCount);
				this.countMaps.put(key, newCountMap);
				final ChartSeries countSeries = new ChartSeries();
				countSeries.setData(newCountMap);
				countSeries.setLabel(key);
				this.countModel.addSeries(countSeries);
			}
		}
		for (final String key : this.chartMaps.keySet()) {
			if (!this.signatureResponsetimeMap.containsKey(key)) {
				this.chartMaps.get(key).put(minutesAndSeconds, 0);
				this.countMaps.get(key).put(minutesAndSeconds, 0);
			}
		}

	}

	private double convertFromNanosToMillis(final long duration) {
		final long tempDuration = TimeUnit.NANOSECONDS.convert(duration, this.timeunit);
		return Math.round(tempDuration / 100000.0) / 10.0;
	}

	private String createShortSignature(final String signature) {
		String[] array = signature.split("\\(");
		array = array[0].split("\\.");
		final int end = array.length;
		return "..." + array[end - 2] + "." + array[end - 1] + "(...)";
	}

}
