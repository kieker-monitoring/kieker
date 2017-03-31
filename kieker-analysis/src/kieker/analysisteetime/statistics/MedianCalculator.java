package kieker.analysisteetime.statistics;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import kieker.analysisteetime.util.RunningMedian;

public class MedianCalculator<T> implements StatisticsCalculator<T> {

	private final static Property AVERAGE_PROPERTY = PredefinedProperties.AVERAGE;
	
	private Function<T, Long> valueAccessor;
	private Map<Object, RunningMedian<Long>> runningMedians = new HashMap<>();	
	
	@Override
	public void calculate(Statistic statistic, T input, Object modelObject) {
		RunningMedian<Long> runningMedian = this.runningMedians.computeIfAbsent(modelObject, o -> new RunningMedian<>());
		runningMedian.add(this.valueAccessor.apply(input));
		long newMedian = runningMedian.getMedian();
		statistic.setProperty(AVERAGE_PROPERTY, newMedian);	
	}
	
}
