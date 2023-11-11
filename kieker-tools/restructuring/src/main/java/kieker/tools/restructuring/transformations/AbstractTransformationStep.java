package kieker.tools.restructuring.transformations;

import kieker.model.analysismodel.assembly.AssemblyModel;
import kieker.model.analysismodel.assembly.impl.AssemblyFactoryImpl;

/**
 *
 * @author Serafim Simonov
 * @since 1.3.0
 */
public abstract class AbstractTransformationStep {

    protected final static AssemblyFactoryImpl FACTORY = new AssemblyFactoryImpl();

    protected AssemblyModel model;

    public AbstractTransformationStep(final AssemblyModel model) {
        this.model = model;
    }

    public AssemblyModel getModel() {
        return this.model;
    }

    public abstract void applyTransformation(AssemblyModel model);
}
