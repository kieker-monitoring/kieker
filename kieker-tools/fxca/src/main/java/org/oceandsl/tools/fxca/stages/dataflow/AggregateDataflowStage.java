/***************************************************************************
 * Copyright 2023 Kieker Project (http://kieker-monitoring.net)
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
package org.oceandsl.tools.fxca.stages.dataflow;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import teetime.framework.AbstractConsumerStage;
import teetime.framework.OutputPort;

import org.oceandsl.analysis.code.CodeUtils;
import org.oceandsl.tools.fxca.stages.dataflow.data.CallerCalleeDataflow;
import org.oceandsl.tools.fxca.stages.dataflow.data.CommonBlockArgumentDataflow;
import org.oceandsl.tools.fxca.stages.dataflow.data.IDataflowEntry;

/**
 * @author Reiner Jung
 * @since 1.3.0
 *
 */
public class AggregateDataflowStage extends AbstractConsumerStage<IDataflowEntry> {

    private final OutputPort<CallerCalleeDataflow> callerCalleeDataflowOutputPort = this
            .createOutputPort(CallerCalleeDataflow.class);
    private final OutputPort<CommonBlockArgumentDataflow> commonBlockDataflowOutputPort = this
            .createOutputPort(CommonBlockArgumentDataflow.class);

    private final List<CallerCalleeDataflow> callerCalleeDataflows = new ArrayList<>();
    private final List<CommonBlockArgumentDataflow> commonBlockDataflows = new ArrayList<>();

    @Override
    protected void execute(final IDataflowEntry entry) throws Exception {
        if (entry instanceof CallerCalleeDataflow) {
            this.insertOrMergeDuplicate((CallerCalleeDataflow) entry);
        } else if (entry instanceof CommonBlockArgumentDataflow) {
            this.insertOrMergeDuplicate((CommonBlockArgumentDataflow) entry);
        }
    }

    @Override
    protected void onTerminating() {
        this.callerCalleeDataflows.forEach(cc -> this.callerCalleeDataflowOutputPort.send(cc));
        this.commonBlockDataflows.forEach(cc -> this.commonBlockDataflowOutputPort.send(cc));
        super.onTerminating();
    }

    public OutputPort<CallerCalleeDataflow> getCallerCalleeDataflowOutputPort() {
        return this.callerCalleeDataflowOutputPort;
    }

    public OutputPort<CommonBlockArgumentDataflow> getCommonBlockDataflowOutputPort() {
        return this.commonBlockDataflowOutputPort;
    }

    private void insertOrMergeDuplicate(final CallerCalleeDataflow callerCalleeDataflow) {
        final Optional<CallerCalleeDataflow> ccOptional = this.callerCalleeDataflows.stream()
                .filter(cc -> cc.getSourceFileName().equals(callerCalleeDataflow.getSourceFileName())
                        && cc.getSourceModuleName().equals(callerCalleeDataflow.getSourceModuleName())
                        && cc.getSourceOperationName().equals(callerCalleeDataflow.getSourceOperationName())
                        && cc.getTargetFileName().equals(callerCalleeDataflow.getTargetFileName())
                        && cc.getTargetModuleName().equals(callerCalleeDataflow.getTargetModuleName())
                        && cc.getTargetOperatioName().equals(callerCalleeDataflow.getTargetOperatioName()))
                .findFirst();
        if (ccOptional.isPresent()) {
            ccOptional.get().setDirection(
                    CodeUtils.merge(ccOptional.get().getDirection(), callerCalleeDataflow.getDirection()));
        } else {
            this.callerCalleeDataflows.add(callerCalleeDataflow);
        }
    }

    private void insertOrMergeDuplicate(final CommonBlockArgumentDataflow commonBlockDataflow) {
        final Optional<CommonBlockArgumentDataflow> cbOptional = this.commonBlockDataflows.stream()
                .filter(cb -> cb.getFileName().equals(commonBlockDataflow.getFileName())
                        && cb.getModuleName().equals(commonBlockDataflow.getModuleName())
                        && cb.getOperationName().equals(commonBlockDataflow.getOperationName())
                        && cb.getCommonBlockName().equals(commonBlockDataflow.getCommonBlockName()))
                .findFirst();
        if (cbOptional.isPresent()) {
            cbOptional.get()
                    .setDirection(CodeUtils.merge(cbOptional.get().getDirection(), commonBlockDataflow.getDirection()));
        } else {
            this.commonBlockDataflows.add(commonBlockDataflow);
        }
    }
}
