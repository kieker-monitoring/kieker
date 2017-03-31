package kieker.analysisteetime;

import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;

import kieker.analysisteetime.statistics.Statistic;
import kieker.analysisteetime.statistics.Statistics;
import kieker.analysisteetime.statistics.StatisticsCalculator;
import kieker.analysisteetime.statistics.Unit;

public class StatisticsDecorator<T> {

	private Map<Object, Statistics> statisticsModel;
	private Unit unit;
	private StatisticsCalculator<T> statisticCalculator;
	private Function<T, Object> objectAccesor;
	
	public StatisticsDecorator(Consumer<Statistic> statisticCalculator) {
		this.statisticCalculator = (s,i,o) -> statisticCalculator.accept(s);
	}
	
	public StatisticsDecorator(BiConsumer<Statistic, T> statisticCalculator) {
		this.statisticCalculator = (s,i,o) -> statisticCalculator.accept(s, i);
	}
	
	public void decorate(T input) {
		final Object object = this.objectAccesor.apply(input);
		final Statistic statistic = this.statisticsModel.get(object).getStatistic(this.unit);
		this.statisticCalculator.calculate(statistic, input, object);
	}
	
}
