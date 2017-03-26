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

public class StatisticDecorator<T> {

	private final Map<T, Statistics> statisticsModel = new HashMap<>();
	//private ModelWrapper<K, T> model;
	//private Supplier<K> keyGetter;
	private Unit unit = PredefinedUnits.RESPONSE_TIME;
	private Property property = PredefinedProperties.TOTAL;
	
	private Function<OperationCall, T> objectAcccesor;
	
	public void process(OperationCall operationCall) {
		
		T object = this.objectAcccesor.apply(operationCall);
		Statistics statistics = statisticsModel.get(object);
		
		long property = statistics.getStatistic(unit).getProperty(this.property);
		long duration = operationCall.getDuration().toNanos();
		statistics.getStatistic(unit).setProperty(this.property, property + duration);
		
	}
	
	public void scratch() {
		/*
		K key = this.keyGetter.get();
		T object = model.getMap().get(key);
		Statistics statistics = statisticsModel.get(object);
	
		long property = statistics.getStatistic(unit).getProperty(this.property);
		statistics.getStatistic(unit).setProperty(this.property, property + 1);
		*/
	}	
	
}
