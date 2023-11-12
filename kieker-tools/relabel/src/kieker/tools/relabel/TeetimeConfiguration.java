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
package kieker.tools.relabel;

import teetime.framework.Configuration;

import org.oceandsl.analysis.architecture.stages.ModelRepositoryProducerStage;

/**
 * Pipe and Filter configuration relabling tool.
 *
 * @author Reiner Jung
 * @since 1.1
 */
public class TeetimeConfiguration extends Configuration {

    public TeetimeConfiguration(final Settings settings) {
        final ModelRepositoryProducerStage readerStage = new ModelRepositoryProducerStage(settings.getInputDirectory());
        final ReplaceSourceLabelStage replaceSourceLabelStage = new ReplaceSourceLabelStage(
                settings.getExperimentName(), settings.getReplacements());
        final ModelRepositoryWriterStage writerStage = new ModelRepositoryWriterStage(settings.getOutputDirectory());

        this.connectPorts(readerStage.getOutputPort(), replaceSourceLabelStage.getInputPort());
        this.connectPorts(replaceSourceLabelStage.getOutputPort(), writerStage.getInputPort());
    }
}
