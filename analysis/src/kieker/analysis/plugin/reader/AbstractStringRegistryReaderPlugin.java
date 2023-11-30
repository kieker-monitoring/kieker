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

package kieker.analysis.plugin.reader;

import java.nio.ByteBuffer;
import java.util.concurrent.TimeUnit;

import kieker.analysis.IProjectContext;
import kieker.analysis.plugin.annotation.Plugin;
import kieker.common.configuration.Configuration;
import kieker.common.record.IMonitoringRecord;

/**
 * This class should be used as the abstract super class for all readers which make
 * use of string registries to reduce the amount of transferred data. It provides
 * the necessary infrastructure to handle multiple concurrent registries for multiple
 * clients and other convenience methods.
 *
 * @author Holger Knoche
 *
 * @since 1.13
 * @deprecated since 1.15.1 old plugin api
 */
@Deprecated
@Plugin
public abstract class AbstractStringRegistryReaderPlugin extends AbstractReaderPlugin {

	private static final long DEFAULT_CACHE_EXPIRATION_DURATION = 1;
	private static final TimeUnit DEFAULT_CACHE_EXPIRATION_TIME_UNIT = TimeUnit.MINUTES;

	private final StringRegistryCache stringRegistryLookup;

	private volatile boolean threadsStarted;

	private volatile Thread registryRecordHandlerThread;
	private volatile RegistryRecordHandler registryRecordHandler;
	private volatile RegularRecordHandler regularRecordHandler;

	private volatile Thread regularRecordHandlerThread;

	/**
	 * Each Plugin requires a constructor with a Configuration object and a IProjectContext.
	 *
	 * @param configuration
	 *            The configuration for this component.
	 * @param projectContext
	 *            The project context for this component. The component will be registered.
	 */
	public AbstractStringRegistryReaderPlugin(final Configuration configuration, final IProjectContext projectContext) {
		this(configuration, projectContext, DEFAULT_CACHE_EXPIRATION_DURATION, DEFAULT_CACHE_EXPIRATION_TIME_UNIT);
	}

	/**
	 * Constructor which allows to specify a configuration property which contains the cache duration in the
	 * given time unit. Since defaults are populated by the super constructor, the access to the configuration
	 * property needs to be deferred until after the super constructor invocation.
	 *
	 * @param configuration
	 *            The configuration for this component.
	 * @param projectContext
	 *            The project context for this component. The component will be registered.
	 * @param cacheDurationProperty
	 *            The name of the configuration property containing the cache duration
	 * @param cacheDurationTimeUnit
	 *            The time unit for the cache duration
	 */
	protected AbstractStringRegistryReaderPlugin(final Configuration configuration, final IProjectContext projectContext, final String cacheDurationProperty,
			final TimeUnit cacheDurationTimeUnit) {

		super(configuration, projectContext);

		// Since the defaults are populated by the super constructor, the property access
		// must be performed after the super call
		final long cacheExpirationDuration = configuration.getLongProperty(cacheDurationProperty);
		this.stringRegistryLookup = new StringRegistryCache(cacheExpirationDuration, cacheDurationTimeUnit);
	}

	private AbstractStringRegistryReaderPlugin(final Configuration configuration, final IProjectContext projectContext, final long cacheExpirationDuration,
			final TimeUnit cacheExpirationTimeUnit) {

		super(configuration, projectContext);

		this.stringRegistryLookup = this.createCache(cacheExpirationDuration, cacheExpirationTimeUnit);
	}

	@Override
	public boolean init() {
		final boolean superInitSucceeded = super.init();

		if (!superInitSucceeded) {
			return false;
		}

		// Set up record handlers
		this.registryRecordHandler = new RegistryRecordHandler(this.stringRegistryLookup);
		this.regularRecordHandler = new RegularRecordHandler(this, this.stringRegistryLookup);

		// Set up threads
		this.registryRecordHandlerThread = new Thread(this.registryRecordHandler);
		this.registryRecordHandlerThread.setDaemon(true);

		this.regularRecordHandlerThread = new Thread(this.regularRecordHandler);
		this.regularRecordHandlerThread.setDaemon(true);

		return true;
	}

	/**
	 * Creates the cache to be used for storing string registries. This method can be overriden
	 * to use a particular cache implementation.
	 *
	 * @param expirationDuration
	 *            The duration until a cache entry expires
	 * @param expirationTimeUnit
	 *            The time unit for the cache expiration
	 * @return An appropriate cache instance
	 */
	protected StringRegistryCache createCache(final long expirationDuration, final TimeUnit expirationTimeUnit) {
		return new StringRegistryCache(expirationDuration, expirationTimeUnit);
	}

	/**
	 * Ensures that the required threads are started. This method should be called before
	 * registering a record.
	 */
	protected final void ensureThreadsStarted() {
		if (!this.threadsStarted) {
			this.registryRecordHandlerThread.start();
			this.regularRecordHandlerThread.start();
			this.threadsStarted = true;
		}
	}

	/**
	 * Handles the given raw data for a registry record.
	 *
	 * @param buffer
	 *            Byte buffer containing the raw data for the record
	 */
	protected void handleRegistryRecord(final ByteBuffer buffer) {
		this.registryRecordHandler.enqueueRegistryRecord(buffer);
	}

	/**
	 * Handles the given raw data for a regular record.
	 *
	 * @param buffer
	 *            Byte buffer containing the raw data for the record
	 */
	protected void handleRegularRecord(final ByteBuffer buffer) {
		this.regularRecordHandler.enqueueRegularRecord(buffer);
	}

	/**
	 * Delivers the given record to the appropriate output port(s).
	 *
	 * @param monitoringRecord
	 *            The monitoring record to deliver
	 */
	protected abstract void deliverRecord(IMonitoringRecord monitoringRecord);

}
