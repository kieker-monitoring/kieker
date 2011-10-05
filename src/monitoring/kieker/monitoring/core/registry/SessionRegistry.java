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
	public final void storeThreadLocalSessionId(final String sessionId) {
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

	private SessionRegistry() {}
}
