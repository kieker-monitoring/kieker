/***************************************************************************
 * Copyright 2019 Kieker Project (http://kieker-monitoring.net)
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
package kieker.analysis.source.rewriter;

import java.io.IOException;

import kieker.analysis.source.tcp.Connection;
import kieker.common.record.IMonitoringRecord;

import teetime.framework.OutputPort;

/**
 * This is an empty rewriter which sends all received records to the output port.
 *
 * @author Reiner Jung
 *
 * @since 1.15
 */
public class NoneTraceMetadataRewriter implements ITraceMetadataRewriter {

	/**
	 * Create the rewriter.
	 */
	public NoneTraceMetadataRewriter() {
		// empty default constructor
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see org.iobserve.stages.source.ITraceMetadataRewriter#rewrite(org.iobserve.stages.source.
	 * Connection, kieker.common.record.IMonitoringRecord, long, teetime.framework.OutputPort)
	 */
	@Override
	public void rewrite(final Connection connection, final IMonitoringRecord record, final long loggingTimestamp,
			final OutputPort<IMonitoringRecord> outputPort) throws IOException {
		outputPort.send(record);
	}

}
