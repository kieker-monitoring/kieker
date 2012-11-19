package kieker.monitoring.probe;

public abstract class AbstractProbeInfo {
	private final String probeId;

	/**
	 * @param probeId
	 */
	public AbstractProbeInfo(final String probeId) {
		this.probeId = probeId;
	}

	/**
	 * @return the probeId
	 */
	public String getProbeId() {
		return this.probeId;
	}

}
