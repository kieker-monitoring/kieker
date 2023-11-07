package org.oceandsl.tools.restructuring.mapper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.Random;
import java.util.Set;

import kieker.model.analysismodel.assembly.AssemblyComponent;
import kieker.model.analysismodel.assembly.AssemblyModel;

/**
 *
 * @author Serafim Simonov
 * @since 1.3.0
 */
public class RandomMapper extends BasicComponentMapper {

    /**
     * Constructor is used to create a random mapper.
     *
     * @param original
     *            original model
     * @param goal
     *            goal model
     * @param originalModelName
     *            name of the original model
     * @param goalModelName
     *            name of the goal model
     */
    public RandomMapper(final AssemblyModel original, final AssemblyModel goal, final String originalModelName,
            final String goalModelName) {
        super(originalModelName, goalModelName);
        this.original = original;
        this.goal = goal;

        // init mappings
        this.populateOperationTocomponentG();
        this.populateOperationToComponentO();
        this.populateTraceModel();
        this.computeOriginalComponentNames();
    }

    private void computeOriginalComponentNames() {
        final List<String> randComps = this.getRandomComponents(new ArrayList<>(this.goal.getComponents().keySet()));

        int currentRand = 0;
        for (final String origC : this.original.getComponents().keySet()) {
            this.goalToOriginal.put(randComps.get(currentRand), origC);
            this.originalToGoal.put(origC, randComps.get(currentRand));
            currentRand++;
        }
    }

    private List<String> getRandomComponents(final List<String> componentNames) {
        final Random rand = new Random();
        final List<String> result = new ArrayList<>();
        final int numOfComponents = componentNames.size();
        for (int i = 0; i < numOfComponents; i++) {
            final int randomIndex = rand.nextInt(numOfComponents);
            result.add(componentNames.get(randomIndex));
            componentNames.remove(randomIndex);

        }
        return result;

    }

    private void populateTraceModel() {
        // For each component in the goal model...
        for (final Entry<String, AssemblyComponent> entry : this.goal.getComponents().entrySet()) {

            final AssemblyComponent comp = entry.getValue();
            final String name = entry.getKey();

            // get operations of the current component
            final Set<String> ops = comp.getOperations().keySet();
            // here we store the components where operations actually originate from and
            // count how many operations of each
            // original component are in the "goal component"
            final HashMap<String, Integer> referencedComponents = new HashMap<>();

            // For each operation of current component..
            for (final String op : ops) {

                // String opName = op.getKey();
                // look up the name of the "original component" for that operation
                final String originalComponent = this.operationToComponentO.get(op);

                // Already found or not?
                if (referencedComponents.containsKey(originalComponent)) {
                    referencedComponents.put(originalComponent, referencedComponents.get(originalComponent) + 1);

                } else {
                    // first occurence
                    referencedComponents.put(originalComponent, 1);
                }

            }
            this.traceModell.put(name, referencedComponents);

        }
    }

    private void populateOperationToComponentO() {
        for (final Entry<String, AssemblyComponent> e : this.original.getComponents().entrySet()) {
            final Set<String> ops = e.getValue().getOperations().keySet();
            for (final String s : ops) {
                this.operationToComponentO.put(s, e.getKey());
            }
        }
    }

    private void populateOperationTocomponentG() {
        for (final Entry<String, AssemblyComponent> e : this.goal.getComponents().entrySet()) {
            final Set<String> ops = e.getValue().getOperations().keySet();
            for (final String s : ops) {
                this.operationToComponentG.put(s, e.getKey());
            }
        }
    }
}
