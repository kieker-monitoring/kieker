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

package kieker.tools.bridge.connector.tcp;

import java.lang.reflect.Field;
import java.security.PrivilegedAction;
import java.util.HashMap;
import java.util.Map;

import kieker.common.record.IMonitoringRecord;
import kieker.tools.bridge.LookupEntity;
import kieker.tools.bridge.connector.ConnectorDataTransmissionException;
import kieker.tools.bridge.connector.IServiceConnector;

/**
 * 
 * 
 * @author Reiner Jung
 * @since 1.8
 */
public abstract class AbstractTCPConnector implements IServiceConnector {

	private static final String TYPES = "TYPES";

	protected Map<Integer, LookupEntity> lookupEntityMap;

	private final Map<Integer, Class<IMonitoringRecord>> recordMap;

	/**
	 * AbstractTCPService constructor.
	 * 
	 * @param configuration
	 *            Kieker configuration
	 * @param recordMap
	 *            IMonitoringRecord to id map
	 */
	public AbstractTCPConnector(final Map<Integer, Class<IMonitoringRecord>> recordMap) {
		this.recordMap = recordMap;
	}

	public void initialize() throws ConnectorDataTransmissionException {
		this.lookupEntityMap = new HashMap<Integer, LookupEntity>();
		for (final int key : this.recordMap.keySet()) {
			final Class<IMonitoringRecord> type = this.recordMap.get(key);

			try {
				final Field parameterTypesField = type.getDeclaredField(TYPES);
				java.security.AccessController.doPrivileged(new PrivilegedAction<Object>() {
					public Object run() {
						parameterTypesField.setAccessible(true);
						return null;
					}
				});
				final LookupEntity entity = new LookupEntity(type.getConstructor(Object[].class), (Class<?>[]) parameterTypesField.get(null));
				this.lookupEntityMap.put(key, entity);
			} catch (final NoSuchFieldException e) {
				throw new ConnectorDataTransmissionException("Field " + TYPES + " does not exist.", e);
			} catch (final SecurityException e) {
				throw new ConnectorDataTransmissionException("Security exception.", e);
			} catch (final NoSuchMethodException e) {
				throw new ConnectorDataTransmissionException("Method not found. Should not occur, as we are not looking for any method.", e);
			} catch (final IllegalArgumentException e) {
				throw new ConnectorDataTransmissionException(e.getMessage(), e);
			} catch (final IllegalAccessException e) {
				throw new ConnectorDataTransmissionException(e.getMessage(), e);
			}
		}
	}

}
