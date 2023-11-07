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
package org.oceandsl.tools.restructuring;

import java.io.IOException;

import teetime.framework.Configuration;

import org.oceandsl.analysis.architecture.stages.ModelRepositoryReaderStage;
import org.oceandsl.analysis.architecture.stages.ModelSource;
import org.oceandsl.analysis.generic.stages.TableCsvSink;
import org.oceandsl.tools.restructuring.stages.AggregateModelEditDistanceStage;
import org.oceandsl.tools.restructuring.stages.GenerateRestructureModelStage;
import org.oceandsl.tools.restructuring.stages.ModelEditDistanceEntry;
import org.oceandsl.tools.restructuring.stages.RestructureModelSink;
import org.oceandsl.tools.restructuring.stages.RestructurerStage;
import org.oceandsl.tools.restructuring.stages.TraceRestoratorStage;

/**
 * Pipe and Filter configuration for the architecture creation tool.
 *
 * @author Serafim Simonov
 * @since 1.3.0
 */
public class TeetimeConfiguration extends Configuration {

    public TeetimeConfiguration(final Settings settings) throws IOException {

        final ModelSource modelSource = new ModelSource(settings.getInputModelPaths());
        final ModelRepositoryReaderStage modelReader = new ModelRepositoryReaderStage();
        final TraceRestoratorStage traceRestorator = new TraceRestoratorStage(settings.getMappingStrat());
        final RestructurerStage restructurer = new RestructurerStage();
        final GenerateRestructureModelStage generateModelStage = new GenerateRestructureModelStage();
        final RestructureModelSink modelSink = new RestructureModelSink(settings.getOutputDirectory());
        final AggregateModelEditDistanceStage aggregateStage = new AggregateModelEditDistanceStage();
        final TableCsvSink<String, ModelEditDistanceEntry> medSinkStage = new TableCsvSink<>(
                settings.getOutputDirectory(), ModelEditDistanceEntry.class, true, settings.getLineSeparator());

        this.connectPorts(modelSource.getOutputPort(), modelReader.getInputPort());

        this.connectPorts(modelReader.getOutputPort(), traceRestorator.getInputPort());

        this.connectPorts(traceRestorator.getOutputPort(), restructurer.getInputPort());

        this.connectPorts(restructurer.getStepsOutputPort(), generateModelStage.getInputPort());
        this.connectPorts(generateModelStage.getOutputPort(), modelSink.getInputPort());

        this.connectPorts(restructurer.getNumberOfStepsOutputPort(), aggregateStage.getInputPort());
        this.connectPorts(aggregateStage.getOutputPort(), medSinkStage.getInputPort());
    }
}
