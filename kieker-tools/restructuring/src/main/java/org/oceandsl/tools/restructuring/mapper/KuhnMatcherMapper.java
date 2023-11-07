package org.oceandsl.tools.restructuring.mapper;

import java.util.HashSet;
import java.util.Map.Entry;
import java.util.Set;

import org.jgrapht.Graph;
import org.jgrapht.alg.interfaces.MatchingAlgorithm.Matching;
import org.jgrapht.alg.matching.MaximumWeightBipartiteMatching;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleWeightedGraph;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import kieker.analysis.exception.InternalErrorException;
import kieker.model.analysismodel.assembly.AssemblyComponent;
import kieker.model.analysismodel.assembly.AssemblyModel;
import kieker.model.analysismodel.assembly.AssemblyOperation;

import org.oceandsl.tools.restructuring.util.RestructurerUtils;

/**
 *
 * @author Serafim Simonov
 * @since 1.3.0
 */
public class KuhnMatcherMapper extends BasicComponentMapper {

    private final static Logger LOGGER = LoggerFactory.getLogger(KuhnMatcherMapper.class);

    private final Set<String> s = new HashSet<>();
    private final Set<String> t = new HashSet<>();
    private final Matching<String, DefaultEdge> matching;

    /**
     * Constructor is used to create a kuhn mapper.
     *
     * @param original
     *            original model
     * @param goal
     *            goal model
     * @param originalModelName
     *            name of the original model
     * @param goalModelName
     *            name of the goal model
     * @throws InternalErrorException
     *             when there is a difference in functions between original and goal operations
     */
    public KuhnMatcherMapper(final AssemblyModel orig, final AssemblyModel goal, final String originalModelName,
            final String goalModelName) throws InternalErrorException {
        super(originalModelName, goalModelName);

        this.original = RestructurerUtils.cloneModel(orig);
        this.goal = RestructurerUtils.alterComponentNames(goal);
        this.s.addAll(this.original.getComponents().keySet());
        this.t.addAll(this.goal.getComponents().keySet());

        this.populateOperationTocomponentG();
        this.populateOperationToComponentO();

        if (this.operationToComponentO.size() != this.operationToComponentG.size()) {
            throw new InternalErrorException("Some operations are lost.");
        }

        final SimpleWeightedGraph<String, DefaultEdge> graph = this.createInitialGraph();

        assert graph.vertexSet().size() == (this.original.getComponents().size() + this.goal.getComponents().size());

        final MaximumWeightBipartiteMatching<String, DefaultEdge> matcher = new MaximumWeightBipartiteMatching<>(graph,
                this.s, this.t);
        this.matching = matcher.getMatching();
        this.computeOriginalComponentNames();
    }

    private SimpleWeightedGraph<String, DefaultEdge> createInitialGraph() {
        // create graph
        final SimpleWeightedGraph<String, DefaultEdge> graph = new SimpleWeightedGraph<>(DefaultEdge.class);

        // each component is a vertex
        for (final Entry<String, AssemblyComponent> c : this.original.getComponents().entrySet()) {
            graph.addVertex(c.getKey()); // create vertex from original component

            // iterate through operations in original component
            this.processOperations(graph, c);
        }

        return graph;
    }

    private void processOperations(final SimpleWeightedGraph<String, DefaultEdge> graph,
            final Entry<String, AssemblyComponent> componentEntry) {
        final String origVertex = componentEntry.getKey();
        for (final Entry<String, AssemblyOperation> ops : componentEntry.getValue().getOperations().entrySet()) {

            final String goalVertex = this.operationToComponentG.get(ops.getKey());
            if (!graph.containsVertex(goalVertex)) { // vertex was not added yet , thus
                                                     // simply create and edge
                graph.addVertex(goalVertex); // component on goal as vertex
                final DefaultEdge edge = graph.addEdge(origVertex, goalVertex);// add edge
                graph.setEdgeWeight(edge, 1); // weight is 1 first encounter
            } else { // component already in the graph
                     // check if there is already a connection between current orig and goal
                     // components
                DefaultEdge edge = graph.getEdge(origVertex, goalVertex);

                if (edge != null) { // edge exists. Simply adjust the weights
                    final double currentWeight = graph.getEdgeWeight(edge);
                    graph.setEdgeWeight(edge, currentWeight + 1);
                } else {
                    // no edges. Create a simple edge
                    if (origVertex.equals(goalVertex)) {
                        KuhnMatcherMapper.LOGGER.debug("{} {}", origVertex, goalVertex);
                    }
                    edge = graph.addEdge(origVertex, goalVertex); // add edge
                    graph.setEdgeWeight(edge, 1); // weight is 1 first encounter
                }
            }
        }
    }

    private void computeOriginalComponentNames() {
        final Graph<String, DefaultEdge> graph = this.matching.getGraph();
        for (final DefaultEdge e : this.matching.getEdges()) {
            if (graph.getEdgeWeight(e) > 0) {
                final String source = graph.getEdgeSource(e);
                final String target = graph.getEdgeTarget(e);
                // String trueTarget = target.substring(0, target.length()-3);
                this.goalToOriginal.put(target, source);
                this.originalToGoal.put(source, target);

            }
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
