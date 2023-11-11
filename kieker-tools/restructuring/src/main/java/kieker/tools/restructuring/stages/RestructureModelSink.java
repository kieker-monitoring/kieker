package kieker.tools.restructuring.stages;

import java.nio.file.Path;

import teetime.framework.AbstractConsumerStage;

import org.oceandsl.tools.restructuring.restructuremodel.TransformationModel;

import kieker.tools.restructuring.util.WriteModelUtils;

/**
 *
 * @author Serafim Simonov
 * @since 1.3.0
 */
public class RestructureModelSink extends AbstractConsumerStage<TransformationModel> {
    private final Path outputPath;

    public RestructureModelSink(final Path outputPath) {
        this.outputPath = outputPath;
    }

    @Override
    protected void execute(final TransformationModel element) throws Exception {
        WriteModelUtils.writeModelRepository(this.outputPath, element.getName(), element);
    }

}
