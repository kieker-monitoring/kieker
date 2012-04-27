/***************************************************************************
 * Copyright 2012 by
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

package kieker.common.logging;

import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;

/**
 * This is a simple logger for the Kieker WebGUI. It stores the log messages within a buffer of a static size. If the buffer is full, the oldest entry will be
 * removed for new entries. As the entries have to be accessible from outside, the queues are stored statically.
 * 
 * @author Jan Waller, Nils Christian Ehmke
 */
public final class LogImplWebguiLogging implements Log {

	private final String name;
	private static final int MAX_ENTRIES = 100;
	private static final Map<String, Queue<String>> QUEUES = new HashMap<String, Queue<String>>();

	/**
	 * Creates a new instance of this class.
	 * 
	 * @param name
	 *            The name of the logger.
	 */
	protected LogImplWebguiLogging(final String name) {
		this.name = name;
	}

	public boolean isDebugEnabled() {
		return false;
	}

	public void debug(final String message) {
		// Ignore
	}

	public void debug(final String message, final Throwable t) {
		// Ignore
	}

	public void info(final String message) {
		this.addMessage("[Info] " + message);
	}

	public void info(final String message, final Throwable t) {
		this.addMessage("[Info] " + message);
	}

	public void warn(final String message) {
		this.addMessage("[Warn] " + message);
	}

	public void warn(final String message, final Throwable t) {
		this.addMessage("[Warn] " + message);
	}

	public void error(final String message) {
		this.addMessage("[Crit] " + message);
	}

	public void error(final String message, final Throwable t) {
		this.addMessage("[Crit] " + message);
	}

	private void addMessage(final String message) {
		Queue<String> queue;

		synchronized (LogImplWebguiLogging.QUEUES) {
			// Get the right queue and create it if necessary.
			queue = LogImplWebguiLogging.QUEUES.get(this.name);
			if (queue == null) {
				queue = new ArrayBlockingQueue<String>(MAX_ENTRIES);
				LogImplWebguiLogging.QUEUES.put(this.name, queue);
			}
		}

		synchronized (queue) {
			// Is the queue full?
			if (queue.size() >= MAX_ENTRIES) {
				// Yes, remove the oldest entry.
				queue.poll(); // ignore the return value
			}
			final String timestamp = new GregorianCalendar().getTime().toString();
			queue.add(timestamp + ": " + message);
		}
	}

	/**
	 * Clears the saved entries within this logger.
	 */
	public void clear() {
		synchronized (LogImplWebguiLogging.QUEUES) {
			LogImplWebguiLogging.QUEUES.clear();
		}
	}

	/**
	 * Delivers an array with all entries of the logger with the given name.
	 * 
	 * @param name
	 *            The name of the logger.
	 * @return The stored entries of the logger. If a logger with the given name doesn't exist, an empty array will be returned.
	 */
	public static String[] getEntries(final String name) {
		final Queue<String> queue;

		synchronized (LogImplWebguiLogging.QUEUES) {
			// Get the right queue
			queue = LogImplWebguiLogging.QUEUES.get(name);
			if (queue == null) {
				return new String[0];
			}
		}

		synchronized (queue) {
			return queue.toArray(new String[queue.size()]);
		}
	}
}
