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

package kieker.analysis.plugin;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

import kieker.analysis.plugin.port.AInputPort;
import kieker.analysis.plugin.port.AOutputPort;
import kieker.analysis.plugin.port.APlugin;
import kieker.analysis.reader.IMonitoringReader;
import kieker.common.configuration.Configuration;
import kieker.common.logging.Log;
import kieker.common.logging.LogFactory;

/**
 * <b>Do not</b> inherit directly from this class! Instead inherit from the
 * class {@link AbstractAnalysisPlugin} or {@link AbstractMonitoringReader}.
 * 
 * @author Nils Christian Ehmke
 */
@APlugin(outputPorts = {})
public abstract class AbstractPlugin {

	private static final Log LOG = LogFactory.getLog(AbstractPlugin.class);

	private String name = null;

	protected final Configuration configuration;
	private final ConcurrentHashMap<String, ConcurrentLinkedQueue<Method>> registeredMethods;
	private final AOutputPort outputPorts[];
	private final AInputPort inputPorts[];

	/**
	 * Each Plugin requires a constructor with a single Configuration object!
	 */
	public AbstractPlugin(final Configuration configuration) {
		try {
			// somewhat dirty hack...
			final Configuration defaultConfig = this.getDefaultConfiguration(); // NOPMD
			if (defaultConfig != null) {
				configuration.setDefaultConfiguration(defaultConfig);
			}
		} catch (final IllegalAccessException ex) {
			AbstractPlugin.LOG.error("Unable to set plugin default properties");
		}
		this.configuration = configuration;

		this.registeredMethods = new ConcurrentHashMap<String, ConcurrentLinkedQueue<Method>>();
		/*
		 * Create a linked queue for every outputport of the class. KEEP IN MIND: although we use "this",
		 * it points to the actual class. Not to AbstractPlugin!!
		 */
		final APlugin annotation = this.getClass().getAnnotation(APlugin.class);
		this.outputPorts = annotation.outputPorts();
		for (final AOutputPort outputPort : this.outputPorts) {
			this.registeredMethods.put(outputPort.name(), new ConcurrentLinkedQueue<Method>());
		}
		final Method allMethods[] = this.getClass().getMethods();
		final List<AInputPort> inputPorts = new ArrayList<AInputPort>();
		for (final Method method : allMethods) {
			final AInputPort inputPort = method.getAnnotation(AInputPort.class);
			if (inputPort != null) {
				inputPorts.add(inputPort);
			}
		}
		this.inputPorts = inputPorts.toArray(new AInputPort[0]);
	}

	/**
	 * This method should deliver an instance of {@code Properties} containing
	 * the default properties for this class. In other words: Every class
	 * inheriting from {@code AbstractPlugin} should implement this method to
	 * deliver an object which can be used for the constructor of this clas.
	 * 
	 * @return The default properties.
	 */
	protected abstract Configuration getDefaultConfiguration();

	/**
	 * This method should deliver a {@code Configuration} object containing the
	 * current configuration of this instance. In other words: The constructor
	 * should be able to use the given object to initialize a new instance of
	 * this class with the same intern properties.
	 * 
	 * @return A complete filled configuration object.
	 */
	public abstract Configuration getCurrentConfiguration();

	/**
	 * This method delivers the current name of this plugin. The name does not
	 * have to be unique.
	 * 
	 * @return The name of the plugin.
	 */
	public final String getName() {
		return this.name;
	}

	/**
	 * This method sets the current name of this plugin. The name does not
	 * have to be unique.
	 * 
	 * @param name
	 *            The new name of the plugin.
	 */
	public final void setName(final String name) {
		this.name = name;
	}

	protected final boolean deliver(final String outputPort, final Object data) {
		// TODO Make sure the given data fits
		final ConcurrentLinkedQueue<Method> registeredMethods = this.registeredMethods.get(outputPort);
		if (registeredMethods == null) {
			return false;
		}
		boolean result = true;
		final Iterator<Method> methodIterator = registeredMethods.iterator();
		while (methodIterator.hasNext()) {
			try {
				methodIterator.next().invoke(this, data);
			} catch (final Exception e) {
				result = false;
			}
		}
		return result;
	}

	public static final void connect(final AbstractPlugin src, final String output, final AbstractPlugin dst, final String input) {
		if (dst instanceof IMonitoringReader) {
			// TODO Throw exception
		}
		// TODO Check whether the ports are suitable or not

		// TODO Check whether the ports exist or not

		try {
			src.registeredMethods.get(output).add(dst.getClass().getMethod(input, Object.class));
		} catch (final Exception e) {
			// TODO Throw exception
		}
	}
}
