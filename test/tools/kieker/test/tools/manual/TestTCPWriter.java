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

package kieker.test.tools.manual;

import kieker.common.configuration.Configuration;
import kieker.common.record.misc.EmptyRecord;
import kieker.monitoring.core.configuration.ConfigurationFactory;
import kieker.monitoring.core.controller.IMonitoringController;
import kieker.monitoring.core.controller.MonitoringController;
import kieker.monitoring.writer.AbstractAsyncWriter;
import kieker.monitoring.writer.tcp.TCPWriter;

/**
 * @author Jan Waller
 * 
 * @since 1.8
 */
public final class TestTCPWriter {

	private TestTCPWriter() {
		// Private constructor to calm PMD
	}

	public static void main(final String[] args) {
		final Configuration monitoringConfig = ConfigurationFactory.createDefaultConfiguration();
		monitoringConfig.setProperty(ConfigurationFactory.WRITER_CLASSNAME, TCPWriter.class.getName());
		monitoringConfig.setProperty(TCPWriter.class.getName() + "." + AbstractAsyncWriter.CONFIG_BEHAVIOR, "1");
		final IMonitoringController ctrl = MonitoringController.createInstance(monitoringConfig);

		while (true) {
			ctrl.newMonitoringRecord(new EmptyRecord());
		}
	}

}
