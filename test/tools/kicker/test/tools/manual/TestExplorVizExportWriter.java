/***************************************************************************
 * Copyright 2014 Kicker Project (http://kicker-monitoring.net)
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

package kicker.test.tools.manual;

import kicker.analysis.plugin.filter.flow.TraceEventRecords;
import kicker.common.configuration.Configuration;
import kicker.common.record.flow.trace.AbstractTraceEvent;
import kicker.monitoring.core.configuration.ConfigurationFactory;
import kicker.monitoring.core.controller.IMonitoringController;
import kicker.monitoring.core.controller.MonitoringController;
import kicker.monitoring.writer.explorviz.ExplorVizExportWriter;
import kicker.test.analysis.util.plugin.filter.flow.BookstoreEventRecordFactory;

/**
 * @author Florian Fittkau, Jan Waller
 * 
 * @since 1.9
 */
public final class TestExplorVizExportWriter {

	private TestExplorVizExportWriter() {
		// Private constructor to calm PMD
	}

	public static void main(final String[] args) {
		final Configuration monitoringConfig = ConfigurationFactory.createDefaultConfiguration();
		monitoringConfig.setProperty(ConfigurationFactory.WRITER_CLASSNAME, ExplorVizExportWriter.class.getName());
		monitoringConfig.setProperty(ConfigurationFactory.CONTROLLER_NAME, "Bookstore");
		final IMonitoringController ctrl = MonitoringController.createInstance(monitoringConfig);

		int traceId = 0;

		while (traceId < (1000 * 1000 * 10)) {
			final TraceEventRecords events = BookstoreEventRecordFactory.validSyncTraceBeforeAfterEvents(traceId * 1000L, traceId++, "", "localhost");
			ctrl.newMonitoringRecord(events.getTraceMetadata());
			for (final AbstractTraceEvent event : events.getTraceEvents()) {
				ctrl.newMonitoringRecord(event);
			}
		}
	}
}
