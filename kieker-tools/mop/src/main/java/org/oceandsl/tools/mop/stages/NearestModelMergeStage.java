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
package org.oceandsl.tools.mop.stages;

import java.nio.file.Path;

import kieker.analysis.architecture.repository.ModelRepository;

import teetime.framework.CompositeStage;
import teetime.framework.InputPort;
import teetime.framework.OutputPort;

import org.oceandsl.analysis.generic.stages.TableCsvSink;

public class NearestModelMergeStage extends CompositeStage implements IModelOperationStage {

    private final ModelMergeStage modelMergeStage;
    private final MergeClosestFitComponentStage mergeClosestFitComponentStage;

    public NearestModelMergeStage(final String name, final Path path, final double threshold) {
        this.mergeClosestFitComponentStage = new MergeClosestFitComponentStage(threshold);
        this.modelMergeStage = new ModelMergeStage(name);
        final TableCsvSink<String, SimilarityEntry> similartyCsvSink = new TableCsvSink<>(
                n -> path.resolve("similarity-" + n), SimilarityEntry.class, true, TableCsvSink.LF);
        this.connectPorts(this.mergeClosestFitComponentStage.getOutputPort(), this.modelMergeStage.getInputPort());
        this.connectPorts(this.mergeClosestFitComponentStage.getSimilarityOutputPort(),
                similartyCsvSink.getInputPort());
    }

    @Override
    public InputPort<ModelRepository> getInputPort() {
        return this.mergeClosestFitComponentStage.getInputPort();
    }

    @Override
    public OutputPort<ModelRepository> getOutputPort() {
        return this.modelMergeStage.getOutputPort();
    }
}
