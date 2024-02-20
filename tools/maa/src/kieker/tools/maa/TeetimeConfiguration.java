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
package kieker.tools.maa;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import kieker.analysis.architecture.ModelChangeNameStage;
import kieker.analysis.architecture.ModelRepositoryProducerStage;
import kieker.analysis.architecture.ModelSink;
import kieker.analysis.architecture.repository.ModelRepository;
import kieker.analysis.generic.sink.TableCsvSink;
import kieker.tools.maa.stages.CallEntry;
import kieker.tools.maa.stages.CollectInterModuleCallsStage;
import kieker.tools.maa.stages.ComponentStatistics;
import kieker.tools.maa.stages.ComponentStatisticsStage;
import kieker.tools.maa.stages.FindDistinctCollectionsStage;
import kieker.tools.maa.stages.GenerateProvidedInterfacesStage;
import kieker.tools.maa.stages.GroupComponentsHierarchicallyStage;
import kieker.tools.maa.stages.OperationCallsStage;
import kieker.tools.maa.stages.ProvidedInterfaceEntry;
import kieker.tools.maa.stages.ProvidedInterfaceTableTransformation;

import teetime.framework.Configuration;
import teetime.framework.OutputPort;
import teetime.stage.basic.distributor.Distributor;
import teetime.stage.basic.distributor.strategy.CopyByReferenceStrategy;

/**
 * @author Reiner Jung
 * @since 2.0.0
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
			final CollectInterModuleCallsStage collectInterModuleCalls = new CollectInterModuleCallsStage();
			final FindDistinctCollectionsStage findDistinctCollectionsStage = new FindDistinctCollectionsStage();
			final GenerateProvidedInterfacesStage generateProvidedInterfacesStage = new GenerateProvidedInterfacesStage();
			// final AbstractMergeInterfaceStage mergeInterfaceStage = new
			// SimilarMethodSuffixMergeInterfaceStage(0.4);

			this.connectPorts(outputPort, collectInterModuleCalls.getInputPort());
			this.connectPorts(collectInterModuleCalls.getOutputPort(), findDistinctCollectionsStage.getInputPort());
			this.connectPorts(findDistinctCollectionsStage.getOutputPort(),
					generateProvidedInterfacesStage.getInputPort());
			// this.connectPorts(generateProvidedInterfacesStage.getOutputPort(),
			// mergeInterfaceStage.getInputPort());

			// outputPort = mergeInterfaceStage.getOutputPort();
			outputPort = generateProvidedInterfacesStage.getOutputPort();
		}

		final boolean mapFiles = (settings.getMapFiles() != null) && (settings.getMapFiles().size() > 0);
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
