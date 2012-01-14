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
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

import kieker.analysis.plugin.port.InputPort;
import kieker.analysis.plugin.port.OutputPort;
import kieker.analysis.plugin.port.Plugin;
import kieker.analysis.reader.IMonitoringReader;
import kieker.common.configuration.Configuration;
import kieker.common.logging.Log;
import kieker.common.logging.LogFactory;
import kieker.tools.traceAnalysis.systemModel.repository.AbstractRepository;

/**
 * <b>Do not</b> inherit directly from this class! Instead inherit from the class {@link AbstractAnalysisPlugin} or {@link AbstractMonitoringReader}.
 * 
 * @author Nils Christian Ehmke
 */
@Plugin(outputPorts = {})
public abstract class AbstractPlugin {

	private static final Log LOG = LogFactory.getLog(AbstractPlugin.class);

	private String name = null;

	protected final Configuration configuration;
	private final ConcurrentHashMap<String, ConcurrentLinkedQueue<Pair<AbstractPlugin, Method>>> registeredMethods;
	private final HashMap<String, OutputPort> outputPorts;
	private final HashMap<String, InputPort> inputPorts;

	/**
	 * Each Plugin requires a constructor with a single Configuration object and an array of repositories!
	 */
	public AbstractPlugin(final Configuration configuration, final AbstractRepository repositories[]) {
		try {
			// TODO: somewhat dirty hack...
			final Configuration defaultConfig = this.getDefaultConfiguration(); // NOPMD
			if (defaultConfig != null) {
				configuration.setDefaultConfiguration(defaultConfig);
			}
		} catch (final IllegalAccessException ex) {
			AbstractPlugin.LOG.error("Unable to set plugin default properties");
		}
		this.configuration = configuration;

		/* KEEP IN MIND: Although we use "this" in the following code, it points to the actual class. Not to AbstractPlugin!! */

		/* Get all output ports. */
		this.outputPorts = new HashMap<String, OutputPort>();
		final Plugin annotation = this.getClass().getAnnotation(Plugin.class);
		for (final OutputPort outputPort : annotation.outputPorts()) {
			this.outputPorts.put(outputPort.name(), outputPort);
		}
		/* Get all input ports. */
		this.inputPorts = new HashMap<String, InputPort>();
		final Method allMethods[] = this.getClass().getMethods();
		for (final Method method : allMethods) {
			final InputPort inputPort = method.getAnnotation(InputPort.class);
			if (inputPort != null) {
				this.inputPorts.put(method.getName(), inputPort);
			}
		}

		/* Now create a linked queue for every output port of the class, to store the registered methods. */
		this.registeredMethods = new ConcurrentHashMap<String, ConcurrentLinkedQueue<Pair<AbstractPlugin, Method>>>();
		for (final OutputPort outputPort : annotation.outputPorts()) {
			this.registeredMethods.put(outputPort.name(), new ConcurrentLinkedQueue<Pair<AbstractPlugin, Method>>());
		}
	}

	/**
	 * This method should deliver an instance of {@code Properties} containing the default properties for this class. In other words: Every class inheriting from
	 * {@code AbstractPlugin} should implement this method to deliver an object which can be used for the constructor of this class.
	 * 
	 * @return The default properties.
	 */
	protected abstract Configuration getDefaultConfiguration();

	/**
	 * This method should deliver an array of {@code AbstractRepository} containing the default repositories for this class. In other words: Every class inheriting
	 * from {@code AbstractPlugin} should implement this method to deliver an object which can be used for the constructor of this class.
	 * 
	 * @return The default repositories (which can be an empty array if necessary).
	 */
	protected abstract AbstractRepository[] getDefaultRepositories();

	/**
	 * This method should deliver a {@code Configuration} object containing the current configuration of this instance. In other words: The constructor should be
	 * able to use the given object to initialize a new instance of this class with the same intern properties.
	 * 
	 * @return A completely filled configuration object.
	 */
	public abstract Configuration getCurrentConfiguration();

	/**
	 * This method should deliver an array of {@code AbstractRepository} containing the current repositories of this instance. In other words: The constructor should
	 * be able to use the given object to initialize a new instance of this class with the same intern properties.
	 * 
	 * @return An (possible empty) array of repositories.
	 */
	public abstract AbstractRepository[] getCurrentRepositories();

	/**
	 * This method delivers the current name of this plugin. The name does not have to be unique.
	 * 
	 * @return The name of the plugin.
	 */
	public final String getName() {
		return this.name;
	}

	/**
	 * This method sets the current name of this plugin. The name does not have to be unique.
	 * 
	 * @param name
	 *            The new name of the plugin.
	 */
	public final void setName(final String name) {
		this.name = name;
	}

	/**
	 * Delivers the given data to all registered input ports of the given output port.
	 * 
	 * @param outputPortName
	 *            The output port to be used to send the given data.
	 * @param data
	 *            The data to be send.
	 * @return true if and only if the given output port does exist and if the data suits the port's event types.
	 */
	protected final boolean deliver(final String outputPortName, final Object data) {
		/* First step: Get the output port. */
		final OutputPort outputPort = this.outputPorts.get(outputPortName);
		if (outputPort == null) {
			return false;
		}

		/* Second step: Check whether the data fits the event types. */
		for (final Class<?> eventType : outputPort.eventTypes()) {
			if (!eventType.isInstance(data)) {
				return false;
			}
		}

		/* Third step: Send everything to the registered ports. */
		final ConcurrentLinkedQueue<Pair<AbstractPlugin, Method>> registeredMethods = this.registeredMethods.get(outputPortName);

		final Iterator<Pair<AbstractPlugin, Method>> methodIterator = registeredMethods.iterator();
		while (methodIterator.hasNext()) {
			final Pair<AbstractPlugin, Method> methodPair = methodIterator.next();
			try {
				methodPair.getSnd().invoke(methodPair.getFst(), data);
			} catch (final Exception e) {
				AbstractPlugin.LOG.warn(String.format("OutputPort %s couldn't send data to InputPort %s\n", outputPort.name(), methodPair.getSnd().getName()));
			}
		}

		return true;
	}

	/**
	 * This method checks whether two plugins can be connected.
	 * 
	 * @param src
	 *            The source plugin.
	 * @param output
	 *            The output port of the source plugin.
	 * @param dst
	 *            The destination plugin.
	 * @param input
	 *            The input port of the destination port.
	 * @return true if and only if both given plugins are valid, the output and input ports exist and if they are compatible. Furthermore the destination plugin must
	 *         not be a reader.
	 */
	public static final boolean isConnectionAllowed(final AbstractPlugin src, final String output, final AbstractPlugin dst, final String input) {
		/* First step: Check whether the plugins are valid. */
		if ((src == null) || (dst == null) || (dst instanceof IMonitoringReader)) {
			return false;
		}

		/* Second step: Check whether the ports exist. */
		final OutputPort outputPort = src.outputPorts.get(output);
		final InputPort inputPort = dst.inputPorts.get(input);
		if ((outputPort == null) || (inputPort == null)) {
			return false;
		}

		/* Third step: Make sure the ports are compatible. */
		if (inputPort.eventTypes().length != 0) {
			if (outputPort.eventTypes().length == 0) {
				// Output port can deliver everything
				if (!Arrays.asList(inputPort.eventTypes()).contains(Object.class)) {
					// But the input port cannot get everything.
					return false;
				}
			} else {
				for (final Class<?> srcEventType : outputPort.eventTypes()) {
					boolean compatible = false;
					for (final Class<?> dstEventType : inputPort.eventTypes()) {
						if (dstEventType.isAssignableFrom(srcEventType)) {
							compatible = true;
						}
					}
					if (!compatible) {
						return false;
					}
				}
			}
		}

		/* Seems like the connection is okay. */
		return true;
	}

	/**
	 * This method connects two plugins.
	 * 
	 * @param src
	 *            The source plugin.
	 * @param output
	 *            The output port of the source plugin.
	 * @param dst
	 *            The destination plugin.
	 * @param input
	 *            The input port of the destination port.
	 * @return true if and only if both given plugins are valid, the output and input ports exist and if they are compatible. Furthermore the destination plugin must
	 *         not be a reader.
	 */
	public static final boolean connect(final AbstractPlugin src, final String output, final AbstractPlugin dst, final String input) {
		if (!AbstractPlugin.isConnectionAllowed(src, output, dst, input)) {
			return false;
		}

		/* Connect the ports. */
		try {
			final Method method = dst.getClass().getMethod(input, Object.class);
			method.setAccessible(true);
			src.registeredMethods.get(output).add(new Pair<AbstractPlugin, Method>(dst, method));
		} catch (final Exception e) {
			AbstractPlugin.LOG.warn(String.format("Couldn't connect OutputPort %s with InputPort %s\n", output, input));
			return false;
		}

		return true;
	}

	public final String[] getAllOutputPortNames() {
		final List<String> outputNames = new ArrayList<String>();
		final Plugin annotation = this.getClass().getAnnotation(Plugin.class);
		for (final OutputPort outputPort : annotation.outputPorts()) {
			outputNames.add(outputPort.name());
		}
		return outputNames.toArray(new String[0]);
	}

	public final String[] getAllInputPortNames() {
		final List<String> inputNames = new ArrayList<String>();
		final Method allMethods[] = this.getClass().getMethods();
		for (final Method method : allMethods) {
			final InputPort inputPort = method.getAnnotation(InputPort.class);
			if (inputPort != null) {
				inputNames.add(method.getName());
			}
		}
		return inputNames.toArray(new String[0]);
	}

	/**
	 * Delivers the plugins with their ports which are connected with the given output port.
	 * 
	 * @param outputPortName
	 *            The name of the output port.
	 * @return An array of pairs, whereat the first element is the plugin and the second one the name of the input port. If the given output port is invalid, null is
	 *         returned
	 */
	public final List<Pair<AbstractPlugin, String>> getConnectedPlugins(final String outputPortName) {
		/* Make sure that the output port exists */
		final OutputPort outputPort = this.outputPorts.get(outputPortName);
		if (outputPort == null) {
			return null;
		}

		/* Now get the connections. */
		final ConcurrentLinkedQueue<Pair<AbstractPlugin, Method>> currRegisteredMethods = this.registeredMethods.get(outputPortName);
		final Iterator<Pair<AbstractPlugin, Method>> iterator = currRegisteredMethods.iterator();
		final List<Pair<AbstractPlugin, String>> result = new ArrayList<Pair<AbstractPlugin, String>>();
		while (iterator.hasNext()) {
			final Pair<AbstractPlugin, Method> currElem = iterator.next();
			result.add(new Pair<AbstractPlugin, String>(currElem.getFst(), currElem.getSnd().getName()));
		}

		return result;
	}
}
