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

package kieker.common.namedRecordPipe;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 
 * @author Andre van Hoorn
 */
public final class Broker {
	private static final Log LOG = LogFactory.getLog(Broker.class);

	/**
	 * Access synchronized through synchronized method {@link #acquirePipe(String)} !
	 */
	private final Map<String, Pipe> pipeMap = new HashMap<String, Pipe>();

	/**
	 * Used for constructing the singleton instance {@link #getInstance()}.
	 */
	private Broker() {}

	public static Broker getInstance() {
		return LazyHolder.INSTANCE;
	}

	/**
	 * Returns a connection with name @a pipeName. If a connection with this
	 * name does not exist prior to the call, it is created.
	 */
	public Pipe acquirePipe(final String pipeName) throws IllegalArgumentException {
		Pipe conn;
		synchronized (this) {
			if ((pipeName == null) || (pipeName.isEmpty())) {
				final String errorMsg = "pipeName must not be null or empty!  (Found: " + pipeName + ")";
				Broker.LOG.error(errorMsg);
				throw new IllegalArgumentException(errorMsg);
			}
			conn = this.pipeMap.get(pipeName);
			if (conn == null) {
				conn = new Pipe(pipeName);
				this.pipeMap.put(pipeName, conn);
			}
		}
		return conn;
	}

	/**
	 * SINGLETON
	 */
	private static final class LazyHolder { // NOCS (MissingCtorCheck)
		static {
			INSTANCE = new Broker();
		}
		private static final Broker INSTANCE;
	}
}
