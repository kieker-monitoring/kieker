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
package org.oceandsl.tools.mt.stages;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.text.similarity.LevenshteinDistance;

import kieker.analysis.generic.clustering.mtree.IDistanceFunction;
import kieker.analysis.generic.clustering.optics.OpticsData;
import kieker.analysis.generic.graph.clustering.OPTICSDataGED;

import teetime.stage.basic.AbstractTransformation;

import org.oceandsl.analysis.generic.Table;
import org.oceandsl.analysis.generic.data.MoveOperationEntry;

/**
 * Convert table into optics input data.
 *
 * @author Reiner Jung
 * @since 1.4.0
 */
public class ConvertTableToOpticsDataStage
        extends AbstractTransformation<Table<String, MoveOperationEntry>, List<OpticsData<MoveOperationEntry>>> {

    @Override
    protected void execute(final Table<String, MoveOperationEntry> table) throws Exception {
        final IDistanceFunction<MoveOperationEntry> distanceFunction = new IDistanceFunction<>() {

            final LevenshteinDistance distance = new LevenshteinDistance();

            @Override
            public double calculate(final MoveOperationEntry data1, final MoveOperationEntry data2) {
                final String left = data1.getOperationName();
                final String right = data2.getOperationName();
                return (double) this.distance.apply(left, right) / (double) (left.length() + right.length());
            }
        };

        final OPTICSDataGED<MoveOperationEntry> opticsDistance = new OPTICSDataGED<>(distanceFunction);

        final List<OpticsData<MoveOperationEntry>> results = new ArrayList<>();
        table.getRows().forEach(entry -> results.add(new OpticsData<>(entry, opticsDistance)));
        this.outputPort.send(results);
    }

}
