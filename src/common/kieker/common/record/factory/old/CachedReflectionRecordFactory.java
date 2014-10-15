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

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import kieker.common.exception.MonitoringRecordException;
import kieker.common.record.IMonitoringRecord;
import kieker.common.util.registry.IRegistry;

/**
 * @author Christian Wulf
 *
 * @since 1.10
 */
public class CachedReflectionRecordFactory {

	private IRegistry<String> stringRegistry;
	private final CachedClassForNameResolver<IMonitoringRecord> classForNameResolver;
	private final ReflectionRecordFactory reflectionFactory;

	private final ConcurrentMap<String, Constructor<? extends IMonitoringRecord>> bufferConstructors = new ConcurrentHashMap<String, Constructor<? extends IMonitoringRecord>>();
	private final ConcurrentMap<String, Constructor<? extends IMonitoringRecord>> arrayConstructors = new ConcurrentHashMap<String, Constructor<? extends IMonitoringRecord>>();

	@Deprecated
	public CachedReflectionRecordFactory(final CachedClassForNameResolver<IMonitoringRecord> classForNameResolver) {
		this(null, classForNameResolver);
	}

	public CachedReflectionRecordFactory(final IRegistry<String> stringRegistry, final CachedClassForNameResolver<IMonitoringRecord> classForNameResolver) {
		this.stringRegistry = stringRegistry;
		this.classForNameResolver = classForNameResolver;
		this.reflectionFactory = new ReflectionRecordFactory(stringRegistry, this.classForNameResolver);
	}

	public IMonitoringRecord create(final String recordClassName, final ByteBuffer buffer) throws MonitoringRecordException {
		Constructor<? extends IMonitoringRecord> constructor = this.bufferConstructors.get(recordClassName);
		if (constructor == null) {
			Class<? extends IMonitoringRecord> clazz;
			try {
				clazz = this.classForNameResolver.classForName(recordClassName);
			} catch (final ClassNotFoundException e) {
				throw new MonitoringRecordException("", e);
			}

			if (!IMonitoringRecord.BinaryFactory.class.isAssignableFrom(clazz)) {
				// fall-through: use the regular record factory
				return this.reflectionFactory.create(clazz, buffer);
			}

			// Factory interface present
			constructor = this.getConstructorForClass(clazz, ByteBuffer.class, IRegistry.class);
			this.register(recordClassName, constructor);
		}
		return this.createNewInstance(constructor, buffer, this.stringRegistry);
	}

	@Deprecated
	public IMonitoringRecord create(final Class<? extends IMonitoringRecord> clazz, final Object[] values) throws MonitoringRecordException {
		return this.create(clazz.getCanonicalName(), values);
	}

	public IMonitoringRecord create(final String recordClassName, final Object[] values) throws MonitoringRecordException {
		Constructor<? extends IMonitoringRecord> constructor = this.arrayConstructors.get(recordClassName);
		if (constructor == null) {
			Class<? extends IMonitoringRecord> clazz;
			try {
				clazz = this.classForNameResolver.classForName(recordClassName);
			} catch (final ClassNotFoundException e) {
				throw new MonitoringRecordException("", e);
			}

			if (!IMonitoringRecord.Factory.class.isAssignableFrom(clazz)) {
				// fall-through: use the regular record factory
				return this.reflectionFactory.create(clazz, values);
			}

			// Factory interface present
			constructor = this.getConstructorForClass(clazz, Object[].class);
			this.register(recordClassName, constructor);
		}
		return this.createNewInstance(constructor, (Object) values);
	}

	private Constructor<? extends IMonitoringRecord> getConstructorForClass(final Class<? extends IMonitoringRecord> clazz, final Class<?>... parameterTypes)
			throws MonitoringRecordException {
		try {
			return clazz.getConstructor(parameterTypes);
		} catch (final NoSuchMethodException e) {
			throw new MonitoringRecordException("Failed to instatiate new monitoring record of type " + clazz.getCanonicalName(), e);
		} catch (final SecurityException e) {
			throw new MonitoringRecordException("Failed to instatiate new monitoring record of type " + clazz.getCanonicalName(), e);
		}
	}

	private IMonitoringRecord createNewInstance(final Constructor<? extends IMonitoringRecord> constructor, final Object... arguments)
			throws MonitoringRecordException {
		try {
			return constructor.newInstance(arguments);
		} catch (final InstantiationException e) {
			throw new MonitoringRecordException("Failed to instatiate new monitoring record of type " + constructor.getDeclaringClass().getCanonicalName(), e);
		} catch (final IllegalAccessException e) {
			throw new MonitoringRecordException("Failed to instatiate new monitoring record of type " + constructor.getDeclaringClass().getCanonicalName(), e);
		} catch (final IllegalArgumentException e) {
			throw new MonitoringRecordException("Failed to instatiate new monitoring record of type " + constructor.getDeclaringClass().getCanonicalName(), e);
		} catch (final InvocationTargetException e) {
			final Throwable cause = e.getCause();
			if (cause instanceof BufferUnderflowException) {
				throw (BufferUnderflowException) cause;
			}
			throw new MonitoringRecordException("Failed to instatiate new monitoring record of type " + constructor.getDeclaringClass().getCanonicalName(), e);
		}
	}

	public void register(final String recordClassName, final Constructor<? extends IMonitoringRecord> recordConstructor) {
		this.bufferConstructors.putIfAbsent(recordClassName, recordConstructor);
	}

	@Deprecated
	public void setStringRegistry(final IRegistry<String> stringRegistry) {
		this.stringRegistry = stringRegistry;
	}

}
