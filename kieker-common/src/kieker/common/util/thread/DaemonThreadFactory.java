/***************************************************************************
 * Copyright 2023 Kieker Project (http://kieker-monitoring.net)
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
package kieker.common.util.thread;

import java.util.concurrent.ThreadFactory;

/**
 * A thread factory that creates daemon threads. The default thread parameters are based on
 * the default thread factory.
 *
 * @author Holger Knoche
 *
 * @since 1.13
 */
public class DaemonThreadFactory implements ThreadFactory {

	/**
	 * Creates a new daemon thread factory.
	 */
	public DaemonThreadFactory() {
		// Do nothing
	}

	@Override
	public Thread newThread(final Runnable runnable) {
		final Thread thread = new Thread(runnable);

		thread.setDaemon(true);
		thread.setPriority(Thread.NORM_PRIORITY);

		return thread;
	}

}
