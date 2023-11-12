/***************************************************************************
 * Copyright (C) 2023 OceanDSL (https://oceandsl.uni-kiel.de)
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

import teetime.stage.basic.AbstractTransformation;

import org.oceandsl.analysis.generic.Table;
import org.oceandsl.tools.fxca.stages.dataflow.data.CommonBlockArgumentDataflow;

/**
 *
 * @author Reiner Jung
 * @since 1.3.0
 */
public class CreateCommonBlockDataflowTableStage
        extends AbstractTransformation<CommonBlockArgumentDataflow, Table<String, CommonBlockArgumentDataflow>> {

    private final Table<String, CommonBlockArgumentDataflow> callsTable;

    public CreateCommonBlockDataflowTableStage() {
        this.callsTable = new Table<>("dataflow-cb");
    }

    @Override
    protected void execute(final CommonBlockArgumentDataflow commonBlockDataflow) throws Exception {
        this.callsTable.getRows().add(commonBlockDataflow);
    }

    @Override
    protected void onTerminating() {
        this.outputPort.send(this.callsTable);
        super.onTerminating();
    }

}
