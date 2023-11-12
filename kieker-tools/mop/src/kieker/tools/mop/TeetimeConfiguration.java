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
package kieker.tools.mop;

import java.io.IOException;

import teetime.framework.Configuration;

import org.oceandsl.analysis.architecture.stages.ModelRepositoryReaderStage;
import org.oceandsl.analysis.architecture.stages.ModelSink;
import org.oceandsl.analysis.architecture.stages.ModelSource;

import kieker.tools.mop.stages.IModelOperationStage;
import kieker.tools.mop.stages.ModelMergeStage;
import kieker.tools.mop.stages.ModelSelectStage;
import kieker.tools.mop.stages.NearestModelMergeStage;

/**
 * Pipe and Filter configuration for the architecture creation tool.
 *
 * @author Reiner Jung
 * @since 1.0
 */
public class TeetimeConfiguration extends Configuration {

    public TeetimeConfiguration(final Settings settings) throws IOException {

        final ModelSource modelSource = new ModelSource(settings.getInputModelPaths());
        final ModelRepositoryReaderStage modelReader = new ModelRepositoryReaderStage();
        final IModelOperationStage modelOperationStage;

        switch (settings.getOperation()) {
        case FUNCTION_MERGE:
            modelOperationStage = new ModelMergeStage(settings.getExperimentName());
            break;
        case NEAREST_MERGE:
            modelOperationStage = new NearestModelMergeStage(settings.getExperimentName(),
                    settings.getOutputDirectory(), settings.getThreshold());
            break;
        case MERGE:
            modelOperationStage = new ModelMergeStage(settings.getExperimentName());
            break;
        case SELECT:
            modelOperationStage = new ModelSelectStage(settings.getSelectionCriteriaPatterns());
            break;
        default:
            modelOperationStage = new ModelMergeStage(settings.getExperimentName());
            break;
        }

        final ModelSink modelSink = new ModelSink(settings.getOutputDirectory());

        this.connectPorts(modelSource.getOutputPort(), modelReader.getInputPort());
        this.connectPorts(modelReader.getOutputPort(), modelOperationStage.getInputPort());
        this.connectPorts(modelOperationStage.getOutputPort(), modelSink.getInputPort());
    }
}
