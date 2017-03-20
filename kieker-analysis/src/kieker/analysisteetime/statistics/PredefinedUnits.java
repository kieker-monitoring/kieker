package kieker.analysisteetime.statistics;

public enum PredefinedUnits implements Unit {

	RESPONSE_TIME("response time"), CPU_UITL("CPU utilization");
	
	private final String name;
	
	private PredefinedUnits(final String name) {
		this.name = name;
	}

	@Override
	public String getName() {
		return this.name;
	}
	
	@Override
	public String toString() {
		return this.name;
	}
	
}
