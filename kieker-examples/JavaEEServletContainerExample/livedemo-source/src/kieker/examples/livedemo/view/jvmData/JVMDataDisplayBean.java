/***************************************************************************
 * Copyright 2014 Kieker Project (http://kieker-monitoring.net)
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

package kieker.examples.livedemo.view.jvmData;

import java.util.Collection;

import javax.annotation.PostConstruct;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;

import org.primefaces.model.chart.CartesianChartModel;
import org.primefaces.model.chart.ChartSeries;

import kieker.examples.livedemo.view.AnalysisBean;

/**
 * @author Nils Christian Ehmke
 * 
 * @since 1.10
 */
@ManagedBean(name = "jvmDataDisplayBean")
@ApplicationScoped
public class JVMDataDisplayBean {

	@ManagedProperty(value = "#{analysisBean}")
	private AnalysisBean analysisBean;

	private CartesianChartModel gcCountModel;
	private CartesianChartModel gcTimeModel;
	private CartesianChartModel compilationModel;
	private CartesianChartModel classLoadingModel;
	private CartesianChartModel threadsStatusModel;
	private CartesianChartModel jvmHeapModel;
	private CartesianChartModel jvmNonHeapModel;

	public JVMDataDisplayBean() {
		// Initialization in PostContruct method
	}

	@PostConstruct
	protected void initialize() {
		this.gcCountModel = this.analysisBean.getGcCountDisplayFilter().getChartModel();
		this.gcTimeModel = this.analysisBean.getGcTimeDisplayFilter().getChartModel();
		this.compilationModel = this.analysisBean.getJitCompilationDisplayFilter().getChartModel();
		this.classLoadingModel = this.analysisBean.getClassLoadingDisplayFilter().getChartModel();
		this.threadsStatusModel = this.analysisBean.getThreadsStatusDisplayFilter().getChartModel();
		this.jvmHeapModel = this.analysisBean.getJvmHeapMemoryDisplayFilter().getChartModel();
		this.jvmNonHeapModel = this.analysisBean.getJvmNonHeapMemoryDisplayFilter().getChartModel();
	}

	public void setAnalysisBean(final AnalysisBean analysisBean) {
		this.analysisBean = analysisBean;
	}

	public CartesianChartModel getGcCountModel() {
		return this.gcCountModel;
	}

	public CartesianChartModel getGcTimeModel() {
		return this.gcTimeModel;
	}

	public CartesianChartModel getCompilationModel() {
		return this.compilationModel;
	}

	public CartesianChartModel getClassLoadingModel() {
		return this.classLoadingModel;
	}

	public CartesianChartModel getThreadsStatusModel() {
		return this.threadsStatusModel;
	}

	public CartesianChartModel getJvmHeapModel() {
		return this.jvmHeapModel;
	}

	public CartesianChartModel getJvmNonHeapModel() {
		return this.jvmNonHeapModel;
	}

	public int getSeriesMaximum(final Collection<ChartSeries> seriesCollection, final int roundToValue) {
		int maximum = 0;

		for (final ChartSeries series : seriesCollection) {
			for (final Number value : series.getData().values()) {
				maximum = Math.max(value.intValue(), maximum);
			}
		}

		maximum += roundToValue / 2;

		return ((maximum + (roundToValue - 1)) / roundToValue) * roundToValue;
	}

	public int getThreadsStatusMaximum() {
		return this.getSeriesMaximum(this.threadsStatusModel.getSeries(), 50);
	}

	public int getJvmNonHeapMaximum() {
		return this.getSeriesMaximum(this.jvmNonHeapModel.getSeries(), 50);
	}

	public int getJvmHeapMaximum() {
		return this.getSeriesMaximum(this.jvmHeapModel.getSeries(), 50);
	}

	public int getGcTimeMaximum() {
		return this.getSeriesMaximum(this.gcTimeModel.getSeries(), 50);
	}

	public int getGcCountMaximum() {
		return this.getSeriesMaximum(this.gcCountModel.getSeries(), 50);
	}

	public int getCompilationMaximum() {
		return this.getSeriesMaximum(this.compilationModel.getSeries(), 1000);
	}

	public int getClassLoadingMaximum() {
		return this.getSeriesMaximum(this.classLoadingModel.getSeries(), 1000);
	}

}
