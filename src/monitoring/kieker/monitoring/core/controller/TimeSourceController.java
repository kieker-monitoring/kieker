package kieker.monitoring.core.controller;

import kieker.monitoring.core.configuration.Configuration;
import kieker.monitoring.timer.ITimeSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * @author Jan Waller
 */
public class TimeSourceController extends AbstractController implements ITimeSourceController {
	private static final Log log = LogFactory.getLog(TimeSourceController.class);

	/** the ITimeSource used by this instance */
	private final ITimeSource timeSource;

	protected TimeSourceController(final Configuration configuration) {
		this.timeSource = createAndInitialize(ITimeSource.class, configuration.getStringProperty(Configuration.TIMER_CLASSNAME), configuration);
		if (this.timeSource == null) {
			terminate();
		}
	}

	@Override
	protected final void cleanup() {
		TimeSourceController.log.info("Shutting down TimeSource Controller");
		// nothing to do
		// timeSource.cleanup();
	}

	@Override
	public final String toString() {
		final StringBuilder sb = new StringBuilder();
		sb.append("TimeSourceController: ");
		if (this.timeSource != null) {
			sb.append("TimeSource: '");
			sb.append(this.getTimeSource().getClass().getName());
			sb.append("'");
		} else {
			sb.append("No TimeSource available");
		}
		return sb.toString();
	}

	@Override
	public final ITimeSource getTimeSource() {
		return timeSource;
	}
}
