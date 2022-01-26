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

package kieker.common.record.factory;

import kieker.common.record.IMonitoringRecord;
import kieker.common.util.classpath.ClassForNameResolver;

/**
 * @author Christian Wulf
 *
 * @since 1.11
 */
@SuppressWarnings("rawtypes")
public class RecordFactoryResolver {

	private final ClassForNameResolver<IRecordFactory> classForNameResolver;

	/**
	 * Create a record factory resolver.
	 */
	public RecordFactoryResolver() {
		this.classForNameResolver = new ClassForNameResolver<IRecordFactory>(IRecordFactory.class);
	}

	private String buildRecordFactoryClassName(final String recordClassName) {
		return recordClassName + "Factory";
	}

	/**
	 * @param recordClassName
	 *            fully qualified class name of a record
	 * @return a new instance of the record factory belonging to the given <code>recordClassName</code> or <code>null</code> if such a record factory could not be
	 *         found or instantiated
	 */
	@SuppressWarnings("unchecked")
	public IRecordFactory<? extends IMonitoringRecord> get(final String recordClassName) {
		final String recordFactoryClassName = this.buildRecordFactoryClassName(recordClassName);

		try {
			final Class<? extends IRecordFactory> recordFactoryClass = this.classForNameResolver.classForName(recordFactoryClassName);
			return recordFactoryClass.newInstance();
		} catch (final ClassNotFoundException e) {
			return null;
		} catch (final InstantiationException e) {
			return null;
		} catch (final IllegalAccessException e) {
			return null;
		}
	}
}
