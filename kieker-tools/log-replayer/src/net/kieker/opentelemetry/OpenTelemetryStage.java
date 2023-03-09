package net.kieker.opentelemetry;

import java.util.Stack;

import kieker.common.record.IMonitoringRecord;
import kieker.common.record.controlflow.OperationExecutionRecord;
import kieker.common.record.misc.KiekerMetadataRecord;

import teetime.framework.AbstractConsumerStage;

public class OpenTelemetryStage extends AbstractConsumerStage<IMonitoringRecord> {

	private static final String KIEKER_OTEL_TRANSFORMER = "kieker-otel";
	private static final String KIEKER_OTEL_TRANSFORMER_VERSION = "2.0.0-SNAPSHOT";

	private final String endpoint;
	private KiekerSpanCreator creator;
	
	public OpenTelemetryStage(String endpoint) {
		this.endpoint = endpoint;
	}
	
	private final Stack<KiekerSpanData> currentStack = new Stack<>();
	
	@Override
	protected void execute(IMonitoringRecord record) throws Exception {
		if (record instanceof KiekerMetadataRecord) {
			System.out.println("Ignoring metadata record");
		} else if (record instanceof OperationExecutionRecord) {
			OperationExecutionRecord operationExecutionRecord = (OperationExecutionRecord) record;
			
			String hostname = operationExecutionRecord.getHostname();
			if (creator == null || !hostname.equals(creator.getServiceName())) {
				creator = new KiekerSpanCreator(hostname, endpoint);
			}

			while (operationExecutionRecord.getEss() < currentStack.size()) {
				currentStack.pop();
			}
			KiekerSpanData span;
			if (currentStack.size() == 0) {
				span = creator.createSpan(operationExecutionRecord.getOperationSignature(), operationExecutionRecord.getTin(), operationExecutionRecord.getTout());
			} else {
				span = creator.createSubSpan(operationExecutionRecord.getOperationSignature(), operationExecutionRecord.getTin(), operationExecutionRecord.getTout(), currentStack.peek().getSpanContext());
			}
			
			currentStack.push(span);
		} else {
			throw new RuntimeException("Currently unsupported record type: " + record.getClass());
		}
	}

}
