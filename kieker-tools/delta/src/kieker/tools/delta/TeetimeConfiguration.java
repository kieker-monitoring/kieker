/***************************************************************************
 * Copyright (C) 2023 Kieker Project (https://kieker-monitoring.net)
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
package kieker.tools.delta;

import java.io.IOException;
import java.nio.file.Path;

import kieker.analysis.generic.data.MoveOperationEntry;
import kieker.analysis.generic.sink.TableCsvSink;
import kieker.analysis.generic.sink.YamlSink;
import kieker.tools.delta.stages.CompileRestructureTableStage;
import kieker.tools.delta.stages.CompileRestructureYamlStage;
import kieker.tools.delta.stages.ResturctureModelReader;
import kieker.tools.delta.stages.data.YamlRestructureModel;
import kieker.tools.restructuring.restructuremodel.TransformationModel;

import teetime.framework.Configuration;
import teetime.stage.basic.distributor.Distributor;
import teetime.stage.basic.distributor.strategy.CopyByReferenceStrategy;

/**
 * Pipe and Filter configuration for the architecture creation tool.
 *
 * @author Serafim Simonov
 * @since 2.0.0
 */
public class TeetimeConfiguration extends Configuration {

	public TeetimeConfiguration(final Settings settings) throws IOException {

		final ResturctureModelReader reader = new ResturctureModelReader(settings.getInputPath());

		final Distributor<TransformationModel> distributor = new Distributor<>(new CopyByReferenceStrategy());

		final String name = settings.getOutputPath().getFileName().toString();

		final CompileRestructureTableStage csvProcessor = new CompileRestructureTableStage(name);

		final CompileRestructureYamlStage yamlProcessor = new CompileRestructureYamlStage();

		final TableCsvSink<String, MoveOperationEntry> csvSink = new TableCsvSink<>(
				settings.getOutputPath().getParent(), MoveOperationEntry.class, true, settings.getLineSeparator());

		final Path outputPath = settings.getOutputPath().getParent().resolve(name + ".yaml");
		final YamlSink<YamlRestructureModel> yamlSink = new YamlSink<>(outputPath);

		this.connectPorts(reader.getOutputPort(), distributor.getInputPort());

		this.connectPorts(distributor.getNewOutputPort(), csvProcessor.getInputPort());
		this.connectPorts(csvProcessor.getOutputPort(), csvSink.getInputPort());

		this.connectPorts(distributor.getNewOutputPort(), yamlProcessor.getInputPort());
		this.connectPorts(yamlProcessor.getOutputPort(), yamlSink.getInputPort());
	}
}
