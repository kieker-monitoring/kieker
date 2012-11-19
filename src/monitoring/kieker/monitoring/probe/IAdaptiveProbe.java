package kieker.monitoring.probe;

import java.util.List;

public interface IAdaptiveProbe {

	public boolean isProbeActivated(final List<String> probePatterns, final AbstractProbeInfo abstractProbeInfo);

}
