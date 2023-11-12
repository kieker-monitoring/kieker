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
package kieker.tools.mvis.stages.metrics;

import kieker.analysis.generic.graph.IEdge;
import kieker.analysis.generic.graph.IGraph;
import kieker.analysis.generic.graph.INode;

import teetime.stage.basic.AbstractTransformation;

import org.oceandsl.analysis.generic.Table;

/**
 * Counts the incoming and outgoing edges for each node. Where nodes represent modules/components in
 * the architecture.
 *
 * @author Reiner Jung
 * @since 1.1
 */
public class ModuleNodeCountCouplingStage
        extends AbstractTransformation<IGraph<INode, IEdge>, Table<String, ModuleNodeCountCouplingEntry>> {

    @Override
    protected void execute(final IGraph<INode, IEdge> graph) throws Exception {
        final Table<String, ModuleNodeCountCouplingEntry> result = new Table<>(graph.getLabel());

        for (final INode vertex : graph.getGraph().nodes()) {
            result.getRows().add(new ModuleNodeCountCouplingEntry(this.getFilepath(vertex.getId()),
                    graph.getGraph().inDegree(vertex), graph.getGraph().outDegree(vertex)));

        }

        this.outputPort.send(result);
    }

    private String getFilepath(final Object id) {
        final String stringId = (String) id;
        String filepath = stringId.split("\\.f")[0];
        filepath = filepath.split("@")[1];
        return filepath.concat(".f");
    }
}
