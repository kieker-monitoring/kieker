/***************************************************************************
 * Copyright 2021 Kieker Project (http://kieker-monitoring.net)
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

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import kieker.common.record.IMonitoringRecord;

/**
 * @author Christian Wulf
 *
 * @since 1.11
 */
public final class CachedRecordFactoryCatalog {

	private static final CachedRecordFactoryCatalog INSTANCE = new CachedRecordFactoryCatalog(new RecordFactoryResolver());

	private final ConcurrentMap<String, IRecordFactory<? extends IMonitoringRecord>> cachedRecordFactories;
	private final RecordFactoryResolver recordFactoryResolver;

	/**
	 * Create an {@link CachedRecordFactoryCatalog}.
	 */
	public CachedRecordFactoryCatalog() {
		this(new RecordFactoryResolver());
	}

	CachedRecordFactoryCatalog(final RecordFactoryResolver recordFactoryResolver) {
		this.cachedRecordFactories = new ConcurrentHashMap<String, IRecordFactory<? extends IMonitoringRecord>>();
		this.recordFactoryResolver = recordFactoryResolver;
	}

	/**
	 * Returns the only instance of this class.
	 *
	 * @return returns an factory catalog instance.
	 */
	public static CachedRecordFactoryCatalog getInstance() {
		return INSTANCE;
	}

	/**
	 * Hint: This method uses convention over configuration when searching for a record factory class.
	 *
	 * @param recordClassName
	 *            the record class name of the record class for which the factory is returned
	 * @return a cached record factory instance of the record class indicated by <code>recordClassName</code>.
	 *         <ul>
	 *         <li>If the cache does not contain a record factory instance, a new one is searched and instantiated via class path resolution.
	 *         <li>If there is no factory for the given <code>recordClassName</code>, a new {@code RecordFactoryWrapper} is created and stored for the given
	 *         <code>recordClassName</code>.
	 *         </ul>
	 */
	public IRecordFactory<? extends IMonitoringRecord> get(final String recordClassName) {
		IRecordFactory<? extends IMonitoringRecord> recordFactory = this.cachedRecordFactories.get(recordClassName);
		if (recordFactory == null) {
			recordFactory = this.recordFactoryResolver.get(recordClassName);
			if (recordFactory != null) {
				final IRecordFactory<? extends IMonitoringRecord> existingFactory = this.cachedRecordFactories.putIfAbsent(recordClassName, recordFactory);
				if (existingFactory != null) {
					recordFactory = existingFactory;
				}
			}
		}
		return recordFactory;
	}

}
