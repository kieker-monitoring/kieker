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
package kieker.analysis.source.rewriter;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

import kieker.analysis.source.tcp.Connection;
import kieker.common.record.IMonitoringRecord;
import kieker.common.record.flow.ITraceRecord;
import kieker.common.record.flow.trace.TraceMetadata;
import kieker.common.record.misc.KiekerMetadataRecord;

import teetime.framework.OutputPort;

/**
 * This rewriter rewrites trace ids and ignores {@link ITraceRecord}s which are not preceded by a
 * {@link TraceMetadata} record.
 *
 * @author Reiner Jung
 * @since 1.15
 */
public class PlainTraceMetadataRewriter implements ITraceMetadataRewriter {

	private volatile AtomicLong traceId = new AtomicLong();
	private final Map<String, Map<Long, TraceMetadata>> metadatamap = new HashMap<>(); // NOPMD no concurrent use of this class

	/**
	 * Create the plain trace rewriter.
	 */
	public PlainTraceMetadataRewriter() {
		// empty default constructor
	}

	/**
	 * Trace data records use unique ids for their respective host. However, in a multi read stage
	 * these ids may be used on different hosts. Therefore, they have to be mapped.
	 *
	 * Fails in case of records appearing out of order, i.e., a ITraceRecord appearing before a
	 * TraceMetadata record.
	 *
	 * @param record
	 * @throws IOException
	 */
	@Override
	public void rewrite(final Connection connection, final IMonitoringRecord record, final long loggingTimestamp,
			final OutputPort<IMonitoringRecord> outputPort) throws IOException {
		// set logging time stamp
		record.setLoggingTimestamp(loggingTimestamp);
		if (record instanceof TraceMetadata) {
			final TraceMetadata traceMetadata = (TraceMetadata) record;
			traceMetadata.setTraceId(this.traceId.get());
			Map<Long, TraceMetadata> map = this.metadatamap.get(traceMetadata.getHostname());
			if (map == null) {
				map = new HashMap<>();
				this.metadatamap.put(traceMetadata.getHostname(), map);
			}
			map.put(traceMetadata.getTraceId(), traceMetadata);
			this.traceId.incrementAndGet();
			outputPort.send(traceMetadata);
		} else if (record instanceof ITraceRecord) {
			final SocketAddress remoteAddress = connection.getChannel().getRemoteAddress();
			final String ip = this.getIP(remoteAddress);
			final long inputTraceId = ((ITraceRecord) record).getTraceId();
			final Map<Long, TraceMetadata> map = this.metadatamap.get(ip);
			if (map != null) {
				final TraceMetadata metaData = map.get(inputTraceId);
				((ITraceRecord) record).setTraceId(metaData.getTraceId());

				outputPort.send(record);
			}
		} else if (record instanceof KiekerMetadataRecord) { // NOCS NOPMD explicitly ignore
																// metadata record
			/** ignore metadata record. */
		} else {
			/** pass all records unmodified, which are not trace records. */
			outputPort.send(record);
		}
	}

	private String getIP(final SocketAddress remoteAddress) {
		final InetSocketAddress sockaddr = (InetSocketAddress) remoteAddress;

		return sockaddr.getHostString();
	}

}
