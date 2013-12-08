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
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

import org.primefaces.model.chart.CartesianChartModel;
import org.primefaces.model.chart.ChartSeries;

import kieker.analysis.display.XYPlot;

import livedemo.entities.Model;

/**
 * @author Bjoern Weissenfels
 * 
 * @since 1.9
 */
@ManagedBean(name = "cpuXYPlotBean")
@ViewScoped
public class CPUXYPlotBean implements Observer {

	@ManagedProperty(value = "#{analysisBean}")
	private AnalysisBean analysisBean;

	private XYPlot xyPlot;
	private List<String> keys;
	private final List<String> cpuIds;
	private final List<Model<CartesianChartModel>> models;
	private int index;

	private final List<String> availableAttributes = Arrays.asList("idle", "irq", "nice", "system", "totalUtilization", "user");
	private List<String> selectedAttributes;// = Arrays.asList("idle","totalUtilization");

	public CPUXYPlotBean() {
		this.models = Collections.synchronizedList(new ArrayList<Model<CartesianChartModel>>());
		this.cpuIds = new ArrayList<String>();
		this.selectedAttributes = new ArrayList<String>();
		this.selectedAttributes.add("idle");
		this.selectedAttributes.add("totalUtilization");
	}

	@PostConstruct
	public void init() {
		this.xyPlot = this.analysisBean.getCPUUtilizationDisplayFilter().getXYPlot();
		this.keys = new ArrayList<String>(this.xyPlot.getKeys()); // key = hostname - cpuId - idle
		Collections.sort(this.keys);
		this.index = this.keys.get(0).lastIndexOf('-');
		for (final String key : this.keys) {
			final String id = key.substring(0, this.index - 1); // id = hostname - cpuId
			if (!this.cpuIds.contains(id)) {
				this.cpuIds.add(id);
			}
		}
		this.updateModel();
		this.analysisBean.getUpdateThread().addObserver(this);
	}

	@PreDestroy
	public void terminate() {
		this.analysisBean.getUpdateThread().deleteObserver(this);
	}

	public void setAnalysisBean(final AnalysisBean analysisBean) {
		this.analysisBean = analysisBean;
	}

	public List<String> getAvailableAttributes() {
		return this.availableAttributes;
	}

	public List<String> getSelectedAttributes() {
		if (this.selectedAttributes.isEmpty()) {
			this.selectedAttributes.add("totalUtilization");
			this.selectedAttributes.add("idle");
		}
		return this.selectedAttributes;
	}

	public void setSelectedAttributes(final List<String> selectedAttributes) {
		this.selectedAttributes = selectedAttributes;
	}

	public List<Model<CartesianChartModel>> getModels() {
		return this.models;
	}

	private ChartSeries computeModel(final String key, final String attribute) {
		final Map<Object, Number> data = this.xyPlot.getEntries(key);
		final ChartSeries cpuSeries = new ChartSeries();
		cpuSeries.setLabel(attribute);
		cpuSeries.setData(data);
		return cpuSeries;
	}

	private void updateModel() {
		this.models.clear();
		for (final String id : this.cpuIds) { // id = hostname - cpuId
			final CartesianChartModel cpuModel = new CartesianChartModel();
			for (final String key : this.keys) { // key = hostname - cpuId - idle
				if (key.substring(0, this.index - 1).equals(id)) {
					for (final String attribute : this.getSelectedAttributes()) {
						if (key.substring(this.index + 2).equals(attribute)) {
							cpuModel.addSeries(this.computeModel(key, attribute));
						}
					}

				}
			}
			this.models.add(new Model<CartesianChartModel>(cpuModel, id));
		}
	}

	public void update(final Observable o, final Object arg) {
		this.updateModel();

	}

}
