package kieker.tools.restructuring.transformations;

import kieker.model.analysismodel.assembly.AssemblyModel;

/**
 *
 * @author Serafim Simonov
 * @since 1.3.0
 */
public class MoveTransformation extends CompositeTransformation {

    public MoveTransformation(final AssemblyModel model) {
        super(model);
    }

    @Override
    public void applyTransformation(final AssemblyModel model) {

        // TODO CHECK IF THE LIST FORMAT IS APPROPRIETE
        // 1ST OPERATION Split other operation move
        for (final AbstractTransformationStep t : this.steps) {
            t.applyTransformation(model);
        }
        this.model = model;
    }

    public void add(final AbstractTransformationStep transformation) {
        this.steps.add(transformation);
    }

    public CutTransformation getCutTransformation() {
        return (CutTransformation) this.steps.get(0);
    }

    public PasteTransformation getPasteTransformation() {
        return (PasteTransformation) this.steps.get(1);
    }
}
