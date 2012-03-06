/***************************************************************************
 * Copyright 2011 by
 *  + Christian-Albrechts-University of Kiel
 *    + Department of Computer Science
 *      + Software Engineering Group 
 *  and others.
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

package kieker.examples.userguide.ch5bookstore;

import java.util.Random;

import kieker.analysis.AnalysisController;
import kieker.analysis.exception.MonitoringReaderException;
import kieker.analysis.exception.MonitoringRecordConsumerException;
import kieker.analysis.plugin.AbstractAnalysisPlugin;
import kieker.analysis.plugin.annotation.InputPort;
import kieker.analysis.reader.filesystem.FSReader;
import kieker.common.configuration.Configuration;
import kieker.common.record.IMonitoringRecord;
import kieker.common.record.controlflow.OperationExecutionRecord;
import kieker.monitoring.core.controller.IMonitoringController;
import kieker.monitoring.core.controller.MonitoringController;

public class BookstoreHostnameRewriter {

	public static void main(final String[] args)
			throws MonitoringReaderException, MonitoringRecordConsumerException {

		if (args.length == 0) {
			return;
		}

		/* Create Kieker.Analysis instance */
		final AnalysisController analysisInstance = new AnalysisController();

		final HostNameRewriterPlugin plugin = new HostNameRewriterPlugin();
		analysisInstance.registerFilter(plugin);

		/* Set filesystem monitoring log input directory for our analysis */
		final String inputDirs[] = { args[0] };
		final Configuration configuration = new Configuration(null);
		configuration.setProperty(FSReader.CONFIG_INPUTDIRS, Configuration.toProperty(inputDirs));
		final FSReader reader = new FSReader(configuration);
		analysisInstance.registerReader(reader);

		/* Connect the reader with the plugin. */
		analysisInstance.connect(reader, FSReader.OUTPUT_PORT_NAME, plugin, HostNameRewriterPlugin.INPUT_PORT_NAME);

		/* Start the analysis */
		analysisInstance.run();
	}
}

class HostNameRewriterPlugin extends AbstractAnalysisPlugin {

	public static final String INPUT_PORT_NAME = "newEvent";

	private static final IMonitoringController MONITORING_CTRL =
			MonitoringController.getInstance();
	private static final String BOOKSTORE_HOSTNAME = "SRV0";
	private static final Random rnd = new Random();
	private static final int RND_PERCENTILE_HOST_IDX_1 = 34;
	private static final String[] CATALOG_HOSTNAMES = { "SRV0", "SRV1" };
	private static final String CRM_HOSTNAME = "SRV0";

	public HostNameRewriterPlugin() {
		super(new Configuration());
	}

	@InputPort(
			name = HostNameRewriterPlugin.INPUT_PORT_NAME,
			eventTypes = { IMonitoringRecord.class })
	public void newEvent(final Object event) {
		if (!(event instanceof OperationExecutionRecord)) {
			return;
		}

		final OperationExecutionRecord execution = (OperationExecutionRecord) event;

		if (execution.getClassName().equals(Bookstore.class.getName())) {
			execution.setHostName(HostNameRewriterPlugin.BOOKSTORE_HOSTNAME);
		} else if (execution.getClassName().equals(CRM.class.getName())) {
			execution.setHostName(HostNameRewriterPlugin.CRM_HOSTNAME);
		} else if (execution.getClassName().equals(Catalog.class.getName())) {
			if (HostNameRewriterPlugin.rnd.nextInt(99) < HostNameRewriterPlugin.RND_PERCENTILE_HOST_IDX_1) {
				execution.setHostName(HostNameRewriterPlugin.CATALOG_HOSTNAMES[0]);
			} else {
				execution.setHostName(HostNameRewriterPlugin.CATALOG_HOSTNAMES[1]);
			}
		}
		HostNameRewriterPlugin.MONITORING_CTRL.newMonitoringRecord(execution);
	}

	@Override
	protected Configuration getDefaultConfiguration() {
		return new Configuration();
	}

	@Override
	public Configuration getCurrentConfiguration() {
		return new Configuration();
	}
}
