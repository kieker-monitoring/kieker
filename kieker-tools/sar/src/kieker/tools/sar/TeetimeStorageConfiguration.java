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
package kieker.tools.sar;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;

import kieker.analysis.architecture.recovery.ModelAssemblerStage;
import kieker.analysis.architecture.recovery.assembler.StorageAssemblyModelAssembler;
import kieker.analysis.architecture.recovery.assembler.StorageDeploymentModelAssembler;
import kieker.analysis.architecture.recovery.assembler.StorageTypeModelAssembler;
import kieker.analysis.architecture.recovery.events.StorageEvent;
import kieker.analysis.architecture.recovery.signature.IComponentSignatureExtractor;
import kieker.analysis.architecture.recovery.signature.IStorageSignatureExtractor;
import kieker.analysis.architecture.repository.ModelRepository;
import kieker.model.analysismodel.assembly.AssemblyPackage;
import kieker.model.analysismodel.deployment.DeploymentPackage;
import kieker.model.analysismodel.source.SourcePackage;
import kieker.model.analysismodel.type.ComponentType;
import kieker.model.analysismodel.type.StorageType;
import kieker.model.analysismodel.type.TypePackage;
import kieker.tools.sar.signature.processor.AbstractSignatureProcessor;
import kieker.tools.sar.signature.processor.FileBasedSignatureProcessor;
import kieker.tools.sar.signature.processor.MapBasedSignatureProcessor;
import kieker.tools.sar.signature.processor.ModuleBasedSignatureProcessor;
import kieker.tools.sar.stages.dataflow.CleanupStorageComponentSignatureStage;
import kieker.tools.sar.stages.dataflow.StorageToStorageEventStage;

import teetime.framework.Configuration;

import org.oceandsl.analysis.code.stages.data.GlobalDataEntry;
import org.oceandsl.analysis.generic.EModuleMode;
import org.oceandsl.analysis.generic.source.CsvRowReaderProducerStage;

/**
 * Pipe and Filter configuration for the architecture creation tool.
 *
 * @author Reiner Jung
 * @since 1.1
 */
public class TeetimeStorageConfiguration extends Configuration {

    public TeetimeStorageConfiguration(final Logger logger, final Settings settings, final ModelRepository repository)
            throws IOException {

        final Path storagePath = settings.getInputFile().resolve(StaticArchitectureRecoveryMain.STORAGE_FILENAME);

        final CsvRowReaderProducerStage<GlobalDataEntry> storagesReader = new CsvRowReaderProducerStage<>(storagePath,
                settings.getSplitSymbol(), '"', '\\', true, GlobalDataEntry.class);

        final CleanupStorageComponentSignatureStage cleanupComponentSignatureStage = new CleanupStorageComponentSignatureStage(
                this.createProcessors(settings.getModuleModes(), settings, logger));

        final StorageToStorageEventStage storageToStorageEventStage = new StorageToStorageEventStage(
                settings.getHostname());

        /** -- storage -- */
        final ModelAssemblerStage<StorageEvent> storageTypeModelAssemblerStage = new ModelAssemblerStage<>(
                new StorageTypeModelAssembler(repository.getModel(TypePackage.Literals.TYPE_MODEL),
                        repository.getModel(SourcePackage.Literals.SOURCE_MODEL), settings.getSourceLabel(),
                        this.createComponentSignatureExtractor(settings), this.createStorageSignatureExtractor()));
        final ModelAssemblerStage<StorageEvent> storageAssemblyModelAssemblerStage = new ModelAssemblerStage<>(
                new StorageAssemblyModelAssembler(repository.getModel(TypePackage.Literals.TYPE_MODEL),
                        repository.getModel(AssemblyPackage.Literals.ASSEMBLY_MODEL),
                        repository.getModel(SourcePackage.Literals.SOURCE_MODEL), settings.getSourceLabel()));
        final ModelAssemblerStage<StorageEvent> storageDeploymentModelAssemblerStage = new ModelAssemblerStage<>(
                new StorageDeploymentModelAssembler(repository.getModel(AssemblyPackage.Literals.ASSEMBLY_MODEL),
                        repository.getModel(DeploymentPackage.Literals.DEPLOYMENT_MODEL),
                        repository.getModel(SourcePackage.Literals.SOURCE_MODEL), settings.getSourceLabel()));

        /** connecting ports. */
        this.connectPorts(storagesReader.getOutputPort(), cleanupComponentSignatureStage.getInputPort());
        this.connectPorts(cleanupComponentSignatureStage.getOutputPort(), storageToStorageEventStage.getInputPort());
        this.connectPorts(storageToStorageEventStage.getOutputPort(), storageTypeModelAssemblerStage.getInputPort());

        this.connectPorts(storageTypeModelAssemblerStage.getOutputPort(),
                storageAssemblyModelAssemblerStage.getInputPort());
        this.connectPorts(storageAssemblyModelAssemblerStage.getOutputPort(),
                storageDeploymentModelAssemblerStage.getInputPort());

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

    private IStorageSignatureExtractor createStorageSignatureExtractor() {
        return new IStorageSignatureExtractor() {

            @Override
            public void extract(final StorageType storageType) {
                final String name = storageType.getName();
                storageType.setName(name);
            }

        };
    }

}
