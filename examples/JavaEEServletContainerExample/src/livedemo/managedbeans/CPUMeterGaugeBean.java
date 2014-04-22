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
import java.util.Collections;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;

import org.primefaces.model.chart.MeterGaugeChartModel;

import kieker.analysis.display.MeterGauge;

import livedemo.entities.Model;

/**
 * @author Bjoern Weissenfels
 * 
 * @since 1.9
 */
@ManagedBean(name = "cpuMeterGaugeBean")
@ApplicationScoped
public class CPUMeterGaugeBean implements Observer {

	@ManagedProperty(value = "#{analysisBean}")
	private AnalysisBean analysisBean;

	private MeterGauge meterGauge;
	private final List<Model<MeterGaugeChartModel>> meterGaugeModels;

	private List<String> cpuIds;
	private final String colors = "66cc66, E7E658, cc6666";

	public CPUMeterGaugeBean() {
		this.meterGaugeModels = Collections.synchronizedList(new ArrayList<Model<MeterGaugeChartModel>>());
	}

	@PostConstruct
	public void init() {
		this.meterGauge = this.analysisBean.getCPUUtilizationDisplayFilter().getMeterGauge();
		this.cpuIds = new ArrayList<String>(this.meterGauge.getKeys());
		Collections.sort(this.cpuIds);
		this.updateMeterGaugeModels();
		this.analysisBean.getUpdateThread().addObserver(this);
	}

	@PreDestroy
	public void terminate() {
		this.analysisBean.getUpdateThread().deleteObserver(this);
	}

	public void setAnalysisBean(final AnalysisBean analysisBean) {
		this.analysisBean = analysisBean;
	}

	public List<Model<MeterGaugeChartModel>> getMeterGaugeModels() {
		return this.meterGaugeModels;
	}

	public String getColors() {
		return this.colors;
	}

	private void updateMeterGaugeModels() {
		for (final String id : this.cpuIds) {
			final MeterGaugeChartModel meterGaugeChartModel = new MeterGaugeChartModel(this.meterGauge.getValue(id), this.meterGauge.getIntervals(id));
			final Model<MeterGaugeChartModel> model = new Model<MeterGaugeChartModel>(meterGaugeChartModel, id);
			this.meterGaugeModels.add(model);
		}
	}

	@Override
	public void update(final Observable arg0, final Object arg1) {
		this.meterGaugeModels.clear();
		this.updateMeterGaugeModels();
	}

}
