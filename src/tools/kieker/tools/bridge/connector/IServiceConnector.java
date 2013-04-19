/***************************************************************************
 * Copyright 2013 Kieker Project (http://kieker-monitoring.net)
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
 * 
 * 
 * @author Reiner Jung
 * @since 1.8
 */
public interface IServiceConnector {

	// TODO: errors should throw exceptions not deliver null, also avoid Exception, use subsets of it!
	/**
	 * The deserialize method reads source data and returns an {@link IMonitoringRecord}.
	 * 
	 * @return A {@link IMonitoringRecord} or null on read error or end of line.
	 * @throws Exception
	 *             may be caused by a read error or an unknown record id.
	 * 
	 * @since 1.8
	 */
	IMonitoringRecord deserialize() throws Exception;

	/**
	 * Called to setup the channel to read record information.
	 * 
	 * @throws Exception
	 *             when an error occurred setting up the record source.
	 * 
	 * @since 1.8
	 */
	void setup() throws Exception;

	/**
	 * Called to close the previously setup record source.
	 * 
	 * @throws Exception
	 *             when an error occurred during connection close.
	 * 
	 * @since 1.8
	 */
	void close() throws Exception;
}
