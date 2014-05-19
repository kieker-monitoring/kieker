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
package kieker.panalysis.examples.traceReconstruction;

import kieker.common.util.filesystem.FSUtil;
import kieker.panalysis.framework.core.Analysis;
import kieker.panalysis.predicate.FileExtensionPredicate;

/**
 * @author Christian Wulf
 * 
 * @since 1.10
 */
public class TraceAnalysis extends Analysis {

	public static void main(final String[] args) {
		// FSReader reader
		// StringBufferFilter stringBufferFilter
		// TimestampFilter timestampFilter
		// TraceIdFilter traceIdFilter
		// ExecutionRecordTransformationFilter execRecTransformer
		// TraceReconstructionFilter mtReconstrFilter
		// EventRecordTraceReconstructionFilter eventTraceReconstructionFilter
		// EventRecordTraceCounter eventRecordTraceCounter
		// TraceEventRecords2ExecutionAndMessageTraceFilter traceEvents2ExecutionAndMessageTraceFilter

		new FileExtensionPredicate(FSUtil.NORMAL_FILE_EXTENSION);

		// reader -> stringBufferFilter

		// stringBufferFilter -> timestampFilter.INPUT_PORT_NAME_EXECUTION
		// stringBufferFilter -> timestampFilter.INPUT_PORT_NAME_FLOW
		// timestampFilter -> traceIdFilter

		// traceIdFilter -> execRecTransformer
		// execRecTransformer -> SYSTEM_ENTITY_FACTORY

		// mtReconstrFilter -> SYSTEM_ENTITY_FACTORY
		// execRecTransformer -> mtReconstrFilter

		// traceIdFilter -> eventTraceReconstructionFilter
		// eventTraceReconstructionFilter -> eventRecordTraceCounter.INPUT_PORT_NAME_VALID
		// eventTraceReconstructionFilter -> eventRecordTraceCounter.INPUT_PORT_NAME_INVALID

		// eventTraceReconstructionFilter -> traceEvents2ExecutionAndMessageTraceFilter
		// traceEvents2ExecutionAndMessageTraceFilter -> SYSTEM_ENTITY_FACTORY
	}

	@Override
	public void init() {
		// TODO
	}
}
