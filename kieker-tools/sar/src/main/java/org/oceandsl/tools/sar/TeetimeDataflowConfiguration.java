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
package org.oceandsl.tools.sar;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;

import kieker.analysis.architecture.recovery.ModelAssemblerStage;
import kieker.analysis.architecture.recovery.assembler.OperationAssemblyModelAssembler;
import kieker.analysis.architecture.recovery.assembler.OperationDeploymentModelAssembler;
import kieker.analysis.architecture.recovery.assembler.OperationTypeModelAssembler;
import kieker.analysis.architecture.recovery.events.DataflowEvent;
import kieker.analysis.architecture.recovery.events.OperationEvent;
import kieker.analysis.architecture.recovery.signature.IComponentSignatureExtractor;
import kieker.analysis.architecture.recovery.signature.IOperationSignatureExtractor;
import kieker.analysis.architecture.repository.ModelRepository;
import kieker.model.analysismodel.assembly.AssemblyPackage;
import kieker.model.analysismodel.deployment.DeploymentPackage;
import kieker.model.analysismodel.execution.ExecutionPackage;
import kieker.model.analysismodel.source.SourcePackage;
import kieker.model.analysismodel.statistics.StatisticsPackage;
import kieker.model.analysismodel.type.ComponentType;
import kieker.model.analysismodel.type.OperationType;
import kieker.model.analysismodel.type.TypePackage;

import teetime.framework.Configuration;

import org.oceandsl.analysis.code.stages.data.DataflowEntry;
import org.oceandsl.analysis.generic.EModuleMode;
import org.oceandsl.analysis.generic.source.CsvRowReaderProducerStage;
import org.oceandsl.tools.sar.signature.processor.AbstractSignatureProcessor;
import org.oceandsl.tools.sar.signature.processor.FileBasedSignatureProcessor;
import org.oceandsl.tools.sar.signature.processor.MapBasedSignatureProcessor;
import org.oceandsl.tools.sar.signature.processor.ModuleBasedSignatureProcessor;
import org.oceandsl.tools.sar.stages.DataflowConstraintStage;
import org.oceandsl.tools.sar.stages.dataflow.CleanupDataflowComponentSignatureStage;
import org.oceandsl.tools.sar.stages.dataflow.CountUniqueDataflowCallsStage;
import org.oceandsl.tools.sar.stages.dataflow.DataflowExecutionModelAssembler;
import org.oceandsl.tools.sar.stages.dataflow.ElementAndDataflow4StaticDataStage;

/**
 * Pipe and Filter configuration for the architecture creation tool.
 *
 * @author Reiner Jung
 * @since 1.1
 */
public class TeetimeDataflowConfiguration extends Configuration {

    public TeetimeDataflowConfiguration(final Logger logger, final Settings settings, final ModelRepository repository)
            throws IOException {

        final Path callerCalleeDataflowPath = settings.getInputFile()
                .resolve(StaticArchitectureRecoveryMain.CALLER_CALLEE_DATAFLOW_FILENAME);
        final Path storageDataflowPath = settings.getInputFile()
                .resolve(StaticArchitectureRecoveryMain.STORAGE_DATAFLOW_FILENAME);

        final CsvRowReaderProducerStage<DataflowEntry> callerCalleeDataflowReader = new CsvRowReaderProducerStage<>(
                callerCalleeDataflowPath, settings.getSplitSymbol(), '"', '\\', true, DataflowEntry.class);

        final CsvRowReaderProducerStage<StorageOperationDataflow> storageOperationDataflowReader = new CsvRowReaderProducerStage<>(
                storageDataflowPath, settings.getSplitSymbol(), '"', '\\', true, StorageOperationDataflow.class);

        final ElementAndDataflow4StaticDataStage elementAndDataflow4StaticDataStage = new ElementAndDataflow4StaticDataStage(
                settings.getHostname(), repository.getModel(TypePackage.Literals.TYPE_MODEL));
        elementAndDataflow4StaticDataStage.declareActive();

        final CleanupDataflowComponentSignatureStage cleanupComponentSignatureStage = new CleanupDataflowComponentSignatureStage(
                this.createProcessors(settings.getModuleModes(), settings, logger));

        final DataflowConstraintStage dataflowConstraintStage = new DataflowConstraintStage();

        /** -- operation -- */
        final ModelAssemblerStage<OperationEvent> operationTypeModelAssemblerStage = new ModelAssemblerStage<>(
                new OperationTypeModelAssembler(repository.getModel(TypePackage.Literals.TYPE_MODEL),
                        repository.getModel(SourcePackage.Literals.SOURCE_MODEL), settings.getSourceLabel(),
                        this.createComponentSignatureExtractor(settings), this.createOperationSignatureExtractor()));
        final ModelAssemblerStage<OperationEvent> operationAssemblyModelAssemblerStage = new ModelAssemblerStage<>(
                new OperationAssemblyModelAssembler(repository.getModel(TypePackage.Literals.TYPE_MODEL),
                        repository.getModel(AssemblyPackage.Literals.ASSEMBLY_MODEL),
                        repository.getModel(SourcePackage.Literals.SOURCE_MODEL), settings.getSourceLabel()));
        final ModelAssemblerStage<OperationEvent> operationDeploymentModelAssemblerStage = new ModelAssemblerStage<>(
                new OperationDeploymentModelAssembler(repository.getModel(AssemblyPackage.Literals.ASSEMBLY_MODEL),
                        repository.getModel(DeploymentPackage.Literals.DEPLOYMENT_MODEL),
                        repository.getModel(SourcePackage.Literals.SOURCE_MODEL), settings.getSourceLabel()));

        /** -- dataflow -- */
        final ModelAssemblerStage<DataflowEvent> executionModelDataflowGenerationStage = new ModelAssemblerStage<>(
                new DataflowExecutionModelAssembler(repository.getModel(ExecutionPackage.Literals.EXECUTION_MODEL),
                        repository.getModel(DeploymentPackage.Literals.DEPLOYMENT_MODEL),
                        repository.getModel(SourcePackage.Literals.SOURCE_MODEL), settings.getSourceLabel(), logger));
        final CountUniqueDataflowCallsStage countUniqueDataflowCalls = new CountUniqueDataflowCallsStage(
                repository.getModel(StatisticsPackage.Literals.STATISTICS_MODEL),
                repository.getModel(ExecutionPackage.Literals.EXECUTION_MODEL));

        /** connecting ports. */
        this.connectPorts(callerCalleeDataflowReader.getOutputPort(), cleanupComponentSignatureStage.getInputPort());
        this.connectPorts(cleanupComponentSignatureStage.getOutputPort(),
                elementAndDataflow4StaticDataStage.getCallerCalleeDataflowInputPort());
        this.connectPorts(storageOperationDataflowReader.getOutputPort(),
                elementAndDataflow4StaticDataStage.getStorageOperationDataflowInputPort());

        /** -- operation - */
        this.connectPorts(elementAndDataflow4StaticDataStage.getOperationOutputPort(),
                operationTypeModelAssemblerStage.getInputPort());
        this.connectPorts(operationTypeModelAssemblerStage.getOutputPort(),
                operationAssemblyModelAssemblerStage.getInputPort());
        this.connectPorts(operationAssemblyModelAssemblerStage.getOutputPort(),
                operationDeploymentModelAssemblerStage.getInputPort());

        /** -- dataflow - */
        this.connectPorts(elementAndDataflow4StaticDataStage.getDataflowOutputPort(),
                dataflowConstraintStage.getInputPort());
        this.connectPorts(operationDeploymentModelAssemblerStage.getOutputPort(),
                dataflowConstraintStage.getControlInputPort());
        this.connectPorts(dataflowConstraintStage.getOutputPort(),
                executionModelDataflowGenerationStage.getInputPort());
        this.connectPorts(executionModelDataflowGenerationStage.getOutputPort(),
                countUniqueDataflowCalls.getInputPort());
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
