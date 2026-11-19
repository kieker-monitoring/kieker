/***************************************************************************
 * Copyright 2022 Kieker Project (http://kieker-monitoring.net)
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
package kieker.monitoring.probe.bytebuddy.state;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.lang.management.ManagementFactory;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Naive capturing of state data (this, arguments, return value / thrown exception) before and after method
 * executions, for measuring the size and overhead of state-aware tracing.
 *
 * <p>
 * Configuration via environment variables:
 * <ul>
 * <li>{@value #ENV_DEPTH}: unset or <code>none</code> disables state capturing; a number &gt;= 0 sets the maximum
 * expansion depth (see {@link StateSerializer}); <code>full</code> captures the complete reachable object graph.</li>
 * <li>{@value #ENV_OUTPUT}: output directory (default: java.io.tmpdir). Per JVM, a directory
 * <code>kieker-state-&lt;pid&gt;</code> containing one binary file per thread and a summary file is created.</li>
 * </ul>
 * </p>
 *
 * <p>
 * Entry format (big endian): long traceId, int eoi, byte phase (0 = before, 1 = after, 2 = after exception),
 * int length, length bytes of serialized state.
 * </p>
 *
 * @author David Georg Reichelt
 */
public final class StateCapture { // NOPMD (no utility class naming; used from advice code)

	public static final String ENV_DEPTH = "KIEKER_STATE_DEPTH";
	public static final String ENV_OUTPUT = "KIEKER_STATE_OUTPUT";

	public static final byte PHASE_BEFORE = 0;
	public static final byte PHASE_AFTER = 1;
	public static final byte PHASE_AFTER_EXCEPTION = 2;

	private static final int ENTRY_HEADER_SIZE = 8 + 4 + 1 + 4;
	private static final int OUTPUT_BUFFER_SIZE = 1 << 20;

	/** -1: disabled. */
	private static final int MAX_DEPTH = parseDepth(System.getenv(ENV_DEPTH));
	private static final boolean ACTIVE = MAX_DEPTH >= 0;

	private static final ThreadLocal<ThreadState> THREAD_STATE = new ThreadLocal<>();
	private static final Queue<ThreadState> ALL_THREAD_STATES = new ConcurrentLinkedQueue<>();

	private static final AtomicLong ENTRIES = new AtomicLong();
	private static final AtomicLong BYTES = new AtomicLong();
	private static final AtomicLong MAX_ENTRY_BYTES = new AtomicLong();
	private static final AtomicLong NANOS = new AtomicLong();
	private static final AtomicLong FAILURES = new AtomicLong();

	private static final File OUTPUT_DIR;

	static {
		if (ACTIVE) {
			final String configuredDir = System.getenv(ENV_OUTPUT);
			final String baseDir;
			if (configuredDir != null) {
				baseDir = configuredDir;
			} else {
				baseDir = System.getProperty("java.io.tmpdir");
			}
			OUTPUT_DIR = new File(baseDir, "kieker-state-" + pid());
			OUTPUT_DIR.mkdirs();
			Runtime.getRuntime().addShutdownHook(new Thread(StateCapture::shutdown, "kieker-state-shutdown"));
		} else {
			OUTPUT_DIR = null;
		}
	}

	private StateCapture() {}

	/**
	 * @return whether state capturing is configured
	 */
	public static boolean isActive() {
		return ACTIVE;
	}

	/**
	 * @return true if the current thread is currently serializing state; method executions triggered during
	 *         serialization must not be monitored
	 */
	public static boolean isCapturing() {
		if (!ACTIVE) {
			return false;
		}
		final ThreadState state = THREAD_STATE.get();
		return (state != null) && state.capturing;
	}

	public static void before(final long traceId, final int eoi, final Object self, final Object[] args) {
		if (ACTIVE) {
			capture(traceId, eoi, PHASE_BEFORE, new Object[] { self, args });
		}
	}

	public static void after(final long traceId, final int eoi, final Object self, final Object[] args,
			final Object returnValue, final Throwable thrown) {
		if (ACTIVE) {
			if (thrown != null) {
				capture(traceId, eoi, PHASE_AFTER_EXCEPTION, new Object[] { self, args, thrown });
			} else {
				capture(traceId, eoi, PHASE_AFTER, new Object[] { self, args, returnValue });
			}
		}
	}

	private static void capture(final long traceId, final int eoi, final byte phase, final Object[] roots) {
		final ThreadState state = getThreadState();
		if (state == null || state.capturing) {
			return;
		}
		synchronized (state) { // uncontended except during shutdown
			if (state.closed) {
				return;
			}
			captureLocked(state, traceId, eoi, phase, roots);
		}
	}

	private static void captureLocked(final ThreadState state, final long traceId, final int eoi, final byte phase,
			final Object[] roots) {
		state.capturing = true;
		final long start = System.nanoTime();
		try {
			final StateSerializer.GrowableBuffer buffer = state.serializer.serialize(roots);
			final int length = buffer.size();
			state.writeHeader(traceId, eoi, phase, length);
			state.out.write(buffer.array(), 0, length);
			ENTRIES.incrementAndGet();
			BYTES.addAndGet(ENTRY_HEADER_SIZE + length);
			MAX_ENTRY_BYTES.accumulateAndGet(length, Math::max);
		} catch (final IOException | RuntimeException | StackOverflowError e) { // NOPMD NOCS (measurement must not break the application)
			FAILURES.incrementAndGet();
		} finally {
			NANOS.addAndGet(System.nanoTime() - start);
			state.capturing = false;
		}
	}

	private static ThreadState getThreadState() {
		ThreadState state = THREAD_STATE.get();
		if (state == null) {
			try {
				state = new ThreadState(new File(OUTPUT_DIR, "state-" + Thread.currentThread().getId() + ".bin"));
				THREAD_STATE.set(state);
				ALL_THREAD_STATES.add(state);
			} catch (final IOException e) {
				FAILURES.incrementAndGet();
				return null;
			}
		}
		return state;
	}

	private static void shutdown() {
		long expandedObjects = 0;
		for (final ThreadState state : ALL_THREAD_STATES) {
			synchronized (state) {
				state.closed = true;
				expandedObjects += state.serializer.getExpandedObjects();
				try {
					state.out.close();
				} catch (final IOException e) { // NOPMD
					FAILURES.incrementAndGet();
				}
			}
		}
		final String depth;
		if (MAX_DEPTH == Integer.MAX_VALUE) {
			depth = "full";
		} else {
			depth = String.valueOf(MAX_DEPTH);
		}
		final String summary = "depth=" + depth
				+ " entries=" + ENTRIES.get()
				+ " bytes=" + BYTES.get()
				+ " maxEntryBytes=" + MAX_ENTRY_BYTES.get()
				+ " expandedObjects=" + expandedObjects
				+ " serializationNanos=" + NANOS.get()
				+ " failures=" + FAILURES.get();
		try (PrintStream ps = new PrintStream(Files.newOutputStream(new File(OUTPUT_DIR, "summary.txt").toPath()), true,
				StandardCharsets.UTF_8.name())) {
			ps.println(summary);
		} catch (final IOException e) { // NOPMD
			// fall through to stderr
		}
		System.err.println("[kieker-state] " + summary); // NOPMD (logging may already be shut down)
	}

	private static int parseDepth(final String value) {
		if (value == null || value.isEmpty() || "none".equalsIgnoreCase(value)) {
			return -1;
		}
		if ("full".equalsIgnoreCase(value)) {
			return Integer.MAX_VALUE;
		}
		try {
			final int depth = Integer.parseInt(value.trim());
			if (depth >= 0) {
				return depth;
			}
			return -1;
		} catch (final NumberFormatException e) {
			System.err.println("[kieker-state] Invalid " + ENV_DEPTH + "=" + value + "; state capturing disabled"); // NOPMD (called during premain)
			return -1;
		}
	}

	/**
	 * Per-thread state: serializer, output stream and reentrancy flag.
	 */
	private static final class ThreadState {
		private final StateSerializer serializer = new StateSerializer(MAX_DEPTH);
		private final OutputStream out;
		private final byte[] header = new byte[ENTRY_HEADER_SIZE];
		private boolean capturing;
		private boolean closed;

		private ThreadState(final File file) throws IOException {
			this.out = new BufferedOutputStream(Files.newOutputStream(file.toPath()), OUTPUT_BUFFER_SIZE);
		}

		private void writeHeader(final long traceId, final int eoi, final byte phase, final int length) throws IOException {
			int i = 0;
			for (int shift = 56; shift >= 0; shift -= 8) {
				this.header[i++] = (byte) (traceId >>> shift);
			}
			for (int shift = 24; shift >= 0; shift -= 8) {
				this.header[i++] = (byte) (eoi >>> shift);
			}
			this.header[i++] = phase;
			for (int shift = 24; shift >= 0; shift -= 8) {
				this.header[i++] = (byte) (length >>> shift);
			}
			this.out.write(this.header);
		}
	}

	/**
	 * Determines the process id in a Java-8-compatible way.
	 */
	private static String pid() {
		final String name = ManagementFactory.getRuntimeMXBean().getName(); // "pid@hostname"
		final int at = name.indexOf('@');
		if (at > 0) {
			return name.substring(0, at);
		}
		return name;
	}
}
