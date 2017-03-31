package kieker.analysisteetime;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;

import kieker.analysisteetime.model.analysismodel.deployment.DeployedOperation;
import kieker.analysisteetime.model.analysismodel.trace.OperationCall;
import kieker.analysisteetime.statistics.PredefinedProperties;
import kieker.analysisteetime.statistics.PredefinedUnits;
import kieker.analysisteetime.statistics.Property;
import kieker.analysisteetime.statistics.Statistics;
import kieker.analysisteetime.statistics.Unit;
import kieker.analysisteetime.util.RunningMedian;

public class StatisticDecoratorLegacy<T> {

	// General Fields
	private final Map<T, Statistics> statisticsModel = new HashMap<>();
	private Function<OperationCall, T> objectAcccesor;
	
	//Special Fields
	private Unit unit = PredefinedUnits.RESPONSE_TIME;
	private Property property = PredefinedProperties.TOTAL;
	
	private Map<T, RunningMedian<Long>> runningMedians = new HashMap<>();
	
	//private ModelWrapper<K, T> model;
	//private Supplier<K> keyGetter;
	
	public void process(OperationCall operationCall) {
		
		//General Processing
		T object = this.objectAcccesor.apply(operationCall);
		Statistics statistics = statisticsModel.get(object);
		
		//Special Processing
		long duration = operationCall.getDuration().toNanos();
		this.setNumber(statistics);
		this.setTotal(statistics, duration);
		this.setMin(statistics, duration);
		this.setMax(statistics, duration);
		this.setAverage(statistics);
		this.setMedian(object, statistics, duration);
		
	}
	
	private void setNumber(final Statistics statistics) {
		final long oldNumber = statistics.getStatistic(this.unit).getProperty(PredefinedProperties.COUNT);
		final long newNumber = oldNumber + 1;
		statistics.getStatistic(unit).setProperty(PredefinedProperties.COUNT, newNumber);
	}
	
	private void setTotal(final Statistics statistics, final long duration) {
		final long oldTotal = statistics.getStatistic(this.unit).getProperty(PredefinedProperties.TOTAL);
		final long newTotal = oldTotal + duration;
		statistics.getStatistic(unit).setProperty(PredefinedProperties.TOTAL, newTotal);
	}
	
	private void setMin(final Statistics statistics, final long duration) {
		final long oldMin = statistics.getStatistic(this.unit).getProperty(PredefinedProperties.MIN);
		final long newMin = Math.min(oldMin, duration);
		statistics.getStatistic(unit).setProperty(PredefinedProperties.MIN, newMin);
	}
	
	private void setMax(final Statistics statistics, final long duration) {
		final long oldMin = statistics.getStatistic(this.unit).getProperty(PredefinedProperties.MAX);
		final long newMin = Math.max(oldMin, duration);
		statistics.getStatistic(unit).setProperty(PredefinedProperties.MAX, newMin);
	}
	
	private void setAverage(final Statistics statistics) {
		long total = statistics.getStatistic(this.unit).getProperty(PredefinedProperties.TOTAL);
		long number = statistics.getStatistic(this.unit).getProperty(PredefinedProperties.COUNT);
		long newAvg = total / number;
		statistics.getStatistic(unit).setProperty(PredefinedProperties.AVERAGE, newAvg);
	}
	
	private void setMedian(final T key, final Statistics statistics, final long duration) {
		RunningMedian<Long> runningMedian = this.runningMedians.get(key);
		runningMedian.add(duration);
		long newMedian = runningMedian.getMedian();
		statistics.getStatistic(unit).setProperty(PredefinedProperties.AVERAGE, newMedian);
	}
	
	
	
}
