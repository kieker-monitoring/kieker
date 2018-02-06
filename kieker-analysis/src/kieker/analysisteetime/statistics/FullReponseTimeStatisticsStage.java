package kieker.analysisteetime.statistics;

import java.util.function.Function;

import kieker.analysisteetime.model.analysismodel.trace.OperationCall;

/**
 *
 * @author Sören Henning
 *
 * @since 1.13
 *
 */
public class FullReponseTimeStatisticsStage extends FullStatisticsDecoratorStage<OperationCall> {

	public FullReponseTimeStatisticsStage(final StatisticsModel statisticsModel, final Function<OperationCall, Object> objectAccesor) {
		super(statisticsModel, Units.RESPONSE_TIME, c -> c.getDuration().toNanos(), objectAccesor);
	}

}
