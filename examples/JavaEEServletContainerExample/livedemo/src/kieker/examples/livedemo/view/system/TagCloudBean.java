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

package kieker.examples.livedemo.view.system;

import javax.annotation.PostConstruct;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;

import org.primefaces.model.tagcloud.TagCloudModel;

import kieker.examples.livedemo.view.AnalysisBean;

/**
 * @author Bjoern Weissenfels, Nils Christian Ehmke
 * 
 * @since 1.9
 */
@ManagedBean(name = "tagCloudBean")
@ApplicationScoped
public class TagCloudBean {

	@ManagedProperty(value = "#{analysisBean}")
	private AnalysisBean analysisBean;

	private TagCloudModel methodModel;
	private TagCloudModel componentModel;

	public TagCloudBean() {
		// Initialization in PostContruct method
	}

	@PostConstruct
	protected void initialize() {
		this.methodModel = this.analysisBean.getMethodFlowDisplayFilter().getChartModel();
		this.componentModel = this.analysisBean.getComponentFlowDisplayFilter().getChartModel();
	}

	public TagCloudModel getMethodModel() {
		return this.methodModel;
	}

	public TagCloudModel getComponentModel() {
		return this.componentModel;
	}

	public void setAnalysisBean(final AnalysisBean analysisBean) {
		this.analysisBean = analysisBean;
	}

}
