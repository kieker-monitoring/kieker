package kieker.analysisteetime.statistics;

import java.util.Map;
import java.util.function.Function;

import kieker.analysisteetime.model.analysismodel.trace.OperationCall;

import teetime.framework.CompositeStage;
import teetime.framework.InputPort;
import teetime.framework.OutputPort;

public class FullStatisticsDecoratorStage extends CompositeStage {

	private final StatisticsDecoratorStage<OperationCall> countStatistics;
	private final StatisticsDecoratorStage<OperationCall> totalStatistics;
	private final StatisticsDecoratorStage<OperationCall> minStatistics;
	private final StatisticsDecoratorStage<OperationCall> maxStatistics;
	private final StatisticsDecoratorStage<OperationCall> averageStatistics;
	private final StatisticsDecoratorStage<OperationCall> medianStatistics;

	public FullStatisticsDecoratorStage(final Map<Object, Statistics> statisticsModel, final Function<OperationCall, Object> objectAccesor) {
		final Unit unit = Units.RESPONSE_TIME;
		final Function<OperationCall, Long> valueAccessor = c -> c.getDuration().toNanos();

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

	public InputPort<OperationCall> getInputPort() {
		return this.countStatistics.getInputPort();
	}

	public OutputPort<OperationCall> getOutputPort() {
		return this.medianStatistics.getOutputPort();
	}

}
