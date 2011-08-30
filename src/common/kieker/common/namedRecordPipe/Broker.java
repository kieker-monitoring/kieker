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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 
 * @author Andre van Hoorn
 */
public class Broker {
	// TODO: change to lazy construction
	private static final Broker INSTANCE = new Broker();
	private static final Log log = LogFactory.getLog(Broker.class);

	/**
	 * Access synchronized through synchronized method
	 * {@link #acquirePipe(String)} !
	 */
	private final HashMap<String, Pipe> pipeMap = new HashMap<String, Pipe>();

	public static Broker getInstance() {
		return Broker.INSTANCE;
	}

	/**
	 * Returns a connection with name @a pipeName. If a connection with this
	 * name does not exist prior to the call, it is created.
	 */
	public synchronized Pipe acquirePipe(final String pipeName)
			throws IllegalArgumentException {
		if ((pipeName == null) || (pipeName.isEmpty())) {
			final String errorMsg = "pipeName must not be null or empty!  (Found: "
					+ pipeName + ")";
			Broker.log.error(errorMsg);
			throw new IllegalArgumentException(errorMsg);
		}
		Pipe conn = this.pipeMap.get(pipeName);
		if (conn == null) {
			conn = new Pipe(pipeName);
			this.pipeMap.put(pipeName, conn);
		}

		return conn;
	}
}
