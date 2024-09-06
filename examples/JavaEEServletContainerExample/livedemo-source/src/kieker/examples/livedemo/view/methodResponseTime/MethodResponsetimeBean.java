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

package kieker.examples.livedemo.view.methodResponseTime;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.event.ValueChangeEvent;

import org.primefaces.model.chart.CartesianChartModel;
import org.primefaces.model.chart.ChartSeries;

import kieker.examples.livedemo.view.AnalysisBean;

/**
 * @author Bjoern Weissenfels
 * 
 * @since 1.9
 */
@ManagedBean(name = "methodResponsetimeBean", eager = true)
@ViewScoped
public class MethodResponsetimeBean {

	@ManagedProperty(value = "#{analysisBean}")
	private AnalysisBean analysisBean;

	private final List<String> availableMethods;
	private List<String> selectedMethods;
	private int maxY;
	private boolean selectButton = true;

	private CartesianChartModel responsetimeModel;
	private final CartesianChartModel shownResponsetimeModel;
	private CartesianChartModel countingModel;
	private final CartesianChartModel shownCountingModel;

	public MethodResponsetimeBean() {
		this.availableMethods = new ArrayList<String>();
		this.selectedMethods = new ArrayList<String>();
		this.maxY = 4;
		this.shownResponsetimeModel = new CartesianChartModel();
		this.shownCountingModel = new CartesianChartModel();
	}

	@PostConstruct
	public void init() {
		this.countingModel = this.analysisBean.getMethodResponsetimeDisplayFilter().getCountModel();
		this.responsetimeModel = this.analysisBean.getMethodResponsetimeDisplayFilter().getChartModel();
		for (final ChartSeries series : this.responsetimeModel.getSeries()) {
			final String shortSignature = series.getLabel();
			if (!this.availableMethods.contains(shortSignature)) {
				this.availableMethods.add(shortSignature);
			}
		}
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
		this.shownResponsetimeModel.clear();
		for (final ChartSeries series : this.responsetimeModel.getSeries()) {
			final String shortSignature = series.getLabel();
			if (this.selectedMethods.contains(shortSignature)) {
				this.shownResponsetimeModel.addSeries(series);
			}
			if (!this.availableMethods.contains(shortSignature)) {
				this.availableMethods.add(shortSignature);
			}
		}
		return this.shownResponsetimeModel;
	}

	public synchronized CartesianChartModel getCountingModel() {
		this.shownCountingModel.clear();
		for (final ChartSeries series : this.countingModel.getSeries()) {
			final String shortSignature = series.getLabel();
			if (this.selectedMethods.contains(shortSignature)) {
				this.shownCountingModel.addSeries(series);
			}
			if (!this.availableMethods.contains(shortSignature)) {
				this.availableMethods.add(shortSignature);
			}
		}
		return this.shownCountingModel;
	}

	public void setMaxY(final int maxY) {
		this.maxY = maxY;
	}

	public int getMaxY() {
		this.maxY = 4;
		for (final ChartSeries series : this.shownCountingModel.getSeries()) {
			this.maxY = Math.max(this.maxY, this.calculateMaxY(series.getData().values()));
		}
		return this.maxY;
	}

	public void setSelectButton(final boolean selectButton) {
		this.selectButton = selectButton;
	}

	public boolean isSelectButton() {
		return this.selectButton;
	}

	private int calculateMaxY(final Collection<Number> numbers) {
		int max = 1;
		for (final Number n : numbers) {
			max = Math.max(max, n.intValue());
		}
		max = (max + 4) - (max % 4);
		return max;
	}

}
