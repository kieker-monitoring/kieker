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

package livedemo.managedbeans;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;
import java.util.concurrent.ConcurrentHashMap;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.event.ValueChangeEvent;

import org.primefaces.model.chart.CartesianChartModel;
import org.primefaces.model.chart.ChartSeries;

import kieker.analysis.display.XYPlot;

/**
 * @author Bjoern Weissenfels
 * 
 * @since 1.9
 */
@ManagedBean(name = "methodResponsetimeBean", eager = true)
@ViewScoped
public class MethodResponsetimeBean implements Observer {

	@ManagedProperty(value = "#{analysisBean}")
	private AnalysisBean analysisBean;

	private final List<String> availableMethods;
	private List<String> selectedMethods;
	private int maxY;
	private boolean selectButton = true;

	private XYPlot methodResponsetimeXYplot;
	private XYPlot methodCallsXYplot;
	private final CartesianChartModel responsetimeModel;
	private final CartesianChartModel countingModel;
	private final Map<String, String> longToShortSignatures;
	private final Map<String, String> shortToLongSignatures;

	public MethodResponsetimeBean() {
		this.availableMethods = new ArrayList<String>();
		this.selectedMethods = new ArrayList<String>();
		this.maxY = 1;
		this.responsetimeModel = new CartesianChartModel();
		this.countingModel = new CartesianChartModel();
		this.longToShortSignatures = new ConcurrentHashMap<String, String>();
		this.shortToLongSignatures = new ConcurrentHashMap<String, String>();
	}

	@PostConstruct
	public void init() {
		this.methodResponsetimeXYplot = this.analysisBean.getMethodResponsetimeDisplayFilter().getMethodResponsetimeXYPlot();
		this.methodCallsXYplot = this.analysisBean.getMethodResponsetimeDisplayFilter().getMethodCallsXYPlot();
		for (final String signature : this.methodResponsetimeXYplot.getKeys()) {
			final String shortSignature = this.createShortSignature(signature);
			if (!this.availableMethods.contains(shortSignature)) {
				this.availableMethods.add(shortSignature);
				this.longToShortSignatures.put(signature, shortSignature);
				this.shortToLongSignatures.put(shortSignature, signature);
			}
		}
		this.updateModels();
		this.analysisBean.getUpdateThread().addObserver(this);
	}

	@PreDestroy
	public void terminate() {
		this.analysisBean.getUpdateThread().deleteObserver(this);
	}

	public void setAnalysisBean(final AnalysisBean analysisBean) {
		this.analysisBean = analysisBean;
	}

	public void onChange(final ValueChangeEvent event) {
		if (this.selectButton) {
			this.selectedMethods.clear();
			this.selectedMethods.addAll(this.availableMethods);
		} else {
			this.selectedMethods.clear();
		}
	}

	public List<String> getAvailableMethods() {
		return this.availableMethods;
	}

	public void setSelectedMethods(final List<String> selectedMethods) {
		this.selectedMethods = selectedMethods;
	}

	public List<String> getSelectedMethods() {
		if (this.selectedMethods.isEmpty() && !this.availableMethods.isEmpty()) {
			this.selectedMethods.add(this.availableMethods.get(0));
		}
		return this.selectedMethods;
	}

	public synchronized CartesianChartModel getResponsetimeModel() {
		return this.responsetimeModel;
	}

	public synchronized CartesianChartModel getCountingModel() {
		return this.countingModel;
	}

	public void setMaxY(final int maxY) {
		this.maxY = maxY;
	}

	public int getMaxY() {
		return this.maxY;
	}

	public void setSelectButton(final boolean selectButton) {
		this.selectButton = selectButton;
	}

	public boolean isSelectButton() {
		return this.selectButton;
	}

	private String createShortSignature(final String signature) {
		String[] array = signature.split("\\(");
		array = array[0].split("\\.");
		final int end = array.length;
		final String result = "..." + array[end - 2] + "." + array[end - 1] + "(...)";
		return result;
	}

	@SuppressWarnings("unused")
	private double convertFromNanosToMillis(final long duration) {
		return Math.round(duration / 100000.0) / 10.0;
	}

	private synchronized void updateModels() {
		for (final String shortSignature : this.getSelectedMethods()) {
			final String signature = this.shortToLongSignatures.get(shortSignature);

			final ChartSeries responsetimes = new ChartSeries();
			responsetimes.setLabel(shortSignature);
			final Map<Object, Number> map = this.methodResponsetimeXYplot.getEntries(signature);
			responsetimes.setData(map);
			this.responsetimeModel.addSeries(responsetimes);

			final ChartSeries countings = new ChartSeries();
			countings.setLabel(shortSignature);
			final Map<Object, Number> countMap = this.methodCallsXYplot.getEntries(signature);
			this.maxY = this.calculateMaxY(countMap.values());
			countings.setData(countMap);
			this.countingModel.addSeries(countings);
		}
	}

	private int calculateMaxY(final Collection<Number> numbers) {
		int max = 1;
		for (final Number n : numbers) {
			max = Math.max(max, n.intValue());
		}
		max = (max + 4) - (max % 4);
		return max;
	}

	@Override
	public synchronized void update(final Observable o, final Object arg) {
		this.responsetimeModel.clear();
		this.countingModel.clear();

		this.updateModels();

		// look for new methods
		for (final String signature : this.methodResponsetimeXYplot.getKeys()) {
			if (null == this.longToShortSignatures.get(signature)) {
				final String shortSignature = this.createShortSignature(signature);
				this.availableMethods.add(shortSignature);
				this.longToShortSignatures.put(signature, shortSignature);
				this.shortToLongSignatures.put(shortSignature, signature);
			}

		}

	}

}
