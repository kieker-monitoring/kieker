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
package kieker.tools.sar.stages.dataflow;

import java.time.Duration;
import java.util.Optional;

import kieker.analysis.architecture.recovery.events.DataflowEvent;
import kieker.analysis.architecture.recovery.events.OperationEvent;
import kieker.analysis.architecture.recovery.events.StorageEvent;
import kieker.model.analysismodel.type.ComponentType;
import kieker.model.analysismodel.type.TypeModel;
import kieker.tools.sar.StorageOperationDataflow;

import teetime.framework.AbstractStage;
import teetime.framework.InputPort;
import teetime.framework.OutputPort;

import org.oceandsl.analysis.code.CodeUtils;
import org.oceandsl.analysis.code.stages.data.DataflowEntry;

/**
 * Transform @{link CallerCallee}s to @{link OperationEvent}s and @{CallEvent}s on model level. The
 * stage outputs two @{link OperationEvent}s and one @{link CallEvent}s for each @{CallerCallee}
 * event. It is used to convert static caller-callee data to operation and call data compatible with
 * the dynamic architecture reconstruction.
 *
 * @author Reiner Jung
 * @since 1.1
 */
public class ElementAndDataflow4StaticDataStage extends AbstractStage {

    private final InputPort<DataflowEntry> callerCalleeDataflowInputPort = this.createInputPort(DataflowEntry.class);

    private final InputPort<StorageOperationDataflow> storageOperationDataflowInputPort = this
            .createInputPort(StorageOperationDataflow.class);

    private final OutputPort<OperationEvent> operationOutputPort = this.createOutputPort(OperationEvent.class);

    private final OutputPort<DataflowEvent> dataflowOutputPort = this.createOutputPort(DataflowEvent.class);

    private final String hostname;

    private final TypeModel typeModel;

    public ElementAndDataflow4StaticDataStage(final String hostname, final TypeModel typeModel) {
        this.hostname = hostname;
        this.typeModel = typeModel;
    }

    @Override
    protected void execute() throws Exception {
        final DataflowEntry callerCalleeDataflow = this.callerCalleeDataflowInputPort.receive();
        final StorageOperationDataflow storageOperationDataflow = this.storageOperationDataflowInputPort.receive();

        if (callerCalleeDataflow != null) {
            final OperationEvent source = new OperationEvent(this.hostname, callerCalleeDataflow.getSourceModule(),
                    callerCalleeDataflow.getSourceOperation());
            this.operationOutputPort.send(source);
            final OperationEvent target = new OperationEvent(this.hostname, callerCalleeDataflow.getTargetModule(),
                    callerCalleeDataflow.getTargetOperation());
            this.operationOutputPort.send(target);
            final DataflowEvent dataflowEvent = new DataflowEvent(source, target, callerCalleeDataflow.getDirection(),
                    Duration.ZERO);

            this.dataflowOutputPort.send(dataflowEvent);
        }

        if (storageOperationDataflow != null) {
            final OperationEvent operation = new OperationEvent(this.hostname, storageOperationDataflow.getModuleName(),
                    storageOperationDataflow.getOperationName());
            final StorageEvent storage = new StorageEvent(this.hostname,
                    this.lookupComponentSignature(storageOperationDataflow.getCommonBlockName()),
                    storageOperationDataflow.getCommonBlockName(), "unknown");
            final DataflowEvent dataflowEvent = new DataflowEvent(operation, storage,
                    storageOperationDataflow.getDirection(), Duration.ZERO);

            this.dataflowOutputPort.send(dataflowEvent);
        }

    }

    private String lookupComponentSignature(final String storageName) {
        final Optional<ComponentType> componentOptional = this.typeModel.getComponentTypes().values().stream()
                .filter(type -> type.getProvidedStorages().containsKey(storageName)).findFirst();
        if (componentOptional.isPresent()) {
            return componentOptional.get().getSignature();
        } else {
            return CodeUtils.UNKNOWN_COMPONENT;
        }
    }

    public InputPort<DataflowEntry> getCallerCalleeDataflowInputPort() {
        return this.callerCalleeDataflowInputPort;
    }

    public InputPort<StorageOperationDataflow> getStorageOperationDataflowInputPort() {
        return this.storageOperationDataflowInputPort;
    }

    public OutputPort<OperationEvent> getOperationOutputPort() {
        return this.operationOutputPort;
    }

    public OutputPort<DataflowEvent> getDataflowOutputPort() {
        return this.dataflowOutputPort;
    }
}
