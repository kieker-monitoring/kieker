package kieker.monitoring.core.registry;

import java.security.SecureRandom;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

import kieker.monitoring.core.controller.MonitoringController;

public class ThreadRegistry {

	public static final ThreadRegistry INSTANCE = new ThreadRegistry();

	private final AtomicInteger nextThreadId = new AtomicInteger(0);
	/** Represents a thread id prefix to distinguish threads with the same thread id executed by different JVMs/hosts */
	private final long uniqueThreadIdPrefix;

	private final ConcurrentMap<Thread, Long> threads = new ConcurrentHashMap<>();

	private ThreadRegistry() {
		if (MonitoringController.getInstance().isDebug()) {
			this.uniqueThreadIdPrefix = 0;
		} else {
			this.uniqueThreadIdPrefix = ((long) new SecureRandom().nextInt()) << 32;
		}
	}

	public long getIdOfCurrentThread() {
		final Thread thread = Thread.currentThread();
		if (!this.threads.containsKey(thread)) {
			Long newThreadId = getNewId();
			this.threads.put(thread, newThreadId);
		}
		return this.threads.get(thread);
	}

	private long getNewId() {
		return this.uniqueThreadIdPrefix | this.nextThreadId.getAndIncrement();
	}
}
