/***************************************************************************
 * Copyright 2013 by
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
package kieker.tools.bridge.connector;

import kieker.common.record.IMonitoringRecord;

/**
 * 
 * @author Reiner Jung -- initial contribution
 *
 */
public interface IServiceConnector {
	/**
	 * The deserialize method read source data and return an IMonitoringRecord.
	 * 
	 * @return A IMonitoringRecord or null on read error or end of line.
	 * @throws Exception may be caused by a read error or an unknown record id.
	 */
	IMonitoringRecord deserialize() throws Exception;

	/**
	 * Called to setup the channel to read record information. 
	 *  
	 * @throws Exception when an error occurred setting up the record source.
	 */
	void setup() throws Exception;

	/**
	 * Called to close the previously setup record source. 
	 *  
	 * @throws Exception when an error occurred during connection close.
	 */
	void close() throws Exception;

}
