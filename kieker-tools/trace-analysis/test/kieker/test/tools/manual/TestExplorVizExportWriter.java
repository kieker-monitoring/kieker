/***************************************************************************
 * Copyright 2022 Kieker Project (http://kieker-monitoring.net)
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

import kieker.analysis.plugin.filter.flow.TraceEventRecords;
import kieker.common.configuration.Configuration;
import kieker.common.record.flow.trace.AbstractTraceEvent;
import kieker.monitoring.core.configuration.ConfigurationConstants;
import kieker.monitoring.core.configuration.ConfigurationFactory;
import kieker.monitoring.core.controller.IMonitoringController;
import kieker.monitoring.core.controller.MonitoringController;
import kieker.monitoring.writer.explorviz.ExplorVizTcpWriter;

import kieker.test.analysis.util.plugin.filter.flow.BookstoreEventRecordFactory;

/**
 * @author Florian Fittkau, Jan Waller
 *
 * @since 1.9
 * @deprecated
 */
@Deprecated
public final class TestExplorVizExportWriter {

	private TestExplorVizExportWriter() {
		// Private constructor to calm PMD
	}

	public static void main(final String[] args) {
		final Configuration monitoringConfig = ConfigurationFactory.createDefaultConfiguration();
		monitoringConfig.setProperty(ConfigurationConstants.WRITER_CLASSNAME, ExplorVizTcpWriter.class.getName());
		monitoringConfig.setProperty(ConfigurationConstants.CONTROLLER_NAME, "Bookstore");
		final IMonitoringController ctrl = MonitoringController.createInstance(monitoringConfig);

		int traceId = 0;

		while (traceId < 1000 * 1000 * 10) {
			final TraceEventRecords events = BookstoreEventRecordFactory.validSyncTraceBeforeAfterEvents(traceId * 1000L, traceId++, "", "localhost");
			ctrl.newMonitoringRecord(events.getTraceMetadata());
			for (final AbstractTraceEvent event : events.getTraceEvents()) {
				ctrl.newMonitoringRecord(event);
			}
		}
	}
}
