/***************************************************************************
 * Copyright 2015 Kieker Project (http://kieker-monitoring.net)
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

package kieker.examples.livedemo.view.cpuAndMemSwap;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

import org.primefaces.model.chart.CartesianChartModel;
import org.primefaces.model.chart.ChartSeries;

import kieker.examples.livedemo.view.AnalysisBean;
import kieker.examples.livedemo.view.util.Model;

/**
 * @author Bjoern Weissenfels
 * 
 * @since 1.9
 */
@ManagedBean(name = "cpuXYPlotBean")
@ViewScoped
public class CPUXYPlotBean {

	@ManagedProperty(value = "#{analysisBean}")
	private AnalysisBean analysisBean;

	private List<Model<CartesianChartModel>> models;
	private final List<Model<CartesianChartModel>> shownModels;

	private final List<String> availableAttributes = Arrays.asList("idle", "irq", "nice", "system", "totalUtilization", "user");
	private List<String> selectedAttributes; // = Arrays.asList("idle","totalUtilization");

	public CPUXYPlotBean() {
		this.shownModels = Collections.synchronizedList(new ArrayList<Model<CartesianChartModel>>());
		this.selectedAttributes = new ArrayList<String>();
		this.selectedAttributes.add("idle");
		this.selectedAttributes.add("totalUtilization");
	}

	@PostConstruct
	public void init() {
		this.models = this.analysisBean.getCPUUtilizationDisplayFilter().getModels();
		this.updateModel();
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
		this.updateModel();
		return this.shownModels;
	}

	private void updateModel() {
		this.shownModels.clear();
		for (final Model<CartesianChartModel> model : this.models) {
			final CartesianChartModel chartModel = new CartesianChartModel();
			final Model<CartesianChartModel> shownModel = new Model<CartesianChartModel>(chartModel, model.getName());
			for (final ChartSeries series : model.getModel().getSeries()) {
				if (this.selectedAttributes.contains(series.getLabel())) {
					chartModel.addSeries(series);
				}
			}
			this.shownModels.add(shownModel);
		}
	}

}
