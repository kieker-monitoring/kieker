/***************************************************************************
 * Copyright 2013 Kieker Project (http://kieker-monitoring.net)
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

package kieker.analysis.plugin.filter.forward;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import kieker.analysis.IProjectContext;
import kieker.analysis.plugin.annotation.InputPort;
import kieker.analysis.plugin.annotation.OutputPort;
import kieker.analysis.plugin.annotation.Plugin;
import kieker.analysis.plugin.annotation.Property;
import kieker.analysis.plugin.filter.AbstractFilterPlugin;
import kieker.common.configuration.Configuration;
import kieker.common.logging.Log;
import kieker.common.logging.LogFactory;

/**
 * This filter collects the incoming objects in a simple synchronized list. It is mostly used for test purposes.
 * 
 * @param <T>
 *            The type of the list.
 * 
 * @author Nils Ehmke, Jan Waller
 * 
 * @since 1.6
 */
@Plugin(programmaticOnly = true,
		description = "A filter collecting incoming objects in a list (mostly used in testing scenarios)",
		outputPorts = @OutputPort(name = ListCollectionFilter.OUTPUT_PORT_NAME, eventTypes = { Object.class }, description = "Provides each incoming object"),
		configuration = {
			@Property(name = ListCollectionFilter.CONFIG_PROPERTY_NAME_NUMBER_OF_ENTRIES,
					defaultValue = ListCollectionFilter.CONFIG_PROPERTY_VALUE_NUMBER_OF_ENTRIES,
					description = "Sets the number of stored values."),
			@Property(name = ListCollectionFilter.CONFIG_PROPERTY_NAME_LIST_FULL_BEHAVIOR,
					defaultValue = ListCollectionFilter.CONFIG_PROPERTY_VALUE_LIST_FULL_BEHAVIOR,
					description = "Determines what happens to new objects when the list is full.") })
public class ListCollectionFilter<T> extends AbstractFilterPlugin {
	/** The name of the input port for the incoming objects. */
	public static final String INPUT_PORT_NAME = "inputObject";
	/** The name of the output port for the forwared objects. */
	public static final String OUTPUT_PORT_NAME = "outputObjects";

	public static final String CONFIG_PROPERTY_NAME_NUMBER_OF_ENTRIES = "numberOfEntries";
	public static final String CONFIG_PROPERTY_VALUE_NUMBER_OF_ENTRIES = "-1"; // unlimited per default

	public static final String CONFIG_PROPERTY_NAME_LIST_FULL_BEHAVIOR = "listFullBehavior";
	public static final String CONFIG_PROPERTY_VALUE_LIST_FULL_BEHAVIOR = "dropOldest";

	public static final String LIST_FULL_BEHAVIOR_DROP_OLDEST = "dropOldest";
	public static final String LIST_FULL_BEHAVIOR_IGNORE = "ignore";
	public static final String LIST_FULL_BEHAVIOR_ERROR = "error";

	private static final Log LOG = LogFactory.getLog(ListCollectionFilter.class);

	private final int numberOfEntries;
	private final String listFullBehavior;
	private final List<T> list;

	/**
	 * Creates a new instance of this class using the given parameters.
	 * 
	 * @param configuration
	 *            The configuration for this component.
	 * @param projectContext
	 *            The project context for this component.
	 */
	public ListCollectionFilter(final Configuration configuration, final IProjectContext projectContext) {
		super(configuration, projectContext);

		// Read the configuration
		this.numberOfEntries = configuration.getIntProperty(CONFIG_PROPERTY_NAME_NUMBER_OF_ENTRIES);
		this.listFullBehavior = configuration.getStringProperty(CONFIG_PROPERTY_NAME_LIST_FULL_BEHAVIOR);
		if (this.numberOfEntries == -1) {
			this.list = Collections.synchronizedList(new ArrayList<T>());
		} else {
			if (LIST_FULL_BEHAVIOR_DROP_OLDEST.equals(this.listFullBehavior)) {
				this.list = Collections.synchronizedList(new LinkedList<T>());
			} else {
				this.list = Collections.synchronizedList(new ArrayList<T>());
			}
		}
	}

	/**
	 * This method represents the input port.
	 * 
	 * @param data
	 *            The next element.
	 */
	@InputPort(name = ListCollectionFilter.INPUT_PORT_NAME)
	@SuppressWarnings("unchecked")
	public synchronized void input(final Object data) {
		if (this.numberOfEntries == -1) {
			this.list.add((T) data);
		} else {
			if (LIST_FULL_BEHAVIOR_DROP_OLDEST.equals(this.listFullBehavior)) {
				this.list.add((T) data);
				if (this.numberOfEntries < this.size()) {
					((LinkedList<T>) this.list).removeFirst();
				}
			} else if (LIST_FULL_BEHAVIOR_IGNORE.equals(this.listFullBehavior)) {
				if (this.numberOfEntries > this.size()) {
					this.list.add((T) data);
				}
			} else if (LIST_FULL_BEHAVIOR_ERROR.equals(this.listFullBehavior)) {
				if (this.numberOfEntries > this.size()) {
					this.list.add((T) data);
				} else {
					LOG.error("Too many records for ListCollectionFilter, it was initialized with capacity: " + this.numberOfEntries);
				}
			}
		}
		super.deliver(OUTPUT_PORT_NAME, data);
	}

	/**
	 * Clears the list.
	 */
	public void clear() {
		this.list.clear();
	}

	@SuppressWarnings("unchecked")
	public List<T> getList() {
		return new CopyOnWriteArrayList<T>((T[]) this.list.toArray());
	}

	/**
	 * @return The current number of collected objects.
	 */
	public int size() {
		return this.list.size();
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Configuration getCurrentConfiguration() {
		final Configuration configuration = new Configuration();
		configuration.setProperty(CONFIG_PROPERTY_NAME_NUMBER_OF_ENTRIES, String.valueOf(this.numberOfEntries));
		configuration.setProperty(CONFIG_PROPERTY_NAME_LIST_FULL_BEHAVIOR, this.listFullBehavior);
		return configuration;
	}

}
