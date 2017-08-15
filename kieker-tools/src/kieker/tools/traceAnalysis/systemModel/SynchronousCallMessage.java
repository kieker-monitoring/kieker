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

package kieker.tools.traceAnalysis.systemModel;

/**
 * A specified class of messages which represent synchronous calls.
 * 
 * @author Andre van Hoorn
 * 
 * @since 1.1
 */
public class SynchronousCallMessage extends AbstractMessage {

	/**
	 * Creates a new instance of this class using the given parameters.
	 * 
	 * @param timestamp
	 *            The timestamp of the message.
	 * @param sendingExecution
	 *            The {@link Execution} object which sent the message.
	 * @param receivingExecution
	 *            The {@link Execution} object which received the message.
	 */
	public SynchronousCallMessage(final long timestamp, final Execution sendingExecution, final Execution receivingExecution) {
		super(timestamp, sendingExecution, receivingExecution);
	}

	@Override
	public boolean equals(final Object obj) {
		if (!(obj instanceof SynchronousCallMessage)) {
			return false;
		}
		if (this == obj) {
			return true;
		}
		final SynchronousCallMessage other = (SynchronousCallMessage) obj;

		return (this.getTimestamp() == other.getTimestamp()) && this.getSendingExecution().equals(other.getSendingExecution())
				&& this.getReceivingExecution().equals(other.getReceivingExecution());
	}

	@Override
	public int hashCode() { // NOPMD (forward hashcode)
		return super.hashCode();
	}
}
