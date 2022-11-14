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

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Optional;
import java.util.ServiceLoader;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import kieker.common.record.IMonitoringRecord;

/**
 * @author Christian Wulf
 *
 * @since 1.11
 */
@SuppressWarnings("rawtypes")
public class RecordFactoryResolver {

	private final List<IRecordFactoryProvider> factoryProviders;

	private final IRecordFactoryProvider defaultFactoryProvider = new StandardRecordFactoryProvider();

	/**
	 * Creates a new record factory resolver which finds available factory providers automatically using
	 * the service locator.
	 */
	public RecordFactoryResolver() {
		this(ServiceLoader.load(IRecordFactoryProvider.class));
	}
	
	/**
	 * Creates a new record factory resolver with the given factory providers.
	 * @param factoryProviders The factory providers to use
	 */
	public RecordFactoryResolver(final Iterable<IRecordFactoryProvider> factoryProviders) {
		this.factoryProviders = StreamSupport.stream(factoryProviders.spliterator(), false)
			.collect(Collectors.toList());
	}

	/**
	 * Obtains a record factory instance fo the given record class name.
	 * 
	 * @param recordClassName
	 *            fully qualified class name of a record
	 * @return a new instance of the record factory belonging to the given <code>recordClassName</code> or <code>null</code> if such a record factory could not
	 *         be found or instantiated
	 */
	@SuppressWarnings("unchecked")
	public IRecordFactory<? extends IMonitoringRecord> get(final String recordClassName) {
		try {
			final Class<?> recordClass = Class.forName(recordClassName);

			final RecordFactory factoryAnnotation = recordClass.getAnnotation(RecordFactory.class);
			if (factoryAnnotation != null) {
				// If a factory class is explicitly specified, it takes precedence over all other potential
				// factories
				final Class<?> factoryClass = factoryAnnotation.value();
				return this.createFactory(factoryClass);
			}

			// Otherwise, create the factory via the appropriate factory provider
			final IRecordFactoryProvider factoryProvider = this.determineProviderFor(recordClass);
			return (IRecordFactory<? extends IMonitoringRecord>) factoryProvider.createFactoryFor(recordClass);
		} catch (ClassNotFoundException e) {
			return null;
		}		
	}

	private IRecordFactoryProvider determineProviderFor(final Class<?> recordClass) {
		final Optional<IRecordFactoryProvider> applicableProvider = this.factoryProviders.stream()
			.filter(provider -> provider.isApplicableTo(recordClass))
			.findFirst();
			
		return applicableProvider.orElse(this.defaultFactoryProvider);
	}	

	private IRecordFactory createFactory(final Class<?> providerClass) {
		try {
			final Constructor<?> constructor = providerClass.getConstructor();
			return (IRecordFactory) constructor.newInstance();
		} catch (final InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
			return null;
		}
	}

}
