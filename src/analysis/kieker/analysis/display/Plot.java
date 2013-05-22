/***************************************************************************
 * Copyright 2013 Kieker Project (http://kieker-monitoring.net)
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

package kieker.analysis.display;

import java.util.Collection;
import java.util.Collections;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * This class is currently under development, mostly for test purposes, and not designed for productive deployment.
 * 
 * @author Nils Christian Ehmke
 * 
 * @since 1.8
 */
public class Plot extends AbstractDisplay {

	private final Queue<Long> entries = new ConcurrentLinkedQueue<Long>();

	public Plot() {
		// No code necessary
	}

	public Collection<Long> getEntries() {
		return Collections.unmodifiableCollection(this.entries);
	}

	public void addEntry(final Long entry) {
		synchronized (this) {
			this.entries.add(entry); // NOFB (ignore the return value)
			if (this.entries.size() > 50) {
				this.entries.poll(); // NOFB (ignore the return value)
			}
		}
	}

}
