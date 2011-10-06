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

package kieker.tools.traceAnalysis.systemModel;

import java.util.Iterator;
import java.util.Vector;

/**
 * @author Andre van Hoorn
 */
public class MessageTrace extends Trace {

	private final Vector<Message> set;

	public MessageTrace(final long traceId, final Vector<Message> seq) {
		super(traceId);
		this.set = seq;
	}

	public final Vector<Message> getSequenceAsVector() {
		return this.set;
	}

	@Override
	public String toString() {
		final StringBuilder strBuild = new StringBuilder("Trace " + this.getTraceId() + ":\n");
		final Iterator<Message> it = this.set.iterator();
		while (it.hasNext()) {
			final Message m = it.next();
			strBuild.append("<");
			strBuild.append(m.toString());
			strBuild.append(">\n");
		}
		return strBuild.toString();
	}

	// Explicit delegation to super method to make FindBugs happy
	@Override
	public int hashCode() {
		return super.hashCode();
	}

	@Override
	public boolean equals(final Object obj) {
		if (!(obj instanceof MessageTrace)) {
			return false;
		}
		if (this == obj) {
			return true;
		}
		final MessageTrace other = (MessageTrace) obj;

		return this.set.equals(other.set);
	}
}
