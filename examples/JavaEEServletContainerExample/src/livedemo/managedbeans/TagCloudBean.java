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

import java.util.Map;
import java.util.Observable;
import java.util.Observer;
import java.util.concurrent.atomic.AtomicLong;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;

import org.primefaces.model.DashboardColumn;
import org.primefaces.model.DashboardModel;
import org.primefaces.model.DefaultDashboardColumn;
import org.primefaces.model.DefaultDashboardModel;
import org.primefaces.model.tagcloud.DefaultTagCloudItem;
import org.primefaces.model.tagcloud.DefaultTagCloudModel;
import org.primefaces.model.tagcloud.TagCloudModel;

/**
 * @author Bjoern Weissenfels
 * 
 * @since 1.9
 */
@ManagedBean(name = "tagCloudBean")
@ApplicationScoped
public class TagCloudBean implements Observer {

	@ManagedProperty(value = "#{analysisBean}")
	private AnalysisBean analysisBean;

	private Map<String, AtomicLong> methodMap;
	private final TagCloudModel methodModel;
	private Map<String, AtomicLong> componentMap;
	private final TagCloudModel componentModel;

	private final DashboardModel dashboardModel;
	private final DashboardColumn column1;
	private final DashboardColumn column2;

	public TagCloudBean() {
		this.methodModel = new DefaultTagCloudModel();
		this.componentModel = new DefaultTagCloudModel();
		this.dashboardModel = new DefaultDashboardModel();
		this.column1 = new DefaultDashboardColumn();
		this.column2 = new DefaultDashboardColumn();
		this.column1.addWidget("c1");
		this.column2.addWidget("c2");
		this.dashboardModel.addColumn(this.column1);
		this.dashboardModel.addColumn(this.column2);
	}

	@PostConstruct
	public void init() {
		this.methodMap = this.analysisBean.getTagCloudFilter().methodTagCloudDisplay().getCounters();
		this.componentMap = this.analysisBean.getTagCloudFilter().componentTagCloudDisplay().getCounters();
		this.analysisBean.getUpdateThread().addObserver(this);
	}

	@PreDestroy
	public void terminate() {
		this.analysisBean.getUpdateThread().deleteObserver(this);
	}

	public DashboardModel getDashboardModel() {
		return this.dashboardModel;
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

	@Override
	public void update(final Observable arg0, final Object arg1) {
		this.methodModel.clear();
		for (final String key : this.methodMap.keySet()) {
			final int value = this.methodMap.get(key).intValue();
			this.methodModel.addTag(new DefaultTagCloudItem(key, value));
		}
		this.componentModel.clear();
		for (final String key : this.componentMap.keySet()) {
			final int value = this.componentMap.get(key).intValue();
			this.componentModel.addTag(new DefaultTagCloudItem(key, value));
		}
	}

}
