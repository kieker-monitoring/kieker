/***************************************************************************
 * Copyright (C) 2021 OceanDSL (https://oceandsl.uni-kiel.de)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ***************************************************************************/
package kieker.analysis.architecture.recovery;

import java.util.Map.Entry;
import java.util.function.Function;

import org.eclipse.emf.ecore.EObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import kieker.analysis.architecture.dependency.PropertyConstants;
import kieker.analysis.architecture.recovery.events.DeployedOperationCallEvent;
import kieker.analysis.statistics.StatisticsDecoratorStage;
import kieker.analysis.statistics.calculating.CountCalculator;
import kieker.model.analysismodel.deployment.DeployedOperation;
import kieker.model.analysismodel.execution.ExecutionModel;
import kieker.model.analysismodel.execution.Invocation;
import kieker.model.analysismodel.execution.Tuple;
import kieker.model.analysismodel.statistics.StatisticsModel;

/**
 * Counts the number of unique operation calls and stores that information in the statistics model.
 * See {@link StatisticsDecoratorStage} for detail.
 *
 * @author Reiner Jung
 * @since 1.0
 */
public class CountUniqueCallsStage extends StatisticsDecoratorStage<DeployedOperationCallEvent> {

	/**
	 * Count unique calls.
	 *
	 * @param statisticsModel
	 *            statistics model for the statistics
	 * @param executionModel
	 *            model containing the calls
	 */
	public CountUniqueCallsStage(final StatisticsModel statisticsModel, final ExecutionModel executionModel) {
		super(statisticsModel, new CountCalculator<>(PropertyConstants.CALLS),
				CountUniqueCallsStage.createForInvocation(executionModel));
	}

	private static Function<DeployedOperationCallEvent, EObject> createForInvocation(
			final ExecutionModel executionModel) {
		return operationCall -> {
			final Invocation result = CountUniqueCallsStage.getValue(executionModel, operationCall.getOperationCall());

			if (result == null) {
				final Logger logger = LoggerFactory.getLogger(CountUniqueCallsStage.class);
				logger.error("Fatal error: call not does not exist {}:{}",
						operationCall.getOperationCall().getFirst().getAssemblyOperation().getOperationType()
								.getSignature(),
						operationCall.getOperationCall().getSecond().getAssemblyOperation().getOperationType()
								.getSignature());
			}

			return result;
		};
	}

	private static Invocation getValue(final ExecutionModel executionModel,
			final Tuple<DeployedOperation, DeployedOperation> key) {
		for (final Entry<Tuple<DeployedOperation, DeployedOperation>, Invocation> ag : executionModel
				.getInvocations()) {
			if (ag.getKey().hashCode() == key.hashCode()) {
				return ag.getValue();
			}
		}
		return null;
	}
}
