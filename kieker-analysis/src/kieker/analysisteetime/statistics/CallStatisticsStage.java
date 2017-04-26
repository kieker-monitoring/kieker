package kieker.analysisteetime.statistics;

import java.util.Map;

import kieker.analysisteetime.model.ModelObjectFromOperationCallAccesors;
import kieker.analysisteetime.model.analysismodel.execution.ExecutionModel;
import kieker.analysisteetime.model.analysismodel.trace.OperationCall;
import kieker.analysisteetime.statistics.calculating.CountCalculator;

public class CallStatisticsStage extends StatisticsDecoratorStage<OperationCall> {

	public CallStatisticsStage(final Map<Object, Statistics> statisticsModel, final ExecutionModel executionModel) {
		super(statisticsModel, Units.RESPONSE_TIME, new CountCalculator<>(),
				ModelObjectFromOperationCallAccesors.createForAggregatedInvocation(executionModel));
	}

}
