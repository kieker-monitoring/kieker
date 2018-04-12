/***************************************************************************
 * Copyright 2017 Kieker Project (http://kieker-monitoring.net)
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

package kieker.analysis.plugin.reader;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

import kieker.common.registry.reader.ReaderRegistry;

/**
 * Cache to store and retrieve string registries.
 *
 * @author Holger Knoche
 *
 * @since 1.13
 */
public class StringRegistryCache {

	private final Cache<Long, ReaderRegistry<String>> cache;

	/**
	 * Creates a new registry cache using the given cache duration configuration.
	 *
	 * @param expirationDuration
	 *            The duration until a registry is evicted
	 * @param expirationTimeUnit
	 *            The time unit for the expiration duration
	 */
	public StringRegistryCache(final long expirationDuration, final TimeUnit expirationTimeUnit) {
		this.cache = this.createCache(expirationDuration, expirationTimeUnit);
	}

	/**
	 * Creates the underlying cache. This method can be overridden to tweak the cache configuration.
	 *
	 * @param expirationDuration
	 *            The duration until a cache entry expires
	 * @param expirationTimeUnit
	 *            The time unit for the expiration duration
	 * @return The cache instance to use
	 */
	protected Cache<Long, ReaderRegistry<String>> createCache(final long expirationDuration, final TimeUnit expirationTimeUnit) {
		return CacheBuilder.newBuilder()
				.expireAfterAccess(expirationDuration, expirationTimeUnit)
				.build();
	}

	/**
	 * Retrieves the registry with the given ID and creates a new one if necessary.
	 *
	 * @param registryId
	 *            The ID of the desired registry
	 * @return The appropriate string registry
	 */
	@SuppressWarnings("PMD.AvoidThrowingRawExceptionTypes")
	public ReaderRegistry<String> getOrCreateRegistry(final Long registryId) {
		try {
			return this.cache.get(registryId, new RegistryLoader());
		} catch (final ExecutionException e) {
			// This should not happen since the loader only creates a
			// new registry
			throw new RuntimeException(e);
		}
	}

	/**
	 * This class creates a new registry if required.
	 *
	 * @author Holger Knoche
	 *
	 * @since 1.13
	 */
	private static class RegistryLoader implements Callable<ReaderRegistry<String>> {

		public RegistryLoader() {
			// empty constructor
		}

		@Override
		public ReaderRegistry<String> call() throws Exception {
			return new ReaderRegistry<>();
		}

	}

}
