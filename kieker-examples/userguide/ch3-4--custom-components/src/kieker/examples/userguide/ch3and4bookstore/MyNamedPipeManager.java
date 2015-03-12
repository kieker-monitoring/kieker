/***************************************************************************
 * Copyright 2015 Kieker Project (http://kieker-monitoring.net)
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

package kieker.examples.userguide.ch3and4bookstore;

import java.util.HashMap;
import java.util.Map;

public class MyNamedPipeManager {

	private static final MyNamedPipeManager PIPE_MGR_INSTANCE = new MyNamedPipeManager();

	// Not synchronized!
	private final Map<String, MyPipe> pipeMap = new HashMap<String, MyPipe>();

	public static MyNamedPipeManager getInstance() {
		return MyNamedPipeManager.PIPE_MGR_INSTANCE;
	}

	public MyPipe acquirePipe(final String pipeName) throws IllegalArgumentException {
		if ((pipeName == null) || (pipeName.length() == 0)) {
			throw new IllegalArgumentException("Invalid connection name: '" + pipeName + "'");
		}
		MyPipe conn;
		synchronized (this) {
			conn = this.pipeMap.get(pipeName);
			if (conn == null) {
				conn = new MyPipe(pipeName);
				this.pipeMap.put(pipeName, conn);
			}
		}
		return conn;
	}
}
