package kieker.tools.oteltransformer.stages;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import kieker.common.record.controlflow.OperationExecutionRecord;
import teetime.framework.AbstractConsumerStage;
import teetime.framework.OutputPort;

public class OperationExecutionTraceCollector extends AbstractConsumerStage<OperationExecutionRecord> {

	private final Map<Long, List<OTelExecution>> traceCollector;
	private final Map<Long, Long> traceUpdateMarks;
	private final long timeout;
	
	private final OutputPort<List<OTelExecution>> outputPort = this.createOutputPort();

	
	public OperationExecutionTraceCollector(long timeout) {
		super();
		this.traceCollector = new HashMap<>();
		this.traceUpdateMarks = new HashMap<>();
		this.timeout = timeout;
	}
	
	@Override
	protected void execute(OperationExecutionRecord element) throws Exception {
		final List<OTelExecution> collection = this.traceCollector.get(element.getTraceId());
		if (collection == null) {
			List<OTelExecution> newCollection = new ArrayList<>();
			newCollection.add(this.convert(element));
			this.traceCollector.put(element.getTraceId(), newCollection);
			this.traceUpdateMarks.put(element.getTraceId(), System.nanoTime());
		} else {
			collection.add(this.convert(element));
			this.traceUpdateMarks.put(element.getTraceId(), System.nanoTime());
		}
		this.checkForCompleteTraces();
	}
	
	private void checkForCompleteTraces() {
		long now = System.nanoTime();
		for (Long traceId : this.traceCollector.keySet()) {
			if (this.traceUpdateMarks.get(traceId) + this.timeout < now) {
				List<OTelExecution> collection = this.traceCollector.get(traceId);
				this.outputPort.send(collection);
			}
		}
	}
	
	private OTelExecution convert(OperationExecutionRecord record) {
		// TODO the operation signature contains class and operation, must be split
		return new OTelExecution(record.getHostname(),
				record.getOperationSignature(), // TODO only FQN of class required
				record.getOperationSignature(),
				record.getSessionId(),
				record.getTin(), record.getTout(),
				record.getEss(), record.getEoi());
	}

	public OutputPort<List<OTelExecution>> getOutputPort() {
		return this.outputPort;
	}
	
}
