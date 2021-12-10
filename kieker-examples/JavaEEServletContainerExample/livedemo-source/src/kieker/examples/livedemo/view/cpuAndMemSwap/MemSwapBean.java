/***************************************************************************
 * Copyright 2021 Kieker Project (http://kieker-monitoring.net)
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

import javax.annotation.PostConstruct;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;

import org.primefaces.model.chart.CartesianChartModel;

import kieker.examples.livedemo.view.AnalysisBean;
import kieker.examples.livedemo.view.util.Model;

/**
 * @author Bjoern Weissenfels, Nils Christian Ehmke
 * 
 * @since 1.9
 */
@ManagedBean(name = "memSwapBean")
@ApplicationScoped
public class MemSwapBean {

	@ManagedProperty(value = "#{analysisBean}")
	private AnalysisBean analysisBean;

	private Model<CartesianChartModel> memModel;
	private Model<CartesianChartModel> swapModel;

	public MemSwapBean() {
		// No code necessary
	}

	@PostConstruct
	public void init() {
		this.memModel = new Model<CartesianChartModel>(this.analysisBean.getMemoryDisplayFilter().getChartModel(), "KIEKER-DEMO-SVR - MEM");
		this.swapModel = new Model<CartesianChartModel>(this.analysisBean.getSwapDisplayFilter().getChartModel(), "KIEKER-DEMO-SVR - SWAP");
	}

	public void setAnalysisBean(final AnalysisBean analysisBean) {
		this.analysisBean = analysisBean;
	}

	public Model<CartesianChartModel> getMemModel() {
		return this.memModel;
	}

	public Model<CartesianChartModel> getSwapModel() {
		return this.swapModel;
	}

}
