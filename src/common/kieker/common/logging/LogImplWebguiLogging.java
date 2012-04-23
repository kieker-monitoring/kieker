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
	private static final HashMap<String, Queue<String>> queues = new HashMap<String, Queue<String>>();

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
		this.addMessage("[Error] " + message);
	}

	public void error(final String message, final Throwable t) {
		this.addMessage("[Error] " + message);
	}

	private void addMessage(final String message) {
		Queue<String> queue;

		synchronized (LogImplWebguiLogging.queues) {
			// Get the right queue and create it if necessary.
			queue = LogImplWebguiLogging.queues.get(this.name);
			if (queue == null) {
				queue = new ArrayBlockingQueue<String>(MAX_ENTRIES);
				LogImplWebguiLogging.queues.put(this.name, queue);
			}
		}

		synchronized (queue) {
			// Is the queue full?
			if (queue.size() >= MAX_ENTRIES) {
				// Yes, remove the oldest entry.
				queue.poll();
			}
			final String timestamp = new GregorianCalendar().getTime().toString();
			queue.add(timestamp + ": " + message);
		}
	}

	public void clear() {
		synchronized (LogImplWebguiLogging.queues) {
			LogImplWebguiLogging.queues.clear();
		}
	}

	public static String[] getEntries(final String name) {
		Queue<String> queue;

		synchronized (LogImplWebguiLogging.queues) {
			// Get the right queue
			queue = LogImplWebguiLogging.queues.get(name);
			if (queue == null) {
				return new String[0];
			}
		}

		synchronized (queue) {
			return queue.toArray(new String[queue.size()]);
		}
	}
}
