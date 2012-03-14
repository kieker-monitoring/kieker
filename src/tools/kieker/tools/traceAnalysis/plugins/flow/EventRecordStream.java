package kieker.tools.traceAnalysis.plugins.flow;

import java.util.List;

import kieker.common.record.flow.trace.AbstractTraceEvent;

public class EventRecordStream {

	private final List<AbstractTraceEvent> events;
	private final int maxIndex;

	private int currentIndex = 0;

	public EventRecordStream(final EventRecordTrace trace) {
		this.events = trace.eventList();
		this.maxIndex = (this.events.size() - 1);
	}

	public void consume() {
		if (this.currentIndex <= this.maxIndex) {
			this.currentIndex++;
		}
	}

	public AbstractTraceEvent currentElement() {
		return this.lookahead(0);
	}

	public AbstractTraceEvent lookahead(final int amount) {
		final int desiredIndex = (this.currentIndex + amount);

		if ((desiredIndex < 0) || (desiredIndex > this.maxIndex)) {
			return null;
		}

		return this.events.get(desiredIndex);
	}

}
