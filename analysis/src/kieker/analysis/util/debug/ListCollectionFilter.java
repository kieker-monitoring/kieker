/***************************************************************************
 * Copyright 2023 Kieker Project (http://kieker-monitoring.net)
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

package kieker.analysis.util.debug;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import teetime.framework.AbstractConsumerStage;
import teetime.framework.OutputPort;

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
 */
public class ListCollectionFilter<T> extends AbstractConsumerStage<T> {

	private final OutputPort<T> outputPort = this.createOutputPort();

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
	 * @param maxNumberOfEntries
	 *            max number of entries to be stored in list (-1 for unlimited)
	 * @param listFullBehavior
	 *            behavior when list is full
	 */
	public ListCollectionFilter(final int maxNumberOfEntries, final ListFullBehavior listFullBehavior) {
		super();

		// Read the configuration
		this.maxNumberOfEntries = maxNumberOfEntries;
		if (this.maxNumberOfEntries < 0) {
			this.unboundedList = true;
		} else {
			this.unboundedList = false;
		}
		this.listFullBehavior = listFullBehavior;

		this.list = new LinkedList<>();
	}

	/**
	 * This method represents the input port.
	 *
	 * @param data
	 *            The next element.
	 */
	@Override
	protected void execute(final T data) throws Exception {
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
		this.outputPort.send(data);
	}

	public OutputPort<T> getOutputPort() {
		return this.outputPort;
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
}
