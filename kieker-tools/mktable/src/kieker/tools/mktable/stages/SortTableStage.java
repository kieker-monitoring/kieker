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
package kieker.tools.mktable.stages;

import java.util.Collections;
import java.util.Comparator;

import teetime.stage.basic.AbstractFilter;

import org.oceandsl.analysis.generic.Table;
import org.oceandsl.analysis.generic.data.MoveOperationEntry;

/**
 *
 * @author Reiner Jung
 * @since 2.0.0
 */
public class SortTableStage extends AbstractFilter<Table<String, MoveOperationEntry>> {

    @Override
    protected void execute(final Table<String, MoveOperationEntry> optimization) throws Exception {
        Collections.sort(optimization.getRows(), new Comparator<MoveOperationEntry>() {
            @Override
            public int compare(final MoveOperationEntry arg0, final MoveOperationEntry arg1) {
                return arg0.getTargetComponentName().compareTo(arg1.getTargetComponentName());
            }
        });
        this.outputPort.send(optimization);
    }
}
