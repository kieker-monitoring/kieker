package org.oceandsl.tools.restructuring.stages.exec;

import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import org.eclipse.emf.common.util.EMap;

import kieker.model.analysismodel.assembly.AssemblyComponent;
import kieker.model.analysismodel.assembly.AssemblyModel;
import kieker.model.analysismodel.assembly.AssemblyOperation;

import org.oceandsl.tools.restructuring.mapper.BasicComponentMapper;
import org.oceandsl.tools.restructuring.transformations.AbstractTransformationStep;
import org.oceandsl.tools.restructuring.transformations.CreateTransformation;
import org.oceandsl.tools.restructuring.transformations.CutTransformation;
import org.oceandsl.tools.restructuring.transformations.DeleteTransformation;
import org.oceandsl.tools.restructuring.transformations.MergeTransformation;
import org.oceandsl.tools.restructuring.transformations.MoveTransformation;
import org.oceandsl.tools.restructuring.transformations.PasteTransformation;
import org.oceandsl.tools.restructuring.transformations.SplitTransformation;
import org.oceandsl.tools.restructuring.util.TransformationFactory;

/**
 *
 * @author Serafim Simonov
 * @since 1.3.0
 */
public class RestructureStepFinder {

    private final BasicComponentMapper componentMapper;
    private final AssemblyModel original;
    private final AssemblyModel goal;
    private int numberOfSteps = 0; // NOPMD RedundantFieldInitializer, here for documentation
                                   // purposes

    private final List<AbstractTransformationStep> transformations = new ArrayList<>();

    public RestructureStepFinder(final BasicComponentMapper componentMapper) {
        this.componentMapper = componentMapper;
        this.original = componentMapper.getOriginal();
        this.goal = componentMapper.getGoal();
    }

    public BasicComponentMapper getComponentMapper() {
        return this.componentMapper;
    }

    public List<AbstractTransformationStep> getSteps() {
        return this.transformations;
    }

    public AssemblyModel getOriginal() {
        return this.original;
    }

    public AssemblyModel getGoal() {
        return this.goal;
    }

    public int getNumberOfSteps() {
        return this.numberOfSteps;
    }

    public void findTransformation() {
        // goalComponents.
        // System.out.println("Num of gto in step
        // finder:"+this.compMapper.getGoalToOriginal().size());
        // System.out.println("Num of tgo in
        // stepfinder:"+this.compMapper.getOriginallToGoal().size());
        final EMap<String, AssemblyComponent> originalComponents = this.original.getComponents();
        while (!TransformationFactory.areSameModels(this.goal, this.original)) {
            for (final Entry<String, AssemblyComponent> entry : originalComponents) {

                final String originalComponentName = entry.getKey();
                final String goalComponentName = this.componentMapper.getOriginalToGoal().get(originalComponentName);

                // We could find a mapping, yeah!
                if (goalComponentName != null) {
                    // System.out.println("Get nameOfGoalC "+ nameOfGoalC);
                    final AssemblyComponent goalAssemblyComponent = this.goal.getComponents().get(goalComponentName);
                    // final EMap<String, AssemblyOperation> operationInGoalComponent =
                    // goalAssemblyComponent
                    // .getOperations();

                    // If the original and corresponding goal component equal, nothing to do
                    // if (entry.getValue().equals(goalAssemblyComponentObject)) // the mapping
                    // matches look for next
                    if (TransformationFactory.areSameComponents(entry.getValue(), goalAssemblyComponent)) {
                        continue;
                    }

                    // Operations that are in original component but not in goal component
                    final List<String> out = this.getOperationsToMove(entry.getValue(), goalAssemblyComponent);
                    // Operation that are in goal component but not in the original component
                    final List<String> in = this.getOperationToAdd(entry.getValue(), goalAssemblyComponent);
                    this.transformModel(out, in, entry.getKey());
                    break;
                } else {
                    // Mapping could not be found
                    this.nonMappedComponentTransformation(entry.getValue());
                    break;
                }
            }
        }
    }
    // }

    private void nonMappedComponentTransformation(final AssemblyComponent component) {
        for (int i = 0; i < component.getOperations().size(); i++) {
            final Entry<String, AssemblyOperation> operation = component.getOperations().get(i);

            final String operationName = operation.getKey();
            // Where should the operation lie after restructuring?
            final String goalComponent = this.componentMapper.getOperationToComponentG().get(operationName);

            // Where does it lie currently?
            // String originalComponent = operation.getValue().getComponent().getSignature();

            // MP 2023
            final String originalComponent = this.componentMapper.getOperationToComponentO().get(operation.getKey());
            // check if the goal location of the current operation is mapped to some
            // component in original?
            //
            if (!this.componentMapper.getGoalToOriginal().containsKey(goalComponent)) {
                // No mapping exist (This case is rare. Only we compMapper failed!)
                // Thus, we can safely pair the original and goal components
                // Proceed after that normally
                this.componentMapper.getGoalToOriginal().put(goalComponent,
                        this.componentMapper.getOperationToComponentO().get(operationName));
                this.componentMapper.getOriginalToGoal()
                        .put(this.componentMapper.getOperationToComponentO().get(operationName), goalComponent);

                // Collect normally which operation must be moved away and which not
                final AssemblyComponent before = this.original.getComponents()
                        .get(this.componentMapper.getOperationToComponentO().get(operationName));
                final AssemblyComponent after = this.goal.getComponents().get(goalComponent);
                final List<String> out = this.getOperationsToMove(before, after);
                final List<String> in = this.getOperationToAdd(before, after);

                // transform normally
                this.transformModel(out, in, this.componentMapper.getOperationToComponentO().get(operationName));

                // Since we sorted all operations, there is nothing else to do
                break;
            } else {
                // This case means we have a goal component which is mapped to some original
                // component.
                // We move the operation over there!
                // Where should we move?
                final String to = this.componentMapper.getGoalToOriginal().get(goalComponent);

                final CutTransformation cut = new CutTransformation(null);
                cut.setComponentName(originalComponent);
                cut.setOperationName(operationName);
                this.numberOfSteps++;
                // this.transformations.add(cut);
                // cut.applyTransformation(this.orig);

                final PasteTransformation paste = new PasteTransformation(null);
                paste.setComponentName(to);
                paste.setOperationName(operationName);
                this.numberOfSteps++;
                // this.transformations.add(paste);
                // paste.applyTransformation(this.orig);

                final MoveTransformation move = new MoveTransformation(null);
                move.getSteps().add(cut);
                move.getSteps().add(paste);
                move.applyTransformation(this.original);
                this.transformations.add(move);

                if (this.original.getComponents().get(originalComponent).getOperations().size() == 0) {
                    final DeleteTransformation delete = new DeleteTransformation(null);
                    delete.setComponentName(originalComponent);
                    delete.applyTransformation(this.original);
                    this.numberOfSteps++;
                    // this.transformations.add(delete);

                    final MergeTransformation merge = new MergeTransformation(null);
                    merge.add(move);
                    merge.add(delete);
                    this.transformations.remove(this.transformations.size() - 1);
                    this.transformations.add(delete);

                }

            }

        }
        // TODO Auto-generated method stub
    }

    /**
     *
     * @param out
     *            operations that are not contained in the goal model and must be removed from
     *            original
     * @param in
     *            operation that are contained in the goal model and must be added to original
     * @param originalComponentName
     *            name Of the original component
     */
    private void transformModel(final List<String> out, final List<String> in, final String originalComponentName) {

        // move out
        for (final String operationName : out) {
            // get name of component in goal assembly model where the operation lies
            final String goalComponentName = this.componentMapper.getOperationToComponentG().get(operationName);
            // check if goal component is mapped to some component in the original
            final boolean isMapped = this.componentMapper.getGoalToOriginal().containsKey(goalComponentName);

            if (isMapped) { // a correspondence exists
                this.moveOperation(originalComponentName, goalComponentName, operationName);
            } else { // no mapping exists. We must split!
                this.splitAndMove(originalComponentName, goalComponentName, operationName);
            }
        }

        // move in
        // a simpler case
        for (final String op : in) {
            // Where lies the operation currently that must be moved to the component?
            final String compName = this.componentMapper.getOperationToComponentO().get(op);

            final CutTransformation cut = new CutTransformation(null);
            cut.setComponentName(compName);
            cut.setOperationName(op);
            this.numberOfSteps++;
            // this.transformations.add(cut);
            // cut.applyTransformation(this.orig);

            final PasteTransformation paste = new PasteTransformation(null);
            paste.setOperationName(op);
            paste.setComponentName(originalComponentName);
            this.numberOfSteps++;
            // this.transformations.add(paste);
            // paste.applyTransformation(this.orig);

            final MoveTransformation move = new MoveTransformation(null);
            move.getSteps().add(cut);
            move.getSteps().add(paste);
            move.applyTransformation(this.original);
            this.transformations.add(move);
            // If after moving the operation its old componet becomes empty,
            // and this component is not mapped to some component, we can assume,
            // that we must merge. Thus delete it
            if ((this.original.getComponents().get(compName).getOperations().size() == 0)
                    && !this.componentMapper.getOriginalToGoal().containsKey(compName)) {
                final DeleteTransformation delete = new DeleteTransformation(null);
                delete.setComponentName(compName);
                delete.applyTransformation(this.original);
                this.numberOfSteps++;
                this.transformations.add(delete);

                final MergeTransformation merge = new MergeTransformation(null);
                merge.add(move);
                merge.add(delete);
                this.transformations.remove(this.transformations.size() - 1);
                this.transformations.add(merge);
            }

        }
    }

    /**
     * get the mapped component and move the operation over there.
     */
    private void moveOperation(final String originalComponentName, final String goalComponentName,
            final String operationName) {
        final String originalComponentFromGoal = this.componentMapper.getGoalToOriginal().get(goalComponentName);

        // Cut operation from original Component
        final CutTransformation cut = new CutTransformation(null);
        cut.setComponentName(originalComponentName);
        cut.setOperationName(operationName);
        this.numberOfSteps++;
        // this.transformations.add(cut);
        // cut.applyTransformation(this.orig);

        // Paste into another component
        final PasteTransformation paste = new PasteTransformation(null);
        paste.setComponentName(originalComponentFromGoal);
        paste.setOperationName(operationName);
        this.numberOfSteps++;
        // paste.applyTransformation(this.orig);
        // this.transformations.add(paste);

        final MoveTransformation move = new MoveTransformation(null);
        move.getSteps().add(cut);
        move.getSteps().add(paste);
        move.applyTransformation(this.original);
        this.transformations.add(move);

    }

    /**
     *
     */
    private void splitAndMove(final String originalComponentName, final String goalComponentName,
            final String operationName) {
        final CreateTransformation create = new CreateTransformation(null);
        create.setComponentName(goalComponentName); // For the new component in original
        create.applyTransformation(this.original);
        this.numberOfSteps++;
        // we use the name as it is specified in goal model

        // this.transformations.add(create);
        // create.applyTransformation(this.orig);
        this.componentMapper.getOriginalToGoal().put(goalComponentName, goalComponentName);
        this.componentMapper.getGoalToOriginal().put(goalComponentName, goalComponentName);

        // TODO update mapping

        final CutTransformation cut = new CutTransformation(null);
        cut.setComponentName(originalComponentName);
        cut.setOperationName(operationName);
        this.numberOfSteps++;
        // this.transformations.add(cut);
        // cut.applyTransformation(this.orig);

        final PasteTransformation paste = new PasteTransformation(null);
        paste.setComponentName(goalComponentName);
        paste.setOperationName(operationName);
        this.numberOfSteps++;
        // this.transformations.add(paste);
        // paste.applyTransformation(this.orig);

        final MoveTransformation move = new MoveTransformation(null);
        move.getSteps().add(cut);
        move.getSteps().add(paste);
        // move.applyTransformation(this.orig);

        final SplitTransformation split = new SplitTransformation(null);
        split.add(create);
        split.add(move);
        split.applyTransformation(this.original);
        this.transformations.add(split);
    }

    private List<String> getOperationToAdd(final AssemblyComponent orig, final AssemblyComponent goal) {

        final EMap<String, AssemblyOperation> origOps = orig.getOperations();
        final EMap<String, AssemblyOperation> goalOps = goal.getOperations();
        final List<String> result = new ArrayList<>();
        for (final Entry<String, AssemblyOperation> op : goalOps.entrySet()) {
            if (!origOps.containsKey(op.getKey())) {
                result.add(op.getKey());
            }
        }
        return result;

    }

    private List<String> getOperationsToMove(final AssemblyComponent orig, final AssemblyComponent goal) {
        // TODO Auto-generated method stub
        final EMap<String, AssemblyOperation> origOps = orig.getOperations();
        final EMap<String, AssemblyOperation> goalOps = goal.getOperations();
        final List<String> result = new ArrayList<>();
        for (final Entry<String, AssemblyOperation> op : origOps.entrySet()) {
            if (!goalOps.containsKey(op.getKey())) {
                result.add(op.getKey());
            }
        }
        return result;
    }
}
