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

package kieker.tools.bridge.connector;

import java.lang.reflect.Field;
import java.security.PrivilegedAction;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import kieker.common.record.IMonitoringRecord;
import kieker.tools.bridge.LookupEntity;

/**
 * This factory is an attempt to simplify the instantiation of new
 * connector types. However, we need to re-think this and move configuration to the Kieker configuration.
 *
 * @author Reiner Jung
 * @since 1.8
 *
 * @deprecated since 1.15 removed in 1.16 replaced by collector
 */
@Deprecated
public final class ServiceConnectorFactory {

	private static final String TYPES = "TYPES";

	// checkstyle wants this! What is the purpose of that?
	private ServiceConnectorFactory() {

	}

	/**
	 * Calculates the lookup table from the record map.
	 *
	 * @param recordMap
	 *            A map containing ids and IMonitoringRecord types
	 * @return A map containing record ids referencing constructor and field information
	 * @throws ConnectorDataTransmissionException
	 *             if the lookup table compilation fails
	 */
	public static ConcurrentMap<Integer, LookupEntity> createLookupEntityMap(final ConcurrentMap<Integer, Class<? extends IMonitoringRecord>> recordMap)
			throws ConnectorDataTransmissionException {
		final ConcurrentMap<Integer, LookupEntity> lookupEntityMap = new ConcurrentHashMap<>();

		for (final Map.Entry<Integer, Class<? extends IMonitoringRecord>> entry : recordMap.entrySet()) {
			final int key = entry.getKey();
			final Class<? extends IMonitoringRecord> type = entry.getValue();

			try {
				final Field parameterTypesField = type.getDeclaredField(TYPES);
				java.security.AccessController.doPrivileged(new PrivilegedAction<Object>() {
					@Override
					public Object run() {
						parameterTypesField.setAccessible(true);
						return null;
					}
				});
				final LookupEntity entity = new LookupEntity(type.getConstructor((Class<?>[]) parameterTypesField.get(null)),
						(Class<?>[]) parameterTypesField.get(null));
				lookupEntityMap.put(key, entity);
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
		return lookupEntityMap;
	}
}
