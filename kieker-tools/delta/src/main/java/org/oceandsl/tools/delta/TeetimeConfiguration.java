package org.oceandsl.tools.delta;

import java.io.IOException;
import java.nio.file.Path;

import teetime.framework.Configuration;
import teetime.stage.basic.distributor.Distributor;
import teetime.stage.basic.distributor.strategy.CopyByReferenceStrategy;

import org.oceandsl.analysis.generic.data.MoveOperationEntry;
import org.oceandsl.analysis.generic.stages.TableCsvSink;
import org.oceandsl.analysis.generic.stages.YamlSink;
import org.oceandsl.tools.delta.stages.CompileRestructureTableStage;
import org.oceandsl.tools.delta.stages.CompileRestructureYamlStage;
import org.oceandsl.tools.delta.stages.ResturctureModelReader;
import org.oceandsl.tools.delta.stages.data.YamlRestructureModel;
import org.oceandsl.tools.restructuring.restructuremodel.TransformationModel;

/**
 * Pipe and Filter configuration for the architecture creation tool.
 *
 * @author Serafim Simonov
 * @since 1.3.0
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
