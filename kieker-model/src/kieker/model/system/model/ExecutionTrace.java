/***************************************************************************
 * Copyright 2022 Kieker Project (http://kieker-monitoring.net)
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

package kieker.model.system.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.SortedSet;
import java.util.Stack;
import java.util.TreeSet;
import java.util.concurrent.atomic.AtomicReference;

import kieker.common.util.dataformat.LoggingTimestampConversionUtils;
import kieker.model.system.model.exceptions.InvalidTraceException;

/**
 * This class is a container for a whole trace of executions (represented as
 * instances of {@link Execution}).
 *
 * Note that no assumptions about the {@link java.util.concurrent.TimeUnit} used
 * for the timestamps are made.
 *
 * @author Andre van Hoorn
 *
 * @since 0.95a
 */
public class ExecutionTrace extends AbstractTrace {

	private final AtomicReference<MessageTrace> messageTraceReference = new AtomicReference<>();
	private int minEoi = -1;
	private int maxEoi = -1;
	private long minTin = -1;
	private long maxTout = -1;
	private int maxEss = -1;
	private final SortedSet<Execution> trace = new TreeSet<>(ExecutionTrace.createExecutionTraceComparator());
	private final SortedSet<Execution> unmodifiableExecutions = Collections.unmodifiableSortedSet(this.trace);

	/**
	 * Creates a new instance of this class using the given parameters.
	 *
	 * @param traceId
	 *            The ID of this trace.
	 */
	public ExecutionTrace(final long traceId) {
		super(traceId);
	}

	/**
	 * Creates a new instance of this class using the given parameters.
	 *
	 * @param traceId
	 *            The ID of this trace.
	 * @param sessionId
	 *            The ID of the current session.
	 */
	public ExecutionTrace(final long traceId, final String sessionId) {
		super(traceId, sessionId);
	}

	/**
	 * Adds an execution to the trace.
	 *
	 * @param execution
	 *            The execution object which will be added to this trace.
	 *
	 * @throws InvalidTraceException
	 *             If the traceId of the passed Execution object
	 *             is not the same as the traceId of this
	 *             ExecutionTrace object.
	 */
	public void add(final Execution execution) throws InvalidTraceException {
		System.err.println(">> ESS " + execution.toString());
		synchronized (this) {
			if (this.getTraceId() != execution.getTraceId()) {
				throw new InvalidTraceException(
						String.format("TraceId of new record (%d) differs from Id of this trace (%d)", execution.getTraceId(), this.getTraceId()));
			}
			if (this.minTin < 0 || execution.getTin() < this.minTin) {
				this.minTin = execution.getTin();
			}
			if (this.maxTout < 0 || execution.getTout() > this.maxTout) {
				this.maxTout = execution.getTout();
			}
			if (this.minEoi < 0 || execution.getEoi() < this.minEoi) {
				this.minEoi = execution.getEoi();
			}
			if (this.maxEoi < 0 || execution.getEoi() > this.maxEoi) {
				this.maxEoi = execution.getEoi();
			}
			if (execution.getEss() > this.maxEss) {
				this.maxEss = execution.getEss();
			}
			this.trace.add(execution);
			// Invalidate the current message trace representation
			this.messageTraceReference.set(null);
		}
	}

	/**
	 * Returns the message trace representation for this trace.<br/>
	 *
	 * The transformation to a message trace is only computed during the first
	 * execution of this method. After this, the stored reference is returned ---
	 * unless executions are added to the trace afterwards.
	 *
	 * @param rootExecution
	 *            The root execution object.
	 *
	 * @return The resulting message trace.
	 *
	 * @throws InvalidTraceException
	 *             If the given execution is somehow inconsistent
	 *             or invalid.
	 */
	public MessageTrace toMessageTrace(final Execution rootExecution) throws InvalidTraceException {
		synchronized (this) {
			MessageTrace messageTrace = this.messageTraceReference.get();
			if (messageTrace != null) {
				return messageTrace;
			}

			final List<AbstractMessage> messages = new ArrayList<>();
			final Stack<AbstractMessage> currentMessageStack = new Stack<>();
			final Iterator<Execution> executionsIterator = this.trace.iterator();

			Execution previousExecution = rootExecution;
			int previousEoi = -1;
			boolean expectingEntryCall = true; // used to make that entry call found in first iteration
			while (executionsIterator.hasNext()) {
				final Execution currentExecution = executionsIterator.next();

				if (expectingEntryCall && currentExecution.getEss() != 0) {
					throw new InvalidTraceException(String.format("First execution must have ess 0 (found %d)\n Causing execution: %s",
							currentExecution.getEss(), currentExecution.toString()));
				}
				expectingEntryCall = false; // now we're happy
				if (previousEoi != currentExecution.getEoi() - 1) {
					throw new InvalidTraceException(String.format("Eois must increment by 1 -- but found sequence <%d,%d> (Execution: %s)",
							previousEoi, currentExecution.getEoi(), currentExecution.toString()));
				}
				previousEoi = currentExecution.getEoi();

				// First, we might need to clean up the stack for the next execution callMessage
				if (!previousExecution.equals(rootExecution) && previousExecution.getEss() >= currentExecution.getEss()) {
					Execution currentReturnReceiver; // receiverComponentName of return message
					while (currentMessageStack.size() > currentExecution.getEss()) {
						final AbstractMessage poppedCall = currentMessageStack.pop();
						previousExecution = poppedCall.getReceivingExecution();
						currentReturnReceiver = poppedCall.getSendingExecution();
						final AbstractMessage message = new SynchronousReplyMessage(previousExecution.getTout(), previousExecution,
								currentReturnReceiver);
						messages.add(message);
						previousExecution = currentReturnReceiver;
					}
				}

				final SynchronousCallMessage callMessage = this.createCallMessage(rootExecution, previousExecution, currentExecution);
				messages.add(callMessage);
				currentMessageStack.push(callMessage);

				if (!executionsIterator.hasNext()) { // empty stack completely, since no more executions
					Execution curReturnReceiver; // receiverComponentName of return message
					while (!currentMessageStack.empty()) {
						final AbstractMessage poppedCall = currentMessageStack.pop();
						previousExecution = poppedCall.getReceivingExecution();
						curReturnReceiver = poppedCall.getSendingExecution();
						final AbstractMessage message = new SynchronousReplyMessage(previousExecution.getTout(), previousExecution,
								curReturnReceiver);
						messages.add(message);
						previousExecution = curReturnReceiver;
					}
				}
				previousExecution = currentExecution; // prepair next loop
			}
			messageTrace = new MessageTrace(this.getTraceId(), this.getSessionId(), messages);
			this.messageTraceReference.set(messageTrace);
			return messageTrace;
		}
	}

	private SynchronousCallMessage createCallMessage(final Execution rootExecution, final Execution prevE,
			final Execution curE) throws InvalidTraceException {
		final SynchronousCallMessage message;

		if (prevE.equals(rootExecution)) { // initial execution callMessage
			message = new SynchronousCallMessage(curE.getTin(), rootExecution, curE);
		} else if (prevE.getEss() + 1 == curE.getEss()) { // usual callMessage with senderComponentName and
			// receiverComponentName
			message = new SynchronousCallMessage(curE.getTin(), prevE, curE);
		} else if (prevE.getEss() < curE.getEss()) { // detect ess incrementation by > 1
			final InvalidTraceException ex = new InvalidTraceException(
					"Ess are only allowed to increment by 1 --" + "but found sequence <" + prevE.getEss() + ","
							+ curE.getEss() + ">" + "(Execution: " + curE + ")");
			// don't log and throw
			// LOG.error("Found invalid trace:" + ex.getMessage()); // don't need the stack
			// trace here
			throw ex;
		} else {
			final String errorMessage = "Unexpected trace: " + prevE + " and " + curE;
			throw new IllegalStateException(errorMessage);
		}

		return message;
	}

	/**
	 * Returns a sorted set (unmodifiable) of {@link Execution}s in this trace.
	 *
	 * Note that the returned data structure is the (wrapped )internal data
	 * structure of this {@link ExecutionTrace} object, to which further elements
	 * may be added by the
	 * {@link kieker.model.system.model.ExecutionTrace#add(Execution)} method.
	 * Consider to create a copy of the returned list, while synchronizing on this
	 * (i.e., the {@link ExecutionTrace}) object.
	 *
	 * @return the sorted set of {@link Execution}s in this trace
	 */
	public final SortedSet<Execution> getTraceAsSortedExecutionSet() {
		// The justification why this works can be found in #1537.
		synchronized (this) {
			return this.unmodifiableExecutions;
		}
	}

	/**
	 * Returns the length of this trace in terms of the number of contained
	 * executions.
	 *
	 * @return the length of this trace.
	 */
	public final int getLength() {
		synchronized (this) {
			return this.trace.size();
		}
	}

	@Override
	public String toString() {
		final StringBuilder strBuild = new StringBuilder(512);
		synchronized (this) {
			strBuild.append("TraceId ").append(this.getTraceId());
			strBuild.append(" (minTin=").append(this.minTin);
			strBuild.append(" (").append(LoggingTimestampConversionUtils.convertLoggingTimestampToUTCString(this.minTin));
			strBuild.append("); maxTout=").append(this.maxTout);
			strBuild.append(" (").append(LoggingTimestampConversionUtils.convertLoggingTimestampToUTCString(this.maxTout));
			strBuild.append("); maxEss=").append(this.maxEss).append("):\n");
			for (final Execution e : this.trace) {
				strBuild.append('<');
				strBuild.append(e.toString()).append(">\n");
			}
		}
		return strBuild.toString();
	}

	/**
	 * Returns the maximum execution stack size (ess) value, i.e., the maximum stack
	 * depth, within the trace.
	 *
	 * @return the maximum ess; -1 if the trace contains no executions.
	 */
	public int getMaxEss() {
		synchronized (this) {
			return this.maxEss;
		}
	}

	/**
	 * Returns the maximum execution order index (eoi) value within the trace.
	 *
	 * @return the maximum eoi; -1 if the trace contains no executions.
	 */
	public int getMaxEoi() {
		synchronized (this) {
			return this.maxEoi;
		}
	}

	/**
	 * Returns the minimum execution order index (eoi) value within the trace.
	 *
	 * @return the minimum eoi; -1 if the trace contains no executions.
	 */
	public int getMinEoi() {
		synchronized (this) {
			return this.minEoi;
		}
	}

	/**
	 * Returns the duration of this (possibly incomplete) trace.
	 *
	 * This value is the difference between the maximum tout and the minimum tin
	 * value. Note that no specific assumptions about the
	 * {@link java.util.concurrent.TimeUnit} are made.
	 *
	 * @return the duration of this trace.
	 */
	public long getDuration() {
		synchronized (this) {
			return this.getMaxTout() - this.minTin;
		}
	}

	/**
	 * Returns the maximum timestamp value of an execution return in this trace.
	 *
	 * Notice that you should need use this value to reason about the control flow
	 * --- particularly in distributed scenarios.
	 *
	 * @return the maxmum timestamp value; -1 if the trace contains no executions.
	 */
	public long getMaxTout() {
		synchronized (this) {
			return this.maxTout;
		}
	}

	/**
	 * Returns the minimum timestamp of an execution start in this trace.
	 *
	 * Notice that you should need use this value to reason about the control flow
	 * --- particularly in distributed scenarios.
	 *
	 * @return the minimum timestamp value; -1 if the trace contains no executions.
	 */
	public long getMinTin() {
		synchronized (this) {
			return this.minTin;
		}
	}

	@Override
	public long getStartTimestamp() {
		return this.getMinTin();
	}

	@Override
	public long getEndTimestamp() {
		return this.getMaxTout();
	}

	// Explicit delegation to super method to make FindBugs happy
	@Override
	public int hashCode() { // NOPMD (forward hashcode)
		return super.hashCode();
	}

	/**
	 * Returns whether this Execution Trace and the passed Object are equal. Two
	 * execution traces are equal if the set of contained executions is equal.
	 *
	 * @param obj
	 *            The object to be compared for equality with this.
	 *
	 * @return true if and only if the two objects are equal.
	 */
	@Override
	public boolean equals(final Object obj) {
		synchronized (this) {
			if (!(obj instanceof ExecutionTrace)) {
				return false;
			}
			if (this == obj) {
				return true;
			}
			final ExecutionTrace other = (ExecutionTrace) obj;
			if (this.getTraceId() != other.getTraceId()) {
				return false;
			}
			// Note that we are using a TreeSet which is not using the Object's equals
			// method but the Set's Comparator, which we defined in this case.
			return this.trace.equals(other.trace);
		}
	}

	/**
	 * Returns an instance of the {@link Comparator} used by the internal
	 * {@link TreeSet} to compare {@link Execution}s.
	 *
	 * @return A comparator instance to compare execution objects.
	 */
	public static final Comparator<Execution> createExecutionTraceComparator() {
		return new ExecutionTraceComparator();
	}

	/**
	 * @author Andre van Hoorn
	 */
	private static final class ExecutionTraceComparator implements Comparator<Execution>, Serializable {

		private static final long serialVersionUID = -6334359132236475506L;

		/**
		 * Creates a new instance of this class.
		 */
		public ExecutionTraceComparator() {
			// Nothing to do here
		}

		/**
		 * Note that this method is not only used by
		 * {@link ExecutionTrace#add(Execution)} but also by
		 * {@link TreeSet#equals(Object)} utilized in
		 * {@link ExecutionTrace#equals(Object)}.
		 *
		 * @param e1
		 *            The first execution object.
		 * @param e2
		 *            The second execution object.
		 *
		 * @return -1 if e1 < e2, 1 if e1 > e2, 0 otherwise.
		 */
		@Override
		public final int compare(final Execution e1, final Execution e2) {
			// If executions equal, return immediately
			if (e1.equals(e2)) {
				return 0;
			}

			// 1. criterion: trace id
			if (e1.getTraceId() < e2.getTraceId()) {
				return -1;
			} else if (e1.getTraceId() > e2.getTraceId()) {
				return 1;
			}

			// At this location: trace ids equal

			// 2. criterion: eoi
			if (e1.getEoi() < e2.getEoi()) {
				return -1;
			}
			if (e1.getEoi() > e2.getEoi()) {
				return 1;
			}

			// At this location: trace ids, eoi equal

			// 3. criterion: ess
			if (e1.getEss() < e2.getEss()) {
				return -1;
			}
			if (e1.getEss() > e2.getEss()) {
				return 1;
			}

			// At this location: trace ids, eoi, ess equal

			// 4. criterion: tin
			if (e1.getTin() < e2.getTin()) {
				return -1;
			}
			if (e1.getTin() > e2.getTin()) {
				return 1;
			}

			// At this location: trace ids, eoi, ess, tin equal

			// 5. criterion: tout
			if (e1.getTout() < e2.getTout()) {
				return -1;
			}
			if (e1.getTout() > e2.getTout()) {
				return 1;
			}

			// At this location: trace ids, eoi, ess, tin, tout equal
			return e1.hashCode() - e2.hashCode();
		}
	}
}
