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

package kieker.examples.livedemo.view;

import javax.annotation.PostConstruct;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;

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

	private final boolean disabled;
	private long timestamp;
	private final String buttonText;
	private String[] arguments;

	public JMeterBean() {
		this.disabled = false;
		this.timestamp = System.currentTimeMillis();
		this.buttonText = this.defaultButtonText;
	}

	@PostConstruct
	public void init() {
		final String userDir = System.getProperty("user.dir");
		final String fileSeparator = System.getProperty("file.separator");
		final String bin = "webapps" + fileSeparator + "root" + fileSeparator + "WEB-INF" + fileSeparator + "bin";
		final String newUserDir = userDir + fileSeparator + bin;
		System.setProperty("user.dir", newUserDir);
		final String testplan = bin + fileSeparator + "Testplan.jmx";
		this.arguments = new String[] { "-n", "-t", testplan };
	}

	public void setAnalysisBean(final AnalysisBean analysisBean) {
		this.analysisBean = analysisBean;
	}

	public String getButtonText() {
		return this.buttonText;
	}

	public boolean isDisabled() {
		return this.disabled;
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

	// @Override
	// public void update(final Observable arg0, final Object arg1) {
	// final long actualtime = System.currentTimeMillis();
	// if (actualtime > this.timestamp) {
	// this.buttonText = this.defaultButtonText;
	// this.disabled = false;
	// } else {
	// this.buttonText = "Generate Load for " + String.valueOf((int) ((this.timestamp - actualtime) / 1000)) + " s";
	// this.disabled = true;
	// }
	// }

}
