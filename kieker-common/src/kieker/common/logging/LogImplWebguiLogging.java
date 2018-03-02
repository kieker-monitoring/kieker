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

package kieker.common.logging;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Queue;
import java.util.TimeZone;
import java.util.concurrent.ArrayBlockingQueue;

import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;

/**
 * This is a simple logger for the Kieker WebGUI. It stores the log messages within a buffer of a static size. If the buffer is full, the oldest entry will be
 * removed for new entries. As the entries have to be accessible from outside, the queues are stored statically.
 *
 * @author Jan Waller, Nils Christian Ehmke
 *
 * @since 1.6
 */
public final class LogImplWebguiLogging extends AbstractLogImpl {

	private static final int MAX_ENTRIES = 100;
	private static final Map<String, Queue<String>> QUEUES = new HashMap<String, Queue<String>>(); // NOPMD (sync needed)

	private final DateFormat date;
	private final String name;

	/**
	 * Creates a new instance of this class.
	 *
	 * @param name
	 *            The name of the logger.
	 */
	protected LogImplWebguiLogging(final String name) {
		this.name = name;
		this.date = new SimpleDateFormat("yyyyMMdd'-'HHmmssSSS", Locale.US);
		this.date.setTimeZone(TimeZone.getTimeZone("UTC"));
	}

	@Override
	public boolean isTraceEnabled() {
		return false;
	}

	@Override
	public void trace(final String message) {} // NOPMD (does nothing)

	@Override
	public void trace(final String message, final Throwable t) {} // NOPMD (does nothing)

	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean isDebugEnabled() {
		return false;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void debug(final String message) {} // NOPMD (does nothing)

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void debug(final String message, final Throwable t) {} // NOPMD (does nothing)

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void info(final String message) {
		this.addMessage(message, "[Info]", null);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void info(final String message, final Throwable t) {
		this.addMessage(message, "[Info]", t);
	}

	@Override
	public boolean isWarnEnabled() {
		return true;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void warn(final String message) {
		this.addMessage(message, "[Warn]", null);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void warn(final String message, final Throwable t) {
		this.addMessage(message, "[Warn]", t);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void error(final String message) {
		this.addMessage(message, "[Crit]", null);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void error(final String message, final Throwable t) {
		this.addMessage(message, "[Crit]", t);
	}

	private void addMessage(final String message, final String severity, final Throwable throwable) {
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
				this.removeOldestEntryFrom(queue);
			}
			final StringBuilder sb = new StringBuilder(255)
					.append(this.date.format(new java.util.Date())) // this has to be within synchronized
					.append(": ")
					.append(severity)
					.append(' ')
					.append(message);
			if (null != throwable) {
				sb.append('\n');
				sb.append(throwable.toString());
			}
			queue.add(sb.toString());
		}
	}

	// extracted into a separate method to mark it with Findbugs' @SuppressFBWarnings
	@SuppressFBWarnings("RV_RETURN_VALUE_IGNORED")
	private void removeOldestEntryFrom(final Queue<String> queue) {
		queue.poll();
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
	 * Delivers an array with the names of all used loggers.
	 *
	 * @return The available log names.
	 */
	public static String[] getAvailableLogs() {
		synchronized (LogImplWebguiLogging.QUEUES) {
			return QUEUES.keySet().toArray(new String[QUEUES.size()]);
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
