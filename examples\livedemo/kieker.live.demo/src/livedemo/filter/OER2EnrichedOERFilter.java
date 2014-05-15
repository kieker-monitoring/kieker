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

package livedemo.filter;

import kieker.analysis.IProjectContext;
import kieker.analysis.plugin.annotation.InputPort;
import kieker.analysis.plugin.annotation.OutputPort;
import kieker.analysis.plugin.annotation.Plugin;
import kieker.analysis.plugin.filter.AbstractFilterPlugin;
import kieker.common.configuration.Configuration;
import kieker.common.record.controlflow.OperationExecutionRecord;

import livedemo.entities.EnrichedOERecord;

/**
 * @author Bjoern Weissenfels
 * 
 * @since 1.9
 */
@Plugin(programmaticOnly = true,
		description = "A filter collecting incoming objects in a list",
		outputPorts = @OutputPort(
				name = OER2EnrichedOERFilter.OUTPUT_PORT_NAME,
				eventTypes = { EnrichedOERecord.class },
				description = "Provides each incoming object"))
public class OER2EnrichedOERFilter extends AbstractFilterPlugin {

	public static final String INPUT_PORT_NAME = "inputObject";
	public static final String OUTPUT_PORT_NAME = "outputObjects";

	public OER2EnrichedOERFilter(final Configuration configuration,
			final IProjectContext projectContext) {
		super(configuration, projectContext);
	}

	@InputPort(name = OER2EnrichedOERFilter.INPUT_PORT_NAME, eventTypes = OperationExecutionRecord.class)
	public void input(final OperationExecutionRecord record) {
		final double responseTime = this.computeResponseTime(record);
		final String shortSignature = this.createShortSignature(record);
		final String commaSeperatedValues = record.toString();
		this.deliver(
				OER2EnrichedOERFilter.OUTPUT_PORT_NAME,
				new EnrichedOERecord(record.getOperationSignature(), record.getSessionId(), record.getTraceId(), record.getTin(), record.getTout(), record
						.getHostname(), record.getEoi(), record.getEss(), responseTime, shortSignature, commaSeperatedValues));
	}

	private double computeResponseTime(final OperationExecutionRecord record) {
		double resp = record.getTout() - record.getTin();
		resp = resp / 1000000; // conversion to milliseconds
		final double rounded = Math.round(resp * 10) / 10.0; // rounded to one decimal
		return rounded;
	}

	private String createShortSignature(final OperationExecutionRecord record) {
		String[] array = record.getOperationSignature().split("\\(");
		array = array[0].split("\\.");
		final int end = array.length;
		final String result = "..." + array[end - 2] + "." + array[end - 1] + "(...)";
		return result;
	}

	@Override
	public Configuration getCurrentConfiguration() {
		return new Configuration();
	}

}
