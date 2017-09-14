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

package kieker.tools.traceAnalysis.systemModel.repository;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * 
 * @author Andre van Hoorn
 * 
 * @since 1.1
 */
public abstract class AbstractSystemSubRepository { // NOPMD (abstract without abstract)

	/** This constant represents the ID of the root element. */
	public static final int ROOT_ELEMENT_ID = 0;

	private final AtomicInteger nextId = new AtomicInteger(ROOT_ELEMENT_ID + 1);

	private final SystemModelRepository systemFactory;

	/**
	 * Creates a new instance of this class using the given parameters.
	 * 
	 * @param systemFactory
	 *            The system factory.
	 */
	public AbstractSystemSubRepository(final SystemModelRepository systemFactory) {
		this.systemFactory = systemFactory;
	}

	/**
	 * This method delivers the next ID and increments the ID counter atomically.
	 * 
	 * @return The next ID.
	 */
	protected final int getAndIncrementNextId() {
		return this.nextId.getAndIncrement();
	}

	protected final SystemModelRepository getSystemFactory() {
		return this.systemFactory;
	}
}
