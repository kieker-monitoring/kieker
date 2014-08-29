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

import kieker.common.exception.MonitoringRecordException;
import kieker.common.record.IMonitoringRecord;

/**
 * @author Christian Wulf
 *
 * @since 1.10
 */
@SuppressWarnings("rawtypes")
public class RecordFactoryRepository {

	private final ClassForNameResolver<IRecordFactory> classForNameResolver;

	public RecordFactoryRepository() {
		this.classForNameResolver = new ClassForNameResolver<IRecordFactory>(IRecordFactory.class);
	}

	private String buildRecordFactoryClassName(final String recordClassName) {
		return recordClassName + "Factory";
	}

	@SuppressWarnings("unchecked")
	public IRecordFactory<? extends IMonitoringRecord> get(final String recordClassName) throws MonitoringRecordException, ClassNotFoundException {
		final String recordFactoryClassName = this.buildRecordFactoryClassName(recordClassName);
		final Class<? extends IRecordFactory> recordFactoryClass = this.classForNameResolver.classForName(recordFactoryClassName);

		try {
			return recordFactoryClass.newInstance();
		} catch (final InstantiationException e) {
			throw new MonitoringRecordException("Unable to create an instance of " + recordFactoryClass, e);
		} catch (final IllegalAccessException e) {
			throw new MonitoringRecordException("Unable to create an instance of " + recordFactoryClass, e);
		}
	}
}
