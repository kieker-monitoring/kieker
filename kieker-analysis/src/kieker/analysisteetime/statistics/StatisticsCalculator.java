package kieker.analysisteetime.statistics;

@FunctionalInterface
public interface StatisticsCalculator<I> {

	public void calculate(Statistic statistic, I input, Object modelObject);

}
