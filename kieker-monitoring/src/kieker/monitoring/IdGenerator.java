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
package kieker.monitoring;

import java.security.SecureRandom;
import java.util.concurrent.atomic.AtomicInteger;

import kieker.monitoring.core.controller.MonitoringController;

/**
 * Generates unique identifiers in a thread-safe way.
 *
 * @author Christian Wulf
 *
 * @since 1.13
 *
 */
public class IdGenerator {

	private final AtomicInteger nextIdentifier = new AtomicInteger(0);

	/**
	 * Represents a unique id prefix to distinguish identifiers used by other
	 * JVMs/hosts.
	 */
	private final long uniqueIdentifierPrefix;

	/**
	 * Creates a new generator for unique identifiers.
	 */
	public IdGenerator() {
		if (MonitoringController.getInstance().isDebug()) {
			this.uniqueIdentifierPrefix = 0;
		} else {
			this.uniqueIdentifierPrefix = ((long) new SecureRandom().nextInt()) << 32; // lower 32-bit are zeros
		}
	}

	/**
	 * Generate an unique id.
	 * (<i>thread-safe</i>)
	 *
	 * @return a unique identifier
	 */
	public long getNewId() {
		return this.uniqueIdentifierPrefix | this.nextIdentifier.getAndIncrement();
	}
}
