package kieker.monitoring.core.controller;

import java.util.concurrent.atomic.AtomicBoolean;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import kieker.monitoring.core.configuration.Configuration;

/**
 * @author Jan Waller
 */
public abstract class AbstractController {
	private static final Log log = LogFactory.getLog(AbstractController.class);

	private final AtomicBoolean terminated = new AtomicBoolean(false);
	protected volatile MonitoringController monitoringController = null;

	protected final synchronized void setMonitoringController(final MonitoringController monitoringController) {
		if (this.monitoringController == null) {
			this.monitoringController = monitoringController;
			init();
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
			if (monitoringController != null) {
				monitoringController.terminate();
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
	protected void init() {
		//default does nothing!
	}
	
	/**
	 * cleans up
	 */
	protected abstract void cleanup();

	@Override
	public abstract String toString();

	@SuppressWarnings("unchecked")
	protected final static <C> C createAndInitialize(final Class<C> c, final String classname, final Configuration configuration) {
		C createdClass = null;
		try {
			final Class<?> clazz = Class.forName(classname);
			if (c.isAssignableFrom(clazz)) {
				createdClass = (C) clazz.getConstructor(Configuration.class).newInstance(configuration.getPropertiesStartingWith(classname));
			} else {
				log.error("Class '" + classname + "' has to implement '" + c.getSimpleName() + "'");
			}
		} catch (final ClassNotFoundException e) {
			log.error(c.getSimpleName() + ": Class '" + classname + "' not found", e);
		} catch (final NoSuchMethodException e) {
			log.error(c.getSimpleName() + ": Class '" + classname + "' has to implement a constructor that accepts a single Configuration", e);
		} catch (final Throwable e) { // SecurityException, IllegalAccessException, IllegalArgumentException, InstantiationException, InvocationTargetException
			log.error(c.getSimpleName() + ": Failed to load class for name '" + classname + "'", e);
		}
		return createdClass;
	}
}
