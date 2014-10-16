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

package kieker.common.record.factory.old;

import java.nio.ByteBuffer;

import kieker.common.exception.MonitoringRecordException;
import kieker.common.record.IMonitoringRecord;
import kieker.common.util.classpath.CachedClassForNameResolver;
import kieker.common.util.registry.IRegistry;

/**
 * @author Christian Wulf
 *
 * @since 1.10
 */
public class ReflectionRecordFactory {

	private final IRegistry<String> stringRegistry;
	private final CachedClassForNameResolver<IMonitoringRecord> classForNameResolver;

	public ReflectionRecordFactory(final IRegistry<String> stringRegistry, final CachedClassForNameResolver<IMonitoringRecord> classForNameResolver) {
		this.stringRegistry = stringRegistry;
		this.classForNameResolver = classForNameResolver;
	}

	public IMonitoringRecord create(final String recordClassName, final ByteBuffer buffer) throws MonitoringRecordException {
		Class<? extends IMonitoringRecord> clazz;
		try {
			clazz = this.classForNameResolver.classForName(recordClassName);
		} catch (final Exception e) {
			throw new MonitoringRecordException("", e);
		}

		return this.create(clazz, buffer);
	}

	IMonitoringRecord create(final Class<? extends IMonitoringRecord> clazz, final ByteBuffer buffer) throws MonitoringRecordException {
		final IMonitoringRecord record = this.createNewInstance(clazz);
		record.initFromBytes(buffer, this.stringRegistry);
		return record;
	}

	public IMonitoringRecord create(final String recordClassName, final Object[] values) throws MonitoringRecordException {
		Class<? extends IMonitoringRecord> clazz;
		try {
			clazz = this.classForNameResolver.classForName(recordClassName);
		} catch (final Exception e) {
			throw new MonitoringRecordException("", e);
		}

		return this.create(clazz, values);
	}

	IMonitoringRecord create(final Class<? extends IMonitoringRecord> clazz, final Object[] values) throws MonitoringRecordException {
		final IMonitoringRecord record = this.createNewInstance(clazz);
		record.initFromArray(values);
		return record;
	}

	private IMonitoringRecord createNewInstance(final Class<? extends IMonitoringRecord> clazz) throws MonitoringRecordException {
		try {
			return clazz.newInstance();
		} catch (final InstantiationException e) {
			throw new MonitoringRecordException("Failed to instatiate new monitoring record of type " + clazz.getCanonicalName(), e);
		} catch (final IllegalAccessException e) {
			throw new MonitoringRecordException("Failed to instatiate new monitoring record of type " + clazz.getCanonicalName(), e);
		}
	}

	public IRegistry<String> getStringRegistry() {
		return this.stringRegistry;
	}

}
