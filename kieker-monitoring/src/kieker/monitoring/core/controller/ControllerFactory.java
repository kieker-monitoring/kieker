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

package kieker.monitoring.core.controller;

import java.lang.reflect.InvocationTargetException;

import kieker.common.configuration.Configuration;
import kieker.common.logging.Log;
import kieker.common.logging.LogFactory;

/**
 * This class encapsulates the creation of Kieker monitoring controllers.
 *
 * @author Holger Knoche
 *
 * @since 1.13
 */
public final class ControllerFactory {

	private static final Log LOG = LogFactory.getLog(ControllerFactory.class);

	private static final ControllerFactory INSTANCE = new ControllerFactory();

	private ControllerFactory() {
		// Default Constructor
	}

	/**
	 * Get an instance of the controller factory for the given configuration.
	 *
	 * @param configuration
	 *            The configuration to use
	 * @return A controller factory instance
	 */
	public static ControllerFactory getInstance(final Configuration configuration) {
		// Currently, no configuration parameter for choosing a separate controller
		// factory is implemented
		return INSTANCE;
	}

	/**
	 * This is a helper method trying to find, create and initialize the given class, using its public constructor which accepts a single {@link Configuration}.
	 *
	 * @param c
	 *            This class defines the expected result of the method call.
	 * @param classname
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
				createdClass = (C) this.instantiate(clazz, className, configuration);
			} else {
				LOG.error("Class '" + className + "' has to implement '" + c.getSimpleName() + "'");
			}
		} catch (final ClassNotFoundException e) {
			LOG.error(c.getSimpleName() + ": Class '" + className + "' not found", e);
		} catch (final NoSuchMethodException e) {
			LOG.error(c.getSimpleName() + ": Class '" + className
					+ "' has to implement a (public) constructor that accepts a single Configuration", e);
		} catch (final Exception e) { // NOPMD NOCS (IllegalCatchCheck)
			// SecurityException, IllegalAccessException, IllegalArgumentException, InstantiationException, InvocationTargetException
			LOG.error(c.getSimpleName() + ": Failed to load class for name '" + className + "'", e);
		}
		return createdClass;
	}

	/**
	 * This is a helper method trying to find and create the given class, using its public constructor which accepts no parameter.
	 *
	 * @param c
	 *            This class defines the expected result of the method call.
	 * @param classname
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
	public <C> C create(final Class<C> c, final String className) {
		C createdClass = null; // NOPMD (null)
		try {
			final Class<?> clazz = Class.forName(className);
			if (c.isAssignableFrom(clazz)) {
				createdClass = (C) clazz.getConstructor().newInstance();
			} else {
				LOG.error("Class '" + className + "' has to implement '" + c.getSimpleName() + "'");
			}
		} catch (final ClassNotFoundException e) {
			LOG.error(c.getSimpleName() + ": Class '" + className + "' not found", e);
		} catch (final NoSuchMethodException e) {
			LOG.error(c.getSimpleName() + ": Class '" + className
					+ "' has to implement a (public) constructor without any parameter", e);
		} catch (final Exception e) { // NOPMD NOCS (IllegalCatchCheck)
			// SecurityException, IllegalAccessException, IllegalArgumentException, InstantiationException, InvocationTargetException
			LOG.error(c.getSimpleName() + ": Failed to load class for name '" + className + "'", e);
		}
		return createdClass;
	}

	/**
	 * Instantiate a class with one configuration parameter.
	 *
	 * @param clazz
	 *            the class type
	 * @param className
	 *            the class name
	 * @param configuration
	 *            kieker configuration
	 * @return the instantiated class
	 * @throws InstantiationException
	 *             when instantiation fails
	 * @throws IllegalAccessException
	 *             when the class cannot be accessed
	 * @throws IllegalArgumentException
	 *             when there is no zero parameter constructor
	 * @throws InvocationTargetException
	 *             when the invocation fails
	 * @throws NoSuchMethodException
	 *             when there is no constructor
	 * @throws SecurityException
	 *             when the security setup prohibits access
	 */
	private <C> C instantiate(final Class<C> clazz, final String className, final Configuration configuration)
			throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {

		// Choose the appropriate configuration to pass
		final Configuration configurationToPass;
		if (clazz.isAnnotationPresent(ReceiveUnfilteredConfiguration.class)) {
			configurationToPass = configuration.flatten();
		} else {
			configurationToPass = configuration.getPropertiesStartingWith(className);
		}

		return clazz.getConstructor(Configuration.class).newInstance(configurationToPass);
	}

}
