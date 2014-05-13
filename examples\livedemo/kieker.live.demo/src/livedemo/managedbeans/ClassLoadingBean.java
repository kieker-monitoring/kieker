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

import java.util.Collection;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

import kieker.analysis.display.XYPlot;

import org.primefaces.model.chart.CartesianChartModel;
import org.primefaces.model.chart.ChartSeries;

/**
 * @author Nils Christian Ehmke
 * 
 * @since 1.10
 */
@ManagedBean(name = "classLoadingBean", eager = true)
@ViewScoped
public class ClassLoadingBean implements Observer {

	@ManagedProperty(value = "#{analysisBean}")
	private AnalysisBean analysisBean;

	private int maxY;
	private boolean selectButton = true;

	private XYPlot plot;
	private final CartesianChartModel model;

	public ClassLoadingBean() {
		this.maxY = 1;
		this.model = new CartesianChartModel();
	}

	@PostConstruct
	public void init() {
		this.plot = this.analysisBean.getClassLoadingDisplayFilter().getPlot();
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

	public synchronized CartesianChartModel getModel() {
		return this.model;
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



	private int calculateMaxY(final Collection<Number> numbers) {
		int max = 1;
		for (final Number n : numbers) {
			max = Math.max(max, n.intValue());
		}
		max = (max + 4) - (max % 4);
		return max;
	}
	
	private synchronized void updateModels() {
		this.maxY = 4;
		
		for (final String key : this.plot.getKeys()) {
			final ChartSeries series = new ChartSeries();
			series.setLabel(key);
			final Map<Object, Number> map = this.plot.getEntries(key);
			series.setData(map);
			this.model.addSeries(series);
			
			maxY = this.calculateMaxY(map.values());
		}
	}

	public synchronized void update(final Observable o, final Object arg) {
		this.model.clear();
		this.updateModels();
	}

}
