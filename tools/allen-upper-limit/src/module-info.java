/**
 * This module contains an architecture analysis utility that computes
 * the “Allen upper limit” over a model or a generated test graph. In practice,
 * it evaluates upper bounds for temporal/ordering relations (based on Allen’s
 * interval reasoning) or equivalent relation constraints within an architecture/network model.
 * This helps bound the maximum number of feasible relations/edges and assess structural or
 * timing constraints inferred from execution data.
 */
module kieker.tools.aul {

    //Kieker Dependencies
    requires kieker.analysis;
    requires kieker.common;
    requires kieker.model;
    requires kieker.tools;

    //Third-Party-Dependencies
    requires com.google.common;
    requires org.eclipse.emf.common;
    requires org.eclipse.emf.ecore;
    requires org.slf4j;
}