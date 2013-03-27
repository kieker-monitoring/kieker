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

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;

import kieker.common.record.IMonitoringRecord;
import kieker.tools.bridge.connector.jms.JMSEmbeddedService;
import kieker.tools.bridge.connector.jms.JMSService;
import kieker.tools.bridge.connector.tcp.TCPClientService;
import kieker.tools.bridge.connector.tcp.TCPMultiServerService;
import kieker.tools.bridge.connector.tcp.TCPSingleServerService;


/**
 * @author Reiner Jung -- initial contribution
 *
 */
public final class ServiceConnectorFactory {
	
	/**
	 * 
	 * @param recordMap
	 * @param username
	 * @param password
	 * @param url
	 * @return
	 */
	public static IServiceConnector createJMSServiceConnector(final Map<Integer, Class<IMonitoringRecord>> recordMap, 
			final String username, final String password, final URI url) {
		return new JMSService(recordMap, username, password, url);
	}

	/**
	 * 
	 * @param recordMap
	 * @param port
	 * @return
	 * @throws URISyntaxException
	 */
	public static IServiceConnector createJMSEmbeddedServiceConnector(Map<Integer, Class<IMonitoringRecord>> recordMap, int port) throws URISyntaxException {
		return new JMSEmbeddedService(recordMap,port);
	}

	/**
	 * 
	 * @param recordMap
	 * @param port
	 * @return
	 */
	public static IServiceConnector createTCPSingleServerServiceConnector(Map<Integer, Class<IMonitoringRecord>> recordMap, int port) {
		return new TCPSingleServerService(recordMap,port);
	}

	/**
	 * 
	 * @param recordMap
	 * @param port
	 * @return
	 */
	public static IServiceConnector createTCPMultiServerServiceConnector(Map<Integer, Class<IMonitoringRecord>> recordMap, int port) {
		return new TCPMultiServerService(recordMap,port);
	}

	/**
	 * 
	 * @param recordMap
	 * @param hostname
	 * @param port
	 * @return
	 */
	public static IServiceConnector createTCPClientServiceConnector(Map<Integer, Class<IMonitoringRecord>> recordMap, String hostname, int port) {
		return new TCPClientService(recordMap,hostname,port);
	}
}
