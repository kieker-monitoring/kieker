/***************************************************************************
 * Copyright (C) 2021 Kieker Project (https://kieker-monitoring.net)
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
package kieker.tools.mvis.stages.metrics;

import java.util.Map.Entry;

import org.eclipse.emf.common.util.EMap;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;

import kieker.analysis.architecture.dependency.PropertyConstants;
import kieker.analysis.architecture.repository.ModelRepository;
import kieker.model.analysismodel.deployment.DeployedOperation;
import kieker.model.analysismodel.execution.ExecutionModel;
import kieker.model.analysismodel.execution.ExecutionPackage;
import kieker.model.analysismodel.execution.Invocation;
import kieker.model.analysismodel.execution.Tuple;
import kieker.model.analysismodel.statistics.StatisticRecord;
import kieker.model.analysismodel.statistics.StatisticsModel;
import kieker.model.analysismodel.statistics.StatisticsPackage;

import teetime.stage.basic.AbstractTransformation;

import org.oceandsl.analysis.generic.Table;

/**
 * @author Reiner Jung
 * @since 1.1
 */
public class NumberOfCallsStage extends AbstractTransformation<ModelRepository, Table<String, NumberOfCallsEntry>> {

    @Override
    protected void execute(final ModelRepository repository) throws Exception {
        final ExecutionModel executionModel = repository.getModel(ExecutionPackage.Literals.EXECUTION_MODEL);
        final StatisticsModel statisticsModel = repository.getModel(StatisticsPackage.Literals.STATISTICS_MODEL);

        final Table<String, NumberOfCallsEntry> result = new Table<>(repository.getName());

        for (final Entry<Tuple<DeployedOperation, DeployedOperation>, Invocation> invocationEntry : executionModel
                .getInvocations().entrySet()) {
            this.processInvocation(result, statisticsModel, invocationEntry);
        }

        this.outputPort.send(result);
    }

    private void processInvocation(final Table<String, NumberOfCallsEntry> result,
            final StatisticsModel statisticsModel,
            final Entry<Tuple<DeployedOperation, DeployedOperation>, Invocation> invocationEntry) {
        final Invocation value = invocationEntry.getValue();
        if (value == null) {
            this.logger.error("Broken invocation entry. {} -> {}", this.getName(invocationEntry.getKey().getFirst()),
                    this.getName(invocationEntry.getKey().getSecond()));
        }
        final StatisticRecord statistics = this.findStatistics(statisticsModel.getStatistics(), value);
        if (statistics != null) {
            final Long data = (Long) statistics.getProperties().get(PropertyConstants.CALLS);

            result.getRows()
                    .add(new NumberOfCallsEntry(
                            value.getCaller().getAssemblyOperation().getComponent().getComponentType().getSignature(),
                            value.getCaller().getAssemblyOperation().getOperationType().getSignature(),
                            value.getCallee().getAssemblyOperation().getComponent().getComponentType().getSignature(),
                            value.getCallee().getAssemblyOperation().getOperationType().getSignature(), data));
        } else {
            this.logger.warn("Missing statistics for invocation {} -> {}",
                    this.getName(invocationEntry.getValue().getCaller()),
                    this.getName(invocationEntry.getValue().getCallee()));
        }
    }

    private StatisticRecord findStatistics(final EMap<EObject, StatisticRecord> statistics,
            final Invocation invocation) {
        for (final Entry<EObject, StatisticRecord> entry : statistics.entrySet()) {
            final StatisticRecord statistic = this.findStatistic(entry, invocation);
            if (statistic != null) {
                return statistic;
            }
        }
        return null;
    }

    private StatisticRecord findStatistic(final Entry<EObject, StatisticRecord> entry, final Invocation invocation) {
        if (entry.getKey() instanceof Invocation) {
            final Invocation key = (Invocation) entry.getKey();
            if (key != null) {
                if (invocation.getCaller().equals(key.getCaller()) && invocation.getCallee().equals(key.getCallee())) {
                    return entry.getValue();
                }
            } else {
                this.logger.error("Found statistics without a key value");
                for (final Entry<String, Object> recordEntry : entry.getValue().getProperties().entrySet()) {
                    this.logger.error("property {} = {}", recordEntry.getKey(), recordEntry.getValue());
                }
            }
        }
        return null;
    }

    public Object getName(final EObject result) {
        if (result instanceof DeployedOperation) {
            final DeployedOperation operation = (DeployedOperation) result;
            return String.format("%s::%s::[%s]%s", this.getName(operation.getComponent().getContext()),
                    this.getName(operation.getComponent()), result.getClass().getSimpleName(),
                    this.getName(operation.getAssemblyOperation().getOperationType()));

        }
        final EClass clazz = result.eClass();
        for (final EAttribute attribute : clazz.getEAllAttributes()) {
            if ("signature".equals(attribute.getName())) {
                return "signature " + result.eGet(attribute);
            } else if ("name".equals(attribute.getName())) {
                return "name " + result.eGet(attribute);
            }
        }
        return result.toString();
    }
}
