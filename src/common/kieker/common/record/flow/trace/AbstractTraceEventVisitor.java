package kieker.common.record.flow.trace;

import kieker.common.record.flow.trace.operation.AfterOperationEvent;
import kieker.common.record.flow.trace.operation.AfterOperationFailedEvent;
import kieker.common.record.flow.trace.operation.BeforeOperationEvent;
import kieker.common.record.flow.trace.operation.CallOperationEvent;

public interface AbstractTraceEventVisitor {

	public void handleAfterOperationEvent(AfterOperationEvent afterOperationEvent);

	public void handleAfterOperationFailedEvent(AfterOperationFailedEvent afterOperationFailedEvent);

	public void handleBeforeOperationEvent(BeforeOperationEvent beforeOperationEvent);

	public void handleCallOperationEvent(CallOperationEvent callOperationEvent);

	public void handleSplitEvent(SplitEvent splitEvent);

}
