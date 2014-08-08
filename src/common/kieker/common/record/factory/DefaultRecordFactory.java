/***************************************************************************
 * Copyright 2014 Kieker Project (http://kieker-monitoring.net)
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

package kieker.common.record.factory;

import java.nio.ByteBuffer;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import kieker.common.exception.MonitoringRecordException;
import kieker.common.record.IMonitoringRecord;
import kieker.common.util.registry.ILookup;

/**
 * @author Christian Wulf
 *
 * @since 1.10
 */
public class DefaultRecordFactory {

	private final ILookup<String> stringRegistry;
	private final ConcurrentMap<String, IRecordFactoryMethod> recordFactoryMethods = new ConcurrentHashMap<String, IRecordFactoryMethod>();

	public DefaultRecordFactory(final ILookup<String> stringRegistry) {
		this.stringRegistry = stringRegistry;
	}

	public IMonitoringRecord create(final String recordClassName, final ByteBuffer buffer) throws MonitoringRecordException {
		final IRecordFactoryMethod recordFactoryMethod = this.recordFactoryMethods.get(recordClassName);
		if (recordFactoryMethod == null) {
			throw new IllegalStateException("recordClassName: " + recordClassName);
		}
		return recordFactoryMethod.create(buffer, this.stringRegistry);
	}

	public void register(final String recordClassName, final IRecordFactoryMethod recordFactoryMethod) {
		this.recordFactoryMethods.putIfAbsent(recordClassName, recordFactoryMethod);
	}
}
