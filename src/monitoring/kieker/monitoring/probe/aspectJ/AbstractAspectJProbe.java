package kieker.monitoring.probe.aspectJ;

import kieker.monitoring.probe.IMonitoringProbe;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

/**
 * @author Jan Waller
 */
@Aspect
public abstract class AbstractAspectJProbe implements IMonitoringProbe {

	@Pointcut("!within(kieker.common..*) && !within(kieker.monitoring..*) && !within(kieker.analysis..*) && !within(kieker.tools..*)")
	public void notWithinKieker() {
	}
}
