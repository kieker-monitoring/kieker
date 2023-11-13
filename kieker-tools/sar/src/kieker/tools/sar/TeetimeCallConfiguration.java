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
package kieker.tools.sar; // NOPMD ExecessiveImports

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;

import kieker.analysis.architecture.recovery.CallEvent2OperationCallStage;
import kieker.analysis.architecture.recovery.CountUniqueCallsStage;
import kieker.analysis.architecture.recovery.ModelAssemblerStage;
import kieker.analysis.architecture.recovery.assembler.InvocationExecutionModelAssembler;
import kieker.analysis.architecture.recovery.assembler.OperationAssemblyModelAssembler;
import kieker.analysis.architecture.recovery.assembler.OperationDeploymentModelAssembler;
import kieker.analysis.architecture.recovery.assembler.OperationTypeModelAssembler;
import kieker.analysis.architecture.recovery.events.DeployedOperationCallEvent;
import kieker.analysis.architecture.recovery.events.OperationEvent;
import kieker.analysis.architecture.recovery.signature.IComponentSignatureExtractor;
import kieker.analysis.architecture.recovery.signature.IOperationSignatureExtractor;
import kieker.analysis.architecture.repository.ModelRepository;
import kieker.analysis.code.data.CallerCalleeEntry;
import kieker.analysis.generic.EModuleMode;
import kieker.analysis.generic.sink.StringFileWriterSink;
import kieker.analysis.generic.source.CsvRowReaderProducerStage;
import kieker.model.analysismodel.assembly.AssemblyPackage;
import kieker.model.analysismodel.deployment.DeploymentPackage;
import kieker.model.analysismodel.execution.ExecutionPackage;
import kieker.model.analysismodel.source.SourcePackage;
import kieker.model.analysismodel.statistics.StatisticsPackage;
import kieker.model.analysismodel.type.ComponentType;
import kieker.model.analysismodel.type.OperationType;
import kieker.model.analysismodel.type.TypePackage;
import kieker.tools.sar.signature.processor.AbstractSignatureProcessor;
import kieker.tools.sar.signature.processor.FileBasedSignatureProcessor;
import kieker.tools.sar.signature.processor.MapBasedSignatureProcessor;
import kieker.tools.sar.signature.processor.ModuleBasedSignatureProcessor;
import kieker.tools.sar.stages.calls.CleanupComponentSignatureStage;
import kieker.tools.sar.stages.calls.OperationAndCall4StaticDataStage;

import teetime.framework.Configuration;

/**
 * Pipe and Filter configuration for the architecture creation tool.
 *
 * @author Reiner Jung
 * @since 2.0.0
 */
public class TeetimeCallConfiguration extends Configuration {

	public TeetimeCallConfiguration(final Logger logger, final Settings settings, final ModelRepository repository)
			throws IOException {
		super();
		final Path inputCallPath = settings.getInputFile().resolve(StaticArchitectureRecoveryMain.CALLTABLE_FILENAME);

		final CsvRowReaderProducerStage<CallerCalleeEntry> readCallsCsvStage = new CsvRowReaderProducerStage<>(
				inputCallPath, settings.getSplitSymbol(), '"', '\\', true, CallerCalleeEntry.class);

		final CleanupComponentSignatureStage cleanupComponentSignatureStage = new CleanupComponentSignatureStage(
				this.createProcessors(settings.getModuleModes(), settings, logger));

		final StringFileWriterSink errorMessageSink;
		if (settings.getMissingMappingsFile() != null) {
			errorMessageSink = new StringFileWriterSink(settings.getMissingMappingsFile());
		} else {
			errorMessageSink = null;
		}

		final OperationAndCall4StaticDataStage operationAndCallStage = new OperationAndCall4StaticDataStage(
				settings.getHostname());

		/** -- call based modeling -- */
		final ModelAssemblerStage<OperationEvent> typeModelAssemblerStage = new ModelAssemblerStage<>(
				new OperationTypeModelAssembler(repository.getModel(TypePackage.Literals.TYPE_MODEL),
						repository.getModel(SourcePackage.Literals.SOURCE_MODEL), settings.getSourceLabel(),
						this.createComponentSignatureExtractor(settings), this.createOperationSignatureExtractor()));
		final ModelAssemblerStage<OperationEvent> assemblyModelAssemblerStage = new ModelAssemblerStage<>(
				new OperationAssemblyModelAssembler(repository.getModel(TypePackage.Literals.TYPE_MODEL),
						repository.getModel(AssemblyPackage.Literals.ASSEMBLY_MODEL),
						repository.getModel(SourcePackage.Literals.SOURCE_MODEL), settings.getSourceLabel()));
		final ModelAssemblerStage<OperationEvent> deploymentModelAssemblerStage = new ModelAssemblerStage<>(
				new OperationDeploymentModelAssembler(repository.getModel(AssemblyPackage.Literals.ASSEMBLY_MODEL),
						repository.getModel(DeploymentPackage.Literals.DEPLOYMENT_MODEL),
						repository.getModel(SourcePackage.Literals.SOURCE_MODEL), settings.getSourceLabel()));

		final CallEvent2OperationCallStage callEvent2OperationCallStage = new CallEvent2OperationCallStage(
				repository.getModel(DeploymentPackage.Literals.DEPLOYMENT_MODEL));

		final ModelAssemblerStage<DeployedOperationCallEvent> executionModelGenerationStage = new ModelAssemblerStage<>(
				new InvocationExecutionModelAssembler(repository.getModel(ExecutionPackage.Literals.EXECUTION_MODEL),
						repository.getModel(SourcePackage.Literals.SOURCE_MODEL), settings.getSourceLabel()));

		final CountUniqueCallsStage countUniqueCalls = new CountUniqueCallsStage(
				repository.getModel(StatisticsPackage.Literals.STATISTICS_MODEL),
				repository.getModel(ExecutionPackage.Literals.EXECUTION_MODEL));

		/** connecting ports. */
		this.connectPorts(readCallsCsvStage.getOutputPort(), cleanupComponentSignatureStage.getInputPort());
		this.connectPorts(cleanupComponentSignatureStage.getOutputPort(), operationAndCallStage.getInputPort());
		if (errorMessageSink != null) {
			this.connectPorts(cleanupComponentSignatureStage.getErrorMessageOutputPort(),
					errorMessageSink.getInputPort());
		}
		this.connectPorts(operationAndCallStage.getOperationOutputPort(), typeModelAssemblerStage.getInputPort());
		this.connectPorts(typeModelAssemblerStage.getOutputPort(), assemblyModelAssemblerStage.getInputPort());
		this.connectPorts(assemblyModelAssemblerStage.getOutputPort(), deploymentModelAssemblerStage.getInputPort());

		this.connectPorts(operationAndCallStage.getCallOutputPort(), callEvent2OperationCallStage.getInputPort());
		this.connectPorts(callEvent2OperationCallStage.getOutputPort(), executionModelGenerationStage.getInputPort());

		this.connectPorts(executionModelGenerationStage.getOutputPort(), countUniqueCalls.getInputPort());
	}

	private List<AbstractSignatureProcessor> createProcessors(final List<EModuleMode> modes, final Settings settings,
			final Logger logger) throws IOException {
		final List<AbstractSignatureProcessor> processors = new ArrayList<>();

		for (final EModuleMode mode : modes) {
			switch (mode) {
			case MAP_MODE:
				processors.add(this.createMapBasedProcessor(logger, settings));
				break;
			case MODULE_MODE:
				processors.add(this.createModuleBasedProcessor(logger));
				break;
			case JAVA_CLASS_MODE:
				break;
			case PYTHON_CLASS_MODE:
				break;
			case FILE_MODE:
			default:
				processors.add(this.createFileBasedProcessor(logger));
				break;
			}
		}

		return processors;
	}

	private AbstractSignatureProcessor createModuleBasedProcessor(final Logger logger) {
		logger.info("Module based component definition");
		return new ModuleBasedSignatureProcessor(false);
	}

	private AbstractSignatureProcessor createFileBasedProcessor(final Logger logger) {
		logger.info("File based component definition");
		return new FileBasedSignatureProcessor(false);
	}

	private AbstractSignatureProcessor createMapBasedProcessor(final Logger logger, final Settings settings)
			throws IOException {
		if (settings.getComponentMapFiles() != null) {
			logger.info("Map based component definition");
			return new MapBasedSignatureProcessor(settings.getComponentMapFiles(), false,
					String.valueOf(settings.getSplitSymbol()));
		} else {
			logger.error("Missing map files for component identification.");
			return null;
		}
	}

	private IComponentSignatureExtractor createComponentSignatureExtractor(final Settings settings) {
		return new IComponentSignatureExtractor() {

			@Override
			public void extract(final ComponentType componentType) {
				String signature = componentType.getSignature();
				if (signature == null) {
					signature = "-- none --";
				}
				final Path path = Paths.get(signature);
				final String name = path.getName(path.getNameCount() - 1).toString();
				final String rest = path.getParent() == null ? settings.getExperimentName()
						: settings.getExperimentName() + "." + path.getParent().toString();
				componentType.setName(name);
				componentType.setPackage(rest);
			}
		};
	}

	private IOperationSignatureExtractor createOperationSignatureExtractor() {
		return new IOperationSignatureExtractor() {

			@Override
			public void extract(final OperationType operationType) {
				final String name = operationType.getSignature();
				operationType.setName(name);
				operationType.setReturnType("unknown");
			}

		};
	}
}
