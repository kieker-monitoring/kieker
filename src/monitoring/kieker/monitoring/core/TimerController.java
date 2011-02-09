package kieker.monitoring.core;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import kieker.monitoring.core.configuration.Configuration;
import kieker.monitoring.timer.ITimeSource;

abstract class TimerController extends ReplayController implements ITimerController {
	private static final Log log = LogFactory.getLog(TimerController.class);

	/** The {@link ITimeSource} used */
	private final ITimeSource timesource;
	
	protected TimerController(final Configuration configuration) {
		super(configuration);
		if (isMonitoringTerminated()) {
			this.timesource = null;
			return;
		}
		final String timerClassname = configuration.getStringProperty(Configuration.TIMER_CLASSNAME);
		ITimeSource timesource = null;
		try {
		// search for correct Constructor -> 1 correct parameter
			timesource = ITimeSource.class.cast(Class.forName(timerClassname).
					getConstructor(Configuration.class).newInstance(
							configuration.getPropertiesStartingWith(timerClassname)));
		} catch (final NoSuchMethodException ex) {
			TimerController.log.error("Timer Class '" + timerClassname + "' has to implement a constructor that accepts a single Configuration");
		} catch (final NoClassDefFoundError ex) {
			TimerController.log.error("Timer Class '" + timerClassname + "' not found");
		} catch (final ClassNotFoundException ex) {
			TimerController.log.error("Timer Class '" + timerClassname + "' not found");
		} catch (final Throwable ex) {
			TimerController.log.error("Failed to load timer class for name '" + timerClassname + "'", ex);
		}
		if ((this.timesource = timesource) == null) {
			terminateMonitoring();
			return;
		}
	}
	
	@Override
	public String getState() {
		StringBuilder sb = new StringBuilder();
		sb.append(super.getState());
		sb.append("; Time Source: '");
		sb.append(this.timesource.getClass().getName());
		return sb.toString();
	}
	
	@Override
	public final ITimeSource getTimeSource() {
		return this.timesource;
	}
}
