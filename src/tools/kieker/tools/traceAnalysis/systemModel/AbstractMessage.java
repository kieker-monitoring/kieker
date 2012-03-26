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

/**
 * 
 * @author Andre van Hoorn
 */
public abstract class AbstractMessage {

	private final long timestamp;
	private final Execution sendingExecution;
	private final Execution receivingExecution;

	public AbstractMessage(final long timestamp, final Execution sendingExecution, final Execution receivingExecution) {
		this.timestamp = timestamp;
		this.sendingExecution = sendingExecution;
		this.receivingExecution = receivingExecution;
	}

	public final Execution getReceivingExecution() {
		return this.receivingExecution;
	}

	public final Execution getSendingExecution() {
		return this.sendingExecution;
	}

	public final long getTimestamp() {
		return this.timestamp;
	}

	@Override
	public String toString() {
		final StringBuilder strBuild = new StringBuilder();

		if (this instanceof SynchronousCallMessage) {
			strBuild.append("SYNC-CALL").append(" ");
		} else {
			strBuild.append("SYNC-RPLY").append(" ");
		}

		strBuild.append(this.timestamp);
		strBuild.append(" ");
		if (this.getSendingExecution().getOperation().getId() == Operation.ROOT_OPERATION_ID) {
			strBuild.append("$");
		} else {
			strBuild.append(this.getSendingExecution());
		}
		strBuild.append(" --> ");
		if (this.getReceivingExecution().getOperation().getId() == Operation.ROOT_OPERATION_ID) {
			strBuild.append("$");
		} else {
			strBuild.append(this.getReceivingExecution());
		}
		return strBuild.toString();
	}

	@Override
	public abstract boolean equals(Object obj);

	@Override
	public int hashCode() {
		return (int) (this.timestamp ^ (this.timestamp >>> 32));
	}
}
