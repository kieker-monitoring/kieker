/***************************************************************************
 * Copyright 2020 Kieker Project (http://kieker-monitoring.net)
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

/**
 * This filter collects the incoming objects in a simple synchronized list. It
 * is mostly used for test purposes.
 *
 * @param <T>
 *            The type of the list.
 *
 * @author Nils Christian Ehmke, Jan Waller, Bjoern Weissenfels
 *
 * @since 1.6
 * @deprecated 1.15 ported to teetime
 */
@Deprecated
@Plugin(programmaticOnly = true, description = "A filter collecting incoming objects in a list (mostly used in testing scenarios)",
		outputPorts = @OutputPort(name = ListCollectionFilter.OUTPUT_PORT_NAME,
				eventTypes = Object.class, description = "Provides each incoming object"),
		configuration = {
			@Property(name = ListCollectionFilter.CONFIG_PROPERTY_NAME_MAX_NUMBER_OF_ENTRIES,
					defaultValue = ListCollectionFilter.CONFIG_PROPERTY_VALUE_NUMBER_OF_ENTRIES,
					description = "Sets the maximum number of stored values."),
			@Property(name = ListCollectionFilter.CONFIG_PROPERTY_NAME_LIST_FULL_BEHAVIOR,
					defaultValue = ListCollectionFilter.CONFIG_PROPERTY_VALUE_LIST_FULL_BEHAVIOR,
					description = "Determines what happens to new objects when the list is full.") })
public class ListCollectionFilter<T> extends AbstractFilterPlugin {

	/** The name of the input port for the incoming objects. */
	public static final String INPUT_PORT_NAME = "inputObject";
	/** The name of the output port for the forwarded objects. */
	public static final String OUTPUT_PORT_NAME = "outputObjects";

	/**
	 * The name of the property determining the maximal number of allowed entries.
	 */
	public static final String CONFIG_PROPERTY_NAME_MAX_NUMBER_OF_ENTRIES = "maxNumberOfEntries";
	/** The default value for the maximal number of allowed entries (unlimited. */
	public static final String CONFIG_PROPERTY_VALUE_NUMBER_OF_ENTRIES = "-1"; // unlimited per default

	/** The name of the property determining the behavior of a full list. */
	public static final String CONFIG_PROPERTY_NAME_LIST_FULL_BEHAVIOR = "listFullBehavior";
	/** The default value for the behavior of a full list (drop oldest). */
	public static final String CONFIG_PROPERTY_VALUE_LIST_FULL_BEHAVIOR = "DROP_OLDEST"; // must really be a String here

	private final LinkedList<T> list; // NOCS NOPMD (we actually need LinkedLIst here, no good interface is provided)

	private final int maxNumberOfEntries;
	private final boolean unboundedList;
	private final ListFullBehavior listFullBehavior;

	/**
	 * An enum for all possible list full behaviors.
	 *
	 * @author Jan Waller
	 * @since 1.8
	 */
	public enum ListFullBehavior {
		/** Drops the oldest entry. */
		DROP_OLDEST,
		/** Ignores the given entry. */
		IGNORE,
		/** Throws a runtime exception. */
		ERROR;
	}

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
		this.maxNumberOfEntries = configuration.getIntProperty(CONFIG_PROPERTY_NAME_MAX_NUMBER_OF_ENTRIES);
		if (this.maxNumberOfEntries < 0) {
			this.unboundedList = true;
		} else {
			this.unboundedList = false;
		}
		final String strListFullBehavior = configuration.getStringProperty(CONFIG_PROPERTY_NAME_LIST_FULL_BEHAVIOR);
		ListFullBehavior tmpListFullBehavior;
		try {
			tmpListFullBehavior = ListFullBehavior.valueOf(strListFullBehavior);
		} catch (final IllegalArgumentException ex) {
			this.logger.warn("{} is no valid list full behavior! Using 'ignore' instead.", strListFullBehavior);
			tmpListFullBehavior = ListFullBehavior.IGNORE;
		}
		this.listFullBehavior = tmpListFullBehavior;

		this.list = new LinkedList<>();
	}

	/**
	 * This method represents the input port.
	 *
	 * @param data
	 *            The next element.
	 */
	@InputPort(name = ListCollectionFilter.INPUT_PORT_NAME)
	public void input(final T data) {
		if (this.unboundedList) {
			synchronized (this.list) {
				this.list.add(data);
			}
		} else {
			switch (this.listFullBehavior) {
			case DROP_OLDEST:
				synchronized (this.list) {
					this.list.add(data);
					if (this.list.size() > this.maxNumberOfEntries) {
						this.list.removeFirst();
					}
				}
				break;
			case IGNORE:
				synchronized (this.list) {
					if (this.maxNumberOfEntries > this.list.size()) {
						this.list.add(data);
					}
				}
				break;
			case ERROR:
				synchronized (this.list) {
					if (this.maxNumberOfEntries > this.list.size()) {
						this.list.add(data);
					} else {
						throw new RuntimeException(// NOPMD
								"Too many records for ListCollectionFilter, it was initialized with capacity: " // NOPMD
										+ this.maxNumberOfEntries); // NOPMD
					}
				}
				break;
			default:
				// should not happen
				break;
			}
		}
		super.deliver(OUTPUT_PORT_NAME, data);
	}

	/**
	 * Clears the list.
	 */
	public void clear() {
		synchronized (this.list) {
			this.list.clear();
		}
	}

	/**
	 * Delivers a copy of the internal list.
	 *
	 * @return The content of the internal list.
	 */
	public List<T> getList() {
		synchronized (this.list) {
			return new CopyOnWriteArrayList<>(this.list);
		}
	}

	/**
	 * @return The current number of collected objects.
	 */
	public int size() {
		synchronized (this.list) {
			return this.list.size();
		}
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Configuration getCurrentConfiguration() {
		final Configuration configuration = new Configuration();

		configuration.setProperty(CONFIG_PROPERTY_NAME_MAX_NUMBER_OF_ENTRIES, String.valueOf(this.maxNumberOfEntries));
		configuration.setProperty(CONFIG_PROPERTY_NAME_LIST_FULL_BEHAVIOR, this.listFullBehavior.name());

		return configuration;
	}
}
