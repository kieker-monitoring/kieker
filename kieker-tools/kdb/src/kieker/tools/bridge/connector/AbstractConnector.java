/***************************************************************************
 * Copyright 2022 Kieker Project (http://kieker-monitoring.net)
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

import java.util.concurrent.ConcurrentMap;

import kieker.common.configuration.Configuration;
import kieker.tools.bridge.LookupEntity;

/**
 * Generic abstract connector used in all TCP services.
 *
 * @author Reiner Jung
 * @since 1.8
 *
 * @deprecated since 1.15 removed in 1.16 replaced by collector
 */
@Deprecated
public abstract class AbstractConnector implements IServiceConnector {

	/**
	 * Map containing record ids and the assigned constructor and field type list.
	 */
	protected ConcurrentMap<Integer, LookupEntity> lookupEntityMap;
	/**
	 * general configuration object used for the connector.
	 */
	protected Configuration configuration;

	/**
	 * AbstractTCPService constructor.
	 *
	 * @param configuration
	 *            Kieker configuration including setup for connectors
	 * @param lookupEntityMap
	 *            IMonitoringRecord constructor and TYPES-array to id map
	 */
	public AbstractConnector(final Configuration configuration, final ConcurrentMap<Integer, LookupEntity> lookupEntityMap) {
		this.lookupEntityMap = lookupEntityMap;
		this.configuration = configuration;
	}

}
