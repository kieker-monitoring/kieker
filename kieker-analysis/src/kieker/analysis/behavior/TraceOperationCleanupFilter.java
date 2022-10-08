package kieker.analysis.behavior;

import kieker.analysis.behavior.data.EntryCallEvent;
import kieker.analysis.behavior.data.UserSession;

import teetime.framework.AbstractConsumerStage;
import teetime.framework.OutputPort;

/**
 * Cleanup filter to rewrite class and operation signatures.
 *
 * @author Reiner Jung
 *
 */
public class TraceOperationCleanupFilter extends AbstractConsumerStage<UserSession> {

	public static String TRACE_OPERATION_CLEANUP_REWRITER = TraceOperationCleanupFilter.class + ".cleanupRewriter";

	private final ITraceSignatureCleanupRewriter rewriter;
	private final OutputPort<UserSession> outputPort = this.createOutputPort();

	/**
	 * Create the cleanup.
	 *
	 * @param rewriter
	 *            rewrite rule class.
	 */
	public TraceOperationCleanupFilter(final ITraceSignatureCleanupRewriter rewriter) {
		this.rewriter = rewriter;
	}

	@Override
	protected void execute(final UserSession session) throws Exception {
		for (final EntryCallEvent event : session.getEvents()) {
			event.setClassSignature(this.rewriter.rewriteClassSignature(event.getClassSignature()));
			event.setClassSignature(this.rewriter.rewriteOperationSignature(event.getOperationSignature()));
		}

		this.outputPort.send(session);
	}

	public OutputPort<UserSession> getOutputPort() {
		return this.outputPort;
	}

}
