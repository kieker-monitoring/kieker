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

package kieker.examples.livedemo.view;

import javax.annotation.PostConstruct;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

import org.apache.jmeter.JMeter;

/**
 * @author Bjoern Weissenfels
 * 
 * @since 1.9
 */
@ManagedBean(name = "jMeterBean")
@ApplicationScoped
public class JMeterBean {

	private final long timeOutMillis = 120000;
	private final String defaultButtonText = "Generate Load";
	private final JMeter jMeter = new JMeter();

	@ManagedProperty(value = "#{analysisBean}")
	private AnalysisBean analysisBean;

	private long timestamp;
	private String[] arguments;

	public JMeterBean() {
		this.timestamp = System.currentTimeMillis();
	}

	@PostConstruct
	public void init() {
		final ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();

		final String bin = context.getRealPath("WEB-INF/bin/");
		System.setProperty("user.dir", bin);
		final String testplan = context.getRealPath("WEB-INF/bin/Testplan.jmx");

		this.arguments = new String[] { "-n", "-t", testplan };
	}

	public void setAnalysisBean(final AnalysisBean analysisBean) {
		this.analysisBean = analysisBean;
	}

	public boolean isDisabled() {
		final long actualtime = System.currentTimeMillis();
		return (actualtime < this.timestamp);
	}

	public void runJMeter() {
		synchronized (this) {
			final long actualtime = System.currentTimeMillis();
			if (actualtime < this.timestamp) {
				return;
			} else {
				this.timestamp = actualtime + this.timeOutMillis;
			}
		}
		this.jMeter.start(this.arguments);
	}

	public String getButtonText() {
		final long actualtime = System.currentTimeMillis();
		if (actualtime >= this.timestamp) {
			return this.defaultButtonText;
		} else {
			return "Generate Load for " + String.valueOf((int) ((this.timestamp - actualtime) / 1000)) + " s";
		}
	}

}
