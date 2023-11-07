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
package org.oceandsl.tools.maa;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import kieker.analysis.architecture.repository.ModelRepository;

import teetime.framework.Configuration;
import teetime.framework.OutputPort;
import teetime.stage.basic.distributor.Distributor;
import teetime.stage.basic.distributor.strategy.CopyByReferenceStrategy;

import org.oceandsl.analysis.architecture.stages.ModelChangeNameStage;
import org.oceandsl.analysis.architecture.stages.ModelRepositoryProducerStage;
import org.oceandsl.analysis.architecture.stages.ModelSink;
import org.oceandsl.analysis.generic.stages.TableCsvSink;
import org.oceandsl.tools.maa.stages.CallEntry;
import org.oceandsl.tools.maa.stages.CollectConnectionsStage;
import org.oceandsl.tools.maa.stages.ComponentStatistics;
import org.oceandsl.tools.maa.stages.ComponentStatisticsStage;
import org.oceandsl.tools.maa.stages.FindDistinctCollectionsStage;
import org.oceandsl.tools.maa.stages.GenerateProvidedInterfacesStage;
import org.oceandsl.tools.maa.stages.GroupComponentsHierarchicallyStage;
import org.oceandsl.tools.maa.stages.OperationCallsStage;
import org.oceandsl.tools.maa.stages.ProvidedInterfaceEntry;
import org.oceandsl.tools.maa.stages.ProvidedInterfaceTableTransformation;

/**
 * @author Reiner Jung
 * @since 1.2
 */
public class TeetimeConfiguration extends Configuration {

    private static final Logger LOGGER = LoggerFactory.getLogger(TeetimeConfiguration.class);

    public TeetimeConfiguration(final Settings settings) {
        final ModelRepositoryProducerStage modelReader = new ModelRepositoryProducerStage(settings.getInputModelPath());

        final Distributor<ModelRepository> distributor = new Distributor<>(new CopyByReferenceStrategy());

        final ModelChangeNameStage modelChangeNameStage = new ModelChangeNameStage(settings.getExperimentName());
        final ModelSink modelSink = new ModelSink(settings.getOutputModelPath());

        final ProvidedInterfaceTableTransformation providedInterfaceTableTransformation = new ProvidedInterfaceTableTransformation();
        final TableCsvSink<String, ProvidedInterfaceEntry> providedInterfaceSink = new TableCsvSink<>(
                settings.getOutputModelPath(), "provided-interfaces.csv", ProvidedInterfaceEntry.class, true,
                settings.getLineSeparator());

        OutputPort<ModelRepository> outputPort = modelReader.getOutputPort();
        if (settings.isComputeInterfaces()) {
            final CollectConnectionsStage computeInterfaces = new CollectConnectionsStage();
            final FindDistinctCollectionsStage findDistinctCollectionsStage = new FindDistinctCollectionsStage();
            final GenerateProvidedInterfacesStage generateProvidedInterfacesStage = new GenerateProvidedInterfacesStage();
            // final AbstractMergeInterfaceStage mergeInterfaceStage = new
            // SimilarMethodSuffixMergeInterfaceStage(0.4);

            this.connectPorts(outputPort, computeInterfaces.getInputPort());
            this.connectPorts(computeInterfaces.getOutputPort(), findDistinctCollectionsStage.getInputPort());
            this.connectPorts(findDistinctCollectionsStage.getOutputPort(),
                    generateProvidedInterfacesStage.getInputPort());
            // this.connectPorts(generateProvidedInterfacesStage.getOutputPort(),
            // mergeInterfaceStage.getInputPort());

            // outputPort = mergeInterfaceStage.getOutputPort();
            outputPort = generateProvidedInterfacesStage.getOutputPort();
        }

        final boolean mapFiles = settings.getMapFiles() != null && settings.getMapFiles().size() > 0;
        if (mapFiles) {
            try {
                final GroupComponentsHierarchicallyStage groupComponentHierarchicallyStage = new GroupComponentsHierarchicallyStage(
                        settings.getMapFiles(), settings.getSeparator(), true);
                this.connectPorts(outputPort, groupComponentHierarchicallyStage.getInputPort());
                outputPort = groupComponentHierarchicallyStage.getOutputPort();
            } catch (final IOException ex) {
                TeetimeConfiguration.LOGGER.error("Error reading map files");
            }
        }

        this.connectPorts(outputPort, distributor.getInputPort());

        if (settings.isComputeInterfaces() || mapFiles) {
            this.connectPorts(distributor.getNewOutputPort(), modelChangeNameStage.getInputPort());
            this.connectPorts(modelChangeNameStage.getOutputPort(), modelSink.getInputPort());
            this.connectPorts(distributor.getNewOutputPort(), providedInterfaceTableTransformation.getInputPort());
            this.connectPorts(providedInterfaceTableTransformation.getOutputPort(),
                    providedInterfaceSink.getInputPort());
        }

        if (settings.isOperationCalls()) {
            final OperationCallsStage operationCallsStage = new OperationCallsStage();
            final TableCsvSink<String, CallEntry> operationCallSink = new TableCsvSink<>(settings.getOutputModelPath(),
                    "operation-calls.csv", CallEntry.class, true, settings.getLineSeparator());

            this.connectPorts(distributor.getNewOutputPort(), operationCallsStage.getInputPort());
            this.connectPorts(operationCallsStage.getOutputPort(), operationCallSink.getInputPort());
        }

        if (settings.isComponentStatistics()) {
            final ComponentStatisticsStage componentStatisticsStage = new ComponentStatisticsStage();
            final TableCsvSink<String, ComponentStatistics> operationCallSink = new TableCsvSink<>(
                    settings.getOutputModelPath(), "component-statistics.csv", ComponentStatistics.class, true,
                    settings.getLineSeparator());

            this.connectPorts(distributor.getNewOutputPort(), componentStatisticsStage.getInputPort());
            this.connectPorts(componentStatisticsStage.getOutputPort(), operationCallSink.getInputPort());
        }
    }

}
