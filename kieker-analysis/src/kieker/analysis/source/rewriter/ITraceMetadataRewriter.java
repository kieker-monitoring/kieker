/***************************************************************************
 * Copyright 2021 Kieker Project (http://kieker-monitoring.net)
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
import kieker.common.record.flow.trace.TraceMetadata;

import teetime.framework.OutputPort;

/**
 * In case {@link TraceMetadata} records are received out of order, i.e., not before the first trace
 * related record, different strategies can be applied. This interface provides a common interface
 * for the implementation of such strategies.
 *
 * @author Reiner Jung
 *
 * @since 1.15
 */
public interface ITraceMetadataRewriter {

	/**
	 * Rewrite a record if necessary.
	 *
	 * @param connection
	 *            connection used for incoming records
	 * @param record
	 *            the incoming record
	 * @param loggingTimestamp
	 *            the logging timestamp to be used
	 * @param outputPort
	 *            the output port for sending the rewritten record
	 *
	 * @throws IOException
	 *             might occur during connection
	 * @since 1.15
	 */
	void rewrite(Connection connection, IMonitoringRecord record, long loggingTimestamp,
			OutputPort<IMonitoringRecord> outputPort) throws IOException;

}
