package kieker.monitoring.core.registry;

/**
 * @author Andre van Hoorn
 */
public final class SessionRegistry {

	private final ThreadLocal<String> threadLocalSessionId = new ThreadLocal<String>();

	/**
	 * @return the singleton instance of SessionRegistry
	 */
	public final static SessionRegistry getInstance() {
		return LazyHolder.INSTANCE;
	}

	/**
	 * Used by the spring aspect to explicitly register a sessionid that is to be collected within
	 * a servlet method (that knows the request object).
	 * The thread is responsible for invalidating the stored curTraceId using
	 * the method unsetThreadLocalSessionId()!
	 */
	public final void storeThreadLocalSessionId(String sessionId) {
		this.threadLocalSessionId.set(sessionId);
	}

	/**
	 * This method returns the thread-local traceid previously
	 * registered using the method registerTraceId(curTraceId).
	 * 
	 * @return the sessionid. null if no session registered.
	 */
	public final String recallThreadLocalSessionId() {
		return this.threadLocalSessionId.get();
	}

	/**
	 * This method unsets a previously registered sessionid.
	 */
	public final void unsetThreadLocalSessionId() {
		this.threadLocalSessionId.remove();
	}

	/**
	 * SINGLETON
	 */
	private final static class LazyHolder {
		private static final SessionRegistry INSTANCE = new SessionRegistry();
	}
	private SessionRegistry() {
	}
}
