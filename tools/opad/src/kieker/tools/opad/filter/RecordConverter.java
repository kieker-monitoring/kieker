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

package kieker.tools.opad.filter;

import kieker.analysis.IProjectContext;
import kieker.analysis.plugin.annotation.InputPort;
import kieker.analysis.plugin.annotation.OutputPort;
import kieker.analysis.plugin.annotation.Plugin;
import kieker.analysis.plugin.filter.AbstractFilterPlugin;
import kieker.common.configuration.Configuration;
import kieker.common.record.controlflow.OperationExecutionRecord;
import kieker.tools.opad.record.NamedDoubleRecord;

/**
 * Converts OperationExecutionRecords to NamedDoubleRecords.
 *
 * @author Thomas Duellmann
 * @since 1.10
 *
 */
@Plugin(
		description = "Converts OperationExecutionRecords to NamedDoubleRecords",
		outputPorts = @OutputPort(name = RecordConverter.OUTPUT_PORT_NAME_NDR, eventTypes = { NamedDoubleRecord.class }))
public class RecordConverter extends AbstractFilterPlugin {

	/**
	 * Input port that accepts OperationExecutionRecords.
	 */
	public static final String INPUT_PORT_NAME_OER = "oer";

	/**
	 * Ouput port that delivers NamedDoubleRecords.
	 */
	public static final String OUTPUT_PORT_NAME_NDR = "ndr";

	/**
	 * Constructor.
	 *
	 * @param configuration
	 *            configuration for the converter
	 * @param projectContext
	 *            controller
	 */
	public RecordConverter(final Configuration configuration,
			final IProjectContext projectContext) {
		super(configuration, projectContext);
	}

	@Override
	public Configuration getCurrentConfiguration() {
		return new Configuration();
	}

	/**
	 * Receives OperationExecutionRecords and delivers them converted to NamedDoubleRecords.
	 *
	 * @param oer
	 *            OperationExecutionRecord object to be converted
	 */
	@InputPort(name = RecordConverter.INPUT_PORT_NAME_OER, eventTypes = { OperationExecutionRecord.class })
	public void convert(final OperationExecutionRecord oer) {
		// string, long, double
		final long timestamp = oer.getLoggingTimestamp();
		final String application = oer.getHostname() + ":" + oer.getOperationSignature();
		final double response = oer.getTout() - oer.getTin();

		if (response >= 0.0d) {
			final NamedDoubleRecord ndr = new NamedDoubleRecord(application, timestamp, response);
			super.deliver(OUTPUT_PORT_NAME_NDR, ndr);
		}
	}
}
