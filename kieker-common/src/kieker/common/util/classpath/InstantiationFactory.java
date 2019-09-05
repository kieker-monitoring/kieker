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

package kieker.common.util.classpath;

import java.lang.reflect.InvocationTargetException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import kieker.common.configuration.Configuration;
import kieker.common.exception.ConfigurationException;

/**
 * This class encapsulates the creation of Kieker monitoring controllers.
 *
 * @author Holger Knoche
 * @author Reiner Jung
 *
 * @since 1.15
 */
public final class InstantiationFactory {

	private static final Logger LOGGER = LoggerFactory.getLogger(InstantiationFactory.class);

	private static final InstantiationFactory INSTANCE = new InstantiationFactory();

	private InstantiationFactory() {
		// Default Constructor
	}

	/**
	 * Get an instance of the controller factory for the given configuration.
	 *
	 * @param configuration
	 *            The configuration to use
	 * @return A controller factory instance
	 */
	public static InstantiationFactory getInstance(final Configuration configuration) {
		// Currently, no configuration parameter for choosing a separate controller
		// factory is implemented
		return INSTANCE;
	}

	/**
	 * This is a helper method trying to find, create and initialize the given class, using its public constructor which accepts a single {@link Configuration}.
	 *
	 * @param c
	 *            This class defines the expected result of the method call.
	 * @param className
	 *            The name of the class to be created.
	 * @param configuration
	 *            The configuration which will be used to initialize the class in question.
	 *
	 * @return A new and initializes class instance if everything went well.
	 *
	 * @param <C>
	 *            The type of the returned class.
	 */
	@SuppressWarnings("unchecked")
	public <C> C createAndInitialize(final Class<C> c, final String className, final Configuration configuration) {
		C createdClass = null; // NOPMD (null)
		try {
			final Class<?> clazz = Class.forName(className);
			if (c.isAssignableFrom(clazz)) {
				createdClass = (C) clazz.getConstructor(Configuration.class).newInstance(configuration.flatten());
			} else {
				LOGGER.error("Class '{}' has to implement '{}'", className, c.getSimpleName());
			}
		} catch (final ClassNotFoundException e) {
			LOGGER.error("{}: Class '{}' not found", c.getSimpleName(), className, e);
		} catch (final NoSuchMethodException e) {
			LOGGER.error("{}: Class '{}' has to implement a (public) constructor that accepts a single Configuration", c.getSimpleName(), className, e);
		} catch (final Exception e) { // NOPMD NOCS (IllegalCatchCheck)
			// SecurityException, IllegalAccessException, IllegalArgumentException, InstantiationException, InvocationTargetException
			LOGGER.error("{}: Failed to load class for name '{}'", c.getSimpleName(), className, e);
		}
		return createdClass;
	}

	/**
	 * This is a helper method trying to find, create and initialize the given class, using its
	 * public constructor which accepts a single {@link Configuration}.
	 *
	 * @param implementedInterface
	 *            This class defines the expected result of the method call.
	 * @param className
	 *            The name of the class to be created.
	 * @param configuration
	 *            The configuration which will be used to initialize the class in question.
	 *
	 * @return A new and initializes class instance if everything went well.
	 *
	 * @param <C>
	 *            The type of the returned class.
	 *
	 * @throws ConfigurationException
	 *             on configuration errors during instantiation
	 */
	public static <C> C createWithConfiguration(final Class<C> implementedInterface, final String className,
			final kieker.common.configuration.Configuration configuration) throws ConfigurationException {
		try {
			final Class<?> clazz = Class.forName(className);
			if (implementedInterface.isAssignableFrom(clazz)) {
				final Class<?>[] parameterTypes = { kieker.common.configuration.Configuration.class };
				return InstantiationFactory.instantiateClass(implementedInterface, clazz, parameterTypes,
						configuration);
			} else {
				InstantiationFactory.LOGGER.error("Class '{}' has to implement '{}'.", className,
						implementedInterface.getSimpleName());
				throw new ConfigurationException("Requested class does not match interface.");
			}
		} catch (final ClassNotFoundException e) {
			InstantiationFactory.LOGGER.error("{}: Class '{}' not found: {}", implementedInterface.getSimpleName(),
					className, e.getLocalizedMessage());
			throw new ConfigurationException(e);
		}
	}

	/**
	 * This is a helper method .
	 *
	 * @param c
	 *            This class defines the expected result of the method call.
	 * @param className
	 *            The name of the class to be created.
	 * @param parameterTypes
	 *            types of the parameters
	 * @param parameters
	 *            parameter for the constructor
	 *
	 * @return A new and initializes class instance if everything went well.
	 *
	 * @param <C>
	 *            The type of the returned class.
	 */
	@SuppressWarnings("unchecked")
	public <C> C create(final Class<C> c, final String className, final Class<?>[] parameterTypes, final Object... parameters) {
		C createdClass = null; // NOPMD (null)
		try {
			final Class<?> clazz = Class.forName(className);
			if (c.isAssignableFrom(clazz)) {
				createdClass = (C) clazz.getConstructor(parameterTypes).newInstance(parameters);
			} else {
				LOGGER.error("Class '{}' has to implement '{}'", className, c.getSimpleName());
			}
		} catch (final ClassNotFoundException e) {
			LOGGER.error("{}: Class '{}' not found", c.getSimpleName(), className, e);
		} catch (final NoSuchMethodException e) {
			final StringBuilder parameterTypeNames = new StringBuilder();
			for (final Object parameter : parameters) {
				parameterTypeNames.append(", ");
				parameterTypeNames.append(parameter.getClass().getName());
			}
			LOGGER.error("{}: Class '{}' has to implement a (public) constructor that accepts {}", c.getSimpleName(), className, parameterTypeNames.toString(), e);
		} catch (final Exception e) { // NOPMD NOCS (IllegalCatchCheck)
			// SecurityException, IllegalAccessException, IllegalArgumentException, InstantiationException, InvocationTargetException
			LOGGER.error("{}: Failed to load class for name '{}'", c.getSimpleName(), className, e);
		}
		return createdClass;
	}

	/**
	 * Instantiate a class implementing the given interface.
	 *
	 * @param implementedInterface
	 *            a class or interface defining the implementation interface
	 * @param clazz
	 *            the type to be instantiated
	 * @param parameterTypes
	 *            the signature for the constructor
	 * @param parameters
	 *            the matching parameter values for the constructor
	 * @return returns an initialized instance of clazz
	 *
	 * @throws ConfigurationException
	 *             in case the class could not be instantiated
	 */
	@SuppressWarnings("unchecked")
	private static <C> C instantiateClass(final Class<C> implementedInterface, final Class<?> clazz, // NOPMD
			final Class<?>[] parameterTypes, final Object... parameters) throws ConfigurationException { // NOPMD
		// complexity necessary to check all types of errors
		try {
			return (C) clazz.getConstructor(parameterTypes).newInstance(parameters);
		} catch (final InstantiationException e) {
			InstantiationFactory.LOGGER.error("{}: Class '{}' cannot be instantiated (abstract class): {}",
					implementedInterface.getSimpleName(), clazz.getName(), e.getLocalizedMessage());
			throw new ConfigurationException(e);
		} catch (final IllegalAccessException | SecurityException e) {
			InstantiationFactory.LOGGER.error("{}: Access to class '{}' denied: {}",
					implementedInterface.getSimpleName(), clazz.getName(), e.getLocalizedMessage());
			throw new ConfigurationException(e);
		} catch (final IllegalArgumentException e) {
			InstantiationFactory.LOGGER.error(
					"{}: Constructor signature of class '{}' does not match given parameters: {}",
					implementedInterface.getSimpleName(), clazz.getName(), e.getLocalizedMessage());
			throw new ConfigurationException(e);
		} catch (final InvocationTargetException e) {
			InstantiationFactory.LOGGER.error("{}: Constructor of class '{}' failed to initialite instance: {}",
					implementedInterface.getSimpleName(), clazz.getName(), e.getLocalizedMessage());
			throw new ConfigurationException(e);
		} catch (final NoSuchMethodException e) {
			final StringBuilder parameterTypeNames = new StringBuilder();
			for (final Object parameter : parameters) {
				parameterTypeNames.append(", ");
				parameterTypeNames.append(parameter.getClass().getName());
			}
			InstantiationFactory.LOGGER.error(
					"{}: Class '{}' has to implement a (public) constructor that accepts the signature {}.",
					implementedInterface.getSimpleName(), clazz.getName(), parameterTypeNames.toString(), e);
			throw new ConfigurationException(e);
		}
	}
}
