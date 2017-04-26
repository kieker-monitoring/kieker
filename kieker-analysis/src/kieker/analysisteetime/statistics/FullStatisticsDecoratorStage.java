package kieker.analysisteetime.statistics;

import java.util.function.Function;

import kieker.analysisteetime.statistics.calculating.AverageCalculator;
import kieker.analysisteetime.statistics.calculating.CountCalculator;
import kieker.analysisteetime.statistics.calculating.MaxCalculator;
import kieker.analysisteetime.statistics.calculating.MedianCalculator;
import kieker.analysisteetime.statistics.calculating.MinCalculator;
import kieker.analysisteetime.statistics.calculating.TotalCalculator;

import teetime.framework.CompositeStage;
import teetime.framework.InputPort;
import teetime.framework.OutputPort;

public class FullStatisticsDecoratorStage<T> extends CompositeStage {

	private final StatisticsDecoratorStage<T> countStatistics;
	private final StatisticsDecoratorStage<T> totalStatistics;
	private final StatisticsDecoratorStage<T> minStatistics;
	private final StatisticsDecoratorStage<T> maxStatistics;
	private final StatisticsDecoratorStage<T> averageStatistics;
	private final StatisticsDecoratorStage<T> medianStatistics;

	public FullStatisticsDecoratorStage(final StatisticsModel statisticsModel, final Unit unit, final Function<T, Long> valueAccessor,
			final Function<T, Object> objectAccesor) {

		this.countStatistics = new StatisticsDecoratorStage<>(statisticsModel, unit, new CountCalculator<>(), objectAccesor);
		this.totalStatistics = new StatisticsDecoratorStage<>(statisticsModel, unit, new TotalCalculator<>(valueAccessor), objectAccesor);
		this.minStatistics = new StatisticsDecoratorStage<>(statisticsModel, unit, new MinCalculator<>(valueAccessor), objectAccesor);
		this.maxStatistics = new StatisticsDecoratorStage<>(statisticsModel, unit, new MaxCalculator<>(valueAccessor), objectAccesor);
		this.averageStatistics = new StatisticsDecoratorStage<>(statisticsModel, unit, new AverageCalculator<>(), objectAccesor);
		this.medianStatistics = new StatisticsDecoratorStage<>(statisticsModel, unit, new MedianCalculator<>(valueAccessor), objectAccesor);

		super.connectPorts(this.countStatistics.getOutputPort(), this.totalStatistics.getInputPort());
		super.connectPorts(this.totalStatistics.getOutputPort(), this.minStatistics.getInputPort());
		super.connectPorts(this.minStatistics.getOutputPort(), this.maxStatistics.getInputPort());
		super.connectPorts(this.maxStatistics.getOutputPort(), this.averageStatistics.getInputPort());
		super.connectPorts(this.averageStatistics.getOutputPort(), this.medianStatistics.getInputPort());
	}

	public InputPort<T> getInputPort() {
		return this.countStatistics.getInputPort();
	}

	public OutputPort<T> getOutputPort() {
		return this.medianStatistics.getOutputPort();
	}

}
