package kieker.tools.oteltransformer.stages;

import java.util.List;
import java.util.Map;
import java.util.ArrayList;
import java.util.HashMap;

import kieker.analysis.util.Tuple;
import kieker.common.record.flow.IFlowRecord;
import kieker.common.record.flow.trace.TraceMetadata;
import kieker.common.record.flow.trace.operation.AfterOperationEvent;
import kieker.common.record.flow.trace.operation.BeforeOperationEvent;
import teetime.framework.AbstractConsumerStage;
import teetime.framework.OutputPort;

public class FlowTraceCollector extends AbstractConsumerStage<IFlowRecord> {
	
	private final Map<Long, Tuple<TraceMetadata, List<IFlowRecord>>> traceCollector;
	private List<IFlowRecord> stash;
	private final OutputPort<List<OTelExecution>> outputPort = this.createOutputPort();
	
	public FlowTraceCollector() {
		super();
		this.traceCollector = new HashMap<>();
		this.stash = new ArrayList<>();
	}

	protected void execute(IFlowRecord element) throws Exception {
		if (element instanceof TraceMetadata) {
			TraceMetadata traceMetadata = (TraceMetadata)element;
			Tuple<TraceMetadata, List<IFlowRecord>> tuple = new Tuple<>(traceMetadata, new ArrayList<IFlowRecord>());
			this.traceCollector.put(traceMetadata.getTraceId(), tuple);
			this.applyStash();
		} else if (element instanceof BeforeOperationEvent) {
			BeforeOperationEvent beforeOperationEvent = (BeforeOperationEvent) element;
			Tuple<TraceMetadata, List<IFlowRecord>> tuple = this.traceCollector.get(beforeOperationEvent.getTraceId());
			if (tuple == null)
				stash.add(beforeOperationEvent);
			else
				tuple.getSecond().add(beforeOperationEvent);
		} else if (element instanceof AfterOperationEvent) {
			AfterOperationEvent afterOperationEvent = (AfterOperationEvent) element;
			Tuple<TraceMetadata, List<IFlowRecord>> tuple = this.traceCollector.get(afterOperationEvent.getTraceId());
			if (tuple == null)
				stash.add(afterOperationEvent);
			else {
				tuple.getSecond().add(afterOperationEvent);
				if (afterOperationEvent.getOperationSignature() == ((BeforeOperationEvent)tuple.getSecond().get(0)).getOperationSignature()) {
					// TODO end of trace, does not check orderIndex
					this.makeOperationExecutionRecord(tuple.getFirst(), tuple.getSecond());
					this.traceCollector.remove(tuple.getFirst().getTraceId());
				}
			}
		}
	}
	
	private void makeOperationExecutionRecord(TraceMetadata metadata, List<IFlowRecord> events) {
		final List<BeforeOperationEvent> stack = new ArrayList<BeforeOperationEvent>();
		final List<OTelExecution> traceExecutionList = new ArrayList<>();
		final Map<BeforeOperationEvent, Integer> eoiForEvent = new HashMap<>();
		int eoiCounter = 0;
		for (IFlowRecord event : events) {
			if (event instanceof BeforeOperationEvent) {
				BeforeOperationEvent beforeOperationEvent = (BeforeOperationEvent) event;
				eoiForEvent.put(beforeOperationEvent, eoiCounter++);
				stack.add(beforeOperationEvent);
			} else if (event instanceof AfterOperationEvent) {
				AfterOperationEvent afterOperationEvent = (AfterOperationEvent) event;
				BeforeOperationEvent beforeOperationEvent = stack.remove(stack.size()-1);
				final String hostname = metadata.getHostname();
				final String sessionId = metadata.getSessionId();
				// TODO is this correct?
				int ess = stack.size();;
				int eoi = eoiForEvent.get(beforeOperationEvent);
				traceExecutionList.add(new OTelExecution(hostname, 
						afterOperationEvent.getClassSignature(),
						afterOperationEvent.getOperationSignature(),
						sessionId,
						beforeOperationEvent.getTimestamp(),
						afterOperationEvent.getTimestamp(), ess, eoi));
			}
		}
		this.outputPort.send(traceExecutionList);
	}
	
	private void applyStash() throws Exception {
		List<IFlowRecord> oldStash = this.stash;
		this.stash = new ArrayList<>();
		for (IFlowRecord element : oldStash) {
			this.execute(element);
		}
	}

	public OutputPort<List<OTelExecution>> getOuputPort() {
		return this.outputPort;
	}
}
