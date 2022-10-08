package kieker.analysis.behavior;

import kieker.analysis.behavior.data.EntryCallEvent;
import kieker.analysis.behavior.data.UserSession;

import teetime.framework.AbstractConsumerStage;
import teetime.framework.OutputPort;

/**
 * Tests whether a trace contains only operations which are considered valid trace elements. In
 * effect it ignores invalid sessions.
 *
 * @author Reiner Jung
 * @since 2.0.0
 */
public class SessionAcceptanceFilter extends AbstractConsumerStage<UserSession> {

	private final OutputPort<UserSession> outputPort = this.createOutputPort();
	private final IEntryCallAcceptanceMatcher matcher;

	/**
	 * Create an acceptance filter with an external matcher.
	 *
	 * @param matcher
	 *            a acceptance matcher
	 */
	public SessionAcceptanceFilter(final IEntryCallAcceptanceMatcher matcher) {
		this.matcher = matcher;
	}

	@Override
	protected void execute(final UserSession session) throws Exception {
		for (final EntryCallEvent call : session.getEvents()) {
			if (!this.matcher.match(call)) {
				return;
			}
		}
		this.outputPort.send(session);
	}

	public OutputPort<UserSession> getOutputPort() {
		return this.outputPort;
	}

}
