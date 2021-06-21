/***************************************************************************
 * Copyright 2020 Kieker Project (http://kieker-monitoring.net)
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

package kieker.tools.bridge.connector;

import kieker.common.record.IMonitoringRecord;

/**
 * Generic interface for every servcie connector.
 *
 * @author Reiner Jung
 * @since 1.8
 *
 * @deprecated since 1.15 removed in 1.16 replaced by collector
 */
@Deprecated
public interface IServiceConnector {

	/**
	 * The deserialize method reads source data and returns an {@link IMonitoringRecord}.
	 *
	 * @return A {@link IMonitoringRecord} or null on read error or end of line.
	 * @throws ConnectorDataTransmissionException
	 *             if a read error or an unknown record id occurs
	 * @throws ConnectorEndOfDataException
	 *             if the transmission is terminated or otherwise signaled that the last record has been received.
	 *
	 * @since 1.8
	 */
	IMonitoringRecord deserializeNextRecord() throws ConnectorDataTransmissionException, ConnectorEndOfDataException;

	/**
	 * Called to initialize the channel to read record information.
	 *
	 * @throws ConnectorDataTransmissionException
	 *             when an error occurred setting up the record source.
	 *
	 * @since 1.8
	 */
	void initialize() throws ConnectorDataTransmissionException;

	/**
	 * Called to close the previously initialize record source.
	 *
	 * @throws ConnectorDataTransmissionException
	 *             when an error occurred during connection close.
	 *
	 * @since 1.8
	 */
	void close() throws ConnectorDataTransmissionException;
}
