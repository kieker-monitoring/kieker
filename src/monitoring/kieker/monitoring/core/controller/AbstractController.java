/***************************************************************************
 * Copyright 2011 by
 *  + Christian-Albrechts-University of Kiel
 *    + Department of Computer Science
 *      + Software Engineering Group 
 *  and others.
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

import java.util.concurrent.atomic.AtomicBoolean;

import kieker.monitoring.core.configuration.Configuration;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author Jan Waller
 */
public abstract class AbstractController {
	private static final Log LOG = LogFactory.getLog(AbstractController.class);

	private final AtomicBoolean terminated = new AtomicBoolean(false);
	protected volatile MonitoringController monitoringController = null;

	protected final synchronized void setMonitoringController(final MonitoringController monitoringController) {
		if (this.monitoringController == null) {
			this.monitoringController = monitoringController;
			this.init();
		}
	}

	/**
	 * Permanently terminates this controller.
	 * 
	 * @see #isTerminated()
	 */
	protected final boolean terminate() {
		if (!this.terminated.getAndSet(true)) {
			this.cleanup();
			if (this.monitoringController != null) {
				this.monitoringController.terminate();
			}
			return true;
		}
		return false;
	}

	/**
	 * Returns whether this controller is terminated.
	 * 
	 * @see #terminate()
	 * @return true if terminated
	 */
	protected final boolean isTerminated() {
		return this.terminated.get();
	}

	/**
	 * inits
	 */
	protected abstract void init();
	
	/**
	 * cleans up
	 */
	protected abstract void cleanup();

	@Override
	public abstract String toString();

	@SuppressWarnings("unchecked")
	protected final static <C> C createAndInitialize(final Class<C> c, final String classname, final Configuration configuration) {
		C createdClass = null;  // NOPMD
		try {
			final Class<?> clazz = Class.forName(classname);
			if (c.isAssignableFrom(clazz)) {
				createdClass = (C) clazz.getConstructor(Configuration.class).newInstance(configuration.getPropertiesStartingWith(classname));
			} else {
				AbstractController.LOG.error("Class '" + classname + "' has to implement '" + c.getSimpleName() + "'");
			}
		} catch (final ClassNotFoundException e) {
			AbstractController.LOG.error(c.getSimpleName() + ": Class '" + classname + "' not found", e);
		} catch (final NoSuchMethodException e) {
			AbstractController.LOG.error(c.getSimpleName() + ": Class '" + classname + "' has to implement a (public) constructor that accepts a single Configuration", e);
		} catch (final Exception e) { // SecurityException, IllegalAccessException, IllegalArgumentException, InstantiationException, InvocationTargetException
			AbstractController.LOG.error(c.getSimpleName() + ": Failed to load class for name '" + classname + "'", e);
		}
		return createdClass;
	}
}
