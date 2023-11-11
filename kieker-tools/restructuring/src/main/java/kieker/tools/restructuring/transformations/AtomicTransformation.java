package kieker.tools.restructuring.transformations;

import kieker.model.analysismodel.assembly.AssemblyModel;

/**
 *
 * @author Serafim Simonov
 * @since 1.3.0
 */
public abstract class AtomicTransformation extends AbstractTransformationStep {

    public AtomicTransformation(final AssemblyModel model) {
        super(model);
    }

}
