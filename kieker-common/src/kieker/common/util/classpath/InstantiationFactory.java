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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import kieker.common.configuration.Configuration;

/**
 * This class encapsulates the creation of Kieker monitoring controllers.
 *
 * TODO integrate additional features from iObserve here
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
				// TODO is flatten really necessary? We do not modify the configuration
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
}
