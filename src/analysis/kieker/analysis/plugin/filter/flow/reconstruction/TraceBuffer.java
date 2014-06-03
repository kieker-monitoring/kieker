package kieker.analysis.plugin.filter.flow.reconstruction;

import java.io.Serializable;
import java.util.Comparator;
import java.util.SortedSet;
import java.util.TreeSet;

import kieker.analysis.plugin.filter.flow.TraceEventRecords;
import kieker.common.logging.Log;
import kieker.common.logging.LogFactory;
import kieker.common.record.flow.trace.AbstractTraceEvent;
import kieker.common.record.flow.trace.TraceMetadata;
import kieker.common.record.flow.trace.operation.AfterOperationEvent;
import kieker.common.record.flow.trace.operation.AfterOperationFailedEvent;
import kieker.common.record.flow.trace.operation.BeforeOperationEvent;

/**
 * The TraceBuffer is synchronized to prevent problems with concurrent access.
 * 
 * @author Jan Waller
 */
public final class TraceBuffer implements ValueFactory<TraceBuffer> {
	private static final Log LOG = LogFactory.getLog(TraceBuffer.class);
	private static final Comparator<AbstractTraceEvent> COMPARATOR = new TraceEventComperator();

	private TraceMetadata trace;
	private final SortedSet<AbstractTraceEvent> events = new TreeSet<AbstractTraceEvent>(COMPARATOR);

	private boolean closeable;
	private boolean damaged;
	private int openEvents;
	private int maxOrderIndex = -1;

	private long minLoggingTimestamp = Long.MAX_VALUE;
	private long maxLoggingTimestamp = -1;

	private long traceId = -1;

	/**
	 * Creates a new instance of this class.
	 */
	public TraceBuffer() {
		// default empty constructor
	}

	public void insertEvent(final AbstractTraceEvent event) {
		final long myTraceId = event.getTraceId();
		synchronized (this) {
			if (this.traceId == -1) {
				this.traceId = myTraceId;
			} else if (this.traceId != myTraceId) {
				LOG.error("Invalid traceId! Expected: " + this.traceId + " but found: " + myTraceId + " in event " + event.toString());
				this.damaged = true;
			}
			final long loggingTimestamp = event.getTimestamp();
			if (loggingTimestamp > this.maxLoggingTimestamp) {
				this.maxLoggingTimestamp = loggingTimestamp;
			}
			if (loggingTimestamp < this.minLoggingTimestamp) {
				this.minLoggingTimestamp = loggingTimestamp;
			}
			final int orderIndex = event.getOrderIndex();
			if (orderIndex > this.maxOrderIndex) {
				this.maxOrderIndex = orderIndex;
			}
			if (event instanceof BeforeOperationEvent) {
				if (orderIndex == 0) {
					this.closeable = true;
				}
				this.openEvents++;
			} else if (event instanceof AfterOperationEvent) {
				this.openEvents--;
			} else if (event instanceof AfterOperationFailedEvent) {
				this.openEvents--;
			}
			if (!this.events.add(event)) {
				LOG.error("Duplicate entry for orderIndex " + orderIndex + " with traceId " + myTraceId);
				this.damaged = true;
			}
		}
	}

	public void setTrace(final TraceMetadata trace) {
		final long myTraceId = trace.getTraceId();
		synchronized (this) {
			if (this.traceId == -1) {
				this.traceId = myTraceId;
			} else if (this.traceId != myTraceId) {
				LOG.error("Invalid traceId! Expected: " + this.traceId + " but found: " + myTraceId + " in trace " + trace.toString());
				this.damaged = true;
			}
			if (this.trace == null) {
				this.trace = trace;
			} else {
				LOG.error("Duplicate Trace entry for traceId " + myTraceId);
				this.damaged = true;
			}
		}
	}

	public boolean isFinished() {
		synchronized (this) {
			return this.closeable && !this.isInvalid();
		}
	}

	public boolean isInvalid() {
		synchronized (this) {
			return (this.trace == null) || this.damaged || (this.openEvents != 0) || (((this.maxOrderIndex + 1) != this.events.size()) || this.events.isEmpty());
		}
	}

	public TraceEventRecords toTraceEvents() {
		synchronized (this) {
			return new TraceEventRecords(this.trace, this.events.toArray(new AbstractTraceEvent[this.events.size()]));
		}
	}

	public long getMaxLoggingTimestamp() {
		synchronized (this) {
			return this.maxLoggingTimestamp;
		}
	}

	public long getMinLoggingTimestamp() {
		synchronized (this) {
			return this.minLoggingTimestamp;
		}
	}

	/**
	 * @author Jan Waller
	 */
	private static final class TraceEventComperator implements Comparator<AbstractTraceEvent>, Serializable {
		private static final long serialVersionUID = 8920737343446332517L;

		/**
		 * Creates a new instance of this class.
		 */
		public TraceEventComperator() {
			// default empty constructor
		}

		public int compare(final AbstractTraceEvent o1, final AbstractTraceEvent o2) {
			return o1.getOrderIndex() - o2.getOrderIndex();
		}
	}

	public TraceBuffer create() {
		return new TraceBuffer();
	}
}
