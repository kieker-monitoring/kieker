/***************************************************************************
 * Copyright 2017 Kieker Project (http://kieker-monitoring.net)
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

package kieker.examples.livedemo.analysis.filter;

import kieker.analysis.IProjectContext;
import kieker.analysis.plugin.annotation.InputPort;
import kieker.analysis.plugin.annotation.OutputPort;
import kieker.analysis.plugin.annotation.Plugin;
import kieker.analysis.plugin.filter.AbstractFilterPlugin;
import kieker.common.configuration.Configuration;
import kieker.common.record.controlflow.OperationExecutionRecord;
import kieker.common.util.signature.ClassOperationSignaturePair;
import kieker.common.util.signature.Signature;
import kieker.examples.livedemo.analysis.util.LimitedHashMap;
import kieker.examples.livedemo.common.EnrichedOperationExecutionRecord;

/**
 * A filter enriching Kieker's {@link OperationExecutionRecord} with a short signature and some comma seperated values. The filter uses an internal LRU cache for the
 * short signatures.
 * 
 * @author Bjoern Weissenfels, Nils Christian Ehmke
 * 
 * @since 1.9
 */
@Plugin(programmaticOnly = true,
		outputPorts = @OutputPort(name = OperationExecutionRecordEnrichmentFilter.OUTPUT_PORT_NAME_RECORDS, eventTypes = EnrichedOperationExecutionRecord.class))
public final class OperationExecutionRecordEnrichmentFilter extends AbstractFilterPlugin {

	public static final String INPUT_PORT_NAME_RECORDS = "inputRecords";
	public static final String OUTPUT_PORT_NAME_RECORDS = "outputRecords";

	private final LimitedHashMap<String, String> shortSignatureCache = new LimitedHashMap<String, String>(20);

	public OperationExecutionRecordEnrichmentFilter(final Configuration configuration, final IProjectContext projectContext) {
		super(configuration, projectContext);
	}

	@InputPort(name = OperationExecutionRecordEnrichmentFilter.INPUT_PORT_NAME_RECORDS, eventTypes = OperationExecutionRecord.class)
	public void input(final OperationExecutionRecord record) {
		final double responseTime = this.computeAndRoundResponseTime(record);
		final String shortSignature = this.createShortOperationSignature(record);
		final String commaSeperatedValues = record.toString();

		final EnrichedOperationExecutionRecord enrichedRecord = new EnrichedOperationExecutionRecord(record.getOperationSignature(), record.getSessionId(),
				record.getTraceId(), record.getTin(), record.getTout(), record.getHostname(), record.getEoi(), record.getEss(), responseTime, shortSignature,
				commaSeperatedValues);
		this.deliver(OperationExecutionRecordEnrichmentFilter.OUTPUT_PORT_NAME_RECORDS, enrichedRecord);
	}

	private double computeAndRoundResponseTime(final OperationExecutionRecord record) {
		double resp = record.getTout() - record.getTin();
		resp = resp / 1000000; // conversion to milliseconds
		return Math.round(resp * 10) / 10.0; // rounded to one decimal
	}

	private String createShortOperationSignature(final OperationExecutionRecord record) {
		final String operationSignatureStr = record.getOperationSignature();

		if (!this.shortSignatureCache.containsKey(operationSignatureStr)) {
			final ClassOperationSignaturePair classOperationSignaturePair = ClassOperationSignaturePair.splitOperationSignatureStr(operationSignatureStr);
			final Signature operationSignature = classOperationSignaturePair.getSignature();

			final String simpleClassName = classOperationSignaturePair.getSimpleClassname();
			final String operationName = operationSignature.getName();

			final String shortOperationSignature = "..." + simpleClassName + "." + operationName + "(...)";

			this.shortSignatureCache.put(operationSignatureStr, shortOperationSignature);
		}

		return this.shortSignatureCache.get(operationSignatureStr);
	}

	@Override
	public Configuration getCurrentConfiguration() {
		return new Configuration();
	}

}
