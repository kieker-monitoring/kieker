/***************************************************************************
 * Copyright 2020 Kieker Project (http://kieker-monitoring.net)
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

package kieker.tools.trace.analysis.systemModel;

import kieker.tools.trace.analysis.systemModel.repository.SystemModelRepository;

/**
 * An abstract base for messages which can later be used and combined in a {@link kieker.tools.trace.analysis.systemModel.MessageTrace}.
 *
 * @author Andre van Hoorn
 *
 * @since 0.95a
 */
public abstract class AbstractMessage {

	private final long timestamp;
	private final Execution sendingExecution;
	private final Execution receivingExecution;

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
	public AbstractMessage(final long timestamp, final Execution sendingExecution, final Execution receivingExecution) {
		this.timestamp = timestamp;
		this.sendingExecution = sendingExecution;
		this.receivingExecution = receivingExecution;
	}

	/**
	 * Delivers the object which received the message.
	 *
	 * @return The receiving object.
	 */
	public final Execution getReceivingExecution() {
		return this.receivingExecution;
	}

	/**
	 * Delivers the object which sent the message.
	 *
	 * @return The sending object.
	 */
	public final Execution getSendingExecution() {
		return this.sendingExecution;
	}

	/**
	 * Delivers the timestamp at which the message was created.
	 *
	 * @return The timestamp of the message.
	 */
	public final long getTimestamp() {
		return this.timestamp;
	}

	@Override
	public String toString() {
		final StringBuilder strBuild = new StringBuilder();

		strBuild.append(this.timestamp)
				.append(' ');
		if (this.getSendingExecution().getOperation().getId() == Operation.ROOT_OPERATION_ID) {
			strBuild.append(SystemModelRepository.ROOT_NODE_LABEL);
		} else {
			strBuild.append(this.getSendingExecution());
		}
		strBuild.append(" --> ");
		if (this.getReceivingExecution().getOperation().getId() == Operation.ROOT_OPERATION_ID) {
			strBuild.append(SystemModelRepository.ROOT_NODE_LABEL);
		} else {
			strBuild.append(this.getReceivingExecution());
		}
		return strBuild.toString();
	}

	@Override
	public abstract boolean equals(Object obj);

	@Override // NOCS requests implementation of equals and hashCode in pairs
	public int hashCode() { // NOCS requests implementation of equals and hashCode in pairs
		return (int) (this.timestamp ^ (this.timestamp >>> 32));
	}
}
