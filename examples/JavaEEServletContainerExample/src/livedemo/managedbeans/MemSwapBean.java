/***************************************************************************
 * Copyright 2013 Kicker Project (http://kicker-monitoring.net)
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

import java.util.Observable;
import java.util.Observer;

import javax.annotation.PostConstruct;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;

import org.primefaces.model.chart.CartesianChartModel;
import org.primefaces.model.chart.ChartSeries;

import kicker.analysis.display.XYPlot;
import livedemo.entities.Model;

/**
 * @author Bjoern Weissenfels
 * 
 * @since 1.9
 */
@ManagedBean(name = "memSwapBean")
@ApplicationScoped
public class MemSwapBean implements Observer {

	@ManagedProperty(value = "#{analysisBean}")
	private AnalysisBean analysisBean;

	private XYPlot xyplot;
	private final Model<CartesianChartModel> memModel;
	private final Model<CartesianChartModel> swapModel;

	public MemSwapBean() {
		this.memModel = new Model<CartesianChartModel>(new CartesianChartModel(), "KIEKER-DEMO-SVR - MEM");
		this.swapModel = new Model<CartesianChartModel>(new CartesianChartModel(), "KIEKER-DEMO-SVR - SWAP");
	}

	@PostConstruct
	public void init() {
		this.xyplot = this.analysisBean.getMemSwapUtilizationDisplayFilter().getXYPlot();
		this.updateXYPlot();
		final String key = this.xyplot.getKeys().iterator().next();
		final int index = key.lastIndexOf('-');
		final String hostname = key.substring(0, index - 1);
		this.memModel.setName(hostname + " - MEM");
		this.swapModel.setName(hostname + " - SWAP");
		this.analysisBean.getUpdateThread().addObserver(this);
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

	private void updateXYPlot() {
		final CartesianChartModel mem = new CartesianChartModel();
		final CartesianChartModel swap = new CartesianChartModel();
		for (final String key : this.xyplot.getKeys()) { // key = hostname - memFree|memTotal|memUsed|swapFree|...
			if (key.endsWith("memFree")) {
				final ChartSeries series = new ChartSeries();
				series.setLabel("memFree");
				series.setData(this.xyplot.getEntries(key));
				mem.addSeries(series);
			} else if (key.endsWith("memUsed")) {
				final ChartSeries series = new ChartSeries();
				series.setLabel("memUsed");
				series.setData(this.xyplot.getEntries(key));
				mem.addSeries(series);
			} else if (key.endsWith("swapFree")) {
				final ChartSeries series = new ChartSeries();
				series.setLabel("swapFree");
				series.setData(this.xyplot.getEntries(key));
				swap.addSeries(series);
			} else if (key.endsWith("swapUsed")) {
				final ChartSeries series = new ChartSeries();
				series.setLabel("swapUsed");
				series.setData(this.xyplot.getEntries(key));
				swap.addSeries(series);
			}
		}
		this.memModel.setModel(mem);
		this.swapModel.setModel(swap);
	}

	@Override
	public void update(final Observable arg0, final Object arg1) {
		this.updateXYPlot();
	}
}
