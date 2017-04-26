package kieker.analysisteetime.statistics;

import kieker.analysisteetime.model.ModelObjectFromOperationCallAccesors;
import kieker.analysisteetime.model.analysismodel.execution.ExecutionModel;
import kieker.analysisteetime.model.analysismodel.trace.OperationCall;
import kieker.analysisteetime.statistics.calculating.CountCalculator;

public class CallStatisticsStage extends StatisticsDecoratorStage<OperationCall> {

	public CallStatisticsStage(final StatisticsModel statisticsModel, final ExecutionModel executionModel) {
		super(statisticsModel, Units.RESPONSE_TIME, new CountCalculator<>(),
				ModelObjectFromOperationCallAccesors.createForAggregatedInvocation(executionModel));
	}

}
